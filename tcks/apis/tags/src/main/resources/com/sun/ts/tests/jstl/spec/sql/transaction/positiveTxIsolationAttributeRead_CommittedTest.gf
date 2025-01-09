







<html>
<head><title>positiveTxIsolationAttributeRead_CommittedTest</title></head>
<body>



   

   

   <!-- Validate sql:transaction action which specifies read_committed for the
          isolation attribute  -->

   <h1>Validate sql:transaction action which specifies read_committed for the
          isolation attribute </h1>
   <p>

   

      
   


    <strong>Dump LifeCycle for sql:transaction action setting isolation attribute</strong>
    <p>
    
       Connection.getTransactionIsolation()
       <br>
   
       Connection.setAutoCommit(false)
       <br>
   
       Connection.commit()
       <br>
   
       Connection.setAutoCommit(true)
       <br>
   
       Connection.close()
       <br>
   

   


</body>
</html>

