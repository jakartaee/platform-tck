package org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback.ejb;

import jakarta.inject.Inject;
import org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback.Bar;

public abstract class Animal {

    @Inject
    protected Bar bar;

    void foo() {
    }

    public Bar getBar() {
        return bar;
    }

    public abstract String getAnimalType();
}
