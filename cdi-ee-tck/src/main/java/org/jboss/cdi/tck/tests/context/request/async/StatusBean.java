/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.context.request.async;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StatusBean {

    private boolean onError;
    private boolean onComplete;
    private boolean onTimeout;
    private boolean onStartAsync;
    private Long requestBeanId;

    public Long getRequestBeanId() {
        return requestBeanId;
    }

    public void setRequestBeanId(Long requestBeanId) {
        this.requestBeanId = requestBeanId;
    }

    public boolean isOnStartAsync() {
        return onStartAsync;
    }

    public void setOnStartAsync(boolean onStartAsync) {
        this.onStartAsync = onStartAsync;
    }

    public boolean isOnComplete() {
        return onComplete;
    }

    public void setOnComplete(boolean onComplete) {
        this.onComplete = onComplete;
    }

    public boolean isOnError() {
        return onError;
    }

    public void setOnError(boolean onError) {
        this.onError = onError;
    }

    public boolean isOnTimeout() {
        return onTimeout;
    }

    public void setOnTimeout(boolean onTimeout) {
        this.onTimeout = onTimeout;
    }

    public void reset(){
        this.onComplete = false;
        this.onStartAsync = false;
        this.onTimeout = false;
        this.onError = false;
        this.requestBeanId = null;

    }

}
