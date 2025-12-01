package com.example.flowforge.messaging;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String JOBS_EXCHANGE = "jobs_exchange";
    public static final String JOBS_QUEUE = "jobs_queue";
    public static final String JOBS_KEY = "job.*";

    public static final String RESULTS_EXCHANGE = "results_exchange";
    public static final String RESULTS_QUEUE = "results_queue";
    public static final String RESULTS_KEY = "result.*";

    @Bean
    public TopicExchange jobsExchange() {
        return new TopicExchange(JOBS_EXCHANGE);
    }

    @Bean
    public Queue jobsQueue() {
        return QueueBuilder.durable(JOBS_QUEUE).build();
    }

    @Bean
    public Binding jobsBinding() {
        return BindingBuilder.bind(jobsQueue()).to(jobsExchange()).with(JOBS_KEY);
    }

    @Bean
    public TopicExchange resultsExchange() {
        return new TopicExchange(RESULTS_EXCHANGE);
    }

    @Bean
    public Queue resultsQueue() {
        return QueueBuilder.durable(RESULTS_QUEUE).build();
    }

    @Bean
    public Binding resultsBinding() {
        return BindingBuilder.bind(resultsQueue()).to(resultsExchange()).with(RESULTS_KEY);
    }
}
