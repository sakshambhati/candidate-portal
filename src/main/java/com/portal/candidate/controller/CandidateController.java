package com.portal.candidate.controller;

import com.portal.candidate.dto.CandidateFlightBookingRequest;
import com.portal.candidate.dto.CandidateRequest;
import com.portal.candidate.dto.FlightBookingAcknowledgement;
import com.portal.candidate.entity.Candidate;
import com.portal.candidate.exception.CandidateNotFoundException;
import com.portal.candidate.exception.UpdationFailedException;
import com.portal.candidate.service.CandidateService;
import com.portal.candidate.service.FlightBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@EnableTransactionManagement
//@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateService cds;

    Logger log = LoggerFactory.getLogger(CandidateController.class);

    @Autowired
    private FlightBookingService service;

    // add new candidate
    @PostMapping("/candidate/addCandidate")
    public ResponseEntity<Candidate> addCandidate(@RequestBody @Valid CandidateRequest candidateRequest) throws CandidateNotFoundException {
        log.debug("Request {}", candidateRequest);
        try {
            Candidate candidate = cds.saveCandidate(candidateRequest);
            log.debug("Response {}", candidate);
            return new ResponseEntity<>(cds.saveCandidate(candidateRequest), HttpStatus.CREATED);
        }catch(Exception e) {
            log.error(e.getMessage());
            throw  new CandidateNotFoundException("Error while saving candidate");
        }
    }

    // add new list of candidates
    @PostMapping("/candidate/addCandidates")
    public ResponseEntity<List<Candidate>> addCandidates(@RequestBody List<Candidate> candidates) throws CandidateNotFoundException {
        log.debug("Request {}", candidates);
        try {
            log.debug("Response {}", HttpStatus.CREATED);
            return new ResponseEntity<>(cds.saveCandidates(candidates), HttpStatus.CREATED);
        }catch (Exception e)
        {
            log.error(e.getMessage());
            throw new CandidateNotFoundException("Error while saving candidates");
        }

    }

    // fetch all candidates
    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> findAllCandidates() throws CandidateNotFoundException {
        log.debug("Request {}", "Fetching all candidates");
        try {
            log.debug("Response {}", "candidates fetched");
            return ResponseEntity.ok(cds.getCandidates());
        } catch (Exception e)
        {
            log.error(e.getMessage());
            throw new CandidateNotFoundException("unable to find required candidates");
        }

    }

    // get candidate by id
    @GetMapping("/candidates/{id}")
    public ResponseEntity<Candidate> findCandidateById(@PathVariable int id) throws CandidateNotFoundException {
        return ResponseEntity.ok(cds.getCandidateById(id));
    }

    // update candidate details
    @PutMapping("/candidate/update")
    public Candidate updateCandidate(@RequestBody Candidate candidate) throws CandidateNotFoundException, UpdationFailedException {
        return cds.updateCandidate(candidate);
    }

    // delete candidate
    @DeleteMapping("/candidate/delete/{id}")
    public ResponseEntity<Candidate> deleteCandidate(@PathVariable int id) throws CandidateNotFoundException {
         cds.deleteCandidate(id);
         return ResponseEntity.ok().build();
    }

    // paging
    @GetMapping("/candidates/{pageNo}/{pageSize}")
    public List<Candidate> findPagedCandidates(@PathVariable int pageNo, @PathVariable int pageSize) throws CandidateNotFoundException {
        try {
            return cds.getPagedCandidates(pageNo, pageSize);
        } catch (Exception e)
        {
            log.error(e.getMessage());
            throw new CandidateNotFoundException("unable to find required candidates");
        }
    }

    // get candidate by name
    @GetMapping("/candidates/name/{name}")
    public List<Candidate> findCandidateByName(@PathVariable String name) throws CandidateNotFoundException {
        return cds.getCandidateByName(name);
    }

    // get candidate by job
    @GetMapping("/candidates/job/{job}")
    public List<Candidate> findCandidateByJob(@PathVariable String job) throws CandidateNotFoundException {
        return cds.getCandidateByJob(job);
    }

    // transaction management
    @PostMapping("/candidate/bookFlightTicket")
    public FlightBookingAcknowledgement bookFlightTicket(@RequestBody CandidateFlightBookingRequest request)
    {
        return service.bookFlightTicket(request);
    }

    // adding multiple candidates and rollback, if any
    @Transactional(rollbackFor = UpdationFailedException.class)
    @PutMapping("/candidate/updateCandidates")
    public List<Candidate> updateCandidates(@RequestBody List<Candidate> candidates) throws UpdationFailedException {
        log.debug("Request {}", candidates);
        try {
            log.debug("Response {}", candidates);
            return cds.updateCandidates(candidates);
        } catch (Exception e)
        {
            log.error(e.getMessage());
            throw new UpdationFailedException("Error updating database, preparing for rollback..");
        }
    }

}
