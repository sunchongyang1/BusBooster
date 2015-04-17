/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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
public class Bus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String busNo;
    private String direction;
    private Double longitude;
    private Double latitude;
    private Double speed;
    private Timestamp lastUpdateTime; //update time of bus info, eg, location speed
    private Timestamp timeLeftLastStop;
    private Double distanceFromPreviousStop;
    private Double distanceToNextStop;
    
    private BusStop previousStop;
    private BusStop nextStop;
    
    private Timestamp expectedArrivalTime;
    private Double delay; // from previous stop, only for 1st model
    
    private Double previousRouteTravelTime;
    
    @OneToMany
    private List<User> userList = new ArrayList();
    
    private Integer numberOfUserOnboard;
    
    public Bus(){}
    
    public Bus(String busNo, String direction, Double latitude, Double longitude, BusStop previousStop, BusStop nextStop){
        this.setBusNo(busNo);
        this.setDirection(direction);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        Date now = new Date();
        this.setLastUpdateTime(new Timestamp(now.getTime()));
        this.setPreviousStop(previousStop);
        this.setNextStop(nextStop);
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
     * @return the previousStop
     */
    public BusStop getPreviousStop() {
        return previousStop;
    }

    /**
     * @param previousStop the previousStop to set
     */
    public void setPreviousStop(BusStop previousStop) {
        this.previousStop = previousStop;
    }

    /**
     * @return the nextStop
     */
    public BusStop getNextStop() {
        return nextStop;
    }

    /**
     * @param nextStop the nextStop to set
     */
    public void setNextStop(BusStop nextStop) {
        this.nextStop = nextStop;
    }

    /**
     * @return the userList
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * @param userList the userList to set
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
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
     * @return the expectedArrivalTime
     */
    public Timestamp getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    /**
     * @param expectedArrivalTime the expectedArrivalTime to set
     */
    public void setExpectedArrivalTime(Timestamp expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }
    
}
