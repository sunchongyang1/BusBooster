/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author chongyangsun
 */
@Entity
public class Bus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String busNo;
    private String plateNo;//no use currently
    private String direction;
    private Double longitude;
    private Double latitude;
    private Double speed;
    private Timestamp lastUpdateTime;
    private Long previousStopId;
    private String previousStopName;
    private Long nextStopId;
    private String nextStopName;
    private Timestamp timeLeftLastStop;
    private Double distanceFromPreviousStop;
    private Double distanceToNextStop;
    
    private Double arrivalTime; // to next stop
    private Timestamp scheduledArrivingTime;
    private Double delay;
    
    private Double previousRouteTravelTime;
    
    public Bus(){}
    
    public Bus(String busNo, String direction, Double longitude, Double latitude, Timestamp lastUpdateTime, Long previousStopId){
        this.setBusNo(busNo);
        this.setDirection(direction);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setLastUpdateTime(lastUpdateTime);
        this.setTimeLeftLastStop(lastUpdateTime);
        this.setPreviousStopId(previousStopId);
        this.setNextStopId(previousStopId+1);
        this.setSpeed(0.0);
        this.setDistanceFromPreviousStop(0.0);
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
        if (!(object instanceof Bus)) {
            return false;
        }
        Bus other = (Bus) object;
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
     * @return the plateNo
     */
    public String getPlateNo() {
        return plateNo;
    }

    /**
     * @param plateNo the plateNo to set
     */
    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    /**
     * @return the speed
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    /**
     * @return the lastUpdateTime
     */
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
     * @return the timeLeftLastStop
     */
    public Timestamp getTimeLeftLastStop() {
        return timeLeftLastStop;
    }

    /**
     * @param timeLeftLastStop the timeLeftLastStop to set
     */
    public void setTimeLeftLastStop(Timestamp timeLeftLastStop) {
        this.timeLeftLastStop = timeLeftLastStop;
    }

    /**
     * @return the distanceFromPreviousStop
     */
    public Double getDistanceFromPreviousStop() {
        return distanceFromPreviousStop;
    }

    /**
     * @param distanceFromPreviousStop the distanceFromPreviousStop to set
     */
    public void setDistanceFromPreviousStop(Double distanceFromPreviousStop) {
        this.distanceFromPreviousStop = distanceFromPreviousStop;
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
     * @return the delay
     */
    public Double getDelay() {
        return delay;
    }

    /**
     * @param delay the delay to set
     */
    public void setDelay(Double delay) {
        this.delay = delay;
    }

    /**
     * @return the previousStopName
     */
    public String getPreviousStopName() {
        return previousStopName;
    }

    /**
     * @param previousStopName the previousStopName to set
     */
    public void setPreviousStopName(String previousStopName) {
        this.previousStopName = previousStopName;
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

    /**
     * @return the arrivalTime
     */
    public Double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @param arrivalTime the arrivalTime to set
     */
    public void setArrivalTime(Double arrivalTime) {
        this.arrivalTime = arrivalTime;
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
     * @return the previousRouteTravelTime
     */
    public Double getPreviousRouteTravelTime() {
        return previousRouteTravelTime;
    }

    /**
     * @param previousRouteTravelTime the previousRouteTravelTime to set
     */
    public void setPreviousRouteTravelTime(Double previousRouteTravelTime) {
        this.previousRouteTravelTime = previousRouteTravelTime;
    }

    /**
     * @return the scheduledArrivingTime
     */
    public Timestamp getScheduledArrivingTime() {
        return scheduledArrivingTime;
    }

    /**
     * @param scheduledArrivingTime the scheduledArrivingTime to set
     */
    public void setScheduledArrivingTime(Timestamp scheduledArrivingTime) {
        this.scheduledArrivingTime = scheduledArrivingTime;
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
    
}
