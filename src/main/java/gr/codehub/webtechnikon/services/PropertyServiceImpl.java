package gr.codehub.webtechnikon.services;

import gr.codehub.webtechnikon.exception.DuplicateEntryException;
import gr.codehub.webtechnikon.exception.InvalidInputException;
import gr.codehub.webtechnikon.exception.OwnerNotFoundException;
import gr.codehub.webtechnikon.exception.ResourceNotFoundException;
import gr.codehub.webtechnikon.model.Property;
import gr.codehub.webtechnikon.model.PropertyOwner;
import gr.codehub.webtechnikon.model.PropertyType;
import gr.codehub.webtechnikon.repository.PropertyOwnerRepository;
import gr.codehub.webtechnikon.repository.PropertyRepository;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import static javax.swing.UIManager.get;
import lombok.Data;

@Data
public class PropertyServiceImpl implements PropertyService {

    @Inject
    private PropertyRepository propertyRepository;

    @Inject
    private PropertyOwnerRepository propertyOwnerRepository;

    @Override
    public Optional<Property> findByPropertyIdNumber(Long propertyIdNumber) {
        return propertyRepository.findByPropertyIdNumber(propertyIdNumber);
    }

    public List<Property> getAll(){
        return propertyRepository.findAll();
    }

    @Override
    public List<Property> findByOwnerVatNumber(Long vatNumber) {
        return propertyRepository.findByOwnerVatNumber(vatNumber);
    }

    @Override
    public Property createProperty(Long propertyIdNumber, String address, int yearOfConstruction, PropertyType propertyType, Long propertyOwnerId) {
        //check if owner exists
        Optional<PropertyOwner> owner = propertyOwnerRepository.findById(propertyOwnerId);
        if (!owner.isPresent()) {
            throw new OwnerNotFoundException("Owner with ID " + propertyOwnerId + " does not exist.");
        }
        Optional<Property> existingProperty = propertyRepository.findByPropertyIdNumber(propertyIdNumber);

        if (existingProperty.isPresent()) {
            throw new DuplicateEntryException("Property with ID " + propertyIdNumber + " already exists.");
        }

        Property property = Property.builder()
                .propertyId(propertyIdNumber)
                .address(address)
                .yearOfConstruction(yearOfConstruction)
                .propertyType(propertyType)
                .propertyOwner(owner.get())
                .isActive(true)
                .build();
        propertyRepository.create(property);
        return property;
    }

    //edw isws na eexei kapoio lathos an den treksei na to dw SOSSS.
    //Na valw kai mesa sto if to owner service
    @Override
    public void update(Property updateProperty) throws InvalidInputException {
        Property existProperty = (Property) get(updateProperty.getId());
        if (!existProperty.getIsActive() || existProperty == null) {
            throw new ResourceNotFoundException("Property with ID " + updateProperty.getId() + " does not exist or is inactive.");
        }
        existProperty.setPropertyId(updateProperty.getPropertyId());
        existProperty.setAddress(updateProperty.getAddress());
        existProperty.setYearOfConstruction(updateProperty.getYearOfConstruction());
        existProperty.setPropertyType(updateProperty.getPropertyType());

        propertyRepository.update(existProperty);

    }

    @Override
    public void softDeleteProperty(Property property) {
        Property existProperty = (Property) get(property.getId());
        existProperty.setIsActive(false);
        propertyRepository.update(existProperty);
    }

    @Override
    public void deleteProperty(Long id) {
        Property existProperty = (Property) get(id);
        try {
            propertyRepository.delete(existProperty);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResourceNotFoundException("Property with ID " + id + " does not exist or is inactive.");
        }
    }
}
