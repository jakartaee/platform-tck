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

import org.jboss.arquillian.container.test.spi.client.protocol.Protocol;
import org.jboss.arquillian.core.spi.LoadableExtension;

/**
 * Arquillian extension for the AppClient protocol.
 */
public class AppClientProtocolExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(Protocol.class, AppClientProtocol.class);
    }
}
