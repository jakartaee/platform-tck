package com.sun.ts.tests.common.connector.embedded.adapter1;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class RADeployment {
    public static JavaArchive create_whitebox_rd_rar() {
        JavaArchive resouredef_jar = ShrinkWrap.create(JavaArchive.class, "resouredef.jar")
            .addClasses(
                    com.sun.ts.tests.common.connector.embedded.adapter1.CRDActivationSpec.class,
                    com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject.class,
                    com.sun.ts.tests.common.connector.embedded.adapter1.CRDManagedConnectionFactory.class,
                    com.sun.ts.tests.common.connector.embedded.adapter1.CRDMessageListener.class,
                    com.sun.ts.tests.common.connector.embedded.adapter1.CRDMessageWork.class,
                    com.sun.ts.tests.common.connector.embedded.adapter1.CRDResourceAdapterImpl.class,
                    com.sun.ts.tests.common.connector.embedded.adapter1.CRDWorkManager.class,
                    com.sun.ts.tests.common.connector.embedded.adapter1.MsgXAResource.class,
                    com.sun.ts.tests.common.connector.embedded.adapter1.NestedWorkXid1.ContextType.class,
                    com.sun.ts.tests.common.connector.embedded.adapter1.NestedWorkXid1.class,
                    com.sun.ts.tests.common.connector.util.ConnectorStatus.class,
                    com.sun.ts.tests.common.connector.util.Log.class,
                    com.sun.ts.tests.common.connector.whitebox.WorkImpl.class,
                    com.sun.ts.tests.common.connector.whitebox.WorkListenerImpl.class,
                    com.sun.ts.tests.common.connector.whitebox.XidImpl.class,
                    com.sun.ts.tests.common.connector.whitebox.NestedWorkXid.class,
                    com.sun.ts.tests.common.connector.whitebox.TSDataSource.class,
                    com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory.class,
                    com.sun.ts.tests.common.connector.whitebox.Debug.class
            );


        JavaArchive whitebox_rd_rar = ShrinkWrap.create(JavaArchive.class, "whitebox-rd.rar");
        whitebox_rd_rar.add(resouredef_jar, "resouredef.jar", ZipExporter.class);
        String mf = """
                Manifest-Version: 1.0
                Ant-Version: Apache Ant 1.10.14
                Created-By: 17.0.12+7 (Red Hat, Inc.)
                Extension-List: tsharness cts whitebox
                tsharness-Extension-Name: tsharness
                tsharness-Specification-Version: 1.4
                tsharness-Implementation-Version: 1.4
                tsharness-Implementation-Vendor-Id: com.sun
                cts-Extension-Name: cts
                cts-Specification-Version: 1.4
                cts-Implementation-Version: 1.4
                cts-Implementation-Vendor-Id: com.sun
                whitebox-Extension-Name: whitebox
                whitebox-Specification-Version: 1.6
                whitebox-Implementation-Version: 1.6
                whitebox-Implementation-Vendor-Id: com.sun

                """;
        whitebox_rd_rar.setManifest(new StringAsset(mf));
        return whitebox_rd_rar;
    }
}
