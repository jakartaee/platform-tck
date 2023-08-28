package com.sun.ts.tests.servlet.common.servlets;

import com.sun.ts.tests.servlet.api.common.sharedfiles.HSessionAttributeListener;
import com.sun.ts.tests.servlet.api.common.sharedfiles.HSessionListener;
import com.sun.ts.tests.servlet.common.response.HttpResponseTestServlet;
import com.sun.ts.tests.servlet.common.response.ResponseTestServlet;
import com.sun.ts.tests.servlet.common.response.ResponseTests;
import com.sun.ts.tests.servlet.common.util.Data;
import com.sun.ts.tests.servlet.common.util.ServletTestUtil;
import com.sun.ts.tests.servlet.common.util.StaticLog;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import java.util.ArrayList;
import java.util.List;

public class CommonServlets {

    private static final CommonServlets INSTANCE = new CommonServlets();

    private final JavaArchive[] javaArchives;

    private CommonServlets() {
        List<JavaArchive> archives = new ArrayList<>();
        archives.add(ShrinkWrap.create(JavaArchive.class, "common-servlets.jar")
                .addClasses(GenericCheckTestResultServlet.class, GenericTCKServlet.class, RequestTestServlet.class,
                        HttpCheckTestResultServlet.class, HttpRequestTestServlet.class, RequestTests.class,
                        HttpTCKServlet.class, Data.class, StaticLog.class, ServletTestUtil.class,
                        ResponseTests.class, ResponseTestServlet.class, HttpResponseTestServlet.class,
                        HSessionListener.class, HSessionAttributeListener.class));

        javaArchives = archives.toArray(new JavaArchive[0]);
    }

    public static JavaArchive[] getCommonServletsArchive() {
        return INSTANCE.javaArchives;
    }

}
