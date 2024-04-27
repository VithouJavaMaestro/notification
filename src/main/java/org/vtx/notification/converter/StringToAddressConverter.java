package org.vtx.notification.converter;

import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.vtx.notification.exception.ConverterException;

import java.util.function.Function;

@Slf4j
public class StringToAddressConverter extends AbstractConversionHandler<String, Address> {

    public StringToAddressConverter(Function<String, Boolean> function) {
        super(function);
    }

    public StringToAddressConverter() {
        super(StringUtils::hasText);
    }

    @Override
    public Address convertInternal(String email) throws ConverterException {

        try {

            final InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(email);
            internetAddress.validate();

            return internetAddress;
        } catch (AddressException exception) {
            log.error("email validated is invalid", exception);
            throw new ConverterException("email validated is invalid", exception);
        }
    }
}
