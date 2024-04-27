package org.vtx.notification.converter;

import org.vtx.notification.exception.ConverterException;

import java.util.Collection;
import java.util.List;

public interface Converter<T, R> {
    R convert(T request) throws ConverterException;

    List<R> converts(Collection<T> requests);

    boolean canConvert(T request);
}
