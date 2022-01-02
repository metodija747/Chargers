package si.fri.rso.charging.api.resources;


import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rso.charging.models.Chargers;
import si.fri.rso.charging.services.producers.ChargerBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Log
@RequestScoped
@Path("/chargers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS, PUT")
public class ElStationResource {

    @Inject
    private ChargerBean chargerBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Metered
    public Response getChargers() {

        List<Chargers> chargers = chargerBean.getChargers();

        return Response.ok(chargers).build();
    }

    @GET
    @Path("/{chargerId}")
    @Log
    @Timed
    public Response getChargers(@PathParam("chargerId") String chargerId) {

        Chargers charger = chargerBean.getChargers(chargerId);

        if (charger == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(charger).build();
    }
    @POST
    @Log
    @Timed
    public Response createCharger(Chargers charger) {

        if ((charger.getName() == null || charger.getName().isEmpty()) || (charger.getLocation() == null
                || charger.getLocation().isEmpty())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            charger = chargerBean.createCharger(charger);
        }

        if (charger.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(charger).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(charger).build();
        }
    }

    @PUT
    @Path("/{chargerId}")
    public Response putComment(@PathParam("chargerId") String chargerId, Chargers charger) {

        charger = chargerBean.putCharger(chargerId, charger);

        if (charger == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (charger.getId() != null)
                return Response.status(Response.Status.OK).entity(charger).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }
    @DELETE
    @Path("{chargerId}")
    @Log
    public Response deleteCustomer(@PathParam("chargerId") String chargerId) {

        boolean deleted = chargerBean.deleteCharger(chargerId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
