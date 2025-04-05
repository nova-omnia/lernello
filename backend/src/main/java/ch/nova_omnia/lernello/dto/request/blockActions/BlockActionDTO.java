package ch.nova_omnia.lernello.dto.request.blockActions;

public sealed interface BlockActionDTO permits AddBlockActionDTO, ReorderBlockActionDTO, RemoveBlockActionDTO{
}
