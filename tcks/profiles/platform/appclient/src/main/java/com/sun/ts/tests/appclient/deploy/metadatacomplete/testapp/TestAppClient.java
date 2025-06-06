/*
 * Copyright (c) 2017, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)TestAppClient.java	1.11 07/20/2017
 */
package com.sun.ts.tests.appclient.deploy.metadatacomplete.testapp;

import java.util.Properties;

import javax.naming.InitialContext;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;

import jakarta.annotation.Resource;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.EJB;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.Queue;
import jakarta.mail.MailSessionDefinition;
import jakarta.mail.Session;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

@MailSessionDefinition(name = "java:comp/myMailSession", host = "smtp.gmail.com", transportProtocol = "smtps", properties = {
		"mail.debug=true" })
@DataSourceDefinition(name = "java:global/MyApp/MyDataSource", className = "oracle.jdbc.pool.OracleDataSource", url = "jdbc:oracle:thin:@localhost:1521:orcl", user = "TESTU", password = "TESTU")
@JMSConnectionFactoryDefinition(description = "Define ConnectionFactory JSPMyTestConnectionFactory", interfaceName = "jakarta.jms.ConnectionFactory", name = "java:global/JSPMyTestConnectionFactory", user = "j2ee", password = "j2ee")
@JMSDestinationDefinition(name = "java:app/jms/myappTopic", interfaceName = "jakarta.jms.Topic", destinationName = "MyPhysicalAppTopic")

public class TestAppClient extends EETest {

	private InitialContext initialContext;

	private TSNamingContext nctx = null;

	private Properties props = null;

	@Resource(lookup = "java:comp/myMailSession")
	private static Session session;

	@EJB
	static DataStoreRemote dataStore;

	@Resource(lookup = "java:app/jms/TestConnectionFactory")
	private static ConnectionFactory testFac;

	@Resource(lookup = "java:app/jms/TestQ")
	private static Queue testQueue;

	@PersistenceUnit(unitName = "TEST-EM-APPCLIENT")
	static EntityManagerFactory emf;

	/*
	 * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
	 * generateSQL;
	 *
	 * @class.testArgs: -ap tssql.stmt
	 *
	 */
	public void setup(String[] args, Properties props) throws Fault {
		this.props = props;
		try {
			nctx = new TSNamingContext();
			logMsg("[Client] Setup succeed (got naming context).");
		} catch (Exception e) {
			throw new Fault("Setup failed:", e);
		}
	}

	public static void main(String[] args) {
		TestAppClient theTests = new TestAppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/**
	 * @testName: testMailSession
	 *
	 * @assertion_ids: JavaEE:SPEC:323
	 *
	 * @test_Strategy:
	 *
	 *                 We check that:
	 *
	 *                 - When the meta-data complete attribute is set to
	 *                 true,Resource annotation should be ignored - as it is one of
	 *                 the annotations to which metadata-complete is applicable.
	 *
	 */
	public void testMailSession() throws Fault {
		try {
			Object sess = nctx.lookup("mail/MailSession");
			logMsg("sess" + sess);
			if (null == sess) {
				throw new Fault("lookup failed!");
			}
		} catch (Exception e) {
			throw new Fault("MailSession test failed: " + e, e);
		}
	}

	/**
	 * @testName: testMailSessionDefinition
	 *
	 * @assertion_ids: JavaEE:SPEC:323
	 *
	 * @test_Strategy:
	 *
	 *                 We check that:
	 *
	 *                 - When the meta-data complete attribute is set to
	 *                 true,MailSessionDefinition annotation should be ignored - as
	 *                 it is one of the annotations to which metadata-complete is
	 *                 applicable.
	 *
	 */
	public void testMailSessionDefinition() throws Fault {
		try {
			logMsg("session" + session);
			logMsg("mail.debug:" + session.getProperty("mail.debug"));
			if (null != session) {
				throw new Fault("MailSessionDefinition test failed!");
			}
		} catch (Exception e) {
			throw new Fault("MailSessionDefinition test failed: " + e, e);
		}
	}

	/**
	 * @testName: testEJBAnnotation
	 *
	 * @assertion_ids: JavaEE:SPEC:323
	 *
	 * @test_Strategy:
	 *
	 *                 We check that:
	 *
	 *                 - When the meta-data complete attribute is set to true,EJB
	 *                 annotation should be ignored - as it is one of the
	 *                 annotations to which metadata-complete is applicable.
	 *
	 */
	public void testEJBAnnotation() throws Fault {
		try {
			logMsg("datastore" + dataStore);
			if (null != dataStore) {
				throw new Fault("EJBAnnotation test failed!");
			}
		} catch (Exception e) {
			throw new Fault("EJBAnnotation test failed: " + e, e);
		}
	}

	/**
	 * @testName: testDataSourceDefinitionAnnotation
	 *
	 * @assertion_ids: JavaEE:SPEC:323
	 *
	 * @test_Strategy:
	 *
	 *                 We check that:
	 *
	 *                 - When the meta-data complete attribute is set to
	 *                 true,DataSourceDefinition annotation should be ignored - as
	 *                 it is one of the annotations to which metadat-complete is
	 *                 applicable.
	 *
	 */
	public void testDataSourceDefinitionAnnotation() throws Fault {
		try {
			Object dataSource = nctx.lookup("java:global/MyApp/MyDataSource");
			if (dataSource != null) {
				throw new Fault("DataSourceDefinition test failed!");
			}
		} catch (Exception e) {
			throw new Fault("DataSourceDefinition test failed: " + e, e);
		}
	}

	/**
	 * @testName: testJMSConnectionFactoryDefinitionAnnotation
	 *
	 * @assertion_ids: JavaEE:SPEC:323
	 *
	 * @test_Strategy:
	 *
	 *                 We check that:
	 *
	 *                 - When the meta-data complete attribute is set to
	 *                 true,JMSConnectionFactoryDefinition annotation should be
	 *                 ignored - as it is one of the annotations to which
	 *                 metadata-complete is applicable.
	 *
	 */
	public void testJMSConnectionFactoryDefinitionAnnotation() throws Fault {
		try {
			Object connFactory = nctx.lookup("java:global/JSPMyTestConnectionFactory");
			logMsg("connFactory" + connFactory);
			if (connFactory != null) {
				throw new Fault("JMSConnectionFactoryDefinition test failed!");
			}
		} catch (Exception e) {
			throw new Fault("JMSConnectionFactoryDefinition test failed: " + e, e);
		}
	}

	/**
	 * @testName: testJMSDestinationDefinitionAnnotation
	 *
	 * @assertion_ids: JavaEE:SPEC:323
	 *
	 * @test_Strategy:
	 *
	 *                 We check that:
	 *
	 *                 - When the meta-data complete attribute is set to
	 *                 true,JMSDestinationDefinition annotation should be ignored -
	 *                 as it is one of the annotations to which metadata-complete is
	 *                 applicable.
	 *
	 */
	public void testJMSDestinationDefinitionAnnotation() throws Fault {
		try {
			Object destination = nctx.lookup("java:app/jms/myappTopic");
			logMsg("destination" + destination);
			if (destination != null) {
				throw new Fault("JMSDestinationDefinition test failed!");
			}
		} catch (Exception e) {
			throw new Fault("JMSDestinationDefinition test failed: " + e, e);
		}
	}

	/**
	 * @testName: testConnectionFactoryDefinitionAnnotation
	 *
	 * @assertion_ids: JavaEE:SPEC:323
	 *
	 * @test_Strategy:
	 *
	 *                 We check that:
	 *
	 *                 - When the meta-data complete attribute is set to
	 *                 true,ConnectionFactoryDefinition annotation should be ignored
	 *                 - as it is one of the annotations to which metadata-complete
	 *                 is applicable.
	 *
	 */
	public void testConnectionFactoryDefinitionAnnotation() throws Fault {
		try {
			logMsg("testFac" + testFac);
			if (testFac != null) {
				throw new Fault("ConnectionFactoryDefinition test failed!");
			}
		} catch (Exception e) {
			throw new Fault("ConnectionFactoryDefinition test failed: " + e, e);
		}
	}

	/**
	 * @testName: testAdministeredObjectDefinitionAnnotation
	 *
	 * @assertion_ids: JavaEE:SPEC:323
	 *
	 * @test_Strategy:
	 *
	 *                 We check that:
	 *
	 *                 - When the meta-data complete attribute is set to
	 *                 true,AdministeredObjectDefinition annotation should be
	 *                 ignored - as it is one of the annotations to which
	 *                 metadata-complete is applicable.
	 *
	 */
	public void testAdministeredObjectDefinitionAnnotation() throws Fault {
		try {
			logMsg("testQueue" + testQueue);
			if (testFac != null) {
				throw new Fault("AdministeredObjectDefinition test failed!");
			}
		} catch (Exception e) {
			throw new Fault("AdministeredObjectDefinition test failed: " + e, e);
		}
	}

	/**
	 * @testName: testPersistenceUnitDefinitionAnnotation
	 *
	 * @assertion_ids: JavaEE:SPEC:323
	 *
	 * @test_Strategy:
	 *
	 *                 We check that:
	 *
	 *                 - When the meta-data complete attribute is set to
	 *                 true,PersistenceUnitDefinition annotation should be ignored -
	 *                 as it is one of the annotations to which metadata-complete is
	 *                 applicable.
	 *
	 */
	public void testPersistenceUnitDefinitionAnnotation() throws Fault {
		try {
			logMsg("emf" + emf);
			if (emf != null) {
				throw new Fault("PersistenceUnitDefinition test failed!");
			}
		} catch (Exception e) {
			throw new Fault("PersistenceUnitDefinition test failed: " + e, e);
		}
	}

	public void cleanup() throws Fault {
		logMsg("[Client] cleanup()");
	}
}
