package tck.arquillian.porting.lib.spi;

import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.net.URL;

/**
 * Interface that vendors implement to augment test archives with vendor specific deployment content.
 */
public interface TestArchiveProcessor {
    /**
     * Called to process a client archive (jar) that is part of the test deployment.
     * @param clientArchive - the appclient archive
     * @param testClass - the TCK test class
     * @param sunXmlUrl - the URL to the sun-application-client.xml file
     */
    void processClientArchive(JavaArchive clientArchive, Class<?> testClass, URL sunXmlUrl);
    /**
     * Called to process a ejb archive (jar) that is part of the test deployment.
     * @param ejbArchive - the ejb archive
     * @param testClass - the TCK test class
     * @param sunXmlUrl - the URL to the sun-ejb-jar.xml file
     */
    void processEjbArchive(JavaArchive ejbArchive, Class<?> testClass, URL sunXmlUrl);
    /**
     * Called to process a web archive (war) that is part of the test deployment.
     * @param webArchive - the web archive
     * @param testClass - the TCK test class
     * @param sunXmlUrl - the URL to the sun-web.xml file
     */
    void processWebArchive(WebArchive webArchive, Class<?> testClass, URL sunXmlUrl);
    /**
     * Called to process a resource adaptor archive (rar) that is part of the test deployment.
     * @param rarArchive - the resource archive
     * @param testClass - the TCK test class
     * @param sunXmlUrl - the URL to the sun-ra.xml file
     */
    void processRarArchive(JavaArchive rarArchive, Class<?> testClass, URL sunXmlUrl);
    /**
     * Called to process a persistence unit archive (par) that is part of the test deployment.
     * @param parArchive - the resource archive
     * @param testClass - the TCK test class
     * @param persistenceXmlUrl - the URL to the sun-ra.xml file
     */
    void processParArchive(JavaArchive parArchive, Class<?> testClass, URL persistenceXmlUrl);
    /**
     * Called to process an enterprise archive (ear) that is part of the test deployment.
     * @param earArchive - the application archive
     * @param testClass - the TCK test class
     * @param sunXmlUrl - the URL to the sun-application.xml file
     */
    void processEarArchive(EnterpriseArchive earArchive, Class<?> testClass, URL sunXmlUrl);
}
