package com.powerledger.energyprovider.providers.payload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author Meena Shah
 */
@Getter
@Setter
@Builder
public class ProviderRequest {
    @NotBlank
    private String providerName;
    private Set<BatteryRequest> batteryInfo;
}
