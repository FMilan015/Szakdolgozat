package org.szakdolgozat.szakdolgozatbackend.authentication.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.szakdolgozat.szakdolgozatbackend.model.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Role perm;
    private Long userID;
}
