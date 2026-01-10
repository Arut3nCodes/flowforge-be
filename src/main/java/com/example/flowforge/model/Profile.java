package com.example.flowforge.model;

import com.example.flowforge.model.enums.LoadProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer profileId;

    private String profileName;

    @Enumerated(EnumType.STRING)
    private LoadProfile loadProfile;

    private int numberOfIterations;
    private int numberOfWorkers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profileId") // Foreign key in Request table
    private List<Request> listOfInstructions;
}

