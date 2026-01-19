package com.example.flowforge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileDTO {
    private Long profileId;
    private String profileName;
    private int numberOfJobs;
    private int numberOfWorkers;
    private String targetUrl;
    private Double timeoutSec;
    private Double thinkTimeAvg;
    private Double thinkTimeVar;
    private String userAgent;
}
