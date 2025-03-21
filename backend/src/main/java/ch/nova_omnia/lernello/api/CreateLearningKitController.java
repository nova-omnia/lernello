package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.CreateLerningKitDTO;
import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.mapper.LearningKitMapper;
import ch.nova_omnia.lernello.repository.LearningKitRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class CreateLearningKitController {
    private final LearningKitRepository repository;
    private final LearningKitMapper learningKitMapper;

    public CreateLearningKitController(LearningKitRepository repository, LearningKitMapper learningKitMapper) {
        this.repository = repository;
        this.learningKitMapper = learningKitMapper;
    }

    @PostMapping("/learning-kits")
    public @Valid ResponseEntity<LearningKitResDTO> create(@Valid @RequestBody CreateLerningKitDTO learningKit) {
        var entity = learningKitMapper.toEntity(learningKit);
        var savedEntity = repository.save(entity);
        return ResponseEntity.ok(learningKitMapper.toDTO(savedEntity));
    }
}
