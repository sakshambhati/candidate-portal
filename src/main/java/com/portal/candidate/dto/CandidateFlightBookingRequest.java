package com.portal.candidate.dto;

import com.portal.candidate.entity.Candidate;
import com.portal.candidate.entity.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateFlightBookingRequest {

    private Candidate candidateInfo;
    private PaymentInfo paymentInfo;

}
