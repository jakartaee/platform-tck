<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html>
 <head>
  <title>Managed Bean Test Page</title>
 </head>
 <body>
   <f:view>
     <h1>
      <h:outputText value="#{farm.sheepInjected ? 'Injection works' : 'Injection is broken'}"/>
      <h:outputText value="#{farm.initializerCalled ? 'Initializer works' : 'Initializer is broken'}"/>
     </h1>
   </f:view>
 </body>
</html> 