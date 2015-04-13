/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BusData;
import entity.BusRoute;
import entity.BusStop;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author chongyangsun
 */
@Local
public interface PredictionManagementSessionBeanLocal {
    public void updateBusInfo(List<BusData> busDataList);
    
//    public List<Double> getArrivalTime(String busNo, BusStop busStop);
    public List<BusStop> getNearbyBusStop(Double lat, Double lon);
    public List<BusRoute> getBusRouteByBusStop(String busStopNumber);
    
    public Double getArrivalTime(String busStopNumber, String busNo);
    public Double getArrivalTimeBasic(String busStopNumber, String busNo);
}
