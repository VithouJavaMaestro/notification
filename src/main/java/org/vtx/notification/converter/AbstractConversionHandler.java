package org.vtx.notification.converter;

import org.vtx.notification.exception.ConverterException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractConversionHandler<T, R> implements Converter<T, R> {
    private final Function<T, Boolean> function;

    public AbstractConversionHandler(Function<T, Boolean> function) {
        this.function = function;
    }

    @Override
    public R convert(T request) throws ConverterException {
        if (canConvert(request)) {
            return convertInternal(request);
        }
        return null;
    }

    @Override
    public List<R> converts(Collection<T> requests) {
        List<R> responses = new ArrayList<>();
        for (T request : requests) {
            responses.add(this.convert(request));
        }
        return responses;
    }

    @Override
    public boolean canConvert(T request) {
        return function.apply(request);
    }

    protected abstract R convertInternal(T request);
}
