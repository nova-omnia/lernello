package ch.nova_omnia.lernello.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.nova_omnia.lernello.model.data.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
}
