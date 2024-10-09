package tck.arquillian.protocol.javatest;

import org.jboss.arquillian.container.test.spi.TestDeployment;
import org.jboss.arquillian.container.test.spi.client.deployment.DeploymentPackager;
import org.jboss.arquillian.container.test.spi.client.deployment.ProtocolArchiveProcessor;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import tck.arquillian.protocol.common.ProtocolJarResolver;

import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;

public class JavaTestDeploymentPackager implements DeploymentPackager {
    static Logger log = Logger.getLogger(JavaTestDeploymentPackager.class.getName());

    @Override
    public Archive<?> generateDeployment(TestDeployment testDeployment, Collection<ProtocolArchiveProcessor> processors) {
        Archive<?> archive = testDeployment.getApplicationArchive();

        // Include the protocol.jar in the deployment
        Collection<Archive<?>> auxiliaryArchives = testDeployment.getAuxiliaryArchives();
        File protocolJar = ProtocolJarResolver.resolveProtocolJar();
        if(protocolJar == null) {
            throw new RuntimeException("Failed to resolve protocol.jar. You either need a jakarta.tck.arquillian:arquillian-protocol-lib"+
                    " dependency in the runner pom.xml or a downloaded target/protocol/protocol.jar file");
        }

        if(archive instanceof EnterpriseArchive) {
            EnterpriseArchive ear = (EnterpriseArchive) archive;
            ear.addAsLibraries(auxiliaryArchives.toArray(new Archive<?>[0]));
            ear.addAsLibraries(protocolJar);
        } else if(archive instanceof WebArchive) {
            WebArchive war = (WebArchive) archive;
            war.addAsLibraries(auxiliaryArchives.toArray(new Archive<?>[0]));
            war.addAsLibraries(protocolJar);
        }

        return archive;
    }

}
