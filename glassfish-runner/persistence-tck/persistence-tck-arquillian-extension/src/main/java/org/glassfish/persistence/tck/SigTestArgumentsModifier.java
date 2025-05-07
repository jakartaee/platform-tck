/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import com.sun.tdk.signaturetest.core.Exclude;
import com.sun.tdk.signaturetest.core.ExcludeException;
import com.sun.tdk.signaturetest.model.ClassDescription;
import com.sun.tdk.signaturetest.model.MemberDescription;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ondro Mihalyi
 */
public class SigTestArgumentsModifier implements Exclude {

    private static final String BOOT_CP_ARG = "-BootCp";

    public String[] parseParameters(String[] args) {
        for (String arg : args) {
            if (arg.equals(BOOT_CP_ARG)) {
                return args;
            }
        }
        final List<String> argsList = new ArrayList<String>(List.of(args));
        argsList.add(BOOT_CP_ARG);
        return argsList.toArray(new String[] {});
    }

    public void check(ClassDescription testedClass, MemberDescription signature) throws ExcludeException {
    }

    public String report() {
        return null;
    }

}
