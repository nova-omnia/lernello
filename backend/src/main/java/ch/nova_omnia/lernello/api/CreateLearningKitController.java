package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.mapper.LearningKitMapper;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.service.LearningKitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/learning-kits")
@Validated
public class CreateLearningKitController {
    private final LearningKitService learningKitService;

    private final LearningKitMapper learningKitMapper;

    public CreateLearningKitController(LearningKitService learningKitService, LearningKitMapper learningKitMapper) {
        this.learningKitService = learningKitService;
        this.learningKitMapper = learningKitMapper;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid ResponseEntity<LearningKitResDTO> create(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        entity.setLanguage(LearningKit.Language.ENGLISH);
        LearningKit savedEntity = learningKitService.save(entity);
        return ResponseEntity.ok(learningKitMapper.toDTO(savedEntity));
    }
    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid ResponseEntity<LearningKitResDTO> edit(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        LearningKit savedEntity = learningKitService.edit(entity);
        return ResponseEntity.ok(learningKitMapper.toDTO(savedEntity));
    }
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid ResponseEntity<Void> delete(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        learningKitService.deleteById(entity.getUuid());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/getAll")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid ResponseEntity<List<LearningKitResDTO>> getAll() {
        List<LearningKit> learningKits = learningKitService.findAll();
        List<LearningKitResDTO> learningKitResDTOs = learningKits.stream()
                .map(learningKitMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(learningKitResDTOs);
    }
    @PostMapping("/getById")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid ResponseEntity<LearningKitResDTO> getById(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        Optional<LearningKit> savedEntity = learningKitService.findById(entity.getUuid());
        return savedEntity.map(kit -> ResponseEntity.ok(learningKitMapper.toDTO(kit))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
