/*
 * Copyright (c) 2022,2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.glassfish.jsonb.tck;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

/**
 * JUnit5 interceptor that wraps test execution within a container. It's supposed to run within the Jakarta EE container
 * (Arquillian). It requires that the container adapter (Arquillian extension) adds configuration properties into a
 * properties file inside the test application. Configuration properties are read from system properties and the
 * properties file.
 *
 * If executed outside of container (platform.mode is standalone or undefined), it executes tests normally.
 *
 * If executed inside a container, it reads the properties to find out which platform.mode to use and to set System
 * prooeries.
 *
 */
public class TestInvocationInterceptor implements InvocationInterceptor {

    private final Properties props;

    private final InvocationInterceptor testInterceptor;

    public TestInvocationInterceptor() {
        this.props = loadProperties();
        testInterceptor = createInterceptor();
    }

    public static boolean isInContainer(Properties properties) {
        return Boolean.parseBoolean(properties.getProperty(PropertyKeys.IN_CONTAINER));
    }

    public void interceptBeforeEachMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        initTestUtil(invocationContext);
        final Properties oldSysProps = new Properties();
        oldSysProps.putAll(System.getProperties());
        System.getProperties().putAll(props);
        try {
            testInterceptor.interceptBeforeEachMethod(invocation, invocationContext, extensionContext);
        } catch (Throwable ex) {
            System.setProperties(oldSysProps);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        initTestUtil(invocationContext);
        final Properties oldSysProps = new Properties();
        oldSysProps.putAll(System.getProperties());
        System.getProperties().putAll(props);
        try {
            testInterceptor.interceptTestTemplateMethod(invocation, invocationContext, extensionContext);
        } catch (Throwable ex) {
            System.setProperties(oldSysProps);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        initTestUtil(invocationContext);
        final Properties oldSysProps = new Properties();
        oldSysProps.putAll(System.getProperties());
        System.getProperties().putAll(props);
        try {
            testInterceptor.interceptTestMethod(invocation, invocationContext, extensionContext);
        } catch (Throwable ex) {
            System.setProperties(oldSysProps);
            throw new RuntimeException(ex);
        }
    }

    private void initTestUtil(ReflectiveInvocationContext<Method> invocationContext) {
        final String testName = invocationContext.getTargetClass() + "#" + invocationContext.getExecutable().getName();
        final PrintWriter toReporterPrintWriter = new PrintWriter(new WriterToReporterBridge(), false);
    }

    private Properties loadProperties() {
        Properties props = new Properties(System.getProperties());
        try (InputStream propertiesInputStream = this.getClass().getClassLoader()
                .getResourceAsStream(PropertyKeys.SYSTEM_PROPERTIES_FILE_NAME)) {
            if (propertiesInputStream != null) {
                props.load(propertiesInputStream);
                props.setProperty(PropertyKeys.IN_CONTAINER, Boolean.TRUE.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, ex,
                    () -> "Couldn't find resource " + PropertyKeys.SYSTEM_PROPERTIES_FILE_NAME
                    + " on the classpath or read properties from it. Properties from it won't be applied");
        }
        return props;
    }

    private InvocationInterceptor createInterceptor() {
        /*if (isInContainer(props)) {
            String platformMode = props.getProperty(PropertyKeys.PLATFORM_MODE, "");

            if (platformMode.equals(PropertyKeys.PLATFORM_MODE_JAKARTAEE)) {
                return new JSONBExecutionInterceptor(props);
            }
        }
        return new InvocationInterceptor() {
        };*/
        
        return new JSONBExecutionInterceptor(props);
    }
}
