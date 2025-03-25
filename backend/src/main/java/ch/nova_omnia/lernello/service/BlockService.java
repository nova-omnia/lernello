package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.repository.BlockRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BlockService {
    private final BlockRepository blockRepository;

}
