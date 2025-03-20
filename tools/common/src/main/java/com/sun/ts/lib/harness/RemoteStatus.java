/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.harness;

import java.io.Serial;
import java.io.Serializable;

/**
 * Used to return status information from the server to the client.
 */
public class RemoteStatus implements Serializable {
    // @since 11.0.0
    @Serial
    private static final long serialVersionUID = 3604418904944742426L;
    // The Stauts#type
    int type;
    // Information about the status
    String reason;
    // @since 11.0.1
    String errorMessage;
    StackTraceElement[] errorTrace;

    /**
     * Create a RemoteStatus from a Status
     * @param s - test status
     */
    public RemoteStatus(Status s) {
        this(s, null);
    }

    /**
     * Create a RemoteStatus from a Status and an error
     * @param s - test status
     * @param error - error that caused the status
     */
    public RemoteStatus(Status s, Throwable error) {
        type = s.getType();
        reason = s.getReason();
        if(error != null) {
            this.errorTrace = error.getStackTrace();
            this.errorMessage = error.getMessage();
        }
    }

    /**
     * Convert this RemoteStatus to a Status
     * @return Status
     */
    public Status toStatus() {
        return new Status(type, reason);
    }

    /**
     * Get the type of the status
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * See if this status has error information
     * @return true if there is error information
     */
    public boolean hasError() {
        return errorTrace != null;
    }

    /**
     * Get the error trace
     * @return error trace or null
     */
    public StackTraceElement[] getErrorTrace() {
        return errorTrace;
    }
    /**
     * Get the error message
     * @return error message or null
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * String representation with status and cause information. Optionally may have error message and trace.
     * @return string representation
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("RemoteStatus: ");
        sb.append(toStatus());
        if(hasError()) {
            sb.append(" Error: '");
            sb.append(errorMessage);
            sb.append("'\n StackTrace:\n");
            for(int i = 0; i < errorTrace.length; i++) {
                sb.append(errorTrace[i]);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
