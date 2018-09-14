/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.packaging.war.datasource.global;

import static com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest.verifyDataSource;

import java.sql.Connection;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.annotation.sql.DataSourceDefinitions;
import javax.ejb.EJB;
import javax.sql.DataSource;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceIF;


public class Client extends EJBLiteClientBase {
    @EJB
    private DataSourceIF dataSourceBean;
    
    @Resource(lookup="java:global/jdbc/DB3")
    private DataSource db3;
    
    @SuppressWarnings("unused")
    @PostConstruct
    private void postConstruct() {
        boolean c = true;
        Helper.getLogger().info("In postConstruct of " + this);
        
        verifyDataSource(getReasonBuffer(), c, "java:global/jdbc/DB3");
        verifyDataSource(getReasonBuffer(), c, db3);

    }
    
    /*
     * @testName: postConstructRecords
     * @test_Strategy: look up the data sources declared with annotation in this class, 
     * and verify the injected datasource inside PostConstruct method.
     */
    public void postConstructRecords() {
        appendReason("Test result verified inside postConstruct method.");
    }
    
    /*
     * @testName: postConstructRecordsEJB
     * @test_Strategy: look up the data sources declared with annotation in EJB, 
     * and verify the injected datasource inside its PostConstruct method.
     */
    public void postConstructRecordsEJB() {
        appendReason(dataSourceBean.getPostConstructRecords());
    }
    
    /*
     * @testName: getConnectionEJB
     * @test_Strategy: call getConnection() on the datasource
     */
    public void getConnectionEJB() {
        appendReason(dataSourceBean.getConnection());
    }
}

