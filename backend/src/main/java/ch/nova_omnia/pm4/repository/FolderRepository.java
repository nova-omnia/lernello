package ch.nova_omnia.pm4.repository;

import ch.nova_omnia.pm4.model.data.Folder;
import org.springframework.data.repository.CrudRepository;

public interface FolderRepository extends CrudRepository<Folder, Long> {
}