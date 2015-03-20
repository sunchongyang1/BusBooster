/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BusData;
import java.util.ArrayList;
import java.util.Collection;
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

/**
 *
 * @author chongyangsun
 */
@Stateless
public class ServerManagementSessionBean implements ServerManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private TimerService timerService;
    
    @EJB
    DataManagementSessionBeanLocal dmsbl;
    
    @EJB
    PredictionManagementSessionBeanLocal pmsbl;

    @Override
    public void startServer() {
        //start in 10 sec and timeout every 10 seconds
        Timer timer = timerService.createTimer(10000, 10000, "ServerTimer");
    }

    @Override
    public void stopServer() {
        Collection<Timer> timers = timerService.getTimers();
        for (Timer timer : timers) {
            //look for the server timer
            if ("ServerTimer".equals(timer.getInfo())) {
                timer.cancel();
                break;
            }
        }
    }

    //called every 10 seconds
    @Timeout
    private void run() {
        Query q = em.createQuery("SELECT b FROM BusData b WHERE b.archived='false'");
        List<BusData> busDataList = new ArrayList(q.getResultList());
        if(busDataList.isEmpty()) {
            System.out.println("No data received by server!");
            return;
        } else {
            // do something such as update bus arrival time update busdata record
            pmsbl.updateArrivalTime(busDataList);
            dmsbl.archiveData(busDataList);
        }
    }
}
