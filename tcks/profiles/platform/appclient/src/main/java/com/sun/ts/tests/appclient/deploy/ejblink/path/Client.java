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
 * @(#)Client.java	1.14 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.ejblink.path;

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

import com.sun.ts.tests.common.base.EETest;
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

	/** Bean lookup */
	private static final String bean1Lookup = prefix + "Nikita";

	private static final String bean2Lookup = prefix + "Illusion";

	/** Expected value for the bean name */
	private static final String bean1RefName = "Besson";

	private static final String bean2RefName = "Renoir";

	private TSNamingContext nctx = null;

	private Properties props = null;

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")

	@Deployment(name = "appclient_dep_ejblink_path", testable = true)
	public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {

		EnterpriseArchive ear = null;
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejblink_path_client.jar");
		ejbClient.addPackages(false, "com.sun.ts.tests.appclient.deploy.ejblink.path");
		ejbClient.addClass(EETest.class);

		// The appclient-client descriptor
		URL appClientUrl = Client.class.getResource("appclient_dep_ejblink_path_client.xml");
		ejbClient.addAsManifestResource(appClientUrl, "application-client.xml");
		// The sun appclient-client descriptor
		URL sunAppClientUrl = Client.class.getResource("appclient_dep_ejblink_path_client.jar.sun-application-client.xml");
		ejbClient.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");

		ejbClient.addAsManifestResource(
				new StringAsset("Main-Class: " + Client.class.getName() + "\n"),
				"MANIFEST.MF");

		JavaArchive ejb1 = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejblink_path_jar1_ejb.jar");
		ejb1.addClasses(
				com.sun.ts.tests.appclient.deploy.ejblink.path.ReferencedBean.class,
				com.sun.ts.tests.appclient.deploy.ejblink.path.ReferencedBeanEJB.class,
				com.sun.ts.tests.assembly.util.shared.ejbref.common.ReferencedBeanCode.class,
				com.sun.ts.tests.common.ejb.wrappers.StatelessWrapper.class
		);

		URL resURL = Client.class.getResource("appclient_dep_ejblink_path_jar1_ejb.jar.sun-ejb-jar.xml");
			ejb1.addAsManifestResource(resURL, "sun-ejb-jar.xml");

		resURL = Client.class.getResource("appclient_dep_ejblink_path_jar1_ejb.xml");
			ejb1.addAsManifestResource(resURL, "ejb-jar.xml");

		JavaArchive ejb2 = ShrinkWrap.create(JavaArchive.class, "appclient_dep_ejblink_path_jar2_ejb.jar");
		ejb2.addClasses(
				com.sun.ts.tests.appclient.deploy.ejblink.path.ReferencedBean2.class,
				com.sun.ts.tests.appclient.deploy.ejblink.path.ReferencedBean2EJB.class,
				com.sun.ts.tests.assembly.util.shared.ejbref.common.ReferencedBeanCode.class,
				com.sun.ts.tests.common.ejb.wrappers.StatelessWrapper.class
		);

		resURL = Client.class.getResource("appclient_dep_ejblink_path_jar2_ejb.jar.sun-ejb-jar.xml");
			ejb2.addAsManifestResource(resURL, "sun-ejb-jar.xml");

		resURL = Client.class
				.getResource("appclient_dep_ejblink_path_jar2_ejb.xml");
			ejb2.addAsManifestResource(resURL, "ejb-jar.xml");

		ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_ejblink_path.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb1);
		ear.addAsModule(ejb2);

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
			throw new Exception("Setup failed:", e);
		}
	}

	/**
	 * @testName: testScope
	 *
	 * @assertion_ids: JavaEE:SPEC:118; JavaEE:SPEC:119; JavaEE:SPEC:120
	 *
	 * @test_Strategy: An application client references two beans packaged in two
	 *                 different ejb-jar's. Both referenced bean use the same
	 *                 ejb-name in their respective JAR file, and they are
	 *                 identified by a String environment entry ('myName').
	 *
	 *                 The ejb-link for the external bean is in the form
	 *                 'ejbjar#EJBName'.
	 *
	 *                 Check that we can deploy the application, that the client can
	 *                 lookup the two beans. Check that referenced beans identities
	 *                 (as reported by the String env. entry) match the ones
	 *                 specified in the DD.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testScope() throws Exception {
		ReferencedBean bean1 = null;
		ReferencedBean2 bean2 = null;
		String bean1Name;
		String bean2Name;
		boolean pass = false;

		try {
			TestUtil.logTrace("[Client] Looking up '" + bean1Lookup + "'...");
			bean1 = (ReferencedBean) nctx.lookup(bean1Lookup, ReferencedBean.class);
			bean1.createNamingContext();
			bean1.initLogging(props);
			bean1Name = bean1.whoAreYou();
			TestUtil.logTrace(bean1Lookup + " name is '" + bean1Name + "'");

			bean2 = (ReferencedBean2) nctx.lookup(bean2Lookup, ReferencedBean2.class);
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

				throw new Exception("ejb-link path test failed!");
			}
		} catch (Exception e) {
			throw new Exception("ejb-link path test failed: " + e, e);
		}
	}

	public void cleanup() throws Exception {
		logMsg("[Client] cleanup()");
	}

}
