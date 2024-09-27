package gr.codehub.webtechnikon.services;

import gr.codehub.webtechnikon.model.PropertyOwner;
import java.util.List;

public interface PropertyOwnerService {

    PropertyOwner get(Long id);
    
    List<PropertyOwner> getAllOwners();

    PropertyOwner create(String firstName,
            String lastName,
            String email,
            String userName,
            String phoneNumber,
            String address,
            String vat,
            String password);

    void delete(Long id);
    
    void safeDelete(PropertyOwner propertyOwner);

    void update(PropertyOwner Owner);
    
    public PropertyOwner searchByVat(Long Vat);
    
    public PropertyOwner searchByEmail(String email);

}
