package com.powerledger.energyprovider.security.login.payload.request;

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
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;


}