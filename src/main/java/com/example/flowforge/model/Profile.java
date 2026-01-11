package com.example.flowforge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  = "profile_id")
    private Long profileId;

    @Column(name  = "profile_name", length = 64)
    private String profileName;

    @Column(name  = "count_of_jobs")
    private int numberOfJobs;

    @Column(name  = "count_of_workers")
    private int numberOfWorkers;

    @Column(name = "target_url", length = 512)
    private String targetUrl;

    @Column(name = "timeout_sec")
    private Integer timeoutSec;

    @Column(name = "think_time_avg")
    private Double thinkTimeAvg;

    @Column(name = "think_time_var")
    private Double thinkTimeVar;

    @Column(name = "user_agent", length = 512)
    private String userAgent;
}