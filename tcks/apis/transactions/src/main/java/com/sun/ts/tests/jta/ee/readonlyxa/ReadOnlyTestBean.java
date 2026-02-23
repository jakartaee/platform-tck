/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jta.ee.readonlyxa;

import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

public class ReadOnlyTestBean {
    public static final String NAME = "read-only-test-bean";

    @Resource(lookup = "java:comp/env/eis/whitebox-tx")
    private TSDataSource ds;

    public ReadOnlyTestBean() {
    }

    public String getName() {
        return NAME;
    }

	@Transactional
	public void setup() {
		try {
			TSConnection connection = ds.getConnection();
			connection.dropTable();
			connection.insert( "2", "Hello" );
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void tearDown() {
		try {
			ds.getConnection().dropTable();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    @Transactional(value = TxType.REQUIRED, readOnly = true)
    public void insert() {
		try {
			ds.getConnection().insert("1", "Hello");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    @Transactional(value = TxType.REQUIRED, readOnly = true)
    public String get() {
        try {
            return ds.getConnection().readValue("1");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
