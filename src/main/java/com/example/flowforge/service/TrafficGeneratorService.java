package com.example.flowforge.service;

import com.example.flowforge.deploying.DockerService;
import com.example.flowforge.dto.JobDTO;
import com.example.flowforge.dto.ProfileDTO;
import com.example.flowforge.messaging.JobSender;
import org.apache.commons.statistics.distribution.ParetoDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TrafficGeneratorService {

    private final JobSender jobSender;
    private final ParetoDistribution paretoDistribution;

    // Depth range
    private final double minDepth = 1.0;
    private final double maxDepth = 10.0;

    // Start delay range in milliseconds
    private final long minDelay = 100;  // 0.1s
    private final long maxDelay = 1000; // 1s

    public TrafficGeneratorService(JobSender jobSender) {
        this.jobSender = jobSender;
        this.paretoDistribution = ParetoDistribution.of(1.0, 1.0);
    }

    @Async
    public CompletableFuture<Void> generateTraffic(ProfileDTO profileDTO){
        System.out.println("Setting up " + profileDTO.getNumberOfWorkers() + " workers");
        for(int i = 0; i < profileDTO.getNumberOfJobs(); i++){
            try {
                JobDTO jobDTO = new JobDTO();
                jobDTO.setJobId(i);

                double rawDepth = paretoDistribution.inverseCumulativeProbability(Math.random());
                double scaledDepth = minDepth + (rawDepth / (rawDepth + 1)) * (maxDepth - minDepth);
                jobDTO.setDepth((int) scaledDepth);

                double rawDelay = paretoDistribution.inverseCumulativeProbability(Math.random());
                long scaledDelay = minDelay + (long)((rawDelay / (rawDelay + 1)) * (maxDelay - minDelay));
                jobDTO.setStartDelay(scaledDelay);

                jobDTO.setUserAgent("SomeUserAgent");

                System.out.println("Sending job: " + jobDTO);

                jobSender.sendJob(String.valueOf(i), jobDTO);
            } catch (Exception e) {
                e.printStackTrace();  // <-- see any exceptions!
            }
        }

        return CompletableFuture.completedFuture(null);
    }
}
