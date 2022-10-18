/*
 * Copyright (c) "2022" Red Hat and others
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package ee.jakarta.tck.core.json;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.json.spi.JsonProvider;

@Dependent
public class JsonProviderProducer {
    @Produces
    public JsonProvider getJsonProvider() {
        System.out.println("JsonProviderProducer#getJsonProvider");
        return JsonProvider.provider();
    }
}
