package gr.codehub.webtechnikon.repository;

import gr.codehub.webtechnikon.exception.OwnerNotFoundException;
import gr.codehub.webtechnikon.model.PropertyRepair;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@RequestScoped
public class PropertyRepairRepository implements Repository<PropertyRepair> {

    @PersistenceContext(unitName = "Persistence")
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(PropertyRepair propertyRepair) {
        entityManager.persist(propertyRepair);
    }

    @Override
    @Transactional
    public void update(PropertyRepair propertyRepair) {
        entityManager.merge(propertyRepair);
    }

    @Override
    @Transactional
    public void delete(PropertyRepair propertyRepair) {
        entityManager.remove(propertyRepair);
    }

    @Transactional
    public List<PropertyRepair> searchByDateRange(LocalDate startDate, LocalDate endDate) {
        TypedQuery<PropertyRepair> query;
        query = entityManager.createQuery(
                "SELECT r FROM PropertyRepair r WHERE isActive = TRUE AND r.submissionDate BETWEEN :startDate AND :endDate", PropertyRepair.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        if (query.getResultList().isEmpty()) {
            throw new OwnerNotFoundException("There is no Repair between" + startDate + "and" + endDate);
        }
        return query.getResultList();
    }

    @Transactional
    public List<PropertyRepair> searchBySubmissionDate(LocalDate submissionDate) {
        TypedQuery<PropertyRepair> query = entityManager.createQuery(
                "SELECT r FROM PropertyRepair r WHERE r.submissionDate = :submissionDate AND isActive = TRUE", PropertyRepair.class);
        query.setParameter("submissionDate", submissionDate);
        if (query.getResultList().isEmpty()) {
            throw new OwnerNotFoundException("There is no Property Repair at" + submissionDate);
        }
        return query.getResultList();
    }

//    @Transactional
//    public List<PropertyRepair> searchByOwnerId(Long ownerId) {
//        TypedQuery<PropertyRepair> query = entityManager.createQuery(
//                "SELECT r FROM PropertyRepair r WHERE r.ownerId = :ownerId AND isActive = TRUE", PropertyRepair.class);
//        query.setParameter("ownerId", ownerId);
//        if (query.getResultList().isEmpty()) {
//            throw new OwnerNotFoundException("There is no Owner :" + ownerId);
//        }
//        return query.getResultList();
//    }
    
    @Override
    @Transactional
    public List<PropertyRepair> findAll() {
        TypedQuery<PropertyRepair> query
                = entityManager.createQuery("SELECT po FROM PropertyRepair po WHERE isActive = TRUE", PropertyRepair.class);
        if (query.getResultList().isEmpty()) {
            throw new OwnerNotFoundException("No Property Repair Found...");
        }
        return query.getResultList();
    }
    
    @Override
    public <V> Optional<PropertyRepair> findById(V id) {
        try {
            PropertyRepair repair = entityManager.find(PropertyRepair.class, id);
            return Optional.of(repair);
        } catch (Exception e) {
            log.debug("Property's repair not found");
        }
        return Optional.empty();
    }


}
