package com.sts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.entity.PlanDefinition;
import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;

@Repository
public interface PlanDefinitionRepository extends JpaRepository<PlanDefinition, Long> {
     Optional<PlanDefinition> findTopByPlanTypeAndPaymentTermOrderByEffectiveFromDesc(
        PlanType planType,
        String paymentTerm
    );
    Optional<PlanDefinition> findByPlanTypeAndPaymentTermAndActiveTrue(PlanType planType, PaymentTerm paymentTerm);
}
