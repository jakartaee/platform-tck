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
package org.jboss.cdi.tck.tests.extensions.lifecycle.events;

import static org.testng.Assert.fail;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.AfterTypeDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessBean;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.enterprise.inject.spi.ProcessInjectionPoint;
import jakarta.enterprise.inject.spi.ProcessInjectionTarget;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
import jakarta.enterprise.inject.spi.ProcessObserverMethod;
import jakarta.enterprise.inject.spi.ProcessProducer;
import jakarta.enterprise.inject.spi.ProcessProducerField;
import jakarta.enterprise.inject.spi.ProcessProducerMethod;
import jakarta.enterprise.inject.spi.ProcessSessionBean;
import jakarta.enterprise.inject.spi.ProcessSyntheticAnnotatedType;

public class TestExtension implements Extension {

    private BeforeBeanDiscovery bbd;
    private AfterTypeDiscovery atd;
    private AfterBeanDiscovery abd;
    private ProcessAnnotatedType<SimpleBean> pat;
    private ProcessSyntheticAnnotatedType<SimpleBean> psat;
    private AfterDeploymentValidation adv;
    private ProcessBean<SimpleBean> pb;
    private ProcessBeanAttributes<SimpleBean> pba;
    private ProcessObserverMethod<SimpleBean, ?> pom;
    private ProcessInjectionTarget<SimpleBean> pit;
    private ProcessInjectionPoint<SimpleBean, ?> pip;
    private ProcessProducer<SimpleBean, Integer> pp;
    private ProcessManagedBean<SimpleBean> pmb;
    private ProcessProducerField<Integer,SimpleBean> ppf;
    private ProcessProducerMethod<Integer,SimpleBean> ppm;

    void observesBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager bm) {
        this.bbd = event;
        bbd.addAnnotatedType(bm.createAnnotatedType(SimpleBean.class), TestExtension.class.getName() + ":" + SimpleBean.class.getName());
    }

    void observesProcessAnnotatedType(@Observes ProcessAnnotatedType<SimpleBean> event) {
        if (!(event instanceof ProcessSyntheticAnnotatedType<?>)) {
            this.pat = event;
        }
    }

    void observesProcessSyntheticAnnotatedType(@Observes ProcessSyntheticAnnotatedType<SimpleBean> event) {
        this.psat = event;
    }

    void observesAfterTypeDiscovery(@Observes AfterTypeDiscovery event) {
        this.atd = event;
    }

    void observesProcessInjectionTarget(@Observes ProcessInjectionTarget<SimpleBean> event) {
        this.pit = event;
    }

    void observesProcessProducer(@Observes ProcessProducer<SimpleBean, Integer> event) {
        this.pp = event;
    }

    void observesProcessInjectionPoint(@Observes ProcessInjectionPoint<SimpleBean, ?> event) {
        this.pip = event;
    }

    void observesProcessBeanAttributes(@Observes ProcessBeanAttributes<SimpleBean> event) {
        this.pba = event;
    }

    // Modifying  PAT in another lifecycle event should also fail with IllegalStateException
    void observesProcessBean(@Observes ProcessBean<SimpleBean> event, BeanManager bm) {

        this.pb = event;
        try {
            pat.getAnnotatedType();
            fail("Expected exception not thrown");
        } catch (IllegalStateException expected) {
        }

        try {
            pat.setAnnotatedType(bm.createAnnotatedType(SimpleBean.class));
            fail("Expected exception not thrown");
        } catch (IllegalStateException expected) {
        }

    }

    void observesProcessManagedBean(@Observes ProcessManagedBean<SimpleBean> event) {
        this.pmb = event;
    }

    void observesProcessProducerField(@Observes ProcessProducerField<Integer,SimpleBean> event) {
        this.ppf = event;
    }

    void observesProcessSessionBean(@Observes ProcessProducerMethod<Integer,SimpleBean> event) {
        this.ppm = event;
    }

    void observesProcessObserverMethod(@Observes ProcessObserverMethod<SimpleBean, ?> event) {
        this.pom = event;
    }

    void observesAfterBeanDiscovery(@Observes AfterBeanDiscovery event) {
        this.abd = event;
    }

    void observesAfterDeploymentValidation(@Observes AfterDeploymentValidation event) {
        this.adv = event;
    }

    public BeforeBeanDiscovery getBeforeBeanDiscovery() {
        return bbd;
    }

    public AfterTypeDiscovery getAfterTypeDiscovery() {
        return atd;
    }

    public AfterBeanDiscovery getAfterBeanDiscovery() {
        return abd;
    }

    public AfterDeploymentValidation getAfterDeploymentValidation() {
        return adv;
    }

    public ProcessBean<SimpleBean> getProcessBean() {
        return pb;
    }

    public ProcessAnnotatedType<SimpleBean> getProcessAnnotatedType() {
        return pat;
    }

    public ProcessSyntheticAnnotatedType<SimpleBean> getProcessSyntheticAnnotatedType() {
        return psat;
    }

    public ProcessBeanAttributes<SimpleBean> getProcessBeanAttributes() {
        return pba;
    }

    public ProcessObserverMethod<SimpleBean, ?> getProcessObserverMethod() {
        return pom;
    }

    public ProcessInjectionTarget<SimpleBean> getProcessInjectionTarget() {
        return pit;
    }

    public ProcessInjectionPoint<SimpleBean, ?> getProcessInjectionPoint() {
        return pip;
    }

    public ProcessProducer<SimpleBean, Integer> getProcessProducer() {
        return pp;
    }

    public ProcessManagedBean<SimpleBean> getProcessManagedBean() {
        return pmb;
    }

    public ProcessProducerField<Integer,SimpleBean> getProcessProducerField() {
        return ppf;
    }

    public ProcessProducerMethod<Integer,SimpleBean> getProcessProducerMethod() {
        return ppm;
    }

}
