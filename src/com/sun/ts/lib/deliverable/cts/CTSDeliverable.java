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

package com.sun.ts.lib.deliverable.cts;

import com.sun.ts.lib.deliverable.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.DeploymentInfo;
import com.sun.ts.lib.implementation.sun.javaee.runtime.SunRIDeploymentInfo;
import com.sun.javatest.*;
import java.util.*;
import java.io.*;

/**
 * This class serves as a place for CTS Deliverable specific info.
 *
 * @author Kyle Grucci
 */
public class CTSDeliverable extends AbstractDeliverable {

  public PropertyManagerInterface createPropertyManager(TestEnvironment te)
      throws Exception {
    CTSPropertyManager propMgr = CTSPropertyManager.getCTSPropertyManager(te);

    // create CTS specific working directories
    createDir(propMgr.getProperty("wsdlRepository1"));
    createDir(propMgr.getProperty("wsdlRepository2"));
    return propMgr;
  }

  public PropertyManagerInterface createPropertyManager(Properties p)
      throws Exception {
    return CTSPropertyManager.getCTSPropertyManager(p);
  }

  public PropertyManagerInterface getPropertyManager() throws Exception {
    return CTSPropertyManager.getCTSPropertyManager();
  }

  public DeploymentInfo getDeploymentInfo(String earFile,
      String[] sValidRuntimeInfoFilesArray) {
    DeploymentInfo info = null;
    try {
      info = new SunRIDeploymentInfo(earFile, sValidRuntimeInfoFilesArray);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return info;
  }

  public Map getValidVehicles() {
    super.getValidVehicles();

    // sample jdbc tests
    htTSValidVehicles.put("tests_samples_jdbc.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // signature tests
    htTSValidVehicles.put("tests_signaturetest.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // jaxp tests
    htTSValidVehicles.put("tests_jaxp.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // jaxp com directory tests (until we figure out notserializable exc.)
    htTSValidVehicles.put("tests_jaxp_extension_com.service_eetest.vehicles",
        new String[] { "appclient" });

    // rmiiiop/ee tests
    htTSValidVehicles.put("tests_rmiiiop_ee.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // interop/rmiiiop/marshaltests
    htTSValidVehicles.put(
        "tests_interop_rmiiiop_marshaltests.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // interop/naming/cosnamingNoSSL
    htTSValidVehicles.put(
        "tests_interop_naming_cosnamingNoSSL.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // jaxm tests
    htTSValidVehicles.put("tests_jaxm.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // saaj/ee/Standalone tests
    htTSValidVehicles.put("tests_saaj_ee_Standalone.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // jaxrpc tests
    htTSValidVehicles.put("tests_jaxrpc.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });
    htTSValidVehicles.put(
        "tests_jaxrpc_api_javax_xml_rpc_handler.service_eetest.vehicles",
        new String[] { "appclient" });
    htTSValidVehicles.put(
        "tests_jaxrpc_api_javax_xml_rpc_handler_soap.service_eetest.vehicles",
        new String[] { "appclient" });

    // interop/webservices
    htTSValidVehicles.put("tests_interop_webservices.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // interop/saaj/Standalone
    htTSValidVehicles.put(
        "tests_interop_saaj_Standalone.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // jaxr tests
    htTSValidVehicles.put("tests_jaxr.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // jdbc tests
    htTSValidVehicles.put("tests_jdbc_ee.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });

    // jms tests
    htTSValidVehicles.put("tests_jms_ee_appclient.service_eetest.vehicles",
        new String[] { "appclient" });
    htTSValidVehicles.put("tests_jms_ee_web.service_eetest.vehicles",
        new String[] { "servlet", "jsp" });
    htTSValidVehicles.put("tests_jms_ee_ejb.service_eetest.vehicles",
        new String[] { "ejb" });
    htTSValidVehicles.put("tests_jms_ee_all.service_eetest.vehicles",
        new String[] { "appclient", "ejb", "servlet", "jsp" });

    // j2ee Management tests
    htTSValidVehicles.put("tests_j2eetools_mgmt.service_eetest.vehicles",
        new String[] { "ejb" });

    // j2ee deploy tests
    htTSValidVehicles.put("tests_j2eetools_deploy.service_eetest.vehicles",
        new String[] { "standalone" });

    // jmx tests
    // htTSValidVehicles.put("tests_jmx.service_eetest.vehicles", new
    // String[]{"standalone"});

    return htTSValidVehicles;
  }

  public Map getInteropDirections() {
    super.getInteropDirections();

    htValidRunDirections.put("tests_interop.interop", "both");

    htValidRunDirections.put("tests_interop_csiv2_rionly.interop", "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ac_ssl_ssln_upr_noid_forward.interop", "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ac_ssl_ssln_upr_noid_reverse.interop", "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ac_ssl_ssln_upr_noid_a_forward.interop",
        "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ac_ssl_ssln_upr_noid_a_reverse.interop",
        "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ac_ssl_sslr_upn_noid_forward.interop", "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ac_ssl_sslr_upn_noid_reverse.interop", "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_ssln_upn_anonid_forward.interop",
        "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_ssln_upn_anonid_reverse.interop",
        "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_sslr_upn_anonid_forward.interop",
        "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_sslr_upn_anonid_reverse.interop",
        "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_sslr_upn_upid_forward.interop", "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_sslr_upn_upid_reverse.interop", "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssln_ssln_upn_anonid_forward.interop",
        "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssln_ssln_upn_anonid_reverse.interop",
        "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_ssln_upn_ccid_forward.interop", "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_ssln_upn_ccid_reverse.interop", "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_sslr_upn_ccid_forward.interop", "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_sslr_upn_ccid_reverse.interop", "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssln_ssln_upn_ccid_forward.interop", "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssln_ssln_upn_ccid_reverse.interop", "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssln_ssln_upn_upid_forward.interop", "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssln_ssln_upn_upid_reverse.interop", "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_ssln_upn_upid_forward.interop", "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssl_ssln_upn_upid_reverse.interop", "reverse");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssln_ssln_upn_upid_a_forward.interop",
        "forward");
    htValidRunDirections.put(
        "tests_interop_csiv2_ew_ssln_ssln_upn_upid_a_reverse.interop",
        "reverse");

    return htValidRunDirections;
  }

  public boolean supportsInterop() {
    return true;
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
