/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
public class BusStop implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String busStopName;
    private String busStopNo;
    private Double longitude;
    private Double latitude;
    private Double distanceToNextStop; // in meter
    private Double distanceToPreviousStop; // in meter
    private Integer sequence; // bus sequence arrived at this stop;
    
    private List<Long> incomingRouteIdList;
    private List<Long> outgoingRouteIdList;
    
    private Boolean terminal;
    
    public BusStop(){}
    
    //only for populate bean
    public BusStop(String name, String stopNo, Double latitude, Double longitude, Double distanceToN, Double distanceToP, Boolean terminal) {
        this.setBusStopName(name);
        this.setBusStopNo(stopNo);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setDistanceToNextStop(distanceToN);
        this.setDistanceToPreviousStop(distanceToP);
        this.setTerminal(terminal);
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
     * @return the sequence
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the incomingRouteIdList
     */
    public List<Long> getIncomingRouteIdList() {
        return incomingRouteIdList;
    }

    /**
     * @param incomingRouteIdList the incomingRouteIdList to set
     */
    public void setIncomingRouteIdList(List<Long> incomingRouteIdList) {
        this.incomingRouteIdList = incomingRouteIdList;
    }

    /**
     * @return the outgoingRouteIdList
     */
    public List<Long> getOutgoingRouteIdList() {
        return outgoingRouteIdList;
    }

    /**
     * @param outgoingRouteIdList the outgoingRouteIdList to set
     */
    public void setOutgoingRouteIdList(List<Long> outgoingRouteIdList) {
        this.outgoingRouteIdList = outgoingRouteIdList;
    }
    
}
