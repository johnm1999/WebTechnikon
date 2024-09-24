package gr.codehub.webtechnikon.services;

import gr.codehub.webtechnikon.model.PropertyOwner;

public interface PropertyOwnerService {

    PropertyOwner get(Long id);

    PropertyOwner create(String firstName,
            String lastName,
            String email,
            String userName,
            String phoneNumber,
            String address,
            String vat,
            String password);

    void delete(Long id);

    void update(PropertyOwner Owner);

}
