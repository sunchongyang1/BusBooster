/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entity.Bus;
import entity.BusStop;
import java.sql.Timestamp;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author chongyangsun
 */
@Path("updates")
public class BusDataSources {

    @Context
    private UriInfo context;

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
    public Bus getJson() {
        //TODO return proper representation object
        return new Bus("Bus 1", "AAAA", new Double(0), new Double(0), new BusStop(), new BusStop());
//        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of BusDataSources
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
