package com.powerledger.energyprovider.providers.controllers;

import com.powerledger.energyprovider.providers.payload.request.PowerRangeRequest;
import com.powerledger.energyprovider.providers.payload.request.ProviderRequest;
import com.powerledger.energyprovider.providers.payload.response.ProviderResponse;
import com.powerledger.energyprovider.providers.services.BatteryService;
import com.powerledger.energyprovider.security.login.repository.RoleRepository;
import com.powerledger.energyprovider.security.login.repository.UserRepository;
import com.powerledger.energyprovider.security.login.security.jwt.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;



/**
 * @author Meena Shah
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/provider")
public class BatteryController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    BatteryService service;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/addBatteries")
    public ResponseEntity<?> addBatteries(
                                          HttpServletRequest request, @RequestBody ProviderRequest providerRequest) {
        ProviderResponse providerResponse = new ProviderResponse();
        boolean validUser = validateUser(request);
        if(validUser){
            return ResponseEntity.ok().body(service.createProviders(providerRequest));
        }else{
            providerResponse.setMessage("Invalid Token!");
            return ResponseEntity.ok().body(providerResponse);
        }
    }
    @PostMapping("/powerRange")
    public ResponseEntity<?>  getRangeResponse(HttpServletRequest request, @RequestBody PowerRangeRequest powerRangeRequest) {
        boolean validUser = validateUser(request);
        ProviderResponse providerResponse = new ProviderResponse();

        if(validUser){
            return ResponseEntity.ok().body(service.getRangeResponse(powerRangeRequest));
        }else{
            providerResponse.setMessage("Invalid Token!");

            return ResponseEntity.ok().body(providerResponse);
        }
    }
    private boolean validateUser(HttpServletRequest request) {

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtils.getUsernameFromToken(jwtToken);
                if(username != null && jwtUtils.validateToken(jwtToken,username)){
                    return true;
                }else {
                    return false;
                }
            }catch (IllegalArgumentException e) {
                throw new RuntimeException("Unable to get JWT Token");

            } catch (ExpiredJwtException e) {
                throw new RuntimeException("JWT Token has expired");
            }
        }else return false;
    }


}
