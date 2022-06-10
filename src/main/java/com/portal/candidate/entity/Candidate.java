package com.portal.candidate.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Entity
public class Candidate {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String address;
    private String job;

    private String source;
    private String destination;
    private double fare;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    private Date travelDate;

    @Column(nullable = false, length = 255, unique = true)
    private String mail;



}
