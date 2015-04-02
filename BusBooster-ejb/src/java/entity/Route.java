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
public class Route implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long startBusStopId;
    private Long endBusStopId;
    private Double distance;
    private Double travelTime;
    private Timestamp travelTimeUpdateTime;
    
    public Route() {}
    
    public Route(Long start, Long end, Double distance) {
        this.setStartBusStopId(start);
        this.setEndBusStopId(end);
        this.setDistance(distance);
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
        if (!(object instanceof Route)) {
            return false;
        }
        Route other = (Route) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Route[ id=" + id + " ]";
    }

    /**
     * @return the startBusStopId
     */
    public Long getStartBusStopId() {
        return startBusStopId;
    }

    /**
     * @param startBusStopId the startBusStopId to set
     */
    public void setStartBusStopId(Long startBusStopId) {
        this.startBusStopId = startBusStopId;
    }

    /**
     * @return the endBusStopId
     */
    public Long getEndBusStopId() {
        return endBusStopId;
    }

    /**
     * @param endBusStopId the endBusStopId to set
     */
    public void setEndBusStopId(Long endBusStopId) {
        this.endBusStopId = endBusStopId;
    }

    /**
     * @return the distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * @return the travelTime
     */
    public Double getTravelTime() {
        return travelTime;
    }

    /**
     * @param travelTime the travelTime to set
     */
    public void setTravelTime(Double travelTime) {
        this.travelTime = travelTime;
    }

    /**
     * @return the travelTimeUpdateTime
     */
    public Timestamp getTravelTimeUpdateTime() {
        return travelTimeUpdateTime;
    }

    /**
     * @param travelTimeUpdateTime the travelTimeUpdateTime to set
     */
    public void setTravelTimeUpdateTime(Timestamp travelTimeUpdateTime) {
        this.travelTimeUpdateTime = travelTimeUpdateTime;
    }
    
}
