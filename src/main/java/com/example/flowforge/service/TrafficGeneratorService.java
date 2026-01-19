package com.example.flowforge.service;

import com.example.flowforge.dto.AnalysisSummaryDTO;
import com.example.flowforge.dto.JobDTO;
import com.example.flowforge.dto.ProfileDTO;
import com.example.flowforge.messaging.JobSender;
import org.apache.commons.statistics.distribution.ParetoDistribution;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TrafficGeneratorService {

    private final JobSender jobSender;
    private final ParetoDistribution paretoDistribution;

    // Depth range
    private final int minDepth = 1;
    private final int maxDepth = 10;

    // Start delay range in milliseconds
    private final long minDelay = 100;  // 0.1s
    private final long maxDelay = 1000; // 1s

    public TrafficGeneratorService(JobSender jobSender) {
        this.jobSender = jobSender;
        this.paretoDistribution = ParetoDistribution.of(1.0, 1.0);
    }

    @Async
    public CompletableFuture<Void> generateTraffic(ProfileDTO profileDTO) {
        System.out.println("Setting up " + profileDTO.getNumberOfWorkers() + " workers");
        List<JobDTO> jobs = new ArrayList<>();
        int totalDepth = 0;

        for (int i = 0; i < profileDTO.getNumberOfJobs(); i++) {
            JobDTO jobDTO = new JobDTO(i, profileDTO);

            // Generate integer depth
            double rawDepth = paretoDistribution.inverseCumulativeProbability(Math.random());
            int scaledDepth = minDepth + (int)((rawDepth / (rawDepth + 1)) * (maxDepth - minDepth));
            jobDTO.setDepth(scaledDepth);
            totalDepth += scaledDepth;

            // Generate start delay
            double rawDelay = paretoDistribution.inverseCumulativeProbability(Math.random());
            long scaledDelay = minDelay + (long)((rawDelay / (rawDelay + 1)) * (maxDelay - minDelay));
            jobDTO.setStartDelay(scaledDelay);

            jobDTO.setUserAgent("SomeUserAgent");

            jobs.add(jobDTO);
        }

        // 2️⃣ Send summary message first to SUMMARY channel
        try {
            AnalysisSummaryDTO summary = new AnalysisSummaryDTO("analysis_start", totalDepth);
            System.out.println("Sending summary: " + summary);
            jobSender.sendSummary("analysis_start", summary);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3️⃣ Send individual jobs to JOBS channel
        for (JobDTO jobDTO : jobs) {
            try {
                System.out.println("Sending job: " + jobDTO);
                jobSender.sendJob(String.valueOf(jobDTO.getJobId()), jobDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return CompletableFuture.completedFuture(null);
    }
}
