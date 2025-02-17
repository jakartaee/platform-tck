package ee.tck.versions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Run this class to verify the hashes of the artifacts in the staging repository.
 * java VerifyHashes.java
 */
public class VerifyHashes {
    // The base of the jakarta.tck groupId in the maven repository, default to staging
    private static String REPO_URL = "https://jakarta.oss.sonatype.org/content/repositories/staging/jakarta/tck/";
    // A pattern to match the versioned jar file name, not sources or javadoc jars
    private static final Pattern VERSION_PATTERN = Pattern.compile("(.*)-(\\d+\\.\\d+\\.\\d+(-\\w+\\d+)?)\\.jar");

    public static void main(String[] args) throws Exception {
        // The maven repository url
        if(args.length > 0) {
            REPO_URL = args[0];
        }

        // All the jars in the current directory
        List<Path> jarFiles = Files.find(Path.of("."), 1, (path, attr) -> path.toString().endsWith(".jar"))
                .toList().stream().sorted().toList();
        downloadRepoJars(jarFiles);
        int failed = verifyHashes(jarFiles);
        if(failed > 0) {
            System.out.println("+++ Failed: " + failed);
            System.exit(failed);
        } else {
            System.out.println("+++ All jars verified");
        }
    }

    /**
     * Download the jars matching the local test artifact jars from the maven repository.
     * @param jarFiles - full list of TCK artifacts directory jar files
     * @throws IOException
     */
    static void downloadRepoJars(List<Path> jarFiles) throws IOException {
        Path stagedJars = Path.of("staged-jars");
        if(!Files.exists(stagedJars)) {
            Files.createDirectory(stagedJars);
        }
        // Download the jars from the maven repository
        for (Path jarFile : jarFiles) {
            String jarFileName = jarFile.getFileName().toString();
            Matcher matcher = VERSION_PATTERN.matcher(jarFileName);
            // Skip if a sources or javadoc jar, or already downloaded
            if (matcher.matches() && !Files.exists(jarFile)) {
                String artifactId = matcher.group(1);
                String version = matcher.group(2);

                String url = REPO_URL + artifactId + "/" + version + "/" + jarFileName;
                System.out.println("Downloading " + url);
                URL stagingUrl = new URL(url);
                try (InputStream is = stagingUrl.openStream()) {
                    is.transferTo(Files.newOutputStream(stagedJars.resolve(jarFileName)));
                }
            } else {
                //System.out.println("Skipping " + jarFileName);
            }
        }
    }

    /**
     * Verify the hashes of the downloaded jars
     * @param jarFiles - full list of TCK artifacts directory jar files
     * @return the number of failed verifications
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    static int verifyHashes(List<Path> jarFiles) throws IOException, NoSuchAlgorithmException {
        int failed = 0;
        Path stagedJars = Path.of("staged-jars");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        // Verify the hashes of the downloaded jars
        for (Path jarFile : jarFiles) {
            Path repoJar = stagedJars.resolve(jarFile);
            if(!Files.exists(repoJar)) {
                // A sources or javadoc jar
                continue;
            }
            // The md5 sum of the local jar
            md5.update(Files.readAllBytes(jarFile));
            byte[] digest = md5.digest();
            String md5Sum = Base64.getEncoder().encodeToString(digest);
            md5.reset();
            // The md5 sum of the repo jar
            md5.update(Files.readAllBytes(repoJar));
            byte[] stagedDigest = md5.digest();
            String stagedMd5Sum = Base64.getEncoder().encodeToString(stagedDigest);
            if (md5Sum.equals(stagedMd5Sum)) {
                System.out.println(jarFile.getFileName() + " verified");
            } else {
                System.out.println(jarFile.getFileName() + " failed verification");
                failed ++;
            }
        }
        return failed;
    }
}
