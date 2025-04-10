package ch.nova_omnia.lernello.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.mapper.LearningKitMapper;
import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.service.LearningKitService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/learning-kits")
@Validated
public class LearningKitRestController {
    private final LearningKitService learningKitService;

    private final LearningKitMapper learningKitMapper;

    public LearningKitRestController(LearningKitService learningKitService, LearningKitMapper learningKitMapper) {
        this.learningKitService = learningKitService;
        this.learningKitMapper = learningKitMapper;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO create(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        LearningKit savedEntity = learningKitService.save(entity);
        return learningKitMapper.toDTO(savedEntity);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO edit(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        LearningKit savedEntity = learningKitService.edit(entity);
        return learningKitMapper.toDTO(savedEntity);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public void delete(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        learningKitService.deleteById(entity.getUuid());
    }

    @PostMapping("/getAll")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid List<LearningKitResDTO> getAll() {
        List<LearningKit> learningKits = learningKitService.findAll();
        return learningKits.stream().map(learningKitMapper::toDTO).collect(Collectors.toList());
    }

    @PostMapping("/getById")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid LearningKitResDTO getById(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        Optional<LearningKit> savedEntity = learningKitService.findById(entity.getUuid());
        return savedEntity.map(learningKitMapper::toDTO).orElseThrow(RuntimeException::new);
    }
}