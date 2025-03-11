package ch.nova_omnia.pm4.api;

import java.util.List;

import ch.nova_omnia.pm4.service.LearningKitService;
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

    @PostMapping
    public LearningKit createLearningKit(@RequestBody LearningKitRequest request) {
        return learningKitService.createLearningKit(request.getName(), request.getFolderId());
    }

    @GetMapping
    public List<LearningKit> getAllLearningKits() {
        return learningKitService.getAllLearningKits();
    }

    public static class LearningKitRequest {
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