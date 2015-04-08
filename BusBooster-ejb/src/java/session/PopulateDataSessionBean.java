/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BusStop;
import entity.Route;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author chongyangsun
 */
@Stateless
@LocalBean
public class PopulateDataSessionBean {

    @PersistenceContext
    private EntityManager em;
    
    public Boolean isBusStopEmpty() {
        Query q = em.createQuery("SELECT b FROM BusStop b");
        if(q.getResultList().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    public void populateBusStop() {
        if(this.isBusStopEmpty()) {
            // populate bus stop data
            BusStop b1 = this.createNewBusStop("PGP Start", "1", 1.291849, 103.780467, 600.0, null, Boolean.FALSE);
            BusStop b2 = this.createNewBusStop("After Science Park Drive", "2", 1.292584, 103.784508, 290.0, 600.0, Boolean.FALSE);
            this.createRoute(b1, b2, b1.getDistanceToNextStop());
            BusStop b3 = this.createNewBusStop("KR MRT Station", "3", 1.294846, 103.784444, 200.0, 290.0, Boolean.FALSE);
            this.createRoute(b2, b3, b2.getDistanceToNextStop());
            BusStop b4 = this.createNewBusStop("NUH", "4", 1.296326, 103.783477, 300.0, 200.0, Boolean.FALSE);
            this.createRoute(b3, b4, b3.getDistanceToNextStop());
            BusStop b5 = this.createNewBusStop("LT29", "5", 1.297400, 103.781030, 270.0, 300.0, Boolean.FALSE);
            this.createRoute(b4, b5, b4.getDistanceToNextStop());
            BusStop b6 = this.createNewBusStop("University Hall", "6", 1.297287, 103.778576, 400.0, 270.0, Boolean.FALSE);
            this.createRoute(b5, b6, b5.getDistanceToNextStop());
            BusStop b7 = this.createNewBusStop("Opp. University Health Center", "7", 1.298810, 103.775638, 180.0, 400.0, Boolean.FALSE);
            this.createRoute(b6, b7, b6.getDistanceToNextStop());
            BusStop b8 = this.createNewBusStop("Yusof Ishak House", "8", 1.298932, 103.774346, 350.0, 180.0, Boolean.FALSE);
            this.createRoute(b7, b8, b7.getDistanceToNextStop());
            BusStop b9 = this.createNewBusStop("Central Library", "9", 1.296581, 103.772523, 350.0, 350.0, Boolean.FALSE);
            this.createRoute(b8, b9, b8.getDistanceToNextStop());
            BusStop b10 = this.createNewBusStop("LT13", "10", 1.294747, 103.770674, 240.0, 350.0, Boolean.FALSE);
            this.createRoute(b9, b10, b9.getDistanceToNextStop());
            BusStop b11 = this.createNewBusStop("AS7", "11", 1.293661, 103.772072, 350.0, 240.0, Boolean.FALSE);
            this.createRoute(b10, b11, b10.getDistanceToNextStop());
            BusStop b12 = this.createNewBusStop("COM2", "12", 1.294301, 103.773801, 750.0, 350.0, Boolean.FALSE);
            this.createRoute(b11, b12, b11.getDistanceToNextStop());
            BusStop b13 = this.createNewBusStop("BIZ2", "13", 1.293357, 103.775188, 250.0, 750.0, Boolean.FALSE);
            this.createRoute(b12, b13, b12.getDistanceToNextStop());
            BusStop b14 = this.createNewBusStop("Opp. House 12", "14", 1.293702, 103.777082, 100.0, 250.0, Boolean.FALSE);
            this.createRoute(b13, b14, b13.getDistanceToNextStop());
            BusStop b15 = this.createNewBusStop("House 7", "15", 1.293213, 103.777847, 350.0, 100.0, Boolean.FALSE);
            this.createRoute(b14, b15, b14.getDistanceToNextStop());
            BusStop b16 = this.createNewBusStop("PGP Terminal", "16", 1.291849, 103.780467, null, 350.0, Boolean.TRUE);
            this.createRoute(b15, b16, b15.getDistanceToNextStop());
            System.out.println("Populate Bus Stop finished!");
        }
    }
    
    public BusStop createNewBusStop(String busStopName, String busStopNo, Double latitude, Double longitude, Double distanceToNextStop, Double distanceToPreviousStop, Boolean terminal) {
        BusStop busStop = new BusStop(busStopName, busStopNo, latitude, longitude, distanceToNextStop, distanceToPreviousStop, terminal);
        em.persist(busStop);
        return busStop;
    }
    public void createRoute(BusStop start, BusStop end, Double distance) {
        Route route = new Route(start, end, distance);
        em.persist(route);
        start.getOutgoingRouteIdList().add(route.getId());
        end.getIncomingRouteIdList().add(route.getId());
        em.merge(start);
        em.merge(end);
    }
}
