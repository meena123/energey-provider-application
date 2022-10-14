package com.powerledger.energyprovider.security.login.payload.response;

/**
 * @author Meena Shah
 */

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    private String jwtToken;

}
