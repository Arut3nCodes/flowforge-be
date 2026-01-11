package com.example.flowforge.controller;
import com.example.flowforge.deploying.DockerService;
import com.example.flowforge.dto.ProfileDTO;
import com.example.flowforge.messaging.JobSender;
import com.example.flowforge.service.TrafficGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobSender jobSender;

    @Autowired
    private final TrafficGeneratorService trafficService;

    @PostMapping("/{workerId}")
    public String sendTestJob(
            @PathVariable String workerId,
            @RequestBody String payload
    ) {
        jobSender.sendTestJob(workerId, payload);
        return "Job sent to worker: " + workerId;
    }

    @PostMapping("/trafficGen/start")
    public String sendJob(
            @RequestBody ProfileDTO profileDTO
    ) {
        trafficService.generateTraffic(profileDTO);
        return "Traffic generation starter";

    }

}
