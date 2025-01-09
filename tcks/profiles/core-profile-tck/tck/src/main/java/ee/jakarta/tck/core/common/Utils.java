/*
 * Copyright (c) "2022", "2025" Red Hat and others
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package ee.jakarta.tck.core.common;

import java.util.Optional;
import java.util.Stack;

public class Utils {
    private static final Stack<String> callStack = new Stack<>();
    public static void pushMethod() {
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        Optional<String> methodName = walker.walk(frames -> frames
                .skip(1)
                .findFirst()
                .map(Utils::getMethodInfo));
        callStack.push(methodName.get());
    }
    private static String getMethodInfo(StackWalker.StackFrame frame) {
        try {
            return frame.getMethodName() + frame.getDescriptor();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot access frame: " + frame, e);
        }
    }
    public static String popStack() {
        return callStack.pop();
    }
}
