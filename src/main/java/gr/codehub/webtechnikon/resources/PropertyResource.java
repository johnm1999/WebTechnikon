package gr.codehub.webtechnikon.resources;

import gr.codehub.webtechnikon.model.Property;
import gr.codehub.webtechnikon.services.PropertyServiceImpl;
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
import java.util.Optional;

@Path("/property")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PropertyResource {

    @Inject
    private PropertyServiceImpl propertyService;

    @GET
    @Path("getallproperties")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProperties() {
        return Response.ok(propertyService.getAll()).build();
    }

    @GET
    @Path("getbypropidnumber/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByPropertyIdNumber(@PathParam("id") Long id) {
        Optional<Property> property = propertyService.findByPropertyIdNumber(id);
        return Response.ok(property.get()).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Property property) {
        propertyService.createProperty(
                property.getPropertyId(),
                property.getAddress(),
                property.getYearOfConstruction(),
                property.getPropertyType(),
                property.getPropertyOwner().getId());
        return Response.ok(property).build();
    }

    @PUT
    @Path("updatebyId/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProperty(@PathParam("id") Long id, Property property) {
        property.setId(id);
        propertyService.update(property);
        return Response.ok(property).build();
    }

    @PUT
    @Path("safedeleteby/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response safeDelete(@PathParam("id") Long id) {
        Property property = propertyService.get(id);
        propertyService.softDeleteProperty(property);
        return Response.ok("Property with id:" + property.getId() + ":has been deleted").build();
    }

    @DELETE
    @Path("deleteby/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProperty(@PathParam("id") Long id){
        Property property = propertyService.get(id);
        propertyService.deleteProperty(id);
        return Response.ok("Property with id:" + property.getId() + ":has been deleted").build();
    }

}
