package com.portal.candidate.service;

import com.portal.candidate.dto.CandidateFlightBookingRequest;
import com.portal.candidate.dto.FlightBookingAcknowledgement;
import com.portal.candidate.entity.Candidate;
import com.portal.candidate.entity.PaymentInfo;
import com.portal.candidate.repository.CandidateDao;
import com.portal.candidate.repository.PaymentInfoRepository;
import com.portal.candidate.util.PaymentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FlightBookingService {

    @Autowired
    private CandidateDao cdo;
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Transactional
    public FlightBookingAcknowledgement bookFlightTicket(CandidateFlightBookingRequest request)
    {
        //FlightBookingAcknowledgement acknowledgement = null;
        Candidate candidateInfo = request.getCandidateInfo();
        candidateInfo = cdo.save(candidateInfo);

        PaymentInfo paymentInfo = request.getPaymentInfo();

        PaymentUtils.validateCreditLimit(paymentInfo.getAccountNo(), candidateInfo.getFare());

        paymentInfo.setPassengerId(paymentInfo.getPassengerId());

        paymentInfo.setAmount(candidateInfo.getFare());

        paymentInfoRepository.save(paymentInfo);

        return new FlightBookingAcknowledgement("Success", candidateInfo.getFare(), UUID.randomUUID().toString().split("-")[0], candidateInfo);

    }

}
