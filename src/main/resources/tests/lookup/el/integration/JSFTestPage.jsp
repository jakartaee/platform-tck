<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html>
 <head>
  <title>JSF Test Page</title>
 </head>
 <body>
   <f:view>
     <h1>
      <h:outputText value="#{sheep.name}"/>
     </h1>
   </f:view>
 </body>
</html> 