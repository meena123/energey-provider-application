package com.powerledger.energyprovider.providers.repository;

/**
 * @author Meena Shah
 */

import com.powerledger.energyprovider.providers.models.BatteryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;


@Repository
public interface BatteryRepository extends JpaRepository<BatteryInfo, Long> {
    Optional<BatteryInfo> findByBatteryName(String batteryName);

    @Query(
            value = "SELECT * FROM battery_info WHERE watt_capacity >= ?1 and watt_capacity <= ?2",
            nativeQuery = true)
    Collection<BatteryInfo> findAllBatteryInfo(Integer startRange, Integer endRange);
    @Query(
            value = "SELECT * FROM battery_info WHERE watt_capacity >= ?1 and watt_capacity <= ?2 and provider_id = ?3",
            nativeQuery = true)
    Collection<BatteryInfo> findAllBatteryInfoById(Integer startRange, Integer endRange,Long id);


    Boolean existsByBatteryName(String batteryName);

    boolean existsById(Long id);
}