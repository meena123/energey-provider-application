package com.powerledger.energyprovider.providers.repository;

/**
 * @author Meena Shah
 */

import java.util.Collection;
import java.util.Optional;

import com.powerledger.energyprovider.providers.models.BatteryInfo;
import com.powerledger.energyprovider.providers.models.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findByProviderName(String providerName);
    Optional<Provider> findById(Long id);
    @Query(
            value = "SELECT distinct a.id FROM provider_info a,battery_info b WHERE a.id = b.provider_id and watt_capacity >= ?1 and watt_capacity <= ?2",
            nativeQuery = true)
    Collection<Long> findAllProvider(Integer startRange, Integer endRange);
    Boolean existsByProviderName(String providerName);

    Boolean existsById(String providerId);
}
