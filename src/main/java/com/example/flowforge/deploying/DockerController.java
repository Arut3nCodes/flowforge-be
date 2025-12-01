package com.example.flowforge.deploying;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/docker")
public class DockerController {

    private final DockerService dockerService;

    public DockerController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @PostMapping("/start/{count}")
    public List<String> start(@PathVariable int count) {
        return dockerService.startPythonContainers(count, "localhost:5672");
    }

    @PostMapping("/stop")
    public List<String> stop() {
        return dockerService.stopAll();
    }
}

