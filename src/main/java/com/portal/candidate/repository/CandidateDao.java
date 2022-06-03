package com.portal.candidate.repository;

import com.portal.candidate.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateDao extends JpaRepository<Candidate, Integer> {
    List<Candidate> findByName(String name);

    List<Candidate> findByJob(String job);
}
