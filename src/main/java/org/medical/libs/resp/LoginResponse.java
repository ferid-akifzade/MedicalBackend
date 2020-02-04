package org.medical.libs.resp;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.medical.libs.OrdinaryUser;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private OrdinaryUser user;
    private String token;
}
