package ee.jakarta.tck.persistence.ee.cdi;

import jakarta.inject.Qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Qualifier for selecting the CTS-EM2 persistence unit.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface CtsEm2Qualifier {
}
