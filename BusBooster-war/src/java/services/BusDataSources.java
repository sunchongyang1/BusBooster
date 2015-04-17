/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Bus;
import entity.BusSimple;
import entity.BusStop;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import session.BusManagementSessionBeanLocal;
import session.FeedbackManagementSessionBeanLocal;

/**
 * REST Web Service
 *
 * @author chongyangsun
 */
@Path("buses")
@RequestScoped
public class BusDataSources {

    @Context
    private UriInfo context;
    
    @Inject
    BusManagementSessionBeanLocal bmsbl;
    @Inject
    FeedbackManagementSessionBeanLocal fmsbl;

    /**
     * Creates a new instance of BusDataSources
     */
    public BusDataSources() {
    }

    /**
     * Retrieves representation of an instance of services.BusDataSources
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public List<BusSimple> getJson() {
        //TODO return proper representation object
        List<Bus> buses = bmsbl.getAllBuses();
        List<BusSimple> result = new ArrayList();
        for(Bus b: buses) {
            BusSimple temp = new BusSimple(b.getBusNo(), b.getDirection(), b.getLatitude(), b.getLongitude(), 0, fmsbl.getDelayFromFeedback(b.getId()), fmsbl.isBusBreakDown(b.getId()), b.getNumberOfUserOnboard());
            result.add(temp);
        }
        return result;
    }

}
