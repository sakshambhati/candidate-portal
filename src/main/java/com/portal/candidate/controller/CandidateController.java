package com.portal.candidate.controller;

import com.portal.candidate.dto.CandidateFlightBookingRequest;
import com.portal.candidate.dto.CandidateRequest;
import com.portal.candidate.dto.FlightBookingAcknowledgement;
import com.portal.candidate.entity.Candidate;
import com.portal.candidate.exception.CandidateNotFoundException;
import com.portal.candidate.service.CandidateService;
import com.portal.candidate.service.FlightBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@EnableTransactionManagement
public class CandidateController {

    @Autowired
    private CandidateService cds;

    @Autowired
    private FlightBookingService service;

    // add new candidate
    @PostMapping("/addCandidate")
    public ResponseEntity<Candidate> addCandidate(@RequestBody @Valid CandidateRequest candidateRequest)
    {
        return new ResponseEntity<>(cds.saveCandidate(candidateRequest), HttpStatus.CREATED);
    }

    // add new list of candidates
    @PostMapping("/addCandidates")
    public ResponseEntity<List<Candidate>> addCandidates(@RequestBody List<Candidate> candidates)
    {

        return new ResponseEntity<>(cds.saveCandidates(candidates), HttpStatus.CREATED);
    }

    // fetch all candidates
    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> findAllCandidates()
    {
        return ResponseEntity.ok(cds.getCandidates());
    }

    // get candidate by id
    @GetMapping("/candidates/{id}")
    public ResponseEntity<Candidate> findCandidateById(@PathVariable int id) throws CandidateNotFoundException {
        return ResponseEntity.ok(cds.getCandidateById(id));
    }

    // update candidate details
    @PutMapping("/update")
    public Candidate updateCandidate(@RequestBody Candidate candidate) throws CandidateNotFoundException {
        return cds.updateCandidate(candidate);
    }

    // delete candidate
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Candidate> deleteCandidate(@PathVariable int id) throws CandidateNotFoundException {
         cds.deleteCandidate(id);
         return ResponseEntity.ok().build();
    }

    // paging
    @GetMapping("/candidates/{pageNo}/{pageSize}")
    public List<Candidate> findPagedCandidates(@PathVariable int pageNo, @PathVariable int pageSize)
    {
        return cds.getPagedCandidates(pageNo, pageSize);
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
    @PostMapping("/bookFlightTicket")
    public FlightBookingAcknowledgement bookFlightTicket(@RequestBody CandidateFlightBookingRequest request)
    {
        return service.bookFlightTicket(request);
    }
}
