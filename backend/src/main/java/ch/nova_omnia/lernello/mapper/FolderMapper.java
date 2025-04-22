package ch.nova_omnia.lernello.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.dto.request.CreateFolderDTO;
import ch.nova_omnia.lernello.dto.response.folder.FolderResDTO;
import ch.nova_omnia.lernello.model.data.Folder;

@Mapper(componentModel = "spring", uses = {LearningKitMapper.class, ParentFolderMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FolderMapper {
    FolderResDTO toDTO(Folder folder);

    Folder toEntity(CreateFolderDTO createFolderDTO);
}
