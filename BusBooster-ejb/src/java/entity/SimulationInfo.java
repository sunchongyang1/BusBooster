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

/**
 *
 * @author chongyangsun
 */
@Entity
public class SimulationInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String busNo;
    private String direction;
    private Long userId;
    private Long busId;
    private List<Integer> dwells = new ArrayList();
    private List<Integer> speeds = new ArrayList();
    
    private Integer count;
//    private Integer location;
//    private Long routeId;
//    private Integer routeNo;
    
    public SimulationInfo(){}
    
    public SimulationInfo(Long userId, Long busId, String busNo, String direction, List<Integer> dwells, List<Integer> speeds){
        this.setUserId(userId);
        this.setBusId(busId);
        this.setBusNo(busNo);
        this.setDirection(direction);
        this.setDwells(dwells);
        this.setSpeeds(speeds);
//        this.getDwells().add(dwell1);
//        this.getDwells().add(dwell2);
//        this.getDwells().add(dwell3);
//        this.getDwells().add(dwell4);
//        this.getDwells().add(dwell5);
//        this.getDwells().add(dwell6);
//        this.getDwells().add(dwell7);
//        this.getDwells().add(dwell8);
//        this.getDwells().add(dwell9);
//        this.getDwells().add(dwell10);
//        this.getDwells().add(dwell11);
//        this.getDwells().add(dwell12);
//        this.getDwells().add(dwell13);
//        this.getDwells().add(dwell14);

//        this.getSpeeds().add(speed1);
//        this.getSpeeds().add(speed2);
//        this.getSpeeds().add(speed3);
//        this.getSpeeds().add(speed4);
//        this.getSpeeds().add(speed5);
//        this.getSpeeds().add(speed6);
//        this.getSpeeds().add(speed7);
//        this.getSpeeds().add(speed8);
//        this.getSpeeds().add(speed9);
//        this.getSpeeds().add(speed10);
//        this.getSpeeds().add(speed11);
//        this.getSpeeds().add(speed12);
//        this.getSpeeds().add(speed13);
//        this.getSpeeds().add(speed14);
//        this.getSpeeds().add(speed15);
        
        this.setCount(1);
//        this.setLocation(1);
//        this.setRouteNo(1);
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
        if (!(object instanceof SimulationInfo)) {
            return false;
        }
        SimulationInfo other = (SimulationInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SimulationInfo[ id=" + id + " ]";
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
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the dwells
     */
    public List<Integer> getDwells() {
        return dwells;
    }

    /**
     * @param dwells the dwells to set
     */
    public void setDwells(List<Integer> dwells) {
        this.dwells = dwells;
    }

    /**
     * @return the speeds
     */
    public List<Integer> getSpeeds() {
        return speeds;
    }

    /**
     * @param speeds the speeds to set
     */
    public void setSpeeds(List<Integer> speeds) {
        this.speeds = speeds;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the busId
     */
    public Long getBusId() {
        return busId;
    }

    /**
     * @param busId the busId to set
     */
    public void setBusId(Long busId) {
        this.busId = busId;
    }
    
}
