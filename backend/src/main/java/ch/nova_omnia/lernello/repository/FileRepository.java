package ch.nova_omnia.lernello.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.nova_omnia.lernello.model.data.MediaFile;

public interface FileRepository extends JpaRepository<MediaFile, UUID> {
}