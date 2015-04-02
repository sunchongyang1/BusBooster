/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Bus;
import java.sql.Timestamp;
import javax.ejb.Local;

/**
 *
 * @author chongyangsun
 */
@Local
public interface BusManagementSessionBeanLocal {
    public Bus register(String busNo, String direction, Double longitude, Double latitude, Timestamp lastUpdateTime, Long previousStopId);
    //following method no use currently
    public Boolean update(Long busId, String busNo, String direction, Double longitude, Double latitude, Double speed, Timestamp lastUpdateTime);
    public Boolean remove(Long busId);
}
