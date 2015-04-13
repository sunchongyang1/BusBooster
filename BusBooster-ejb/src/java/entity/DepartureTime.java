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
import javax.persistence.ManyToOne;

/**
 *
 * @author chongyangsun
 */
@Entity
public class DepartureTime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private BusStop busStop;
    private Long busId;
    private Integer sequence;
    private Timestamp departureTime;
    private String busNo;
    
    public DepartureTime() {}
    
    public DepartureTime(BusStop busStop, Long busId, Integer sequence, Timestamp departureTime) {
        this.setBusStop(busStop);
        this.setBusId(busId);
        this.setSequence(sequence);
        this.setDepartureTime(departureTime);
    }
    
    public DepartureTime(BusStop busStop, Long busId, Timestamp departureTime) {
        this.setBusStop(busStop);
        this.setBusId(busId);
        this.setDepartureTime(departureTime);
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
        if (!(object instanceof DepartureTime)) {
            return false;
        }
        DepartureTime other = (DepartureTime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DepartureTime[ id=" + id + " ]";
    }

    /**
     * @return the busStop
     */
    public BusStop getBusStop() {
        return busStop;
    }

    /**
     * @param busStop the busStop to set
     */
    public void setBusStop(BusStop busStop) {
        this.busStop = busStop;
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

    /**
     * @return the departureTime
     */
    public Timestamp getDepartureTime() {
        return departureTime;
    }

    /**
     * @param departureTime the departureTime to set
     */
    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
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
    
}
