package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.LearningKit;
import ch.nova_omnia.pm4.repository.LearningKitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LearningKitService extends AbstractCrudService<LearningKit, Long> {

    @Autowired
    private LearningKitRepository learningKitRepository;

    @Override
    protected JpaRepository<LearningKit, Long> getRepository() {
        return learningKitRepository;
    }
}
