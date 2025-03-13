package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.LearningUnit;
import ch.nova_omnia.pm4.repository.LearningUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LearningUnitService extends AbstractCrudService<LearningUnit, Long> {

    @Autowired
    private LearningUnitRepository learningUnitRepository;

    @Override
    protected JpaRepository<LearningUnit, Long> getRepository() {
        return learningUnitRepository;
    }
}
