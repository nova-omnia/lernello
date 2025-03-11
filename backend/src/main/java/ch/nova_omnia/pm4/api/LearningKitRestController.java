package ch.nova_omnia.pm4.api;

import ch.nova_omnia.pm4.service.LearningKitService;
import ch.nova_omnia.pm4.dto.LearningKitDto;
import ch.nova_omnia.pm4.mapper.LearningKitMapper;
import ch.nova_omnia.pm4.model.data.LearningKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/learning-kits")
public class LearningKitRestController extends AbstractCrudRestController<LearningKit, LearningKitDto, Long> {

    @Autowired
    private LearningKitService learningKitService;

    @Autowired
    private LearningKitMapper learningKitMapper;

    @Override
    protected LearningKitService getService() {
        return learningKitService;
    }

    @Override
    protected LearningKitMapper getMapper() {
        return learningKitMapper;
    }
}