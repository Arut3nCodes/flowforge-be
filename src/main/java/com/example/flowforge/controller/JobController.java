package com.example.flowforge.controller;
import com.example.flowforge.deploying.DockerService;
import com.example.flowforge.dto.AnalysisDoneDto;
import com.example.flowforge.dto.ProfileDTO;
import com.example.flowforge.messaging.JobSender;
import com.example.flowforge.service.TrafficGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AnalysisDoneDto> sendJob(
            @RequestBody ProfileDTO profileDTO
    ) {
        System.out.println("Starting traffic generation...");

        AnalysisDoneDto result = trafficService.generateTraffic(profileDTO);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body(null);
        }

        System.out.println("Traffic generation finished: " + result);

        return ResponseEntity.ok(result);
    }
}
