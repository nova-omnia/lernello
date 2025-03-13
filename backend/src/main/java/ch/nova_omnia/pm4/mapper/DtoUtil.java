package ch.nova_omnia.pm4.mapper;

import ch.nova_omnia.pm4.dto.BaseDto;
import ch.nova_omnia.pm4.model.data.AbstractEntity;
import ch.nova_omnia.pm4.model.data.Folder;

public class DtoUtil {

    public static BaseDto toSimpleDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }
        BaseDto dto = new BaseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static BaseDto mapFolderParent(Folder folder) {
        if (folder.getParentFolder() != null) {
            return toSimpleDto(folder.getParentFolder());
        } else {
            return toSimpleDto(folder.getInstructor());
        }
    }
}
