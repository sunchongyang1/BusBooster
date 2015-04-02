/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Bus;
import entity.BusData;
import entity.BusStop;
import entity.Route;
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
    
    @Override
    public void updateArrivalTime(List<BusData> busDataList) {
        // extract all new bus data received from user
        // the data passed in is the user from the same bus
        // analyze the bus data and upadate the corresponding bus data and arrival time and bus delay
        // bus delay only been updated when bus arrives at a bus stop
        // to check whether the bus is at certain bus stop, use the calculate distance method to decide the distance between
        // two points. if less than 10, then we consider these two are at the same location (to avoid in accuracy in gps data)
        // 
        // how to decide whether the user is already alight or not???
        // 
        if (busDataList.isEmpty()) {
            return;
        }
        // Current time
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        
        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.id=:id");
        q.setParameter("id", busDataList.get(0).getBusId()); // 100% can find bus
        List<Bus> busList = new ArrayList(q.getResultList());
        Bus bus = busList.get(0);
        
        Double temp0 = 0.0;
        Double temp1 = 0.0;
        Double temp2 = 0.0;
        
        for (BusData b : busDataList) {
            temp0 += b.getSpeed();
            temp1 += b.getLatitude();
            temp2 += b.getLongitude();
        }
        Double currentSpeed = temp0 / busDataList.size();
        Double avgLat = temp1 / busDataList.size();
        Double avgLon = temp2 / busDataList.size();
        
        Double speed = bus.getSpeed();

        // time interval is 10 seconds, get the distance travelled during such period
        // ***!!!distance between bus stop is assumed as known!!!***
        Double distanceTraveled = 10 * (currentSpeed + speed) / 2;

        //find the bus with the same bus no arrived at next bus stop within 5 min then plus the delay of it
        Double delay = 0.0;
        Query query = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:no AND b.previousStopId=:stopId");
        query.setParameter("no", bus.getBusNo());
        query.setParameter("stopId", bus.getNextStopId());
        List<Bus> preBusList = new ArrayList(query.getResultList());
        if (preBusList.isEmpty()) {
            delay = 0.0;
        }
        delay = preBusList.get(0).getDelay();
        
        Double arrivalTime = (bus.getDistanceToNextStop() - distanceTraveled) / currentSpeed + delay;
        Timestamp currentScheduledArrivingTime = new Timestamp(now.getTime() + (int) (arrivalTime * 1000));

        // update bus information
        bus.setDistanceToNextStop(bus.getDistanceToNextStop() - distanceTraveled);
        bus.setDistanceFromPreviousStop(distanceTraveled);
        bus.setSpeed(currentSpeed);
        bus.setArrivalTime(arrivalTime);
        bus.setLatitude(avgLat);
        bus.setLongitude(avgLon);
        bus.setLastUpdateTime(now);

        // update bus delay
        if (currentScheduledArrivingTime.after(bus.getScheduledArrivingTime())) {
            bus.setDelay((double) currentScheduledArrivingTime.getTime() - bus.getScheduledArrivingTime().getTime());
            bus.setScheduledArrivingTime(currentScheduledArrivingTime);
        } else {
            bus.setDelay(0.0);
            bus.setScheduledArrivingTime(currentScheduledArrivingTime);
        }

        // if bus approach bus stops
        BusStop nextStop = em.find(BusStop.class, bus.getNextStopId());
        if (this.calculateDistance(avgLat, avgLon, nextStop.getLatitude(), nextStop.getLongitude()) < 10
                || bus.getDistanceToNextStop() < 0) {
            double travelTimeBetweenStops = (double) (now.getTime() - bus.getTimeLeftLastStop().getTime()) / 1000; // get the travel time in between stops by seconds
            Query q1 = em.createQuery("SELECT b FROM Route b WHERE b.startBusStopId=:start AND b.endBusStopId=:end");
            q1.setParameter("start", bus.getPreviousStopId());
            q1.setParameter("end", bus.getNextStopId());
            Route bsd = (Route) q1.getSingleResult();
            if(bsd.getTravelTimeUpdateTime().before(now)) {
                bsd.setTravelTime(travelTimeBetweenStops);
            }
            
            bus.setDistanceFromPreviousStop(0.0);
            bus.setDistanceToNextStop(nextStop.getDistanceToNextStop());
            bus.setPreviousStopId(nextStop.getId());
            bus.setPreviousStopName(nextStop.getBusStopName());
            bus.setNextStopId(nextStop.getNextStopId());
            bus.setNextStopName(nextStop.getNextStopName());
            
        }
    }
    
    @Override
    public List<Double> getArrivalTime(String busNo, BusStop busStop) {
        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:busno AND b.nextStopName=:stopname");
        q.setParameter("busno", busNo);
        q.setParameter("stopname", busStop.getBusStopName());
        List<Bus> busList = new ArrayList(q.getResultList());
        if (busList.isEmpty()) { // no bus inbetween current route or no bus at all
            Query query = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:busno");
            query.setParameter("busno", busNo);
            
            
            System.out.println("no such bus exists! please check!");
            return null;
        }
        List<Double> arrivalTime = new ArrayList();
        for (Bus b : busList) {
            // arrival time plus the delay of the previous bus's arrival time
            Bus temp = findPreviousBus(b, busStop.getBusStopName());
            if (temp != null) {
                arrivalTime.add(b.getArrivalTime() + temp.getDelay()); // delay algorithm to be upgraded in the future
            } else {
                arrivalTime.add(b.getArrivalTime());
            }
        }
        
        return arrivalTime;
    }
    
    @Override
    public List<BusStop> getNearbyBusStop(Double lat, Double lon) {
        // sort the nearby bus stop within 1 km and return a list
        return null;
    }

    // currently unused
    private Bus findPreviousBus(Bus bus, String busStopName) {
        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:busno AND b.previousStopName=:stopname");
        q.setParameter("busno", bus.getBusNo());
        q.setParameter("stopname", busStopName);
        List<Bus> busList = new ArrayList(q.getResultList());
        if (busList.isEmpty()) {
            System.out.println("No previous bus found.");
            return null;
        }
        Bus previousBus = busList.get(0);
        for (Bus b : busList) {
            if (b.getTimeLeftLastStop().after(previousBus.getTimeLeftLastStop())) {
                previousBus = b;
            }
        }
        return previousBus;
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
}
