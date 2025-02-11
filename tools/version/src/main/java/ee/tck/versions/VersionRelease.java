package ee.tck.versions;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class VersionRelease {
    // The x.y version of the platform
    private static final String TCK_BASE_VERSION = "11.0";
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: VersionRelease <version>");
            System.exit(1);
        }
        String version = args[0];
        new VersionRelease().run(version);
    }

    private HashSet<TestArtifact> artifacts = new HashSet<>();

    private void run(String version) throws Exception{
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File("pom.xml"));

        Element project = document.getRootElement();
        List<Node> list = document.selectNodes("/project/modules/module");
        System.out.println("Modules:" + list.size());

        List<Element> modules = project.elements("modules");
        modules.get(0).elements("module").forEach(module -> {
            File moduleFile = new File(module.getText() + "/pom.xml");
            try {
                Document moduleDocument = reader.read(moduleFile);
                Element moduleProject = moduleDocument.getRootElement();
                String moduleVersion = getVersion(moduleProject);
                String moduleGroupId = getGroupId(moduleProject);
                String moduleArtifactId = moduleProject.element("artifactId").getText();
                TestArtifact testArtifact = new TestArtifact(moduleGroupId, moduleArtifactId, moduleVersion, moduleFile);
                artifacts.add(testArtifact);
                System.out.printf("%s\n", testArtifact);
                if(moduleVersion.startsWith(TCK_BASE_VERSION)) {
                    System.out.println("Update version");
                    moduleProject.element("version").setText(version);
                    Element parentVersion = moduleProject.element("parent").element("version");
                    if (parentVersion.getText().startsWith(TCK_BASE_VERSION)) {
                        parentVersion.setText(version);
                    }
                }
                // Write the updated module file
                try(FileWriter writer = new FileWriter(moduleFile)) {
                    moduleDocument.write(writer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Write a release script for deploying the artifacts
        writeReleaseScript(version);
        // Write a bom for the artifacts
    }

    void writeReleaseScript(String version) throws IOException {
        System.out.println("Write release script");
        FileWriter writer = new FileWriter("release.sh");
        File root = new File(".");
        for (TestArtifact artifact : artifacts) {
            writer.write(String.format("# %s\n", artifact));
            writer.write(String.format("cd %s\n", artifact.getPomDir()));
            writer.write("mvn install gpg:sign nexus-staging:deploy -DskipTests\n");
        }
        writer.write(String.format("cd %s\n", root.getAbsolutePath()));
        writer.close();
    }
    String getVersion(Element root) {
        return getInfo(root, "version");
    }
    String getGroupId(Element root) {
        return getInfo(root, "groupId");
    }
    String getInfo(Element root, String name) {
        Element versionElement = root.element(name);
        if (versionElement != null) {
            return versionElement.getText();
        } else {
            return root.element("parent").element(name).getText();
        }
    }
}
