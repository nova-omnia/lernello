package ch.nova_omnia.pm4.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import ch.nova_omnia.pm4.dto.LearningUnitDTO;
import ch.nova_omnia.pm4.mapper.LearningUnitMapper;
import ch.nova_omnia.pm4.model.data.LearningUnit;
import ch.nova_omnia.pm4.service.LearningUnitService;

@RestController
@RequestMapping("/api/learning-units")
public class LearningUnitRestController {

    @Autowired
    private LearningUnitService learningUnitService;

    @Autowired
    private LearningUnitMapper learningUnitMapper;

    @PostMapping
    public LearningUnitDTO createLearningUnit(@Valid @RequestBody LearningUnitDTO learningUnitDTO) {

        LearningUnit learningUnit = learningUnitMapper.toEntity(learningUnitDTO);

        LearningUnit createdUnit = learningUnitService.createLearningUnit(
                learningUnit.getName(), learningUnitDTO.getLearningKitId());

        return learningUnitMapper.toDto(createdUnit);
    }

    @GetMapping
    public List<LearningUnitDTO> getAllLearningUnits() {
        List<LearningUnit> learningUnits = learningUnitService.getAllLearningUnits();
        return learningUnits.stream()
                .map(learningUnitMapper::toDto)
                .collect(Collectors.toList());
    }
}
