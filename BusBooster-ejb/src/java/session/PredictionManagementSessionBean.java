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
import entity.Feedback;
import entity.SimulationInfo;
import entity.User;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerService;
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

    @Resource
    private TimerService timerService;

    private final double alpha = 0.7;

    @Override
    public void updateBusInfo(List<BusData> busDataList) {
        // extract all new bus data received from user
        // the data passed in is the user from the same bus
        // analyze the bus data and upadate the corresponding bus data and bus delay
        // bus delay only been updated when bus arrives at a bus stop
        // to check whether the bus is at certain bus stop, use the calculate distance method to decide the distance between
        // two points. if less than 10, then we consider these two are at the same location (to avoid in accuracy in gps data)
        System.out.println("pmsb: initiate update bus info");
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

        BusStop preStop = bus.getPreviousStop();
        System.out.println("pmsb: pre stop name: " + preStop.getBusStopName());
        BusStop busStop = bus.getNextStop();
        System.out.println("pmsb: next stop name: " + busStop.getBusStopName());
//        System.out.println("pmsb: distance to next stop: "+bus.getDistanceToNextStop());
        Double distanceToStop = this.calculateDistance(avgLat, avgLon, busStop.getLatitude(), busStop.getLongitude());

        if (bus.getSpeed() != 0.0 && currentSpeed == 0.0 && distanceToStop < 10 && !bus.getAtStop()) {
            // bus arrives right at the bus stop
            bus.setAtStop(Boolean.TRUE);
            bus.setSpeed(0.0);
            System.out.println("pmsb: Bus just arrives at stop");
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

            // update arrival time and departure time for this bus stop
            ArrivalTime arrivalTime = new ArrivalTime(busStop, bus.getId(), busStop.getArrivalSequence(), now);
            busStop.setArrivalSequence(busStop.getArrivalSequence() + 1);
            busStop.getArrivalTimeList().add(arrivalTime);
            em.merge(busStop);
            em.persist(arrivalTime);

        } else if (bus.getSpeed() == 0.0 && currentSpeed != 0.0 && bus.getAtStop()) {
            System.out.println("pmsb: Bus just leaves at stop");
//            double temp = this.calculateDistance(avgLat, avgLon, preStop.getLatitude(), preStop.getLongitude());
            double distanceTraveled = currentSpeed * 10;
            System.out.println("pmsb: bus speed: " + bus.getSpeed() + " and current speed is " + currentSpeed + " and distance traveled is " + distanceTraveled);
            //bus is leaving the station
            // select largest sequence number first
            DepartureTime departureTime = new DepartureTime(preStop, bus.getId(), preStop.getDepartureSequence(), new Timestamp(now.getTime() - (long) (distanceTraveled / currentSpeed) * 1000));
            preStop.setDepartureSequence(preStop.getDepartureSequence() + 1);
            preStop.getDepartureTimeList().add(departureTime);
            System.out.println("pmsb: just leave stop: updated departure time");
            em.merge(preStop);
            em.persist(departureTime);
            if (!preStop.getArrivalSequence().equals(preStop.getDepartureSequence())) {
                ArrivalTime arrivalTime = new ArrivalTime(preStop, bus.getId(), preStop.getArrivalSequence(), new Timestamp(now.getTime() - (long) (distanceTraveled / currentSpeed) * 1000));
                em.persist(arrivalTime);
                preStop.setArrivalSequence(preStop.getArrivalSequence() + 1);
                preStop.getArrivalTimeList().add(arrivalTime);
                em.merge(preStop);
            }
//            Double distanceTraveled = this.calculateDistance(avgLat, avgLon, preStop.getLatitude(), preStop.getLongitude());

            bus.setDistanceToNextStop(preStop.getDistanceToNextStop() - distanceTraveled);
            bus.setDistanceFromPreviousStop(distanceTraveled);
            System.out.println("pmsb: distance to next stop is " + bus.getDistanceToNextStop());
            bus.setSpeed(currentSpeed);
            bus.setLatitude(avgLat);
            bus.setLongitude(avgLon);
            bus.setLastUpdateTime(now);
            bus.setAtStop(Boolean.FALSE);
            em.merge(bus);

            if (bus.getNextStop().getTerminal()) {
                // next stop is terminal, no point to predict the bus, then remove bus
                for (User u : bus.getUserList()) {
                    u.setBus(null);
                    em.merge(u);
                }
                bus.getUserList().clear();
                em.remove(bus);
                em.flush();
                Collection<Timer> timers = timerService.getTimers();
                for (Timer t : timers) {
                    //look for the timer
                    if (t.getInfo().toString().startsWith("s" + bus.getId() + "s")) {
                        System.out.println("pmsb: simulation timer found!");
                        t.cancel();
                        break;
                    }
                }
            }
        } else if (bus.getSpeed() == 0.0 && currentSpeed == 0.0 && bus.getAtStop()) {
            System.out.println("pmsb: bus is still at stop");
            bus.setLastUpdateTime(now);
            bus.setSpeed(currentSpeed);
            em.merge(bus);
        } else {
            // bus in transit
            System.out.println("pmsb: Bus is in between stops");

//            Double distanceTraveled = this.calculateDistance(avgLat, avgLon, bus.getLatitude(), bus.getLongitude());
            Double distanceTraveled = currentSpeed * 10; // assume bus travels at constant speed!
            System.out.println("pmsb: bus speed: " + bus.getSpeed() + " and current speed is " + currentSpeed + " and distance traveled is " + distanceTraveled);
            bus.setDistanceToNextStop(bus.getDistanceToNextStop() - distanceTraveled);
            bus.setDistanceFromPreviousStop(bus.getDistanceFromPreviousStop() + distanceTraveled);
            bus.setSpeed(currentSpeed);
            bus.setLatitude(avgLat);
            bus.setLongitude(avgLon);
            bus.setLastUpdateTime(now);
            System.out.println("pmsb: Bus in transit: location updated! Lat: " + avgLat + " Lon: " + avgLon);
            System.out.println("pmsb: distance to next stop: " + bus.getDistanceToNextStop());
            System.out.println("pmsb: distance from pre stop: " + bus.getDistanceFromPreviousStop());
            em.merge(bus);

            if (bus.getSpeed() != 0.0 && currentSpeed == 0.0 && distanceToStop < 10 && !bus.getAtStop()) {
                // bus arrives right at the bus stop
                bus.setAtStop(Boolean.TRUE);
                bus.setSpeed(0.0);
                System.out.println("pmsb: Bus just arrives at stop");
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

                // update arrival time and departure time for this bus stop
                ArrivalTime arrivalTime = new ArrivalTime(busStop, bus.getId(), busStop.getArrivalSequence(), now);
                busStop.setArrivalSequence(busStop.getArrivalSequence() + 1);
                busStop.getArrivalTimeList().add(arrivalTime);
                em.merge(busStop);
                em.persist(arrivalTime);
            }

            if (currentSpeed != 0.0 && bus.getDistanceToNextStop() < 0) {
                // bus stops for less than 10 seconds and has passed the bus stop
                // get the distance passed the stop
                System.out.println("pmsb: Oops, bus passed the stop!");
                Double distancePassed = 0 - bus.getDistanceToNextStop();
                System.out.println("pmsb: Distance passed " + distancePassed);
                bus.setPreviousStop(busStop);
                Query q2 = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:busno AND b.direction=:dir");
                q2.setParameter("busno", bus.getBusNo());
                q2.setParameter("dir", bus.getDirection());
                BusRoute busRoute = (BusRoute) q2.getSingleResult();

                int stopIndex = busRoute.getBusStopList().indexOf(busStop);
                bus.setNextStop(busRoute.getBusStopList().get(stopIndex + 1));
                bus.setDistanceFromPreviousStop(distancePassed);
                bus.setDistanceToNextStop(busStop.getDistanceToNextStop() - distancePassed);
                bus.setTimeLeftLastStop(new Timestamp(now.getTime() - (long) (distancePassed / currentSpeed) * 1000));
                bus.setLastUpdateTime(now);
                em.merge(bus);
                // update arrival time and departure time for this bus stop. assume bus did not stop
                ArrivalTime arrivalTime = new ArrivalTime(busStop, bus.getId(), busStop.getArrivalSequence(), new Timestamp(now.getTime() - (long) (distancePassed / currentSpeed) * 1000));
                busStop.setArrivalSequence(busStop.getArrivalSequence() + 1);
                busStop.getArrivalTimeList().add(arrivalTime);
                DepartureTime departureTime = new DepartureTime(busStop, bus.getId(), busStop.getDepartureSequence(), new Timestamp(now.getTime() - (long) (distancePassed / currentSpeed) * 1000));
                busStop.setDepartureSequence(busStop.getDepartureSequence() + 1);
                busStop.getDepartureTimeList().add(departureTime);
                System.out.println("pmsb: passed the stops: updated departure time");
                em.merge(busStop);
                em.persist(arrivalTime);
                em.persist(departureTime);
                if (bus.getNextStop().getTerminal()) {
                    // next stop is terminal, no point to predict the bus, then remove bus
                    for (User u : bus.getUserList()) {
                        u.setBus(null);
                        em.merge(u);
                    }
                    bus.getUserList().clear();
                    em.remove(bus);
                    em.flush();
                    Collection<Timer> timers = timerService.getTimers();
                    for (Timer t : timers) {
                        //look for the timer
                        if (t.getInfo().toString().startsWith("s" + bus.getId() + "s")) {
                            System.out.println("pmsb: simulation timer found!");
                            t.cancel();
                            break;
                        }
                    }
                }

            }
        }
    }

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
//            System.out.println("BusStopList size "+busStopList.size());
            // sort the bus stop according to geo distance to the current location and return the result list from nearest to farthest
            List<BusStop> result = new ArrayList();
//            List<BusStop> temp = new ArrayList();
            boolean flag = true;
            for (BusStop b : busStopList) {
                if (result.isEmpty()) {
//                    System.out.println("I appear only once!");
                    result.add(b);
//                    temp = result;
                } else {
                    flag = true;
//                    System.out.println("temp size "+temp.size());
                    for (BusStop s : result) {
                        if (this.calculateDistance(lat, lon, b.getLatitude(), b.getLongitude())
                                < this.calculateDistance(lat, lon, s.getLatitude(), s.getLongitude())) {
                            result.add(result.indexOf(s), b);
//                            System.out.print(result.size()+" "+temp.size());
//                            System.out.println("Break here");
                            flag = false;
                            break;
                        }
//                        System.out.println("Not this one after break here!");
                    }
//                    System.out.println("temp "+temp.size()+" result "+result.size());
                    if (flag) {
//                        System.out.print("append to the last");
                        result.add(b);
//                        temp = result;
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
    public Double getArrivalTime(String busStopNumber, Bus bus) {
        System.out.println("inside get arrivalTime " + bus.getBusNo());
        if (bus.getBusNo() == null) {
            System.out.println("pmsb: currently no bus available");
            return -1.0;
        }
        // select the bus route in order to get the bus stop list
        Query query = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:bus");
        query.setParameter("bus", bus.getBusNo());
        BusRoute busRoute = (BusRoute) query.getSingleResult();
        List<BusStop> busStopList = busRoute.getBusStopList();
        // create new list contains all bus stop before current bus stop(included) !!!inverse sequence!!!
        // temp list contains bus stop in the sequence of the distance from current bus stop from closest to farthest
        List<BusStop> temp = new ArrayList();
        for (int i = 0; i < busStopList.size(); i++) {
            if (temp.isEmpty()) {
                temp.add(busStopList.get(i));
            } else if (Integer.valueOf(busStopList.get(i).getBusStopNo()) <= Integer.valueOf(busStopNumber)
                    && Integer.valueOf(busStopList.get(i).getBusStopNo()) > Integer.valueOf(temp.get(0).getBusStopNo())) {
                temp.add(0, busStopList.get(i));
            } else if (Integer.valueOf(busStopList.get(i).getBusStopNo()) < Integer.valueOf(busStopNumber)) {
                for (int j = 0; j < temp.size(); j++) {
                    if (Integer.valueOf(busStopList.get(i).getBusStopNo()) > Integer.valueOf(temp.get(j).getBusStopNo())) {
                        temp.add(temp.indexOf(temp.get(j)), busStopList.get(i));
                    }
                }
            }
        }

        // now bus is the nearest bus that to be predicted the arrival time
        // arrivalTime1 is using ESM to calculate the time of next stop of the bus to the current stop
        double arrivalTime1 = 0.0;
        // arrivalTime2 is using ESDAM to calculate the time to arrive at the next stop of the bus 
        double arrivalTime2 = 0.0;

        /**
         * **************
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
//        System.out.println("prediction stop list size " + predictionStopList.size());
        // now predictionStopList contains all stop between bus and current stop
        List<Double> dwellList = new ArrayList();
        List<Double> travelList = new ArrayList();

        BusStop previousBusStop = new BusStop();

        for (BusStop d : predictionStopList) {
            List<Double> dwellListEach = new ArrayList();
            List<Double> travelListEach = new ArrayList();

            int sequence;
            if (d.getArrivalSequence() <= d.getDepartureSequence()) {
                sequence = d.getArrivalSequence();
            } else {
                sequence = d.getDepartureSequence();
            }
            for (int i = sequence - 2; i >= 0; i--) {
                dwellListEach.add((d.getDepartureTimeList().get(i).getDepartureTime().getTime()
                        - d.getArrivalTimeList().get(i).getArrivalTime().getTime()) / 1000.0);
            }

//            for (int p = 0; p < dwellListEach.size(); p++) {
//                System.out.println("pmsb: dwell list each for stop " + d.getBusStopName() + " is " + dwellListEach.get(p));
//            }
            // calculate expected dwell time
            double dwell = 0.0;
            for (int j = 0; j < dwellListEach.size(); j++) {
                dwell += alpha * Math.pow(1 - alpha, j) * dwellListEach.get(j);
            }
            dwellList.add(dwell);
            if (previousBusStop.getId() != null) {
                // calculate the travel time in between stops
                int sequence2;
                int sequence3;
                if (d.getArrivalSequence() <= previousBusStop.getDepartureSequence()) {
                    sequence2 = d.getArrivalSequence();
                } else {
                    sequence2 = previousBusStop.getDepartureSequence();
                }
                if (sequence2 <= sequence) {
                    sequence3 = sequence2;
                } else {
                    sequence3 = sequence;
                }

                for (int i = sequence3 - 2; i >= 0; i--) {
                    travelListEach.add((d.getArrivalTimeList().get(i).getArrivalTime().getTime()
                            - previousBusStop.getDepartureTimeList().get(i).getDepartureTime().getTime()) / 1000.0);
                }
//
//                for (int p = 0; p < travelListEach.size(); p++) {
//                    System.out.println("pmsb: travel list each for stop " + d.getBusStopName() + " is " + travelListEach.get(p));
//                }

                // calculate expected travel time
                double travel = 0.0;
                for (int j = 0; j < travelListEach.size(); j++) {
                    travel += alpha * Math.pow(1 - alpha, j) * travelListEach.get(j);
                }
                travelList.add(travel);
            }
            // before go to the next bus stop, set the current bus stop as previousBusStop
            previousBusStop = d;
        }
        for (int i = 0; i < travelList.size(); i++) {
            arrivalTime1 += dwellList.get(i);
            arrivalTime1 += travelList.get(i);
        }

        /**
         * **************
         * to calculate arrivalTIme2
         */
        if (bus.getAtStop()) {
            if (bus.getPreviousStop().getBusStopNo().equals(busStopNumber)) {
                return 0.0;
            }
            int s;
            List<Double> dwellListEach = new ArrayList();
            if (bus.getPreviousStop().getDepartureSequence() <= bus.getPreviousStop().getArrivalSequence()) {
                s = bus.getPreviousStop().getDepartureSequence();
            } else {
                s = bus.getPreviousStop().getArrivalSequence();
            }
            for (int i = s - 2; i >= 0; i--) {
                dwellListEach.add((bus.getPreviousStop().getDepartureTimeList().get(i).getDepartureTime().getTime()
                        - bus.getPreviousStop().getArrivalTimeList().get(i).getArrivalTime().getTime()) / 1000.0);
            }

            // calculate expected dwell time
            double dwell = 0.0;
            for (int j = 0; j < dwellListEach.size(); j++) {
                dwell += alpha * Math.pow(1 - alpha, j) * dwellListEach.get(j);
            }
            s = 0;
            List<Double> travelListEach = new ArrayList();
            if (bus.getPreviousStop().getDepartureSequence() <= bus.getNextStop().getArrivalSequence()) {
                s = bus.getPreviousStop().getDepartureSequence();
            } else {
                s = bus.getNextStop().getArrivalSequence();
            }

            for (int i = s - 2; i >= 0; i--) {
                travelListEach.add((bus.getNextStop().getArrivalTimeList().get(i).getArrivalTime().getTime()
                        - bus.getPreviousStop().getDepartureTimeList().get(i).getDepartureTime().getTime()) / 1000.0);
            }

            // calculate expected dwell time
            double travel = 0.0;
            for (int j = 0; j < travelListEach.size(); j++) {
                travel += alpha * Math.pow(1 - alpha, j) * travelListEach.get(j);
            }

            arrivalTime2 = dwell + travel * (bus.getDistanceToNextStop() / bus.getPreviousStop().getDistanceToNextStop());
        } else if (!bus.getAtStop() && bus.getSpeed() != 0.0) {
            arrivalTime2 = bus.getDistanceToNextStop() / bus.getSpeed();
        } else {
            int s;
            List<Double> travelListEach = new ArrayList();
            if (bus.getPreviousStop().getDepartureSequence() <= bus.getNextStop().getArrivalSequence()) {
                s = bus.getPreviousStop().getDepartureSequence();
            } else {
                s = bus.getNextStop().getArrivalSequence();
            }

            for (int i = s - 2; i >= 0; i--) {
                travelListEach.add((bus.getNextStop().getArrivalTimeList().get(i).getArrivalTime().getTime()
                        - bus.getPreviousStop().getDepartureTimeList().get(i).getDepartureTime().getTime()) / 1000.0);
            }

            // calculate expected dwell time
            double travel = 0.0;
            for (int j = 0; j < travelListEach.size(); j++) {
                travel += alpha * Math.pow(1 - alpha, j) * travelListEach.get(j);
            }
            arrivalTime2 = travel * (bus.getDistanceToNextStop() / bus.getPreviousStop().getDistanceToNextStop());
        }
        System.out.println("pmsb: arrivalTime1 " + arrivalTime1 + "; arrivalTime2: " + arrivalTime2);
        return arrivalTime1 + arrivalTime2; // in seconds
    }

    @Override
    public Double getArrivalTimeBasic(String busStopNumber, Bus bus) {
        if (bus.getBusNo() == null) {
            System.out.println("pmsb: currently no bus available");
            return -1.0;
        }
        // select the bus route in order to get the bus stop list
        Query query = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:busno");
        query.setParameter("busno", bus.getBusNo());
        BusRoute busRoute = (BusRoute) query.getSingleResult();
        List<BusStop> busStopList = busRoute.getBusStopList();
        // create new list contains all bus stop before current bus stop(included) !!!inverse sequence!!!
        // temp list contains bus stop in the sequence of the distance from current bus stop from closest to farthest
//        List<BusStop> temp = new ArrayList();
//        for (int i = 0; i < busStopList.size(); i++) {
//            if (temp.isEmpty()) {
//                temp.add(busStopList.get(i));
//            } else if (Integer.valueOf(busStopList.get(i).getBusStopNo()) <= Integer.valueOf(busStopNumber)
//                    && Integer.valueOf(busStopList.get(i).getBusStopNo()) > Integer.valueOf(temp.get(0).getBusStopNo())) {
//                temp.add(0, busStopList.get(i));
//            } else if (Integer.valueOf(busStopList.get(i).getBusStopNo()) < Integer.valueOf(busStopNumber)) {
//                for (int j = 0; j < temp.size(); j++) {
//                    if (Integer.valueOf(busStopList.get(i).getBusStopNo()) > Integer.valueOf(temp.get(j).getBusStopNo())) {
//                        temp.add(temp.indexOf(temp.get(j)), busStopList.get(i));
//                    }
//                }
//            }
//        }

        // now bus is the nearest bus that to be predicted the arrival time
        // arrivalTime2 is using basic model to calculate the time to arrive at the next stop of the bus 
        double arrivalTime = 0.0;

        List<BusStop> predictionStopList = new ArrayList();
//        for (BusStop l : temp) {
//            if (l.getBusStopNo().equals(bus.getNextStop().getBusStopNo())) {
//                predictionStopList.add(0, l);
//                break;
//            } else {
//                predictionStopList.add(0, l);
//            }
//        }
        for(int i = Integer.parseInt(bus.getNextStop().getBusStopNo()); i<=Integer.parseInt(busStopNumber); i++) {
            predictionStopList.add(busStopList.get(i-1));
        }

//        if(bus.getAtStop()) {
//            predictionStopList.add(0, bus.getPreviousStop());
//        }
        // now predictionStopList contains all stop between bus and current stop
        double totalDistance = 0.0;
        for (int i = 0; i < predictionStopList.size() - 1; i++) {
            totalDistance += predictionStopList.get(i).getDistanceToNextStop();
        }
        totalDistance += bus.getDistanceToNextStop();
        
        if (bus.getAtStop()) {
            int q;
            if (bus.getNextStop().getArrivalSequence() <= bus.getPreviousStop().getDepartureSequence()) {
                q = bus.getNextStop().getArrivalSequence();
            } else {
                q = bus.getPreviousStop().getDepartureSequence();
            }
            arrivalTime = totalDistance / (bus.getPreviousStop().getDistanceToNextStop() / ((bus.getNextStop().getArrivalTimeList().get(q-2).getArrivalTime().getTime() - bus.getPreviousStop().getDepartureTimeList().get(q-2).getDepartureTime().getTime()) / 1000));
        } else {
            arrivalTime = totalDistance / bus.getSpeed();
        }

        return arrivalTime; // in seconds
    }

    @Override
    public Double getArrivalTimeActual(String busStopNumber, Bus bus) {
        if (bus.getBusNo() == null) {
            System.out.println("pmsb: currently no bus available");
            return -1.0;
        }
        // select the bus route in order to get the bus stop list
        Query query = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:busno");
        query.setParameter("busno", bus.getBusNo());
        BusRoute busRoute = (BusRoute) query.getSingleResult();
        List<BusStop> busStopList = busRoute.getBusStopList();
        // create new list contains all bus stop before current bus stop(included) !!!inverse sequence!!!
        // temp list contains bus stop in the sequence of the distance from current bus stop from closest to farthest
//        List<BusStop> temp = new ArrayList();
//        for (int i = 0; i < busStopList.size(); i++) {
//            if (temp.isEmpty()) {
//                temp.add(busStopList.get(i));
//            } else if (Integer.valueOf(busStopList.get(i).getBusStopNo()) <= Integer.valueOf(busStopNumber)
//                    && Integer.valueOf(busStopList.get(i).getBusStopNo()) > Integer.valueOf(temp.get(0).getBusStopNo())) {
//                temp.add(0, busStopList.get(i));
//            } else if (Integer.valueOf(busStopList.get(i).getBusStopNo()) < Integer.valueOf(busStopNumber)) {
//                for (int j = 0; j < temp.size(); j++) {
//                    if (Integer.valueOf(busStopList.get(i).getBusStopNo()) > Integer.valueOf(temp.get(j).getBusStopNo())) {
//                        temp.add(temp.indexOf(temp.get(j)), busStopList.get(i));
//                    }
//                }
//            }
//        }

        // now bus is the nearest bus that to be predicted the arrival time
        // arrivalTime2 is using basic model to calculate the time to arrive at the next stop of the bus 
        double arrivalTime = 0.0;

        List<BusStop> predictionStopList = new ArrayList();
//        for (BusStop l : temp) {
//            if (l.getBusStopNo().equals(bus.getNextStop().getBusStopNo())) {
//                predictionStopList.add(0, l);
//                break;
//            } else {
//                predictionStopList.add(0, l);
//            }
//        }
        for(int i = Integer.parseInt(bus.getNextStop().getBusStopNo()); i<=Integer.parseInt(busStopNumber); i++) {
            predictionStopList.add(busStopList.get(i-1));
        }

        Query q = em.createQuery("SELECT s FROM SimulationInfo s WHERE s.busId=:id");
        q.setParameter("id", bus.getId());
        SimulationInfo si = (SimulationInfo) q.getSingleResult();
        List<Integer> dwells = si.getDwells();
        List<Integer> speeds = si.getSpeeds();
        int totalDwell = 0;
        double totalTravel = 0.0;
        if (bus.getAtStop()) {
            predictionStopList.add(0, bus.getPreviousStop());
            for (int i = Integer.parseInt(predictionStopList.get(0).getBusStopNo()); i < Integer.parseInt(predictionStopList.get(predictionStopList.size()-1).getBusStopNo()); i++) {
                totalDwell += dwells.get(i - 1);
                totalTravel += busStopList.get(i - 1).getDistanceToNextStop() / speeds.get(i - 1);
            }
            arrivalTime = totalDwell + totalTravel;
        } else {
            for (int i = Integer.parseInt(predictionStopList.get(0).getBusStopNo()); i < Integer.parseInt(predictionStopList.get(predictionStopList.size()-1).getBusStopNo()); i++) {
                totalDwell += dwells.get(i - 1);
                totalTravel += busStopList.get(i - 1).getDistanceToNextStop() / speeds.get(i - 1);
            }
            totalTravel += bus.getDistanceToNextStop() / bus.getSpeed();
            arrivalTime = totalDwell + totalTravel;
        }

        return arrivalTime;
    }

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

    @Override
    public Bus getNearestBus(String busStopNumber, String busNo) {
        // first check whether there are buses available
//        System.out.println("bus no is " + busNo);
        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:no");
        q.setParameter("no", busNo);
        List<Bus> busList = new ArrayList(q.getResultList());
        if (busList.isEmpty()) {
            // no bus prediction available!
            System.out.println("There is currently no bus available for this route!");
            return null; // no prediction returns -1 or null
        } else {
            for (Bus p : busList) {
                if (p.getAtStop() && p.getPreviousStop().getBusStopNo().equals(busStopNumber)) {
                    System.out.println("pmsb: found bus arrived!");
                    return p;
                }
            }

            // select the bus route in order to get the bus stop list
            Query query = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:busno");
            query.setParameter("busno", busNo);
            BusRoute busRoute = (BusRoute) query.getSingleResult();
            List<BusStop> busStopList = busRoute.getBusStopList();
//            System.out.println("bus stop retreived from bus route size is " + busStopList.size());
            // create new list contains all bus stop before current bus stop(included) !!!inverse sequence!!!
            // temp list contains bus stop in the sequence of the distance from current bus stop from closest to farthest
            List<BusStop> temp = new ArrayList();
            for (int i = 0; i < busStopList.size(); i++) {
                if (temp.isEmpty()) {
//                    System.out.println("see me once");
                    temp.add(busStopList.get(i));
                } else if (Integer.parseInt(busStopList.get(i).getBusStopNo()) <= Integer.parseInt(busStopNumber)
                        && Integer.parseInt(busStopList.get(i).getBusStopNo()) > Integer.parseInt(temp.get(0).getBusStopNo())) {
//                    System.out.println("im here");
                    temp.add(0, busStopList.get(i));
                } else if (Integer.parseInt(busStopList.get(i).getBusStopNo()) < Integer.parseInt(busStopNumber)) {
//                    System.out.println("another me!");
                    for (int j = 0; j < temp.size(); j++) {
                        if (Integer.parseInt(busStopList.get(i).getBusStopNo()) > Integer.parseInt(temp.get(j).getBusStopNo())) {
                            temp.add(temp.indexOf(temp.get(j)), busStopList.get(i));
                        }
                    }
                }
//                System.out.println("test " + Integer.parseInt(busStopList.get(i).getBusStopNo()) + " haha " + Integer.parseInt(busStopNumber));
            }
//            System.out.println("pmsb: select bus stop list reverse: " + temp + " size is " + temp.size());
            Bus bus = new Bus(); // target bus
            List<Bus> busListTemp = new ArrayList();
            // select the nearest bus available for prediction
            for (int a = 0; a < temp.size(); a++) {
                for (int b = 0; b < busList.size(); b++) {
                    if (busList.get(b).getNextStop().getBusStopNo().equals(temp.get(a).getBusStopNo())) {
                        busListTemp.add(busList.get(b));
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
            System.out.println("nearest bus is " + bus);
            return bus;
        }
    }

    @Override
    public List<Feedback> getFeedback(Bus bus) {
        Query q = em.createQuery("SELECT f FROM Feedback f WHERE f.busId=:id");
        q.setParameter("id", bus.getId());
        List<Feedback> feedbacks = new ArrayList(q.getResultList());
        if (feedbacks.isEmpty()) {
            return null;
        } else {
            return feedbacks;
        }
    }
}
