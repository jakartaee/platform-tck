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

package com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer;

import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.Animal;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.serializer.AnimalBuilder;

import javax.inject.Inject;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
import java.lang.reflect.Type;

import static com.sun.ts.tests.jsonb.customizedmapping.serializers.model.serializer.AnimalBuilder.TYPE.*;

public class AnimalDeserializerInjected implements JsonbDeserializer<Animal> {
  @Inject
  private AnimalBuilder animalBuilder;

  public Animal deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext, Type type) {
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
            animalBuilder.setType(CAT);
            break;
          case "dog":
            animalBuilder.setType(DOG);
            break;
          default:
            animalBuilder.setType(GENERIC);
          }
          break;
        case "name":
          jsonParser.next();
          animalBuilder.setName(jsonParser.getString());
          break;
        case "age":
          jsonParser.next();
          animalBuilder.setAge(jsonParser.getInt());
          break;
        case "furry":
          event = jsonParser.next();
          animalBuilder.setFurry(event == JsonParser.Event.VALUE_TRUE);
          break;
        case "weight":
          jsonParser.next();
          animalBuilder.setWeight(jsonParser.getBigDecimal().floatValue());
          break;
        case "cuddly":
          event = jsonParser.next();
          animalBuilder.setCuddly(event == JsonParser.Event.VALUE_TRUE);
          break;
        case "barking":
          event = jsonParser.next();
          animalBuilder.setBarking(event == JsonParser.Event.VALUE_TRUE);
        }
      }
    }
    return animalBuilder.build();
  }
}
