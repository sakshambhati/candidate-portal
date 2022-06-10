package com.portal.candidate.dto;
// request will come from front end and on top of this we will add validation
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.PrivateKey;
import java.util.Date;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class CandidateRequest {

    private int id;
    @NotNull(message = "Candidate name should not be NULL")
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String job;
    @NotBlank
    private String mail;

    private String source;
    private String destination;
    private double fare;
    private Date travelDate;
}
