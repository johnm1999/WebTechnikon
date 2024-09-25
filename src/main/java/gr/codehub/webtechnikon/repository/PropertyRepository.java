package gr.codehub.webtechnikon.repository;

import gr.codehub.webtechnikon.model.Property;
import gr.codehub.webtechnikon.model.PropertyOwner;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@RequestScoped
public class PropertyRepository implements Repository<Property> {

    @PersistenceContext(unitName = "Persistence")
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(Property property) {
        entityManager.persist(property);
    }

    @Override
    @Transactional
    public void update(Property property) {
        entityManager.merge(property);
    }

    @Override
    @Transactional
    public void delete(Property property) {
        entityManager.remove(property);
    }

    @Transactional
    public void softDelete(Property property) {
        property.setIsActive(false);
        entityManager.merge(property);
    }

    @Override
    public <V> Optional<Property> findById(V id) {
        Property property = entityManager.find(Property.class, id);
        if (property.getIsActive() == false) {
            return Optional.empty();
        }
        return Optional.of(property);
    }

    @Override
    @Transactional
    public List<Property> findAll() {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.isActive = true", Property.class);
        return query.getResultList();
    }

    @Transactional
    public Optional<Property> findByPropertyIdNumber(Long propertyId) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.propertyId = :propertyId AND p.isActive = true", Property.class);
        query.setParameter("propertyId", propertyId);
        List<Property> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    @Transactional
    public List<Property> findByOwnerVatNumber(Long vatNumber) {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.propertyOwner.vat = :vatNumber AND p.isActive = true", Property.class);
        query.setParameter("vatNumber", vatNumber);
        return query.getResultList();
    }

}
