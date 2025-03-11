package ch.nova_omnia.pm4.dto;

public class LearningKitDTO extends AbstractDTO {
    private Long parentFolderId;

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

}
