/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.ArrivalTime;
import entity.BusRoute;
import entity.BusStop;
import entity.DepartureTime;
import entity.Route;
import entity.User;
import java.sql.Timestamp;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author chongyangsun
 */
@Stateless
public class PopulateDataManagementSessionBean implements PopulateDataManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    private Boolean isBusStopEmpty() {
        Query q = em.createQuery("SELECT b FROM BusStop b");
        if (q.getResultList().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean isBusRouteEmpty() {
        Query q = em.createQuery("SELECT b FROM BusRoute b");
        if (q.getResultList().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean isArrivalEmpty() {
        Query q = em.createQuery("SELECT a FROM ArrivalTime a");
        if (q.getResultList().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void populateBusStop() {
        if (this.isBusStopEmpty()) {
            User user = new User("testUser");
            em.persist(user);
            // populate bus stop data
            BusStop b1 = this.createNewBusStop("PGP Start", "1", 1.291849, 103.780467, 600.0, null, Boolean.FALSE);
            System.out.println("b1 populated " + b1);
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

            if (this.isBusRouteEmpty()) {
                BusRoute busRoute = this.createBusRoute("A1", "SCI-BIZ-PGP");
                busRoute.getBusStopList().add(b1);
                busRoute.getBusStopList().add(b2);
                busRoute.getBusStopList().add(b3);
                busRoute.getBusStopList().add(b4);
                busRoute.getBusStopList().add(b5);
                busRoute.getBusStopList().add(b6);
                busRoute.getBusStopList().add(b7);
                busRoute.getBusStopList().add(b8);
                busRoute.getBusStopList().add(b9);
                busRoute.getBusStopList().add(b10);
                busRoute.getBusStopList().add(b11);
                busRoute.getBusStopList().add(b12);
                busRoute.getBusStopList().add(b13);
                busRoute.getBusStopList().add(b14);
                busRoute.getBusStopList().add(b15);
                busRoute.getBusStopList().add(b16);
                em.merge(busRoute);
                b1.getBusRouteList().add(busRoute);
                b2.getBusRouteList().add(busRoute);
                b3.getBusRouteList().add(busRoute);
                b4.getBusRouteList().add(busRoute);
                b5.getBusRouteList().add(busRoute);
                b6.getBusRouteList().add(busRoute);
                b7.getBusRouteList().add(busRoute);
                b8.getBusRouteList().add(busRoute);
                b9.getBusRouteList().add(busRoute);
                b10.getBusRouteList().add(busRoute);
                b11.getBusRouteList().add(busRoute);
                b12.getBusRouteList().add(busRoute);
                b13.getBusRouteList().add(busRoute);
                b14.getBusRouteList().add(busRoute);
                b15.getBusRouteList().add(busRoute);
                b16.getBusRouteList().add(busRoute);
                em.merge(b1);
                em.merge(b2);
                em.merge(b3);
                em.merge(b4);
                em.merge(b5);
                em.merge(b6);
                em.merge(b7);
                em.merge(b8);
                em.merge(b9);
                em.merge(b10);
                em.merge(b11);
                em.merge(b12);
                em.merge(b13);
                em.merge(b14);
                em.merge(b15);
                em.merge(b16);

                System.out.println("Populate Bus Route finished!");

                // populate arrival time and departure time
                if (this.isArrivalEmpty()) {
                    Date now = new Date();
                    long tenMinutes = 10 * 60 * 1000;

                    this.createHistory(b1, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes), 0);
                    this.createHistory(b2, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 75 * 1000), 0);
                    this.createHistory(b3, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 161 * 1000), 50);
                    this.createHistory(b4, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 181 * 1000), 0);
                    this.createHistory(b5, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 241 * 1000), 30);
                    this.createHistory(b6, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 306 * 1000), 20);
                    this.createHistory(b7, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 356 * 1000), 10);
                    this.createHistory(b8, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 402 * 1000), 10);
                    this.createHistory(b9, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 532 * 1000), 60);
                    this.createHistory(b10, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 577 * 1000), 10);
                    this.createHistory(b11, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 597 * 1000), 0);
                    this.createHistory(b12, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 671 * 1000), 30);
                    this.createHistory(b13, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 806 * 1000), 10);
                    this.createHistory(b14, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 847 * 1000), 10);
                    this.createHistory(b15, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 857 * 1000), 0);
                    this.createHistory(b16, Long.valueOf(0), new Timestamp(now.getTime() - 6 * tenMinutes + 892 * 1000), 0);

                    this.createHistory(b1, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes), 0);
                    this.createHistory(b2, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 60 * 1000), 0);
                    this.createHistory(b3, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 119 * 1000), 30);
                    this.createHistory(b4, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 136 * 1000), 0);
                    this.createHistory(b5, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 171 * 1000), 10);
                    this.createHistory(b6, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 215 * 1000), 10);
                    this.createHistory(b7, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 275 * 1000), 10);
                    this.createHistory(b8, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 331 * 1000), 20);
                    this.createHistory(b9, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 414 * 1000), 40);
                    this.createHistory(b10, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 459 * 1000), 10);
                    this.createHistory(b11, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 483 * 1000), 0);
                    this.createHistory(b12, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 547 * 1000), 20);
                    this.createHistory(b13, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 632 * 1000), 10);
                    this.createHistory(b14, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 692 * 1000), 10);
                    this.createHistory(b15, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 702 * 1000), 0);
                    this.createHistory(b16, Long.valueOf(1), new Timestamp(now.getTime() - 5 * tenMinutes + 737 * 1000), 0);

                    this.createHistory(b1, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes), 0);
                    this.createHistory(b2, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 55 * 1000), 0);
                    this.createHistory(b3, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 101 * 1000), 20);
                    this.createHistory(b4, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 116 * 1000), 0);
                    this.createHistory(b5, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 169 * 1000), 30);
                    this.createHistory(b6, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 219 * 1000), 20);
                    this.createHistory(b7, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 274 * 1000), 10);
                    this.createHistory(b8, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 314 * 1000), 10);
                    this.createHistory(b9, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 413 * 1000), 60);
                    this.createHistory(b10, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 455 * 1000), 10);
                    this.createHistory(b11, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 476 * 1000), 0);
                    this.createHistory(b12, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 545 * 1000), 30);
                    this.createHistory(b13, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 623 * 1000), 10);
                    this.createHistory(b14, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 675 * 1000), 10);
                    this.createHistory(b15, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 684 * 1000), 0);
                    this.createHistory(b16, Long.valueOf(2), new Timestamp(now.getTime() - 4 * tenMinutes + 719 * 1000), 0);

                    this.createHistory(b1, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes), 0);
                    this.createHistory(b2, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 46 * 1000), 0);
                    this.createHistory(b3, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 118 * 1000), 50);
                    this.createHistory(b4, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 132 * 1000), 0);
                    this.createHistory(b5, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 182 * 1000), 30);
                    this.createHistory(b6, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 226 * 1000), 20);
                    this.createHistory(b7, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 273 * 1000), 10);
                    this.createHistory(b8, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 305 * 1000), 10);
                    this.createHistory(b9, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 397 * 1000), 60);
                    this.createHistory(b10, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 434 * 1000), 10);
                    this.createHistory(b11, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 452 * 1000), 0);
                    this.createHistory(b12, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 514 * 1000), 30);
                    this.createHistory(b13, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 582 * 1000), 10);
                    this.createHistory(b14, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 623 * 1000), 10);
                    this.createHistory(b15, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 631 * 1000), 0);
                    this.createHistory(b16, Long.valueOf(3), new Timestamp(now.getTime() - 3 * tenMinutes + 666 * 1000), 0);

                    this.createHistory(b1, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes), 0);
                    this.createHistory(b2, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 50 * 1000), 0);
                    this.createHistory(b3, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 124 * 1000), 50);
                    this.createHistory(b4, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 139 * 1000), 0);
                    this.createHistory(b5, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 190 * 1000), 30);
                    this.createHistory(b6, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 237 * 1000), 20);
                    this.createHistory(b7, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 287 * 1000), 10);
                    this.createHistory(b8, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 323 * 1000), 10);
                    this.createHistory(b9, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 418 * 1000), 60);
                    this.createHistory(b10, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 457 * 1000), 10);
                    this.createHistory(b11, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 477 * 1000), 0);
                    this.createHistory(b12, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 542 * 1000), 30);
                    this.createHistory(b13, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 614 * 1000), 10);
                    this.createHistory(b14, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 660 * 1000), 10);
                    this.createHistory(b15, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 670 * 1000), 0);
                    this.createHistory(b16, Long.valueOf(4), new Timestamp(now.getTime() - 2 * tenMinutes + 703 * 1000), 0);

                    this.createHistory(b1, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes), 0);
                    this.createHistory(b2, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 55 * 1000), 0);
                    this.createHistory(b3, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 131 * 1000), 20);
                    this.createHistory(b4, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 146 * 1000), 0);
                    this.createHistory(b5, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 199 * 1000), 30);
                    this.createHistory(b6, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 249 * 1000), 20);
                    this.createHistory(b7, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 304 * 1000), 10);
                    this.createHistory(b8, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 344 * 1000), 10);
                    this.createHistory(b9, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 443 * 1000), 60);
                    this.createHistory(b10, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 485 * 1000), 10);
                    this.createHistory(b11, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 506 * 1000), 0);
                    this.createHistory(b12, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 575 * 1000), 30);
                    this.createHistory(b13, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 654 * 1000), 10);
                    this.createHistory(b14, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 705 * 1000), 10);
                    this.createHistory(b15, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 714 * 1000), 0);
                    this.createHistory(b16, Long.valueOf(5), new Timestamp(now.getTime() - 1 * tenMinutes + 749 * 1000), 0);

                    System.out.println("Populate historical data finished!");
                }
            }
        }
    }

    private BusStop createNewBusStop(String busStopName, String busStopNo, Double latitude, Double longitude, Double distanceToNextStop, Double distanceToPreviousStop, Boolean terminal) {
        BusStop busStop = new BusStop(busStopName, busStopNo, latitude, longitude, distanceToNextStop, distanceToPreviousStop, terminal);
        em.persist(busStop);
        em.flush();
        return busStop;
    }

    private void createRoute(BusStop start, BusStop end, Double distance) {
        Route route = new Route(start.getId(), end.getId(), distance);
        em.persist(route);
        em.flush();
        System.out.println("route populated " + route);
        System.out.println("start is " + start);
        start.getOutgoingRouteIdList().add(route.getId());
        end.getIncomingRouteIdList().add(route.getId());
        em.merge(start);
        em.merge(end);
    }

    private BusRoute createBusRoute(String busNo, String direction) {
        BusRoute busRoute = new BusRoute(busNo, direction);
        em.persist(busRoute);
        return busRoute;
    }

    private void createHistory(BusStop busStop, Long busId, Timestamp departureTime, Integer dwell) {
        ArrivalTime arrival = new ArrivalTime(busStop, busId, busStop.getArrivalSequence(), new Timestamp(departureTime.getTime() - dwell * 1000));
        em.persist(arrival);
        busStop.getArrivalTimeList().add(arrival);
        busStop.setArrivalSequence(busStop.getArrivalSequence() + 1);

        if (!busStop.getTerminal()) {
//            Timestamp departureTime = new Timestamp(arrivalTime.getTime() + dwell * 1000);
            DepartureTime departure = new DepartureTime(busStop, busId, busStop.getDepartureSequence(), departureTime);
            em.persist(departure);
            busStop.getDepartureTimeList().add(departure);
            busStop.setDepartureSequence(busStop.getDepartureSequence() + 1);
        } else {
            busStop.setDepartureTimeList(null);
            busStop.setDepartureSequence(busStop.getArrivalSequence());
        }

        em.merge(busStop);
    }
}
