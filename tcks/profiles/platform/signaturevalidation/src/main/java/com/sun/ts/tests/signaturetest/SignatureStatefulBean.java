/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/*
 * $Id$
 */

/*
 * @(#)SignatureStatefulBean.java
 */


package com.sun.ts.tests.signaturetest;

import jakarta.ejb.Stateful;

/**
 * SignatureStatefulBean
 */
@Stateful
public class SignatureStatefulBean {

    private transient Exception result = null;

    public void runEJBSignatureTests(JakartaEESigTest sigTest) {
        try {
            sigTest.signatureTest();
        } catch (Exception e) {
            result = e;
        }
    }

    public Exception getTestException() {
        Exception result = this.result;
        this.result = null;
        return result;
    }
}
