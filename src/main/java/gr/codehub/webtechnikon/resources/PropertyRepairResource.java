package gr.codehub.webtechnikon.resources;

import gr.codehub.webtechnikon.model.AcceptRepairRequest;
import gr.codehub.webtechnikon.model.PropertyRepair;
import gr.codehub.webtechnikon.services.PropertyRepairService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;

@Path("/propertyRepair")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PropertyRepairResource {

    @Inject
    private PropertyRepairService propertyRepairService;

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(PropertyRepair propertyRepair) {
        propertyRepairService.initiateRepair(
                propertyRepair.getProperty().getId(),//it's the id of the property
                propertyRepair.getTypeOfRepair(),
                propertyRepair.getShortDescription(),
                propertyRepair.getDescription());
        return Response.ok(propertyRepair).build();
    }

    @PUT
    @Path("acceptRepair")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptRepairResponse(AcceptRepairRequest request) {
        Long id = request.getId();
        boolean accepted = request.isAccepted();
        
        PropertyRepair propertyRepair = propertyRepairService.getById(id);

        propertyRepairService.acceptRepair(propertyRepair);
        
        return Response.ok(propertyRepair).build();
    }
}
