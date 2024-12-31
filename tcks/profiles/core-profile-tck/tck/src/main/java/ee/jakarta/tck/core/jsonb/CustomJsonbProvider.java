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
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.spi.JsonbProvider;

public class CustomJsonbProvider extends JsonbProvider {

    @Override
    public JsonbBuilder create() {
        Utils.pushMethod();
        System.out.printf("CustomJsonbProvider.create()\n");
        return new CustomJsonbBuilder();
    }
}
