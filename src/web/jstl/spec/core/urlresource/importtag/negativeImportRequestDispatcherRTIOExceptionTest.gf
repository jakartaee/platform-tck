




<html>
<head><title>negativeImportRequestDispatcherRTIOExceptionTest</title></head>
<body>

    <!-- If RequestDispather.include() method throws an IOException
             or RuntimeException, a JspException must be thrown
             with the message of the original exception included in the
             message, and the original Exception as the root cuase. -->
    IOException:<br>
    
        
    The expected Exception <strong>javax.servlet.jsp.JspException</strong> was thrown!<br>
The root cause Exception <strong>java.io.IOException</strong> was of the expected type.<br>
The expected Exception text was found in the Exception message!<br>
    ServletException:<br>
    
        
    The expected Exception <strong>javax.servlet.jsp.JspException</strong> was thrown!<br>
The root cause Exception <strong>java.lang.RuntimeException</strong> was of the expected type.<br>
The expected Exception text was found in the Exception message!<br>

</body>
</html>


