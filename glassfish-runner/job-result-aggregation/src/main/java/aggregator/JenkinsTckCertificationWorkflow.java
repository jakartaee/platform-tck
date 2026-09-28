/*
 * Copyright (c) 2026 Eclipse Foundation and/or its affiliates.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package aggregator;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class JenkinsTckCertificationWorkflow {

    private static final String VERSION = "2026-06-10-profile-specific-results-url";

    private static final String DEFAULT_OVERVIEW_TREE = String.join(",",
            "name",
            "fullName",
            "url",
            "lastBuild[number,url,result,timestamp,duration,building]",
            "lastCompletedBuild[number,url,result,timestamp,duration,building]",
            "lastSuccessfulBuild[number,url,result,timestamp,duration,building]"
    );

    private static final String BUILD_TREE = String.join(",",
            "number",
            "url",
            "result",
            "timestamp",
            "duration",
            "building",
            "artifacts[fileName,relativePath]"
    );

    private static final String DEFAULT_TCK_FOLDER = "JakartaEE-TCK/11/tck";
    private static final String DEFAULT_PLATFORM_FOLDER = "JakartaEE-TCK/11/tck/platform";
    private static final String DEFAULT_CORE_PROFILE_FOLDER = "JakartaEE-TCK/11/tck/core-profile";
    private static final String DEFAULT_COMPONENT_KEY = "";

    private static final String CERTIFICATION_PROFILE_AUTO = "auto";
    private static final String CERTIFICATION_PROFILE_PLATFORM = "platform";
    private static final String CERTIFICATION_PROFILE_CORE = "core";
    private static final List<String> DEFAULT_EXCLUDED_PLATFORM_JOB_PREFIXES = List.of("persistence/se-ee-");

    private static final List<AdditionalTckRequirement> ADDITIONAL_TCK_REQUIREMENTS = List.of(
            new AdditionalTckRequirement("jakarta-annotations-tck-glassfish", "Jakarta Annotations 3.0 TCK"),
            new AdditionalTckRequirement("jakarta-authentication-tck-glassfish", "Jakarta Authentication 3.1 TCK"),
            new AdditionalTckRequirement("jakarta-cdi", "Jakarta Contexts and Dependency Injection 4.1 TCK"),
            new AdditionalTckRequirement("jakarta-concurrency-tck-glassfish", "Jakarta Concurrency 3.1 TCK"),
            new AdditionalTckRequirement("jakarta-data-tck-glassfish", "Jakarta Data 1.0 TCK"),
            new AdditionalTckRequirement("jakarta-debugging-support-for-other-languages-tck-glassfish", "Jakarta Debugging Support for Other Languages 2.0 TCK"),
            new AdditionalTckRequirement("jakarta-di-tck-glassfish", "Jakarta Dependency Injection 2.0 TCK"),
            new AdditionalTckRequirement("jakarta-faces", "Jakarta Faces 4.1 TCK"),
            new AdditionalTckRequirement("jakarta-jsonp-tck-glassfish", "Jakarta JSON Processing 2.1 TCK"),
            new AdditionalTckRequirement("jakarta-jsonb-tck-glassfish", "Jakarta JSON Binding 3.0 TCK"),
            new AdditionalTckRequirement("jakarta-pages-tck-glassfish", "Jakarta Pages 4.0 TCK"),
            new AdditionalTckRequirement("jakarta-rest-tck-glassfish", "Jakarta REST 4.0 TCK"),
            new AdditionalTckRequirement("jakarta-security-tck-glassfish", "Jakarta Security 4.0 TCK"),
            new AdditionalTckRequirement("jakarta-servlet-tck-glassfish", "Jakarta Servlet 6.1 TCK"),
            new AdditionalTckRequirement("jakarta-validation-tck-glassfish", "Jakarta Validation 3.1 TCK"),
            new AdditionalTckRequirement("jakarta-websocket-tck-glassfish", "Jakarta WebSocket 2.2 TCK"),
            new AdditionalTckRequirement("jakarta-activation-tck-glassfish", "Jakarta Activation 2.1 TCK"),
            new AdditionalTckRequirement("jakarta-authorization-tck-glassfish", "Jakarta Authorization 3.0 TCK"),
            new AdditionalTckRequirement("jakarta-batch-tck-glassfish", "Jakarta Batch 2.1 TCK"),
            new AdditionalTckRequirement("jakarta-mail-tck-glassfish", "Jakarta Mail 2.1 TCK")
    );

    private static final List<AdditionalTckRequirement> CORE_PROFILE_ADDITIONAL_TCK_REQUIREMENTS = List.of(
            new AdditionalTckRequirement("jakarta-annotations-tck-glassfish", "Jakarta Annotations 3.0 TCK"),
            new AdditionalTckRequirement("jakarta-cdi", "Jakarta Contexts and Dependency Injection 4.1 TCK"),
            new AdditionalTckRequirement("jakarta-di-tck-glassfish", "Jakarta Dependency Injection 2.0 TCK"),
            new AdditionalTckRequirement("jakarta-jsonb-tck-glassfish", "Jakarta JSON Binding 3.0 TCK"),
            new AdditionalTckRequirement("jakarta-jsonp-tck-glassfish", "Jakarta JSON Processing 2.1 TCK"),
            new AdditionalTckRequirement("jakarta-rest-tck-glassfish", "Jakarta RESTful Web Services 4.0 TCK")
    );

    private static final String DEFAULT_PLATFORM_TITLE = "Jakarta Platform";
    private static final String DEFAULT_CORE_PROFILE_TITLE = "Jakarta Core Profile";
    private static final String DEFAULT_PLATFORM_VERSION = "11.0";
    private static final String DEFAULT_PLATFORM_TCK_LINK_TEXT = "Jakarta EE Platform TCK 11.0";
    private static final String DEFAULT_CORE_PROFILE_TCK_LINK_TEXT = "Jakarta EE Core Profile TCK 11.0";
    private static final String DEFAULT_PLATFORM_TCK_DOWNLOAD_LINK = "https://download.eclipse.org/ee4j/jakartaee-tck/jakartaee11/staged/eftl/jakartaeetck-11.0.0-dist.zip";
    private static final String DEFAULT_PLATFORM_TCK_SHA = "b2c0ad6db0514b75ff612dd7d855a3976c8ae9f240a3b690a684f61bf503bead";
    private static final String DEFAULT_CORE_PROFILE_TCK_DOWNLOAD_LINK = "UNKNOWN";
    private static final String DEFAULT_CORE_PROFILE_TCK_SHA = "UNKNOWN";

    private static final String NL = System.lineSeparator();
    private static final char QUOTE = (char) 34;
    private static final char BACKSLASH = (char) 92;
    private static final char LF = (char) 10;
    private static final char CR = (char) 13;
    private static final char TAB = (char) 9;

    public static void main(String[] args) throws Exception {
        System.out.println("Generator version: " + VERSION);
        Arguments arguments = Arguments.parse(args);
        JenkinsHttp http = new JenkinsHttp(arguments.user, arguments.token, arguments.timeoutSeconds);

        if (!arguments.skipDiscover) {
            List<JobOverview> overviews = discoverJobs(arguments, http);
            writeOverviewJson(Path.of(arguments.overviewJsonOut), arguments, overviews);
            printDiscoverSummary(overviews);
            System.out.println("Wrote " + arguments.overviewJsonOut);
        } else {
            System.out.println("Skipping discovery; using existing " + arguments.overviewJsonOut);
        }

        if (!arguments.skipInventory) {
            List<JobOverview> overviews = OverviewJsonReader.readJobs(
                    Files.readString(Path.of(arguments.overviewJsonOut), StandardCharsets.UTF_8));
            List<DiscoveredJob> selectedJobs = selectBuilds(overviews, arguments.buildSelector);
            List<BuildInventory> inventories = inspectAllBuilds(arguments, http, selectedJobs);
            writeInventoryJson(Path.of(arguments.inventoryJsonOut), arguments, inventories);
            printInventorySummary(inventories);
            System.out.println("Wrote " + arguments.inventoryJsonOut);
        } else {
            System.out.println("Skipping inventory; using existing " + arguments.inventoryJsonOut);
        }

        if (!arguments.skipReport || !arguments.skipCcr) {
            renderReport(arguments, http);
        } else {
            System.out.println("Skipping report and CCR generation");
        }
    }

    private static void renderReport(Arguments arguments, JenkinsHttp http) throws Exception {
        List<BuildInventory> builds = InventoryJsonReader.readBuilds(
                Files.readString(Path.of(arguments.inventoryJsonOut), StandardCharsets.UTF_8));

        List<PlatformJob> platformJobs = discoverPlatformJobs(builds, arguments.platformFolder);
        List<ComponentGroup> componentGroups = discoverComponentGroups(
                builds, arguments.tckFolder, arguments.platformFolder, arguments.componentKey);

        StringBuilder markdown = new StringBuilder();
        List<String> warnings = new ArrayList<>();
        ReportMetadata metadata;

        try (ArtifactFetchPool fetchPool = new ArtifactFetchPool(arguments.fetchThreads)) {
            prefetchPlatformArtifacts(fetchPool, http, platformJobs);
            prefetchComponentSummaries(fetchPool, http, componentGroups);

            metadata = collectReportMetadata(fetchPool, http, platformJobs, componentGroups);
            printProductRuntimeDebug(metadata);
            printEnvironmentMetadataDebug(metadata);

            appendCertificationHeader(markdown, metadata, arguments);
            appendEnvironmentSections(markdown, metadata);
            appendTestCountsHeading(markdown, arguments);

            if (!arguments.skipPlatform) {
                PlatformRenderResult platform = renderPlatformSection(fetchPool, http, platformJobs, arguments, metadata);
                markdown.append(platform.markdown);
                warnings.addAll(platform.warnings);
            }

            if (!arguments.skipComponents) {
                if (!markdown.isEmpty() && !markdown.toString().endsWith(NL + NL)) {
                    markdown.append(NL);
                }
                for (ComponentGroup group : componentGroups) {
                    ComponentRenderResult rendered = renderComponentGroup(fetchPool, http, group);
                    markdown.append(rendered.markdown);
                    warnings.addAll(rendered.warnings);
                }
            }
        }

        if (!arguments.skipReport) {
            Files.writeString(Path.of(arguments.reportOut), markdown.toString(), StandardCharsets.UTF_8);
            System.out.println("Wrote " + arguments.reportOut);
        } else {
            System.out.println("Skipping report generation");
        }
        if (!arguments.skipCcr) {
            String ccr = renderCompatibilityCertificationRequest(metadata, arguments);
            Files.writeString(Path.of(arguments.ccrOut), ccr, StandardCharsets.UTF_8);
            System.out.println("Wrote " + arguments.ccrOut);
        } else {
            System.out.println("Skipping CCR generation");
        }

        System.out.println("TCK folder: " + arguments.tckFolder);
        System.out.println("Platform folder: " + arguments.platformFolder);
        System.out.println("Certification profile: " + arguments.effectiveCertificationProfile());
        System.out.println("Platform jobs generated: " + (arguments.skipPlatform ? 0 : platformJobs.size()));
        System.out.println("Component key: " + (arguments.componentKey.isBlank() ? "<all>" : arguments.componentKey));
        System.out.println("Component groups generated: " + (arguments.skipComponents ? 0 : componentGroups.size()));
        System.out.println("Fetch threads: " + arguments.fetchThreads);
        System.out.println("Platform jobs with summary.txt: " + platformJobs.stream().filter(j -> j.build.hasSummaryTxt()).count());
        System.out.println("Platform jobs with Surefire XML: " + platformJobs.stream().filter(j -> j.build.hasSurefireXml()).count());
        System.out.println("Platform jobs with Failsafe XML: " + platformJobs.stream().filter(j -> j.build.hasFailsafeXml()).count());

        if (!warnings.isEmpty()) {
            System.out.println();
            System.out.println("Warnings:");
            warnings.forEach(warning -> System.out.println("- " + warning));
        }
    }

    private static List<JobOverview> discoverJobs(Arguments arguments, JenkinsHttp http) throws Exception {
        String configXml = loadAggregatorConfig(arguments, http);
        List<String> projects = extractProjectsFromConfig(configXml);
        if (projects.isEmpty()) {
            throw new IllegalStateException("No downstream projects found in aggregator config.xml");
        }

        String jobsBaseUrl = arguments.jobsBaseUrl != null
                ? ensureSlash(arguments.jobsBaseUrl)
                : inferJobsBaseUrl(arguments.aggregatorUrl);

        List<JobOverview> overviews = new ArrayList<>();
        int index = 1;
        for (String project : projects) {
            String jobUrl = projectToJobUrl(jobsBaseUrl, project);
            overviews.add(fetchJobOverview(http, index++, project, jobUrl, arguments.overviewTree));
            if (arguments.sleepMillis > 0) {
                Thread.sleep(arguments.sleepMillis);
            }
        }
        return overviews;
    }

    private static String loadAggregatorConfig(Arguments arguments, JenkinsHttp http) throws Exception {
        if (arguments.configFile != null) {
            return Files.readString(Path.of(arguments.configFile), StandardCharsets.UTF_8);
        }
        if (arguments.aggregatorUrl == null) {
            throw new IllegalArgumentException("Either --aggregator-url or --config-file is required unless --skip-discover true");
        }
        return http.getText(ensureSlash(arguments.aggregatorUrl) + "config.xml");
    }

    private static List<String> extractProjectsFromConfig(String configXml) throws Exception {
        Document document = parseXml(configXml);
        NodeList nodes = document.getElementsByTagName("projects");
        Set<String> result = new LinkedHashSet<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            String text = nodes.item(i).getTextContent();
            if (text == null || text.isBlank()) {
                continue;
            }
            for (String raw : text.split(",")) {
                String project = raw.trim();
                if (!project.isEmpty()) {
                    result.add(project);
                }
            }
        }
        return new ArrayList<>(result);
    }

    private static JobOverview fetchJobOverview(JenkinsHttp http, int index, String project, String jobUrl, String tree) {
        try {
            String apiUrl = ensureSlash(jobUrl) + "api/xml?tree=" + encodeQueryParam(tree);
            Document document = parseXml(http.getText(apiUrl));
            Element root = document.getDocumentElement();
            return new JobOverview(index, project, jobUrl, true,
                    childText(root, "name"),
                    childText(root, "fullName"),
                    buildRef(childElement(root, "lastBuild")),
                    buildRef(childElement(root, "lastCompletedBuild")),
                    buildRef(childElement(root, "lastSuccessfulBuild")),
                    null);
        } catch (Exception e) {
            return new JobOverview(index, project, jobUrl, false, null, null, null, null, null,
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static BuildRef buildRef(Element element) {
        if (element == null) {
            return null;
        }
        Long timestampMillis = parseLong(childText(element, "timestamp"));
        String timestampIso = timestampMillis == null ? null : timestampIso(timestampMillis);
        return new BuildRef(
                parseInteger(childText(element, "number")),
                childText(element, "url"),
                childText(element, "result"),
                timestampIso,
                parseLong(childText(element, "duration")),
                parseBoolean(childText(element, "building"))
        );
    }

    private static String projectToJobUrl(String jobsBaseUrl, String project) {
        StringBuilder url = new StringBuilder(ensureSlash(jobsBaseUrl));
        for (String rawSegment : project.split("/")) {
            String segment = rawSegment.trim();
            if (!segment.isEmpty()) {
                url.append("job/").append(encodePathSegment(segment)).append('/');
            }
        }
        return url.toString();
    }

    private static String inferJobsBaseUrl(String aggregatorUrl) {
        if (aggregatorUrl == null || aggregatorUrl.isBlank()) {
            throw new IllegalArgumentException("--jobs-base-url is required when --aggregator-url is not provided");
        }
        String url = ensureSlash(aggregatorUrl);
        String withoutTrailingSlash = url.substring(0, url.length() - 1);
        int index = withoutTrailingSlash.lastIndexOf("/job/");
        if (index < 0) {
            throw new IllegalArgumentException("Could not infer --jobs-base-url; please provide it explicitly");
        }
        return ensureSlash(withoutTrailingSlash.substring(0, index));
    }

    private static void printDiscoverSummary(List<JobOverview> rows) {
        long ok = rows.stream().filter(JobOverview::ok).count();
        long failed = rows.size() - ok;
        System.out.println("Discovered jobs: " + rows.size() + "; reachable: " + ok + "; failed: " + failed);
    }

    private static void writeOverviewJson(Path path, Arguments arguments, List<JobOverview> rows) throws IOException {
        String jobsBaseUrl = arguments.jobsBaseUrl != null ? ensureSlash(arguments.jobsBaseUrl)
                : arguments.aggregatorUrl == null ? null : inferJobsBaseUrl(arguments.aggregatorUrl);
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        appendJsonField(json, 1, "aggregatorUrl", arguments.aggregatorUrl, true);
        appendJsonField(json, 1, "jobsBaseUrl", jobsBaseUrl, true);
        appendJsonField(json, 1, "discoveredCount", rows.size(), true);
        appendJsonField(json, 1, "reachableCount", rows.stream().filter(JobOverview::ok).count(), true);
        indent(json, 1).append("\"jobs\": [\n");
        for (int i = 0; i < rows.size(); i++) {
            appendJobOverviewJson(json, rows.get(i), 2);
            json.append(i + 1 < rows.size() ? ",\n" : "\n");
        }
        indent(json, 1).append("]\n");
        json.append("}\n");
        Files.writeString(path, json.toString(), StandardCharsets.UTF_8);
    }

    private static void appendJobOverviewJson(StringBuilder json, JobOverview row, int level) {
        indent(json, level).append("{\n");
        appendJsonField(json, level + 1, "index", row.index(), true);
        appendJsonField(json, level + 1, "project", row.project(), true);
        appendJsonField(json, level + 1, "jobUrl", row.jobUrl(), true);
        appendJsonField(json, level + 1, "ok", row.ok(), true);
        appendJsonField(json, level + 1, "name", row.name(), true);
        appendJsonField(json, level + 1, "fullName", row.fullName(), true);
        appendBuildRefJson(json, level + 1, "lastBuild", row.lastBuild(), true);
        appendBuildRefJson(json, level + 1, "lastCompletedBuild", row.lastCompletedBuild(), true);
        appendBuildRefJson(json, level + 1, "lastSuccessfulBuild", row.lastSuccessfulBuild(), true);
        appendJsonField(json, level + 1, "error", row.error(), false);
        indent(json, level).append("}");
    }

    private static void appendBuildRefJson(StringBuilder json, int level, String name, BuildRef build, boolean comma) {
        indent(json, level).append(QUOTE).append(escapeJson(name)).append("\": ");
        if (build == null) {
            json.append("null");
        } else {
            json.append("{");
            json.append("\"number\": ").append(build.number() == null ? "null" : build.number());
            json.append(", \"url\": ").append(jsonValue(build.url()));
            json.append(", \"result\": ").append(jsonValue(build.result()));
            json.append(", \"timestamp\": ").append(jsonValue(build.timestamp()));
            json.append(", \"durationMillis\": ").append(build.durationMillis() == null ? "null" : build.durationMillis());
            json.append(", \"building\": ").append(build.building() == null ? "null" : build.building());
            json.append("}");
        }
        if (comma) {
            json.append(',');
        }
        json.append(NL);
    }

    private static List<DiscoveredJob> selectBuilds(List<JobOverview> overviews, String selector) {
        List<DiscoveredJob> result = new ArrayList<>();
        for (JobOverview overview : overviews) {
            BuildRef build = switch (selector) {
                case "lastBuild" -> overview.lastBuild();
                case "lastSuccessfulBuild" -> overview.lastSuccessfulBuild();
                case "lastCompletedBuild" -> overview.lastCompletedBuild();
                default -> throw new IllegalArgumentException("Unsupported --build-selector: " + selector);
            };
            DiscoveredJob job = new DiscoveredJob();
            job.index = overview.index();
            job.project = overview.project();
            job.fullName = overview.fullName();
            job.jobUrl = overview.jobUrl();
            job.selectedBuildKind = selector;
            if (build != null) {
                job.buildNumber = build.number();
                job.buildUrl = build.url();
            }
            result.add(job);
        }
        return result;
    }

    private static List<BuildInventory> inspectAllBuilds(Arguments arguments, JenkinsHttp http, List<DiscoveredJob> jobs)
            throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(arguments.threads);
        List<Future<BuildInventory>> futures = new ArrayList<>(jobs.size());
        try {
            System.out.println("Inspecting " + jobs.size() + " builds using " + arguments.threads + " threads");
            for (int i = 0; i < jobs.size(); i++) {
                int index = i + 1;
                DiscoveredJob job = jobs.get(i);
                futures.add(executor.submit(() -> inspectJobBuild(http, index, job)));
                if (arguments.sleepMillis > 0) {
                    Thread.sleep(arguments.sleepMillis);
                }
            }
            List<BuildInventory> inventories = new ArrayList<>(jobs.size());
            for (int i = 0; i < futures.size(); i++) {
                BuildInventory inventory;
                try {
                    inventory = futures.get(i).get();
                } catch (ExecutionException e) {
                    DiscoveredJob job = jobs.get(i);
                    inventory = new BuildInventory();
                    inventory.index = i + 1;
                    inventory.project = job.project;
                    inventory.fullName = job.fullName;
                    inventory.jobUrl = job.jobUrl;
                    inventory.selectedBuildKind = job.selectedBuildKind;
                    inventory.inputBuildUrl = job.buildUrl;
                    inventory.inputBuildNumber = job.buildNumber;
                    inventory.ok = false;
                    inventory.error = e.getCause() == null ? e.toString()
                            : e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage();
                }
                inventories.add(inventory);
                System.out.printf("%3d/%3d %-7s %6s %-10s %s%n",
                        i + 1,
                        jobs.size(),
                        Boolean.TRUE.equals(inventory.ok) ? "OK" : "FAILED",
                        inventory.buildNumber == null ? "-" : inventory.buildNumber,
                        inventory.result == null ? "-" : inventory.result,
                        inventory.project);
                if (!Boolean.TRUE.equals(inventory.ok)) {
                    System.out.println("        " + inventory.error);
                }
            }
            return inventories;
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }
    }

    private static BuildInventory inspectJobBuild(JenkinsHttp http, int index, DiscoveredJob job) {
        BuildInventory inventory = new BuildInventory();
        inventory.index = index;
        inventory.project = job.project;
        inventory.fullName = job.fullName;
        inventory.jobUrl = job.jobUrl;
        inventory.selectedBuildKind = job.selectedBuildKind;
        inventory.inputBuildUrl = job.buildUrl;
        inventory.inputBuildNumber = job.buildNumber;
        if (job.buildUrl == null || job.buildUrl.isBlank()) {
            inventory.ok = false;
            inventory.error = "No selected build URL in input for selector " + job.selectedBuildKind;
            return inventory;
        }
        try {
            String buildApiUrl = ensureSlash(job.buildUrl) + "api/xml?tree=" + encodeQueryParam(BUILD_TREE);
            Document document = parseXml(http.getText(buildApiUrl));
            Element root = document.getDocumentElement();
            inventory.ok = true;
            inventory.buildNumber = parseInteger(childText(root, "number"));
            inventory.buildUrl = childText(root, "url");
            inventory.result = childText(root, "result");
            inventory.timestamp = timestampIso(parseLong(childText(root, "timestamp")));
            inventory.durationMillis = parseLong(childText(root, "duration"));
            inventory.building = parseBoolean(childText(root, "building"));
            inventory.artifacts = readArtifacts(root);
            inventory.artifactCount = inventory.artifacts.size();
            classifyArtifacts(inventory);
            ConsoleProbe consoleProbe = probeConsole(http, job.buildUrl);
            inventory.consoleReachable = consoleProbe.reachable;
            inventory.consoleProbeUrl = consoleProbe.url;
            inventory.consoleSizeHeader = consoleProbe.textSize;
            inventory.consoleError = consoleProbe.error;
            return inventory;
        } catch (Exception e) {
            inventory.ok = false;
            inventory.error = e.getClass().getSimpleName() + ": " + e.getMessage();
            return inventory;
        }
    }

    private static List<ArtifactRef> readArtifacts(Element root) {
        List<ArtifactRef> result = new ArrayList<>();
        NodeList artifactNodes = root.getElementsByTagName("artifact");
        for (int i = 0; i < artifactNodes.getLength(); i++) {
            Node node = artifactNodes.item(i);
            if (node instanceof Element artifact) {
                String fileName = childText(artifact, "fileName");
                String relativePath = childText(artifact, "relativePath");
                if (relativePath != null && !relativePath.isBlank()) {
                    ArtifactRef artifactRef = new ArtifactRef();
                    artifactRef.fileName = fileName;
                    artifactRef.relativePath = relativePath;
                    result.add(artifactRef);
                }
            }
        }
        return result;
    }

    private static void classifyArtifacts(BuildInventory inventory) {
        for (ArtifactRef artifact : inventory.artifacts) {
            String path = artifact.relativePath == null ? "" : artifact.relativePath;
            String lower = path.toLowerCase();
            if (lower.endsWith("summary.txt") || lower.equals("summary.txt")) {
                inventory.hasSummaryTxtFlag = true;
            }
            if (lower.endsWith("run.log") || lower.equals("run.log")) {
                inventory.hasRunLog = true;
            }
            if (lower.endsWith("server.log")) {
                inventory.hasServerLog = true;
            }
            if (lower.endsWith("failsafe-summary.xml")) {
                inventory.hasFailsafeSummaryXml = true;
            }
            if (lower.contains("/surefire-reports/") && lower.endsWith(".xml")) {
                inventory.hasSurefireXmlFlag = true;
                inventory.surefireXmlCount++;
            }
            if (lower.contains("/failsafe-reports/") && lower.endsWith(".xml")) {
                inventory.hasFailsafeXmlFlag = true;
                inventory.failsafeXmlCount++;
            }
            if (lower.endsWith("failsafe-report.html") || lower.endsWith("failsafe.html")) {
                inventory.hasFailsafeHtml = true;
            }
            if (lower.contains("/reports/") || lower.endsWith(".html")) {
                inventory.hasHtmlOrReports = true;
            }
        }
    }

    private static ConsoleProbe probeConsole(JenkinsHttp http, String buildUrl) {
        String url = ensureSlash(buildUrl) + "logText/progressiveText?start=0";
        try {
            HttpResponse<String> response = http.get(url);
            ConsoleProbe probe = new ConsoleProbe();
            probe.url = url;
            probe.reachable = response.statusCode() >= 200 && response.statusCode() < 300;
            probe.textSize = response.headers().firstValue("X-Text-Size").orElse(null);
            if (!probe.reachable) {
                String body = response.body() == null ? "" : response.body();
                probe.error = "HTTP " + response.statusCode() + " body=" + abbreviate(body, 300);
            }
            return probe;
        } catch (Exception e) {
            ConsoleProbe probe = new ConsoleProbe();
            probe.url = url;
            probe.reachable = false;
            probe.error = e.getClass().getSimpleName() + ": " + e.getMessage();
            return probe;
        }
    }

    private static void printInventorySummary(List<BuildInventory> inventories) {
        long ok = inventories.stream().filter(i -> Boolean.TRUE.equals(i.ok)).count();
        long failed = inventories.size() - ok;
        long success = inventories.stream().filter(i -> Objects.equals(i.result, "SUCCESS")).count();
        long unstable = inventories.stream().filter(i -> Objects.equals(i.result, "UNSTABLE")).count();
        long failures = inventories.stream().filter(i -> Objects.equals(i.result, "FAILURE")).count();
        long summaryTxt = inventories.stream().filter(BuildInventory::hasSummaryTxt).count();
        long surefire = inventories.stream().filter(BuildInventory::hasSurefireXml).count();
        long failsafe = inventories.stream().filter(BuildInventory::hasFailsafeXml).count();
        long console = inventories.stream().filter(i -> Boolean.TRUE.equals(i.consoleReachable)).count();
        System.out.println();
        System.out.println("Inventory summary");
        System.out.println("-----------------");
        System.out.println("builds inspected          = " + inventories.size());
        System.out.println("inspection OK             = " + ok);
        System.out.println("inspection failed         = " + failed);
        System.out.println("Jenkins SUCCESS           = " + success);
        System.out.println("Jenkins UNSTABLE          = " + unstable);
        System.out.println("Jenkins FAILURE           = " + failures);
        System.out.println("has summary.txt           = " + summaryTxt);
        System.out.println("has surefire XML artifacts= " + surefire);
        System.out.println("has failsafe XML artifacts= " + failsafe);
        System.out.println("console reachable         = " + console);
    }

    private static void writeInventoryJson(Path path, Arguments arguments, List<BuildInventory> inventories) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        appendJsonField(json, 1, "input", arguments.overviewJsonOut, true);
        appendJsonField(json, 1, "buildSelector", arguments.buildSelector, true);
        appendJsonField(json, 1, "threads", arguments.threads, true);
        appendJsonField(json, 1, "inspectedCount", inventories.size(), true);
        appendJsonField(json, 1, "okCount", inventories.stream().filter(i -> Boolean.TRUE.equals(i.ok)).count(), true);
        indent(json, 1).append("\"builds\": [\n");
        for (int i = 0; i < inventories.size(); i++) {
            appendInventoryJson(json, inventories.get(i), 2);
            json.append(i + 1 < inventories.size() ? ",\n" : "\n");
        }
        indent(json, 1).append("]\n");
        json.append("}\n");
        Files.writeString(path, json.toString(), StandardCharsets.UTF_8);
    }

    private static void appendInventoryJson(StringBuilder json, BuildInventory i, int level) {
        indent(json, level).append("{\n");
        appendJsonField(json, level + 1, "index", i.index, true);
        appendJsonField(json, level + 1, "project", i.project, true);
        appendJsonField(json, level + 1, "fullName", i.fullName, true);
        appendJsonField(json, level + 1, "jobUrl", i.jobUrl, true);
        appendJsonField(json, level + 1, "selectedBuildKind", i.selectedBuildKind, true);
        appendJsonField(json, level + 1, "inputBuildNumber", i.inputBuildNumber, true);
        appendJsonField(json, level + 1, "inputBuildUrl", i.inputBuildUrl, true);
        appendJsonField(json, level + 1, "ok", i.ok, true);
        appendJsonField(json, level + 1, "buildNumber", i.buildNumber, true);
        appendJsonField(json, level + 1, "buildUrl", i.buildUrl, true);
        appendJsonField(json, level + 1, "result", i.result, true);
        appendJsonField(json, level + 1, "timestamp", i.timestamp, true);
        appendJsonField(json, level + 1, "durationMillis", i.durationMillis, true);
        appendJsonField(json, level + 1, "building", i.building, true);
        appendJsonField(json, level + 1, "artifactCount", i.artifactCount, true);
        appendJsonField(json, level + 1, "hasSummaryTxt", i.hasSummaryTxt(), true);
        appendJsonField(json, level + 1, "hasRunLog", i.hasRunLog, true);
        appendJsonField(json, level + 1, "hasServerLog", i.hasServerLog, true);
        appendJsonField(json, level + 1, "hasFailsafeSummaryXml", i.hasFailsafeSummaryXml, true);
        appendJsonField(json, level + 1, "hasSurefireXml", i.hasSurefireXml(), true);
        appendJsonField(json, level + 1, "surefireXmlCount", i.surefireXmlCount, true);
        appendJsonField(json, level + 1, "hasFailsafeXml", i.hasFailsafeXml(), true);
        appendJsonField(json, level + 1, "failsafeXmlCount", i.failsafeXmlCount, true);
        appendJsonField(json, level + 1, "hasFailsafeHtml", i.hasFailsafeHtml, true);
        appendJsonField(json, level + 1, "hasHtmlOrReports", i.hasHtmlOrReports, true);
        appendJsonField(json, level + 1, "consoleReachable", i.consoleReachable, true);
        appendJsonField(json, level + 1, "consoleProbeUrl", i.consoleProbeUrl, true);
        appendJsonField(json, level + 1, "consoleSizeHeader", i.consoleSizeHeader, true);
        appendJsonField(json, level + 1, "consoleError", i.consoleError, true);
        appendArtifactsJson(json, i.artifacts, level + 1, true);
        appendJsonField(json, level + 1, "error", i.error, false);
        indent(json, level).append("}");
    }

    private static void appendArtifactsJson(StringBuilder json, List<ArtifactRef> artifacts, int level, boolean comma) {
        indent(json, level).append("\"artifacts\": [");
        if (!artifacts.isEmpty()) {
            json.append(NL);
            for (int i = 0; i < artifacts.size(); i++) {
                ArtifactRef artifact = artifacts.get(i);
                indent(json, level + 1).append("{");
                json.append("\"fileName\": ").append(jsonValue(artifact.fileName));
                json.append(", \"relativePath\": ").append(jsonValue(artifact.relativePath));
                json.append("}");
                json.append(i + 1 < artifacts.size() ? ",\n" : "\n");
            }
            indent(json, level).append("]");
        } else {
            json.append("]");
        }
        if (comma) {
            json.append(',');
        }
        json.append(NL);
    }


    private static PlatformRenderResult renderPlatformSection(
            ArtifactFetchPool fetchPool, JenkinsHttp http, List<PlatformJob> platformJobs, Arguments arguments,
            ReportMetadata metadata) {
        StringBuilder markdown = new StringBuilder();
        List<String> warnings = new ArrayList<>();
        appendPlatformHeader(markdown, arguments, metadata);

        String currentFolder = null;
        TestCount currentFolderTotal = new TestCount();

        for (PlatformJob platformJob : platformJobs) {
            BuildInventory build = platformJob.build;
            String folder = folderSegmentOrNull(platformJob.displayName);
            if (!Objects.equals(currentFolder, folder)) {
                if (currentFolder != null) {
                    appendSplitTotal(markdown, "Total for " + currentFolder, currentFolderTotal);
                }
                currentFolder = folder;
                currentFolderTotal = new TestCount();
            }

            String snippet;
            markdown.append("Job name: ").append(platformJob.displayName).append(NL).append(NL);
            markdown.append("```").append(NL);
            if (!"SUCCESS".equals(build.result)) {
                snippet = "MISSING: selected build result is " + build.result + " for " + build.fullName + " #" + build.buildNumber;
                warnings.add(snippet);
            } else {
                Extraction extraction = extractPlatformSnippet(fetchPool, http, build, platformJob.displayName);
                snippet = extraction.snippet.strip();
                if (extraction.warning != null) {
                    warnings.add(platformJob.sortKey + ": " + extraction.warning);
                }
            }
            markdown.append(snippet).append(NL);
            markdown.append("```").append(NL).append(NL);

            if (folder != null) {
                currentFolderTotal.add(TestCount.parse(snippet));
            }
        }

        if (currentFolder != null) {
            appendSplitTotal(markdown, "Total for " + currentFolder, currentFolderTotal);
        }
        return new PlatformRenderResult(markdown.toString(), warnings);
    }

    private static void appendCertificationHeader(StringBuilder markdown, ReportMetadata metadata, Arguments arguments) {
        String productVersion = metadata.productVersion();
        String specificationName = certificationSpecificationName(arguments);
        String resultProfileName = certificationResultProfileName(arguments);
        markdown.append("TCK Results").append(NL);
        markdown.append("===========").append(NL).append(NL).append(NL);
        markdown.append("As required by the").append(NL);
        markdown.append("[Eclipse Foundation Technology Compatibility Kit License](https://www.eclipse.org/legal/tck.php),").append(NL);
        markdown.append("following is a summary of the TCK results for releases of ")
                .append(specificationName).append(' ')
                .append(arguments.platformVersion).append(", certification summary.").append(NL).append(NL);

        markdown.append("# ").append(specificationName).append(' ').append(arguments.platformVersion)
                .append(", Eclipse GlassFish ").append(productVersion == null ? "UNKNOWN" : productVersion)
                .append(", TCK Certification Summary").append(NL).append(NL);

        markdown.append("- [X] Organization Name (\"Organization\") and, if applicable, URL:").append(NL);
        markdown.append("  [Eclipse Foundation](https://eclipse.org) <br/><br/>").append(NL);

        markdown.append("- [X] Product Name, Version and download URL (if applicable):").append(NL);
        if (metadata.productRuntimesByKey.isEmpty()) {
            markdown.append("   - Eclipse GlassFish UNKNOWN <br/><br/>").append(NL);
        } else if (metadata.productRuntimesByKey.size() == 1) {
            ProductRuntime runtime = metadata.productRuntimesByKey.values().iterator().next();
            String version = glassFishVersionFromDownloadLink(runtime.glassFishDownloadLink());
            markdown.append("   - Eclipse GlassFish ")
                    .append(version == null || version.isBlank() ? "UNKNOWN" : version)
                    .append(" - [").append(glassFishDownloadLabel(runtime.glassFishDownloadLink())).append("](")
                    .append(runtime.glassFishDownloadLink()).append(")");
            if (runtime.glassFishSha() != null && !runtime.glassFishSha().isBlank()) {
                markdown.append(", SHA-256: `").append(runtime.glassFishSha()).append('`');
            }
            markdown.append(" <br/><br/>").append(NL);
        } else {
            markdown.append("   Eclipse GlassFish").append(NL);
            for (ProductRuntime runtime : metadata.productRuntimesByKey.values()) {
                markdown.append("     - JDK ")
                        .append(runtime.jdkVersion().isBlank() ? "UNKNOWN" : runtime.jdkVersion())
                        .append(" [").append(glassFishDownloadLabel(runtime.glassFishDownloadLink())).append("](")
                        .append(runtime.glassFishDownloadLink()).append(")");
                if (runtime.glassFishSha() != null && !runtime.glassFishSha().isBlank()) {
                    markdown.append(", SHA-256: `").append(runtime.glassFishSha()).append('`');
                }
                markdown.append(' ').append(NL);
            }
            markdown.append("     <br/><br/>").append(NL);
        }

        appendSpecificationName(markdown, arguments);

        PlatformTckMetadata platformTck = metadata.platformTckMetadataOrDefault(arguments);
        markdown.append("- [X] TCK Version, digital SHA-256 fingerprint and download URL:").append(NL);
        markdown.append("  [").append(certificationTckName(arguments)).append(' ').append(platformTck.tckVersion()).append("](")
                .append(platformTck.tckDownloadLink()).append(")").append(NL);
        markdown.append("  SHA-256: `").append(platformTck.tckSha()).append("` <br/><br/> ").append(NL).append(NL);

        String publicResultVersion = productVersion == null || productVersion.isBlank() ? "UNKNOWN" : productVersion;
        markdown.append("- [X] Public URL of TCK Results Summary:").append(NL);
        markdown.append("  [Eclipse GlassFish ").append(resultProfileName).append(' ').append(publicResultVersion).append(" TCK Results](")
                .append(glassFishCertificationResultsUrl(arguments, publicResultVersion))
                .append(") <br/><br/>")
                .append(NL).append(NL);

        appendAdditionalSpecificationRequirements(markdown, metadata, arguments);
    }

    private static void appendSpecificationName(StringBuilder markdown, Arguments arguments) {
        markdown.append("- [X] Specification Name, Version and download URL:").append(NL);
        markdown.append("  [").append(certificationSpecificationName(arguments)).append(' ')
                .append(platformSpecificationDisplayVersion(arguments.platformVersion))
                .append("](https://jakarta.ee/specifications/")
                .append(certificationSpecificationUrlSegment(arguments)).append('/')
                .append(platformSpecificationUrlVersion(arguments.platformVersion))
                .append(") <br/><br/>")
                .append(NL);
    }

    private static void appendAdditionalSpecificationRequirements(StringBuilder markdown, ReportMetadata metadata, Arguments arguments) {
        markdown.append("- [X] Any Additional Specification Certification Requirements:").append(NL).append(NL);
        for (AdditionalTckRequirement requirement : additionalTckRequirements(arguments)) {
            String downloadLink = metadata.componentTckDownloadLinksByKey.get(requirement.componentKey());
            markdown.append("     - [")
                    .append(requirement.label())
                    .append("](")
                    .append(downloadLink == null || downloadLink.isBlank() ? "UNKNOWN" : downloadLink)
                    .append(")")
                    .append(NL);
        }
        markdown.append("<br/><br/>").append(NL).append(NL);
    }

    private static void appendEnvironmentSections(StringBuilder markdown, ReportMetadata metadata) {
        markdown.append("- [X] Java runtime used to run the implementation:<br/>").append(NL);
        if (metadata.javaVersions.isEmpty()) {
            markdown.append("  - UNKNOWN<br/><br/>").append(NL);
        } else if (metadata.javaVersions.size() == 1) {
            markdown.append("  - `").append(metadata.javaVersions.iterator().next()).append("`").append(NL).append(NL);
        } else {
            int index = 1;
            for (String javaVersion : metadata.javaVersions) {
                markdown.append("  - Java runtime ").append(index++).append(" used to run the implementation: <br/>").append(NL);
                markdown.append("   ```").append(NL);
                markdown.append("   ").append(javaVersion).append(NL);
                markdown.append("   ```").append(NL);
            }
            markdown.append(NL);
        }

        markdown.append("- [X] Summary of the information for the certification environment, operating system, cloud, ...:<br/>")
                .append(NL);
        if (metadata.osDetails.isEmpty()) {
            markdown.append("  - UNKNOWN<br/><br/>").append(NL).append(NL);
        } else {
            int index = 1;
            for (String osDetails : metadata.osDetails) {
                markdown.append("  - Operating System ").append(index++).append(" used to run the implementation: <br/>").append(NL);
                markdown.append("   ```").append(NL);
                markdown.append("   ").append(osDetails).append(NL);
                markdown.append("   ```").append(NL);
            }
            markdown.append(NL);
        }
    }

    private static String renderCompatibilityCertificationRequest(ReportMetadata metadata, Arguments arguments) {
        StringBuilder markdown = new StringBuilder();
        String productVersion = metadata.productVersion();
        String publicResultVersion = productVersion == null || productVersion.isBlank() ? "UNKNOWN" : productVersion;
        PlatformTckMetadata platformTck = metadata.platformTckMetadataOrDefault(arguments);

        markdown.append("- [x] Organization Name (\"Organization\") and, if applicable, URL:<br/>").append(NL);
        markdown.append("  [Eclipse Foundation](https://eclipse.org)").append(NL);

        markdown.append("- [x] Product Name, Version and download URL (if applicable):<br/>").append(NL);
        if (metadata.productRuntimesByKey.isEmpty()) {
            markdown.append("  Eclipse GlassFish, UNKNOWN").append(NL);
        } else {
            for (ProductRuntime runtime : metadata.productRuntimesByKey.values()) {
                String version = glassFishVersionFromDownloadLink(runtime.glassFishDownloadLink());
                markdown.append("  [Eclipse GlassFish, ")
                        .append(version == null || version.isBlank() ? "UNKNOWN" : version)
                        .append("](").append(runtime.glassFishDownloadLink()).append(")");
                if (runtime.glassFishSha() != null && !runtime.glassFishSha().isBlank()) {
                    markdown.append(", SHA-256: `").append(runtime.glassFishSha()).append('`');
                }
                markdown.append(NL);
            }
        }

        markdown.append("- [x] Specification Name, Version and download URL:<br/>").append(NL);
        markdown.append("  [").append(certificationSpecificationName(arguments)).append(", ")
                .append(platformSpecificationDisplayVersion(arguments.platformVersion))
                .append("](https://jakarta.ee/specifications/")
                .append(certificationSpecificationUrlSegment(arguments)).append('/')
                .append(platformSpecificationUrlVersion(arguments.platformVersion))
                .append(")").append(NL);

        markdown.append("- [x] TCK Version, digital SHA-256 fingerprint and download URL:<br/>").append(NL);
        markdown.append("  [").append(certificationTckName(arguments)).append(' ').append(platformTck.tckVersion()).append("](")
                .append(platformTck.tckDownloadLink()).append("),  ").append(NL);
        markdown.append("  `").append(platformTck.tckSha()).append('`').append(NL);

        markdown.append("- [x] Public URL of TCK Results Summary:<br/>").append(NL);
        markdown.append("  [Eclipse GlassFish ").append(certificationResultProfileName(arguments)).append(' ').append(publicResultVersion)
                .append(" TCK Results](").append(glassFishCertificationResultsUrl(arguments, publicResultVersion)).append(")").append(NL);

        markdown.append("- [x] Any Additional Specification Certification Requirements:<br/>").append(NL);
        for (AdditionalTckRequirement requirement : additionalTckRequirements(arguments)) {
            ComponentTckMetadata component = metadata.componentTckMetadataByKey.get(requirement.componentKey());
            String downloadLink = component == null || component.tckDownloadLink() == null || component.tckDownloadLink().isBlank()
                    ? "UNKNOWN" : component.tckDownloadLink();
            String bundleName = component == null || component.tckBundleName() == null || component.tckBundleName().isBlank()
                    ? downloadFileName(downloadLink) : component.tckBundleName();
            String sha = component == null || component.tckSha() == null || component.tckSha().isBlank()
                    ? "UNKNOWN" : component.tckSha();
            markdown.append("  ").append(ccrRequirementLabel(requirement.label())).append(NL);
            markdown.append("  [").append(bundleName == null || bundleName.isBlank() ? "UNKNOWN" : bundleName).append("](")
                    .append(downloadLink).append("),").append(NL);
            markdown.append("  `").append(sha).append('`').append(NL).append(NL);
        }

        markdown.append("- [x] Java runtime used to run the implementation:<br/>").append(NL);
        if (metadata.javaVersions.isEmpty()) {
            markdown.append("  UNKNOWN").append(NL);
        } else if (metadata.javaVersions.size() == 1) {
            markdown.append("  ").append(metadata.javaVersions.iterator().next()).append(NL);
        } else {
            int index = 1;
            for (String javaVersion : metadata.javaVersions) {
                markdown.append("  Java runtime ").append(index++).append(" used to run the implementation:").append(NL);
                markdown.append("  ").append(javaVersion).append(NL).append(NL);
            }
        }

        markdown.append("- [x] Summary of the information for the certification environment, operating system, cloud, ...:<br/>").append(NL);
        if (metadata.osDetails.isEmpty()) {
            markdown.append("  UNKNOWN").append(NL);
        } else {
            int index = 1;
            for (String osDetails : metadata.osDetails) {
                markdown.append("  Operating System ").append(index++).append(" used to run the implementation:").append(NL);
                markdown.append("  ").append(osDetails).append(NL).append(NL);
            }
        }

        markdown.append("- [x] By checking this box I acknowledge that the Organization I represent accepts the terms of the [EFTL](https://www.eclipse.org/legal/tck.php).").append(NL);
        markdown.append("- [x] By checking this box I attest that all TCK requirements have been met, including any compatibility rules.").append(NL).append(NL);
        markdown.append("# Task for Copilot").append(NL).append(NL);
        markdown.append("## Instructions").append(NL);
        markdown.append("Follow these repository-wide rules when working on this issue: https://github.com/jakartaee/platform/wiki/Compatibility_Certification_Request_CCR_Review_Guidance").append(NL);
        return markdown.toString();
    }

    private static String ccrRequirementLabel(String reportLabel) {
        if (reportLabel == null || reportLabel.isBlank()) {
            return "UNKNOWN";
        }
        String label = reportLabel.replaceFirst("[ \\t]+[0-9][0-9.]*[ \\t]+TCK$", "")
                .replaceFirst("[ \\t]+TCK$", "")
                .strip();
        return switch (label) {
            case "Jakarta REST" -> "Jakarta RESTful Web Services";
            default -> label;
        };
    }

    private static String downloadFileName(String downloadLink) {
        if (downloadLink == null || downloadLink.isBlank() || "UNKNOWN".equals(downloadLink)) {
            return "UNKNOWN";
        }
        String normalized = downloadLink.strip();
        int query = normalized.indexOf('?');
        if (query >= 0) {
            normalized = normalized.substring(0, query);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        int slash = normalized.lastIndexOf('/');
        return slash >= 0 ? normalized.substring(slash + 1) : normalized;
    }

    private static void appendTestCountsHeading(StringBuilder markdown, Arguments arguments) {
        if (!arguments.skipPlatform || !arguments.skipComponents) {
            markdown.append("# Test counts per TCK").append(NL).append(NL);
        }
    }

    private static List<AdditionalTckRequirement> additionalTckRequirements(Arguments arguments) {
        return isCoreProfile(arguments) ? CORE_PROFILE_ADDITIONAL_TCK_REQUIREMENTS : ADDITIONAL_TCK_REQUIREMENTS;
    }

    private static boolean isCoreProfile(Arguments arguments) {
        return CERTIFICATION_PROFILE_CORE.equals(arguments.effectiveCertificationProfile());
    }

    private static String certificationSpecificationName(Arguments arguments) {
        return isCoreProfile(arguments) ? "Jakarta EE Core Profile" : "Jakarta EE Platform";
    }

    private static String certificationTckName(Arguments arguments) {
        return certificationSpecificationName(arguments) + " TCK";
    }

    private static String certificationSpecificationUrlSegment(Arguments arguments) {
        return isCoreProfile(arguments) ? "coreprofile" : "platform";
    }

    private static String certificationResultProfileName(Arguments arguments) {
        return isCoreProfile(arguments) ? "Core Profile" : "Platform";
    }

    private static String platformSpecificationDisplayVersion(String platformVersion) {
        if (platformVersion == null || platformVersion.isBlank()) {
            return "11";
        }
        int dot = platformVersion.indexOf('.');
        return dot > 0 ? platformVersion.substring(0, dot) : platformVersion;
    }

    private static String platformSpecificationUrlVersion(String platformVersion) {
        return platformSpecificationDisplayVersion(platformVersion);
    }

    private static void appendPlatformHeader(StringBuilder markdown, Arguments arguments, ReportMetadata metadata) {
        PlatformTckMetadata platformTck = metadata.platformTckMetadataOrDefault(arguments);
        markdown.append("## ").append(arguments.platformTitle).append(' ').append(arguments.platformVersion).append(NL).append(NL);
        markdown.append("   - [").append(certificationTckName(arguments)).append(' ').append(platformTck.tckVersion()).append("]")
                .append('(').append(platformTck.tckDownloadLink()).append("),")
                .append(NL).append("  SHA-256: `").append(platformTck.tckSha()).append("` <br/> ")
                .append(NL).append(NL).append(NL);
    }

    private static Extraction extractPlatformSnippet(ArtifactFetchPool fetchPool, JenkinsHttp http, BuildInventory build, String displayName) {
        try {
            Optional<ArtifactRef> summary = build.findPreferredSummaryTxt();
            if (summary.isPresent()) {
                String text = fetchPool.get(http, build, summary.get());
                String snippet = ResultBlockExtractor.extractPlatform(text);
                if (!snippet.isBlank()) {
                    return new Extraction(snippet, null);
                }
            }

            List<ArtifactRef> xmlReports = build.junitXmlArtifacts();
            if (!xmlReports.isEmpty()) {
                return new Extraction(xmlSnippetFromReports(fetchPool, http, build, displayName, xmlReports), null);
            }

            Optional<ArtifactRef> failsafeSummary = build.findArtifactByFileName("failsafe-summary.xml");
            if (failsafeSummary.isPresent()) {
                String xml = fetchPool.get(http, build, failsafeSummary.get());
                return new Extraction(FailsafeSummaryTotals.parse(xml).toCompletedBlock(), null);
            }
            return new Extraction("MISSING: no summary.txt or XML result artifacts found", "no summary.txt or XML result artifacts found");
        } catch (Exception e) {
            return new Extraction("ERROR: " + e.getClass().getSimpleName() + ": " + e.getMessage(),
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static String xmlSnippetFromReports(
            ArtifactFetchPool fetchPool, JenkinsHttp http, BuildInventory build, String displayName, List<ArtifactRef> xmlReports)
            throws Exception {
        if (displayName.contains("signature") && xmlReports.size() > 1) {
            List<String> lines = new ArrayList<>();
            for (ArtifactRef report : xmlReports) {
                lines.add(JUnitXmlTotals.parseLenient(fetchPool.get(http, build, report)).toSurefireLine());
            }
            return String.join(NL, lines);
        }
        JUnitXmlTotals total = new JUnitXmlTotals();
        for (ArtifactRef report : xmlReports) {
            total.add(JUnitXmlTotals.parseLenient(fetchPool.get(http, build, report)));
        }
        return total.toSurefireLine();
    }

    private static ComponentRenderResult renderComponentGroup(ArtifactFetchPool fetchPool, JenkinsHttp http, ComponentGroup group) {
        List<String> warnings = new ArrayList<>();
        List<JobSummary> summaries = new ArrayList<>();

        for (ComponentJob job : group.jobs) {
            if (!"SUCCESS".equals(job.build.result)) {
                warnings.add(job.relativePath + ": selected build result is " + job.build.result);
                summaries.add(JobSummary.failed(job, "MISSING: selected build result is " + job.build.result));
                continue;
            }
            try {
                summaries.add(readComponentJobSummary(fetchPool, http, job));
            } catch (Exception e) {
                warnings.add(job.relativePath + ": " + e.getClass().getSimpleName() + ": " + e.getMessage());
                summaries.add(JobSummary.failed(job, "ERROR: " + e.getClass().getSimpleName() + ": " + e.getMessage()));
            }
        }

        ComponentMetadata metadata = chooseMetadata(group, summaries);
        String sectionTitle = metadata.sectionTitle != null ? metadata.sectionTitle : deriveSectionTitle(group.componentKey);
        String sectionVersion = metadata.majorVersion != null ? metadata.majorVersion : "UNKNOWN";
        String exactVersion = metadata.tckVersion != null ? metadata.tckVersion : "UNKNOWN";
        String downloadLink = metadata.tckDownloadLink != null ? metadata.tckDownloadLink : "UNKNOWN";
        String sha = metadata.tckSha != null ? metadata.tckSha : "UNKNOWN";

        if (metadata.tckVersion == null || metadata.tckDownloadLink == null || metadata.tckSha == null) {
            warnings.add(group.componentKey + ": no complete TCK metadata found in any job summary.txt in this component group");
        }

        StringBuilder markdown = new StringBuilder();
        markdown.append("## ").append(sectionTitle);
        if (!"UNKNOWN".equals(sectionVersion)) {
            markdown.append(' ').append(sectionVersion);
        }
        markdown.append(NL).append(NL);

        markdown.append(" - [").append(sectionTitle).append(' ').append(exactVersion).append(" TCK](")
                .append(downloadLink).append("), ").append(NL)
                .append("  SHA-256: `").append(sha).append("` <br/> ").append(NL).append(NL);

        if (summaries.size() == 1) {
            markdown.append("```").append(NL).append(summaries.get(0).resultBlock.strip()).append(NL).append("```")
                    .append(NL).append(NL);
        } else {
            for (JobSummary summary : summaries) {
                markdown.append("Job name: ").append(summary.job.relativePath).append(NL).append(NL);
                markdown.append("```").append(NL).append(summary.resultBlock.strip()).append(NL).append("```")
                        .append(NL).append(NL);
            }
        }

        if (isSplitComponentGroup(group)) {
            TestCount total = new TestCount();
            summaries.forEach(summary -> total.add(TestCount.parse(summary.resultBlock)));
            appendSplitTotal(markdown, "Total for " + sectionTitle, total);
        }
        return new ComponentRenderResult(markdown.toString(), warnings);
    }

    private static JobSummary readComponentJobSummary(ArtifactFetchPool fetchPool, JenkinsHttp http, ComponentJob job) throws Exception {
        Optional<ArtifactRef> summaryRef = job.build.findPreferredSummaryTxt();
        if (summaryRef.isEmpty()) {
            return JobSummary.failed(job, "MISSING: no summary.txt artifact found");
        }
        String summaryText = fetchPool.get(http, job.build, summaryRef.get());
        Map<String, String> properties = SummaryPropertyParser.parseProperties(summaryText);
        String resultBlock = ResultBlockExtractor.extractComponent(summaryText);
        if (resultBlock.isBlank()) {
            resultBlock = "MISSING: no result block found in summary.txt";
        }
        return new JobSummary(job, properties, resultBlock);
    }

    private static ComponentMetadata chooseMetadata(ComponentGroup group, List<JobSummary> summaries) {
        ComponentMetadata metadata = new ComponentMetadata();
        metadata.sectionTitle = deriveSectionTitle(group.componentKey);
        for (JobSummary summary : summaries) {
            metadata.tckVersion = firstNonBlank(metadata.tckVersion, summary.properties.get("TCK_VERSION"));
            metadata.tckBundleName = firstNonBlank(metadata.tckBundleName, summary.properties.get("TCK_BUNDLE_NAME"));
            metadata.tckDownloadLink = firstNonBlank(metadata.tckDownloadLink, summary.properties.get("TCK_DOWNLOAD_LINK"));
            metadata.tckSha = firstNonBlank(metadata.tckSha, summary.properties.get("TCK_SHA"));
            metadata.sourceJob = firstNonBlank(metadata.sourceJob, summary.job.relativePath);
        }
        metadata.majorVersion = deriveMajorVersion(metadata.tckDownloadLink, metadata.tckVersion);
        return metadata;
    }

    private static List<PlatformJob> discoverPlatformJobs(List<BuildInventory> builds, String platformFolder) {
        String normalizedFolder = normalizePath(platformFolder);
        return builds.stream()
                .filter(build -> isUnderFolder(build.fullName, normalizedFolder))
                .map(build -> {
                    String relativePath = relativePath(build.fullName, normalizedFolder);
                    return new PlatformJob(build, relativePath, relativePath);
                })
                .filter(job -> !isExcludedPlatformJob(job.sortKey))
                .sorted(Comparator.comparing((PlatformJob job) -> job.sortKey, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private static List<ComponentGroup> discoverComponentGroups(
            List<BuildInventory> builds, String tckFolder, String platformFolder, String requestedComponentKey) {
        String normalizedTckFolder = normalizePath(tckFolder);
        String normalizedPlatformFolder = normalizePath(platformFolder);
        String normalizedRequestedKey = normalizePath(requestedComponentKey);
        Map<String, ComponentGroup> groups = new LinkedHashMap<>();
        for (BuildInventory build : builds) {
            if (!isUnderFolder(build.fullName, normalizedTckFolder) || isUnderFolder(build.fullName, normalizedPlatformFolder)) {
                continue;
            }
            String relative = relativePath(build.fullName, normalizedTckFolder);
            String componentKey = firstPathSegment(relative);
            if (!normalizedRequestedKey.isBlank() && !componentKey.equals(normalizedRequestedKey)) {
                continue;
            }
            groups.computeIfAbsent(componentKey, ComponentGroup::new).jobs.add(new ComponentJob(build, relative));
        }
        groups.values().forEach(group -> group.jobs.sort(Comparator.comparing(job -> job.relativePath, String.CASE_INSENSITIVE_ORDER)));
        return groups.values().stream()
                .sorted(Comparator.comparing(group -> group.componentKey, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private static void prefetchPlatformArtifacts(ArtifactFetchPool fetchPool, JenkinsHttp http, List<PlatformJob> platformJobs) {
        for (PlatformJob job : platformJobs) {
            if (!"SUCCESS".equals(job.build.result)) {
                continue;
            }
            Optional<ArtifactRef> summary = job.build.findPreferredSummaryTxt();
            if (summary.isPresent()) {
                fetchPool.prefetch(http, job.build, summary.get());
            } else if (!job.build.junitXmlArtifacts().isEmpty()) {
                job.build.junitXmlArtifacts().forEach(artifact -> fetchPool.prefetch(http, job.build, artifact));
            } else {
                job.build.findArtifactByFileName("failsafe-summary.xml")
                        .ifPresent(artifact -> fetchPool.prefetch(http, job.build, artifact));
            }
        }
    }

    private static void prefetchComponentSummaries(ArtifactFetchPool fetchPool, JenkinsHttp http, List<ComponentGroup> groups) {
        for (ComponentGroup group : groups) {
            for (ComponentJob job : group.jobs) {
                job.build.findPreferredSummaryTxt().ifPresent(summary -> fetchPool.prefetch(http, job.build, summary));
            }
        }
    }

    private static ReportMetadata collectReportMetadata(
            ArtifactFetchPool fetchPool, JenkinsHttp http, List<PlatformJob> platformJobs, List<ComponentGroup> componentGroups) {
        ReportMetadata result = new ReportMetadata();
        for (PlatformJob job : platformJobs) {
            job.build.findPreferredSummaryTxt()
                    .ifPresent(summary -> collectReportMetadata(fetchPool, http, job.build, summary, result, true, null));
        }
        for (ComponentGroup group : componentGroups) {
            for (ComponentJob job : group.jobs) {
                job.build.findPreferredSummaryTxt()
                        .ifPresent(summary -> collectReportMetadata(fetchPool, http, job.build, summary, result, false, group.componentKey));
            }
        }
        return result;
    }

    private static void collectReportMetadata(
            ArtifactFetchPool fetchPool, JenkinsHttp http, BuildInventory build, ArtifactRef summary, ReportMetadata result,
            boolean platformSummary, String componentKey) {
        try {
            Map<String, String> properties = SummaryPropertyParser.parseProperties(fetchPool.get(http, build, summary));
            String source = build.fullName + " #" + build.buildNumber;
            result.addJavaVersion(source, properties.get("JAVA_VERSION"));
            result.addOsDetails(source, properties.get("OS_DETAILS"));
            if (platformSummary) {
                result.addProductRuntime(source, properties.get("JDK_VERSION"), properties.get("GLASSFISH_DOWNLOAD_LINK"),
                        properties.get("GLASSFISH_SHA"));
                result.addPlatformTckMetadata(source, properties.get("TCK_VERSION"), properties.get("TCK_DOWNLOAD_LINK"),
                        properties.get("TCK_SHA"));
            } else if (componentKey != null && !componentKey.isBlank()) {
                result.addComponentTckMetadata(componentKey,
                        properties.get("TCK_VERSION"),
                        properties.get("TCK_BUNDLE_NAME"),
                        properties.get("TCK_DOWNLOAD_LINK"),
                        properties.get("TCK_SHA"));
            }
        } catch (Exception e) {
            System.out.println("WARNING: could not collect metadata from " + build.fullName + " #" + build.buildNumber
                    + ": " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static void printProductRuntimeDebug(ReportMetadata metadata) {
        if (metadata.productRuntimeSources.isEmpty()) {
            System.out.println("Product runtime debug: no platform GLASSFISH_DOWNLOAD_LINK/JDK_VERSION pairs found in summary.txt artifacts");
            return;
        }
        System.out.println();
        System.out.println("Product runtime debug:");
        for (ProductRuntime runtime : metadata.productRuntimesByKey.values()) {
            System.out.println("- JDK_VERSION=" + runtime.jdkVersion() + "; GLASSFISH_DOWNLOAD_LINK="
                    + runtime.glassFishDownloadLink() + "; GLASSFISH_SHA=" + nullToUnknown(runtime.glassFishSha()));
            metadata.productRuntimeSources.getOrDefault(runtime, List.of()).forEach(source -> System.out.println("    from " + source));
        }
        System.out.println();
    }

    private static void printEnvironmentMetadataDebug(ReportMetadata metadata) {
        System.out.println();
        System.out.println("Environment metadata debug:");
        printMetadataSources("JAVA_VERSION", metadata.javaVersionSources);
        printMetadataSources("OS_DETAILS", metadata.osDetailsSources);
        System.out.println();
    }

    private static void printMetadataSources(String key, Map<String, List<String>> sourcesByValue) {
        if (sourcesByValue.isEmpty()) {
            System.out.println("- " + key + ": <none>");
            return;
        }
        System.out.println("- " + key + ": " + sourcesByValue.size() + " unique value(s)");
        for (Map.Entry<String, List<String>> entry : sourcesByValue.entrySet()) {
            System.out.println("    value: " + entry.getKey());
            for (String source : entry.getValue()) {
                System.out.println("        from " + source);
            }
        }
    }

    private static boolean isExcludedPlatformJob(String relativePath) {
        String normalized = normalizePath(relativePath);
        return DEFAULT_EXCLUDED_PLATFORM_JOB_PREFIXES.stream().anyMatch(normalized::startsWith);
    }

    private static String deriveSectionTitle(String componentKey) {
        String name = componentKey;
        if (name.startsWith("jakarta-")) {
            name = name.substring("jakarta-".length());
        }
        if (name.endsWith("-tck-glassfish")) {
            name = name.substring(0, name.length() - "-tck-glassfish".length());
        }
        if (name.endsWith("-tck")) {
            name = name.substring(0, name.length() - "-tck".length());
        }
        return switch (name) {
            case "activation" -> "Jakarta Activation";
            case "annotations" -> "Jakarta Annotations";
            case "authentication" -> "Jakarta Authentication";
            case "authorization" -> "Jakarta Authorization";
            case "batch" -> "Jakarta Batch";
            case "cdi" -> "Jakarta Contexts and Dependency Injection";
            case "concurrency" -> "Jakarta Concurrency";
            case "data" -> "Jakarta Data";
            case "debugging-support-for-other-languages" -> "Jakarta Debugging Support for Other Languages";
            case "di" -> "Jakarta Dependency Injection";
            case "faces" -> "Jakarta Faces";
            case "jsonb" -> "Jakarta JSON Binding";
            case "jsonp" -> "Jakarta JSON Processing";
            case "mail" -> "Jakarta Mail";
            case "pages" -> "Jakarta Pages";
            case "rest" -> "Jakarta REST";
            case "security" -> "Jakarta Security";
            case "servlet" -> "Jakarta Servlet";
            case "validation" -> "Jakarta Validation";
            case "websocket" -> "Jakarta WebSocket";
            default -> "Jakarta " + titleCaseWords(name);
        };
    }

    private static String deriveMajorVersion(String downloadLink, String tckVersion) {
        String fromLink = deriveMajorVersionFromDownloadLink(downloadLink);
        return fromLink != null ? fromLink : deriveMajorVersionFromVersion(tckVersion);
    }

    private static String deriveMajorVersionFromDownloadLink(String downloadLink) {
        if (downloadLink == null) {
            return null;
        }
        for (String part : downloadLink.split("/")) {
            if (looksLikeMajorMinor(part)) {
                return part;
            }
        }
        return null;
    }

    private static String deriveMajorVersionFromVersion(String version) {
        if (version == null) {
            return null;
        }
        String[] parts = version.split("[.]");
        return parts.length >= 2 && isUnsignedInteger(parts[0]) && isUnsignedInteger(parts[1]) ? parts[0] + "." + parts[1] : null;
    }

    private static boolean looksLikeMajorMinor(String value) {
        int dot = value.indexOf('.');
        return dot > 0 && dot < value.length() - 1
                && isUnsignedInteger(value.substring(0, dot))
                && isUnsignedInteger(value.substring(dot + 1));
    }

    private static boolean isUnsignedInteger(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static String titleCaseWords(String value) {
        List<String> words = new ArrayList<>();
        for (String part : value.split("-")) {
            if (!part.isBlank()) {
                words.add(Character.toUpperCase(part.charAt(0)) + part.substring(1));
            }
        }
        return String.join(" ", words);
    }

    private static String firstNonBlank(String current, String candidate) {
        return current != null && !current.isBlank() ? current : candidate == null || candidate.isBlank() ? null : candidate;
    }

    private static String firstPathSegment(String path) {
        int slash = path.indexOf('/');
        return slash >= 0 ? path.substring(0, slash) : path;
    }

    private static boolean isUnderFolder(String value, String normalizedFolder) {
        String normalizedValue = normalizePath(value);
        return !normalizedFolder.isBlank() && normalizedValue.startsWith(normalizedFolder + "/");
    }

    private static String relativePath(String fullName, String normalizedFolder) {
        String normalizedSource = normalizePath(fullName);
        return normalizedSource.startsWith(normalizedFolder + "/") ? normalizedSource.substring(normalizedFolder.length() + 1) : normalizedSource;
    }

    private static String normalizePath(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value.trim();
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private static String ensureSlash(String url) {
        return url.endsWith("/") ? url : url + "/";
    }

    private static String nullToUnknown(String value) {
        return value == null || value.isBlank() ? "UNKNOWN" : value;
    }

    private static String derivePlatformTckVersionFromLinkText(String linkText) {
        if (linkText == null || linkText.isBlank()) {
            return null;
        }
        int lastSpace = linkText.lastIndexOf(' ');
        return lastSpace >= 0 ? linkText.substring(lastSpace + 1).strip() : linkText.strip();
    }

    private static String glassFishCertificationResultsUrl(Arguments arguments, String productVersion) {
        String version = productVersion == null || productVersion.isBlank() ? "UNKNOWN" : productVersion.strip();
        String profilePath = isCoreProfile(arguments) ? "jakarta-core-profile" : "jakarta-platform";
        return "https://glassfish.org/certifications/" + profilePath + "/11/TCK-Results-" + version + ".html";
    }

    private static String glassFishVersionFromDownloadLink(String downloadLink) {
        String label = glassFishDownloadLabel(downloadLink);
        String withoutZip = label.endsWith(".zip") ? label.substring(0, label.length() - ".zip".length()) : label;
        if (withoutZip.startsWith("glassfish-")) {
            return withoutZip.substring("glassfish-".length());
        }
        if (withoutZip.startsWith("web-")) {
            return withoutZip.substring("web-".length());
        }
        return "UNKNOWN".equals(withoutZip) ? null : withoutZip;
    }

    private static String glassFishDownloadLabel(String downloadLink) {
        if (downloadLink == null || downloadLink.isBlank()) {
            return "UNKNOWN";
        }
        String normalized = downloadLink.strip();
        int query = normalized.indexOf('?');
        if (query >= 0) {
            normalized = normalized.substring(0, query);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        int slash = normalized.lastIndexOf('/');
        return slash >= 0 ? normalized.substring(slash + 1) : normalized;
    }

    private static boolean isSplitComponentGroup(ComponentGroup group) {
        return group.jobs.size() > 1 && group.jobs.stream().anyMatch(job -> job.relativePath.contains("/"));
    }

    private static String folderSegmentOrNull(String relativePath) {
        int slash = relativePath.indexOf('/');
        return slash > 0 ? relativePath.substring(0, slash) : null;
    }

    private static void appendSplitTotal(StringBuilder markdown, String label, TestCount total) {
        markdown.append(label).append(NL).append(NL);
        markdown.append("```").append(NL);
        markdown.append("********************************************************************************").append(NL);
        markdown.append("Total tests run: ").append(total.tests).append(NL);
        markdown.append("********************************************************************************").append(NL);
        markdown.append("```").append(NL).append(NL);
    }

    private static int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    private static void appendJsonField(StringBuilder json, int level, String name, Object value, boolean comma) {
        indent(json, level).append(QUOTE).append(escapeJson(name)).append("\": ").append(jsonValue(value));
        if (comma) {
            json.append(',');
        }
        json.append(NL);
    }

    private static StringBuilder indent(StringBuilder builder, int level) {
        return builder.append("  ".repeat(level));
    }

    private static String jsonValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        return QUOTE + escapeJson(value.toString()) + QUOTE;
    }

    private static String escapeJson(String value) {
        StringBuilder result = new StringBuilder(value.length() + 16);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\\' -> result.append("\\\\");
                case '"' -> result.append("\\\"");
                case '\b' -> result.append("\\b");
                case '\f' -> result.append("\\f");
                case '\n' -> result.append("\\n");
                case '\r' -> result.append("\\r");
                case '\t' -> result.append("\\t");
                default -> {
                    if (c < 0x20) {
                        result.append(String.format("\\u%04x", (int) c));
                    } else {
                        result.append(c);
                    }
                }
            }
        }
        return result.toString();
    }

    private static Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    private static Element childElement(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element element && tagName.equals(element.getTagName())) {
                return element;
            }
        }
        return null;
    }

    private static String childText(Element parent, String tagName) {
        Element child = childElement(parent, tagName);
        return child == null ? null : child.getTextContent();
    }

    private static String encodeQueryParam(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String encodePathSegment(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%2F", "/");
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int intOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    private static Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Boolean parseBoolean(String value) {
        return value == null || value.isBlank() ? null : Boolean.parseBoolean(value.trim());
    }

    private static Boolean extractBooleanField(String object, String fieldName) {
        String field = String.valueOf(QUOTE) + fieldName + QUOTE;
        int fieldIndex = object.indexOf(field);
        if (fieldIndex < 0) {
            return null;
        }
        int colon = object.indexOf(':', fieldIndex);
        if (colon < 0) {
            return null;
        }
        int i = colon + 1;
        while (i < object.length() && Character.isWhitespace(object.charAt(i))) {
            i++;
        }
        if (object.startsWith("true", i)) {
            return true;
        }
        if (object.startsWith("false", i)) {
            return false;
        }
        return null;
    }

    private static Long extractLongField(String object, String fieldName) {
        String field = String.valueOf(QUOTE) + fieldName + QUOTE;
        int fieldIndex = object.indexOf(field);
        if (fieldIndex < 0) {
            return null;
        }
        int colon = object.indexOf(':', fieldIndex);
        if (colon < 0) {
            return null;
        }
        int i = colon + 1;
        while (i < object.length() && Character.isWhitespace(object.charAt(i))) {
            i++;
        }
        if (object.startsWith("null", i)) {
            return null;
        }
        int start = i;
        if (i < object.length() && object.charAt(i) == '-') {
            i++;
        }
        while (i < object.length() && Character.isDigit(object.charAt(i))) {
            i++;
        }
        return start == i ? null : Long.parseLong(object.substring(start, i));
    }

    private static String timestampIso(Long timestampMillis) {
        return timestampMillis == null ? null : DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                Instant.ofEpochMilli(timestampMillis).atOffset(ZoneOffset.UTC));
    }

    private static String abbreviate(String value, int max) {
        if (value == null) {
            return "";
        }
        String normalized = value.replace('\n', ' ').replace('\r', ' ');
        return normalized.length() <= max ? normalized : normalized.substring(0, max) + "...";
    }

    private static final class TestCount {
        private static final Pattern TEST_RESULTS_TOTAL = Pattern.compile("Test results:.*?total:[ \\t]*([0-9]+)");
        private static final Pattern COMPLETED_RUNNING = Pattern.compile("Completed running[ \\t]+([0-9]+)[ \\t]+tests[.]");
        private static final Pattern TOTAL_TESTS_RUN = Pattern.compile("Total tests run:[ \\t]*([0-9]+)");
        private static final Pattern SUREFIRE_TESTS_RUN = Pattern.compile("Tests run:[ \\t]*([0-9]+)[ \\t]*,[ \\t]*Failures:");
        int tests;
        static TestCount parse(String text) {
            TestCount total = new TestCount();
            total.tests += sumMatches(TEST_RESULTS_TOTAL, text);
            total.tests += sumMatches(COMPLETED_RUNNING, text);
            total.tests += sumMatches(TOTAL_TESTS_RUN, text);
            total.tests += sumMatches(SUREFIRE_TESTS_RUN, text);
            return total;
        }
        void add(TestCount other) { tests += other.tests; }
        private static int sumMatches(Pattern pattern, String text) {
            int sum = 0;
            Matcher matcher = pattern.matcher(text == null ? "" : text);
            while (matcher.find()) { sum += parseIntOrZero(matcher.group(1)); }
            return sum;
        }
    }

    private record AdditionalTckRequirement(String componentKey, String label) {}
    private record ProductRuntimeKey(String jdkVersion, String glassFishDownloadLink) {}
    private record ProductRuntime(String jdkVersion, String glassFishDownloadLink, String glassFishSha) {}
    private record PlatformTckMetadata(String tckVersion, String tckDownloadLink, String tckSha) {}
    private record ComponentTckMetadata(String tckVersion, String tckBundleName, String tckDownloadLink, String tckSha) {}
    private record PlatformRenderResult(String markdown, List<String> warnings) {}
    private record ComponentRenderResult(String markdown, List<String> warnings) {}
    private record Extraction(String snippet, String warning) {}
    private record PlatformJob(BuildInventory build, String displayName, String sortKey) {}
    private record ComponentJob(BuildInventory build, String relativePath) {}
    private record JobSummary(ComponentJob job, Map<String, String> properties, String resultBlock) {
        static JobSummary failed(ComponentJob job, String message) { return new JobSummary(job, Map.of(), message); }
    }

    private static final class ComponentGroup {
        final String componentKey;
        final List<ComponentJob> jobs = new ArrayList<>();
        ComponentGroup(String componentKey) { this.componentKey = componentKey; }
    }

    private static final class ComponentMetadata {
        String sectionTitle, majorVersion, tckVersion, tckBundleName, tckDownloadLink, tckSha, sourceJob;
    }

    private static final class ReportMetadata {
        final Set<String> javaVersions = new LinkedHashSet<>();
        final Set<String> osDetails = new LinkedHashSet<>();
        final Map<String, List<String>> javaVersionSources = new LinkedHashMap<>();
        final Map<String, List<String>> osDetailsSources = new LinkedHashMap<>();
        final Map<String, String> componentTckDownloadLinksByKey = new LinkedHashMap<>();
        final Map<String, ComponentTckMetadata> componentTckMetadataByKey = new LinkedHashMap<>();
        final Map<ProductRuntimeKey, ProductRuntime> productRuntimesByKey = new LinkedHashMap<>();
        final Map<ProductRuntime, List<String>> productRuntimeSources = new LinkedHashMap<>();
        String platformTckVersion, platformTckDownloadLink, platformTckSha, platformTckMetadataSource;

        void addJavaVersion(String source, String value) { addNormalized(javaVersions, javaVersionSources, source, value); }
        void addOsDetails(String source, String value) { addNormalized(osDetails, osDetailsSources, source, value); }

        void addComponentTckMetadata(String componentKey, String tckVersion, String tckBundleName, String tckDownloadLink, String tckSha) {
            String version = normalizeMetadataValue(tckVersion);
            String bundle = normalizeMetadataValue(tckBundleName);
            String link = normalizeMetadataValue(tckDownloadLink);
            String sha = normalizeMetadataValue(tckSha);
            if (!componentKey.isBlank() && !link.isBlank()) {
                componentTckDownloadLinksByKey.putIfAbsent(componentKey, link);
                componentTckMetadataByKey.putIfAbsent(componentKey,
                        new ComponentTckMetadata(version, bundle, link, sha));
            }
        }

        void addProductRuntime(String source, String jdkVersion, String glassFishDownloadLink, String glassFishSha) {
            String jdk = normalizeMetadataValue(jdkVersion);
            String link = normalizeMetadataValue(glassFishDownloadLink);
            String sha = normalizeMetadataValue(glassFishSha);
            if (jdk.isBlank() || link.isBlank()) { return; }
            ProductRuntimeKey key = new ProductRuntimeKey(jdk, link);
            ProductRuntime runtime = productRuntimesByKey.get(key);
            if (runtime == null) {
                runtime = new ProductRuntime(jdk, link, sha.isBlank() ? null : sha);
                productRuntimesByKey.put(key, runtime);
            } else if ((runtime.glassFishSha() == null || runtime.glassFishSha().isBlank()) && !sha.isBlank()) {
                List<String> sources = productRuntimeSources.remove(runtime);
                runtime = new ProductRuntime(runtime.jdkVersion(), runtime.glassFishDownloadLink(), sha);
                productRuntimesByKey.put(key, runtime);
                if (sources != null) { productRuntimeSources.put(runtime, sources); }
            }
            productRuntimeSources.computeIfAbsent(runtime, ignored -> new ArrayList<>()).add(source);
        }

        void addPlatformTckMetadata(String source, String tckVersion, String tckDownloadLink, String tckSha) {
            String version = normalizeMetadataValue(tckVersion);
            String link = normalizeMetadataValue(tckDownloadLink);
            String sha = normalizeMetadataValue(tckSha);
            if (version.isBlank() || link.isBlank() || sha.isBlank()) {
                return;
            }
            if (platformTckVersion == null || platformTckVersion.isBlank()) {
                platformTckVersion = version;
                platformTckDownloadLink = link;
                platformTckSha = sha;
                platformTckMetadataSource = source;
            }
        }

        PlatformTckMetadata platformTckMetadataOrDefault(Arguments arguments) {
            String version = firstNonBlank(platformTckVersion, derivePlatformTckVersionFromLinkText(arguments.platformTckLinkText));
            String link = firstNonBlank(platformTckDownloadLink, arguments.platformTckDownloadLink);
            String sha = firstNonBlank(platformTckSha, arguments.platformTckSha);
            return new PlatformTckMetadata(version == null ? "UNKNOWN" : version, link == null ? "UNKNOWN" : link, sha == null ? "UNKNOWN" : sha);
        }

        String productVersion() {
            return productRuntimesByKey.values().stream()
                    .map(runtime -> glassFishVersionFromDownloadLink(runtime.glassFishDownloadLink()))
                    .filter(Objects::nonNull).filter(value -> !value.isBlank()).findFirst().orElse(null);
        }

        private static void addNormalized(Set<String> values, Map<String, List<String>> sourcesByValue, String source, String value) {
            String normalized = normalizeMetadataValue(value);
            if (!normalized.isBlank()) {
                values.add(normalized);
                sourcesByValue.computeIfAbsent(normalized, ignored -> new ArrayList<>()).add(source);
            }
        }
    }

    private static String normalizeMetadataValue(String value) {
        if (value == null) { return ""; }
        return value.replace(String.valueOf(CR) + LF, " ")
                .replace(String.valueOf(LF), " ")
                .replace(String.valueOf(CR), " ")
                .replaceAll("[ \\t]+", " ")
                .strip();
    }

    private static final class SummaryPropertyParser {
        static Map<String, String> parseProperties(String summary) {
            Map<String, String> properties = new LinkedHashMap<>();
            for (String rawLine : splitLines(summary)) {
                String line = rawLine.strip();
                if (line.isEmpty() || line.startsWith("*") || line.startsWith("=")) { continue; }
                int equals = line.indexOf('=');
                if (equals <= 0) { continue; }
                String key = line.substring(0, equals).trim();
                String value = line.substring(equals + 1).trim();
                if (!key.isBlank()) { properties.putIfAbsent(key, value); }
            }
            return properties;
        }
    }

    private static final class ResultBlockExtractor {
        static String extractPlatform(String summary) { return extract(summary, true); }
        static String extractComponent(String summary) { return extract(summary, false); }
        static String extract(String summary, boolean platform) {
            List<String> result = new ArrayList<>();
            List<String> current = new ArrayList<>();
            boolean inBlock = false;
            boolean blockHasResult = false;
            String separator = null;
            for (String rawLine : splitLines(summary)) {
                String line = rawLine.strip();
                if (line.isEmpty()) { continue; }
                if (separatorLine(line)) {
                    if (separator == null) { separator = line; }
                    if (!inBlock) {
                        inBlock = true; blockHasResult = false; current.clear(); current.add(line);
                    } else {
                        current.add(line);
                        if (blockHasResult) { result.addAll(current); }
                        inBlock = false; blockHasResult = false; current.clear();
                    }
                    continue;
                }
                if (inBlock) {
                    current.add(line);
                    if (isResultLine(line)) { blockHasResult = true; }
                } else if (isResultLine(line) || isTckTitle(line, platform)) {
                    result.add(line);
                }
            }
            if (result.isEmpty()) { return ""; }
            boolean allSurefire = result.stream().allMatch(ResultBlockExtractor::isSurefireLine);
            if (platform && separator != null && !allSurefire && !result.get(0).equals(separator)) {
                List<String> wrapped = new ArrayList<>();
                wrapped.add(separator); wrapped.addAll(result); wrapped.add(separator);
                return String.join(NL, wrapped);
            }
            return String.join(NL, result);
        }
        private static boolean isResultLine(String line) {
            return line.startsWith("Test results:") || line.startsWith("Completed running ") || line.startsWith("Number of Tests Passed")
                    || line.startsWith("Number of Tests Failed") || line.startsWith("Number of Tests with Errors")
                    || line.startsWith("Number of Tests Skipped") || line.startsWith("Total tests run:") || isSurefireLine(line);
        }
        private static boolean isSurefireLine(String line) {
            return line.startsWith("Tests run:") && line.contains("Failures:") && line.contains("Errors:") && line.contains("Skipped:");
        }
        private static boolean isTckTitle(String line, boolean platform) {
            return line.contains("TCK") && !line.contains("=") && line.length() <= 100
                    && (!platform || !line.startsWith("Jakarta EE Platform TCK"));
        }
    }

    private static boolean separatorLine(String line) {
        if (line.length() < 20) { return false; }
        char first = line.charAt(0);
        if (first != '*' && first != '=') { return false; }
        for (int i = 1; i < line.length(); i++) {
            if (line.charAt(i) != first) { return false; }
        }
        return true;
    }

    private static List<String> splitLines(String value) {
        String normalized = value.replace(String.valueOf(CR) + LF, String.valueOf(LF)).replace(CR, LF);
        String[] split = normalized.split(String.valueOf(LF));
        List<String> result = new ArrayList<>(split.length);
        for (String line : split) { result.add(line); }
        return result;
    }

    private static final class JUnitXmlTotals {
        int tests, failures, errors, skipped;
        static JUnitXmlTotals parseLenient(String xml) {
            String tag = firstTag(xml, "testsuite");
            if (tag == null) { tag = firstTag(xml, "testsuites"); }
            if (tag == null) { throw new IllegalArgumentException("No testsuite tag found"); }
            JUnitXmlTotals totals = new JUnitXmlTotals();
            totals.tests = xmlIntAttribute(tag, "tests");
            totals.failures = xmlIntAttribute(tag, "failures");
            totals.errors = xmlIntAttribute(tag, "errors");
            totals.skipped = xmlIntAttribute(tag, "skipped");
            return totals;
        }
        void add(JUnitXmlTotals other) { tests += other.tests; failures += other.failures; errors += other.errors; skipped += other.skipped; }
        String toSurefireLine() { return "Tests run: " + tests + ", Failures: " + failures + ", Errors: " + errors + ", Skipped: " + skipped; }
    }

    private static String firstTag(String xml, String name) {
        int start = xml.indexOf("<" + name);
        if (start < 0) { return null; }
        int end = xml.indexOf('>', start);
        return end < 0 ? null : xml.substring(start, end + 1);
    }

    private static int xmlIntAttribute(String tag, String name) {
        String prefix = name + "=" + QUOTE;
        int start = tag.indexOf(prefix);
        if (start < 0) { return 0; }
        start += prefix.length();
        int end = tag.indexOf(QUOTE, start);
        return end < 0 ? 0 : parseIntOrZero(tag.substring(start, end));
    }

    private static final class FailsafeSummaryTotals {
        int completed, failures, errors;
        static FailsafeSummaryTotals parse(String xml) {
            FailsafeSummaryTotals totals = new FailsafeSummaryTotals();
            totals.completed = xmlElementInt(xml, "completed");
            totals.failures = xmlElementInt(xml, "failures");
            totals.errors = xmlElementInt(xml, "errors");
            return totals;
        }
        String toCompletedBlock() {
            return String.join(NL, "********************************************************************************", "Completed running " + completed + " tests.",
                    "Number of Tests Failed      = " + failures, "Number of Tests with Errors = " + errors,
                    "********************************************************************************");
        }
    }

    private static int xmlElementInt(String xml, String name) {
        String open = "<" + name + ">";
        String close = "</" + name + ">";
        int start = xml.indexOf(open);
        if (start < 0) { return 0; }
        start += open.length();
        int end = xml.indexOf(close, start);
        return end < 0 ? 0 : parseIntOrZero(xml.substring(start, end).trim());
    }

    private static final class ArtifactFetchPool implements AutoCloseable {
        private final ExecutorService executor;
        private final Map<String, Future<String>> futures = new LinkedHashMap<>();
        ArtifactFetchPool(int threads) {
            if (threads < 1) { throw new IllegalArgumentException("fetch threads must be >= 1"); }
            executor = Executors.newFixedThreadPool(threads);
        }
        synchronized void prefetch(JenkinsHttp http, BuildInventory build, ArtifactRef artifact) {
            futures.computeIfAbsent(key(build, artifact), ignored -> executor.submit(() -> http.getText(artifactUrl(build, artifact))));
        }
        String get(JenkinsHttp http, BuildInventory build, ArtifactRef artifact) throws Exception {
            prefetch(http, build, artifact);
            try { return futures.get(key(build, artifact)).get(); }
            catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof Exception exception) { throw exception; }
                throw new RuntimeException(cause);
            }
        }
        private String key(BuildInventory build, ArtifactRef artifact) { return build.buildUrl + "::" + artifact.relativePath; }
        @Override public void close() {
            executor.shutdown();
            try { if (!executor.awaitTermination(30, TimeUnit.SECONDS)) { executor.shutdownNow(); } }
            catch (InterruptedException e) { executor.shutdownNow(); Thread.currentThread().interrupt(); }
        }
    }

    private static String artifactUrl(BuildInventory build, ArtifactRef artifact) { return ensureSlash(build.buildUrl) + "artifact/" + artifact.relativePath; }

    private static final class BuildInventory {
        int index;
        String project, fullName, jobUrl, selectedBuildKind, inputBuildUrl, buildUrl, result, timestamp;
        String consoleProbeUrl, consoleSizeHeader, consoleError, error;
        Integer buildNumber, inputBuildNumber;
        Long durationMillis;
        Boolean building, ok, consoleReachable;
        int artifactCount, surefireXmlCount, failsafeXmlCount;
        boolean hasSummaryTxtFlag, hasRunLog, hasServerLog, hasFailsafeSummaryXml;
        boolean hasSurefireXmlFlag, hasFailsafeXmlFlag, hasFailsafeHtml, hasHtmlOrReports;
        List<ArtifactRef> artifacts = new ArrayList<>();
        boolean hasSummaryTxt() { return hasSummaryTxtFlag || findArtifactByFileName("summary.txt").isPresent(); }
        boolean hasSurefireXml() { return hasSurefireXmlFlag || artifacts.stream().anyMatch(a -> a.relativePath.contains("/surefire-reports/") && a.relativePath.endsWith(".xml")); }
        boolean hasFailsafeXml() { return hasFailsafeXmlFlag || artifacts.stream().anyMatch(a -> a.relativePath.contains("/failsafe-reports/") && a.relativePath.endsWith(".xml")); }
        Optional<ArtifactRef> findArtifactByFileName(String fileName) { return artifacts.stream().filter(a -> fileName.equals(a.fileName)).findFirst(); }
        Optional<ArtifactRef> findPreferredSummaryTxt() {
            Optional<ArtifactRef> root = artifacts.stream().filter(a -> "summary.txt".equals(a.relativePath)).findFirst();
            if (root.isPresent()) { return root; }
            Optional<ArtifactRef> nonJt = artifacts.stream().filter(a -> "summary.txt".equals(a.fileName))
                    .filter(a -> !a.relativePath.contains("/JTreport/")).filter(a -> !a.relativePath.contains("/JTreport-Pluggability/"))
                    .min(Comparator.comparingInt(a -> a.relativePath.length()));
            return nonJt.isPresent() ? nonJt : artifacts.stream().filter(a -> "summary.txt".equals(a.fileName)).min(Comparator.comparingInt(a -> a.relativePath.length()));
        }
        List<ArtifactRef> junitXmlArtifacts() {
            return artifacts.stream().filter(a -> a.relativePath.endsWith(".xml"))
                    .filter(a -> a.relativePath.contains("/surefire-reports/") || a.relativePath.contains("/failsafe-reports/"))
                    .filter(a -> !"failsafe-summary.xml".equals(a.fileName))
                    .filter(a -> a.fileName.startsWith("TEST-") || a.relativePath.contains("/junitreports/"))
                    .sorted(Comparator.comparing(a -> a.relativePath, String.CASE_INSENSITIVE_ORDER)).toList();
        }
    }

    private static final class ArtifactRef { String fileName, relativePath; }

    private static final class InventoryJsonReader {
        static List<BuildInventory> readBuilds(String json) {
            String buildsArray = extractArray(json, "builds");
            List<BuildInventory> builds = new ArrayList<>();
            for (String buildObject : splitTopLevelObjects(buildsArray)) {
                BuildInventory build = new BuildInventory();
                build.project = extractStringField(buildObject, "project");
                build.fullName = extractStringField(buildObject, "fullName");
                build.buildUrl = extractStringField(buildObject, "buildUrl");
                build.buildNumber = extractIntegerField(buildObject, "buildNumber");
                build.result = extractStringField(buildObject, "result");
                String artifactsArray = extractArrayOrNull(buildObject, "artifacts");
                if (artifactsArray != null) {
                    for (String artifactObject : splitTopLevelObjects(artifactsArray)) {
                        ArtifactRef artifact = new ArtifactRef();
                        artifact.fileName = extractStringField(artifactObject, "fileName");
                        artifact.relativePath = extractStringField(artifactObject, "relativePath");
                        if (artifact.fileName != null && artifact.relativePath != null) { build.artifacts.add(artifact); }
                    }
                }
                if (build.fullName != null && build.buildUrl != null) { builds.add(build); }
            }
            return builds;
        }
        private static String extractArray(String json, String fieldName) {
            String result = extractArrayOrNull(json, fieldName);
            if (result == null) { throw new IllegalArgumentException("No array field found: " + fieldName); }
            return result;
        }
        private static String extractArrayOrNull(String json, String fieldName) {
            String field = String.valueOf(QUOTE) + fieldName + QUOTE;
            int fieldIndex = json.indexOf(field);
            if (fieldIndex < 0) { return null; }
            int start = json.indexOf('[', fieldIndex);
            if (start < 0) { return null; }
            int end = findMatching(json, start, '[', ']');
            return json.substring(start + 1, end);
        }
        private static List<String> splitTopLevelObjects(String arrayContent) {
            List<String> objects = new ArrayList<>();
            int depth = 0, start = -1;
            boolean inString = false, escape = false;
            for (int i = 0; i < arrayContent.length(); i++) {
                char c = arrayContent.charAt(i);
                if (escape) { escape = false; continue; }
                if (c == BACKSLASH && inString) { escape = true; continue; }
                if (c == QUOTE) { inString = !inString; continue; }
                if (inString) { continue; }
                if (c == '{') { if (depth == 0) { start = i; } depth++; }
                else if (c == '}') { depth--; if (depth == 0 && start >= 0) { objects.add(arrayContent.substring(start, i + 1)); start = -1; } }
            }
            return objects;
        }
        private static int findMatching(String value, int start, char open, char close) {
            int depth = 0;
            boolean inString = false, escape = false;
            for (int i = start; i < value.length(); i++) {
                char c = value.charAt(i);
                if (escape) { escape = false; continue; }
                if (c == BACKSLASH && inString) { escape = true; continue; }
                if (c == QUOTE) { inString = !inString; continue; }
                if (inString) { continue; }
                if (c == open) { depth++; }
                else if (c == close) { depth--; if (depth == 0) { return i; } }
            }
            throw new IllegalArgumentException("Unclosed structure starting at " + start);
        }
        private static String extractStringField(String object, String fieldName) {
            String field = String.valueOf(QUOTE) + fieldName + QUOTE;
            int fieldIndex = object.indexOf(field);
            if (fieldIndex < 0) { return null; }
            int colon = object.indexOf(':', fieldIndex);
            if (colon < 0) { return null; }
            int quoteStart = object.indexOf(QUOTE, colon + 1);
            if (quoteStart < 0) { return null; }
            StringBuilder raw = new StringBuilder();
            boolean escape = false;
            for (int i = quoteStart + 1; i < object.length(); i++) {
                char c = object.charAt(i);
                if (escape) { raw.append(BACKSLASH).append(c); escape = false; continue; }
                if (c == BACKSLASH) { escape = true; continue; }
                if (c == QUOTE) { return unescapeJsonString(raw.toString()); }
                raw.append(c);
            }
            return null;
        }
        private static Integer extractIntegerField(String object, String fieldName) {
            String field = String.valueOf(QUOTE) + fieldName + QUOTE;
            int fieldIndex = object.indexOf(field);
            if (fieldIndex < 0) { return null; }
            int colon = object.indexOf(':', fieldIndex);
            if (colon < 0) { return null; }
            int i = colon + 1;
            while (i < object.length() && Character.isWhitespace(object.charAt(i))) { i++; }
            if (object.startsWith("null", i)) { return null; }
            int start = i;
            if (i < object.length() && object.charAt(i) == '-') { i++; }
            while (i < object.length() && Character.isDigit(object.charAt(i))) { i++; }
            return start == i ? null : Integer.parseInt(object.substring(start, i));
        }
        private static String unescapeJsonString(String value) {
            StringBuilder result = new StringBuilder(value.length());
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (c != BACKSLASH || i + 1 >= value.length()) { result.append(c); continue; }
                char next = value.charAt(++i);
                switch (next) {
                    case '"' -> result.append(QUOTE);
                    case '/' -> result.append('/');
                    case 'b' -> result.append((char) 8);
                    case 'f' -> result.append((char) 12);
                    case 'n' -> result.append(LF);
                    case 'r' -> result.append(CR);
                    case 't' -> result.append(TAB);
                    case 'u' -> { result.append((char) Integer.parseInt(value.substring(i + 1, i + 5), 16)); i += 4; }
                    default -> result.append(next);
                }
            }
            return result.toString();
        }
    }


    private static final class OverviewJsonReader {
        static List<JobOverview> readJobs(String json) {
            String jobsArray = InventoryJsonReader.extractArray(json, "jobs");
            List<JobOverview> jobs = new ArrayList<>();
            for (String jobObject : InventoryJsonReader.splitTopLevelObjects(jobsArray)) {
                JobOverview job = new JobOverview(
                        intOrZero(InventoryJsonReader.extractIntegerField(jobObject, "index")),
                        InventoryJsonReader.extractStringField(jobObject, "project"),
                        InventoryJsonReader.extractStringField(jobObject, "jobUrl"),
                        extractBooleanField(jobObject, "ok"),
                        InventoryJsonReader.extractStringField(jobObject, "name"),
                        InventoryJsonReader.extractStringField(jobObject, "fullName"),
                        buildRefFromJson(jobObject, "lastBuild"),
                        buildRefFromJson(jobObject, "lastCompletedBuild"),
                        buildRefFromJson(jobObject, "lastSuccessfulBuild"),
                        InventoryJsonReader.extractStringField(jobObject, "error")
                );
                if (job.project() != null && job.jobUrl() != null) {
                    jobs.add(job);
                }
            }
            return jobs;
        }

        private static BuildRef buildRefFromJson(String object, String fieldName) {
            String buildObject = extractObjectOrNull(object, fieldName);
            if (buildObject == null || "null".equals(buildObject.strip())) {
                return null;
            }
            return new BuildRef(
                    InventoryJsonReader.extractIntegerField(buildObject, "number"),
                    InventoryJsonReader.extractStringField(buildObject, "url"),
                    InventoryJsonReader.extractStringField(buildObject, "result"),
                    InventoryJsonReader.extractStringField(buildObject, "timestamp"),
                    extractLongField(buildObject, "durationMillis"),
                    extractBooleanField(buildObject, "building")
            );
        }

        private static String extractObjectOrNull(String json, String fieldName) {
            String field = String.valueOf(QUOTE) + fieldName + QUOTE;
            int fieldIndex = json.indexOf(field);
            if (fieldIndex < 0) {
                return null;
            }
            int colon = json.indexOf(':', fieldIndex);
            if (colon < 0) {
                return null;
            }
            int i = colon + 1;
            while (i < json.length() && Character.isWhitespace(json.charAt(i))) {
                i++;
            }
            if (json.startsWith("null", i)) {
                return null;
            }
            if (i >= json.length() || json.charAt(i) != '{') {
                return null;
            }
            int end = InventoryJsonReader.findMatching(json, i, '{', '}');
            return json.substring(i, end + 1);
        }
    }

    private record BuildRef(Integer number, String url, String result, String timestamp, Long durationMillis, Boolean building) {}
    private record JobOverview(int index, String project, String jobUrl, boolean ok, String name, String fullName,
                               BuildRef lastBuild, BuildRef lastCompletedBuild, BuildRef lastSuccessfulBuild, String error) {}

    private static final class DiscoveredJob {
        int index;
        String project, fullName, jobUrl, selectedBuildKind, buildUrl;
        Integer buildNumber;
    }

    private static final class ConsoleProbe {
        String url, textSize, error;
        Boolean reachable;
    }

    private static final class JenkinsHttp {
        private final HttpClient client;
        private final String authorizationHeader;
        private final int timeoutSeconds;

        JenkinsHttp(String user, String token, int timeoutSeconds) {
            this.authorizationHeader = basicAuthenticationHeader(user, token);
            this.timeoutSeconds = timeoutSeconds;
            this.client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                    .build();
        }

        String getText(String url) throws Exception {
            HttpResponse<String> response = get(url);
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String body = response.body() == null ? "" : response.body();
                throw new IOException("GET " + url + " returned HTTP " + response.statusCode()
                        + "; headers=" + response.headers().map()
                        + "; body-preview=" + abbreviate(body, 500));
            }
            return response.body();
        }

        HttpResponse<String> get(String url) throws Exception {
            System.out.println("[" + Thread.currentThread().getId() + "]" + "Fetching " + url);

            for (int i = 0; i < 3; i++) {
                try {
                    HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                            .GET()
                            .version(HttpClient.Version.HTTP_1_1)
                            .timeout(Duration.ofSeconds(timeoutSeconds))
                            .header("User-Agent", "curl/8.0 jakartaee-tck-result-inventory/0.1")
                            .header("Accept", "application/xml,text/xml,text/plain,*/*");

                    if (authorizationHeader != null) {
                        builder.header("Authorization", authorizationHeader);
                    }

                    HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));


                    System.out.println("[" + Thread.currentThread().getId() + "]" + "Fetched " + url + " status = " + response.statusCode());

                    return response;
                } catch (Exception e) {
                    System.out.println("[" + Thread.currentThread().getId() + "]" + " Exception when fetching " + url);
                    if (i < 2) {
                        Thread.sleep(100);
                    } else {
                        throw e;
                    }
                }
            }

            return null;
        }

        private static String abbreviate(String value, int max) {
            if (value == null) {
                return "";
            }
            String normalized = value.replace('\n', ' ').replace('\r', ' ');
            return normalized.length() <= max ? normalized : normalized.substring(0, max) + "...";
        }


        private static String basicAuthenticationHeader(String user, String token) {
            if (user == null || user.isBlank() || token == null || token.isBlank()) {
                return null;
            }
            String credentials = stripTrailingLineBreaks(user) + ":" + stripTrailingLineBreaks(token);
            String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            return "Basic " + encoded;
        }

        private static String stripTrailingLineBreaks(String value) {
            while (value.endsWith("\n") || value.endsWith("\r")) {
                value = value.substring(0, value.length() - 1);
            }
            return value;
        }
    }

    private static final class Arguments {
        String aggregatorUrl;
        String configFile;
        String jobsBaseUrl;
        String overviewTree = DEFAULT_OVERVIEW_TREE;
        String overviewJsonOut = "jenkins-tck-jobs-overview.json";
        String inventoryJsonOut = "jenkins-tck-result-inventory.json";
        String reportOut = "tck-report-sections.md";
        String ccrOut = "ccr.md";
        String buildSelector = "lastCompletedBuild";
        String tckFolder = DEFAULT_TCK_FOLDER;
        String platformFolder = DEFAULT_PLATFORM_FOLDER;
        String componentKey = DEFAULT_COMPONENT_KEY;
        String certificationProfile = CERTIFICATION_PROFILE_AUTO;
        String user = System.getenv("JENKINS_USER");
        String token = System.getenv("JENKINS_TOKEN");
        int timeoutSeconds = 30;
        int threads = 8;
        int fetchThreads = 8;
        int sleepMillis;
        boolean skipDiscover;
        boolean skipInventory;
        boolean skipReport;
        boolean skipCcr;
        boolean skipPlatform;
        boolean skipComponents;
        String platformTitle = DEFAULT_PLATFORM_TITLE;
        String platformVersion = DEFAULT_PLATFORM_VERSION;
        String platformTckLinkText = DEFAULT_PLATFORM_TCK_LINK_TEXT;
        String platformTckDownloadLink = DEFAULT_PLATFORM_TCK_DOWNLOAD_LINK;
        String platformTckSha = DEFAULT_PLATFORM_TCK_SHA;

        static Arguments parse(String[] args) {
            Arguments result = new Arguments();
            Map<String, String> values = parseOptions(args);
            if (values.containsKey("--help")) { printUsageAndExit(); }
            result.aggregatorUrl = values.getOrDefault("--aggregator-url", result.aggregatorUrl);
            result.configFile = values.getOrDefault("--config-file", result.configFile);
            result.jobsBaseUrl = values.getOrDefault("--jobs-base-url", result.jobsBaseUrl);
            result.overviewTree = values.getOrDefault("--tree", result.overviewTree);
            result.overviewJsonOut = values.getOrDefault("--overview-json-out", result.overviewJsonOut);
            result.inventoryJsonOut = values.getOrDefault("--inventory-json-out", result.inventoryJsonOut);
            result.inventoryJsonOut = values.getOrDefault("--inventory", result.inventoryJsonOut);
            result.reportOut = values.getOrDefault("--report-out", result.reportOut);
            result.reportOut = values.getOrDefault("--out", result.reportOut);
            result.ccrOut = values.getOrDefault("--ccr-out", result.ccrOut);
            result.buildSelector = values.getOrDefault("--build-selector", result.buildSelector);
            result.tckFolder = values.getOrDefault("--tck-folder", result.tckFolder);
            result.platformFolder = values.getOrDefault("--platform-folder", result.platformFolder);
            result.componentKey = values.getOrDefault("--component-key", result.componentKey);
            result.user = values.getOrDefault("--user", result.user);
            result.token = values.getOrDefault("--token", result.token);
            result.timeoutSeconds = Integer.parseInt(values.getOrDefault("--timeout", String.valueOf(result.timeoutSeconds)));
            result.threads = Integer.parseInt(values.getOrDefault("--threads", String.valueOf(result.threads)));
            result.fetchThreads = Integer.parseInt(values.getOrDefault("--fetch-threads", String.valueOf(result.threads)));
            result.sleepMillis = Integer.parseInt(values.getOrDefault("--sleep-millis", String.valueOf(result.sleepMillis)));
            result.skipDiscover = Boolean.parseBoolean(values.getOrDefault("--skip-discover", "false"));
            result.skipInventory = Boolean.parseBoolean(values.getOrDefault("--skip-inventory", "false"));
            result.skipReport = Boolean.parseBoolean(values.getOrDefault("--skip-report", "false"));
            result.skipCcr = Boolean.parseBoolean(values.getOrDefault("--skip-ccr", "false"));
            result.skipPlatform = Boolean.parseBoolean(values.getOrDefault("--skip-platform", "false"));
            result.skipComponents = Boolean.parseBoolean(values.getOrDefault("--skip-components", "false"));
            result.platformTitle = values.getOrDefault("--platform-title", result.platformTitle);
            result.platformVersion = values.getOrDefault("--platform-version", result.platformVersion);
            result.platformTckLinkText = values.getOrDefault("--platform-tck-link-text", result.platformTckLinkText);
            result.platformTckDownloadLink = values.getOrDefault("--platform-tck-download-link", result.platformTckDownloadLink);
            result.platformTckSha = values.getOrDefault("--platform-tck-sha", result.platformTckSha);
            result.certificationProfile = values.getOrDefault("--certification-profile", result.certificationProfile);

            result.applyCertificationProfileDefaults(values);

            if (result.threads < 1) { throw new IllegalArgumentException("--threads must be >= 1"); }
            if (result.fetchThreads < 1) { throw new IllegalArgumentException("--fetch-threads must be >= 1"); }
            if (!List.of("lastCompletedBuild", "lastSuccessfulBuild", "lastBuild").contains(result.buildSelector)) {
                throw new IllegalArgumentException("--build-selector must be lastCompletedBuild, lastSuccessfulBuild, or lastBuild");
            }
            if (!List.of(CERTIFICATION_PROFILE_AUTO, CERTIFICATION_PROFILE_PLATFORM, CERTIFICATION_PROFILE_CORE).contains(result.certificationProfile)) {
                throw new IllegalArgumentException("--certification-profile must be auto, platform, or core");
            }
            if (!result.skipDiscover && result.configFile == null && result.aggregatorUrl == null) {
                throw new IllegalArgumentException("Either --aggregator-url or --config-file is required unless --skip-discover true");
            }
            if (!result.skipDiscover && result.configFile != null && result.jobsBaseUrl == null && result.aggregatorUrl == null) {
                throw new IllegalArgumentException("--jobs-base-url is required with --config-file unless --aggregator-url is also provided for inference");
            }
            if (!result.skipReport && result.skipPlatform && result.skipComponents) {
                throw new IllegalArgumentException("Cannot use both --skip-platform true and --skip-components true unless --skip-report true");
            }
            return result;
        }
        private void applyCertificationProfileDefaults(Map<String, String> values) {
            if (!CERTIFICATION_PROFILE_CORE.equals(effectiveCertificationProfile())) {
                return;
            }
            if (!values.containsKey("--platform-folder")) {
                platformFolder = DEFAULT_CORE_PROFILE_FOLDER;
            }
            if (!values.containsKey("--platform-title")) {
                platformTitle = DEFAULT_CORE_PROFILE_TITLE;
            }
            if (!values.containsKey("--platform-tck-link-text")) {
                platformTckLinkText = DEFAULT_CORE_PROFILE_TCK_LINK_TEXT;
            }
            if (!values.containsKey("--platform-tck-download-link")) {
                platformTckDownloadLink = DEFAULT_CORE_PROFILE_TCK_DOWNLOAD_LINK;
            }
            if (!values.containsKey("--platform-tck-sha")) {
                platformTckSha = DEFAULT_CORE_PROFILE_TCK_SHA;
            }
        }

        String effectiveCertificationProfile() {
            if (CERTIFICATION_PROFILE_CORE.equals(certificationProfile) || CERTIFICATION_PROFILE_PLATFORM.equals(certificationProfile)) {
                return certificationProfile;
            }
            String url = aggregatorUrl == null ? "" : aggregatorUrl.toLowerCase();
            String folder = normalizePath(platformFolder).toLowerCase();
            if (url.contains("run-core") || folder.endsWith("/core-profile") || folder.equals("core-profile")) {
                return CERTIFICATION_PROFILE_CORE;
            }
            return CERTIFICATION_PROFILE_PLATFORM;
        }

        private static Map<String, String> parseOptions(String[] args) {
            Map<String, String> result = new LinkedHashMap<>();
            for (int i = 0; i < args.length; i++) {
                String key = args[i];
                if (Objects.equals(key, "--help")) { result.put(key, "true"); continue; }
                if (!key.startsWith("--")) { throw new IllegalArgumentException("Unexpected argument: " + key); }
                if (i + 1 >= args.length) { throw new IllegalArgumentException("Missing value for " + key); }
                result.put(key, args[++i]);
            }
            return result;
        }
        private static void printUsageAndExit() {
            System.out.println("Usage:");
            System.out.println("  java aggregator.JenkinsTckCertificationWorkflow \\");
            System.out.println("    --aggregator-url URL \\");
            System.out.println("    --jobs-base-url URL \\");
            System.out.println("    --overview-json-out jenkins-tck-jobs-overview.json \\");
            System.out.println("    --inventory-json-out jenkins-tck-result-inventory.json \\");
            System.out.println("    --report-out TCK-Results.md");
            System.out.println();
            System.out.println("Workflow options:");
            System.out.println("  --aggregator-url URL               aggregator Jenkins job URL; fetches config.xml");
            System.out.println("  --config-file FILE                 local aggregator config.xml instead of fetching it");
            System.out.println("  --jobs-base-url URL                base URL containing downstream jobs");
            System.out.println("  --skip-discover true|false         use existing overview JSON; default: false");
            System.out.println("  --skip-inventory true|false        use existing inventory JSON; default: false");
            System.out.println("  --skip-report true|false           do not render main report Markdown; default: false");
            System.out.println("  --skip-ccr true|false              do not render CCR Markdown; default: false");
            System.out.println("  --build-selector NAME              lastCompletedBuild|lastSuccessfulBuild|lastBuild; default: lastCompletedBuild");
            System.out.println("  --threads N                        inventory fetch threads; default: 8");
            System.out.println("  --fetch-threads N                  report artifact fetch threads; default: --threads");
            System.out.println("  --sleep-millis N                   optional throttle between job submissions; default: 0");
            System.out.println();
            System.out.println("Output options:");
            System.out.println("  --overview-json-out FILE           default: jenkins-tck-jobs-overview.json");
            System.out.println("  --inventory-json-out FILE          default: jenkins-tck-result-inventory.json");
            System.out.println("  --report-out FILE                  default: tck-report-sections.md");
            System.out.println("  --out FILE                         alias for --report-out");
            System.out.println("  --ccr-out FILE                     default: ccr.md");
            System.out.println();
            System.out.println("Report options:");
            System.out.println("  --tck-folder NAME                  default: " + DEFAULT_TCK_FOLDER);
            System.out.println("  --platform-folder NAME             default: " + DEFAULT_PLATFORM_FOLDER + "; core profile auto-default: " + DEFAULT_CORE_PROFILE_FOLDER);
            System.out.println("  --component-key NAME               optional top-level component filter; default: all components");
            System.out.println("  --certification-profile NAME       auto|platform|core; default: auto");
            System.out.println("  --skip-platform true|false         default: false");
            System.out.println("  --skip-components true|false       default: false");
            System.out.println("  --user USER                        Jenkins user; defaults to JENKINS_USER");
            System.out.println("  --token TOKEN                      Jenkins token; defaults to JENKINS_TOKEN");
            System.out.println("  --timeout SECONDS                  default: 30");
            System.out.println("  --platform-title TEXT              default: " + DEFAULT_PLATFORM_TITLE);
            System.out.println("  --platform-version TEXT            default: " + DEFAULT_PLATFORM_VERSION);
            System.out.println("  --platform-tck-link-text TEXT      default: " + DEFAULT_PLATFORM_TCK_LINK_TEXT);
            System.out.println("  --platform-tck-download-link URL   default: " + DEFAULT_PLATFORM_TCK_DOWNLOAD_LINK);
            System.out.println("  --platform-tck-sha SHA             default: " + DEFAULT_PLATFORM_TCK_SHA);
            System.exit(0);
        }
    }

}
