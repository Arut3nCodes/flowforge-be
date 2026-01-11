package com.example.flowforge.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobDTO implements Serializable {
    private int jobId;
    private String targetUrl;
    private double timeoutSec;
    private double startDelay;
    private int depth;
    private double thinkTimeAvg;
    private double thinkTimeVar;
    private String userAgent;

    public JobDTO(int jobId, ProfileDTO profileDTO){
        targetUrl = profileDTO.getTargetUrl();
        timeoutSec = profileDTO.getTimeoutSec();
        thinkTimeAvg = profileDTO.getThinkTimeAvg();
        thinkTimeVar = profileDTO.getThinkTimeVar();
    }
}
