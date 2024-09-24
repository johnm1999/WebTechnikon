package gr.codehub.webtechnikon.repository;

import gr.codehub.webtechnikon.model.PropertyOwner;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void create(PropertyOwner propertyOwner);
    
    void update(T t);

    void delete(T t);

    <V> Optional<T>  findById(V v);

    List<T> findAll();
}
