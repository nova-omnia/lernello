package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.dto.request.CreateBlockDTO;
import ch.nova_omnia.lernello.model.data.blocks.Block;
import ch.nova_omnia.lernello.service.LearningUnitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/learning-units/{learningUnitId}/blocks")
public class BlockRestController {

    private final LearningUnitService learningUnitService;

    public BlockRestController(LearningUnitService learningUnitService) {
        this.learningUnitService = learningUnitService;
    }

}