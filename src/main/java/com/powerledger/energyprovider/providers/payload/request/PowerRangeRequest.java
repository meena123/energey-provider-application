package com.powerledger.energyprovider.providers.payload.request;

/**
 * @author Meena Shah
 */

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PowerRangeRequest {

    private Integer startRange;

    private Integer endRange;

}
