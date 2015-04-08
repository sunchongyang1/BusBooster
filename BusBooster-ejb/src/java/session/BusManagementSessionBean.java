/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Bus;
import entity.BusRoute;
import entity.BusStop;
import java.sql.Timestamp;
import java.util.ArrayList;
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
public class BusManagementSessionBean implements BusManagementSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Bus register(String busNo, String direction, Double longitude, Double latitude, Timestamp lastUpdateTime, BusStop previousBusStop) {
        Query query = em.createQuery("SELECT b FROM BusRoute b WHERE b.busNo=:no AND b.direction=:dir");
        query.setParameter("no", busNo);
        query.setParameter("dir", direction);
        BusRoute busRoute = (BusRoute) query.getSingleResult();
        int i = busRoute.getBusStopList().indexOf(previousBusStop);
        BusStop nextBusStop = busRoute.getBusStopList().get(i);
        try {
            Bus bus = new Bus(busNo, direction, longitude, latitude, previousBusStop, nextBusStop);
            Query q = em.createQuery("SELECT r FROM Route r WHERE r.startStop=:start AND r.endStop=:end");
            q.setParameter("start", previousBusStop);
            q.setParameter("end", nextBusStop);
            
            bus.setDistanceToNextStop((Double)q.getSingleResult());
            bus.setDistanceFromPreviousStop(0.0);
            bus.setDelay(0.0);
            em.persist(bus);
            return bus;
        } catch (Exception e) {
            System.out.println("Error regsiter bus");
            return null;
        }
        
    }
    
    // method no use currently
    @Override
    public Boolean update(Long busId, String busNo, String direction, Double longitude, Double latitude, Double speed, Timestamp lastUpdateTime) {
        //calculate arrival time here, record arrival and delay
        
        Query query = em.createQuery("SELECT b FROM Bus b WHERE b.id=:busId");
        query.setParameter("busId", busId);
        List<Bus> busList = new ArrayList(query.getResultList());
        if(busList.isEmpty()) {
            System.out.println("Cannot find bus");
            return false;
        }
        Bus bus = busList.get(0);
        
        if(busNo != null && !bus.getBusNo().equals(busNo)) {
            System.out.println("BusNo does not match");
            return false;
        }
        
        if(longitude != bus.getLatitude()) {
            bus.setLongitude(longitude);
            System.out.println("longitude updated!");
        }
        
        if(latitude != bus.getLatitude()) {
            bus.setLatitude(latitude);
            System.out.println("latitude updated!");
        }
        
        
        
        if(bus.getLastUpdateTime().before(lastUpdateTime)) {
            bus.setLastUpdateTime(lastUpdateTime);
            System.out.println("Last update time updated!");
        }
        
        return true;
    }
    
    @Override
    public Boolean remove(Long busId) {
        return false;
    }
}
