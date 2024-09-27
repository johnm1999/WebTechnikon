package gr.codehub.webtechnikon.services;

import gr.codehub.webtechnikon.model.PropertyRepair;
import gr.codehub.webtechnikon.model.TypeOfRepairEnum;
import java.time.LocalDate;
import java.util.List;

public interface PropertyRepairService {
    void initiateRepair(Long ownerId, Long propertyId, TypeOfRepairEnum typeOfRepair, String shortDescription, String fullDescription);
    List<PropertyRepair> findRepairsInProgress(Long propertyId);
    List<PropertyRepair> findRepairsCompleted(Long propertyId);
    void deletePendingRepair(Long repairId);
 
    Boolean acceptRepair(PropertyRepair propertyRepair);
    void updateRepair(PropertyRepair propertyRepair);
    List<PropertyRepair> searchRepairsByDateRage(LocalDate startDate,LocalDate endDate);
    List<PropertyRepair> searchRepairsBySubmissionDate(LocalDate submissionDate);
    void softDelete(Long repairId);
}
