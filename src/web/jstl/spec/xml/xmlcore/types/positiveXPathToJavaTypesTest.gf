





<html>
<head><title>positiveXPathToJavaTypesTest</title></head>
<body>


    <!-- The variable defined by var will have different types
              dending on the XPath expression applied.
              The mapping is defined as follows:
              XPath      Java
              boolean    java.lang.Boolean
              number     java.lang.Number
              String     java.lang.String
              node-set   Implementation specified - will check java.lang.Object -->
    XPath expression: boolean(/Server/GlobalNamingResources/ResourceParams) - Result should be of type Boolean<br>
    
    Result: reBool is of type java.lang.Boolean<br><br>
    XPath expression: count(/Server/GlobalNamingResources/ResourceParams) - Result should be of type Number<br>
    
    Result: reNum is of type java.lang.Number<br><br>
    XPath expression: string($doc/*) - Result should be of type String<br>
    
    Result: reStr is of type java.lang.String<br><br>
    XPath expression: $doc//parameter - Result should be a Node-Set of type java.lang.Object<br>
    
    Result: reNode is of type java.lang.Object<br><br>

</body>
</html>

