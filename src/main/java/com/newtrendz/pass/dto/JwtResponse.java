package com.newtrendz.pass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String id;
    private String name;
    private String email;
    private Long phoneNumber;
    private int status;
    private int deletedStatus;
    private String refreshToken;
    private String accessToken;

}
