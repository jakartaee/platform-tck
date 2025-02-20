/*
 * Copyright (c) 2013, 2024 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.signaturetest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.sun.ts.tests.signaturetest.SigTest;

/*
 * This class is a simple example of a signature test that extends the
 * SigTest framework class.  This signature test is run outside of the
 * Java EE containers.  This class also contains the boilerplate
 * code necessary to create a signature test using the test framework.
 * To see a complete TCK example see the javaee directory for the Java EE
 * TCK signature test class.
 */
public class WebSocketSigTestIT extends SigTest {

	private static final long serialVersionUID = -5264596249711347960L;

	public static final String EJB_VEHICLE = "ejb";

	public static final String SERVLET_VEHICLE = "servlet";

	public static final String JSP_VEHICLE = "jsp";

	public static final String APP_CLIENT_VEHICLE = "appclient";

	public static final String NO_VEHICLE = "standalone";

	private static final Logger logger = System.getLogger(WebSocketSigTestIT.class.getName());

	/*
	 * Defines the packages that are included when running signature tests for any
	 * container (the default packages). This includes the appclient, ejb, jsp, and
	 * servlet containers.
	 */
	private static final String[] DEFAULT_PKGS = { "jakarta.websocket", "jakarta.websocket.server" };

	/*
	 * Defines additional packages that are included when running signature tests
	 * for the ejb, jsp and servlet containers.
	 */
	private static final String[] EJB_SERVLET_JSP_PKGS = {};

	/*
	 * Defines additional packages that are included when running signature tests
	 * for the jsp and servlet containers.
	 */
	private static final String[] SERVLET_JSP_PKGS = {};

	private static final String[] NO_CONTAINER_PKGS = { "jakarta.websocket", "jakarta.websocket.server" };

	/***** Abstract Method Implementation *****/
	/**
	 * Returns a list of strings where each string represents a package name. Each
	 * package name will have it's signature tested by the signature test framework.
	 * 
	 * @return String[] The names of the packages whose signatures should be
	 *         verified.
	 */
	protected String[] getPackages() {
		return DEFAULT_PKGS;
	}

	/**
	 * Adds the default packages and the command line flags to the specified list
	 * for each package defined in the list of default packages to check during
	 * signature tests. Note: The specified list is modified as a result of this
	 * method call.
	 *
	 * @param sigArgsList The arg list being constructed to pass to the utility that
	 *                    records and runs signature file tests.
	 */
	private static void addDefaultPkgs(List<String> sigArgsList) {
		for (int i = 0; i < DEFAULT_PKGS.length; i++) {
			sigArgsList.add(DEFAULT_PKGS[i]);
		}
	}

	/**
	 * Adds the ejb, servlet, and jsp packages and the command line flags to the
	 * specified list for each package defined in the list of ejb, servlet, and jsp
	 * packages to check during signature tests. Note: The specified list is
	 * modified as a result of this method call.
	 *
	 * @param sigArgsList The arg list being constructed to pass to the utility that
	 *                    records and runs signature file tests.
	 */
	private static void addEjbServletJspPkgs(List<String> sigArgsList) {
		for (int i = 0; i < EJB_SERVLET_JSP_PKGS.length; i++) {
			sigArgsList.add(EJB_SERVLET_JSP_PKGS[i]);
		}
	}

	/**
	 * Adds the servlet, and jsp packages and the command line flags to the
	 * specified list for each package defined in the list of servlet, and jsp
	 * packages to check during signature tests. Note: The specified list is
	 * modified as a result of this method call.
	 *
	 * @param sigArgsList The arg list being constructed to pass to the utility that
	 *                    records and runs signature file tests.
	 */
	private static void addServletJspPkgs(List<String> sigArgsList) {
		for (int i = 0; i < SERVLET_JSP_PKGS.length; i++) {
			sigArgsList.add(SERVLET_JSP_PKGS[i]);
		}
	}

	/**
	 * Adds the pkgs for tests to be run in NO Container (ie standalone) packages to
	 * check during signature tests. Note: The specified list is modified as a
	 * result of this method call.
	 *
	 * @param sigArgsList The arg list being constructed to pass to the utility that
	 *                    records and runs signature file tests.
	 */
	private static void addNoContainerPkgs(List<String> sigArgsList) {
		for (int i = 0; i < NO_CONTAINER_PKGS.length; i++) {
			sigArgsList.add(NO_CONTAINER_PKGS[i]);
		}
	}

	public WebSocketSigTestIT() {
		setup(new String[]{}, System.getProperties());
	}

	/**
	 * Returns a list of strings where each string represents a package name. Each
	 * package name will have it's signature tested by the signature test framework.
	 * 
	 * @param vehicleName The name of the container where the signature tests
	 *                    should be conducted.
	 * @return String[] The names of the packages whose signatures should be
	 *         verified.
	 */
	protected String[] getPackages(String vehicleName) {
		List<String> packages = new LinkedList<String>();

		if (vehicleName.equals(NO_VEHICLE)) {
			addNoContainerPkgs(packages);
		} else {
			addDefaultPkgs(packages); // add default vehicle packages
			if (vehicleName.equals(EJB_VEHICLE) || vehicleName.equals(SERVLET_VEHICLE)
					|| vehicleName.equals(JSP_VEHICLE)) {
				addEjbServletJspPkgs(packages);
			}
			if (vehicleName.equals(SERVLET_VEHICLE) || vehicleName.equals(JSP_VEHICLE)) {
				addServletJspPkgs(packages);
			}
		}
		return packages.toArray(new String[packages.size()]);
	}

	/*
	 * The following comments are specified in the base class that defines the
	 * signature tests. This is done so the test finders will find the right class
	 * to run. The implementation of these methods is inherited from the super class
	 * which is part of the signature test framework.
	 */

	// NOTE: If the API under test is not part of your testing runtime
	// environment, you may use the property sigTestClasspath to specify
	// where the API under test lives. This should almost never be used.
	// Normally the API under test should be specified in the classpath
	// of the VM running the signature tests. Use either the first
	// comment or the one below it depending on which properties your
	// signature tests need. Please do not use both comments.

	/*
	 * @class.setup_props: sigTestClasspath;
	 */
	/*
	 * @testName: signatureTest
	 * 
	 * @assertion: A WebSocket container must implement the required classes and
	 * APIs specified in the WebSocket Specification (JSR 356).
	 * 
	 * @test_Strategy: Using reflection, gather the implementation specific classes
	 * and APIs. Compare these results with the expected (required) classes and
	 * APIs.
	 *
	 */

	@Test
	public void signatureTest() throws Exception {

		logger.log(Logger.Level.INFO, "$$$ SigTestIT.signatureTest() called");
		String mapFile = null;
		String packageFile = null;
		String repositoryDir = null;
		Properties mapFileAsProps = null;
		String[] packages = getPackages();
		String apiPackage = "jakarta.websocket";

		try {

			InputStream inStreamMapfile = WebSocketSigTestIT.class.getClassLoader()
					.getResourceAsStream("com/sun/ts/tests/websocket/signaturetest/sig-test.map");
			File mFile = writeStreamToTempFile(inStreamMapfile, "sig-test", ".map");
			mapFile = mFile.getCanonicalPath();
			logger.log(Logger.Level.INFO, "mapFile location is :" + mapFile);

			InputStream inStreamPackageFile = WebSocketSigTestIT.class.getClassLoader()
					.getResourceAsStream("com/sun/ts/tests/websocket/signaturetest/sig-test-pkg-list.txt");
			File pFile = writeStreamToTempFile(inStreamPackageFile, "sig-test-pkg-list", ".txt");
			packageFile = pFile.getCanonicalPath();
			logger.log(Logger.Level.INFO, "packageFile location is :" + packageFile);

			mapFileAsProps = getSigTestDriver().loadMapFile(mapFile);
			String packageVersion = mapFileAsProps.getProperty(apiPackage);
			logger.log(Logger.Level.INFO, "Package version from mapfile :" + packageVersion);

			InputStream inStreamSigFile = WebSocketSigTestIT.class.getClassLoader().getResourceAsStream(
					"com/sun/ts/tests/websocket/signaturetest/jakarta.websocket.sig_" + packageVersion);
			File sigFile = writeStreamToSigFile(inStreamSigFile, apiPackage, packageVersion);
			logger.log(Logger.Level.INFO, "signature File location is :" + sigFile.getCanonicalPath());

		} catch (IOException ex) {
			logger.log(Logger.Level.ERROR, "Exception while creating temp files :" + ex);
		}

		super.signatureTest(mapFile, packageFile, mapFileAsProps, packages);
	}

	/*
	 * Call the parent class's cleanup method.
	 */
}
