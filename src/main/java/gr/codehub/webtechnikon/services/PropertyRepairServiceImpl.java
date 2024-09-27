package gr.codehub.webtechnikon.services;

import gr.codehub.webtechnikon.exception.InvalidInputException;
import gr.codehub.webtechnikon.exception.OwnerNotFoundException;
import gr.codehub.webtechnikon.exception.PropertyNotFoundException;
import gr.codehub.webtechnikon.exception.PropertyOwnerExistsException;
import gr.codehub.webtechnikon.model.Property;
import gr.codehub.webtechnikon.model.PropertyRepair;
import gr.codehub.webtechnikon.model.StatusOfRepairEnum;
import gr.codehub.webtechnikon.model.TypeOfRepairEnum;
import gr.codehub.webtechnikon.repository.PropertyRepairRepository;
import gr.codehub.webtechnikon.repository.PropertyRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class PropertyRepairServiceImpl implements PropertyRepairService {

    //auto logika den tha to xreiastw
//    @Inject
//    private PropertyOwnerRepository propertyOwnerRepository;
    @Inject
    private PropertyRepairRepository propertyRepairRepository;

    @Inject
    private PropertyRepository propertyRepository;

    @Override
    public void initiateRepair(Long propertyId, TypeOfRepairEnum typeOfRepair, String shortDescription, String fullDescription) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property with id " + propertyId + " not found"));

        PropertyRepair repair = PropertyRepair.builder()
                .shortDescription(shortDescription)
                .property(property)
                .typeOfRepair(typeOfRepair)
                .submissionDate(LocalDate.now())
                .description(fullDescription)
                .status(StatusOfRepairEnum.PENDING)
                .isActive(true)
                .build();
        propertyRepairRepository.create(repair);
    }

    @Override
    public Boolean acceptRepair(PropertyRepair propertyRepair) throws PropertyOwnerExistsException {
        if (propertyRepair != null) {
            if (propertyRepair.isOwnerAcceptance() == true) {
                propertyRepair.setStatus(StatusOfRepairEnum.INPROGRESS);
                propertyRepair.setActualStartDate(propertyRepair.getProposedStartDate());
                propertyRepair.setActualEndDate(propertyRepair.getProposedEndDate());
                propertyRepairRepository.update(propertyRepair);
                return true;
            }
            propertyRepair.setStatus(StatusOfRepairEnum.DECLINED);
            /*-------------------Stay null--------------*/
            propertyRepairRepository.update(propertyRepair);
        } else {
            throw new OwnerNotFoundException("There is no property to repair");
        }
        return false;
    }

    @Override
    public List<PropertyRepair> findRepairsInProgress(Long propertyId) {
        return propertyRepairRepository.findAll().stream()
                .filter(repair -> StatusOfRepairEnum.INPROGRESS.equals(repair.getStatus()) && propertyId.equals(repair.getProperty().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyRepair> findRepairsCompleted(Long propertyId) {
        return propertyRepairRepository.findAll().stream()
                .filter(repair -> StatusOfRepairEnum.COMPLETE.equals(repair.getStatus()) && propertyId.equals(repair.getProperty().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deletePendingRepair(Long repairId) {
        propertyRepairRepository.findById(repairId).ifPresent(repair -> {
            if (StatusOfRepairEnum.PENDING.equals(repair.getStatus())) {
                propertyRepairRepository.delete(repair);
            }
        });
    }

    @Override
    public void updateRepair(PropertyRepair propertyRepair) {
        if (propertyRepair.getStatus().equals(StatusOfRepairEnum.PENDING) && propertyRepair.getIsActive()) {
            propertyRepairRepository.update(propertyRepair);//gia to resources tha ginetai o katallilos elegxos
        } else {
            throw new InvalidInputException("Your input is Invalid");
        }
    }

    @Override
    public List<PropertyRepair> searchRepairsByDateRage(LocalDate startDate, LocalDate endDate) {
        return propertyRepairRepository.searchByDateRange(startDate, endDate);
    }

    @Override
    public List<PropertyRepair> searchRepairsBySubmissionDate(LocalDate submissionDate) {
        return propertyRepairRepository.searchBySubmissionDate(submissionDate);
    }

    @Override
    public void softDelete(Long repairId) throws PropertyOwnerExistsException {
        PropertyRepair repair = propertyRepairRepository.findById(repairId)
                .orElseThrow(() -> new IllegalStateException("Repair with id " + repairId + " does not exist"));

        if (repair.getStatus().equals(StatusOfRepairEnum.PENDING)) {
            repair.setIsActive(false);
            propertyRepairRepository.update(repair);
        } else {
            throw new IllegalStateException("Repair is not pending and cannot be deleted");
        }
    }

    @Override
    public PropertyRepair getById(Long id) {
        Optional<PropertyRepair> pr = propertyRepairRepository.findById(id);
        if (pr.isEmpty()) {
            throw new OwnerNotFoundException("No Property Found");
        }else{
            return pr.get();
        }
    }

}
