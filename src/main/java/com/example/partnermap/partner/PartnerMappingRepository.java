package com.example.partnermap.partner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartnerMappingRepository extends JpaRepository<PartnerMapping, Long> {
    List<PartnerMapping> findAllByCustomerId(Long customerId);

    Optional<PartnerMapping> findByIdAndCustomerId(Long mappingId, Long customerId);
}
