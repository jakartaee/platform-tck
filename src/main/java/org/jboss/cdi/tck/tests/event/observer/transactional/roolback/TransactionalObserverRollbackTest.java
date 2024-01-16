/*
 * Copyright 2017, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.transactional.roolback;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;

import java.util.Arrays;

import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This test was originally part of the Weld testsuite.
 */
@Test(groups = { INTEGRATION })
@SpecVersion(spec = "cdi", version = "2.0")
public class TransactionalObserverRollbackTest extends AbstractTest{

    @Inject
    EjbTestBean ejbTestBean;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(TransactionalObserverRollbackTest.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.TRANSACTIONAL_OBSERVER_METHODS, id = "aa")
    public void afterSuccessObserverIsNotNotifiedAfterTxRollBack() throws SystemException {
        ActionSequence.reset();
        ejbTestBean.initTransaction();
        ActionSequence.assertSequenceDataContainsAll(
                Arrays.asList(TransactionPhase.IN_PROGRESS.toString(), TransactionPhase.BEFORE_COMPLETION.toString(),
                        TransactionPhase.AFTER_COMPLETION.toString(),
                        TransactionPhase.AFTER_FAILURE.toString()));
        Assert.assertFalse(ActionSequence.getSequenceData().contains(TransactionPhase.AFTER_SUCCESS.toString()));
    }
}
