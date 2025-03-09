package ch.nova_omnia.pm4.repository;

import ch.nova_omnia.pm4.model.data.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FolderRepository is a repository interface for managing {@link Folder} entities.
 * It extends {@link JpaRepository} to provide CRUD operations and additional query methods.
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    /**
     * Finds all folders that do not have a parent folder.
     * 
     * @return a list of folders with no parent folder
     */
    List<Folder> findByParentFolderIsNull();
}