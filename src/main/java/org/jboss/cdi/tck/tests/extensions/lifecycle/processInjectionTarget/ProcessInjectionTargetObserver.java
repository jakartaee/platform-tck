/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionTarget;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessInjectionTarget;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.jsp.tagext.SimpleTag;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class ProcessInjectionTargetObserver implements Extension {

    private static ProcessInjectionTarget<TestServlet> servletEvent = null;
    private static ProcessInjectionTarget<TestListener> listenerEvent = null;
    private static ProcessInjectionTarget<TestFilter> filterEvent = null;
    private static ProcessInjectionTarget<TagLibraryListener> tagLibraryListenerEvent = null;
    private static ProcessInjectionTarget<TestTagHandler> tagHandlerEvent = null;
    private static ProcessInjectionTarget<Farm> jsfManagedBeanEvent = null;

    private static boolean servletSuperTypeObserved = false;
    private static boolean servletSubTypeObserved = false;
    private static boolean listenerSuperTypeObserved = false;
    private static boolean tagHandlerSuperTypeObserved = false;
    private static boolean tagHandlerSubTypeObserved = false;
    private static boolean stringObserved = false;

    public void observeJsfManagedBean(@Observes ProcessInjectionTarget<Farm> event, BeanManager beanManager) {
        jsfManagedBeanEvent = event;
    }

    public void observeServlet(@Observes ProcessInjectionTarget<TestServlet> event, BeanManager beanManager) {
        servletEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TestServlet>(event.getInjectionTarget(), () -> TestServlet.setIsWrappedInjectionSuccessfull(true)));
    }

    public void observeFilter(@Observes ProcessInjectionTarget<TestFilter> event, BeanManager beanManager) {
        filterEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TestFilter>(event.getInjectionTarget(), () -> TestFilter.setIsWrappedInjectionSuccessfull(true)));
    }

    public void observeListener(@Observes ProcessInjectionTarget<TestListener> event, BeanManager beanManager) {
        listenerEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TestListener>(event.getInjectionTarget(), () -> TestListener.setIsWrappedInjectionSuccessfull(true)));
    }

    public void observeTagHandler(@Observes ProcessInjectionTarget<TestTagHandler> event, BeanManager beanManager) {
        tagHandlerEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TestTagHandler>(event.getInjectionTarget(), () -> TestTagHandler.setIsWrappedInjectionSuccessfull(true)));
    }

    public void observeTagLibraryListener(@Observes ProcessInjectionTarget<TagLibraryListener> event, BeanManager beanManager) {
        tagLibraryListenerEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TagLibraryListener>(event.getInjectionTarget(), () -> TagLibraryListener.setIsWrappedInjectionSuccessfull(true)));
    }

    public void observeServletSuperType(@Observes ProcessInjectionTarget<? super HttpServlet> event) {
        servletSuperTypeObserved = true;
    }

    public void observeServletSubType(@Observes ProcessInjectionTarget<? extends HttpServlet> event) {
        servletSubTypeObserved = true;

    }

    public void observeListenerSuperType(@Observes ProcessInjectionTarget<? super ServletContextListener> event) {
        listenerSuperTypeObserved = true;
    }

    public void tagHandlerSuperType(@Observes ProcessInjectionTarget<? super SimpleTagSupport> event) {
        tagHandlerSuperTypeObserved = true;
    }

    public void tagHandlerSubType(@Observes ProcessInjectionTarget<? extends SimpleTag> event) {
        tagHandlerSubTypeObserved = true;
    }

    public void observeSessionBean(@Observes ProcessInjectionTarget<Fence> event, BeanManager beanManager) {
        event.setInjectionTarget(new CustomInjectionTarget<Fence>(event.getInjectionTarget(), () -> Fence.setIsWrappedInjectionSuccessfull(true)));
    }

    public void observeEJBInterceptor(@Observes ProcessInjectionTarget<FenceInterceptor> event, BeanManager beanManager) {
        event.setInjectionTarget(new CustomInjectionTarget<FenceInterceptor>(event.getInjectionTarget(), () -> FenceInterceptor.setIsWrappedInjectionSuccessfull(true)));
    }

    public void stringObserver(@Observes ProcessInjectionTarget<String> event) {
        stringObserved = true;
    }

    public static ProcessInjectionTarget<TestServlet> getServletEvent() {
        return servletEvent;
    }

    public static ProcessInjectionTarget<TestListener> getListenerEvent() {
        return listenerEvent;
    }

    public static ProcessInjectionTarget<TestFilter> getFilterEvent() {
        return filterEvent;
    }

    public static ProcessInjectionTarget<TagLibraryListener> getTagLibraryListenerEvent() {
        return tagLibraryListenerEvent;
    }

    public static ProcessInjectionTarget<TestTagHandler> getTagHandlerEvent() {
        return tagHandlerEvent;
    }

    public static ProcessInjectionTarget<Farm> getJsfManagedBeanEvent() {
        return jsfManagedBeanEvent;
    }

    public static boolean isServletSuperTypeObserved() {
        return servletSuperTypeObserved;
    }

    public static boolean isServletSubTypeObserved() {
        return servletSubTypeObserved;
    }

    public static boolean isListenerSuperTypeObserved() {
        return listenerSuperTypeObserved;
    }

    public static boolean isTagHandlerSuperTypeObserved() {
        return tagHandlerSuperTypeObserved;
    }

    public static boolean isTagHandlerSubTypeObserved() {
        return tagHandlerSubTypeObserved;
    }

    public static boolean isStringObserved() {
        return stringObserved;
    }
}
