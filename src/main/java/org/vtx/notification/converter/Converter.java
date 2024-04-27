package org.vtx.notification.converter;

import org.vtx.notification.exception.ConverterException;

import java.util.Collection;
import java.util.List;

/**
 * Interface for converting objects from type T to type R.
 *
 * @param <T> The type of the object to convert from.
 * @param <R> The type of the object to convert to.
 * @author Chanthavithou
 * @see FileConverter
 * @see MultipartFileConverter
 * @see ResourceConverter
 * @see StringToAddressConverter
 * @author Chanthavithou
 */
public interface Converter<T, R> {
    /**
     * Converts a single object of type T to type R.
     *
     * @param request The object to convert.
     * @return The converted object.
     * @throws ConverterException if an error occurs during the conversion process.
     */
    R convert(T request) throws ConverterException;

    /**
     * Converts a collection of objects of type T to a list of objects of type R.
     *
     * @param requests The collection of objects to convert.
     * @return A list of converted objects.
     */
    List<R> converts(Collection<T> requests);

    /**
     * Checks if the given object can be converted.
     *
     * @param request The object to check for conversion.
     * @return {@code true} if the object can be converted, {@code false} otherwise.
     */
    boolean canConvert(T request);
}
