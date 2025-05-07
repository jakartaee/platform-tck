/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.glassfish.persistence.tck;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ondro Mihalyi
 */
public class UserTransactionWrapper implements EntityTransaction {

    UserTransaction userTx;

    public UserTransactionWrapper(UserTransaction userTx) {
        this.userTx = userTx;
    }

    public void begin() {
        try {
            userTx.begin();
        } catch (NotSupportedException | SystemException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @FunctionalInterface
    public interface RunnableWithException {

        void run() throws Exception;
    }

    private void withHandledException(RunnableWithException runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        };
    }

    public void commit() {
        withHandledException(userTx::commit);
    }

    public void rollback() {
        withHandledException(userTx::rollback);
    }

    public void setRollbackOnly() {
        withHandledException(userTx::setRollbackOnly);
    }

    public boolean getRollbackOnly() {
        try {
            return userTx.getStatus() == Status.STATUS_MARKED_ROLLBACK;
        } catch (SystemException e) {
            throw new PersistenceException(e);
        }
    }

    public boolean isActive() {
        try {
            int txStatus = userTx.getStatus();
            return ((txStatus == Status.STATUS_ACTIVE)
                    || (txStatus == Status.STATUS_MARKED_ROLLBACK));
        } catch (SystemException e) {
            throw new PersistenceException(e);
        }
    }

    public void setTimeout(Integer timeout) {
    }

    public Integer getTimeout() {
        return null;
    }

}
