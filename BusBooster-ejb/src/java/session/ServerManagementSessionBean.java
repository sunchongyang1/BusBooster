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
        if (busDataList.isEmpty()) {
            System.out.println("No data received by server!");
            return;
        } else {
            // do something such as update bus arrival time update busdata record
            List<BusData> temp = new ArrayList();
            int i;
            int j;
            boolean newBus = false;
            for (i = 0; i < busDataList.size(); i++) {
                temp.clear();
                if (busDataList.get(i).getArchived()) {
                    // if archived, continue
                    continue;
                } else if (!busDataList.get(i).getNewBus()) {
                    // existing bus and the user already on board
                    temp.add(busDataList.get(i));

                    for (j = i + 1; j < busDataList.size(); j++) {
                        if (!busDataList.get(j).getArchived() && busDataList.get(j).getBusNo().equals(busDataList.get(i).getBusNo())
                                && this.calculateDistance(busDataList.get(j).getLatitude(), busDataList.get(j).getLongitude(),
                                        busDataList.get(i).getLatitude(), busDataList.get(i).getLongitude()) <= 10.0) {
                            temp.add(busDataList.get(j));
                        } // 0 tolerant for no bus no, future development to accommodate user who does not enter bus no.
                    }

                    pmsbl.updateBusInfo(temp);
                    dmsbl.archiveData(temp);
                }
            }

        }
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
