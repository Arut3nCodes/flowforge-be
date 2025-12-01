package com.example.flowforge.messaging;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ResultListener {

    @RabbitListener(queues = RabbitConfig.RESULTS_QUEUE)
    public void handleResult(String message) {
        System.out.println("Received worker result: " + message);
    }
}

