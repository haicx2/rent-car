package org.example.rentcar.dto;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityConverter<T,D> {
    private final ModelMapper modelMapper;

    public D mapEntityToDTO(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public T mapDTOToEntity(D dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }
}
