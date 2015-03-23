/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BusData;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author chongyangsun
 */
@Local
public interface DataManagementSessionBeanLocal {
    public BusData updateRecord(Long busId, String busNo, String plateNo, Double Longtitude, Double Latitude, Double speed);
    
    public Boolean archiveData(List<BusData> busDataList);
}
