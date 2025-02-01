/*
 * Copyright 2024 Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package tck.arquillian.protocol.appclient;

import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.test.spi.ContainerMethodExecutor;
import org.jboss.arquillian.container.test.spi.client.deployment.DeploymentPackager;
import org.jboss.arquillian.container.test.spi.client.protocol.Protocol;
import org.jboss.arquillian.container.test.spi.command.CommandCallback;
import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;

/**
 * Implementation of the Arquillian Protocol interface for Application Client testing.
 * This protocol handles the deployment and execution of tests in a Jakarta EE
 * Application Client container environment.
 */
public class AppClientProtocol implements Protocol<AppClientProtocolConfiguration> {
    @Inject
    private Instance<Injector> injectorInstance;

    /**
     * {@inheritDoc}
     * @return The configuration class for the Application Client protocol
     */
    @Override
    public Class<AppClientProtocolConfiguration> getProtocolConfigurationClass() {
        return AppClientProtocolConfiguration.class;
    }

    /**
     * {@inheritDoc}
     * @return A protocol description identifying this as the "appclient" protocol
     */
    @Override
    public ProtocolDescription getDescription() {
        return new ProtocolDescription("appclient");
    }

    /**
     * {@inheritDoc}
     * Creates and returns a deployment packager configured for Application Client deployments.
     * The packager is injected with Arquillian dependencies before being returned.
     * 
     * @return An initialized AppClientDeploymentPackager instance
     */
    @Override
    public DeploymentPackager getPackager() {
        AppClientDeploymentPackager packager = new AppClientDeploymentPackager();
        Injector injector = injectorInstance.get();
        injector.inject(packager);

        return packager;
    }

    /**
     * {@inheritDoc}
     * Creates and returns a method executor that can run tests in an Application Client container.
     * 
     * @param protocolConfiguration The protocol-specific configuration
     * @param metaData Metadata about the deployed application
     * @param callback Callback for handling command results
     * @return A ContainerMethodExecutor configured for Application Client test execution
     */
    @Override
    public ContainerMethodExecutor getExecutor(AppClientProtocolConfiguration protocolConfiguration, ProtocolMetaData metaData,
            CommandCallback callback) {

        // Create the AppClientCmd and AppClientMethodExecutor instances and have arquillian inject the Deployment into the executor
        AppClientCmd clientCmd = new AppClientCmd(protocolConfiguration);
        AppClientMethodExecutor executor = new AppClientMethodExecutor(clientCmd, protocolConfiguration);
        Injector injector = injectorInstance.get();
        injector.inject(executor);
        return executor;
    }
}
