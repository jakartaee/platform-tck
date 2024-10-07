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

public class AppClientProtocol implements Protocol<AppClientProtocolConfiguration> {
    @Inject
    private Instance<Injector> injectorInstance;

    @Override
    public Class<AppClientProtocolConfiguration> getProtocolConfigurationClass() {
        return AppClientProtocolConfiguration.class;
    }

    @Override
    public ProtocolDescription getDescription() {
        return new ProtocolDescription("appclient");
    }

    @Override
    public DeploymentPackager getPackager() {
        AppClientDeploymentPackager packager = new AppClientDeploymentPackager();
        Injector injector = injectorInstance.get();
        injector.inject(packager);

        return packager;
    }

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
