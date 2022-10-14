package com.powerledger.energyprovider.security.login.payload.request;

/**
 * @author Meena Shah
 */


import lombok.*;

import java.util.Set;

import javax.validation.constraints.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;


}
