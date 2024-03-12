





<html>
<head><title>positiveSetVarNullValueTest</title></head>
<body>

    
    
    


    <!-- Validate that if value is null, the variable
             referenced by var is removed from scope. If
             setting a property of a Bean or a Map, then
             Map entry is removed, and the Bean property
             is set to null.-->
    Var is set and value is null:<br>
    
    Attribute removed.  Test PASSED<br>

    Map property, key2 should be removed, thus the value whem accessed, would be null:<br>
    
    Map Key: 'key2' removed.  Test PASSED<br>


    JavaBean property, properly value should be set to null:<br>
    
    
    Bean property null.  Test PASSED<br>

</body>
</html>


