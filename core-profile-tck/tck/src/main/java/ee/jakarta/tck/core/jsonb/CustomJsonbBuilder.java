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

import ee.jakarta.tck.core.common.Utils;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.spi.JsonProvider;

public class CustomJsonbBuilder implements JsonbBuilder {
    @Override
    public JsonbBuilder withConfig(JsonbConfig jsonbConfig) {
        Utils.pushMethod();
        return null;
    }

    @Override
    public JsonbBuilder withProvider(JsonProvider jsonProvider) {
        Utils.pushMethod();
        return null;
    }

    @Override
    public Jsonb build() {
        Utils.pushMethod();
        return null;
    }
}
