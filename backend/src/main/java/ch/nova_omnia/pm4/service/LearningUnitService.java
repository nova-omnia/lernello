package ch.nova_omnia.pm4.service;

import ch.nova_omnia.pm4.model.data.LearningKit;
import ch.nova_omnia.pm4.model.data.LearningUnit;
import ch.nova_omnia.pm4.repository.LearningKitRepository;
import ch.nova_omnia.pm4.repository.LearningUnitRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LearningUnitService {
    
    @Autowired
    private LearningUnitRepository learningUnitRepository; 

    @Autowired
    private LearningKitRepository learningKitRepository;

    public LearningUnit createLearningUnit(String name, Long learningKitId) {
        LearningUnit newLearningUnit = new LearningUnit();
        newLearningUnit.setName(name);
        LearningKit learningKit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new RuntimeException("Learning kit not found"));
        newLearningUnit.setParentLearningKit(learningKit);
        return learningUnitRepository.save(newLearningUnit);
    }

    public List<LearningUnit> getAllLearningUnits() {
        return (List<LearningUnit>) learningUnitRepository.findAll();
    }

    public LearningUnit getLearningUnitById(Long id) {
        return learningUnitRepository.findById(id).orElseThrow(() -> new RuntimeException("Learning unit not found"));
    }

    public LearningUnit updateLearningUnit(Long id, LearningUnit learningUnit) {
        LearningUnit existingLearningUnit = learningUnitRepository.findById(id).orElseThrow(() -> new RuntimeException("Learning unit not found"));
        existingLearningUnit.setName(learningUnit.getName());
        return learningUnitRepository.save(existingLearningUnit);
    }

    public void deleteLearningUnit(Long id) {
        learningUnitRepository.deleteById(id);
    }
}
