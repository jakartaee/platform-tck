<%--

    Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<html>
<title>positiveSetIndexedProp</title>
<body>
<%
/**
 *  TestCase name : positiveSetIndexedProp
 *  Description   : Here, the setProperty tag is used to set the value for
 *                  an indexed property.An array is declared and defined in a
 *                  scriptlet and then assigned through the setProperty tag, with 
 *                  an expression.
 *  Result        : Expected to set the value of the array.
 */
 %>

<%
//Declare primative arrays
byte[] bArray = { 1, 2, 3 };
char[] cArray = { '1', '2', '3' };
short[] sArray = { 1, 2, 3 };
int[] iArray = {24, 25, 26 }; 
float[] fArray = { 1.1f, 1.2f, 1.3f };
long[] lArray = { 1, 1, 1 };
double[] dArray = { 1.1d, 1.2d, 1.3d };
boolean[] boArray = { false, true, false };

//Declare Object arrays
Boolean[] booleanArray = { new Boolean( "true" ), new Boolean( "false" ), new Boolean( "true" ) };
Byte[] byteArray = { new Byte( Byte.MIN_VALUE ), new Byte( Byte.MAX_VALUE ) };
Character[] charArray = { new Character( '1' ), new Character( '2' ), new Character( '3' ) };
Short[] shortArray = { new Short( Short.MIN_VALUE ), new Short( Short.MAX_VALUE ) };
Integer[] integerArray = { new Integer( Integer.MIN_VALUE ), new Integer( Integer.MAX_VALUE ) };
Float[] floatArray = { new Float( 1.1f ), new Float( 1.2F ), new Float( 3.14f ) };
Long[] longArray = { new Long( Long.MIN_VALUE ), new Long( Long.MAX_VALUE ) };
Double[] doubleArray = { new Double( 1.1d ), new Double( 2.81d ), new Double( 3.14d ) };

%>

<!-- Declaring the bean without body -->
<jsp:useBean id="myBean" scope="request" class="com.sun.ts.tests.jsp.spec.core_syntax.actions.setproperty.SetpropBean" />
<jsp:setProperty name="myBean" property="BArray" value="<%= bArray %>" />
<jsp:setProperty name="myBean" property="CArray" value="<%= cArray %>" />
<jsp:setProperty name="myBean" property="SArray" value="<%= sArray %>" />
<jsp:setProperty name="myBean" property="IArray" value="<%= iArray %>" />
<jsp:setProperty name="myBean" property="FArray" value="<%= fArray %>" />
<jsp:setProperty name="myBean" property="LArray" value="<%= lArray %>" />
<jsp:setProperty name="myBean" property="DArray" value="<%= dArray %>" />
<jsp:setProperty name="myBean" property="boArray" value="<%= boArray %>" />

<jsp:setProperty name="myBean" property="byteArray" value="<%= byteArray %>" />
<jsp:setProperty name="myBean" property="charArray" value="<%= charArray %>" />
<jsp:setProperty name="myBean" property="shortArray" value="<%= shortArray %>" />
<jsp:setProperty name="myBean" property="integerArray" value="<%= integerArray %>" />
<jsp:setProperty name="myBean" property="floatArray" value="<%= floatArray %>" />
<jsp:setProperty name="myBean" property="longArray" value="<%= longArray %>" />
<jsp:setProperty name="myBean" property="doubleArray" value="<%= doubleArray %>" />
<jsp:setProperty name="myBean" property="booleanArray" value="<%= booleanArray %>" />
<!-- Accessing the property through a scriptlet -->

<%
// Accessing the set property.
byte[] bary = myBean.getBArray();
for(int j = 0; j < bary.length; j++ ) {
    if ( j == bary.length - 1 ) {
       out.println( bary[ j ] );
    } else {
       out.print( bary[ j ] + " " );
    }
}

char[] cary = myBean.getCArray();
for(int j = 0; j < cary.length; j++ ) {
    if ( j == cary.length - 1 ) {
       out.println( cary[ j ] );
    } else {
       out.print( cary[ j ] + " " );
    }
}

short[] sary = myBean.getSArray();
for(int j = 0; j < sary.length; j++ ) {
    if ( j == sary.length - 1 ) {
       out.println( sary[ j ] );
    } else {
       out.print( sary[ j ] + " " );
    }
}

int[] iary = myBean.getIArray();
for(int j = 0; j < iary.length; j++ ) {
    if ( j == iary.length - 1 ) {
       out.println( iary[ j ] );
    } else {
       out.print( iary[ j ] + " " );
    }
}

float[] fary = myBean.getFArray();
for(int j = 0; j < fary.length; j++ ) {
    if ( j == fary.length - 1 ) {
       out.println( fary[ j ] );
    } else {
       out.print( fary[ j ] + " " );
    }
}

long[] lary = myBean.getLArray();
for(int j = 0; j < lary.length; j++ ) {
    if ( j == lary.length - 1 ) {
       out.println( lary[ j ] );
    } else {
       out.print( lary[ j ] + " " );
    }
}

double[] dary = myBean.getDArray();
for(int j = 0; j < dary.length; j++ ) {
    if ( j == dary.length - 1 ) {
       out.println( dary[ j ] );
    } else {
       out.print( dary[ j ] + " " );
    }
}

boolean[] boary = myBean.getBoArray();
for(int j = 0; j < boary.length; j++ ) {
    if ( j == boary.length - 1 ) {
       out.println( boary[ j ] );
    } else {
       out.print( boary[ j ] + " " );
    }
}

Byte[] byteary = myBean.getByteArray();
for(int j = 0; j < byteary.length; j++ ) {
    if ( j == byteary.length - 1 ) {
       out.println( byteary[ j ] );
    } else {
       out.print( byteary[ j ] + " " );
    }
}

Character[] charary = myBean.getCharArray();
for(int j = 0; j < charary.length; j++ ) {
    if ( j == charary.length - 1 ) {
       out.println( charary[ j ] );
    } else {
       out.print( charary[ j ] + " " );
    }
}

Short[] shortary = myBean.getShortArray();
for(int j = 0; j < shortary.length; j++ ) {
    if ( j == shortary.length - 1 ) {
       out.println( shortary[ j ] );
    } else {
       out.print( shortary[ j ] + " " );
    }
}

Integer[] intary = myBean.getIntegerArray();
for(int j = 0; j < intary.length; j++ ) {
    if ( j == intary.length - 1 ) {
       out.println( intary[ j ] );
    } else {
       out.print( intary[ j ] + " " );
    }
}

Float[] floatary = myBean.getFloatArray();
for(int j = 0; j < floatary.length; j++ ) {
    if ( j == floatary.length - 1 ) {
       out.println( floatary[ j ] );
    } else {
       out.print( floatary[ j ] + " " );
    }
}

Long[] longary = myBean.getLongArray();
for(int j = 0; j < longary.length; j++ ) {
    if ( j == longary.length - 1 ) {
       out.println( longary[ j ] );
    } else {
       out.print( longary[ j ] + " " );
    }
}

Double[] doary = myBean.getDoubleArray();
for(int j = 0; j < doary.length; j++ ) {
    if ( j == doary.length - 1 ) {
       out.println( doary[ j ] );
    } else {
       out.print( doary[ j ] + " " );
    }
}

Boolean[] bolary = myBean.getBooleanArray();
for(int j = 0; j < bolary.length; j++ ) {
    if ( j == bolary.length - 1 ) {
       out.println( bolary[ j ] );
    } else {
       out.print( bolary[ j ] + " " );
    }
}

%>

</body>
</html>
