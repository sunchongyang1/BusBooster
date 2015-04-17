/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import session.SimulationSessionBeanLocal;

/**
 *
 * @author chongyangsun
 */
@Named(value = "simulationManagedBean")
@SessionScoped
public class SimulationManagedBean implements Serializable {
    
    @EJB
    SimulationSessionBeanLocal ssbl;
    
    private int dwell1;
    private int dwell2;
    private int dwell3;
    private int dwell4;
    private int dwell5;
    private int dwell6;
    private int dwell7;
    private int dwell8;
    private int dwell9;
    private int dwell10;
    private int dwell11;
    private int dwell12;
    private int dwell13;
    private int dwell14;
    private int dwell15;
    
    private int speed1;
    private int speed2;
    private int speed3;
    private int speed4;
    private int speed5;
    private int speed6;
    private int speed7;
    private int speed8;
    private int speed9;
    private int speed10;
    private int speed11;
    private int speed12;
    private int speed13;
    private int speed14;
    private int speed15;
    
    
    @PostConstruct
    public void init() {
        dwell1=0;
        dwell2=0;
        dwell3=50;
        dwell4=0;
        dwell5=30;
        dwell6=20;
        dwell7=10;
        dwell8=10;
        dwell9=60;
        dwell10=10;
        dwell11=0;
        dwell12=30;
        dwell13=10;
        dwell14=10;
        dwell15=0;
        
        speed1=10;
        speed2=10;
        speed3=12;
        speed4=12;
        speed5=8;
        speed6=8;
        speed7=5;
        speed8=8;
        speed9=10;
        speed10=10;
        speed11=8;
        speed12=10;
        speed13=5;
        speed14=10;
        speed15=10;
        
    }
    
    /**
     * Creates a new instance of SimulationManagedBean
     */
    public SimulationManagedBean() {
    }
    
    public void registerBus() {
        List<Integer> dwells = new ArrayList();
        dwells.add(dwell1);
        dwells.add(dwell2);
        dwells.add(dwell3);
        dwells.add(dwell4);
        dwells.add(dwell5);
        dwells.add(dwell6);
        dwells.add(dwell7);
        dwells.add(dwell8);
        dwells.add(dwell9);
        dwells.add(dwell10);
        dwells.add(dwell11);
        dwells.add(dwell12);
        dwells.add(dwell13);
        dwells.add(dwell14);
        dwells.add(dwell15);
        List<Integer> speeds = new ArrayList();
        speeds.add(speed1);
        speeds.add(speed2);
        speeds.add(speed3);
        speeds.add(speed4);
        speeds.add(speed5);
        speeds.add(speed6);
        speeds.add(speed7);
        speeds.add(speed8);
        speeds.add(speed9);
        speeds.add(speed10);
        speeds.add(speed11);
        speeds.add(speed12);
        speeds.add(speed13);
        speeds.add(speed14);
        speeds.add(speed15);
        if(ssbl.createNewBus("A1", "SCI-BIZ-PGP", dwells, speeds)) {
            this.faceMsg("Bus Register Successful!");
        } else {
            this.errorMsg("Error Register Bus");
        }
    }
    
    private void faceMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
//        LOGGER.info("MESSAGE INFO: " + message);
    }

    private void errorMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
//        LOGGER.info("MESSAGE INFO: " + message);
    }

    private void warnMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
//        LOGGER.info("MESSAGE INFO: " + message);
    }

    public int getDwell1() {
        return dwell1;
    }

    public void setDwell1(int dwell1) {
        this.dwell1 = dwell1;
    }

    public int getDwell2() {
        return dwell2;
    }

    public void setDwell2(int dwell2) {
        this.dwell2 = dwell2;
    }

    public int getDwell3() {
        return dwell3;
    }

    public void setDwell3(int dwell3) {
        this.dwell3 = dwell3;
    }

    public int getDwell4() {
        return dwell4;
    }

    public void setDwell4(int dwell4) {
        this.dwell4 = dwell4;
    }

    public int getDwell5() {
        return dwell5;
    }

    public void setDwell5(int dwell5) {
        this.dwell5 = dwell5;
    }

    public int getDwell6() {
        return dwell6;
    }

    public void setDwell6(int dwell6) {
        this.dwell6 = dwell6;
    }

    public int getDwell7() {
        return dwell7;
    }

    public void setDwell7(int dwell7) {
        this.dwell7 = dwell7;
    }

    public int getDwell8() {
        return dwell8;
    }

    public void setDwell8(int dwell8) {
        this.dwell8 = dwell8;
    }

    public int getDwell9() {
        return dwell9;
    }

    public void setDwell9(int dwell9) {
        this.dwell9 = dwell9;
    }

    public int getDwell10() {
        return dwell10;
    }

    public void setDwell10(int dwell10) {
        this.dwell10 = dwell10;
    }

    public int getDwell11() {
        return dwell11;
    }

    public void setDwell11(int dwell11) {
        this.dwell11 = dwell11;
    }

    public int getDwell12() {
        return dwell12;
    }

    public void setDwell12(int dwell12) {
        this.dwell12 = dwell12;
    }

    public int getDwell13() {
        return dwell13;
    }

    public void setDwell13(int dwell13) {
        this.dwell13 = dwell13;
    }

    public int getDwell14() {
        return dwell14;
    }

    public void setDwell14(int dwell14) {
        this.dwell14 = dwell14;
    }

    public int getDwell15() {
        return dwell15;
    }

    public void setDwell15(int dwell15) {
        this.dwell15 = dwell15;
    }

    public int getSpeed1() {
        return speed1;
    }

    public void setSpeed1(int speed1) {
        this.speed1 = speed1;
    }

    public int getSpeed2() {
        return speed2;
    }

    public void setSpeed2(int speed2) {
        this.speed2 = speed2;
    }

    public int getSpeed3() {
        return speed3;
    }

    public void setSpeed3(int speed3) {
        this.speed3 = speed3;
    }

    public int getSpeed4() {
        return speed4;
    }

    public void setSpeed4(int speed4) {
        this.speed4 = speed4;
    }

    public int getSpeed5() {
        return speed5;
    }

    public void setSpeed5(int speed5) {
        this.speed5 = speed5;
    }

    public int getSpeed6() {
        return speed6;
    }

    public void setSpeed6(int speed6) {
        this.speed6 = speed6;
    }

    public int getSpeed7() {
        return speed7;
    }

    public void setSpeed7(int speed7) {
        this.speed7 = speed7;
    }

    public int getSpeed8() {
        return speed8;
    }

    public void setSpeed8(int speed8) {
        this.speed8 = speed8;
    }

    public int getSpeed9() {
        return speed9;
    }

    public void setSpeed9(int speed9) {
        this.speed9 = speed9;
    }

    public int getSpeed10() {
        return speed10;
    }

    public void setSpeed10(int speed10) {
        this.speed10 = speed10;
    }

    public int getSpeed11() {
        return speed11;
    }

    public void setSpeed11(int speed11) {
        this.speed11 = speed11;
    }

    public int getSpeed12() {
        return speed12;
    }

    public void setSpeed12(int speed12) {
        this.speed12 = speed12;
    }

    public int getSpeed13() {
        return speed13;
    }

    public void setSpeed13(int speed13) {
        this.speed13 = speed13;
    }

    public int getSpeed14() {
        return speed14;
    }

    public void setSpeed14(int speed14) {
        this.speed14 = speed14;
    }

    public int getSpeed15() {
        return speed15;
    }

    public void setSpeed15(int speed15) {
        this.speed15 = speed15;
    }
    
}
