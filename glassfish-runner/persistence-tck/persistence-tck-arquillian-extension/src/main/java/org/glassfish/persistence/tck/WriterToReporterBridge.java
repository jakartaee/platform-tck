/*
 * Copyright (c) 2024, 2025 Contributors to the Eclipse Foundation
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
package org.glassfish.persistence.tck;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Ondro Mihalyi
 */
class WriterToReporterBridge extends Writer {

    StringBuilder buffer = new StringBuilder();

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        buffer.append(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        if (!buffer.isEmpty()) {
            Reporter.log(buffer.toString());
            buffer = new StringBuilder();
        }
    }

    @Override
    public void close() throws IOException {
        flush();
    }

}
