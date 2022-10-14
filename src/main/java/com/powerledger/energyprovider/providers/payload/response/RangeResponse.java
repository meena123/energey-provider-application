package com.powerledger.energyprovider.providers.payload.response;

/**
 * @author Meena Shah
 */


import com.powerledger.energyprovider.providers.payload.request.ProviderRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RangeResponse {

    private Set<ProviderRequest> providerInfo;

    private Integer totalCapacity;

    private Integer averageCapacity;

}