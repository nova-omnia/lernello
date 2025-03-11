package ch.nova_omnia.pm4.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.pm4.dto.LearningUnitDto;
import ch.nova_omnia.pm4.mapper.LearningUnitMapper;
import ch.nova_omnia.pm4.model.data.LearningUnit;
import ch.nova_omnia.pm4.service.LearningUnitService;

@RestController
@RequestMapping("/api/learning-units")
public class LearningUnitRestController extends AbstractCrudRestController<LearningUnit, LearningUnitDto, Long> {

    @Autowired
    private LearningUnitService learningUnitService;

    @Autowired
    private LearningUnitMapper learningUnitMapper;

    @Override
    protected LearningUnitService getService() {
        return learningUnitService;
    }

    @Override
    protected LearningUnitMapper getMapper() {
        return learningUnitMapper;
    }
}
