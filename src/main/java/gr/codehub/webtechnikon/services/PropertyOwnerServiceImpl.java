package gr.codehub.webtechnikon.services;

import gr.codehub.webtechnikon.exception.InvalidInputException;
import gr.codehub.webtechnikon.exception.MissingInputException;
import gr.codehub.webtechnikon.exception.OwnerNotFoundException;
import gr.codehub.webtechnikon.exception.PropertyOwnerExistsException;
import gr.codehub.webtechnikon.model.PropertyOwner;
import gr.codehub.webtechnikon.repository.PropertyOwnerRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public class PropertyOwnerServiceImpl implements PropertyOwnerService {

    @Inject
    private PropertyOwnerRepository propertyOwnerRepository;

    private PropertyOwner propertyOwner;

    @Override
    public List<PropertyOwner> getAllOwners() {
        return propertyOwnerRepository.findAll();
    }

    @Override
    public PropertyOwner get(Long id) throws OwnerNotFoundException {
        Optional<PropertyOwner> optionalPropertyOwner = propertyOwnerRepository.findById(id);

        if (optionalPropertyOwner.isEmpty()) {
            throw new OwnerNotFoundException("This is not an existing owner");
        } else {
            return optionalPropertyOwner.get();
        }
    }

    @Override
    public PropertyOwner searchByVat(Long Vat){
        PropertyOwner propertyOwner = propertyOwnerRepository.searchByVat(Vat);
        if (propertyOwner == null) {
            throw new OwnerNotFoundException("This is not an existing owner");
        } else {
            return propertyOwner;
        }
    }
    
    @Override
    public PropertyOwner searchByEmail(String email){
        PropertyOwner propertyOwner = propertyOwnerRepository.searchByEmail(email);
        if (propertyOwner == null) {
            throw new OwnerNotFoundException("This is not an existing owner");
        } else {
            return propertyOwner;
        }
    }

    @Override
    public PropertyOwner create(String firstName,
            String lastName,
            String email,
            String userName,
            String phoneNumber,
            String address,
            String vat,
            String password) throws PropertyOwnerExistsException, InvalidInputException, MissingInputException {
        try {
            if (!PatternService.EMAIL_PATTERN.matcher(email.trim()).matches()) {
                throw new InvalidInputException("This is not a valid email");
            }
            if (!PatternService.PASSWORD_PATTERN.matcher(password.trim()).matches()) {
                throw new InvalidInputException("This is not a valid password");
            }
            if (!PatternService.VAT_PATTERN.matcher(vat.trim()).matches()) {
                throw new InvalidInputException("This is not a valid vat number");
            }
            if (!PatternService.PHONE_NUMBER_PATTERN.matcher(phoneNumber.trim()).matches()) {
                throw new InvalidInputException("This is not a valid phone number");
            }

            propertyOwner = PropertyOwner.builder()
                    .vat(Long.valueOf(vat))
                    .phoneNumber(Long.valueOf(phoneNumber))
                    .address(address)
                    .firstName(firstName)
                    .lastName(lastName)
                    .password(password)
                    .userName(userName)
                    .email(email)
                    .isActive(true)
                    .build();

            // throws a persistence exception
            propertyOwnerRepository.create(propertyOwner);
            return propertyOwner;
        } catch (PersistenceException e) {
            // a null value was passed
            if (e.getMessage().contains("PropertyValueException")) {
                throw new MissingInputException("There's input missing");

                // the user already exists
            } else {
                throw new PropertyOwnerExistsException("This user already exists");
            }
        }
    }

    @Override
    public void delete(Long owner_Id) throws OwnerNotFoundException {
        PropertyOwner owner = get(owner_Id);
        if(owner != null){
            propertyOwnerRepository.delete(owner);
        }else{
            throw new OwnerNotFoundException("This is not an existing user");
        }
    }

    @Override
    public void safeDelete(PropertyOwner propertyOwner) throws PersistenceException {
        PropertyOwner existingOwner = get(propertyOwner.getId());
        existingOwner.setIsActive(false);
        propertyOwnerRepository.update(existingOwner);
    }

    @Override
    public void update(PropertyOwner updatedPropertyOwner) throws InvalidInputException, PropertyOwnerExistsException {
        PropertyOwner existingOwner = get(updatedPropertyOwner.getId());
        if (!existingOwner.getIsActive()) {
            throw new OwnerNotFoundException("Owner with id " + updatedPropertyOwner.getId() + " not found.");
        }
        existingOwner.setVat(updatedPropertyOwner.getVat());
        existingOwner.setFirstName(updatedPropertyOwner.getFirstName());
        existingOwner.setLastName(updatedPropertyOwner.getLastName());
        existingOwner.setAddress(updatedPropertyOwner.getAddress());
        existingOwner.setPhoneNumber(updatedPropertyOwner.getPhoneNumber());
        existingOwner.setEmail(updatedPropertyOwner.getEmail());
        existingOwner.setUserName(updatedPropertyOwner.getUserName());
        existingOwner.setPassword(updatedPropertyOwner.getPassword());

        propertyOwnerRepository.update(existingOwner);
    }

}
