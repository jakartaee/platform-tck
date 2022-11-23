/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/**
 *
 * @author Raja Perumal
 */
package com.sun.ts.lib.deliverable.jaspic;

import com.sun.javatest.TestEnvironment;
import com.sun.ts.lib.deliverable.AbstractDeliverable;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.implementation.sun.javaee.runtime.SunRIDeploymentInfo;
import com.sun.ts.lib.porting.DeploymentInfo;
import com.sun.ts.lib.util.TestUtil;
import java.io.File;
import java.util.Properties;

/**
 * This class serves as a place for JaspicJakartaEE Deliverable specific info.
 */
public class JaspicJakartaEEDeliverable extends AbstractDeliverable {

    public PropertyManagerInterface createPropertyManager(TestEnvironment te) throws Exception {

        JaspicJakartaEEPropertyManager propMgr = JaspicJakartaEEPropertyManager.getJaspicJakartaEEPropertyManager(te);

        // create JaspicJakartaEE specific working directories
        createDir(propMgr.getProperty("wsdlRepository1"));
        createDir(propMgr.getProperty("wsdlRepository2"));
        return propMgr;
    }

    public PropertyManagerInterface createPropertyManager(Properties p) throws Exception {
        return JaspicJakartaEEPropertyManager.getJaspicJakartaEEPropertyManager(p);
    }

    public PropertyManagerInterface getPropertyManager() throws Exception {
        return JaspicJakartaEEPropertyManager.getJaspicJakartaEEPropertyManager();
    }

    public DeploymentInfo getDeploymentInfo(String earFile, String[] sValidRuntimeInfoFilesArray) {
        DeploymentInfo info = null;
        try {
            info = new SunRIDeploymentInfo(earFile, sValidRuntimeInfoFilesArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public boolean supportsInterop() {
        return false;
    }

    private void createDir(String sDir) throws Exception {
        File fDir = new File(sDir);

        if (!fDir.exists()) {
            if (!fDir.mkdirs()) {
                throw new Exception("Failed to create directory: " + sDir);
            }
            TestUtil.logHarnessDebug("Successfully created directory: " + sDir);
        }
    }
}
