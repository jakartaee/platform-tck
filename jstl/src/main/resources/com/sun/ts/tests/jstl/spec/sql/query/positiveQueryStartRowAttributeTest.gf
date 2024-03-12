







<html>
<head><title>positiveQueryStartRowAttributeTest</title></head>
<body>







   <!-- Validate sql:query action utilizing the
              the startRow attribute -->

   <h1>Validating sql:query action startRow attribute </h1>
   <p>







         The startRow attribute was <strong>NOT</strong> specified and
         a Result Object that contained
         <strong>10</strong> rows was
         returned as expected.
         <p>









         The startRow attribute was set to <strong>
         0</strong>
         and a Result Object that contained
         <strong>10</strong> rows was
         returned as expected.
         <p>











         The startRow attribute was set to <strong>
         1</strong>.
         The query  resulted in the first row having a value of
         <strong>"2"</strong> for the
         first column as expected.
         <p>













         The startRow attribute was set to <strong>
         1</strong>.
         The query resulted in the first row having a value of
         <strong>"2"</strong> for the
         first column as expected.
         <p>












         The startRow attribute was set to <strong>
         1</strong>.
         The maxRows attribute was set to <strong>
          "8"</strong>.
         The query resulted in the first row having a value of
         <strong>"2"</strong> for the
         first column as expected.
         <p>




</body>
</html>


