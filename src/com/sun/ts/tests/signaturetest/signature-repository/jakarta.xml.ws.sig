#Signature file v4.1
#Version 4.0

CLSS public abstract interface !annotation jakarta.xml.bind.annotation.XmlRootElement
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String name()
meth public abstract !hasdefault java.lang.String namespace()

CLSS public abstract interface !annotation jakarta.xml.bind.annotation.XmlSchema
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PACKAGE])
fld public final static java.lang.String NO_LOCATION = "##generate"
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.xml.bind.annotation.XmlNsForm attributeFormDefault()
meth public abstract !hasdefault jakarta.xml.bind.annotation.XmlNsForm elementFormDefault()
meth public abstract !hasdefault jakarta.xml.bind.annotation.XmlNs[] xmlns()
meth public abstract !hasdefault java.lang.String location()
meth public abstract !hasdefault java.lang.String namespace()

CLSS public abstract interface !annotation jakarta.xml.bind.annotation.XmlTransient
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[FIELD, METHOD, TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.xml.bind.annotation.XmlType
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
innr public final static DEFAULT
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<?> factoryClass()
meth public abstract !hasdefault java.lang.String factoryMethod()
meth public abstract !hasdefault java.lang.String name()
meth public abstract !hasdefault java.lang.String namespace()
meth public abstract !hasdefault java.lang.String[] propOrder()

CLSS public abstract interface !annotation jakarta.xml.ws.Action
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.xml.ws.FaultAction[] fault()
meth public abstract !hasdefault java.lang.String input()
meth public abstract !hasdefault java.lang.String output()

CLSS public abstract interface jakarta.xml.ws.AsyncHandler<%0 extends java.lang.Object>
 anno 0 java.lang.FunctionalInterface()
meth public abstract void handleResponse(jakarta.xml.ws.Response<{jakarta.xml.ws.AsyncHandler%0}>)

CLSS public abstract interface jakarta.xml.ws.Binding
meth public abstract java.lang.String getBindingID()
meth public abstract java.util.List<jakarta.xml.ws.handler.Handler> getHandlerChain()
meth public abstract void setHandlerChain(java.util.List<jakarta.xml.ws.handler.Handler>)

CLSS public abstract interface jakarta.xml.ws.BindingProvider
fld public final static java.lang.String ENDPOINT_ADDRESS_PROPERTY = "jakarta.xml.ws.service.endpoint.address"
fld public final static java.lang.String PASSWORD_PROPERTY = "jakarta.xml.ws.security.auth.password"
fld public final static java.lang.String SESSION_MAINTAIN_PROPERTY = "jakarta.xml.ws.session.maintain"
fld public final static java.lang.String SOAPACTION_URI_PROPERTY = "jakarta.xml.ws.soap.http.soapaction.uri"
fld public final static java.lang.String SOAPACTION_USE_PROPERTY = "jakarta.xml.ws.soap.http.soapaction.use"
fld public final static java.lang.String USERNAME_PROPERTY = "jakarta.xml.ws.security.auth.username"
meth public abstract <%0 extends jakarta.xml.ws.EndpointReference> {%%0} getEndpointReference(java.lang.Class<{%%0}>)
meth public abstract jakarta.xml.ws.Binding getBinding()
meth public abstract jakarta.xml.ws.EndpointReference getEndpointReference()
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getRequestContext()
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getResponseContext()

CLSS public abstract interface !annotation jakarta.xml.ws.BindingType
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String value()

CLSS public abstract interface jakarta.xml.ws.Dispatch<%0 extends java.lang.Object>
intf jakarta.xml.ws.BindingProvider
meth public abstract jakarta.xml.ws.Response<{jakarta.xml.ws.Dispatch%0}> invokeAsync({jakarta.xml.ws.Dispatch%0})
meth public abstract java.util.concurrent.Future<?> invokeAsync({jakarta.xml.ws.Dispatch%0},jakarta.xml.ws.AsyncHandler<{jakarta.xml.ws.Dispatch%0}>)
meth public abstract void invokeOneWay({jakarta.xml.ws.Dispatch%0})
meth public abstract {jakarta.xml.ws.Dispatch%0} invoke({jakarta.xml.ws.Dispatch%0})

CLSS public abstract jakarta.xml.ws.Endpoint
cons public init()
fld public final static java.lang.String WSDL_PORT = "jakarta.xml.ws.wsdl.port"
fld public final static java.lang.String WSDL_SERVICE = "jakarta.xml.ws.wsdl.service"
meth public !varargs static jakarta.xml.ws.Endpoint create(java.lang.Object,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs static jakarta.xml.ws.Endpoint create(java.lang.String,java.lang.Object,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs static jakarta.xml.ws.Endpoint publish(java.lang.String,java.lang.Object,jakarta.xml.ws.WebServiceFeature[])
meth public abstract !varargs <%0 extends jakarta.xml.ws.EndpointReference> {%%0} getEndpointReference(java.lang.Class<{%%0}>,org.w3c.dom.Element[])
meth public abstract !varargs jakarta.xml.ws.EndpointReference getEndpointReference(org.w3c.dom.Element[])
meth public abstract boolean isPublished()
meth public abstract jakarta.xml.ws.Binding getBinding()
meth public abstract java.lang.Object getImplementor()
meth public abstract java.util.List<javax.xml.transform.Source> getMetadata()
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getProperties()
meth public abstract java.util.concurrent.Executor getExecutor()
meth public abstract void publish(java.lang.Object)
meth public abstract void publish(java.lang.String)
meth public abstract void setExecutor(java.util.concurrent.Executor)
meth public abstract void setMetadata(java.util.List<javax.xml.transform.Source>)
meth public abstract void setProperties(java.util.Map<java.lang.String,java.lang.Object>)
meth public abstract void stop()
meth public static jakarta.xml.ws.Endpoint create(java.lang.Object)
meth public static jakarta.xml.ws.Endpoint create(java.lang.String,java.lang.Object)
meth public static jakarta.xml.ws.Endpoint publish(java.lang.String,java.lang.Object)
meth public void publish(jakarta.xml.ws.spi.http.HttpContext)
meth public void setEndpointContext(jakarta.xml.ws.EndpointContext)
supr java.lang.Object

CLSS public abstract jakarta.xml.ws.EndpointContext
cons public init()
meth public abstract java.util.Set<jakarta.xml.ws.Endpoint> getEndpoints()
supr java.lang.Object

CLSS public abstract jakarta.xml.ws.EndpointReference
cons protected init()
meth public !varargs <%0 extends java.lang.Object> {%%0} getPort(java.lang.Class<{%%0}>,jakarta.xml.ws.WebServiceFeature[])
meth public abstract void writeTo(javax.xml.transform.Result)
meth public java.lang.String toString()
meth public static jakarta.xml.ws.EndpointReference readFrom(javax.xml.transform.Source)
supr java.lang.Object

CLSS public abstract interface !annotation jakarta.xml.ws.FaultAction
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String value()
meth public abstract java.lang.Class<? extends java.lang.Exception> className()

CLSS public final jakarta.xml.ws.Holder<%0 extends java.lang.Object>
cons public init()
cons public init({jakarta.xml.ws.Holder%0})
fld public {jakarta.xml.ws.Holder%0} value
intf java.io.Serializable
supr java.lang.Object
hfds serialVersionUID

CLSS public abstract interface jakarta.xml.ws.LogicalMessage
meth public abstract java.lang.Object getPayload(jakarta.xml.bind.JAXBContext)
meth public abstract javax.xml.transform.Source getPayload()
meth public abstract void setPayload(java.lang.Object,jakarta.xml.bind.JAXBContext)
meth public abstract void setPayload(javax.xml.transform.Source)

CLSS public jakarta.xml.ws.ProtocolException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.xml.ws.WebServiceException
hfds serialVersionUID

CLSS public abstract interface jakarta.xml.ws.Provider<%0 extends java.lang.Object>
meth public abstract {jakarta.xml.ws.Provider%0} invoke({jakarta.xml.ws.Provider%0})

CLSS public abstract interface !annotation jakarta.xml.ws.RequestWrapper
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String className()
meth public abstract !hasdefault java.lang.String localName()
meth public abstract !hasdefault java.lang.String partName()
meth public abstract !hasdefault java.lang.String targetNamespace()

CLSS public abstract interface !annotation jakarta.xml.ws.RespectBinding
 anno 0 jakarta.xml.ws.spi.WebServiceFeatureAnnotation(java.lang.Class<? extends jakarta.xml.ws.WebServiceFeature> bean=class jakarta.xml.ws.RespectBindingFeature, java.lang.String id="jakarta.xml.ws.RespectBindingFeature")
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean enabled()

CLSS public final jakarta.xml.ws.RespectBindingFeature
cons public init()
cons public init(boolean)
fld public final static java.lang.String ID = "jakarta.xml.ws.RespectBindingFeature"
meth public java.lang.String getID()
supr jakarta.xml.ws.WebServiceFeature

CLSS public abstract interface jakarta.xml.ws.Response<%0 extends java.lang.Object>
intf java.util.concurrent.Future<{jakarta.xml.ws.Response%0}>
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getContext()

CLSS public abstract interface !annotation jakarta.xml.ws.ResponseWrapper
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String className()
meth public abstract !hasdefault java.lang.String localName()
meth public abstract !hasdefault java.lang.String partName()
meth public abstract !hasdefault java.lang.String targetNamespace()

CLSS public jakarta.xml.ws.Service
cons protected !varargs init(java.net.URL,javax.xml.namespace.QName,jakarta.xml.ws.WebServiceFeature[])
cons protected init(java.net.URL,javax.xml.namespace.QName)
innr public final static !enum Mode
meth public !varargs <%0 extends java.lang.Object> jakarta.xml.ws.Dispatch<{%%0}> createDispatch(jakarta.xml.ws.EndpointReference,java.lang.Class<{%%0}>,jakarta.xml.ws.Service$Mode,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs <%0 extends java.lang.Object> jakarta.xml.ws.Dispatch<{%%0}> createDispatch(javax.xml.namespace.QName,java.lang.Class<{%%0}>,jakarta.xml.ws.Service$Mode,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs <%0 extends java.lang.Object> {%%0} getPort(jakarta.xml.ws.EndpointReference,java.lang.Class<{%%0}>,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs <%0 extends java.lang.Object> {%%0} getPort(java.lang.Class<{%%0}>,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs <%0 extends java.lang.Object> {%%0} getPort(javax.xml.namespace.QName,java.lang.Class<{%%0}>,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs jakarta.xml.ws.Dispatch<java.lang.Object> createDispatch(jakarta.xml.ws.EndpointReference,jakarta.xml.bind.JAXBContext,jakarta.xml.ws.Service$Mode,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs jakarta.xml.ws.Dispatch<java.lang.Object> createDispatch(javax.xml.namespace.QName,jakarta.xml.bind.JAXBContext,jakarta.xml.ws.Service$Mode,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs static jakarta.xml.ws.Service create(java.net.URL,javax.xml.namespace.QName,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs static jakarta.xml.ws.Service create(javax.xml.namespace.QName,jakarta.xml.ws.WebServiceFeature[])
meth public <%0 extends java.lang.Object> jakarta.xml.ws.Dispatch<{%%0}> createDispatch(javax.xml.namespace.QName,java.lang.Class<{%%0}>,jakarta.xml.ws.Service$Mode)
meth public <%0 extends java.lang.Object> {%%0} getPort(java.lang.Class<{%%0}>)
meth public <%0 extends java.lang.Object> {%%0} getPort(javax.xml.namespace.QName,java.lang.Class<{%%0}>)
meth public jakarta.xml.ws.Dispatch<java.lang.Object> createDispatch(javax.xml.namespace.QName,jakarta.xml.bind.JAXBContext,jakarta.xml.ws.Service$Mode)
meth public jakarta.xml.ws.handler.HandlerResolver getHandlerResolver()
meth public java.net.URL getWSDLDocumentLocation()
meth public java.util.Iterator<javax.xml.namespace.QName> getPorts()
meth public java.util.concurrent.Executor getExecutor()
meth public javax.xml.namespace.QName getServiceName()
meth public static jakarta.xml.ws.Service create(java.net.URL,javax.xml.namespace.QName)
meth public static jakarta.xml.ws.Service create(javax.xml.namespace.QName)
meth public void addPort(javax.xml.namespace.QName,java.lang.String,java.lang.String)
meth public void setExecutor(java.util.concurrent.Executor)
meth public void setHandlerResolver(jakarta.xml.ws.handler.HandlerResolver)
supr java.lang.Object
hfds delegate

CLSS public final static !enum jakarta.xml.ws.Service$Mode
 outer jakarta.xml.ws.Service
fld public final static jakarta.xml.ws.Service$Mode MESSAGE
fld public final static jakarta.xml.ws.Service$Mode PAYLOAD
meth public static jakarta.xml.ws.Service$Mode valueOf(java.lang.String)
meth public static jakarta.xml.ws.Service$Mode[] values()
supr java.lang.Enum<jakarta.xml.ws.Service$Mode>

CLSS public abstract interface !annotation jakarta.xml.ws.ServiceMode
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.xml.ws.Service$Mode value()

CLSS public abstract interface !annotation jakarta.xml.ws.WebEndpoint
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String name()

CLSS public abstract interface !annotation jakarta.xml.ws.WebFault
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String faultBean()
meth public abstract !hasdefault java.lang.String messageName()
meth public abstract !hasdefault java.lang.String name()
meth public abstract !hasdefault java.lang.String targetNamespace()

CLSS public abstract interface !annotation jakarta.xml.ws.WebServiceClient
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String name()
meth public abstract !hasdefault java.lang.String targetNamespace()
meth public abstract !hasdefault java.lang.String wsdlLocation()

CLSS public abstract interface jakarta.xml.ws.WebServiceContext
meth public abstract !varargs <%0 extends jakarta.xml.ws.EndpointReference> {%%0} getEndpointReference(java.lang.Class<{%%0}>,org.w3c.dom.Element[])
meth public abstract !varargs jakarta.xml.ws.EndpointReference getEndpointReference(org.w3c.dom.Element[])
meth public abstract boolean isUserInRole(java.lang.String)
meth public abstract jakarta.xml.ws.handler.MessageContext getMessageContext()
meth public abstract java.security.Principal getUserPrincipal()

CLSS public jakarta.xml.ws.WebServiceException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public abstract jakarta.xml.ws.WebServiceFeature
cons protected init()
fld protected boolean enabled
meth public abstract java.lang.String getID()
meth public boolean isEnabled()
supr java.lang.Object

CLSS public final jakarta.xml.ws.WebServicePermission
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
supr java.security.BasicPermission
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.xml.ws.WebServiceProvider
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String portName()
meth public abstract !hasdefault java.lang.String serviceName()
meth public abstract !hasdefault java.lang.String targetNamespace()
meth public abstract !hasdefault java.lang.String wsdlLocation()

CLSS public abstract interface !annotation jakarta.xml.ws.WebServiceRef
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.xml.ws.WebServiceRefs)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.xml.ws.Service> value()
meth public abstract !hasdefault java.lang.Class<?> type()
meth public abstract !hasdefault java.lang.String lookup()
meth public abstract !hasdefault java.lang.String mappedName()
meth public abstract !hasdefault java.lang.String name()
meth public abstract !hasdefault java.lang.String wsdlLocation()

CLSS public abstract interface !annotation jakarta.xml.ws.WebServiceRefs
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.xml.ws.WebServiceRef[] value()

CLSS public abstract interface jakarta.xml.ws.handler.Handler<%0 extends jakarta.xml.ws.handler.MessageContext>
meth public abstract boolean handleFault({jakarta.xml.ws.handler.Handler%0})
meth public abstract boolean handleMessage({jakarta.xml.ws.handler.Handler%0})
meth public abstract void close(jakarta.xml.ws.handler.MessageContext)

CLSS public abstract interface jakarta.xml.ws.handler.HandlerResolver
meth public abstract java.util.List<jakarta.xml.ws.handler.Handler> getHandlerChain(jakarta.xml.ws.handler.PortInfo)

CLSS public abstract interface jakarta.xml.ws.handler.LogicalHandler<%0 extends jakarta.xml.ws.handler.LogicalMessageContext>
intf jakarta.xml.ws.handler.Handler<{jakarta.xml.ws.handler.LogicalHandler%0}>

CLSS public abstract interface jakarta.xml.ws.handler.LogicalMessageContext
intf jakarta.xml.ws.handler.MessageContext
meth public abstract jakarta.xml.ws.LogicalMessage getMessage()

CLSS public abstract interface jakarta.xml.ws.handler.MessageContext
fld public final static java.lang.String HTTP_REQUEST_HEADERS = "jakarta.xml.ws.http.request.headers"
fld public final static java.lang.String HTTP_REQUEST_METHOD = "jakarta.xml.ws.http.request.method"
fld public final static java.lang.String HTTP_RESPONSE_CODE = "jakarta.xml.ws.http.response.code"
fld public final static java.lang.String HTTP_RESPONSE_HEADERS = "jakarta.xml.ws.http.response.headers"
fld public final static java.lang.String INBOUND_MESSAGE_ATTACHMENTS = "jakarta.xml.ws.binding.attachments.inbound"
fld public final static java.lang.String MESSAGE_OUTBOUND_PROPERTY = "jakarta.xml.ws.handler.message.outbound"
fld public final static java.lang.String OUTBOUND_MESSAGE_ATTACHMENTS = "jakarta.xml.ws.binding.attachments.outbound"
fld public final static java.lang.String PATH_INFO = "jakarta.xml.ws.http.request.pathinfo"
fld public final static java.lang.String QUERY_STRING = "jakarta.xml.ws.http.request.querystring"
fld public final static java.lang.String REFERENCE_PARAMETERS = "jakarta.xml.ws.reference.parameters"
fld public final static java.lang.String SERVLET_CONTEXT = "jakarta.xml.ws.servlet.context"
fld public final static java.lang.String SERVLET_REQUEST = "jakarta.xml.ws.servlet.request"
fld public final static java.lang.String SERVLET_RESPONSE = "jakarta.xml.ws.servlet.response"
fld public final static java.lang.String WSDL_DESCRIPTION = "jakarta.xml.ws.wsdl.description"
fld public final static java.lang.String WSDL_INTERFACE = "jakarta.xml.ws.wsdl.interface"
fld public final static java.lang.String WSDL_OPERATION = "jakarta.xml.ws.wsdl.operation"
fld public final static java.lang.String WSDL_PORT = "jakarta.xml.ws.wsdl.port"
fld public final static java.lang.String WSDL_SERVICE = "jakarta.xml.ws.wsdl.service"
innr public final static !enum Scope
intf java.util.Map<java.lang.String,java.lang.Object>
meth public abstract jakarta.xml.ws.handler.MessageContext$Scope getScope(java.lang.String)
meth public abstract void setScope(java.lang.String,jakarta.xml.ws.handler.MessageContext$Scope)

CLSS public final static !enum jakarta.xml.ws.handler.MessageContext$Scope
 outer jakarta.xml.ws.handler.MessageContext
fld public final static jakarta.xml.ws.handler.MessageContext$Scope APPLICATION
fld public final static jakarta.xml.ws.handler.MessageContext$Scope HANDLER
meth public static jakarta.xml.ws.handler.MessageContext$Scope valueOf(java.lang.String)
meth public static jakarta.xml.ws.handler.MessageContext$Scope[] values()
supr java.lang.Enum<jakarta.xml.ws.handler.MessageContext$Scope>

CLSS public abstract interface jakarta.xml.ws.handler.PortInfo
meth public abstract java.lang.String getBindingID()
meth public abstract javax.xml.namespace.QName getPortName()
meth public abstract javax.xml.namespace.QName getServiceName()

CLSS abstract interface jakarta.xml.ws.handler.package-info

CLSS public abstract interface jakarta.xml.ws.handler.soap.SOAPHandler<%0 extends jakarta.xml.ws.handler.soap.SOAPMessageContext>
intf jakarta.xml.ws.handler.Handler<{jakarta.xml.ws.handler.soap.SOAPHandler%0}>
meth public abstract java.util.Set<javax.xml.namespace.QName> getHeaders()

CLSS public abstract interface jakarta.xml.ws.handler.soap.SOAPMessageContext
intf jakarta.xml.ws.handler.MessageContext
meth public abstract jakarta.xml.soap.SOAPMessage getMessage()
meth public abstract java.lang.Object[] getHeaders(javax.xml.namespace.QName,jakarta.xml.bind.JAXBContext,boolean)
meth public abstract java.util.Set<java.lang.String> getRoles()
meth public abstract void setMessage(jakarta.xml.soap.SOAPMessage)

CLSS abstract interface jakarta.xml.ws.handler.soap.package-info

CLSS public abstract interface jakarta.xml.ws.http.HTTPBinding
fld public final static java.lang.String HTTP_BINDING = "http://www.w3.org/2004/08/wsdl/http"
intf jakarta.xml.ws.Binding

CLSS public jakarta.xml.ws.http.HTTPException
cons public init(int)
meth public int getStatusCode()
supr jakarta.xml.ws.ProtocolException
hfds serialVersionUID,statusCode

CLSS abstract interface jakarta.xml.ws.http.package-info

CLSS abstract interface jakarta.xml.ws.package-info

CLSS public abstract interface !annotation jakarta.xml.ws.soap.Addressing
 anno 0 jakarta.xml.ws.spi.WebServiceFeatureAnnotation(java.lang.Class<? extends jakarta.xml.ws.WebServiceFeature> bean=class jakarta.xml.ws.soap.AddressingFeature, java.lang.String id="http://www.w3.org/2005/08/addressing/module")
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean enabled()
meth public abstract !hasdefault boolean required()
meth public abstract !hasdefault jakarta.xml.ws.soap.AddressingFeature$Responses responses()

CLSS public final jakarta.xml.ws.soap.AddressingFeature
cons public init()
cons public init(boolean)
cons public init(boolean,boolean)
cons public init(boolean,boolean,jakarta.xml.ws.soap.AddressingFeature$Responses)
fld public final static java.lang.String ID = "http://www.w3.org/2005/08/addressing/module"
innr public final static !enum Responses
meth public boolean isRequired()
meth public jakarta.xml.ws.soap.AddressingFeature$Responses getResponses()
meth public java.lang.String getID()
supr jakarta.xml.ws.WebServiceFeature
hfds required,responses

CLSS public final static !enum jakarta.xml.ws.soap.AddressingFeature$Responses
 outer jakarta.xml.ws.soap.AddressingFeature
fld public final static jakarta.xml.ws.soap.AddressingFeature$Responses ALL
fld public final static jakarta.xml.ws.soap.AddressingFeature$Responses ANONYMOUS
fld public final static jakarta.xml.ws.soap.AddressingFeature$Responses NON_ANONYMOUS
meth public static jakarta.xml.ws.soap.AddressingFeature$Responses valueOf(java.lang.String)
meth public static jakarta.xml.ws.soap.AddressingFeature$Responses[] values()
supr java.lang.Enum<jakarta.xml.ws.soap.AddressingFeature$Responses>

CLSS public abstract interface !annotation jakarta.xml.ws.soap.MTOM
 anno 0 jakarta.xml.ws.spi.WebServiceFeatureAnnotation(java.lang.Class<? extends jakarta.xml.ws.WebServiceFeature> bean=class jakarta.xml.ws.soap.MTOMFeature, java.lang.String id="http://www.w3.org/2004/08/soap/features/http-optimization")
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean enabled()
meth public abstract !hasdefault int threshold()

CLSS public final jakarta.xml.ws.soap.MTOMFeature
cons public init()
cons public init(boolean)
cons public init(boolean,int)
cons public init(int)
fld public final static java.lang.String ID = "http://www.w3.org/2004/08/soap/features/http-optimization"
meth public int getThreshold()
meth public java.lang.String getID()
supr jakarta.xml.ws.WebServiceFeature
hfds threshold

CLSS public abstract interface jakarta.xml.ws.soap.SOAPBinding
fld public final static java.lang.String SOAP11HTTP_BINDING = "http://schemas.xmlsoap.org/wsdl/soap/http"
fld public final static java.lang.String SOAP11HTTP_MTOM_BINDING = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true"
fld public final static java.lang.String SOAP12HTTP_BINDING = "http://www.w3.org/2003/05/soap/bindings/HTTP/"
fld public final static java.lang.String SOAP12HTTP_MTOM_BINDING = "http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true"
intf jakarta.xml.ws.Binding
meth public abstract boolean isMTOMEnabled()
meth public abstract jakarta.xml.soap.MessageFactory getMessageFactory()
meth public abstract jakarta.xml.soap.SOAPFactory getSOAPFactory()
meth public abstract java.util.Set<java.lang.String> getRoles()
meth public abstract void setMTOMEnabled(boolean)
meth public abstract void setRoles(java.util.Set<java.lang.String>)

CLSS public jakarta.xml.ws.soap.SOAPFaultException
cons public init(jakarta.xml.soap.SOAPFault)
meth public jakarta.xml.soap.SOAPFault getFault()
supr jakarta.xml.ws.ProtocolException
hfds fault,serialVersionUID

CLSS abstract interface jakarta.xml.ws.soap.package-info

CLSS public abstract jakarta.xml.ws.spi.Invoker
cons public init()
meth public abstract !varargs java.lang.Object invoke(java.lang.reflect.Method,java.lang.Object[]) throws java.lang.IllegalAccessException,java.lang.reflect.InvocationTargetException
meth public abstract void inject(jakarta.xml.ws.WebServiceContext) throws java.lang.IllegalAccessException,java.lang.reflect.InvocationTargetException
supr java.lang.Object

CLSS public abstract jakarta.xml.ws.spi.Provider
cons protected init()
meth public !varargs jakarta.xml.ws.Endpoint createAndPublishEndpoint(java.lang.String,java.lang.Object,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs jakarta.xml.ws.Endpoint createEndpoint(java.lang.String,java.lang.Class<?>,jakarta.xml.ws.spi.Invoker,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs jakarta.xml.ws.Endpoint createEndpoint(java.lang.String,java.lang.Object,jakarta.xml.ws.WebServiceFeature[])
meth public !varargs jakarta.xml.ws.spi.ServiceDelegate createServiceDelegate(java.net.URL,javax.xml.namespace.QName,java.lang.Class<? extends jakarta.xml.ws.Service>,jakarta.xml.ws.WebServiceFeature[])
meth public abstract !varargs <%0 extends java.lang.Object> {%%0} getPort(jakarta.xml.ws.EndpointReference,java.lang.Class<{%%0}>,jakarta.xml.ws.WebServiceFeature[])
meth public abstract jakarta.xml.ws.Endpoint createAndPublishEndpoint(java.lang.String,java.lang.Object)
meth public abstract jakarta.xml.ws.Endpoint createEndpoint(java.lang.String,java.lang.Object)
meth public abstract jakarta.xml.ws.EndpointReference readEndpointReference(javax.xml.transform.Source)
meth public abstract jakarta.xml.ws.spi.ServiceDelegate createServiceDelegate(java.net.URL,javax.xml.namespace.QName,java.lang.Class<? extends jakarta.xml.ws.Service>)
meth public abstract jakarta.xml.ws.wsaddressing.W3CEndpointReference createW3CEndpointReference(java.lang.String,javax.xml.namespace.QName,javax.xml.namespace.QName,java.util.List<org.w3c.dom.Element>,java.lang.String,java.util.List<org.w3c.dom.Element>)
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReference createW3CEndpointReference(java.lang.String,javax.xml.namespace.QName,javax.xml.namespace.QName,javax.xml.namespace.QName,java.util.List<org.w3c.dom.Element>,java.lang.String,java.util.List<org.w3c.dom.Element>,java.util.List<org.w3c.dom.Element>,java.util.Map<javax.xml.namespace.QName,java.lang.String>)
meth public static jakarta.xml.ws.spi.Provider provider()
supr java.lang.Object
hfds DEFAULT_JAXWSPROVIDER

CLSS public abstract jakarta.xml.ws.spi.ServiceDelegate
cons protected init()
meth public abstract !varargs <%0 extends java.lang.Object> jakarta.xml.ws.Dispatch<{%%0}> createDispatch(jakarta.xml.ws.EndpointReference,java.lang.Class<{%%0}>,jakarta.xml.ws.Service$Mode,jakarta.xml.ws.WebServiceFeature[])
meth public abstract !varargs <%0 extends java.lang.Object> jakarta.xml.ws.Dispatch<{%%0}> createDispatch(javax.xml.namespace.QName,java.lang.Class<{%%0}>,jakarta.xml.ws.Service$Mode,jakarta.xml.ws.WebServiceFeature[])
meth public abstract !varargs <%0 extends java.lang.Object> {%%0} getPort(jakarta.xml.ws.EndpointReference,java.lang.Class<{%%0}>,jakarta.xml.ws.WebServiceFeature[])
meth public abstract !varargs <%0 extends java.lang.Object> {%%0} getPort(java.lang.Class<{%%0}>,jakarta.xml.ws.WebServiceFeature[])
meth public abstract !varargs <%0 extends java.lang.Object> {%%0} getPort(javax.xml.namespace.QName,java.lang.Class<{%%0}>,jakarta.xml.ws.WebServiceFeature[])
meth public abstract !varargs jakarta.xml.ws.Dispatch<java.lang.Object> createDispatch(jakarta.xml.ws.EndpointReference,jakarta.xml.bind.JAXBContext,jakarta.xml.ws.Service$Mode,jakarta.xml.ws.WebServiceFeature[])
meth public abstract !varargs jakarta.xml.ws.Dispatch<java.lang.Object> createDispatch(javax.xml.namespace.QName,jakarta.xml.bind.JAXBContext,jakarta.xml.ws.Service$Mode,jakarta.xml.ws.WebServiceFeature[])
meth public abstract <%0 extends java.lang.Object> jakarta.xml.ws.Dispatch<{%%0}> createDispatch(javax.xml.namespace.QName,java.lang.Class<{%%0}>,jakarta.xml.ws.Service$Mode)
meth public abstract <%0 extends java.lang.Object> {%%0} getPort(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> {%%0} getPort(javax.xml.namespace.QName,java.lang.Class<{%%0}>)
meth public abstract jakarta.xml.ws.Dispatch<java.lang.Object> createDispatch(javax.xml.namespace.QName,jakarta.xml.bind.JAXBContext,jakarta.xml.ws.Service$Mode)
meth public abstract jakarta.xml.ws.handler.HandlerResolver getHandlerResolver()
meth public abstract java.net.URL getWSDLDocumentLocation()
meth public abstract java.util.Iterator<javax.xml.namespace.QName> getPorts()
meth public abstract java.util.concurrent.Executor getExecutor()
meth public abstract javax.xml.namespace.QName getServiceName()
meth public abstract void addPort(javax.xml.namespace.QName,java.lang.String,java.lang.String)
meth public abstract void setExecutor(java.util.concurrent.Executor)
meth public abstract void setHandlerResolver(jakarta.xml.ws.handler.HandlerResolver)
supr java.lang.Object

CLSS public abstract interface !annotation jakarta.xml.ws.spi.WebServiceFeatureAnnotation
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends jakarta.xml.ws.WebServiceFeature> bean()
meth public abstract java.lang.String id()

CLSS public abstract jakarta.xml.ws.spi.http.HttpContext
cons public init()
fld protected jakarta.xml.ws.spi.http.HttpHandler handler
meth public abstract java.lang.Object getAttribute(java.lang.String)
meth public abstract java.lang.String getPath()
meth public abstract java.util.Set<java.lang.String> getAttributeNames()
meth public void setHandler(jakarta.xml.ws.spi.http.HttpHandler)
supr java.lang.Object

CLSS public abstract jakarta.xml.ws.spi.http.HttpExchange
cons public init()
fld public final static java.lang.String REQUEST_CIPHER_SUITE = "jakarta.xml.ws.spi.http.request.cipher.suite"
fld public final static java.lang.String REQUEST_KEY_SIZE = "jakarta.xml.ws.spi.http.request.key.size"
fld public final static java.lang.String REQUEST_X509CERTIFICATE = "jakarta.xml.ws.spi.http.request.cert.X509Certificate"
meth public abstract boolean isUserInRole(java.lang.String)
meth public abstract jakarta.xml.ws.spi.http.HttpContext getHttpContext()
meth public abstract java.io.InputStream getRequestBody() throws java.io.IOException
meth public abstract java.io.OutputStream getResponseBody() throws java.io.IOException
meth public abstract java.lang.Object getAttribute(java.lang.String)
meth public abstract java.lang.String getContextPath()
meth public abstract java.lang.String getPathInfo()
meth public abstract java.lang.String getProtocol()
meth public abstract java.lang.String getQueryString()
meth public abstract java.lang.String getRequestHeader(java.lang.String)
meth public abstract java.lang.String getRequestMethod()
meth public abstract java.lang.String getRequestURI()
meth public abstract java.lang.String getScheme()
meth public abstract java.net.InetSocketAddress getLocalAddress()
meth public abstract java.net.InetSocketAddress getRemoteAddress()
meth public abstract java.security.Principal getUserPrincipal()
meth public abstract java.util.Map<java.lang.String,java.util.List<java.lang.String>> getRequestHeaders()
meth public abstract java.util.Map<java.lang.String,java.util.List<java.lang.String>> getResponseHeaders()
meth public abstract java.util.Set<java.lang.String> getAttributeNames()
meth public abstract void addResponseHeader(java.lang.String,java.lang.String)
meth public abstract void close() throws java.io.IOException
meth public abstract void setStatus(int)
supr java.lang.Object

CLSS public abstract jakarta.xml.ws.spi.http.HttpHandler
cons public init()
meth public abstract void handle(jakarta.xml.ws.spi.http.HttpExchange) throws java.io.IOException
supr java.lang.Object

CLSS abstract interface jakarta.xml.ws.spi.http.package-info

CLSS abstract interface jakarta.xml.ws.spi.package-info

CLSS public final jakarta.xml.ws.wsaddressing.W3CEndpointReference
cons public init(javax.xml.transform.Source)
meth public void writeTo(javax.xml.transform.Result)
supr jakarta.xml.ws.EndpointReference
hfds NS,address,attributes,elements,metadata,referenceParameters,w3cjc
hcls Address,Elements

CLSS public final jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder
cons public init()
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReference build()
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder address(java.lang.String)
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder attribute(javax.xml.namespace.QName,java.lang.String)
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder element(org.w3c.dom.Element)
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder endpointName(javax.xml.namespace.QName)
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder interfaceName(javax.xml.namespace.QName)
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder metadata(org.w3c.dom.Element)
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder referenceParameter(org.w3c.dom.Element)
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder serviceName(javax.xml.namespace.QName)
meth public jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder wsdlDocumentLocation(java.lang.String)
supr java.lang.Object
hfds address,attributes,elements,endpointName,interfaceName,metadata,referenceParameters,serviceName,wsdlDocumentLocation

CLSS abstract interface jakarta.xml.ws.wsaddressing.package-info

CLSS public abstract interface java.io.Serializable

CLSS public abstract interface java.lang.Comparable<%0 extends java.lang.Object>
meth public abstract int compareTo({java.lang.Comparable%0})

CLSS public abstract java.lang.Enum<%0 extends java.lang.Enum<{java.lang.Enum%0}>>
cons protected init(java.lang.String,int)
intf java.io.Serializable
intf java.lang.Comparable<{java.lang.Enum%0}>
meth protected final java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected final void finalize()
meth public final boolean equals(java.lang.Object)
meth public final int compareTo({java.lang.Enum%0})
meth public final int hashCode()
meth public final int ordinal()
meth public final java.lang.Class<{java.lang.Enum%0}> getDeclaringClass()
meth public final java.lang.String name()
meth public java.lang.String toString()
meth public static <%0 extends java.lang.Enum<{%%0}>> {%%0} valueOf(java.lang.Class<{%%0}>,java.lang.String)
supr java.lang.Object

CLSS public java.lang.Exception
cons protected init(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Throwable

CLSS public abstract interface !annotation java.lang.FunctionalInterface
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation

CLSS public java.lang.Object
cons public init()
meth protected java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected void finalize() throws java.lang.Throwable
 anno 0 java.lang.Deprecated(boolean forRemoval=false, java.lang.String since="9")
meth public boolean equals(java.lang.Object)
meth public final java.lang.Class<?> getClass()
meth public final void notify()
meth public final void notifyAll()
meth public final void wait() throws java.lang.InterruptedException
meth public final void wait(long) throws java.lang.InterruptedException
meth public final void wait(long,int) throws java.lang.InterruptedException
meth public int hashCode()
meth public java.lang.String toString()

CLSS public java.lang.RuntimeException
cons protected init(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Exception

CLSS public java.lang.Throwable
cons protected init(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
intf java.io.Serializable
meth public final java.lang.Throwable[] getSuppressed()
meth public final void addSuppressed(java.lang.Throwable)
meth public java.lang.StackTraceElement[] getStackTrace()
meth public java.lang.String getLocalizedMessage()
meth public java.lang.String getMessage()
meth public java.lang.String toString()
meth public java.lang.Throwable fillInStackTrace()
meth public java.lang.Throwable getCause()
meth public java.lang.Throwable initCause(java.lang.Throwable)
meth public void printStackTrace()
meth public void printStackTrace(java.io.PrintStream)
meth public void printStackTrace(java.io.PrintWriter)
meth public void setStackTrace(java.lang.StackTraceElement[])
supr java.lang.Object

CLSS public abstract interface java.lang.annotation.Annotation
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> annotationType()
meth public abstract java.lang.String toString()

CLSS public abstract interface !annotation java.lang.annotation.Documented
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation java.lang.annotation.Inherited
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation java.lang.annotation.Repeatable
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> value()

CLSS public abstract interface !annotation java.lang.annotation.Retention
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.RetentionPolicy value()

CLSS public abstract interface !annotation java.lang.annotation.Target
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.ElementType[] value()

CLSS public abstract java.security.BasicPermission
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
intf java.io.Serializable
meth public boolean equals(java.lang.Object)
meth public boolean implies(java.security.Permission)
meth public int hashCode()
meth public java.lang.String getActions()
meth public java.security.PermissionCollection newPermissionCollection()
supr java.security.Permission

CLSS public abstract interface java.security.Guard
meth public abstract void checkGuard(java.lang.Object)

CLSS public abstract java.security.Permission
cons public init(java.lang.String)
intf java.io.Serializable
intf java.security.Guard
meth public abstract boolean equals(java.lang.Object)
meth public abstract boolean implies(java.security.Permission)
meth public abstract int hashCode()
meth public abstract java.lang.String getActions()
meth public final java.lang.String getName()
meth public java.lang.String toString()
meth public java.security.PermissionCollection newPermissionCollection()
meth public void checkGuard(java.lang.Object)
supr java.lang.Object

CLSS public abstract interface java.util.Map<%0 extends java.lang.Object, %1 extends java.lang.Object>
innr public abstract interface static Entry
meth public !varargs static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> ofEntries(java.util.Map$Entry<? extends {%%0},? extends {%%1}>[])
 anno 0 java.lang.SafeVarargs()
meth public abstract boolean containsKey(java.lang.Object)
meth public abstract boolean containsValue(java.lang.Object)
meth public abstract boolean equals(java.lang.Object)
meth public abstract boolean isEmpty()
meth public abstract int hashCode()
meth public abstract int size()
meth public abstract java.util.Collection<{java.util.Map%1}> values()
meth public abstract java.util.Set<java.util.Map$Entry<{java.util.Map%0},{java.util.Map%1}>> entrySet()
meth public abstract java.util.Set<{java.util.Map%0}> keySet()
meth public abstract void clear()
meth public abstract void putAll(java.util.Map<? extends {java.util.Map%0},? extends {java.util.Map%1}>)
meth public abstract {java.util.Map%1} get(java.lang.Object)
meth public abstract {java.util.Map%1} put({java.util.Map%0},{java.util.Map%1})
meth public abstract {java.util.Map%1} remove(java.lang.Object)
meth public boolean remove(java.lang.Object,java.lang.Object)
meth public boolean replace({java.util.Map%0},{java.util.Map%1},{java.util.Map%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map$Entry<{%%0},{%%1}> entry({%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> copyOf(java.util.Map<? extends {%%0},? extends {%%1}>)
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of()
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1},{%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1},{%%0},{%%1},{%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1})
meth public static <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.Map<{%%0},{%%1}> of({%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1},{%%0},{%%1})
meth public void forEach(java.util.function.BiConsumer<? super {java.util.Map%0},? super {java.util.Map%1}>)
meth public void replaceAll(java.util.function.BiFunction<? super {java.util.Map%0},? super {java.util.Map%1},? extends {java.util.Map%1}>)
meth public {java.util.Map%1} compute({java.util.Map%0},java.util.function.BiFunction<? super {java.util.Map%0},? super {java.util.Map%1},? extends {java.util.Map%1}>)
meth public {java.util.Map%1} computeIfAbsent({java.util.Map%0},java.util.function.Function<? super {java.util.Map%0},? extends {java.util.Map%1}>)
meth public {java.util.Map%1} computeIfPresent({java.util.Map%0},java.util.function.BiFunction<? super {java.util.Map%0},? super {java.util.Map%1},? extends {java.util.Map%1}>)
meth public {java.util.Map%1} getOrDefault(java.lang.Object,{java.util.Map%1})
meth public {java.util.Map%1} merge({java.util.Map%0},{java.util.Map%1},java.util.function.BiFunction<? super {java.util.Map%1},? super {java.util.Map%1},? extends {java.util.Map%1}>)
meth public {java.util.Map%1} putIfAbsent({java.util.Map%0},{java.util.Map%1})
meth public {java.util.Map%1} replace({java.util.Map%0},{java.util.Map%1})

CLSS public abstract interface java.util.concurrent.Future<%0 extends java.lang.Object>
meth public abstract boolean cancel(boolean)
meth public abstract boolean isCancelled()
meth public abstract boolean isDone()
meth public abstract {java.util.concurrent.Future%0} get() throws java.lang.InterruptedException,java.util.concurrent.ExecutionException
meth public abstract {java.util.concurrent.Future%0} get(long,java.util.concurrent.TimeUnit) throws java.lang.InterruptedException,java.util.concurrent.ExecutionException,java.util.concurrent.TimeoutException

