/*
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
package org.jboss.cdi.tck.tests.context.passivating.dependency.resource.persistence;

import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;

@SessionScoped
public class Pool implements Serializable {

    private static final long serialVersionUID = -6404342841354281000L;

    private String id;

    @Inject
    @Another
    DataSource dataSource;

    public String getId() {
        return id;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @PostConstruct
    public void init() {
        this.id = UUID.randomUUID().toString();
    }

}
