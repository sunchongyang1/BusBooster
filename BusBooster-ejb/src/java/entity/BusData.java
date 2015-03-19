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
public class BusData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String busNo;
    private String plateNo;
    private Double Longtitude;
    private Double Latitude;
    private Double speed;
    private Timestamp time;
    
    private Boolean archived;
    
    public BusData(){}
    
    public BusData(String busNo, String plateNo, Double longtitude, Double latitude, Double speed, Timestamp time){
        this.setBusNo(busNo);
        this.setPlateNo(plateNo);
        this.setLongtitude(longtitude);
        this.setLatitude(latitude);
        this.setSpeed(speed);
        this.setTime(time);
        this.setArchived(Boolean.FALSE);
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
        if (!(object instanceof BusData)) {
            return false;
        }
        BusData other = (BusData) object;
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
     * @return the Longtitude
     */
    public Double getLongtitude() {
        return Longtitude;
    }

    /**
     * @param Longtitude the Longtitude to set
     */
    public void setLongtitude(Double Longtitude) {
        this.Longtitude = Longtitude;
    }

    /**
     * @return the Latitude
     */
    public Double getLatitude() {
        return Latitude;
    }

    /**
     * @param Latitude the Latitude to set
     */
    public void setLatitude(Double Latitude) {
        this.Latitude = Latitude;
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
     * @return the time
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     * @return the archived
     */
    public Boolean getArchived() {
        return archived;
    }

    /**
     * @param archived the archived to set
     */
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
    
}
