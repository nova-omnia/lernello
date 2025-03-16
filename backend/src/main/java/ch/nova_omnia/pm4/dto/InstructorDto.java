package ch.nova_omnia.pm4.dto;

import java.util.List;

public class InstructorDto extends BaseDto {
    private List<FolderDto> folders;

    public List<FolderDto> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderDto> folders) {
        this.folders = folders;
    }
}
