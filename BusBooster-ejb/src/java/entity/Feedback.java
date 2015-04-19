/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author chongyangsun
 */
@Entity
public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long userId;
    private String username;
    private String email;
    private Timestamp feedbackTime;
    private Boolean onboard;// no use
    private Long busId;
    private String busNo;
    private String direction;
    
    private Double latitude;
    private Double longitude;
    private Integer delay;
    
    private String comment;
    
    private Boolean isBusBreakDown;
    private Boolean trafficJam;
    
    public Feedback() {}
    
    public Feedback(Long userId, Long busId, String busNo, String direction, Double latitude, Double longitude) {
        this.setUserId(userId);
        this.setBusId(busId);
        this.setBusNo(busNo);
        this.setDirection(direction);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        Date now = new Date();
        Timestamp time = new Timestamp(now.getTime());
        this.setFeedbackTime(time);
        this.setOnboard(Boolean.TRUE);
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
        if (!(object instanceof Feedback)) {
            return false;
        }
        Feedback other = (Feedback) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Feedback[ id=" + id + " ]";
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the feedbackTime
     */
    public Timestamp getFeedbackTime() {
        return feedbackTime;
    }

    /**
     * @param feedbackTime the feedbackTime to set
     */
    public void setFeedbackTime(Timestamp feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    /**
     * @return the onboard
     */
    public Boolean getOnboard() {
        return onboard;
    }

    /**
     * @param onboard the onboard to set
     */
    public void setOnboard(Boolean onboard) {
        this.onboard = onboard;
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
     * @return the isBusBreakDown
     */
    public Boolean getIsBusBreakDown() {
        return isBusBreakDown;
    }

    /**
     * @param isBusBreakDown the isBusBreakDown to set
     */
    public void setIsBusBreakDown(Boolean isBusBreakDown) {
        this.isBusBreakDown = isBusBreakDown;
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
     * @return the trafficJam
     */
    public Boolean getTrafficJam() {
        return trafficJam;
    }

    /**
     * @param trafficJam the trafficJam to set
     */
    public void setTrafficJam(Boolean trafficJam) {
        this.trafficJam = trafficJam;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
}
