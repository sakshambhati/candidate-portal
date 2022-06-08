package com.portal.candidate.service;

import com.portal.candidate.dto.CandidateRequest;
import com.portal.candidate.entity.Candidate;
import com.portal.candidate.exception.CandidateNotFoundException;
import com.portal.candidate.exception.UpdationFailedException;
import com.portal.candidate.model.EmailRequest;
import com.portal.candidate.repository.CandidateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableAsync
public class CandidateService {

    @Autowired
    private CandidateDao cdo;
    @Autowired
    private CandidateService cds;
    @Autowired
    private EmailRequest emailRequest;
    @Autowired
    private EmailService emailService;

    Logger log = LoggerFactory.getLogger(CandidateService.class);

    public Candidate saveCandidate(CandidateRequest candidateRequest) throws UpdationFailedException {
        // Candidate candidate = Candidate.build(0, candidateRequest.getName(),
        //        candidateRequest.getAddress(), candidateRequest.getJob(), candidate.);

        Candidate candidate = new Candidate();
        synchronized (candidate) {
            candidate.setId(candidateRequest.getId());
            candidate.setName(candidateRequest.getName());
            candidate.setAddress(candidateRequest.getAddress());
            candidate.setJob(candidateRequest.getJob());
            candidate.setMail(candidateRequest.getMail());
        }

        emailRequest.setSubject("Registration");
        emailRequest.setMessage(candidate.getName() + " has been added to Portal");
        emailRequest.setTo(candidate.getMail());
        String email = emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getMessage());
        log.info("Email Response {} ", email);

        return cdo.save(candidate);

    }

    // save list
    public List<Candidate> saveCandidates(List<Candidate> candidates) throws UpdationFailedException {
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            emailRequest.setSubject("Registration");
            emailRequest.setMessage(c.getName() + " has been added to Portal");
            emailRequest.setTo(c.getMail());

            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getMessage());
            log.info("Email Response {} ", c);
        }
        return cdo.saveAll(candidates);
    }

    // fetch list
    public List<Candidate> getCandidates() {
        return cdo.findAll();
    }

    public Candidate getCandidateById(int id) throws CandidateNotFoundException {
        Candidate candidate = cdo.findById(id).orElse(null);
        log.debug("Request {}", candidate);
        if (candidate != null) {
            log.debug("Response {}", candidate);
            return candidate;
        } else {
            throw new CandidateNotFoundException("No user with ID " + id);
        }
    }

    public String deleteCandidate(int id) throws CandidateNotFoundException {
        Candidate existingCandidate = cdo.findById(id).orElseThrow(() -> new CandidateNotFoundException(
                "User not found with id : " + id));
        log.debug("Request {}", existingCandidate);
        cdo.deleteById(id);
        log.debug("Response {}", "deleted");
        return "Deleted " + id;
    }

    public Candidate updateCandidate(Candidate candidate) throws CandidateNotFoundException, UpdationFailedException {
        Candidate existingCandidate = cdo.findById(candidate.getId()).orElseThrow(() -> new CandidateNotFoundException(
                "User not found with id : " + candidate.getId()));
        log.debug("Request {}", existingCandidate);

        synchronized (existingCandidate) {
            existingCandidate.setName(candidate.getName());
            existingCandidate.setAddress(candidate.getAddress());
            existingCandidate.setMail(candidate.getMail());
        }
        log.debug("Response {}", existingCandidate);

        emailRequest.setSubject("Updation");
        emailRequest.setMessage("Details for id " + existingCandidate.getId() + " have been updated");
        emailRequest.setTo(existingCandidate.getMail());
        String email = emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getMessage());
        log.info("Email Response {} ", email);

        return cdo.save(existingCandidate);
    }

    // fetch details using paging
    public List<Candidate> getPagedCandidates(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        log.debug("Request {}", paging);

        Page<Candidate> pagedResult = cdo.findAll(paging);
        log.debug("Response {}", pagedResult);
        return pagedResult.toList();
    }

    public List<Candidate> getCandidateByName(String name) throws CandidateNotFoundException {
        List<Candidate> existingCandidate = cdo.findByName(name);
        log.debug("Request {}", existingCandidate);

        if (existingCandidate == null || name == null) {
            throw new CandidateNotFoundException("Candidate Not found");
        }
        log.debug("Response {}", name);
        return cdo.findByName(name);
    }

    public List<Candidate> getCandidateByJob(String job) throws CandidateNotFoundException {
        List<Candidate> existingCandidate = cdo.findByJob(job);
        log.debug("Request {}", existingCandidate);

        if (existingCandidate == null || job == null) {
            throw new CandidateNotFoundException("Candidate Not found");
        }
        log.debug("Response {}", job);
        return cdo.findByJob(job);
    }

    public List<Candidate> updateCandidates(List<Candidate> candidates) throws UpdationFailedException {
        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            if (c.getId() == candidates.get(i).getId()) {
                synchronized (c) {
                    c.setName(c.getName());
                    c.setJob(c.getJob());
                    c.setAddress(c.getAddress());
                    c.setMail(c.getMail());
                }
            } else {
                CandidateRequest candidateRequest = new CandidateRequest();
                synchronized (candidateRequest) {
                    candidateRequest.setId(c.getId());
                    candidateRequest.setName(c.getName());
                    candidateRequest.setAddress(c.getAddress());
                    candidateRequest.setJob(c.getJob());
                    candidateRequest.setMail(c.getMail());
                }
                cds.saveCandidate(candidateRequest);
            }

            emailRequest.setSubject("Updation");
            emailRequest.setMessage("Details for id " + c.getId() + " have been updated");
            emailRequest.setTo(c.getMail());

            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getMessage());
            log.info("Email Response {} ", c);
        }
        return cdo.saveAll(candidates);
    }
}



