package com.example.flowforge.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // === JOBS ===
    public static final String JOBS_EXCHANGE = "jobs_exchange";
    public static final String JOBS_QUEUE = "jobs_queue";
    public static final String JOBS_KEY = "job.*";

    // === RESULTS ===
    public static final String RESULTS_EXCHANGE = "results_exchange";
    public static final String RESULTS_QUEUE = "results_queue";
    public static final String RESULTS_KEY = "result.*";

    // === READY (Python compatible) ===
    public static final String READY_QUEUE = "perf.ready"; // <-- match Python

    // === SUMMARY ===
    public static final String SUMMARY_EXCHANGE = "summary_exchange";
    public static final String SUMMARY_QUEUE = "summary_queue";
    public static final String SUMMARY_KEY = "analysis_start";

    // ================== JOBS ==================
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
        return BindingBuilder
                .bind(jobsQueue())
                .to(jobsExchange())
                .with(JOBS_KEY);
    }

    // ================== RESULTS ==================
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
        return BindingBuilder
                .bind(resultsQueue())
                .to(resultsExchange())
                .with(RESULTS_KEY);
    }

    // ================== READY (Python compatible) ==================
    @Bean
    public Queue readyQueue() {
        return QueueBuilder.durable(READY_QUEUE).build();
    }

    @Bean
    public Binding readyBinding() {
        // Bind to default direct exchange to match Python publishing
        return BindingBuilder
                .bind(readyQueue())
                .to(new DirectExchange("")) // default exchange
                .with(READY_QUEUE);
    }

    // ================== SUMMARY ==================
    @Bean
    public DirectExchange summaryExchange() {
        return new DirectExchange(SUMMARY_EXCHANGE);
    }

    @Bean
    public Queue summaryQueue() {
        return QueueBuilder.durable(SUMMARY_QUEUE).build();
    }

    @Bean
    public Binding summaryBinding() {
        return BindingBuilder
                .bind(summaryQueue())
                .to(summaryExchange())
                .with(SUMMARY_KEY);
    }
}
