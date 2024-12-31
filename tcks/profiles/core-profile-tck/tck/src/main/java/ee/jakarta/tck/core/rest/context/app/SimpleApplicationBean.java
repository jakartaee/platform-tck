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

package ee.jakarta.tck.core.rest.context.app;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimpleApplicationBean {
    private double id = Math.random();

    public double getId() {
        return id;
    }
}
