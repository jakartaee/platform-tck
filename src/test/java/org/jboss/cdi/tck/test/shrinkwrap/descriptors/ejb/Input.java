/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.test.shrinkwrap.descriptors.ejb;

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;

/**
 *
 * @author Martin Kouba
 */
public class Input implements LSInput {

    private String publicId;

    private String systemId;

    private InputStream inputStream;

    public Input(String publicId, String systemId, InputStream inputStream) {
        super();
        this.publicId = publicId;
        this.systemId = systemId;
        this.inputStream = inputStream;
    }

    @Override
    public Reader getCharacterStream() {
        return null;
    }

    @Override
    public void setCharacterStream(Reader characterStream) {
    }

    @Override
    public InputStream getByteStream() {
        return inputStream;
    }

    @Override
    public void setByteStream(InputStream byteStream) {
    }

    @Override
    public String getStringData() {
        return null;
    }

    @Override
    public void setStringData(String stringData) {
    }

    @Override
    public String getSystemId() {
        return systemId;
    }

    @Override
    public void setSystemId(String systemId) {
    }

    @Override
    public String getPublicId() {
        return publicId;
    }

    @Override
    public void setPublicId(String publicId) {
    }

    @Override
    public String getBaseURI() {
        return null;
    }

    @Override
    public void setBaseURI(String baseURI) {
    }

    @Override
    public String getEncoding() {
        return null;
    }

    @Override
    public void setEncoding(String encoding) {
    }

    @Override
    public boolean getCertifiedText() {
        return false;
    }

    @Override
    public void setCertifiedText(boolean certifiedText) {
    }

}
