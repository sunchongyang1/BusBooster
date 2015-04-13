/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.ArrivalTime;
import entity.Bus;
import entity.BusData;
import entity.BusRoute;
import entity.BusStop;
import entity.DepartureTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author chongyangsun
 */
@Stateless
public class PredictionManagementSessionBean implements PredictionManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    private final double alpha = 0.5;

    @Override
    public void updateBusInfo(List<BusData> busDataList) {
        // extract all new bus data received from user
        // the data passed in is the user from the same bus
        // analyze the bus data and upadate the corresponding bus data and bus delay
        // bus delay only been updated when bus arrives at a bus stop
        // to check whether the bus is at certain bus stop, use the calculate distance method to decide the distance between
        // two points. if less than 10, then we consider these two are at the same location (to avoid in accuracy in gps data)
        // 
        if (busDataList.isEmpty()) {
            return;
        }
        // Current time
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());

        // get the bus
        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.id=:id");
        q.setParameter("id", busDataList.get(0).getBusId()); // 100% can find bus
        List<Bus> busList = new ArrayList(q.getResultList());
        Bus bus = busList.get(0);

        // calculate the average speed and location to increase accuracy
        Double temp0 = 0.0; //speed
        Double temp1 = 0.0; //latitude
        Double temp2 = 0.0; //longitude

        for (BusData b : busDataList) {
            temp0 += b.getSpeed();
            temp1 += b.getLatitude();
            temp2 += b.getLongitude();
        }
        Double currentSpeed = temp0 / busDataList.size();
        Double avgLat = temp1 / busDataList.size();
        Double avgLon = temp2 / busDataList.size();

        // *** if bus approach bus stops
        BusStop busStop = bus.getNextStop();
        Double distanceToStop = this.calculateDistance(avgLat, avgLon, busStop.getLatitude(), busStop.getLongitude());
        if (bus.getSpeed() != 0.0 && currentSpeed != 0.0 && bus.getDistanceToNextStop() < 0) {
            // bus stops for less than 10 seconds and has passed the bus stop
            // get the distance passed the stop
            Double distancePassed = 0 - bus.getDistanceToNextStop();

            bus.setPreviousStop(busStop);
            Query q2 = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:busno AND b.direction=:dir");
            q2.setParameter("busno", bus.getBusNo());
            q2.setParameter("dir", bus.getDirection());
            BusRoute busRoute = (BusRoute) q2.getSingleResult();
            int stopIndex = busRoute.getBusStopList().indexOf(bus.getNextStop());
            bus.setNextStop(busRoute.getBusStopList().get(stopIndex + 1));
            bus.setDistanceFromPreviousStop(distancePassed);
            bus.setDistanceToNextStop(bus.getPreviousStop().getDistanceToNextStop() - distancePassed);
            bus.setTimeLeftLastStop(now);
            em.merge(bus);
            // to code - update arrival time and departure time for this bus stop
            ArrivalTime arrivalTime = new ArrivalTime(busStop, bus.getId(), busStop.getArrivalSequence(), now);
            busStop.setArrivalSequence(busStop.getArrivalSequence() + 1);
            busStop.getArrivalTimeList().add(arrivalTime);
            DepartureTime departureTime = new DepartureTime(busStop, bus.getId(), busStop.getDepartureSequence(), now);
            busStop.setDepartureSequence(busStop.getDepartureSequence() + 1);
            busStop.getDepartureTimeList().add(departureTime);
            em.merge(busStop);
            em.persist(arrivalTime);
            em.persist(departureTime);

        } else if (bus.getSpeed() != 0.0 && currentSpeed == 0.0 && distanceToStop < 10) {
            // bus arrives right at the bus stop
            bus.setPreviousStop(busStop);
            Query q2 = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:busno AND b.direction=:dir");
            q2.setParameter("busno", bus.getBusNo());
            q2.setParameter("dir", bus.getDirection());
            BusRoute busRoute = (BusRoute) q2.getSingleResult();
            int stopIndex = busRoute.getBusStopList().indexOf(bus.getNextStop());
            bus.setNextStop(busRoute.getBusStopList().get(stopIndex + 1));
            bus.setDistanceFromPreviousStop(0.0);
            bus.setDistanceToNextStop(bus.getPreviousStop().getDistanceToNextStop());
            bus.setTimeLeftLastStop(now);
            bus.setLatitude(busStop.getLatitude());
            bus.setLongitude(busStop.getLongitude());
            em.merge(bus);
            // need to process user dropoff the bus???
            //  no, user select alight, then automatically stop sending BusData

            // to code - update arrival time and departure time for this bus stop
//            int sequence = nextStop.getSequence();
            ArrivalTime arrivalTime = new ArrivalTime(busStop, bus.getId(), busStop.getArrivalSequence(), now);
            busStop.setArrivalSequence(busStop.getArrivalSequence() + 1);
            busStop.getArrivalTimeList().add(arrivalTime);
            em.merge(busStop);
            em.persist(arrivalTime);

        } else if (bus.getSpeed() == 0.0 && currentSpeed != 0.0) {
            //bus is leaving the station
            // select largest sequence number first
            DepartureTime departureTime = new DepartureTime(busStop, bus.getId(), busStop.getDepartureSequence(), now);
            busStop.setDepartureSequence(busStop.getDepartureSequence() + 1);
            busStop.getDepartureTimeList().add(departureTime);
            em.merge(busStop);
            em.persist(departureTime);
        } else {
        // bus in transit

            // time interval is 10 seconds, get the distance travelled during such period
            // assume the bus accelarates constantly
//        Double distanceTraveled = 10 * (currentSpeed + speed) / 2;
            Double distanceTraveled = this.calculateDistance(avgLat, avgLon, bus.getLatitude(), bus.getLongitude());

            //find the bus with the same bus no arrived at next bus stop within 5 min then plus the delay of it
//        Double delay = 0.0;
//        Query query = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:no AND b.previousStop=:stop");
//        query.setParameter("no", bus.getBusNo());
//        query.setParameter("stop", bus.getNextStop());
//        List<Bus> preBusList = new ArrayList(query.getResultList());
//        if (preBusList.isEmpty()) {
//            delay = 0.0;
//        } else {
//            delay = preBusList.get(0).getDelay();
//        }
//        Double arrivalTime = (bus.getDistanceToNextStop() - distanceTraveled) / currentSpeed + delay;
//        Timestamp currentScheduledArrivalTime = new Timestamp(now.getTime() + (int) (arrivalTime * 1000));
//        Double arrivalTime = (bus.getDistanceToNextStop() - distanceTraveled) / currentSpeed;
            // update bus information
            bus.setDistanceToNextStop(bus.getDistanceToNextStop() - distanceTraveled);
            bus.setDistanceFromPreviousStop(bus.getDistanceFromPreviousStop() + distanceTraveled);
            bus.setSpeed(currentSpeed);
            bus.setLatitude(avgLat);
            bus.setLongitude(avgLon);
            bus.setLastUpdateTime(now);
//        if(currentScheduledArrivalTime.after(bus.getExpectedArrivalTime())) {
//            bus.setExpectedArrivalTime(currentScheduledArrivalTime);
//        }
            em.merge(bus);

//        // update bus delay
//        if (currentScheduledArrivingTime.after(bus.getScheduledArrivingTime())) {
//            bus.setDelay((double) currentScheduledArrivingTime.getTime() - bus.getScheduledArrivingTime().getTime());
//            bus.setScheduledArrivingTime(currentScheduledArrivingTime);
//        } else {
//            bus.setDelay(0.0);
//            bus.setScheduledArrivingTime(currentScheduledArrivingTime);
//        }
        }
    }

//    @Override
//    public List<Double> getArrivalTime(String busNo, BusStop busStop) {
//        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:busno AND b.nextStopName=:stopname");
//        q.setParameter("busno", busNo);
//        q.setParameter("stopname", busStop.getBusStopName());
//        List<Bus> busList = new ArrayList(q.getResultList());
//        if (busList.isEmpty()) { // no bus inbetween current route or no bus at all
//            Query query = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:busno");
//            query.setParameter("busno", busNo);
//
//            System.out.println("no such bus exists! please check!");
//            return null;
//        }
//        List<Double> arrivalTime = new ArrayList();
//        for (Bus b : busList) {
//            // arrival time plus the delay of the previous bus's arrival time
//            Bus temp = findPreviousBus(b, busStop.getBusStopName());
//            if (temp != null) {
////                arrivalTime.add(b.getArrivalTime() + temp.getDelay()); // delay algorithm to be upgraded in the future
//            } else {
////                arrivalTime.add(b.getArrivalTime());
//            }
//        }
//
//        return arrivalTime;
//    }

    @Override
    public List<BusStop> getNearbyBusStop(Double lat, Double lon) {
        // sort the nearby bus stop within 1 km and return a list
        Query q = em.createQuery("SELECT b FROM BusStop b WHERE (b.latitude BETWEEN :l1 AND :l2) AND (b.longitude BETWEEN :l3 AND :l4)");
        q.setParameter("l1", lat - 0.01);
        q.setParameter("l2", lat + 0.01);
        q.setParameter("l3", lon - 0.01);
        q.setParameter("l4", lon + 0.01);
        List<BusStop> busStopList = new ArrayList(q.getResultList());
        if (busStopList.isEmpty()) {
            System.out.println("No Bus Stop Nearby!");
            return null;
        } else {
            // sort the bus stop according to geo distance to the current location and return the result list from nearest to farthest
            List<BusStop> result = new ArrayList();
            List<BusStop> temp = new ArrayList();
            for (BusStop b : busStopList) {
                if (result.isEmpty()) {
                    result.add(b);
                    temp = result;
                } else {
                    for (BusStop s : temp) {
                        if (this.calculateDistance(lat, lon, b.getLatitude(), b.getLongitude())
                                < this.calculateDistance(lat, lon, s.getLatitude(), s.getLongitude())) {
                            result.add(result.indexOf(s), b);
                            break;
                        }
                    }
                    if (temp.size() != result.size()) {
                        temp = result;
                    } else {
                        result.add(b);
                        temp = result;
                    }
                }
            }
            return result;
        }
    }

    @Override
    public List<BusRoute> getBusRouteByBusStop(String busStopNumber) {
        Query q = em.createQuery("SELECT b FROM BusRoute b");
        List<BusRoute> busRouteList = new ArrayList(q.getResultList());
        List<BusRoute> result = new ArrayList();
        List<BusStop> temp = new ArrayList();
        for (BusRoute b : busRouteList) {
            temp = b.getBusStopList();
            for (BusStop t : temp) {
                if (t.getBusStopNo().equals(busStopNumber)) {
                    result.add(b);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public Double getArrivalTime(String busStopNumber, String busNo) {
        // first check whether there are buses available
        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:no");
        q.setParameter("no", busNo);
        List<Bus> busList = new ArrayList(q.getResultList());
        if (busList.isEmpty()) {
            // no bus prediction available!
            System.out.println("There is currently no bus available for this route!");
            return -1.0; // no prediction returns -1 or null
        } else {
            // select the bus route in order to get the bus stop list
            Query query = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:busno");
            query.setParameter("busno", busNo);
            BusRoute busRoute = (BusRoute) query.getSingleResult();
            List<BusStop> busStopList = busRoute.getBusStopList();
            // create new list contains all bus stop before current bus stop(included) !!!inverse sequence!!!
            // temp list contains bus stop in the sequence of the distance from current bus stop from closest to farthest
            List<BusStop> temp = new ArrayList();
            for (BusStop m : busStopList) {
                if (m.getBusStopNo().equals(busStopNumber)) {
                    temp.add(0, m);
                    break;
                } else {
                    temp.add(0, m);
                }
            }
            Bus bus = new Bus();
            List<Bus> busListTemp = new ArrayList();
            // select the nearest bus available for prediction
            for (BusStop a : temp) {
                for (Bus b : busList) {
                    if (b.getNextStop().getBusStopNo().equals(a.getBusStopNo())) {
                        busListTemp.add(b);
                    }
                }
                if (!busListTemp.isEmpty()) {
                    break;
                }
            }
            // busListTemp contains a list of buses that approaches the nearest stop near current stop
            for (Bus c : busListTemp) {
                if (bus.getDistanceToNextStop() == null) {
                    bus = c;
                } else if (bus.getDistanceToNextStop() > c.getDistanceToNextStop()) {
                    bus = c;
                }
            }
            // now bus is the nearest bus that to be predicted the arrival time
            // arrivalTime1 is using ESM to calculate the time of next stop of the bus to the current stop
            double arrivalTime1 = 0.0;
            // arrivalTime2 is using ESDAM to calculate the time to arrive at the next stop of the bus 
            double arrivalTime2 = 0.0;

            /**************** 
             * to calculate arrivalTIme1
             */
            List<BusStop> predictionStopList = new ArrayList();
            for (BusStop l : temp) {
                if (l.getBusStopNo().equals(bus.getNextStop().getBusStopNo())) {
                    predictionStopList.add(0, l);
                    break;
                } else {
                    predictionStopList.add(0, l);
                }
            }
            // now predictionStopList contains all stop between bus and current stop
            List<Double> dwellList = new ArrayList();
            List<Double> dwellListEach = new ArrayList();
            List<Double> travelList = new ArrayList();
            List<Double> travelListEach = new ArrayList();
            BusStop previousBusStop = new BusStop();
            for (BusStop d : predictionStopList) {
                for (int i = d.getArrivalSequence(); i >= 0; i--) {
                    dwellListEach.add((d.getDepartureTimeList().get(i).getDepartureTime().getTime()
                            - d.getArrivalTimeList().get(i).getArrivalTime().getTime()) / 1000.0);
                }
                // calculate expected dwell time
                double dwell = 0.0;
                for (int j = 0; j < dwellListEach.size(); j++) {
                    dwell += alpha * Math.pow(1 - alpha, j) * dwellListEach.get(j);
                }
                dwellList.add(dwell);
                if (previousBusStop.getId() != null) {
                    // calculate the travel time in between stops
                    for (int i = d.getArrivalSequence(); i >= 0; i--) {
                        travelListEach.add((d.getArrivalTimeList().get(i).getArrivalTime().getTime()
                                - previousBusStop.getDepartureTimeList().get(i).getDepartureTime().getTime()) / 1000.0);
                    }
                    // calculate expected dwell time
                    double travel = 0.0;
                    for (int j = 0; j < travelListEach.size(); j++) {
                        travel += alpha * Math.pow(1 - alpha, j) * travelListEach.get(j);
                    }
                    travelList.add(travel);
                }
                // before go to the next bus stop, set the current bus stop as previousBusStop
                previousBusStop = d;
            }
            for(int i=0; i<travelList.size(); i++) {
                arrivalTime1 += dwellList.get(i);
                arrivalTime1 += travelList.get(i);
            }
            
            /**************** 
             * to calculate arrivalTIme2
             */
            arrivalTime2 = bus.getDistanceToNextStop() / bus.getSpeed();
            
            return arrivalTime1+arrivalTime2; // in seconds
        }
    }
    
    @Override
    public Double getArrivalTimeBasic(String busStopNumber, String busNo) {
        // first check whether there are buses available
        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:no");
        q.setParameter("no", busNo);
        List<Bus> busList = new ArrayList(q.getResultList());
        if (busList.isEmpty()) {
            // no bus prediction available!
            System.out.println("There is currently no bus available for this route!");
            return -1.0; // no prediction returns -1 or null
        } else {
            // select the bus route in order to get the bus stop list
            Query query = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:busno");
            query.setParameter("busno", busNo);
            BusRoute busRoute = (BusRoute) query.getSingleResult();
            List<BusStop> busStopList = busRoute.getBusStopList();
            // create new list contains all bus stop before current bus stop(included) !!!inverse sequence!!!
            // temp list contains bus stop in the sequence of the distance from current bus stop from closest to farthest
            List<BusStop> temp = new ArrayList();
            for (BusStop m : busStopList) {
                if (m.getBusStopNo().equals(busStopNumber)) {
                    temp.add(0, m);
                    break;
                } else {
                    temp.add(0, m);
                }
            }
            Bus bus = new Bus();
            List<Bus> busListTemp = new ArrayList();
            // select the nearest bus available for prediction
            for (BusStop a : temp) {
                for (Bus b : busList) {
                    if (b.getNextStop().getBusStopNo().equals(a.getBusStopNo())) {
                        busListTemp.add(b);
                    }
                }
                if (!busListTemp.isEmpty()) {
                    break;
                }
            }
            // busListTemp contains a list of buses that approaches the nearest stop near current stop
            for (Bus c : busListTemp) {
                if (bus.getDistanceToNextStop() == null) {
                    bus = c;
                } else if (bus.getDistanceToNextStop() > c.getDistanceToNextStop()) {
                    bus = c;
                }
            }
            // now bus is the nearest bus that to be predicted the arrival time
            // arrivalTime2 is using basic model to calculate the time to arrive at the next stop of the bus 
            double arrivalTime = 0.0;

            /**************** 
             * to calculate arrivalTIme1
             */
            List<BusStop> predictionStopList = new ArrayList();
            for (BusStop l : temp) {
                if (l.getBusStopNo().equals(bus.getNextStop().getBusStopNo())) {
                    predictionStopList.add(0, l);
                    break;
                } else {
                    predictionStopList.add(0, l);
                }
            }
            // now predictionStopList contains all stop between bus and current stop
            double totalDistance = 0.0;
            for (int i=0; i<predictionStopList.size()-1; i++) {
                totalDistance += predictionStopList.get(i).getDistanceToNextStop();
            }
            totalDistance += bus.getDistanceToNextStop();
            /**************** 
             * to calculate arrivalTIme2
             */
            arrivalTime = totalDistance / bus.getSpeed();
            
            return arrivalTime; // in seconds
        }
    }

    // currently unused
//    private Bus findPreviousBus(Bus bus, String busStopName) {
//        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:busno AND b.previousStopName=:stopname");
//        q.setParameter("busno", bus.getBusNo());
//        q.setParameter("stopname", busStopName);
//        List<Bus> busList = new ArrayList(q.getResultList());
//        if (busList.isEmpty()) {
//            System.out.println("No previous bus found.");
//            return null;
//        }
//        Bus previousBus = busList.get(0);
//        for (Bus b : busList) {
//            if (b.getTimeLeftLastStop().after(previousBus.getTimeLeftLastStop())) {
//                previousBus = b;
//            }
//        }
//        return previousBus;
//    }

    private Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        double latMid, m_per_deg_lat, m_per_deg_lon, deltaLat, deltaLon, dist_m;

        latMid = (lat1 + lat2) / 2.0;  // or just use Lat1 for slightly less accurate estimate

        m_per_deg_lat = 111132.954 - 559.822 * Math.cos(2.0 * latMid) + 1.175 * Math.cos(4.0 * latMid);
        m_per_deg_lon = (3.14159265359 / 180) * 6367449 * Math.cos(latMid);

        deltaLat = Math.abs(lat1 - lat2);
        deltaLon = Math.abs(lon1 - lon2);

        dist_m = Math.sqrt(Math.pow(deltaLat * m_per_deg_lat, 2) + Math.pow(deltaLon * m_per_deg_lon, 2));
        return dist_m;
    }
}
