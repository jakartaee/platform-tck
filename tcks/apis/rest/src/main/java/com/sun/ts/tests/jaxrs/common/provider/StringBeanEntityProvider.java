/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.common.provider;

import com.sun.ts.tests.jaxrs.common.util.JaxrsUtil;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class StringBeanEntityProvider implements MessageBodyReader<StringBean>, MessageBodyWriter<StringBean> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return StringBean.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(StringBean t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return t.get().length();
    }

    @Override
    public void writeTo(StringBean t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        entityStream.write(t.get().getBytes());
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isWriteable(type, genericType, annotations, mediaType);
    }

    @Override
    public StringBean readFrom(Class<StringBean> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        String stream = JaxrsUtil.readFromStream(entityStream);
        StringBean bean = new StringBean(stream);
        return bean;
    }

}
