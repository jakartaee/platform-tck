package arquillian;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import static org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL;

public class ProgressListener implements TestExecutionListener {

    private int totalTests = 0;
    private final AtomicInteger executedTests = new AtomicInteger(1);
    private final AtomicInteger failedTests = new AtomicInteger(0);

    private final Map<String, Integer> totalPerClass = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> startedPerClass = new ConcurrentHashMap<>();
    private final Set<String> printedClassHeader = ConcurrentHashMap.newKeySet();

    private volatile TestPlan plan;

    private static final String BOLD_BLUE = "\u001B[34m\u001B[1m";
    private static final String GREEN = "\u001B[32m";
    private static final String BOLD_GREEN = "\u001B[1;32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD_RED = "\u001B[1;31m";
    private static final String RESET = "\u001B[0m";

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        this.plan = testPlan;

        Set<String> testClasses = new LinkedHashSet<>();

        testPlan.accept(new TestPlan.Visitor() {
            @Override public void visit(TestIdentifier id) {
                if (id.isTest()) {
                    owningClassName(testPlan, id).ifPresent(
                        className -> {
                            totalTests++;

                            totalPerClass.merge(className, 1, Integer::sum);
                            testClasses.add(className);
                        }
                    );
                }
            }
        });

        System.out.println(
            "\n\n\n" + BOLD_BLUE +

            ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> \n" +
            ">\n" +
            "> S T A R T I N G   T E S T   R U N   W I T H  " + totalTests + "  T E S T S \n" +
            ">\n" +
            ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> \n" +

            RESET + "\n");

        System.out.println("Running the following test classes: \n");

        int i = 0;
        for (String testClass : testClasses ) {
            System.out.println(BOLD_BLUE + (i++) + ") " + testClass + RESET);
        }

    }

    @Override
    public void executionStarted(TestIdentifier id) {
        if (id.isContainer() && isClassContainer(id)) {
            String className = classNameFrom(id).orElse(id.getDisplayName());
            if (printedClassHeader.add(className)) {
                int total = totalPerClass.getOrDefault(className, 0);
                System.out.println("\n\n" + BOLD_BLUE + ">>> Starting class: " + className +
                        (total > 0 ? "  (" + total + " tests)" : "") + RESET + "\n");
            }
            return;
        }

        if (id.isTest()) {
            String className = owningClassName(plan, id).orElse("UNKNOWN");
            int total = totalPerClass.getOrDefault(className, 0);
            int seq = startedPerClass
                    .computeIfAbsent(className, k -> new AtomicInteger(0))
                    .incrementAndGet();

            int runSoFar = executedTests.get();

            System.out.println(
                "\n[" + BOLD_GREEN + "Running" + RESET + "] " +
                GREEN + className + "#" + id.getDisplayName() + RESET +
                 " | " + BOLD_GREEN + "class: (" + seq + "/" + total + ")" + RESET +
                 " > " + BOLD_BLUE + "total: (" + runSoFar + "/" + totalTests + ")" + RESET +

                 (failedTests.get() > 0? (" ! " + BOLD_RED + "error: (" + failedTests + ")") : "") +

                 "\n" + RESET
            );
        }
    }

    @Override
    public void executionSkipped(TestIdentifier id, String reason) {
        if (id.isTest()) {
            String className = owningClassName(plan, id).orElse("UNKNOWN");
            int total = totalPerClass.getOrDefault(className, 0);
            int seq = startedPerClass
                    .computeIfAbsent(className, k -> new AtomicInteger(0))
                    .incrementAndGet();

            System.out.println("[" + YELLOW + "Skipped" + RESET + "] " +
                    className + "#" + id.getDisplayName() +
                    " | (" + seq + "/" + total + ")  reason: " + reason);
        }
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest()) {
            executedTests.incrementAndGet();
            if (testExecutionResult.getStatus() != SUCCESSFUL) {
                failedTests.incrementAndGet();
            }
        }
    }

    // If you want fail-fast integration, keep your previous executionFinished() hook here.

    // ---- helpers

    private static boolean isClassContainer(TestIdentifier id) {
        return id.getSource().filter(s -> s instanceof ClassSource).isPresent();
    }

    private static Optional<String> classNameFrom(TestIdentifier id) {
        return id.getSource()
                 .filter(s -> s instanceof ClassSource)
                 .map(s -> ((ClassSource) s).getClassName());
    }

    private static Optional<String> owningClassName(TestPlan plan, TestIdentifier id) {
        // Walk up ancestors via TestPlan#getParent until we hit a ClassSource
        Optional<TestIdentifier> cur = Optional.of(id);
        while (cur.isPresent()) {
            Optional<TestSource> src = cur.get().getSource();
            if (src.isPresent() && src.get() instanceof ClassSource cs) {
                return Optional.of(cs.getClassName());
            }
            cur = plan.getParent(cur.get());
        }

        // Fallback: some engines provide only MethodSource on tests
        return id.getSource()
                 .filter(s -> s instanceof MethodSource)
                 .map(s -> ((MethodSource) s).getClassName());
    }
}
