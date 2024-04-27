package org.vtx.notification.converter;

import org.vtx.notification.exception.ConverterException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * An abstract class for implementing conversion handlers.
 * This class provides a template for converting objects of type T to type R.
 * Subclasses must implement the {@code convertInternal} method to define the conversion logic.
 *
 * @param <T> The type of the object to convert from.
 * @param <R> The type of the object to convert to.
 * @see FileConverter
 * @see MultipartFileConverter
 * @see ResourceConverter
 * @see StringToAddressConverter
 * @author Chanthavithou
 */
public abstract class AbstractConversionHandler<T, R> implements Converter<T, R> {
    /** The function to determine if the conversion is possible for a given object. */
    private final Function<T, Boolean> function;

    /**
     * Constructs a new AbstractConversionHandler with the specified function.
     *
     * @param function The function to determine if the conversion is possible.
     */
    public AbstractConversionHandler(Function<T, Boolean> function) {
        this.function = function;
    }

    /**
     * Converts a single object of type T to type R.
     *
     * @param request The object to convert.
     * @return The converted object.
     * @throws ConverterException if an error occurs during the conversion process.
     */
    @Override
    public R convert(T request) throws ConverterException {
        if (canConvert(request)) {
            return convertInternal(request);
        }
        return null;
    }

    /**
     * Converts a collection of objects of type T to a list of objects of type R.
     *
     * @param requests The collection of objects to convert.
     * @return A list of converted objects.
     */
    @Override
    public List<R> converts(Collection<T> requests) {
        List<R> responses = new ArrayList<>();
        for (T request : requests) {
            responses.add(this.convert(request));
        }
        return responses;
    }

    /**
     * Checks if the given object can be converted.
     *
     * @param request The object to check for conversion.
     * @return {@code true} if the object can be converted, {@code false} otherwise.
     */
    @Override
    public boolean canConvert(T request) {
        return function.apply(request);
    }

    /**
     * Converts the object of type T to type R.
     * Subclasses must implement this method to define the conversion logic.
     *
     * @param request The object to convert.
     * @return The converted object.
     */
    protected abstract R convertInternal(T request);
}
