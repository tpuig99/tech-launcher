package ar.edu.itba.paw.webapp.config;

import org.glassfish.jersey.server.validation.ValidationConfig;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.Locale;


/* According to https://docs.jboss.org/hibernate/validator/4.1/reference/en-US/html/validator-usingvalidator.html#section-message-interpolation */
@Provider
public class CustomContextResolver implements ContextResolver<ValidationConfig> {

    @Override
    public ValidationConfig getContext(Class<?> type) {
        final ValidationConfig configuration = new ValidationConfig();
        configuration.messageInterpolator(new CustomMessageInterpolator());
        return configuration;
    }

    private static class CustomMessageInterpolator implements MessageInterpolator {

        private final MessageInterpolator messageInterpolator;

        public CustomMessageInterpolator() {
            this.messageInterpolator = Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();
        }

        @Override
        public String interpolate(String messageTemplate, Context context) {
            return messageInterpolator.interpolate(messageTemplate, context, LocaleContextHolder.getLocale());
        }

        @Override
        public String interpolate(String messageTemplate, Context context, Locale locale) {
            return messageInterpolator.interpolate(messageTemplate, context, LocaleContextHolder.getLocale());
        }
    }
}
