package com.example.flowforge.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobDTO {
    private int jobId;
    private String targetUrl;
    private double timeoutSec;
    private double startDelay;
    private int depth;
    private double thinkTimeAvg;
    private double thinkTimeVar;
    private String userAgent;
}
