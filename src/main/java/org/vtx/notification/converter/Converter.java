package org.vtx.notification.converter;

import org.vtx.notification.exception.ConverterException;

public interface Converter<T, R> {
    R convert(T request) throws ConverterException;

    boolean canConvert(T request);
}
