package jpms;

import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * A simple test class to validate that a module-info.java can import and use Jakarta EE APIs.
 * https://github.com/jakartaee/jakartaee-api/issues/185
 * https://github.com/jakartaee/platform-tck/issues/2005
 *
 * The module-info.java only require the jakarta.cdi module, but this class uses classes from jakarta.annotation,
 * jakarta.inject, and jakarta.cdi to validate that the jakarta.platform:jakarta.jakartaee-bom dependency
 * does not exclude these APIs.
 */
@ApplicationScoped
public class SimpleApiTest {
    @Inject
    Object nothing;

    @Test
    public void doNothing() {
        System.out.println("Just validating that the test runs, no jpms compile errors");
        doNothingPrivate("non-null arg");
    }

    private void doNothingPrivate(@Nonnull String arg) {
        System.out.printf("doNothingPrivate(%s)%n", arg);
    }
}
