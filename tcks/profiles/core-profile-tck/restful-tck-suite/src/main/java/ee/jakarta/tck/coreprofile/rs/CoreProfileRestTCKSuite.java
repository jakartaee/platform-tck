/*
 * Copyright \(c\) "2022" Red Hat and others
 *
 * This program and the accompanying materials are made available under the Apache Software License 2.0 which is available at:
 *  https://www.apache.org/licenses/LICENSE-2.0.
 *
 *  SPDX-License-Identifier: Apache-2.0
 */

package ee.jakarta.tck.coreprofile.rs;


import org.junit.platform.suite.api.ExcludeClassNamePatterns;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * This Suite class redefines the tests that should be run as part of the Jakarta RESTful TCK
 */
@Suite
@SuiteDisplayName("Jakarta Core Profile RESTful TCK")
@SelectPackages("ee.jakarta.tck.ws.rs")
@ExcludeTags({"xml_binding", "servlet", "security"})
@IncludeClassNamePatterns(".*IT.*")
public class CoreProfileRestTCKSuite {

}
