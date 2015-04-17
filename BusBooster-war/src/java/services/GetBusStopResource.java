/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.BusStop;
import entity.BusStopSimple;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.WebApplicationException;
import session.PopulateDataManagementSessionBeanLocal;
import session.PredictionManagementSessionBeanLocal;

/**
 * REST Web Service
 *
 * @author chongyangsun
 */
@Path("getBusStop")
@RequestScoped
public class GetBusStopResource {

    @Context
    private UriInfo context;
    
    @Inject
    PredictionManagementSessionBeanLocal pmsbl;
    @Inject
    PopulateDataManagementSessionBeanLocal pdmsbl;

    /**
     * Creates a new instance of GetBusStopResource
     */
    public GetBusStopResource() {
    }

    /**
     * Retrieves representation of an instance of services.GetBusStopResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces({"application/json","application/xml"})
    public List<BusStopSimple> getJson() {
        pdmsbl.populateBusStop();
//        System.out.println("after populate");
        List<BusStop> busStopList = pmsbl.getNearbyBusStop(1.293357, 103.775188);
        System.out.println(busStopList);
        List<BusStopSimple> busStopSimpleList = new ArrayList();
        for(BusStop b: busStopList) {
            BusStopSimple temp = new BusStopSimple(b.getId(), b.getBusStopName(), b.getBusStopNo(), b.getLatitude(), b.getLongitude(), 
            b.getDistanceToNextStop(), b.getDistanceToPreviousStop(), b.getArrivalSequence(), b.getDepartureSequence(), b.getTerminal());
            busStopSimpleList.add(temp);
//            System.out.println("busstop "+temp.getBusStopId());
        }
        
//        Gson gson = new Gson();
        
        if(busStopList.isEmpty()) {
            throw new WebApplicationException(404);
        } else {
//            System.out.println("i am here");
            return busStopSimpleList;
        }
    }

    /**
     * PUT method for updating or creating an instance of GetBusStopResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
