package com.halyk.onlinestore.mapper.base;

public interface Mapper<E, R> {

    R toResponse(E entity);
}
