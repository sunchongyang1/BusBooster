/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Feedback;
import entity.UserSimple;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import session.FeedbackManagementSessionBeanLocal;

/**
 * REST Web Service
 *
 * @author chongyangsun
 */
@Path("sendFeedback")
@RequestScoped
public class SendFeedbackResource {

    @Context
    private UriInfo context;

    @Inject
    FeedbackManagementSessionBeanLocal fmsbl;

    /**
     * Creates a new instance of SendFeedbackResource
     */
    public SendFeedbackResource() {
    }

    /**
     * Retrieves representation of an instance of services.SendFeedbackResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public UserSimple submitFeedback(@QueryParam("userId") Long userId, @QueryParam("busId") Long busId, @QueryParam("breakDown") String breakDown, @QueryParam("delay") Integer delay, @QueryParam("comment") String comment) {
        System.out.println("receive feedback! userId=" + userId + " busId=" + busId + " breakDown=" + breakDown + " delay=" + delay);
        boolean busBreakDown;
        if (breakDown.equals("Y")) {
            busBreakDown = true;
        } else {
            busBreakDown = false;
        }
        boolean trafficJam;
        if (delay > 0) {
            trafficJam = true;
        } else {
            trafficJam = false;
        }
        Feedback feedback = fmsbl.createFeedback(userId, busId, busBreakDown, trafficJam, delay, comment);
        if (feedback == null) {
            UserSimple result = new UserSimple();
            result.setUsername("Fail");
            return result;
        } else {
            UserSimple result = new UserSimple();
            result.setUsername("Success");
            return result;
        }
    }
}
