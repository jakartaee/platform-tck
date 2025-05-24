package com.sun.ts.lib.harness;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * This exception is used only by EETest. Overrides 3 printStackTrace methods to preserver the original stack trace.
 * Using setStackTraceElement() would be more elegant but it is not available prior to j2se 1.4.
 *
 * @author Kyle Grucci
 */
public class SetupException extends Exception {
    private static final long serialVersionUID = -7616313680616499158L;

    public Exception e;

    /**
     * creates a Fault with a message
     */
    public SetupException(String msg) {
        super(msg);
    }

    /**
     * creates a SetupException with a message
     *
     * @param msg the message
     * @param e   prints this exception's stacktrace
     */
    public SetupException(String msg, Exception e) {
        super(msg);
        this.e = e;
    }

    /**
     * Prints this Throwable and its backtrace to the standard error stream.
     */
    public void printStackTrace() {
        if (this.e != null) {
            this.e.printStackTrace();
        } else {
            super.printStackTrace();
        }
    }

    /**
     * Prints this throwable and its backtrace to the specified print stream.
     *
     * @param s <code>PrintStream</code> to use for output
     */
    public void printStackTrace(PrintStream s) {
        if (this.e != null) {
            this.e.printStackTrace(s);
        } else {
            super.printStackTrace(s);
        }
    }

    /**
     * Prints this throwable and its backtrace to the specified print writer.
     *
     * @param s <code>PrintWriter</code> to use for output
     */
    public void printStackTrace(PrintWriter s) {
        if (this.e != null) {
            this.e.printStackTrace(s);
        } else {
            super.printStackTrace(s);
        }
    }

    @Override
    public Throwable getCause() {
        return e;
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        if (e != null)
            throw new IllegalStateException("Can't overwrite cause");
        if (!Exception.class.isInstance(cause))
            throw new IllegalArgumentException("Cause not permitted");
        this.e = (Exception) cause;
        return this;
    }
}
