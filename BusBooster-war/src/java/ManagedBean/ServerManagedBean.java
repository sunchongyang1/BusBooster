/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import entity.Output;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    
    private List<Output> outputs;
    
    @PostConstruct
    public void init(){
        outputs = new ArrayList();
    }
    
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
    
    public void refreshOutput() {
        outputs = smsbl.getOutput();
        System.out.println("managedbean: outputs "+outputs);
        if(outputs == null) {
            this.errorMsg("Update failed!");
        } else {
            this.faceMsg("Update success!");
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

    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }
    
}
