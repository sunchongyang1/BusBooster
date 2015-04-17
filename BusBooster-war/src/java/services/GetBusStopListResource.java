/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import entity.Bus;
import entity.BusRoute;
import entity.BusRouteSimple;
import entity.BusStop;
import entity.BusStopSimple;
import entity.Feedback;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import org.primefaces.json.JSONObject;
import session.FeedbackManagementSessionBeanLocal;
import session.PopulateDataManagementSessionBean;
import session.PopulateDataManagementSessionBeanLocal;
import session.PredictionManagementSessionBeanLocal;

/**
 * REST Web Service
 *
 * @author chongyangsun
 */
@Path("getBusStopList")
@RequestScoped
public class GetBusStopListResource {

    @Context
    private UriInfo context;
    
    @Inject
    PredictionManagementSessionBeanLocal pmsbl;
    @Inject
    FeedbackManagementSessionBeanLocal fmsbl;

    /**
     * Creates a new instance of GetBusStopListResource
     */
    public GetBusStopListResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetBusStopListResource
     * @return an List of bus stops
     */
    @GET
    @Produces("application/json")
    public List<BusRouteSimple> getBusRouteByStop(@QueryParam("busStopId") @DefaultValue("1") String busStopNo) {
        System.out.println("inside get bus route by stop");
        List<BusRoute> busRouteList = pmsbl.getBusRouteByBusStop(busStopNo);
        List<BusRouteSimple> result = new ArrayList();
//        System.out.println(busRouteList);
        for(BusRoute b: busRouteList) {
            
            
            BusRouteSimple temp = new BusRouteSimple(b.getId(), b.getBusNo(), b.getDirection());
            
            for(BusStop a: b.getBusStopList()) {
                temp.getBusStopIdList().add(a.getId());
            }
            
            
            //
            Bus bus = pmsbl.getNearestBus(busStopNo, busStopNo);
            if(bus == null) {
                temp.setArrivalTime(-1);
                temp.setNumberOfUserOnboard(0);
                temp.setDelay(0);
                temp.setBusBreakDown(Boolean.FALSE);
            } else {
                temp.setArrivalTime(pmsbl.getArrivalTime(busStopNo, busStopNo).intValue());
                temp.setNumberOfUserOnboard(bus.getNumberOfUserOnboard());
                temp.setDelay(fmsbl.getDelayFromFeedback(bus.getId()));
                temp.setBusBreakDown(fmsbl.isBusBreakDown(bus.getId()));
            }
            
            result.add(temp);
        }
        return result;
    }
}
