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

package ee.jakarta.tck.core.jsonb;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.json.bind.spi.JsonbProvider;

@Dependent
public class JsonbProviderProducer {
    @Produces
    public JsonbProvider getJsonbProvider() {
        return JsonbProvider.provider(CustomJsonbProvider.class.getName());
    }
}
