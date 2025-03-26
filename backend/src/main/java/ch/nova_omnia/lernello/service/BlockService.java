package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.repository.BlockRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;

}
