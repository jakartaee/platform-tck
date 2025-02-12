package ee.tck.versions;

import java.io.File;

public record TestArtifact(String groupId, String artifactId, String version, File pomXml) {
    public TestArtifact {
        if (groupId == null || groupId.isBlank()) {
            throw new IllegalArgumentException("groupId is required");
        }
        if (artifactId == null || artifactId.isBlank()) {
            throw new IllegalArgumentException("artifactId is required");
        }
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("version is required");
        }
    }

    public String toString() {
        return String.format("TestArtifacts(%s): %s:%s:%s", pomXml.getPath(), groupId, artifactId, version);
    }

    public String getPomDir() {
        File parent = pomXml.getParentFile();
        return parent == null ? "." : parent.getAbsolutePath();
    }
}
