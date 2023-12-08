package org.jboss.cdi.tck.test;

import org.testng.annotations.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.ComparisonType;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.DifferenceEvaluators;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class ExclusionListsTest {
    @Test
    public void compareExclusionLists() throws IOException {
        List<URL> xmls = new ArrayList<>();
        ExclusionListsTest.class.getClassLoader().getResources("tck-tests.xml").asIterator().forEachRemaining(xmls::add);
        assertEquals(xmls.size(), 2);
        URL control = xmls.stream().filter(it -> !it.toString().contains("web")).findFirst().orElseThrow();
        URL test = xmls.stream().filter(it -> it.toString().contains("web")).findFirst().orElseThrow();

        Diff diff = DiffBuilder.compare(control)
                .withTest(test)
                .checkForSimilar()
                .ignoreComments()
                .ignoreWhitespace()
                .withDifferenceEvaluator(DifferenceEvaluators.chain(
                        DifferenceEvaluators.Default,
                        (comparison, outcome) -> {
                            if (outcome != ComparisonResult.DIFFERENT) {
                                return outcome;
                            }
                            if (comparison.getType() == ComparisonType.CHILD_NODELIST_LENGTH
                                    && (Integer) comparison.getControlDetails().getValue() < (Integer) comparison.getTestDetails().getValue()) {
                                return ComparisonResult.SIMILAR;
                            }
                            if (comparison.getType() == ComparisonType.CHILD_LOOKUP
                                    && comparison.getControlDetails().getValue() == null
                                    && comparison.getTestDetails().getValue() != null) {
                                return ComparisonResult.SIMILAR;
                            }
                            return outcome;
                        }))
                .build();
        assertFalse(diff.hasDifferences(),
                "Exclusion list in `web` must contain the exclusion list in `impl` as a prefix:\n" + diff.fullDescription());
    }
}
