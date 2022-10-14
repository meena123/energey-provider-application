package com.powerledger.energyprovider.providers.payload.request;

/**
 * @author Meena Shah
 */


import lombok.*;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatteryRequest {
    @NotBlank
    private String  batteryName;

    @NotBlank
    private String postCode;

    private Integer wattCapacity;

}