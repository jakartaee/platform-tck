/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package com.sun.ts.tests.javamail.provider;

import java.io.InputStream;
import java.io.OutputStream;

import jakarta.mail.util.LineInputStream;
import jakarta.mail.util.LineOutputStream;

import jakarta.mail.util.StreamProvider;
import jakarta.mail.util.SharedByteArrayInputStream;


public class MyMailProvider implements StreamProvider {

    /**
     * Public constructor.
     */
    public MyMailProvider() {
    }

    public InputStream inputBase64(InputStream in) {
        return null;
    }

    public OutputStream outputBase64(OutputStream out) {
        return null;
    }

    public InputStream inputBinary(InputStream in) {
        return null;
    }

    public OutputStream outputBinary(OutputStream out) {
        return null;
    }

    public OutputStream outputB(OutputStream out) {
        return null;
    }

    public InputStream inputQ(InputStream in) {
        return null;
    }

    public OutputStream outputQ(OutputStream out, boolean encodingWord) {
        return null;
    }

    public LineInputStream inputLineStream(InputStream in, boolean allowutf8) {
        return null;
    }

    public LineOutputStream outputLineStream(OutputStream out, boolean allowutf8) {
        return null;
    }

    public InputStream inputQP(InputStream in) {
        return null;
    }

    public OutputStream outputQP(OutputStream out) {
        return null;
    }

    public InputStream inputSharedByteArray(byte[] bytes) {
        return null;
    }

    public InputStream inputUU(InputStream in) {
        return null;
    }

    public OutputStream outputUU(OutputStream out, String filename) {
        return null;
    }

}