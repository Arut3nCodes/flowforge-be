package com.example.flowforge.messaging;
import com.example.flowforge.dto.AnalysisDoneDto;
import lombok.Data;
import lombok.Setter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;

@Component
public class AnalysisDoneReceiver {

    @Setter
    private static CountDownLatch latch;
    @Setter
    private static AnalysisDoneDto[] resultHolder;

    @RabbitListener(queues = RabbitConfig.DONE_QUEUE)
    public void receive(AnalysisDoneDto dto) {
        System.out.println("Received analysis_done sessionId=" + dto.getSessionId());
        if (resultHolder != null) {
            resultHolder[0] = dto;
        }
        if (latch != null) {
            latch.countDown();
        }
    }
}


