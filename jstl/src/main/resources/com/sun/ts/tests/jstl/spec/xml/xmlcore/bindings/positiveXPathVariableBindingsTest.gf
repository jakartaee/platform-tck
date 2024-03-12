





<html>
<head><title>positiveXPathVariableBindingsTest</title></head>
<body>

    <!-- Validate the following bindings 
            foo - pageContext.findAttribute("foo")
            $param.foo - request.getParameter("foo")
            $header:foo - request.getHeader("foo")
            $initParam:foo - application.getInitParamter("foo")
            $cooke:foo - maps to the cookies value for name foo
            $pageScope:foo - pageContext.getAttribute("foo", PageContext.PAGE_SCOPE)
            $requestScope:foo - pageContext.getAttribute("foo", PageContext.REQUEST_SCOPE)
            $sessionScope:foo - pageContext.getAttribute("foo", PageContext.SESSION_SCOPE)
            $applicationScope:foo - pageContext.getAttribute("foo", PageContext.APPLICATION_SCOPE) -->
    
    
    
    
    
    
    
    

    findAttribute: foundit<br>
    Page scope: page<br>
    Request scope: req<br>
    Session scope: sess<br>
    Application scope: appl<br>
    Init param: initBinding<br>
    Request param: RequestParameter1<br>
    Cookie: CookieFound<br>

</body>
</html>

