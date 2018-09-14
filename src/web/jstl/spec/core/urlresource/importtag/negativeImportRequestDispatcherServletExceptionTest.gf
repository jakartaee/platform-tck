




<html>
<head><title>negativeImportRequestDispatcherServletExceptionTest</title></head>
<body>

    <!-- If RequestDispatcher.include() method throws a ServletException
             and a root cause exists, a JspException is thrown with the
             root cause message as the exception message and the original
             root cause as the JspException root cause.  If no root
             cause is present, the exception text will be included in the
             message and the original exception as the root cause. -->
    
        
    The expected Exception <strong>javax.servlet.jsp.JspException</strong> was thrown!<br>
The root cause Exception <strong>java.lang.IllegalStateException</strong> was of the expected type.<br>
The expected Exception text was found in the Exception message!<br>
    <br>
    
        
    The expected Exception <strong>javax.servlet.jsp.JspException</strong> was thrown!<br>
The root cause Exception <strong>javax.servlet.ServletException</strong> was of the expected type.<br>
The expected Exception text was found in the Exception message!<br>

</body>
</html>


