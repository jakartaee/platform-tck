/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
