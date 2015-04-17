/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import session.ServerManagementSessionBeanLocal;

/**
 *
 * @author chongyangsun
 */
@Named(value = "serverManagedBean")
@SessionScoped
public class ServerManagedBean implements Serializable {
    
    @EJB
    ServerManagementSessionBeanLocal smsbl;
    
    @PostConstruct
    public void init(){}
    
    /**
     * Creates a new instance of ServerManagedBean
     */
    public ServerManagedBean() {
    }
    
    public void startServer(){
        if(smsbl.startServer()) {
            this.faceMsg("Server Start Successful!");
        } else {
            this.errorMsg("Server Start Failed!");
        }
    }
    
    public void stopServer(){
        if(smsbl.stopServer()) {
            this.faceMsg("Server Stop Successful!");
        } else {
            this.errorMsg("Server Stop Failed!");
        }
    }
    
    private void faceMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
    }

    private void errorMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
    }

    private void warnMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
}
