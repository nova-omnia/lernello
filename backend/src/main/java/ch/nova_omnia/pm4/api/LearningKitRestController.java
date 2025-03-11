package ch.nova_omnia.pm4.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import ch.nova_omnia.pm4.service.LearningKitService;
import ch.nova_omnia.pm4.dto.LearningKitDTO;
import ch.nova_omnia.pm4.mapper.LearningKitMapper;
import ch.nova_omnia.pm4.model.data.LearningKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/learning-kits")
public class LearningKitRestController {

    @Autowired
    private LearningKitService learningKitService;

    @Autowired
    private LearningKitMapper learningKitMapper;

    @PostMapping
    public LearningKitDTO createLearningKit(@Valid @RequestBody LearningKitDTO learningKitDTO) {

        LearningKit learningKit = learningKitMapper.toEntity(learningKitDTO);

        LearningKit createdKit = learningKitService.createLearningKit(
                learningKit.getName(), learningKitDTO.getParentFolderId());

        return learningKitMapper.toDto(createdKit);
    }

    @GetMapping
    public List<LearningKitDTO> getAllLearningKits() {
        List<LearningKit> learningKits = learningKitService.getAllLearningKits();
        return learningKits.stream()
                .map(learningKitMapper::toDto)
                .collect(Collectors.toList());
    }
}