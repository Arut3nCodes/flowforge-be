package com.example.flowforge.messaging;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobSender {

    private final RabbitTemplate rabbit;

    public void sendJob(String id, Object payload) {
        rabbit.convertAndSend(
                RabbitConfig.JOBS_EXCHANGE,
                "job." + id,
                payload
        );
        System.out.println("Sent job to worker: " + id);
    }
}

