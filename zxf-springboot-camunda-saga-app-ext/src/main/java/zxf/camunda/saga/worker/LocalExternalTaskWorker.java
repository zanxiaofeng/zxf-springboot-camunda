package zxf.camunda.saga.worker;

import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@AllArgsConstructor
public class LocalExternalTaskWorker {
    private static final String WORKER_ID = "local-worker-01";
    private static final int MAX_TASKS = 10;
    private static final long LOCK_DURATION = 1800_000L;
    private static final List<String> TOPICS = List.of("ext-topic-1", "ext-topic-2", "ext-topic-3");
    private final ExecutorService workerPool = Executors.newFixedThreadPool(5);

    private final ExternalTaskService externalTaskService;

    @Scheduled(fixedDelay = 30000)
    public void pollTasks() {
        var fetchBuilder = externalTaskService.fetchAndLock(MAX_TASKS, WORKER_ID);
        TOPICS.forEach(topic -> fetchBuilder.topic(topic, LOCK_DURATION));

        List<LockedExternalTask> tasks = fetchBuilder.execute();
        if (!tasks.isEmpty()) {
            log.info("Fetched {} external task(s)", tasks.size());
        }

        for (LockedExternalTask task : tasks) {
            workerPool.execute(() -> processTask(task));
        }
    }

    private void processTask(LockedExternalTask task) {
        String taskId = (String) task.getVariables().get("task-id");
        log.info("start, topic={}, taskId={}, externalTaskId={}", task.getTopicName(), taskId, task.getId());

        try {
            handleBusinessLogic(task);

            externalTaskService.complete(task.getId(), WORKER_ID);
            log.info("end, topic={}, taskId={}, externalTaskId={}", task.getTopicName(), taskId, task.getId());
        } catch (Exception ex) {
            log.error("Failed to process external task: topic={}, taskId={}, externalTaskId={}, error={}", task.getTopicName(), taskId, task.getId(), ex.getMessage(), ex);
            externalTaskService.handleFailure(task.getId(), WORKER_ID, ex.getMessage(), task.getRetries() - 1, 5000L);
        }
    }

    private void handleBusinessLogic(LockedExternalTask task) throws InterruptedException {
        // Simulate work — safe here because this runs in the worker's own thread pool,
        // NOT in the Camunda Job Executor pool
        Thread.sleep(2000);
    }

    @PreDestroy
    public void shutdown() {
        workerPool.shutdown();
        log.info("LocalExternalTaskWorker shutdown");
    }
}
