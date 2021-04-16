package org.jboss.cdi.tck.tests.alternative.selection.enterprise;

import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.tests.alternative.selection.AssertBean;
import org.jboss.cdi.tck.tests.alternative.selection.Tame;
import org.jboss.cdi.tck.tests.alternative.selection.TestBean;
import org.jboss.cdi.tck.tests.alternative.selection.Wild;

public class SelectedAlternativeTestUtil {

    public static WebArchiveBuilder createBuilderBase() {
        return new WebArchiveBuilder().withLibrary(org.jboss.cdi.tck.tests.alternative.selection.SelectedAlternativeTestUtil.class, TestBean.class, Wild.class, Tame.class,
                                                   AssertBean.class);
    }
    public static EnterpriseArchiveBuilder createEnterpriseBuilderBase() {
        return new EnterpriseArchiveBuilder().withLibrary(org.jboss.cdi.tck.tests.alternative.selection.SelectedAlternativeTestUtil.class, TestBean.class, Wild.class,
                                                          Tame.class, AssertBean.class);
    }

}
