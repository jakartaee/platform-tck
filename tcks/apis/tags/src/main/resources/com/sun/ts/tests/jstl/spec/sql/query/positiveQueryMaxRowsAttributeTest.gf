







<html>
<head><title>positiveQueryMaxRowsAttributeTest</title></head>
<body>




   <!-- Validate that a sql:query action will return
              the number of rows specified by the maxRows attribute -->

   <h1>Validating sql:query action MaxRows attribute </h1>
   <p>






         The maxRows attribute was <strong>NOT</strong> specified and
         a Result object that contained
         <strong>10</strong> rows was
         returned as expected.
         <p>









         The maxRows attribute was set to <strong>
         -1</strong>
         and a Result object that contained
         <strong>10</strong> rows was
         returned as expected.
         <p>








         The maxRows attribute was set to <strong>
         5</strong>
         and a Result object that contained this row count
          was returned as expected.
         <p>











         The maxRows attribute was set to <strong>
         5</strong>
         and a Result object that contained this row count
          was returned as expected.
         <p>




</body>
</html>


