/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BusData;
import java.lang.Math.*;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
