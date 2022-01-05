package si.fri.rso.charging.api.resources;


import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.charging.models.Chargers;
import si.fri.rso.charging.services.producers.ChargerBean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Log
@ApplicationScoped
@Path("/chargers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS, PUT")
public class ElStationResource {

    @Inject
    private ChargerBean chargerBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get for all chargers information.", summary = "Get all chargers")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of chargers and their details",
                    content = @Content(schema = @Schema(implementation = Chargers.class, type = SchemaType.ARRAY))
            )})
    @GET
    @Metered
    public Response getChargers() {

        List<Chargers> chargers = chargerBean.getChargers();

        return Response.ok(chargers).build();
    }

    @Operation(description = "Get charger information.", summary = "Get details for specific charger")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Specific charger details",
                    content = @Content(
                            schema = @Schema(implementation = Chargers.class))
            ),
            @APIResponse(responseCode = "404", description = "Resource not found .")
    })
    @GET
    @Path("/{chargerId}")
    @Log
    @Timed
    public Response getCharger(@Parameter(description = "Charger ID.", required = true)
            @PathParam("chargerId") String chargerId) {

        Chargers charger = chargerBean.getCharger(chargerId);

        if (charger == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(charger).build();
    }

    @Operation(description = "Add new charger.", summary = "Add charger")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Charger successfully added."
            ),
            @APIResponse(responseCode = "400", description = "Bas request .")
    })
    @POST
    @Log
    @Timed
    public Response createCharger(@RequestBody(
            description = "DTO object with charger details.",
            required = true, content = @Content(
            schema = @Schema(implementation = Chargers.class))) Chargers charger) {

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


    @Operation(description = "Update charger details.", summary = "Update charger's info")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Details successfully updated."
            ),
            @APIResponse(responseCode = "400", description = "Bas request."),
            @APIResponse(responseCode = "304", description = "Resource not modified.")
    })
    @PUT
    @Path("/{chargerId}")
    public Response putComment(@Parameter(description = "Charger ID.", required = true)
            @PathParam("chargerId") String chargerId, Chargers charger) {

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

    @Operation(description = "Delete charger.", summary = "Delete charger")
    @APIResponses({
            @APIResponse(
                    responseCode = "410",
                    description = "Charger successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{chargerId}")
    @Log
    public Response deleteCustomer(@Parameter(description = "Charger ID.", required = true)
            @PathParam("chargerId") String chargerId) {

        boolean deleted = chargerBean.deleteCharger(chargerId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
