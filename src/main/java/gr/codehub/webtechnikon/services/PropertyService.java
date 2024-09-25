package gr.codehub.webtechnikon.services;

import gr.codehub.webtechnikon.model.Property;
import gr.codehub.webtechnikon.model.PropertyType;
import java.util.List;
import java.util.Optional;

public interface PropertyService {

    Property createProperty(Long propertyIdNumber, String address, int yearOfConstruction, PropertyType propertyType, Long propertyOwnerId);

    void update(Property updateProperty);

    Optional<Property> findByPropertyIdNumber(Long propertyIdNumber);

    List<Property> findByOwnerVatNumber(Long vatNumber);

    void deleteProperty(Long id);

    void softDeleteProperty(Property property);
}
