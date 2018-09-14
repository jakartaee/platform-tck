/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package com.sun.ts.tests.jsonb.customizedmapping.serializers.model.serializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;

import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.Animal;

public class AnimalListDeserializer implements JsonbDeserializer<List<Animal>> {
  private AnimalDeserializer animalDeserializer = new AnimalDeserializer();

  public List<Animal> deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext, Type type) {
    List<Animal> animals = new ArrayList<>();
    while (jsonParser.hasNext()) {
      JsonParser.Event event = jsonParser.next();
      while (event == JsonParser.Event.START_OBJECT) {
        animals.add(animalDeserializer.deserialize(jsonParser,
            deserializationContext, type));
        event = jsonParser.next();
      }
    }
    return animals;
  }
}
