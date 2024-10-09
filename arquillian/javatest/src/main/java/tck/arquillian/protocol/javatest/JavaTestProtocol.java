package tck.arquillian.protocol.javatest;

import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.test.spi.ContainerMethodExecutor;
import org.jboss.arquillian.container.test.spi.client.deployment.DeploymentPackager;
import org.jboss.arquillian.container.test.spi.client.protocol.Protocol;
import org.jboss.arquillian.container.test.spi.command.CommandCallback;
import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;

public class JavaTestProtocol implements Protocol<JavaTestProtocolConfiguration> {
    @Inject
    private Instance<Injector> injectorInstance;

    @Override
    public Class<JavaTestProtocolConfiguration> getProtocolConfigurationClass() {
        return JavaTestProtocolConfiguration.class;
    }

    @Override
    public ProtocolDescription getDescription() {
        return new ProtocolDescription("javatest");
    }

    @Override
    public DeploymentPackager getPackager() {
        return new JavaTestDeploymentPackager();
    }

    @Override
    public ContainerMethodExecutor getExecutor(JavaTestProtocolConfiguration protocolConfiguration, ProtocolMetaData metaData,
                                               CommandCallback callback) {

        JavaTestMethodExecutor executor = new JavaTestMethodExecutor(protocolConfiguration);
        Injector injector = injectorInstance.get();
        injector.inject(executor);
        return executor;
    }
}

