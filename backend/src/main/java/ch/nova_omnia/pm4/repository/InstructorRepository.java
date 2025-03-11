package ch.nova_omnia.pm4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ch.nova_omnia.pm4.model.data.Instructor;


@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}