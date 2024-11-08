package tck.arquillian.protocol.javatest;

import org.jboss.arquillian.container.test.spi.client.protocol.Protocol;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class JavaTestProtocolExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(Protocol.class, JavaTestProtocol.class);
    }

}
