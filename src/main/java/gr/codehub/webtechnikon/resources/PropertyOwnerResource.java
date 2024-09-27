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
import lombok.extern.slf4j.Slf4j;

@Path("/propertyOwners")
@Slf4j
public class PropertyOwnerResource {

    @Inject
    private PropertyOwnerServiceImpl propertyOwnerService;

    @GET
    @Path("getall")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(propertyOwnerService.getAllOwners()).build();
    }

    @GET
    @Path("getbyid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        PropertyOwner owner = propertyOwnerService.get(id);
        return Response.ok(owner).build();
    }

    @GET
    @Path("getbyvat/{vat}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByVat(@PathParam("vat") Long vat) {
        PropertyOwner owner = propertyOwnerService.searchByVat(vat);
        return Response.ok(owner).build();
    }

    @GET
    @Path("getbyemail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByVat(@PathParam("email") String email) {
        PropertyOwner owner = propertyOwnerService.searchByEmail(email);
        return Response.ok(owner).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, PropertyOwner propertyOwner) {
        propertyOwner.setId(id);  // Set the ID for the existing owner
        propertyOwnerService.update(propertyOwner);
        return Response.ok(propertyOwner).build();
    }

    @PUT
    @Path("safedeleteby/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response safeDelete(@PathParam("id") Long id) {
        PropertyOwner owner = propertyOwnerService.get(id);
        propertyOwnerService.safeDelete(owner);
        return Response.ok("Owner with id:" + owner.getId() + "has been deleted").build();
    }

    @DELETE
    @Path("deleteby/{id}")
    public Response delete(@PathParam("id") Long id) {
        propertyOwnerService.delete(id);
        return Response.noContent().build();

    }
}
