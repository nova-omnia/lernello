package ch.nova_omnia.pm4.mapper;

public interface GenericMapper<T, D> {
    D toDto(T entity);
}
