/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.glassfish.persistence.tck;

import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager;
import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.UserTransaction;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

/**
 *
 * @author Ondro Mihalyi
 */
public class JakartaEeExecutionInterceptor implements InvocationInterceptor {

    private Properties props;

    public JakartaEeExecutionInterceptor(Properties props) {
        this.props = props;
    }

    public void interceptBeforeEachMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        interceptTestMethod(invocation, invocationContext, extensionContext);
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        interceptTestMethod(invocation, invocationContext, extensionContext);
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        Reporter.logTrace("Invoking test method " + invocationContext.getExecutable().getName() + " in a Jakarta EE container.");
            invocationContext.getTarget().ifPresent(newTestObject -> {
                setEntityManager(newTestObject);
                setEntityManagerFactory(newTestObject);
                setEntityTransaction(newTestObject);
            });
            invocation.proceed();
        Reporter.logTrace("...finished test method " + invocationContext.getExecutable().getName() + " in a Jakarts EE container.");
    }

    private <T> void setEntityManager(T testObject) {
        if (testObject instanceof UseEntityManager) {
            UseEntityManager useEm = (UseEntityManager) testObject;
            String unitName = System.getProperty(PropertyKeys.DEFAULT_PERSISTENCE_UNIT);
            PersistenceContext persistenceContextAnnotation = null;
            try {
                persistenceContextAnnotation = useEm.getClass().getMethod("setEntityManager", EntityManager.class)
                        .getAnnotation(PersistenceContext.class);
            } catch (NoSuchMethodException | SecurityException ex) {
            }
            if (persistenceContextAnnotation != null) {
                if (persistenceContextAnnotation.unitName() != null) {
                    unitName = persistenceContextAnnotation.unitName();
                }
            }
            useEm.setEntityManager(getEM(unitName));
        }
    }

    private <T> void setEntityManagerFactory(T testObject) {
        if (testObject instanceof UseEntityManagerFactory) {
            UseEntityManagerFactory useEmf = (UseEntityManagerFactory) testObject;
            String unitName = System.getProperty(PropertyKeys.DEFAULT_PERSISTENCE_UNIT);
            PersistenceUnit persistenceUnitAnnotation = null;
            try {
                persistenceUnitAnnotation = useEmf.getClass().getMethod("setEntityManagerFactory", EntityManagerFactory.class)
                        .getAnnotation(PersistenceUnit.class);
            } catch (NoSuchMethodException | SecurityException ex) {
            }
            if (persistenceUnitAnnotation != null) {
                if (persistenceUnitAnnotation.unitName() != null) {
                    unitName = persistenceUnitAnnotation.unitName();
                }
            }
            useEmf.setEntityManagerFactory(getEMF(unitName));
        }
    }

    private void setEntityTransaction(Object testObject) {
        if (testObject instanceof UseEntityManager) {
            try {
                UseEntityManager useEm = (UseEntityManager) testObject;
                Context namingContext = new InitialContext();
                UserTransaction userTx = (UserTransaction) namingContext.lookup("java:comp/UserTransaction");
                useEm.setEntityTransaction(new UserTransactionWrapper(userTx));
            } catch (NamingException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private EntityManager getEM(String unitName) {
        return getEMF(unitName).createEntityManager();
    }

    private EntityManagerFactory getEMF(String unitName) {
        return Persistence.createEntityManagerFactory(unitName);
    }

}
