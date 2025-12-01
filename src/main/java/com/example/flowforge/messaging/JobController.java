package com.example.flowforge.messaging;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobSender jobSender;

    @PostMapping("/{workerId}")
    public String sendJob(
            @PathVariable String workerId,
            @RequestBody String payload
    ) {
        jobSender.sendJob(workerId, payload);
        return "Job sent to worker: " + workerId;
    }
}
