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
import javax.persistence.ManyToOne;

/**
 *
 * @author chongyangsun
 */
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String username;
    private String email;
    private String password;
    private Double contributionPoint;
    private Long currentOnBusId;
    private Long currentAtStopId;
    private Boolean onBoard;
    @ManyToOne
    private Bus bus;
    
    private String sessionId;
    
    public User(){}
    
    public User(String username) {
        this.setUsername(username);
    }
    
    public User(String username, String email, String password) {
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
        this.setContributionPoint(0.0);
        this.setOnBoard(Boolean.FALSE);
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.User[ id=" + id + " ]";
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the contributionPoint
     */
    public Double getContributionPoint() {
        return contributionPoint;
    }

    /**
     * @param contributionPoint the contributionPoint to set
     */
    public void setContributionPoint(Double contributionPoint) {
        this.contributionPoint = contributionPoint;
    }

    /**
     * @return the currentOnBusId
     */
    public Long getCurrentOnBusId() {
        return currentOnBusId;
    }

    /**
     * @param currentOnBusId the currentOnBusId to set
     */
    public void setCurrentOnBusId(Long currentOnBusId) {
        this.currentOnBusId = currentOnBusId;
    }

    /**
     * @return the currentAtStopId
     */
    public Long getCurrentAtStopId() {
        return currentAtStopId;
    }

    /**
     * @param currentAtStopId the currentAtStopId to set
     */
    public void setCurrentAtStopId(Long currentAtStopId) {
        this.currentAtStopId = currentAtStopId;
    }

    /**
     * @return the onBoard
     */
    public Boolean getOnBoard() {
        return onBoard;
    }

    /**
     * @param onBoard the onBoard to set
     */
    public void setOnBoard(Boolean onBoard) {
        this.onBoard = onBoard;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the bus
     */
    public Bus getBus() {
        return bus;
    }

    /**
     * @param bus the bus to set
     */
    public void setBus(Bus bus) {
        this.bus = bus;
    }
    
}
