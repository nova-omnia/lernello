package ch.nova_omnia.pm4.api;

import java.util.List;

import ch.nova_omnia.pm4.service.LearningUnitService;
import ch.nova_omnia.pm4.model.data.LearningUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/learning-units")
public class LearningUnitRestController {

    @Autowired
    private LearningUnitService learningUnitService;

    @PostMapping
    public LearningUnit createLearningUnit(@RequestBody LearningUnitRequest request) {
        return learningUnitService.createLearningUnit(request.getName(), request.getFolderId());
    }

    @GetMapping
    public List<LearningUnit> getAllLearningUnits() {
        return learningUnitService.getAllLearningUnits();
    }

    public static class LearningUnitRequest {
        private String name;
        private Long folderId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getFolderId() {
            return folderId;
        }

        public void setFolderId(Long folderId) {
            this.folderId = folderId;
        }
    }
}