




<html>
<head><title>positiveCWOTest</title></head>
<body>


    <!-- Validate that the first nested when within a choose,
              whose select condition evaluates to true will process
              its body content.  This means no other body content
              from subsequent whens that could evaluated to true,
              would be evaluated. -->
    
        
            Body content properly processed.<br>
        
        
    
    
    <!-- Validate that the first (not the first physical action)
              nested when within a choose,
              whose select condition evaluates to true will process
              its body content.  This means no other body content
              from subsequent whens that could evaluated to true,
              or the body content of an otherwise action 
              would be evaluated. -->
    
        
        
            Body content properly proccessed.<br>
        
        
    

    <!-- Validate that if no when action evaluates to true and an
             othewise action is not present, nothing is written to the
             current JspWriter. -->
    
        
    

    <!-- Validate that if no when action evaluates to true and
             an otherwise action is present, the body of the otherwise
             action is written to the current JspWriter. -->
    
        
        
        
            Body content properly processed.<br>
        
    

</body>
</html>


