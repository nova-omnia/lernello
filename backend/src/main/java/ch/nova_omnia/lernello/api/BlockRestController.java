package ch.nova_omnia.lernello.api;

import ch.nova_omnia.lernello.service.LearningUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/learning-units/{learningUnitId}/blocks")
@RequiredArgsConstructor
public class BlockRestController {
    private final LearningUnitService learningUnitService;

}