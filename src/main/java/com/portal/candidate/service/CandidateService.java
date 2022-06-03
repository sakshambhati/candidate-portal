package com.portal.candidate.service;

import com.portal.candidate.dto.CandidateRequest;
import com.portal.candidate.entity.Candidate;
import com.portal.candidate.exception.CandidateNotFoundException;
import com.portal.candidate.repository.CandidateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateDao cdo;

    public Candidate saveCandidate(CandidateRequest candidateRequest)
    {
        Candidate candidate = Candidate.build(0, candidateRequest.getName(),
                candidateRequest.getAddress(), candidateRequest.getJob());
        return cdo.save(candidate);
    }

    // save list
    public List<Candidate> saveCandidates(List<Candidate> candidates)
    {
        return cdo.saveAll(candidates);
    }

    // fetch list
    public List<Candidate> getCandidates()
    {
        return cdo.findAll();
    }

    public Candidate getCandidateById(int id) throws CandidateNotFoundException {
        Candidate candidate = cdo.findById(id).orElse(null);
        if(candidate != null)
        {
            return candidate;
        }
        else
        {
            throw new CandidateNotFoundException("No user with ID " + id);
        }
    }

    public String deleteCandidate(int id) throws CandidateNotFoundException {
        Candidate existingCandidate = cdo.findById(id).orElseThrow(() -> new CandidateNotFoundException(
                "User not found with id : " + id));
        cdo.deleteById(id);
        return "Deleted "+ id;
    }

    public Candidate updateCandidate(Candidate candidate) throws CandidateNotFoundException {
        Candidate existingCandidate = cdo.findById(candidate.getId()).orElseThrow(() -> new CandidateNotFoundException(
                "User not found with id : " + candidate.getId()));
//        existingCandidate.setId(candidate.getId());
        existingCandidate.setName(candidate.getName());
        existingCandidate.setAddress(candidate.getAddress());
        return cdo.save(existingCandidate);
    }

    // fetch details using paging
    public List<Candidate> getPagedCandidates(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Candidate> pagedResult = cdo.findAll(paging);
        return pagedResult.toList();
    }

    public List<Candidate> getCandidateByName(String name) throws CandidateNotFoundException {
        List<Candidate> existingCandidate = cdo.findByName(name);
        if(existingCandidate == null || name == null)
        {
            throw new CandidateNotFoundException("Candidate Not found");
        }
        return cdo.findByName(name);
    }

    public List<Candidate> getCandidateByJob(String job) throws CandidateNotFoundException {
        List<Candidate> existingCandidate = cdo.findByJob(job);
        if(existingCandidate == null || job == null)
        {
            throw new CandidateNotFoundException("Candidate Not found");
        }
        return cdo.findByJob(job);
    }
}

