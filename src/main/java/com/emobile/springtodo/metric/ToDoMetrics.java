package com.emobile.springtodo.metric;

import com.emobile.springtodo.repository.ToDoRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ToDoMetrics {

    public ToDoMetrics(MeterRegistry meterRegistry, ToDoRepository toDoRepository) {

        Gauge.builder("todo.tasks.done.count",
                      () -> toDoRepository.countByDone(true))  // Предполагается, что такой метод есть в репозитории
                .description("Количество выполненных задач (done = true)")
                .register(meterRegistry);
    }
}