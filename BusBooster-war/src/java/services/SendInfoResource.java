/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Bus;
import entity.User;
import entity.UserSimple;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import session.DataManagementSessionBeanLocal;
import session.FeedbackManagementSessionBeanLocal;

/**
 * REST Web Service
 *
 * @author chongyangsun
 */
@Path("sendInfo")
@RequestScoped
public class SendInfoResource {

    @Context
    private UriInfo context;
    
    @Inject
    DataManagementSessionBeanLocal dmsbl;
    
    @Inject
    FeedbackManagementSessionBeanLocal fmsbl;

    /**
     * Creates a new instance of SendInfoResource
     */
    public SendInfoResource() {
    }

    /**
     * Retrieves representation of an instance of services.SendInfoResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public UserSimple getJson(@QueryParam("busNo") String busNo, @QueryParam("busStopNo") String busStopNo, @QueryParam("direction") String direction) {
        User user = dmsbl.getBusNumberAndDirection(busNo, direction, busStopNo);
        if(user == null) {
            return null;
        }
        UserSimple result = new UserSimple(user.getId(), user.getUsername(), user.getBus().getId());
        System.out.println("userId="+result.getUserId()+" busId="+result.getBusId());
        return result;
    }
    
}
