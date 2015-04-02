/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Bus;
import entity.BusData;
import entity.BusStop;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author chongyangsun
 */
@Stateless
public class DataManagementSessionBean implements DataManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    BusManagementSessionBeanLocal bmsbl;

    @Override
    public BusData updateRecord(Long busId, String busNo, String direction, Double longitude, Double latitude, Double speed) {
        // this is a web service. return the busid as a success reply
        Date now = new Date();
        Timestamp time = new Timestamp(now.getTime());
        // if new bus then register bus first. do not wait for server to process the request.
        if (busId == null) {
            if (checkIfNewUser(busNo, latitude, longitude) != null) {
                // new user on registered bus, find the bus id and return
                BusData busData = new BusData(busId, busNo, direction, longitude, latitude, speed, time);
                em.persist(busData);
                return busData;
            } else {
                // new user on unregistered bus, register new bus and return
                // find the location of user, at one of the bus stops
                BusStop busStop = this.findBusStop(latitude, longitude);
                Bus bus = bmsbl.register(busNo, direction, longitude, latitude, time, busStop.getId());
                
                BusData busData = new BusData(bus.getId(), busNo, direction, longitude, latitude, speed, time);
                em.persist(busData);
                return busData;
            }
        } else {
            BusData busData = new BusData(busId, busNo, direction, longitude, latitude, speed, time);
            em.persist(busData);
            return busData;

        }

    }

    public Boolean archiveData(List<BusData> busDataList) {
        for (BusData b : busDataList) {
            b.setArchived(Boolean.TRUE);
        }
        em.persist(busDataList);
        return true;
    }
    
    private BusStop findBusStop(Double lat, Double lon) {
        Query q = em.createQuery("SELECT b FROM BusStop b WHERE b.latitude>:lat1 AND b.latitude<:lat2 AND b.longitude>:lon1 AND b.longitude<:lon2");
        q.setParameter("lat1", lat - 0.01);
        q.setParameter("lat2", lat + 0.01);
        q.setParameter("lon1", lon - 0.01);
        q.setParameter("lon2", lon + 0.01);
        List<BusStop> busStopList = new ArrayList(q.getResultList());
        if (busStopList.isEmpty()) {
            return null;
        }
        if (busStopList.size() > 1) {
            Double distance = this.calculateDistance(lat, lon, busStopList.get(0).getLatitude(), busStopList.get(0).getLongitude());
            int i;
            int min = 0;
            for (i = 1; i < busStopList.size(); i++) {
                Double temp = this.calculateDistance(lat, lon, busStopList.get(i).getLatitude(), busStopList.get(i).getLongitude());
                if (temp < distance) {
                    distance = temp;
                    min = i;
                }
            }
            return busStopList.get(min);
        } else {
            return busStopList.get(0);
        }
    }

    private Long checkIfNewUser(String busNo, Double lat, Double lon) {
        Query q = em.createQuery("SELECT b FROM Bus b WHERE b.busNo=:bno AND b.latitude>:lat1 AND b.latitude<:lat2 AND b.longitude>:lon1 AND b.longitude<:lon2");
        q.setParameter("bno", busNo);
        q.setParameter("lat1", lat - 0.01);
        q.setParameter("lat2", lat + 0.01);
        q.setParameter("lon1", lon - 0.01);
        q.setParameter("lon2", lon + 0.01);
        List<Bus> busList = new ArrayList(q.getResultList());
        if (busList.isEmpty()) {
            return null;
        }
        if (busList.size() > 1) {
            Double distance = this.calculateDistance(lat, lon, busList.get(0).getLatitude(), busList.get(0).getLongitude());
            int i;
            int min = 0;
            for (i = 1; i < busList.size(); i++) {
                Double temp = this.calculateDistance(lat, lon, busList.get(i).getLatitude(), busList.get(i).getLongitude());
                if (temp < distance) {
                    distance = temp;
                    min = i;
                }
            }
            return busList.get(min).getId();
        } else {
            return busList.get(0).getId();
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
