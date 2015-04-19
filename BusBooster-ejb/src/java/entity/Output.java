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
public class Output implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String busNo;
    private String busStopNo;
    private String busStopName;
    private Integer arrivalTimeHybrid;
    private Integer arrivalTimeBasic;
    private Integer arrivalTimeActual;
    private Integer varianceHybrid;
    private Integer varianceBasic;
    
    public Output(){}
    
    public Output(String busNo, String busStopNo, String busStopName, Integer arrivalTimeHybrid, Integer arrivalTimeBasic, 
            Integer arrivalTimeActual, Integer varianceHybrid, Integer varianceBasic) {
        this.setBusNo(busNo);
        this.setBusStopNo(busStopNo);
        this.setBusStopName(busStopName);
        this.setArrivalTimeActual(arrivalTimeActual);
        this.setArrivalTimeHybrid(arrivalTimeHybrid);
        this.setArrivalTimeBasic(arrivalTimeBasic);
        this.setVarianceBasic(varianceBasic);
        this.setVarianceHybrid(varianceHybrid);
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
        if (!(object instanceof Output)) {
            return false;
        }
        Output other = (Output) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Output[ id=" + id + " ]";
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
     * @return the arrivalTimeHybrid
     */
    public Integer getArrivalTimeHybrid() {
        return arrivalTimeHybrid;
    }

    /**
     * @param arrivalTimeHybrid the arrivalTimeHybrid to set
     */
    public void setArrivalTimeHybrid(Integer arrivalTimeHybrid) {
        this.arrivalTimeHybrid = arrivalTimeHybrid;
    }

    /**
     * @return the arrivalTimeBasic
     */
    public Integer getArrivalTimeBasic() {
        return arrivalTimeBasic;
    }

    /**
     * @param arrivalTimeBasic the arrivalTimeBasic to set
     */
    public void setArrivalTimeBasic(Integer arrivalTimeBasic) {
        this.arrivalTimeBasic = arrivalTimeBasic;
    }

    /**
     * @return the arrivalTimeActual
     */
    public Integer getArrivalTimeActual() {
        return arrivalTimeActual;
    }

    /**
     * @param arrivalTimeActual the arrivalTimeActual to set
     */
    public void setArrivalTimeActual(Integer arrivalTimeActual) {
        this.arrivalTimeActual = arrivalTimeActual;
    }

    /**
     * @return the varianceHybrid
     */
    public Integer getVarianceHybrid() {
        return varianceHybrid;
    }

    /**
     * @param varianceHybrid the varianceHybrid to set
     */
    public void setVarianceHybrid(Integer varianceHybrid) {
        this.varianceHybrid = varianceHybrid;
    }

    /**
     * @return the varianceBasic
     */
    public Integer getVarianceBasic() {
        return varianceBasic;
    }

    /**
     * @param varianceBasic the varianceBasic to set
     */
    public void setVarianceBasic(Integer varianceBasic) {
        this.varianceBasic = varianceBasic;
    }
    
}
