package com.example.flowforge.service;

import com.example.flowforge.dto.AnalysisDoneDto;
import com.example.flowforge.dto.AnalysisSummaryDTO;
import com.example.flowforge.dto.JobDTO;
import com.example.flowforge.dto.ProfileDTO;
import com.example.flowforge.messaging.AnalysisDoneReceiver;
import com.example.flowforge.messaging.JobSender;
import org.apache.commons.statistics.distribution.ParetoDistribution;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    public AnalysisDoneDto generateTraffic(ProfileDTO profileDTO) {
        System.out.println("Setting up " + profileDTO.getNumberOfWorkers() + " workers");
        List<JobDTO> jobs = new ArrayList<>();
        int totalDepth = 0;

        for (int i = 0; i < profileDTO.getNumberOfJobs(); i++) {
            JobDTO jobDTO = new JobDTO(i, profileDTO);

            // Depth
            double rawDepth = paretoDistribution.inverseCumulativeProbability(Math.random());
            int scaledDepth = minDepth + (int)((rawDepth / (rawDepth + 1)) * (maxDepth - minDepth));
            jobDTO.setDepth(scaledDepth);
            totalDepth += scaledDepth;

            // Start delay
            double rawDelay = paretoDistribution.inverseCumulativeProbability(Math.random());
            long scaledDelay = minDelay + (long)((rawDelay / (rawDelay + 1)) * (maxDelay - minDelay));
            jobDTO.setStartDelay(scaledDelay);

            jobDTO.setUserAgent("SomeUserAgent");
            jobs.add(jobDTO);
        }

        CountDownLatch latch = new CountDownLatch(1);
        AnalysisDoneReceiver.setLatch(latch); // listener will countDown when done

        try {
            AnalysisSummaryDTO summary = new AnalysisSummaryDTO("analysis_start", totalDepth);
            System.out.println("Sending summary: " + summary);
            jobSender.sendSummary("analysis_start", summary);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (JobDTO jobDTO : jobs) {
            try {
                System.out.println("Sending job: " + jobDTO);
                jobSender.sendJob(String.valueOf(jobDTO.getJobId()), jobDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AnalysisDoneDto[] resultHolder = new AnalysisDoneDto[1]; // store result from listener
        AnalysisDoneReceiver.setResultHolder(resultHolder);

        try {
            System.out.println("Waiting for analysis_done from worker...");
            boolean received = latch.await(totalDepth * 3L, TimeUnit.SECONDS); // timeout 60s
            if (!received) {
                System.out.println("Timeout waiting for analysis_done!");
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(resultHolder[0].getPdfB64());

            FileOutputStream fos = new FileOutputStream("output.pdf");
            fos.write(decodedBytes);
            fos.close();

            System.out.println("PDF created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultHolder[0];
    }
}
