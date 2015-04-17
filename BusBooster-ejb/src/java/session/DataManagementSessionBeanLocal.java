/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Bus;
import entity.BusData;
import entity.BusStop;
import entity.User;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author chongyangsun
 */
@Local
public interface DataManagementSessionBeanLocal {
    public User getBusNumberAndDirection(String busNo, String direction, String busStopNumber); // no existing bus - create. bus exists - attach user to bus. create new user currently.
    public BusData updateRecord(Long userId, Double latitude, Double longitude, Double speed);
    public Boolean archiveData(List<BusData> busDataList);
//    public List<BusStop> getBusStopList(Double latitude, Double longitude);
    
    //for test
    public int getCustomerCount();
}
