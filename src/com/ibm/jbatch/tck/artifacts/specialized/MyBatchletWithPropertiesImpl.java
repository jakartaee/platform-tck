/*
 * Copyright 2012 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.ibm.jbatch.tck.artifacts.specialized;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;

@javax.inject.Named("myBatchletWithPropertiesImpl")
public class MyBatchletWithPropertiesImpl extends AbstractBatchlet {

	private final static Logger logger = Logger.getLogger(MyBatchletWithPropertiesImpl.class.getName());
	
    private static int count = 1;

    public static String GOOD_EXIT_CODE = "VERY GOOD INVOCATION";

    @Inject    
    @BatchProperty
    private String myProperty1;

    @Inject    
    @BatchProperty
    public String myProperty2 = "This EYECATCHER should get overwritten from the job xml!!";

    @Inject    
    @BatchProperty
    public String myDefaultProp1 = "Should get overwritten by default value";
    
    @Inject    
    @BatchProperty
    public String mySubmittedProp = "This EYECATCHER should get overwritten by a submitted prop.";
    
    @Inject    
    @BatchProperty
    public String batchletProp = "This EYECATCHER should get overwritten.";
    
    @Inject    
    @BatchProperty
    private String javaDefaultValueProp = "JAVA DEFAULT INITIALIZER";
    
    @Inject    
    @BatchProperty(name="myProperty4")
    private String property4;

    @Inject    
    @BatchProperty
    String myConcatProp;
    
    @Inject    
    @BatchProperty
    String myJavaSystemProp;
    
    @Inject    
    @BatchProperty
    String defaultPropName1;
    
    @Inject    
    @BatchProperty
    String defaultPropName2;
    
    @Override
    public String process() throws Exception {

        //FIXME use a submitted job parameter here instead so all tests are independent of each other.
        String propName = System.getProperty("property.junit.propName");
        String propertyValue =  this.getBatchPropertyValue(propName);
        
        if (propertyValue == null) {
            System.clearProperty("property.junit.result");
        } else {
            System.setProperty("property.junit.result", "" + propertyValue);
        }
        
        return this.getBatchPropertyValue(propName);
    	
            
    }

    
    @Override
    public void stop() throws Exception {
        logger.fine("MyBatchletWithProperties.cancel() - @Cancel #" + count);
    }


    
    private String getBatchPropertyValue(String name) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = MyBatchletWithPropertiesImpl.class.getDeclaredFields();
        
        for (Field field: fields) {
            BatchProperty batchProperty = field.getAnnotation(BatchProperty.class);
            if (batchProperty != null) {
                if (!batchProperty.name().equals("") && batchProperty.name().equals(name)){
                    return (String)field.get(this);
                } else if (field.getName().equals(name)){
                    return (String)field.get(this);
                }
            } 
        }
        
        return null;
        
    }
}
