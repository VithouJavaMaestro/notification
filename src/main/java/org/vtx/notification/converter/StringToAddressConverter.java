package org.vtx.notification.converter;

import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.vtx.notification.exception.ConverterException;

import java.util.function.Function;

/**
 * A converter class for converting String representations of email addresses to Address objects.
 *
 * <p>This class extends {@link AbstractConversionHandler} and implements the conversion logic
 * in the {@code convertInternal} method.</p>
 *
 * <p>The conversion criteria can be provided through a custom function or a default check
 * that ensures the String representation is not empty or null.</p>
 *
 * <p>The conversion process involves validating the email address and creating an Address object
 * representing it.</p>
 * @author Chanthavithou
 */
@Slf4j
public class StringToAddressConverter extends AbstractConversionHandler<String, Address> {

    /**
     * Constructs a StringToAddressConverter with the specified conversion function.
     *
     * @param function The function to determine if the conversion is possible.
     */
    public StringToAddressConverter(Function<String, Boolean> function) {
        super(function);
    }

    /**
     * Constructs a StringToAddressConverter with a default conversion function.
     * The default function checks if the String representation is not empty or null.
     */
    public StringToAddressConverter() {
        super(StringUtils::hasText);
    }

    /**
     * Converts a String representation of an email address to an Address object.
     *
     * @param email The String representation of the email address to convert.
     * @return The converted Address object.
     * @throws ConverterException if the provided email address is invalid.
     */
    @Override
    public Address convertInternal(String email) throws ConverterException {

        try {

            final InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(email);
            internetAddress.validate();

            return internetAddress;
        } catch (AddressException exception) {
            log.error("Email address is invalid", exception);
            throw new ConverterException("Email address is invalid", exception);
        }
    }
}
