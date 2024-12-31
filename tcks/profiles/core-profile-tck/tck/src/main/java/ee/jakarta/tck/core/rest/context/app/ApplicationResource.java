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

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/**
 * RESTful web service which returns a value from an application scoped bean.
 *
 */
@Path("/application-id")
public class ApplicationResource {
    @Inject
    SimpleApplicationBean simpleApplicationBean;

    @GET
    @Produces("text/plain")
    public String getValue() {
        return Double.toString(simpleApplicationBean.getId());
    }
}
