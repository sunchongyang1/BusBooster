/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author chongyangsun
 */
@Entity
public class BusRouteSimple implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long busRouteId;
    private String busNo;
    private String direction;//different direction, same bus number will have separate entry in database
    private List<Long> busStopIdList = new ArrayList();
    
    private Integer numberOfUserOnboard;
    private Integer arrivalTime;
    private Boolean busBreakDown;
    private Integer delay;
    
    public BusRouteSimple() {}
    
    public BusRouteSimple(Long busRouteId, String busNo, String direction) {
        this.setBusRouteId(busRouteId);
        this.setBusNo(busNo);
        this.setDirection(direction);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BusRouteSimple)) {
            return false;
        }
        BusRouteSimple other = (BusRouteSimple) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BusRoute[ id=" + id + " ]";
    }

    /**
     * @return the busNo
     */
    public String getBusNo() {
        return busNo;
    }

    /**
     * @param busNo the busNo to set
     */
    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return the busStopIdList
     */
    public List<Long> getBusStopIdList() {
        return busStopIdList;
    }

    /**
     * @param busStopIdList the busStopIdList to set
     */
    public void setBusStopIdList(List<Long> busStopIdList) {
        this.busStopIdList = busStopIdList;
    }

    /**
     * @return the busRouteId
     */
    public Long getBusRouteId() {
        return busRouteId;
    }

    /**
     * @param busRouteId the busRouteId to set
     */
    public void setBusRouteId(Long busRouteId) {
        this.busRouteId = busRouteId;
    }

    /**
     * @return the numberOfUserOnboard
     */
    public Integer getNumberOfUserOnboard() {
        return numberOfUserOnboard;
    }

    /**
     * @param numberOfUserOnboard the numberOfUserOnboard to set
     */
    public void setNumberOfUserOnboard(Integer numberOfUserOnboard) {
        this.numberOfUserOnboard = numberOfUserOnboard;
    }

    /**
     * @return the arrivalTime
     */
    public Integer getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @param arrivalTime the arrivalTime to set
     */
    public void setArrivalTime(Integer arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * @return the busBreakDown
     */
    public Boolean getBusBreakDown() {
        return busBreakDown;
    }

    /**
     * @param busBreakDown the busBreakDown to set
     */
    public void setBusBreakDown(Boolean busBreakDown) {
        this.busBreakDown = busBreakDown;
    }

    /**
     * @return the delay
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * @param delay the delay to set
     */
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

}
