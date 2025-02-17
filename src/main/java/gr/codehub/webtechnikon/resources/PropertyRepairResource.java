package gr.codehub.webtechnikon.resources;

import gr.codehub.webtechnikon.exception.InvalidInputException;
import gr.codehub.webtechnikon.model.AcceptRepairRequest;
import gr.codehub.webtechnikon.model.PropertyRepair;
import gr.codehub.webtechnikon.services.PropertyRepairService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Path("/propertyRepair")
@Slf4j
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
        propertyRepair.setOwnerAcceptance(accepted);

        return Response.ok("Owner Accept:" + propertyRepairService.acceptRepair(propertyRepair)).build();
    }

    @GET
    @Path("getinprogressbyid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInProgressById(@PathParam("id") Long id) {
        List<PropertyRepair> pr = propertyRepairService.findRepairsInProgress(id);
        return Response.ok(pr).build();
    }

    @GET
    @Path("getcompletedbyid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompletedById(@PathParam("id") Long id) {
        List<PropertyRepair> pr = propertyRepairService.findRepairsCompleted(id);
        return Response.ok(pr).build();
    }

    @DELETE
    @Path("delete")
    public Response delete(@PathParam("id") Long id) {
        propertyRepairService.deletePendingRepair(id);
        return Response.ok("DELETED").build();
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(PropertyRepair propertyRepair) {
        try {
            propertyRepairService.updateRepair(propertyRepair);
            return Response.ok(propertyRepair).build();
        } catch (InvalidInputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build(); 
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while updating the repair.").build(); 
        }
    }

    @GET
    @Path("/searchByDateRange")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchRepairsByDateRange(
            @QueryParam("startDate") String startDateStr,
            @QueryParam("endDate") String endDateStr) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        List<PropertyRepair> repairs = propertyRepairService.searchRepairsByDateRage(startDate, endDate);

        return Response.ok(repairs).build();
    }

    @GET
    @Path("/searchBysubmdate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchRepairsSumbissionDate(@QueryParam("submDate") String submDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sDate = LocalDate.parse(submDateStr, formatter);
        List<PropertyRepair> repairs = propertyRepairService.searchRepairsBySubmissionDate(sDate);
        return Response.ok(repairs).build();
    }

    @PUT
    @Path("safedeleteby/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response safeDelete(@PathParam("id") Long id) {
        propertyRepairService.softDelete(id);
        return Response.ok("Repair with id:" + id + "has been deleted").build();
    }
}
