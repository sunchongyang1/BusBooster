/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author chongyangsun
 */
@Entity
public class BusSimple implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String busNo;
    private String direction;
    private Double longitude;
    private Double latitude;
    
    private Integer arrivalTime;
    private Integer delay;
    private Boolean breakDown;
    
    private Integer numberOfUserOnboard;
    
    public BusSimple(){}
    
    public BusSimple(String busNo, String direction, Double latitude, Double longitude, Integer arrivalTime, Integer delay, Boolean breakDown, Integer numberOfUserOnboard){
        this.setBusNo(busNo);
        this.setDirection(direction);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setArrivalTime(arrivalTime);
        this.setDelay(delay);
        this.setBreakDown(breakDown);
        this.setNumberOfUserOnboard(numberOfUserOnboard);
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
        if (!(object instanceof BusSimple)) {
            return false;
        }
        BusSimple other = (BusSimple) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Bus[ id=" + id + " ]";
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
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    /**
     * @return the breakDown
     */
    public Boolean getBreakDown() {
        return breakDown;
    }

    /**
     * @param breakDown the breakDown to set
     */
    public void setBreakDown(Boolean breakDown) {
        this.breakDown = breakDown;
    }
    
}
