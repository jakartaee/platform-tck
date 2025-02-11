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
    private static final HashSet<String> SKIPPED_MODULES = new HashSet<>();
    static {
        SKIPPED_MODULES.add("core-profile-tck");
        SKIPPED_MODULES.add("expression-language-outside-container");
        SKIPPED_MODULES.add("messaging-outside-container");
        SKIPPED_MODULES.add("persistence-outside-container");
        SKIPPED_MODULES.add("user_guides");
    }

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
        File pomFile = new File("pom.xml");
        Document document = reader.read(pomFile);

        Element project = document.getRootElement();
        Element projectVersion = project.element("version");
        List<Node> list = document.selectNodes("/project/modules/module");
        System.out.println("Modules:" + list.size());
        // Write the updated module file
        projectVersion.setText(version);
        try(FileWriter writer = new FileWriter(pomFile)) {
            document.write(writer);
        }

        // Update the submodule versions
        updateModules(null, project, version);

        // Update the release module
        File releaseModule = new File("release/pom.xml");
        Document releaseDocument = reader.read(releaseModule);
        Element releaseProject = releaseDocument.getRootElement();
        Element releaseVersion = releaseProject.element("parent").element("version");
        releaseVersion.setText(version);
        try(FileWriter writer = new FileWriter(releaseModule)) {
            releaseDocument.write(writer);
        }

        // Write a release script for deploying the artifacts
        writeReleaseScript(version);
        // Write a bom for the artifacts
    }

    void updateModules(File projectDir, Element project, String version) throws Exception {
        SAXReader reader = new SAXReader();
        List<Element> modules = project.elements("modules");
        if(modules.isEmpty()) {
            return;
        }

        for(Element module : modules.get(0).elements("module")) {
            String modulePath = module.getText();
            int lastSlash = modulePath.lastIndexOf('/');
            String moduleName = modulePath.substring(lastSlash + 1);
            if(SKIPPED_MODULES.contains(moduleName)) {
                continue;
            }
            if(projectDir != null) {
                modulePath = projectDir.getAbsolutePath() + "/" + modulePath;
            }
            File moduleFile = new File(modulePath + "/pom.xml");
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
                    setVersion(moduleProject, version);
                    Element parentVersion = moduleProject.element("parent").element("version");
                    if (parentVersion.getText().startsWith(TCK_BASE_VERSION)) {
                        parentVersion.setText(version);
                    }
                }
                // Write the updated module file
                try(FileWriter writer = new FileWriter(moduleFile)) {
                    moduleDocument.write(writer);
                }

                Element submodules = moduleProject.element("modules");
                if(submodules != null) {
                    updateModules(moduleFile.getParentFile(), moduleProject, version);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void writeReleaseScript(String version) throws IOException {
        System.out.println("Write release script");
        FileWriter writer = new FileWriter("release.sh");
        File root = new File(".");
        // Write the parent pom
        writer.write("mvn install gpg:sign nexus-staging:deploy -DskipTests\n");
        // Write the test artifacts
        for (TestArtifact artifact : artifacts) {
            writer.write(String.format("# %s\n", artifact));
            writer.write(String.format("cd %s\n", artifact.getPomDir()));
            writer.write("mvn nexus-staging:deploy -DskipTests\n");
        }
        writer.write(String.format("cd %s\n", root.getAbsolutePath()));
        writer.close();
    }
    String getVersion(Element root) {
        return getInfo(root, "version");
    }
    void setVersion(Element moduleProject, String version) {
        Element versionElement = moduleProject.element("version");
        if (versionElement != null) {
            versionElement.setText(version);
        } else {
            moduleProject.element("parent").element("version").setText(version);
        }
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
