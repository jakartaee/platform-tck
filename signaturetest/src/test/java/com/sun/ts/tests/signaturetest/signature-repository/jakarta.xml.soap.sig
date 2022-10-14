#Signature file v4.1
#Version 3.0

CLSS public abstract jakarta.xml.soap.AttachmentPart
cons protected init()
meth public abstract byte[] getRawContentBytes() throws jakarta.xml.soap.SOAPException
meth public abstract int getSize() throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.activation.DataHandler getDataHandler() throws jakarta.xml.soap.SOAPException
meth public abstract java.io.InputStream getBase64Content() throws jakarta.xml.soap.SOAPException
meth public abstract java.io.InputStream getRawContent() throws jakarta.xml.soap.SOAPException
meth public abstract java.lang.Object getContent() throws jakarta.xml.soap.SOAPException
meth public abstract java.lang.String[] getMimeHeader(java.lang.String)
meth public abstract java.util.Iterator<jakarta.xml.soap.MimeHeader> getAllMimeHeaders()
meth public abstract java.util.Iterator<jakarta.xml.soap.MimeHeader> getMatchingMimeHeaders(java.lang.String[])
meth public abstract java.util.Iterator<jakarta.xml.soap.MimeHeader> getNonMatchingMimeHeaders(java.lang.String[])
meth public abstract void addMimeHeader(java.lang.String,java.lang.String)
meth public abstract void clearContent()
meth public abstract void removeAllMimeHeaders()
meth public abstract void removeMimeHeader(java.lang.String)
meth public abstract void setBase64Content(java.io.InputStream,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract void setContent(java.lang.Object,java.lang.String)
meth public abstract void setDataHandler(jakarta.activation.DataHandler)
meth public abstract void setMimeHeader(java.lang.String,java.lang.String)
meth public abstract void setRawContent(java.io.InputStream,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract void setRawContentBytes(byte[],int,int,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public java.lang.String getContentId()
meth public java.lang.String getContentLocation()
meth public java.lang.String getContentType()
meth public void setContentId(java.lang.String)
meth public void setContentLocation(java.lang.String)
meth public void setContentType(java.lang.String)
supr java.lang.Object

CLSS public abstract interface jakarta.xml.soap.Detail
intf jakarta.xml.soap.SOAPFaultElement
meth public abstract jakarta.xml.soap.DetailEntry addDetailEntry(jakarta.xml.soap.Name) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.DetailEntry addDetailEntry(javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public abstract java.util.Iterator<jakarta.xml.soap.DetailEntry> getDetailEntries()

CLSS public abstract interface jakarta.xml.soap.DetailEntry
intf jakarta.xml.soap.SOAPElement

CLSS public abstract jakarta.xml.soap.MessageFactory
cons protected init()
meth public abstract jakarta.xml.soap.SOAPMessage createMessage() throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPMessage createMessage(jakarta.xml.soap.MimeHeaders,java.io.InputStream) throws jakarta.xml.soap.SOAPException,java.io.IOException
meth public static jakarta.xml.soap.MessageFactory newInstance() throws jakarta.xml.soap.SOAPException
meth public static jakarta.xml.soap.MessageFactory newInstance(java.lang.String) throws jakarta.xml.soap.SOAPException
supr java.lang.Object
hfds DEFAULT_MESSAGE_FACTORY

CLSS public jakarta.xml.soap.MimeHeader
cons public init(java.lang.String,java.lang.String)
meth public java.lang.String getName()
meth public java.lang.String getValue()
supr java.lang.Object
hfds name,value

CLSS public jakarta.xml.soap.MimeHeaders
cons public init()
meth public java.lang.String[] getHeader(java.lang.String)
meth public java.util.Iterator<jakarta.xml.soap.MimeHeader> getAllHeaders()
meth public java.util.Iterator<jakarta.xml.soap.MimeHeader> getMatchingHeaders(java.lang.String[])
meth public java.util.Iterator<jakarta.xml.soap.MimeHeader> getNonMatchingHeaders(java.lang.String[])
meth public void addHeader(java.lang.String,java.lang.String)
meth public void removeAllHeaders()
meth public void removeHeader(java.lang.String)
meth public void setHeader(java.lang.String,java.lang.String)
supr java.lang.Object
hfds headers
hcls MatchingIterator

CLSS public abstract interface jakarta.xml.soap.Name
meth public abstract java.lang.String getLocalName()
meth public abstract java.lang.String getPrefix()
meth public abstract java.lang.String getQualifiedName()
meth public abstract java.lang.String getURI()

CLSS public abstract interface jakarta.xml.soap.Node
intf org.w3c.dom.Node
meth public abstract jakarta.xml.soap.SOAPElement getParentElement()
meth public abstract java.lang.String getValue()
meth public abstract void detachNode()
meth public abstract void recycleNode()
meth public abstract void setParentElement(jakarta.xml.soap.SOAPElement) throws jakarta.xml.soap.SOAPException
meth public abstract void setValue(java.lang.String)

CLSS public abstract jakarta.xml.soap.SAAJMetaFactory
cons protected init()
meth protected abstract jakarta.xml.soap.MessageFactory newMessageFactory(java.lang.String) throws jakarta.xml.soap.SOAPException
meth protected abstract jakarta.xml.soap.SOAPFactory newSOAPFactory(java.lang.String) throws jakarta.xml.soap.SOAPException
supr java.lang.Object
hfds DEFAULT_META_FACTORY_CLASS

CLSS public jakarta.xml.soap.SAAJResult
cons public init() throws jakarta.xml.soap.SOAPException
cons public init(jakarta.xml.soap.SOAPElement)
cons public init(jakarta.xml.soap.SOAPMessage)
cons public init(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public jakarta.xml.soap.Node getResult()
supr javax.xml.transform.dom.DOMResult

CLSS public abstract interface jakarta.xml.soap.SOAPBody
intf jakarta.xml.soap.SOAPElement
meth public abstract boolean hasFault()
meth public abstract jakarta.xml.soap.SOAPBodyElement addBodyElement(jakarta.xml.soap.Name) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPBodyElement addBodyElement(javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPBodyElement addDocument(org.w3c.dom.Document) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPFault addFault() throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPFault addFault(jakarta.xml.soap.Name,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPFault addFault(jakarta.xml.soap.Name,java.lang.String,java.util.Locale) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPFault addFault(javax.xml.namespace.QName,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPFault addFault(javax.xml.namespace.QName,java.lang.String,java.util.Locale) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPFault getFault()
meth public abstract org.w3c.dom.Document extractContentAsDocument() throws jakarta.xml.soap.SOAPException

CLSS public abstract interface jakarta.xml.soap.SOAPBodyElement
intf jakarta.xml.soap.SOAPElement

CLSS public abstract jakarta.xml.soap.SOAPConnection
cons protected init()
intf java.lang.AutoCloseable
meth public abstract jakarta.xml.soap.SOAPMessage call(jakarta.xml.soap.SOAPMessage,java.lang.Object) throws jakarta.xml.soap.SOAPException
meth public abstract void close() throws jakarta.xml.soap.SOAPException
meth public int getConnectTimeout()
meth public int getReadTimeout()
meth public jakarta.xml.soap.SOAPMessage get(java.lang.Object) throws jakarta.xml.soap.SOAPException
meth public void setConnectTimeout(int)
meth public void setReadTimeout(int)
supr java.lang.Object
hfds connectTimeout,readTimeout

CLSS public abstract jakarta.xml.soap.SOAPConnectionFactory
cons protected init()
meth public abstract jakarta.xml.soap.SOAPConnection createConnection() throws jakarta.xml.soap.SOAPException
meth public static jakarta.xml.soap.SOAPConnectionFactory newInstance() throws jakarta.xml.soap.SOAPException
supr java.lang.Object
hfds DEFAULT_SOAP_CONNECTION_FACTORY

CLSS public abstract interface jakarta.xml.soap.SOAPConstants
fld public final static java.lang.String DEFAULT_SOAP_PROTOCOL = "SOAP 1.1 Protocol"
fld public final static java.lang.String DYNAMIC_SOAP_PROTOCOL = "Dynamic Protocol"
fld public final static java.lang.String SOAP_1_1_CONTENT_TYPE = "text/xml"
fld public final static java.lang.String SOAP_1_1_PROTOCOL = "SOAP 1.1 Protocol"
fld public final static java.lang.String SOAP_1_2_CONTENT_TYPE = "application/soap+xml"
fld public final static java.lang.String SOAP_1_2_PROTOCOL = "SOAP 1.2 Protocol"
fld public final static java.lang.String SOAP_ENV_PREFIX = "env"
fld public final static java.lang.String URI_NS_SOAP_1_1_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/"
fld public final static java.lang.String URI_NS_SOAP_1_2_ENCODING = "http://www.w3.org/2003/05/soap-encoding"
fld public final static java.lang.String URI_NS_SOAP_1_2_ENVELOPE = "http://www.w3.org/2003/05/soap-envelope"
fld public final static java.lang.String URI_NS_SOAP_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/"
fld public final static java.lang.String URI_NS_SOAP_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/"
fld public final static java.lang.String URI_SOAP_1_2_ROLE_NEXT = "http://www.w3.org/2003/05/soap-envelope/role/next"
fld public final static java.lang.String URI_SOAP_1_2_ROLE_NONE = "http://www.w3.org/2003/05/soap-envelope/role/none"
fld public final static java.lang.String URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER = "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver"
fld public final static java.lang.String URI_SOAP_ACTOR_NEXT = "http://schemas.xmlsoap.org/soap/actor/next"
fld public final static javax.xml.namespace.QName SOAP_DATAENCODINGUNKNOWN_FAULT
fld public final static javax.xml.namespace.QName SOAP_MUSTUNDERSTAND_FAULT
fld public final static javax.xml.namespace.QName SOAP_RECEIVER_FAULT
fld public final static javax.xml.namespace.QName SOAP_SENDER_FAULT
fld public final static javax.xml.namespace.QName SOAP_VERSIONMISMATCH_FAULT

CLSS public abstract interface jakarta.xml.soap.SOAPElement
intf jakarta.xml.soap.Node
intf org.w3c.dom.Element
meth public abstract boolean removeAttribute(jakarta.xml.soap.Name)
meth public abstract boolean removeAttribute(javax.xml.namespace.QName)
meth public abstract boolean removeNamespaceDeclaration(java.lang.String)
meth public abstract jakarta.xml.soap.Name getElementName()
meth public abstract jakarta.xml.soap.SOAPElement addAttribute(jakarta.xml.soap.Name,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement addAttribute(javax.xml.namespace.QName,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement addChildElement(jakarta.xml.soap.Name) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement addChildElement(jakarta.xml.soap.SOAPElement) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement addChildElement(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement addChildElement(java.lang.String,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement addChildElement(java.lang.String,java.lang.String,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement addChildElement(javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement addNamespaceDeclaration(java.lang.String,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement addTextNode(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement setElementQName(javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public abstract java.lang.String getAttributeValue(jakarta.xml.soap.Name)
meth public abstract java.lang.String getAttributeValue(javax.xml.namespace.QName)
meth public abstract java.lang.String getEncodingStyle()
meth public abstract java.lang.String getNamespaceURI(java.lang.String)
meth public abstract java.util.Iterator<jakarta.xml.soap.Name> getAllAttributes()
meth public abstract java.util.Iterator<jakarta.xml.soap.Node> getChildElements()
meth public abstract java.util.Iterator<jakarta.xml.soap.Node> getChildElements(jakarta.xml.soap.Name)
meth public abstract java.util.Iterator<jakarta.xml.soap.Node> getChildElements(javax.xml.namespace.QName)
meth public abstract java.util.Iterator<java.lang.String> getNamespacePrefixes()
meth public abstract java.util.Iterator<java.lang.String> getVisibleNamespacePrefixes()
meth public abstract java.util.Iterator<javax.xml.namespace.QName> getAllAttributesAsQNames()
meth public abstract javax.xml.namespace.QName createQName(java.lang.String,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract javax.xml.namespace.QName getElementQName()
meth public abstract void removeContents()
meth public abstract void setEncodingStyle(java.lang.String) throws jakarta.xml.soap.SOAPException

CLSS public abstract interface jakarta.xml.soap.SOAPEnvelope
intf jakarta.xml.soap.SOAPElement
meth public abstract jakarta.xml.soap.Name createName(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.Name createName(java.lang.String,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.Name createName(java.lang.String,java.lang.String,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPBody addBody() throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPBody getBody() throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPHeader addHeader() throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPHeader getHeader() throws jakarta.xml.soap.SOAPException

CLSS public jakarta.xml.soap.SOAPException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
meth public java.lang.String getMessage()
meth public java.lang.Throwable getCause()
meth public java.lang.Throwable initCause(java.lang.Throwable)
supr java.lang.Exception
hfds cause,serialVersionUID

CLSS public abstract jakarta.xml.soap.SOAPFactory
cons protected init()
meth public abstract jakarta.xml.soap.Detail createDetail() throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.Name createName(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.Name createName(java.lang.String,java.lang.String,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement createElement(jakarta.xml.soap.Name) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement createElement(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPElement createElement(java.lang.String,java.lang.String,java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPFault createFault() throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPFault createFault(java.lang.String,javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public jakarta.xml.soap.SOAPElement createElement(javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public jakarta.xml.soap.SOAPElement createElement(org.w3c.dom.Element) throws jakarta.xml.soap.SOAPException
meth public static jakarta.xml.soap.SOAPFactory newInstance() throws jakarta.xml.soap.SOAPException
meth public static jakarta.xml.soap.SOAPFactory newInstance(java.lang.String) throws jakarta.xml.soap.SOAPException
supr java.lang.Object
hfds DEFAULT_SOAP_FACTORY

CLSS public abstract interface jakarta.xml.soap.SOAPFault
intf jakarta.xml.soap.SOAPBodyElement
meth public abstract boolean hasDetail()
meth public abstract jakarta.xml.soap.Detail addDetail() throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.Detail getDetail()
meth public abstract jakarta.xml.soap.Name getFaultCodeAsName()
meth public abstract java.lang.String getFaultActor()
meth public abstract java.lang.String getFaultCode()
meth public abstract java.lang.String getFaultNode()
meth public abstract java.lang.String getFaultReasonText(java.util.Locale) throws jakarta.xml.soap.SOAPException
meth public abstract java.lang.String getFaultRole()
meth public abstract java.lang.String getFaultString()
meth public abstract java.util.Iterator<java.lang.String> getFaultReasonTexts() throws jakarta.xml.soap.SOAPException
meth public abstract java.util.Iterator<java.util.Locale> getFaultReasonLocales() throws jakarta.xml.soap.SOAPException
meth public abstract java.util.Iterator<javax.xml.namespace.QName> getFaultSubcodes()
meth public abstract java.util.Locale getFaultStringLocale()
meth public abstract javax.xml.namespace.QName getFaultCodeAsQName()
meth public abstract void addFaultReasonText(java.lang.String,java.util.Locale) throws jakarta.xml.soap.SOAPException
meth public abstract void appendFaultSubcode(javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public abstract void removeAllFaultSubcodes()
meth public abstract void setFaultActor(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract void setFaultCode(jakarta.xml.soap.Name) throws jakarta.xml.soap.SOAPException
meth public abstract void setFaultCode(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract void setFaultCode(javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public abstract void setFaultNode(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract void setFaultRole(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract void setFaultString(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract void setFaultString(java.lang.String,java.util.Locale) throws jakarta.xml.soap.SOAPException

CLSS public abstract interface jakarta.xml.soap.SOAPFaultElement
intf jakarta.xml.soap.SOAPElement

CLSS public abstract interface jakarta.xml.soap.SOAPHeader
intf jakarta.xml.soap.SOAPElement
meth public abstract jakarta.xml.soap.SOAPHeaderElement addHeaderElement(jakarta.xml.soap.Name) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPHeaderElement addHeaderElement(javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPHeaderElement addNotUnderstoodHeaderElement(javax.xml.namespace.QName) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPHeaderElement addUpgradeHeaderElement(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPHeaderElement addUpgradeHeaderElement(java.lang.String[]) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.SOAPHeaderElement addUpgradeHeaderElement(java.util.Iterator<java.lang.String>) throws jakarta.xml.soap.SOAPException
meth public abstract java.util.Iterator<jakarta.xml.soap.SOAPHeaderElement> examineAllHeaderElements()
meth public abstract java.util.Iterator<jakarta.xml.soap.SOAPHeaderElement> examineHeaderElements(java.lang.String)
meth public abstract java.util.Iterator<jakarta.xml.soap.SOAPHeaderElement> examineMustUnderstandHeaderElements(java.lang.String)
meth public abstract java.util.Iterator<jakarta.xml.soap.SOAPHeaderElement> extractAllHeaderElements()
meth public abstract java.util.Iterator<jakarta.xml.soap.SOAPHeaderElement> extractHeaderElements(java.lang.String)

CLSS public abstract interface jakarta.xml.soap.SOAPHeaderElement
intf jakarta.xml.soap.SOAPElement
meth public abstract boolean getMustUnderstand()
meth public abstract boolean getRelay()
meth public abstract java.lang.String getActor()
meth public abstract java.lang.String getRole()
meth public abstract void setActor(java.lang.String)
meth public abstract void setMustUnderstand(boolean)
meth public abstract void setRelay(boolean) throws jakarta.xml.soap.SOAPException
meth public abstract void setRole(java.lang.String) throws jakarta.xml.soap.SOAPException

CLSS public abstract jakarta.xml.soap.SOAPMessage
cons protected init()
fld public final static java.lang.String CHARACTER_SET_ENCODING = "jakarta.xml.soap.character-set-encoding"
fld public final static java.lang.String WRITE_XML_DECLARATION = "jakarta.xml.soap.write-xml-declaration"
meth public abstract boolean saveRequired()
meth public abstract int countAttachments()
meth public abstract jakarta.xml.soap.AttachmentPart createAttachmentPart()
meth public abstract jakarta.xml.soap.AttachmentPart getAttachment(jakarta.xml.soap.SOAPElement) throws jakarta.xml.soap.SOAPException
meth public abstract jakarta.xml.soap.MimeHeaders getMimeHeaders()
meth public abstract jakarta.xml.soap.SOAPPart getSOAPPart()
meth public abstract java.lang.String getContentDescription()
meth public abstract java.util.Iterator<jakarta.xml.soap.AttachmentPart> getAttachments()
meth public abstract java.util.Iterator<jakarta.xml.soap.AttachmentPart> getAttachments(jakarta.xml.soap.MimeHeaders)
meth public abstract void addAttachmentPart(jakarta.xml.soap.AttachmentPart)
meth public abstract void removeAllAttachments()
meth public abstract void removeAttachments(jakarta.xml.soap.MimeHeaders)
meth public abstract void saveChanges() throws jakarta.xml.soap.SOAPException
meth public abstract void setContentDescription(java.lang.String)
meth public abstract void writeTo(java.io.OutputStream) throws jakarta.xml.soap.SOAPException,java.io.IOException
meth public jakarta.xml.soap.AttachmentPart createAttachmentPart(jakarta.activation.DataHandler)
meth public jakarta.xml.soap.AttachmentPart createAttachmentPart(java.lang.Object,java.lang.String)
meth public jakarta.xml.soap.SOAPBody getSOAPBody() throws jakarta.xml.soap.SOAPException
meth public jakarta.xml.soap.SOAPHeader getSOAPHeader() throws jakarta.xml.soap.SOAPException
meth public java.lang.Object getProperty(java.lang.String) throws jakarta.xml.soap.SOAPException
meth public void setProperty(java.lang.String,java.lang.Object) throws jakarta.xml.soap.SOAPException
supr java.lang.Object

CLSS public abstract jakarta.xml.soap.SOAPPart
cons protected init()
intf jakarta.xml.soap.Node
intf org.w3c.dom.Document
meth public abstract jakarta.xml.soap.SOAPEnvelope getEnvelope() throws jakarta.xml.soap.SOAPException
meth public abstract java.lang.String[] getMimeHeader(java.lang.String)
meth public abstract java.util.Iterator<jakarta.xml.soap.MimeHeader> getAllMimeHeaders()
meth public abstract java.util.Iterator<jakarta.xml.soap.MimeHeader> getMatchingMimeHeaders(java.lang.String[])
meth public abstract java.util.Iterator<jakarta.xml.soap.MimeHeader> getNonMatchingMimeHeaders(java.lang.String[])
meth public abstract javax.xml.transform.Source getContent() throws jakarta.xml.soap.SOAPException
meth public abstract void addMimeHeader(java.lang.String,java.lang.String)
meth public abstract void removeAllMimeHeaders()
meth public abstract void removeMimeHeader(java.lang.String)
meth public abstract void setContent(javax.xml.transform.Source) throws jakarta.xml.soap.SOAPException
meth public abstract void setMimeHeader(java.lang.String,java.lang.String)
meth public java.lang.String getContentId()
meth public java.lang.String getContentLocation()
meth public void setContentId(java.lang.String)
meth public void setContentLocation(java.lang.String)
supr java.lang.Object

CLSS public abstract interface jakarta.xml.soap.Text
intf jakarta.xml.soap.Node
intf org.w3c.dom.Text
meth public abstract boolean isComment()

CLSS abstract interface jakarta.xml.soap.package-info

CLSS public abstract interface java.io.Serializable

CLSS public abstract interface java.lang.AutoCloseable
meth public abstract void close() throws java.lang.Exception

CLSS public java.lang.Exception
cons protected init(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Throwable

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

CLSS public abstract interface javax.xml.transform.Result
fld public final static java.lang.String PI_DISABLE_OUTPUT_ESCAPING = "javax.xml.transform.disable-output-escaping"
fld public final static java.lang.String PI_ENABLE_OUTPUT_ESCAPING = "javax.xml.transform.enable-output-escaping"
meth public abstract java.lang.String getSystemId()
meth public abstract void setSystemId(java.lang.String)

CLSS public javax.xml.transform.dom.DOMResult
cons public init()
cons public init(org.w3c.dom.Node)
cons public init(org.w3c.dom.Node,java.lang.String)
cons public init(org.w3c.dom.Node,org.w3c.dom.Node)
cons public init(org.w3c.dom.Node,org.w3c.dom.Node,java.lang.String)
fld public final static java.lang.String FEATURE = "http://javax.xml.transform.dom.DOMResult/feature"
intf javax.xml.transform.Result
meth public java.lang.String getSystemId()
meth public org.w3c.dom.Node getNextSibling()
meth public org.w3c.dom.Node getNode()
meth public void setNextSibling(org.w3c.dom.Node)
meth public void setNode(org.w3c.dom.Node)
meth public void setSystemId(java.lang.String)
supr java.lang.Object

CLSS public abstract interface org.w3c.dom.CharacterData
intf org.w3c.dom.Node
meth public abstract int getLength()
meth public abstract java.lang.String getData()
meth public abstract java.lang.String substringData(int,int)
meth public abstract void appendData(java.lang.String)
meth public abstract void deleteData(int,int)
meth public abstract void insertData(int,java.lang.String)
meth public abstract void replaceData(int,int,java.lang.String)
meth public abstract void setData(java.lang.String)

CLSS public abstract interface org.w3c.dom.Document
intf org.w3c.dom.Node
meth public abstract boolean getStrictErrorChecking()
meth public abstract boolean getXmlStandalone()
meth public abstract java.lang.String getDocumentURI()
meth public abstract java.lang.String getInputEncoding()
meth public abstract java.lang.String getXmlEncoding()
meth public abstract java.lang.String getXmlVersion()
meth public abstract org.w3c.dom.Attr createAttribute(java.lang.String)
meth public abstract org.w3c.dom.Attr createAttributeNS(java.lang.String,java.lang.String)
meth public abstract org.w3c.dom.CDATASection createCDATASection(java.lang.String)
meth public abstract org.w3c.dom.Comment createComment(java.lang.String)
meth public abstract org.w3c.dom.DOMConfiguration getDomConfig()
meth public abstract org.w3c.dom.DOMImplementation getImplementation()
meth public abstract org.w3c.dom.DocumentFragment createDocumentFragment()
meth public abstract org.w3c.dom.DocumentType getDoctype()
meth public abstract org.w3c.dom.Element createElement(java.lang.String)
meth public abstract org.w3c.dom.Element createElementNS(java.lang.String,java.lang.String)
meth public abstract org.w3c.dom.Element getDocumentElement()
meth public abstract org.w3c.dom.Element getElementById(java.lang.String)
meth public abstract org.w3c.dom.EntityReference createEntityReference(java.lang.String)
meth public abstract org.w3c.dom.Node adoptNode(org.w3c.dom.Node)
meth public abstract org.w3c.dom.Node importNode(org.w3c.dom.Node,boolean)
meth public abstract org.w3c.dom.Node renameNode(org.w3c.dom.Node,java.lang.String,java.lang.String)
meth public abstract org.w3c.dom.NodeList getElementsByTagName(java.lang.String)
meth public abstract org.w3c.dom.NodeList getElementsByTagNameNS(java.lang.String,java.lang.String)
meth public abstract org.w3c.dom.ProcessingInstruction createProcessingInstruction(java.lang.String,java.lang.String)
meth public abstract org.w3c.dom.Text createTextNode(java.lang.String)
meth public abstract void normalizeDocument()
meth public abstract void setDocumentURI(java.lang.String)
meth public abstract void setStrictErrorChecking(boolean)
meth public abstract void setXmlStandalone(boolean)
meth public abstract void setXmlVersion(java.lang.String)

CLSS public abstract interface org.w3c.dom.Element
intf org.w3c.dom.Node
meth public abstract boolean hasAttribute(java.lang.String)
meth public abstract boolean hasAttributeNS(java.lang.String,java.lang.String)
meth public abstract java.lang.String getAttribute(java.lang.String)
meth public abstract java.lang.String getAttributeNS(java.lang.String,java.lang.String)
meth public abstract java.lang.String getTagName()
meth public abstract org.w3c.dom.Attr getAttributeNode(java.lang.String)
meth public abstract org.w3c.dom.Attr getAttributeNodeNS(java.lang.String,java.lang.String)
meth public abstract org.w3c.dom.Attr removeAttributeNode(org.w3c.dom.Attr)
meth public abstract org.w3c.dom.Attr setAttributeNode(org.w3c.dom.Attr)
meth public abstract org.w3c.dom.Attr setAttributeNodeNS(org.w3c.dom.Attr)
meth public abstract org.w3c.dom.NodeList getElementsByTagName(java.lang.String)
meth public abstract org.w3c.dom.NodeList getElementsByTagNameNS(java.lang.String,java.lang.String)
meth public abstract org.w3c.dom.TypeInfo getSchemaTypeInfo()
meth public abstract void removeAttribute(java.lang.String)
meth public abstract void removeAttributeNS(java.lang.String,java.lang.String)
meth public abstract void setAttribute(java.lang.String,java.lang.String)
meth public abstract void setAttributeNS(java.lang.String,java.lang.String,java.lang.String)
meth public abstract void setIdAttribute(java.lang.String,boolean)
meth public abstract void setIdAttributeNS(java.lang.String,java.lang.String,boolean)
meth public abstract void setIdAttributeNode(org.w3c.dom.Attr,boolean)

CLSS public abstract interface org.w3c.dom.Node
fld public final static short ATTRIBUTE_NODE = 2
fld public final static short CDATA_SECTION_NODE = 4
fld public final static short COMMENT_NODE = 8
fld public final static short DOCUMENT_FRAGMENT_NODE = 11
fld public final static short DOCUMENT_NODE = 9
fld public final static short DOCUMENT_POSITION_CONTAINED_BY = 16
fld public final static short DOCUMENT_POSITION_CONTAINS = 8
fld public final static short DOCUMENT_POSITION_DISCONNECTED = 1
fld public final static short DOCUMENT_POSITION_FOLLOWING = 4
fld public final static short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32
fld public final static short DOCUMENT_POSITION_PRECEDING = 2
fld public final static short DOCUMENT_TYPE_NODE = 10
fld public final static short ELEMENT_NODE = 1
fld public final static short ENTITY_NODE = 6
fld public final static short ENTITY_REFERENCE_NODE = 5
fld public final static short NOTATION_NODE = 12
fld public final static short PROCESSING_INSTRUCTION_NODE = 7
fld public final static short TEXT_NODE = 3
meth public abstract boolean hasAttributes()
meth public abstract boolean hasChildNodes()
meth public abstract boolean isDefaultNamespace(java.lang.String)
meth public abstract boolean isEqualNode(org.w3c.dom.Node)
meth public abstract boolean isSameNode(org.w3c.dom.Node)
meth public abstract boolean isSupported(java.lang.String,java.lang.String)
meth public abstract java.lang.Object getFeature(java.lang.String,java.lang.String)
meth public abstract java.lang.Object getUserData(java.lang.String)
meth public abstract java.lang.Object setUserData(java.lang.String,java.lang.Object,org.w3c.dom.UserDataHandler)
meth public abstract java.lang.String getBaseURI()
meth public abstract java.lang.String getLocalName()
meth public abstract java.lang.String getNamespaceURI()
meth public abstract java.lang.String getNodeName()
meth public abstract java.lang.String getNodeValue()
meth public abstract java.lang.String getPrefix()
meth public abstract java.lang.String getTextContent()
meth public abstract java.lang.String lookupNamespaceURI(java.lang.String)
meth public abstract java.lang.String lookupPrefix(java.lang.String)
meth public abstract org.w3c.dom.Document getOwnerDocument()
meth public abstract org.w3c.dom.NamedNodeMap getAttributes()
meth public abstract org.w3c.dom.Node appendChild(org.w3c.dom.Node)
meth public abstract org.w3c.dom.Node cloneNode(boolean)
meth public abstract org.w3c.dom.Node getFirstChild()
meth public abstract org.w3c.dom.Node getLastChild()
meth public abstract org.w3c.dom.Node getNextSibling()
meth public abstract org.w3c.dom.Node getParentNode()
meth public abstract org.w3c.dom.Node getPreviousSibling()
meth public abstract org.w3c.dom.Node insertBefore(org.w3c.dom.Node,org.w3c.dom.Node)
meth public abstract org.w3c.dom.Node removeChild(org.w3c.dom.Node)
meth public abstract org.w3c.dom.Node replaceChild(org.w3c.dom.Node,org.w3c.dom.Node)
meth public abstract org.w3c.dom.NodeList getChildNodes()
meth public abstract short compareDocumentPosition(org.w3c.dom.Node)
meth public abstract short getNodeType()
meth public abstract void normalize()
meth public abstract void setNodeValue(java.lang.String)
meth public abstract void setPrefix(java.lang.String)
meth public abstract void setTextContent(java.lang.String)

CLSS public abstract interface org.w3c.dom.Text
intf org.w3c.dom.CharacterData
meth public abstract boolean isElementContentWhitespace()
meth public abstract java.lang.String getWholeText()
meth public abstract org.w3c.dom.Text replaceWholeText(java.lang.String)
meth public abstract org.w3c.dom.Text splitText(int)

