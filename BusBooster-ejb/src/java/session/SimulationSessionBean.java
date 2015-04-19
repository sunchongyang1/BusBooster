/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Bus;
import entity.BusStop;
import entity.Location;
import entity.Output;
import entity.Route;
import entity.SimulationInfo;
import entity.User;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.tools.DocumentationTool;

/**
 *
 * @author chongyangsun
 */
@Stateless
public class SimulationSessionBean implements SimulationSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private TimerService timerService;

    @EJB
    PredictionManagementSessionBeanLocal pmsbl;

    @EJB
    DataManagementSessionBeanLocal dmsbl;

    @Override
    public Boolean createNewBus(String busNo, String direction, List<Integer> dwells, List<Integer> speeds) {
        Date now = new Date();
        BusStop startStop = this.getBusStop("1");
        System.out.println("ssb: start stop name: "+startStop.getBusStopName());
        
        BusStop endStop = this.getBusStop("2");
        System.out.println("ssb: next stop name: "+endStop.getBusStopName());
        
        Bus bus = new Bus(busNo, direction, startStop.getLatitude(), startStop.getLongitude(), startStop, endStop);
        bus.setDistanceFromPreviousStop(0.0);
        bus.setDistanceToNextStop(startStop.getDistanceToNextStop());
        bus.setTimeLeftLastStop(new Timestamp(now.getTime()));
        em.persist(bus);
        User user = new User("testUser");
        em.persist(user);
        bus.getUserList().add(user);
        user.setBus(bus);
        bus.setNumberOfUserOnboard(bus.getUserList().size());
        em.merge(bus);
        em.merge(user);
        dmsbl.updateRecord(user.getId(), bus.getLatitude(), bus.getLongitude(), 0.0);
        System.out.println("check size " + dwells.size());

        SimulationInfo si = new SimulationInfo(user.getId(), bus.getId(), busNo, direction, dwells, speeds);
        em.persist(si);
        System.out.println("SimulationInfo id " + si.getId() + " and check size " + si.getDwells().size());
        
        //record output
        for(int i=2; i<15; i++) {
            BusStop bs = this.getBusStop(String.valueOf(i));
            int arrivalTimeHybrid = (int)(double)pmsbl.getArrivalTime(String.valueOf(i), bus);
            int arrivalTimeBasic = (int)(double)pmsbl.getArrivalTimeBasic(String.valueOf(i), bus);
            int arrivalTimeActual = (int)(double)pmsbl.getArrivalTimeActual(String.valueOf(i), bus);
            Output output = new Output(busNo, String.valueOf(i), bs.getBusStopName(), arrivalTimeHybrid, arrivalTimeBasic,
                arrivalTimeActual, (int)(double)Math.pow(arrivalTimeHybrid-arrivalTimeActual, 2), (int)(double)Math.pow(arrivalTimeBasic-arrivalTimeActual, 2));
            em.persist(output);
        }

        //start simulation
        System.out.println("Bus start running from PGP Start");
        Timer timer = timerService.createTimer(10000, 10000, "s" + bus.getId() + "s" + si.getId());

        return true;
    }

    private BusStop getBusStop(String busStopNo) {
        Query q = em.createQuery("SELECT b FROM BusStop b WHERE b.busStopNo=:number");
        q.setParameter("number", busStopNo);

        return (BusStop) q.getSingleResult();
    }

    @Timeout
    private void runSimulation(Timer timer) {
        Date now = new Date();
        String info = timer.getInfo().toString();
        String[] temp = info.split("s");
        Long busId = Long.valueOf(temp[1]);
//        System.out.println("Bus id is " + busId);
        Long siId = Long.valueOf(temp[2]);
//        System.out.println("simulation id is " + siId);
        Bus bus = em.find(Bus.class, busId);
        if(bus == null) {
            System.out.println("ssb: bus does not exist!");
            Collection<Timer> timers = timerService.getTimers();
                for (Timer t : timers) {
                    //look for the server timer
                    if ((t.getInfo().toString().startsWith("s" + bus.getId() + "s"))) {
                        System.out.println("ssb: timer found!");
                        t.cancel();
                        break;
                    }
                }
            return;
        }
        SimulationInfo si = em.find(SimulationInfo.class, siId);
//        System.out.println(si.getDwells().size());
        Query q = em.createQuery("SELECT r FROM Route r ORDER BY r.id ASC");
        List<Route> routeList = new ArrayList(q.getResultList());

        int total = 0; // time interval
        int current = si.getCount() * 10; // time interval
        int routeNo = 0; // current route that the bus is travelling on
        int busStopsAt = 0; // the bus stop Number that the bus is currently stopped at. if during travel, this value is 0
        for (int i = 0; i < 15; i++) {
            total += si.getDwells().get(i);
            if (total > current) {
                busStopsAt = i + 1;//bus stop number starts at 1
                routeNo = 0;
//                System.out.println("stop: break at i = " + i);
                break;
            }
            total += routeList.get(i).getDistance() / si.getSpeeds().get(i);
            if (total > current) {
                routeNo = i + 1;// route number starts at 1
                busStopsAt = 0;
//                System.out.println("travel: break at i = " + i);
                break;
            }
            if (i == 14) {
                //bus is terminated, stop the timer and remove the bus
                Collection<Timer> timers = timerService.getTimers();
                for (Timer t : timers) {
                    //look for the server timer
                    if (("s" + bus.getId() + "s" + si.getId()).equals(t.getInfo())) {
                        System.out.println("timer found!");
                        t.cancel();
                        break;
                    }
                }
                User user = em.find(User.class, si.getUserId());
                user.setBus(null);
                em.remove(bus);
                em.flush();
            }
        }
        if (routeNo > 0 && busStopsAt == 0) {
            // bus is travelling, calculate all dwell time
            System.out.println("ssb: bus travels "+routeNo);
            int totalDwell = 0;
            int totalDistance = 0;
            int travelTime = 0;
            for (int j = 0; j < routeNo; j++) {
                totalDwell += si.getDwells().get(j);
            }
            for (int k = 0; k < (routeNo - 1); k++) {
                totalDistance += routeList.get(k).getDistance();
                travelTime += routeList.get(k).getDistance() / si.getSpeeds().get(k);
            }
            int distanceOnCurrentRoute = (current - totalDwell - travelTime) * si.getSpeeds().get(routeNo - 1);
//            bus.setDistanceFromPreviousStop((double)distanceOnCurrentRoute);
//            bus.setDistanceToNextStop(bus.getDistanceToNextStop()-(double)distanceOnCurrentRoute);
//            bus.setLastUpdateTime(new Timestamp(now.getTime()));
//            Route route = routeList.get(routeNo-1);
//            BusStop start = em.find(BusStop.class, route.getStartStopId());
//            BusStop end = em.find(BusStop.class, route.getEndStopId());
//            bus.setPreviousStop(start);
//            bus.setNextStop(end);
//            bus.setSpeed((double)si.getSpeeds().get(routeNo-1));
//            em.merge(bus);
            totalDistance += distanceOnCurrentRoute;
            long locationId = totalDistance / 10;
            if (locationId == 0) {
                locationId = 1;
            }
            Query q2 = em.createQuery("SELECT l FROM Location l WHERE l.id=:id");
            q2.setParameter("id", locationId);
            Location l = (Location) q2.getSingleResult();
            dmsbl.updateRecord(si.getUserId(), l.getLatitude(), l.getLongitude(), (double) si.getSpeeds().get(routeNo - 1));

        } else if (routeNo == 0 && busStopsAt > 0) {
            // bus is stops at bus stop
            System.out.println("ssb: bus stops "+busStopsAt);
//            Query query = em.createQuery("SELECT b FROM BusStop b WHERE b.busStopNo=:number");
//            query.setParameter("number", busStopsAt);
            BusStop busStop = this.getBusStop((new Integer(busStopsAt)).toString());
            dmsbl.updateRecord(si.getUserId(), busStop.getLatitude(), busStop.getLongitude(), 0.0);
        }

        si.setCount(si.getCount() + 1);
        em.merge(si);
    }

}
