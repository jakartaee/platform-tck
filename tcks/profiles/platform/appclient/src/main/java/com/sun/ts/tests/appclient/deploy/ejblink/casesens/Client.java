/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

/*
 * @(#)Client.java	1.12 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.ejblink.casesens;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")
public class Client extends EETest {

	private static final String prefix = "java:comp/env/ejb/";

	/* These lookups differ only by case */
	private static final String bean1Lookup = prefix + "Samba";

	private static final String bean2Lookup = prefix + "Bossa";

	/* Expected values for the bean name */
	private static final String bean1RefName = "Bahia";

	private static final String bean2RefName = "Rio";

	private TSNamingContext nctx = null;

	private Properties props = null;

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient_dep_ejblink_casesens", testable = true)
	public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejblink_casesens_client.jar");
		ejbClient.addPackages(true, Client.class.getPackage());
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");

		// The appclient-client descriptor
		URL appClientUrl = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/ejblink/casesens/appclient_dep_ejblink_casesens_client.xml");
		if (appClientUrl != null) {
			ejbClient.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/ejblink/casesens/appclient_dep_ejblink_casesens_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			ejbClient.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.ejblink.casesens.Client" + "\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejblink_casesens_ejb.jar");
		ejb.addClasses(CaseBean.class, CaseBeanEJB.class);
		ejb.addPackages(true, "com.sun.ts.tests.assembly.util.shared.ejbref.common");
		ejb.addPackages(true, "com.sun.ts.tests.common.ejb.wrappers");

		URL resURL = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/ejblink/casesens/appclient_dep_ejblink_casesens_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/ejblink/casesens/appclient_dep_ejblink_casesens_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}


		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_ejblink_casesens.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);

		return ear;
	};

	/*
	 * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
	 * generateSQL;
	 *
	 * @class.testArgs: -ap tssql.stmt
	 *
	 */
	public void setup(String[] args, Properties props) throws Exception {
		this.props = props;
		try {
			nctx = new TSNamingContext();
			logMsg("[Client] Setup succeed (got naming context).");
		} catch (Exception e) {
			throw new Exception("[Client] Setup failed:", e);
		}
	}

	/**
	 * @testName: testCaseSensitivity
	 *
	 * @assertion_ids: JavaEE:SPEC:279
	 *
	 * @test_Strategy: Have two Stateless Session beans whose ejb-name's differ only
	 *                 by case and whose identity is defined by a String environment
	 *                 entry ('myName').
	 *
	 *                 The application client references these two beans using
	 *                 ejb-link elements. Check that the client can lookup the two
	 *                 beans and check each bean identity based on their value for
	 *                 the 'myName' environment entry. Check that this identity
	 *                 match the references specified in the DD (validates that the
	 *                 EJB references were resolved correctly).
	 */
	@Test
	@TargetVehicle("appclient")
	public void testCaseSensitivity() throws Exception {
		CaseBean bean1 = null;
		CaseBean bean2 = null;
		String bean1Name;
		String bean2Name;
		boolean pass = false;

		try {
			TestUtil.logTrace("[Client] Looking up '" + bean1Lookup + "'...");
			bean1 = (CaseBean) nctx.lookup(bean1Lookup, CaseBean.class);
			bean1.createNamingContext();
			bean1.initLogging(props);
			bean1Name = bean1.whoAreYou();
			TestUtil.logTrace(bean1Lookup + "name is '" + bean1Name + "'");

			TestUtil.logTrace("[Client] Looking up '" + bean2Lookup + "'...");
			bean2 = (CaseBean) nctx.lookup(bean2Lookup, CaseBean.class);
			bean2.createNamingContext();
			bean2.initLogging(props);
			bean2Name = bean2.whoAreYou();
			TestUtil.logTrace(bean2Lookup + " name is '" + bean2Name + "'");

			pass = bean1Name.equals(bean1RefName) && bean2Name.equals(bean2RefName);

			if (!pass) {
				TestUtil.logErr(
						"[Client] " + bean1Lookup + "name is '" + bean1Name + "' expected '" + bean1RefName + "'");

				TestUtil.logErr(
						"[Client] " + bean2Lookup + "name is '" + bean2Name + "' expected '" + bean2RefName + "'");

				throw new Exception("ejb-link casesens test failed!");
			}
		} catch (Exception e) {
			throw new Exception("ejb-link casesens test failed: " + e, e);
		}
	}

	public void cleanup() throws Exception {
		logMsg("[Client] cleanup()");
	}

}
