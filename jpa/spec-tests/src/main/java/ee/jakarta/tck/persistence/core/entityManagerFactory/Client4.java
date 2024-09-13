/*
 * Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.entityManagerFactory;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;


public class Client4 extends PMClientBase {


    public Client4() {
    }
    public static void main(String[] args) {
   		Client4 theTests = new Client4();
   		Status s = theTests.run(args, System.out, System.err);
   		s.exit();
   	}

    public void setup(String[] args, Properties p) throws Exception {
        logTrace( "setup");
        try {
            super.setup(args,p);
        } catch (Exception e) {
            logErr( "Exception: ", e);
            throw new Exception("Setup failed:", e);
        }
    }
    
    public void cleanup() throws Exception {
        try {
            super.cleanup();
        } finally {

        }
    }

    
    public void callInTransactionTest() throws Exception {
        final int MEMBER_ID = 10;

        boolean pass = false;
        try {
            Member member = getEntityManager().getEntityManagerFactory().callInTransaction(em -> {
                Member newMember = new Member(MEMBER_ID, String.valueOf(MEMBER_ID));
                em.persist(newMember);
                return newMember;
            });
            Member foundMember = getEntityManager().find(Member.class, MEMBER_ID);
            if (member.equals(foundMember)) {
                pass = true;
            } else {
                logErr( "Stored entity data are not same as found");
            }
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("callInTransactionTest failed");
        }
    }

    
    public void runInTransactionTest() throws Exception {
        final int MEMBER_ID = 11;

        boolean pass = false;
        try {
            getEntityManager().getEntityManagerFactory().runInTransaction(em -> {
                Member newMember = new Member(MEMBER_ID, String.valueOf(MEMBER_ID));
                em.persist(newMember);
            });
            Member foundMember = getEntityManager().find(Member.class, MEMBER_ID);
            if (foundMember != null) {
                pass = true;
            } else {
                logErr( "Stored entity data was not found");
            }
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("runInTransactionTest failed");
        }
    }

    
    public void getNameTest() throws Exception {

        boolean pass = false;
        try {
            String puName = getEntityManager().getEntityManagerFactory().getName();
            if (getPersistenceUnitName().equals(puName)) {
                pass = true;
            } else {
                logErr( "Persistence unit name |" + puName + "| doesn't match with expected |" + getPersistenceUnitName() + "|");
            }
        } catch (Exception e) {
            logErr( "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("getNameTest failed");
        }
    }
}
