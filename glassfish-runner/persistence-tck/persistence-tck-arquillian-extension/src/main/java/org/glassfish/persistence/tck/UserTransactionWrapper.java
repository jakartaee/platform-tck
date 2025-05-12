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
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import java.util.logging.Logger;

import static jakarta.transaction.Status.STATUS_ACTIVE;
import static jakarta.transaction.Status.STATUS_MARKED_ROLLBACK;
import static java.util.logging.Level.SEVERE;

/**
 *
 * @author Ondro Mihalyi
 */
public class UserTransactionWrapper implements EntityTransaction {

    UserTransaction userTx;

    public UserTransactionWrapper(UserTransaction userTx) {
        this.userTx = userTx;
    }

    @Override
    public void begin() {
        try {
            userTx.begin();
        } catch (NotSupportedException | SystemException ex) {
            Logger.getLogger(this.getClass().getName()).log(SEVERE, null, ex);
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
            Logger.getLogger(this.getClass().getName()).log(SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
        ;
    }

    @Override
    public void commit() {
        withHandledException(userTx::commit);
    }

    @Override
    public void rollback() {
        withHandledException(userTx::rollback);
    }

    @Override
    public void setRollbackOnly() {
        withHandledException(userTx::setRollbackOnly);
    }

    @Override
    public boolean getRollbackOnly() {
        try {
            return userTx.getStatus() == STATUS_MARKED_ROLLBACK;
        } catch (SystemException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean isActive() {
        try {
            int txStatus = userTx.getStatus();

            return txStatus == STATUS_ACTIVE || txStatus == STATUS_MARKED_ROLLBACK;
        } catch (SystemException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void setTimeout(Integer timeout) {
    }

    @Override
    public Integer getTimeout() {
        return null;
    }

}
