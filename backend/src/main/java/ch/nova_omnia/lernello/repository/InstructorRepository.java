package ch.nova_omnia.lernello.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.model.data.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
}
