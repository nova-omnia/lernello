package ch.nova_omnia.lernello.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.dto.request.GenerateLearningUnitDTO;
import ch.nova_omnia.lernello.dto.request.JobStatusDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsyncJobService {
    private final LearningUnitService learningUnitService;

    private final ConcurrentMap<String, JobStatusDTO> jobStore = new ConcurrentHashMap<>();
    private final Map<UUID, String> unitToJobMap = new ConcurrentHashMap<>();

    private ExecutorService executor;

    @PostConstruct
    public void initExecutor() {
        this.executor = Executors.newCachedThreadPool();
    }

    public String startLearningUnitGeneration(UUID learningUnitId, GenerateLearningUnitDTO dto) {
        String jobId = UUID.randomUUID().toString();
        jobStore.put(jobId, new JobStatusDTO("PENDING"));
        unitToJobMap.put(learningUnitId, jobId);

        executor.submit(() -> {
            try {
                jobStore.put(jobId, new JobStatusDTO("IN_PROGRESS"));
                learningUnitService.generateLearningUnitWithAI(dto, learningUnitId);
                jobStore.put(jobId, new JobStatusDTO("DONE"));
            } catch (Exception e) {
                jobStore.put(jobId, new JobStatusDTO("FAILED", e.getMessage()));
            }
        });

        return jobId;
    }

    public JobStatusDTO getStatus(String jobId) {
        return jobStore.getOrDefault(jobId, new JobStatusDTO("UNKNOWN"));
    }

    public Optional<String> getJobIdForUnit(UUID learningUnitId) {
        return Optional.ofNullable(unitToJobMap.get(learningUnitId));
    }
}
