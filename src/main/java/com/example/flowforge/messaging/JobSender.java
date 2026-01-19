package com.example.flowforge.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.example.flowforge.dto.AnalysisSummaryDTO;

@Service
@RequiredArgsConstructor
public class JobSender {

    private final RabbitTemplate rabbit;

    public void sendTestJob(String id, Object payload) {
        rabbit.convertAndSend(
                RabbitConfig.JOBS_EXCHANGE,
                "job." + id,
                payload
        );
        System.out.println("Sent job to worker: " + id);
    }

    public void sendJob(String id, Object payload) {
        rabbit.convertAndSend(
                RabbitConfig.JOBS_EXCHANGE,
                "job." + id,
                payload
        );
        System.out.println("Sent job: " + id);
    }

    // ✅ New method to send summary to SUMMARY_EXCHANGE
    public void sendSummary(String routingKey, AnalysisSummaryDTO summary) {
        rabbit.convertAndSend(
                RabbitConfig.SUMMARY_EXCHANGE,
                routingKey,
                summary
        );
        System.out.println("Sent summary: " + summary);
    }
}
