package gr.codehub.webtechnikon.resources;

import gr.codehub.webtechnikon.exception.OwnerNotFoundException;
import gr.codehub.webtechnikon.model.PropertyOwner;
import gr.codehub.webtechnikon.services.PropertyOwnerServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/propertyOwners")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PropertyOwnerResource {

    @Inject
    private PropertyOwnerServiceImpl propertyOwnerService;

    @GET
    @Path("getbyid/{id}")
    public Response getById(@PathParam("id") Long id) {
        try {
            PropertyOwner owner = propertyOwnerService.get(id);
            return Response.ok(owner).build();
        } catch (OwnerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                       .entity("Owner with ID " + id + " not found.")
                       .build();
        }
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(PropertyOwner propertyOwner) {
        propertyOwnerService.create(
                propertyOwner.getFirstName(),
                propertyOwner.getLastName(), 
                propertyOwner.getEmail(), 
                propertyOwner.getUserName(), 
                propertyOwner.getPhoneNumber().toString(), 
                propertyOwner.getAddress(), 
                propertyOwner.getVat().toString(), 
                propertyOwner.getPassword());
        return Response.ok(propertyOwner).build();
    }

    @PUT
    @Path("updateby/{id}")
    public Response update(@PathParam("id") Long id, PropertyOwner propertyOwner) {
        try {
            propertyOwner.setId(id);  // Set the ID for the existing owner
            propertyOwnerService.update(propertyOwner);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("deleteby/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            propertyOwnerService.delete(id);
            return Response.noContent().build();
        } catch (OwnerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
