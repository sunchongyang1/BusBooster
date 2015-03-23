/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author chongyangsun
 */
@Entity
public class BusStop implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String busStopName;
    private String busStopNo;
    private Double longtitude;
    private Double latitude;
    private Long nextStopId;
    private String nextStopName;
    private Long previousStopId;
    private Double distanceToNextStop;
    private Double distanceToPreviousStop;
    
    private Boolean terminal;
    
    public BusStop(){}

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
        if (!(object instanceof BusStop)) {
            return false;
        }
        BusStop other = (BusStop) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BusStop[ id=" + id + " ]";
    }

    /**
     * @return the busStopName
     */
    public String getBusStopName() {
        return busStopName;
    }

    /**
     * @param busStopName the busStopName to set
     */
    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    /**
     * @return the busStopNo
     */
    public String getBusStopNo() {
        return busStopNo;
    }

    /**
     * @param busStopNo the busStopNo to set
     */
    public void setBusStopNo(String busStopNo) {
        this.busStopNo = busStopNo;
    }

    /**
     * @return the longtitude
     */
    public Double getLongtitude() {
        return longtitude;
    }

    /**
     * @param longtitude the longtitude to set
     */
    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
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
     * @return the nextStopId
     */
    public Long getNextStopId() {
        return nextStopId;
    }

    /**
     * @param nextStopId the nextStopId to set
     */
    public void setNextStopId(Long nextStopId) {
        this.nextStopId = nextStopId;
    }

    /**
     * @return the previousStopId
     */
    public Long getPreviousStopId() {
        return previousStopId;
    }

    /**
     * @param previousStopId the previousStopId to set
     */
    public void setPreviousStopId(Long previousStopId) {
        this.previousStopId = previousStopId;
    }

    /**
     * @return the distanceToNextStop
     */
    public Double getDistanceToNextStop() {
        return distanceToNextStop;
    }

    /**
     * @param distanceToNextStop the distanceToNextStop to set
     */
    public void setDistanceToNextStop(Double distanceToNextStop) {
        this.distanceToNextStop = distanceToNextStop;
    }

    /**
     * @return the distanceToPreviousStop
     */
    public Double getDistanceToPreviousStop() {
        return distanceToPreviousStop;
    }

    /**
     * @param distanceToPreviousStop the distanceToPreviousStop to set
     */
    public void setDistanceToPreviousStop(Double distanceToPreviousStop) {
        this.distanceToPreviousStop = distanceToPreviousStop;
    }

    /**
     * @return the terminal
     */
    public Boolean getTerminal() {
        return terminal;
    }

    /**
     * @param terminal the terminal to set
     */
    public void setTerminal(Boolean terminal) {
        this.terminal = terminal;
    }

    /**
     * @return the nextStopName
     */
    public String getNextStopName() {
        return nextStopName;
    }

    /**
     * @param nextStopName the nextStopName to set
     */
    public void setNextStopName(String nextStopName) {
        this.nextStopName = nextStopName;
    }
    
}
