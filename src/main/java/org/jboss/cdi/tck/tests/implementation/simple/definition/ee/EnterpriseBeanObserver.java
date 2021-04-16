package org.jboss.cdi.tck.tests.implementation.simple.definition.ee;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
import org.jboss.cdi.tck.tests.implementation.simple.definition.Sheep;

public class EnterpriseBeanObserver implements Extension {

    public static boolean observedEnterpriseBean;
    public static boolean observedAnotherBean;

    public void observeAnotherBean(@Observes ProcessManagedBean<Sheep> event) {
        observedAnotherBean = true;
    }

    public void observeEnterpriseBean(@Observes ProcessManagedBean<MockEnterpriseBean> event) {
        observedEnterpriseBean = true;
    }

}
