/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer;

import java.lang.reflect.Type;

import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog;

import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;

public class AnimalDeserializer implements JsonbDeserializer<Animal> {
  public Animal deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext, Type type) {
    Animal animal = null;
    while (jsonParser.hasNext()) {
      JsonParser.Event event = jsonParser.next();
      if (event == JsonParser.Event.START_OBJECT) {
        continue;
      }
      if (event == JsonParser.Event.END_OBJECT) {
        break;
      }
      if (event == JsonParser.Event.KEY_NAME) {
        switch (jsonParser.getString()) {
        case "type":
          jsonParser.next();
          switch (jsonParser.getString()) {
          case "cat":
            animal = new Cat();
            break;
          case "dog":
            animal = new Dog();
            break;
          default:
            animal = new Animal();
          }
          break;
        case "name":
          jsonParser.next();
          animal.setName(jsonParser.getString());
          break;
        case "age":
          jsonParser.next();
          animal.setAge(jsonParser.getInt());
          break;
        case "furry":
          event = jsonParser.next();
          animal.setFurry(event == JsonParser.Event.VALUE_TRUE);
          break;
        case "weight":
          jsonParser.next();
          animal.setWeight(jsonParser.getBigDecimal().floatValue());
          break;
        case "cuddly":
          event = jsonParser.next();
          ((Cat) animal).setCuddly(event == JsonParser.Event.VALUE_TRUE);
          break;
        case "barking":
          event = jsonParser.next();
          ((Dog) animal).setBarking(event == JsonParser.Event.VALUE_TRUE);
        }
      }
    }
    return animal;
  }
}
