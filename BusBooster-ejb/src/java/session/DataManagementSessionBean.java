/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BusData;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author chongyangsun
 */
@Stateless
public class DataManagementSessionBean implements DataManagementSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public BusData updateRecord(String busNo, String plateNo, Double longtitude, Double latitude, Double speed) {
        Date now = new Date();
        Timestamp time = new Timestamp(now.getTime());
        BusData busData = new BusData(busNo, plateNo, longtitude, latitude, speed, time);
        em.persist(busData);
        return busData;
        
    }
    
    public Boolean archiveData(List<BusData> busDataList) {
        for(BusData b: busDataList) {
            b.setArchived(Boolean.TRUE);
        }
        em.persist(busDataList);
        return true;
    }
}
