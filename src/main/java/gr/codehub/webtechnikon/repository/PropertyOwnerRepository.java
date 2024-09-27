package gr.codehub.webtechnikon.repository;

import gr.codehub.webtechnikon.exception.OwnerNotFoundException;
import gr.codehub.webtechnikon.model.PropertyOwner;
import jakarta.enterprise.context.RequestScoped;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@RequestScoped
public class PropertyOwnerRepository implements Repository<PropertyOwner> {

    @PersistenceContext(unitName = "Persistence")
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(PropertyOwner propertyOwner) {
        entityManager.persist(propertyOwner);
    }

    @Override
    @Transactional
    public void update(PropertyOwner propertyOwner) throws PersistenceException {
        entityManager.merge(propertyOwner);
    }

    @Override
    @Transactional
    public void delete(PropertyOwner propertyOwner) throws IllegalArgumentException, NullPointerException {
        entityManager.remove(propertyOwner);
    }

    @Override
    public <V> Optional<PropertyOwner> findById(V id) {
        PropertyOwner owner = entityManager.find(PropertyOwner.class, id);
        if (owner.getIsActive() == false) {
            return Optional.empty();
        }
        return Optional.of(owner);
    }

    @Override
    @Transactional
    public List<PropertyOwner> findAll() {
        TypedQuery<PropertyOwner> query
                = entityManager.createQuery("SELECT po FROM PropertyOwner po WHERE po.isActive=true", PropertyOwner.class);
        return query.getResultList();
    }

    @Transactional
    public PropertyOwner searchByEmail(String email) {

        List<PropertyOwner> owner = entityManager.createQuery("SELECT po FROM PropertyOwner po WHERE po.email = :email", PropertyOwner.class)
                .setParameter("email", email)
                .getResultList();

        if (owner.isEmpty()) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        if (owner.get(0).getIsActive() == false) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        return (PropertyOwner) owner.get(0);
    }
    
    @Transactional
    public PropertyOwner searchByVat(Long vat) {

        List<PropertyOwner> owner = entityManager.createQuery("SELECT po FROM PropertyOwner po WHERE po.vat = :vat", PropertyOwner.class)
                .setParameter("vat", vat)
                .getResultList();

        if (owner.isEmpty()) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        if (owner.get(0).getIsActive() == false) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        return (PropertyOwner) owner.get(0);
    }
}
