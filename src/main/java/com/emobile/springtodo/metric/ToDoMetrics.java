package com.emobile.springtodo.metric;

import com.emobile.springtodo.repository.ToDoRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ToDoMetrics {
    private final MeterRegistry meterRegistry;
    private final ToDoRepository toDoRepository;

    public ToDoMetrics(MeterRegistry meterRegistry, ToDoRepository toDoRepository) {
        this.meterRegistry = meterRegistry;
        this.toDoRepository = toDoRepository;

        Gauge.builder("todo.tasks.done.count",
                      () -> toDoRepository.countByDone(true))  // Предполагается, что такой метод есть в репозитории
                .description("Количество выполненных задач (done = true)")
                .register(meterRegistry);
    }
}