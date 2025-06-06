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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.ee.packaging.web.scope;


import ee.jakarta.tck.persistence.ee.util.AbstractUrlClient;
import org.junit.jupiter.api.Test;

public class Client extends AbstractUrlClient {



	public static final String SERVLET_NAME = "ServletTest";

	public static final String CONTEXT_ROOT = "/jpa_ee_packaging_web_scope";

	/*
	 * @class.setup_props: webServerHost; webServerPort;
	 */

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:894; PERSISTENCE:SPEC:898;
	 * PERSISTENCE:SPEC:900; PERSISTENCE:SPEC:902
	 * 
	 * @test_Strategy: In JavaEE environments, the root of the persistence unit may
	 * be the WEB-INF classes directory of a WAR file. The persistence.xml file is
	 * therefore contained in the resided in the WEB-INF/classes/META-INF directory.
	 *
	 * A container mananaged entity managed is obtained by the application through
	 * direct lookup of the entity manager in the JNDI namespace.
	 *
	 * Client -> SERVLET -> ENTITY -> DB
	 *
	 */
	@Test
	public void test1() throws Exception {
		setServletName(SERVLET_NAME);
		setContextRoot(CONTEXT_ROOT);
		TEST_PROPS.setProperty(APITEST, "test1");
		invoke();
	}

}
