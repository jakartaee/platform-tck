







<html>
<head><title>positiveResultIsLimitedByMaxRowsTest</title></head>
<body>




   <!-- Validate that a sql:query action and Result.isLimitedByMaxRows
            will return the correct value -->

   <h1>Validating sql:query action and  Result.isLimitedByMaxRows </h1>
   <p>






         The maxRows attribute was <strong>NOT</strong> specified.  The value
         returned from <strong>Result.isLimitedByMaxRows()</strong> was
         <strong>false</strong> as expected.
         <p>









         The maxRows attribute was set to <strong>
         -1</strong>. The value
         returned from <strong>Result.isLimitedByMaxRows()</strong> was
         <strong>false</strong> as expected.
         <p>







         The maxRows attribute was set to <strong>
         5</strong>. The value
         returned from <strong>Result.isLimitedByMaxRows()</strong> was
         <strong>true</strong> as expected.
         <p>











         The maxRows attribute was set to <strong>
         5</strong>. The value
         returned from <strong>Result.isLimitedByMaxRows()</strong> was
         <strong>true</strong> as expected.
         <p>





</body>
</html>

