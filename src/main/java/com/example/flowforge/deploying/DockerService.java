package com.example.flowforge.deploying;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DockerService {

    private final DockerClient dockerClient;
    private final List<String> runningContainers = new CopyOnWriteArrayList<>();

    public DockerService() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        dockerClient = DockerClientImpl.getInstance(config, httpClient);
    }

    /**
     * Startuje worker.py w kontenerach
     *
     * @param count liczba instancji
     * @param rabbitHost host RabbitMQ do przekazania przez ENV
     */
    public List<String> startPythonContainers(int count, String rabbitHost) {
        List<String> started = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            CreateContainerResponse container = dockerClient.createContainerCmd("my-python-worker")
                    .withEnv("RABBIT_HOST=" + rabbitHost)      // podanie hosta RabbitMQ
                    .withCmd("python", "/app/worker.py")       // uruchomienie workera
                    .exec();

            dockerClient.startContainerCmd(container.getId()).exec();

            runningContainers.add(container.getId());
            started.add(container.getId());
        }

        return started;
    }

    /**
     * Zatrzymuje wszystkie kontenery
     */
    public List<String> stopAll() {
        for (String id : runningContainers) {
            try {
                dockerClient.stopContainerCmd(id).exec();
            } catch (Exception ignored) {}
            dockerClient.removeContainerCmd(id).exec();
        }

        List<String> stopped = new ArrayList<>(runningContainers);
        runningContainers.clear();
        return stopped;
    }
}
