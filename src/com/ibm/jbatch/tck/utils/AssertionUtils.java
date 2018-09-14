/*
 * Copyright 2012,2013 International Business Machines Corp.
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
package com.ibm.jbatch.tck.utils;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

public class AssertionUtils {
	
	static public void assertObjEquals(Object expected, Object actual) {
		assertWithMessage(null, expected, actual);
	}

	static public void assertWithMessage(String message, Object expected, Object actual)
	{
	    if (expected == null && actual == null) {
	        return;
	    }
		if (expected == null && actual != null) {
			if (message == null)
				throw new AssertionError("Expected 'null' but found value: " + actual);
			else
				throw new AssertionError(message + "; Expected 'null' but found value: " + actual);
		} 
		else if (!expected.equals(actual)) 
		{
			if (message == null)
				throw new AssertionError("Expected value: " + expected + ", but found value: " + actual);
			else
				throw new AssertionError(message + "; Expected value: " + expected + ", but found value: " + actual);
		}
	}
	
	static public void assertWithMessage(String message, boolean result)
	{
		if(!result)
		{
			if (message == null)
	            throw new AssertionError();
			else
				throw new AssertionError(message);
		}
	}
	
	static public void assertWithMessage(String message, int expected, int actual) {
     boolean result = (expected == actual);
		
		if(!result)
		{
			if (message == null)
				throw new AssertionError("Expected value: " + expected + ", but found value: " + actual);
			else
				throw new AssertionError(message + "; Expected value: " + expected + ", but found value: " + actual);
		}
    }
}
