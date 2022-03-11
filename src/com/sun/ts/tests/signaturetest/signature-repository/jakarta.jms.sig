#Signature file v4.1
#Version 3.0

CLSS public abstract interface jakarta.jms.BytesMessage
intf jakarta.jms.Message
meth public abstract boolean readBoolean() throws jakarta.jms.JMSException
meth public abstract byte readByte() throws jakarta.jms.JMSException
meth public abstract char readChar() throws jakarta.jms.JMSException
meth public abstract double readDouble() throws jakarta.jms.JMSException
meth public abstract float readFloat() throws jakarta.jms.JMSException
meth public abstract int readBytes(byte[]) throws jakarta.jms.JMSException
meth public abstract int readBytes(byte[],int) throws jakarta.jms.JMSException
meth public abstract int readInt() throws jakarta.jms.JMSException
meth public abstract int readUnsignedByte() throws jakarta.jms.JMSException
meth public abstract int readUnsignedShort() throws jakarta.jms.JMSException
meth public abstract java.lang.String readUTF() throws jakarta.jms.JMSException
meth public abstract long getBodyLength() throws jakarta.jms.JMSException
meth public abstract long readLong() throws jakarta.jms.JMSException
meth public abstract short readShort() throws jakarta.jms.JMSException
meth public abstract void reset() throws jakarta.jms.JMSException
meth public abstract void writeBoolean(boolean) throws jakarta.jms.JMSException
meth public abstract void writeByte(byte) throws jakarta.jms.JMSException
meth public abstract void writeBytes(byte[]) throws jakarta.jms.JMSException
meth public abstract void writeBytes(byte[],int,int) throws jakarta.jms.JMSException
meth public abstract void writeChar(char) throws jakarta.jms.JMSException
meth public abstract void writeDouble(double) throws jakarta.jms.JMSException
meth public abstract void writeFloat(float) throws jakarta.jms.JMSException
meth public abstract void writeInt(int) throws jakarta.jms.JMSException
meth public abstract void writeLong(long) throws jakarta.jms.JMSException
meth public abstract void writeObject(java.lang.Object) throws jakarta.jms.JMSException
meth public abstract void writeShort(short) throws jakarta.jms.JMSException
meth public abstract void writeUTF(java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.CompletionListener
meth public abstract void onCompletion(jakarta.jms.Message)
meth public abstract void onException(jakarta.jms.Message,java.lang.Exception)

CLSS public abstract interface jakarta.jms.Connection
intf java.lang.AutoCloseable
meth public abstract jakarta.jms.ConnectionConsumer createConnectionConsumer(jakarta.jms.Destination,java.lang.String,jakarta.jms.ServerSessionPool,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.ConnectionConsumer createDurableConnectionConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String,jakarta.jms.ServerSessionPool,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.ConnectionConsumer createSharedConnectionConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String,jakarta.jms.ServerSessionPool,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.ConnectionConsumer createSharedDurableConnectionConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String,jakarta.jms.ServerSessionPool,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.ConnectionMetaData getMetaData() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.ExceptionListener getExceptionListener() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Session createSession() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Session createSession(boolean,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Session createSession(int) throws jakarta.jms.JMSException
meth public abstract java.lang.String getClientID() throws jakarta.jms.JMSException
meth public abstract void close() throws jakarta.jms.JMSException
meth public abstract void setClientID(java.lang.String) throws jakarta.jms.JMSException
meth public abstract void setExceptionListener(jakarta.jms.ExceptionListener) throws jakarta.jms.JMSException
meth public abstract void start() throws jakarta.jms.JMSException
meth public abstract void stop() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.ConnectionConsumer
meth public abstract jakarta.jms.ServerSessionPool getServerSessionPool() throws jakarta.jms.JMSException
meth public abstract void close() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.ConnectionFactory
meth public abstract jakarta.jms.Connection createConnection() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Connection createConnection(java.lang.String,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.JMSContext createContext()
meth public abstract jakarta.jms.JMSContext createContext(int)
meth public abstract jakarta.jms.JMSContext createContext(java.lang.String,java.lang.String)
meth public abstract jakarta.jms.JMSContext createContext(java.lang.String,java.lang.String,int)

CLSS public abstract interface jakarta.jms.ConnectionMetaData
meth public abstract int getJMSMajorVersion() throws jakarta.jms.JMSException
meth public abstract int getJMSMinorVersion() throws jakarta.jms.JMSException
meth public abstract int getProviderMajorVersion() throws jakarta.jms.JMSException
meth public abstract int getProviderMinorVersion() throws jakarta.jms.JMSException
meth public abstract java.lang.String getJMSProviderName() throws jakarta.jms.JMSException
meth public abstract java.lang.String getJMSVersion() throws jakarta.jms.JMSException
meth public abstract java.lang.String getProviderVersion() throws jakarta.jms.JMSException
meth public abstract java.util.Enumeration getJMSXPropertyNames() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.DeliveryMode
fld public final static int NON_PERSISTENT = 1
fld public final static int PERSISTENT = 2

CLSS public abstract interface jakarta.jms.Destination

CLSS public abstract interface jakarta.jms.ExceptionListener
meth public abstract void onException(jakarta.jms.JMSException)

CLSS public jakarta.jms.IllegalStateException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.IllegalStateRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public jakarta.jms.InvalidClientIDException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.InvalidClientIDRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public jakarta.jms.InvalidDestinationException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.InvalidDestinationRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public jakarta.jms.InvalidSelectorException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.InvalidSelectorRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.jms.JMSConnectionFactory
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, PARAMETER, TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.String value()

CLSS public abstract interface !annotation jakarta.jms.JMSConnectionFactoryDefinition
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.jms.JMSConnectionFactoryDefinitions)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean transactional()
meth public abstract !hasdefault int maxPoolSize()
meth public abstract !hasdefault int minPoolSize()
meth public abstract !hasdefault java.lang.String className()
meth public abstract !hasdefault java.lang.String clientId()
meth public abstract !hasdefault java.lang.String description()
meth public abstract !hasdefault java.lang.String interfaceName()
meth public abstract !hasdefault java.lang.String password()
meth public abstract !hasdefault java.lang.String resourceAdapter()
meth public abstract !hasdefault java.lang.String user()
meth public abstract !hasdefault java.lang.String[] properties()
meth public abstract java.lang.String name()

CLSS public abstract interface !annotation jakarta.jms.JMSConnectionFactoryDefinitions
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.jms.JMSConnectionFactoryDefinition[] value()

CLSS public abstract interface jakarta.jms.JMSConsumer
intf java.lang.AutoCloseable
meth public abstract <%0 extends java.lang.Object> {%%0} receiveBody(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> {%%0} receiveBody(java.lang.Class<{%%0}>,long)
meth public abstract <%0 extends java.lang.Object> {%%0} receiveBodyNoWait(java.lang.Class<{%%0}>)
meth public abstract jakarta.jms.Message receive()
meth public abstract jakarta.jms.Message receive(long)
meth public abstract jakarta.jms.Message receiveNoWait()
meth public abstract jakarta.jms.MessageListener getMessageListener()
meth public abstract java.lang.String getMessageSelector()
meth public abstract void close()
meth public abstract void setMessageListener(jakarta.jms.MessageListener)

CLSS public abstract interface jakarta.jms.JMSContext
fld public final static int AUTO_ACKNOWLEDGE = 1
fld public final static int CLIENT_ACKNOWLEDGE = 2
fld public final static int DUPS_OK_ACKNOWLEDGE = 3
fld public final static int SESSION_TRANSACTED = 0
intf java.lang.AutoCloseable
meth public abstract boolean getAutoStart()
meth public abstract boolean getTransacted()
meth public abstract int getSessionMode()
meth public abstract jakarta.jms.BytesMessage createBytesMessage()
meth public abstract jakarta.jms.ConnectionMetaData getMetaData()
meth public abstract jakarta.jms.ExceptionListener getExceptionListener()
meth public abstract jakarta.jms.JMSConsumer createConsumer(jakarta.jms.Destination)
meth public abstract jakarta.jms.JMSConsumer createConsumer(jakarta.jms.Destination,java.lang.String)
meth public abstract jakarta.jms.JMSConsumer createConsumer(jakarta.jms.Destination,java.lang.String,boolean)
meth public abstract jakarta.jms.JMSConsumer createDurableConsumer(jakarta.jms.Topic,java.lang.String)
meth public abstract jakarta.jms.JMSConsumer createDurableConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String,boolean)
meth public abstract jakarta.jms.JMSConsumer createSharedConsumer(jakarta.jms.Topic,java.lang.String)
meth public abstract jakarta.jms.JMSConsumer createSharedConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String)
meth public abstract jakarta.jms.JMSConsumer createSharedDurableConsumer(jakarta.jms.Topic,java.lang.String)
meth public abstract jakarta.jms.JMSConsumer createSharedDurableConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String)
meth public abstract jakarta.jms.JMSContext createContext(int)
meth public abstract jakarta.jms.JMSProducer createProducer()
meth public abstract jakarta.jms.MapMessage createMapMessage()
meth public abstract jakarta.jms.Message createMessage()
meth public abstract jakarta.jms.ObjectMessage createObjectMessage()
meth public abstract jakarta.jms.ObjectMessage createObjectMessage(java.io.Serializable)
meth public abstract jakarta.jms.Queue createQueue(java.lang.String)
meth public abstract jakarta.jms.QueueBrowser createBrowser(jakarta.jms.Queue)
meth public abstract jakarta.jms.QueueBrowser createBrowser(jakarta.jms.Queue,java.lang.String)
meth public abstract jakarta.jms.StreamMessage createStreamMessage()
meth public abstract jakarta.jms.TemporaryQueue createTemporaryQueue()
meth public abstract jakarta.jms.TemporaryTopic createTemporaryTopic()
meth public abstract jakarta.jms.TextMessage createTextMessage()
meth public abstract jakarta.jms.TextMessage createTextMessage(java.lang.String)
meth public abstract jakarta.jms.Topic createTopic(java.lang.String)
meth public abstract java.lang.String getClientID()
meth public abstract void acknowledge()
meth public abstract void close()
meth public abstract void commit()
meth public abstract void recover()
meth public abstract void rollback()
meth public abstract void setAutoStart(boolean)
meth public abstract void setClientID(java.lang.String)
meth public abstract void setExceptionListener(jakarta.jms.ExceptionListener)
meth public abstract void start()
meth public abstract void stop()
meth public abstract void unsubscribe(java.lang.String)

CLSS public abstract interface !annotation jakarta.jms.JMSDestinationDefinition
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.jms.JMSDestinationDefinitions)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String className()
meth public abstract !hasdefault java.lang.String description()
meth public abstract !hasdefault java.lang.String destinationName()
meth public abstract !hasdefault java.lang.String resourceAdapter()
meth public abstract !hasdefault java.lang.String[] properties()
meth public abstract java.lang.String interfaceName()
meth public abstract java.lang.String name()

CLSS public abstract interface !annotation jakarta.jms.JMSDestinationDefinitions
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.jms.JMSDestinationDefinition[] value()

CLSS public jakarta.jms.JMSException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
meth public java.lang.Exception getLinkedException()
meth public java.lang.String getErrorCode()
meth public void setLinkedException(java.lang.Exception)
supr java.lang.Exception
hfds errorCode,linkedException,serialVersionUID

CLSS public abstract interface !annotation jakarta.jms.JMSPasswordCredential
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, PARAMETER, TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.String password()
meth public abstract java.lang.String userName()

CLSS public abstract interface jakarta.jms.JMSProducer
meth public abstract boolean getBooleanProperty(java.lang.String)
meth public abstract boolean getDisableMessageID()
meth public abstract boolean getDisableMessageTimestamp()
meth public abstract boolean propertyExists(java.lang.String)
meth public abstract byte getByteProperty(java.lang.String)
meth public abstract byte[] getJMSCorrelationIDAsBytes()
meth public abstract double getDoubleProperty(java.lang.String)
meth public abstract float getFloatProperty(java.lang.String)
meth public abstract int getDeliveryMode()
meth public abstract int getIntProperty(java.lang.String)
meth public abstract int getPriority()
meth public abstract jakarta.jms.CompletionListener getAsync()
meth public abstract jakarta.jms.Destination getJMSReplyTo()
meth public abstract jakarta.jms.JMSProducer clearProperties()
meth public abstract jakarta.jms.JMSProducer send(jakarta.jms.Destination,byte[])
meth public abstract jakarta.jms.JMSProducer send(jakarta.jms.Destination,jakarta.jms.Message)
meth public abstract jakarta.jms.JMSProducer send(jakarta.jms.Destination,java.io.Serializable)
meth public abstract jakarta.jms.JMSProducer send(jakarta.jms.Destination,java.lang.String)
meth public abstract jakarta.jms.JMSProducer send(jakarta.jms.Destination,java.util.Map<java.lang.String,java.lang.Object>)
meth public abstract jakarta.jms.JMSProducer setAsync(jakarta.jms.CompletionListener)
meth public abstract jakarta.jms.JMSProducer setDeliveryDelay(long)
meth public abstract jakarta.jms.JMSProducer setDeliveryMode(int)
meth public abstract jakarta.jms.JMSProducer setDisableMessageID(boolean)
meth public abstract jakarta.jms.JMSProducer setDisableMessageTimestamp(boolean)
meth public abstract jakarta.jms.JMSProducer setJMSCorrelationID(java.lang.String)
meth public abstract jakarta.jms.JMSProducer setJMSCorrelationIDAsBytes(byte[])
meth public abstract jakarta.jms.JMSProducer setJMSReplyTo(jakarta.jms.Destination)
meth public abstract jakarta.jms.JMSProducer setJMSType(java.lang.String)
meth public abstract jakarta.jms.JMSProducer setPriority(int)
meth public abstract jakarta.jms.JMSProducer setProperty(java.lang.String,boolean)
meth public abstract jakarta.jms.JMSProducer setProperty(java.lang.String,byte)
meth public abstract jakarta.jms.JMSProducer setProperty(java.lang.String,double)
meth public abstract jakarta.jms.JMSProducer setProperty(java.lang.String,float)
meth public abstract jakarta.jms.JMSProducer setProperty(java.lang.String,int)
meth public abstract jakarta.jms.JMSProducer setProperty(java.lang.String,java.lang.Object)
meth public abstract jakarta.jms.JMSProducer setProperty(java.lang.String,java.lang.String)
meth public abstract jakarta.jms.JMSProducer setProperty(java.lang.String,long)
meth public abstract jakarta.jms.JMSProducer setProperty(java.lang.String,short)
meth public abstract jakarta.jms.JMSProducer setTimeToLive(long)
meth public abstract java.lang.Object getObjectProperty(java.lang.String)
meth public abstract java.lang.String getJMSCorrelationID()
meth public abstract java.lang.String getJMSType()
meth public abstract java.lang.String getStringProperty(java.lang.String)
meth public abstract java.util.Set<java.lang.String> getPropertyNames()
meth public abstract long getDeliveryDelay()
meth public abstract long getLongProperty(java.lang.String)
meth public abstract long getTimeToLive()
meth public abstract short getShortProperty(java.lang.String)

CLSS public jakarta.jms.JMSRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
meth public java.lang.String getErrorCode()
supr java.lang.RuntimeException
hfds errorCode,serialVersionUID

CLSS public jakarta.jms.JMSSecurityException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.JMSSecurityRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.jms.JMSSessionMode
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, PARAMETER, TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int value()

CLSS public abstract interface jakarta.jms.MapMessage
intf jakarta.jms.Message
meth public abstract boolean getBoolean(java.lang.String) throws jakarta.jms.JMSException
meth public abstract boolean itemExists(java.lang.String) throws jakarta.jms.JMSException
meth public abstract byte getByte(java.lang.String) throws jakarta.jms.JMSException
meth public abstract byte[] getBytes(java.lang.String) throws jakarta.jms.JMSException
meth public abstract char getChar(java.lang.String) throws jakarta.jms.JMSException
meth public abstract double getDouble(java.lang.String) throws jakarta.jms.JMSException
meth public abstract float getFloat(java.lang.String) throws jakarta.jms.JMSException
meth public abstract int getInt(java.lang.String) throws jakarta.jms.JMSException
meth public abstract java.lang.Object getObject(java.lang.String) throws jakarta.jms.JMSException
meth public abstract java.lang.String getString(java.lang.String) throws jakarta.jms.JMSException
meth public abstract java.util.Enumeration getMapNames() throws jakarta.jms.JMSException
meth public abstract long getLong(java.lang.String) throws jakarta.jms.JMSException
meth public abstract short getShort(java.lang.String) throws jakarta.jms.JMSException
meth public abstract void setBoolean(java.lang.String,boolean) throws jakarta.jms.JMSException
meth public abstract void setByte(java.lang.String,byte) throws jakarta.jms.JMSException
meth public abstract void setBytes(java.lang.String,byte[]) throws jakarta.jms.JMSException
meth public abstract void setBytes(java.lang.String,byte[],int,int) throws jakarta.jms.JMSException
meth public abstract void setChar(java.lang.String,char) throws jakarta.jms.JMSException
meth public abstract void setDouble(java.lang.String,double) throws jakarta.jms.JMSException
meth public abstract void setFloat(java.lang.String,float) throws jakarta.jms.JMSException
meth public abstract void setInt(java.lang.String,int) throws jakarta.jms.JMSException
meth public abstract void setLong(java.lang.String,long) throws jakarta.jms.JMSException
meth public abstract void setObject(java.lang.String,java.lang.Object) throws jakarta.jms.JMSException
meth public abstract void setShort(java.lang.String,short) throws jakarta.jms.JMSException
meth public abstract void setString(java.lang.String,java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.Message
fld public final static int DEFAULT_DELIVERY_MODE = 2
fld public final static int DEFAULT_PRIORITY = 4
fld public final static long DEFAULT_DELIVERY_DELAY = 0
fld public final static long DEFAULT_TIME_TO_LIVE = 0
meth public abstract <%0 extends java.lang.Object> {%%0} getBody(java.lang.Class<{%%0}>) throws jakarta.jms.JMSException
meth public abstract boolean getBooleanProperty(java.lang.String) throws jakarta.jms.JMSException
meth public abstract boolean getJMSRedelivered() throws jakarta.jms.JMSException
meth public abstract boolean isBodyAssignableTo(java.lang.Class) throws jakarta.jms.JMSException
meth public abstract boolean propertyExists(java.lang.String) throws jakarta.jms.JMSException
meth public abstract byte getByteProperty(java.lang.String) throws jakarta.jms.JMSException
meth public abstract byte[] getJMSCorrelationIDAsBytes() throws jakarta.jms.JMSException
meth public abstract double getDoubleProperty(java.lang.String) throws jakarta.jms.JMSException
meth public abstract float getFloatProperty(java.lang.String) throws jakarta.jms.JMSException
meth public abstract int getIntProperty(java.lang.String) throws jakarta.jms.JMSException
meth public abstract int getJMSDeliveryMode() throws jakarta.jms.JMSException
meth public abstract int getJMSPriority() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Destination getJMSDestination() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Destination getJMSReplyTo() throws jakarta.jms.JMSException
meth public abstract java.lang.Object getObjectProperty(java.lang.String) throws jakarta.jms.JMSException
meth public abstract java.lang.String getJMSCorrelationID() throws jakarta.jms.JMSException
meth public abstract java.lang.String getJMSMessageID() throws jakarta.jms.JMSException
meth public abstract java.lang.String getJMSType() throws jakarta.jms.JMSException
meth public abstract java.lang.String getStringProperty(java.lang.String) throws jakarta.jms.JMSException
meth public abstract java.util.Enumeration getPropertyNames() throws jakarta.jms.JMSException
meth public abstract long getJMSDeliveryTime() throws jakarta.jms.JMSException
meth public abstract long getJMSExpiration() throws jakarta.jms.JMSException
meth public abstract long getJMSTimestamp() throws jakarta.jms.JMSException
meth public abstract long getLongProperty(java.lang.String) throws jakarta.jms.JMSException
meth public abstract short getShortProperty(java.lang.String) throws jakarta.jms.JMSException
meth public abstract void acknowledge() throws jakarta.jms.JMSException
meth public abstract void clearBody() throws jakarta.jms.JMSException
meth public abstract void clearProperties() throws jakarta.jms.JMSException
meth public abstract void setBooleanProperty(java.lang.String,boolean) throws jakarta.jms.JMSException
meth public abstract void setByteProperty(java.lang.String,byte) throws jakarta.jms.JMSException
meth public abstract void setDoubleProperty(java.lang.String,double) throws jakarta.jms.JMSException
meth public abstract void setFloatProperty(java.lang.String,float) throws jakarta.jms.JMSException
meth public abstract void setIntProperty(java.lang.String,int) throws jakarta.jms.JMSException
meth public abstract void setJMSCorrelationID(java.lang.String) throws jakarta.jms.JMSException
meth public abstract void setJMSCorrelationIDAsBytes(byte[]) throws jakarta.jms.JMSException
meth public abstract void setJMSDeliveryMode(int) throws jakarta.jms.JMSException
meth public abstract void setJMSDeliveryTime(long) throws jakarta.jms.JMSException
meth public abstract void setJMSDestination(jakarta.jms.Destination) throws jakarta.jms.JMSException
meth public abstract void setJMSExpiration(long) throws jakarta.jms.JMSException
meth public abstract void setJMSMessageID(java.lang.String) throws jakarta.jms.JMSException
meth public abstract void setJMSPriority(int) throws jakarta.jms.JMSException
meth public abstract void setJMSRedelivered(boolean) throws jakarta.jms.JMSException
meth public abstract void setJMSReplyTo(jakarta.jms.Destination) throws jakarta.jms.JMSException
meth public abstract void setJMSTimestamp(long) throws jakarta.jms.JMSException
meth public abstract void setJMSType(java.lang.String) throws jakarta.jms.JMSException
meth public abstract void setLongProperty(java.lang.String,long) throws jakarta.jms.JMSException
meth public abstract void setObjectProperty(java.lang.String,java.lang.Object) throws jakarta.jms.JMSException
meth public abstract void setShortProperty(java.lang.String,short) throws jakarta.jms.JMSException
meth public abstract void setStringProperty(java.lang.String,java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.MessageConsumer
intf java.lang.AutoCloseable
meth public abstract jakarta.jms.Message receive() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Message receive(long) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Message receiveNoWait() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageListener getMessageListener() throws jakarta.jms.JMSException
meth public abstract java.lang.String getMessageSelector() throws jakarta.jms.JMSException
meth public abstract void close() throws jakarta.jms.JMSException
meth public abstract void setMessageListener(jakarta.jms.MessageListener) throws jakarta.jms.JMSException

CLSS public jakarta.jms.MessageEOFException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.MessageFormatException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.MessageFormatRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public abstract interface jakarta.jms.MessageListener
meth public abstract void onMessage(jakarta.jms.Message)

CLSS public jakarta.jms.MessageNotReadableException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.MessageNotWriteableException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.MessageNotWriteableRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public abstract interface jakarta.jms.MessageProducer
intf java.lang.AutoCloseable
meth public abstract boolean getDisableMessageID() throws jakarta.jms.JMSException
meth public abstract boolean getDisableMessageTimestamp() throws jakarta.jms.JMSException
meth public abstract int getDeliveryMode() throws jakarta.jms.JMSException
meth public abstract int getPriority() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Destination getDestination() throws jakarta.jms.JMSException
meth public abstract long getDeliveryDelay() throws jakarta.jms.JMSException
meth public abstract long getTimeToLive() throws jakarta.jms.JMSException
meth public abstract void close() throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Destination,jakarta.jms.Message) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Destination,jakarta.jms.Message,int,int,long) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Destination,jakarta.jms.Message,int,int,long,jakarta.jms.CompletionListener) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Destination,jakarta.jms.Message,jakarta.jms.CompletionListener) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Message) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Message,int,int,long) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Message,int,int,long,jakarta.jms.CompletionListener) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Message,jakarta.jms.CompletionListener) throws jakarta.jms.JMSException
meth public abstract void setDeliveryDelay(long) throws jakarta.jms.JMSException
meth public abstract void setDeliveryMode(int) throws jakarta.jms.JMSException
meth public abstract void setDisableMessageID(boolean) throws jakarta.jms.JMSException
meth public abstract void setDisableMessageTimestamp(boolean) throws jakarta.jms.JMSException
meth public abstract void setPriority(int) throws jakarta.jms.JMSException
meth public abstract void setTimeToLive(long) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.ObjectMessage
intf jakarta.jms.Message
meth public abstract java.io.Serializable getObject() throws jakarta.jms.JMSException
meth public abstract void setObject(java.io.Serializable) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.Queue
intf jakarta.jms.Destination
meth public abstract java.lang.String getQueueName() throws jakarta.jms.JMSException
meth public abstract java.lang.String toString()

CLSS public abstract interface jakarta.jms.QueueBrowser
intf java.lang.AutoCloseable
meth public abstract jakarta.jms.Queue getQueue() throws jakarta.jms.JMSException
meth public abstract java.lang.String getMessageSelector() throws jakarta.jms.JMSException
meth public abstract java.util.Enumeration getEnumeration() throws jakarta.jms.JMSException
meth public abstract void close() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.QueueConnection
intf jakarta.jms.Connection
meth public abstract jakarta.jms.ConnectionConsumer createConnectionConsumer(jakarta.jms.Queue,java.lang.String,jakarta.jms.ServerSessionPool,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.QueueSession createQueueSession(boolean,int) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.QueueConnectionFactory
intf jakarta.jms.ConnectionFactory
meth public abstract jakarta.jms.QueueConnection createQueueConnection() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.QueueConnection createQueueConnection(java.lang.String,java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.QueueReceiver
intf jakarta.jms.MessageConsumer
meth public abstract jakarta.jms.Queue getQueue() throws jakarta.jms.JMSException

CLSS public jakarta.jms.QueueRequestor
cons public init(jakarta.jms.QueueSession,jakarta.jms.Queue) throws jakarta.jms.JMSException
meth public jakarta.jms.Message request(jakarta.jms.Message) throws jakarta.jms.JMSException
meth public void close() throws jakarta.jms.JMSException
supr java.lang.Object
hfds receiver,sender,session,tempQueue

CLSS public abstract interface jakarta.jms.QueueSender
intf jakarta.jms.MessageProducer
meth public abstract jakarta.jms.Queue getQueue() throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Message) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Message,int,int,long) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Queue,jakarta.jms.Message) throws jakarta.jms.JMSException
meth public abstract void send(jakarta.jms.Queue,jakarta.jms.Message,int,int,long) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.QueueSession
intf jakarta.jms.Session
meth public abstract jakarta.jms.Queue createQueue(java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.QueueBrowser createBrowser(jakarta.jms.Queue) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.QueueBrowser createBrowser(jakarta.jms.Queue,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.QueueReceiver createReceiver(jakarta.jms.Queue) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.QueueReceiver createReceiver(jakarta.jms.Queue,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.QueueSender createSender(jakarta.jms.Queue) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TemporaryQueue createTemporaryQueue() throws jakarta.jms.JMSException

CLSS public jakarta.jms.ResourceAllocationException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.ResourceAllocationRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public abstract interface jakarta.jms.ServerSession
meth public abstract jakarta.jms.Session getSession() throws jakarta.jms.JMSException
meth public abstract void start() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.ServerSessionPool
meth public abstract jakarta.jms.ServerSession getServerSession() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.Session
fld public final static int AUTO_ACKNOWLEDGE = 1
fld public final static int CLIENT_ACKNOWLEDGE = 2
fld public final static int DUPS_OK_ACKNOWLEDGE = 3
fld public final static int SESSION_TRANSACTED = 0
intf java.lang.AutoCloseable
intf java.lang.Runnable
meth public abstract boolean getTransacted() throws jakarta.jms.JMSException
meth public abstract int getAcknowledgeMode() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.BytesMessage createBytesMessage() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MapMessage createMapMessage() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Message createMessage() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageConsumer createConsumer(jakarta.jms.Destination) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageConsumer createConsumer(jakarta.jms.Destination,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageConsumer createConsumer(jakarta.jms.Destination,java.lang.String,boolean) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageConsumer createDurableConsumer(jakarta.jms.Topic,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageConsumer createDurableConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String,boolean) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageConsumer createSharedConsumer(jakarta.jms.Topic,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageConsumer createSharedConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageConsumer createSharedDurableConsumer(jakarta.jms.Topic,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageConsumer createSharedDurableConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageListener getMessageListener() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.MessageProducer createProducer(jakarta.jms.Destination) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.ObjectMessage createObjectMessage() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.ObjectMessage createObjectMessage(java.io.Serializable) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Queue createQueue(java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.QueueBrowser createBrowser(jakarta.jms.Queue) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.QueueBrowser createBrowser(jakarta.jms.Queue,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.StreamMessage createStreamMessage() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TemporaryQueue createTemporaryQueue() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TemporaryTopic createTemporaryTopic() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TextMessage createTextMessage() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TextMessage createTextMessage(java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Topic createTopic(java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TopicSubscriber createDurableSubscriber(jakarta.jms.Topic,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TopicSubscriber createDurableSubscriber(jakarta.jms.Topic,java.lang.String,java.lang.String,boolean) throws jakarta.jms.JMSException
meth public abstract void close() throws jakarta.jms.JMSException
meth public abstract void commit() throws jakarta.jms.JMSException
meth public abstract void recover() throws jakarta.jms.JMSException
meth public abstract void rollback() throws jakarta.jms.JMSException
meth public abstract void run()
meth public abstract void setMessageListener(jakarta.jms.MessageListener) throws jakarta.jms.JMSException
meth public abstract void unsubscribe(java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.StreamMessage
intf jakarta.jms.Message
meth public abstract boolean readBoolean() throws jakarta.jms.JMSException
meth public abstract byte readByte() throws jakarta.jms.JMSException
meth public abstract char readChar() throws jakarta.jms.JMSException
meth public abstract double readDouble() throws jakarta.jms.JMSException
meth public abstract float readFloat() throws jakarta.jms.JMSException
meth public abstract int readBytes(byte[]) throws jakarta.jms.JMSException
meth public abstract int readInt() throws jakarta.jms.JMSException
meth public abstract java.lang.Object readObject() throws jakarta.jms.JMSException
meth public abstract java.lang.String readString() throws jakarta.jms.JMSException
meth public abstract long readLong() throws jakarta.jms.JMSException
meth public abstract short readShort() throws jakarta.jms.JMSException
meth public abstract void reset() throws jakarta.jms.JMSException
meth public abstract void writeBoolean(boolean) throws jakarta.jms.JMSException
meth public abstract void writeByte(byte) throws jakarta.jms.JMSException
meth public abstract void writeBytes(byte[]) throws jakarta.jms.JMSException
meth public abstract void writeBytes(byte[],int,int) throws jakarta.jms.JMSException
meth public abstract void writeChar(char) throws jakarta.jms.JMSException
meth public abstract void writeDouble(double) throws jakarta.jms.JMSException
meth public abstract void writeFloat(float) throws jakarta.jms.JMSException
meth public abstract void writeInt(int) throws jakarta.jms.JMSException
meth public abstract void writeLong(long) throws jakarta.jms.JMSException
meth public abstract void writeObject(java.lang.Object) throws jakarta.jms.JMSException
meth public abstract void writeShort(short) throws jakarta.jms.JMSException
meth public abstract void writeString(java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.TemporaryQueue
intf jakarta.jms.Queue
meth public abstract void delete() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.TemporaryTopic
intf jakarta.jms.Topic
meth public abstract void delete() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.TextMessage
intf jakarta.jms.Message
meth public abstract java.lang.String getText() throws jakarta.jms.JMSException
meth public abstract void setText(java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.Topic
intf jakarta.jms.Destination
meth public abstract java.lang.String getTopicName() throws jakarta.jms.JMSException
meth public abstract java.lang.String toString()

CLSS public abstract interface jakarta.jms.TopicConnection
intf jakarta.jms.Connection
meth public abstract jakarta.jms.ConnectionConsumer createConnectionConsumer(jakarta.jms.Topic,java.lang.String,jakarta.jms.ServerSessionPool,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.ConnectionConsumer createDurableConnectionConsumer(jakarta.jms.Topic,java.lang.String,java.lang.String,jakarta.jms.ServerSessionPool,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TopicSession createTopicSession(boolean,int) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.TopicConnectionFactory
intf jakarta.jms.ConnectionFactory
meth public abstract jakarta.jms.TopicConnection createTopicConnection() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TopicConnection createTopicConnection(java.lang.String,java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.TopicPublisher
intf jakarta.jms.MessageProducer
meth public abstract jakarta.jms.Topic getTopic() throws jakarta.jms.JMSException
meth public abstract void publish(jakarta.jms.Message) throws jakarta.jms.JMSException
meth public abstract void publish(jakarta.jms.Message,int,int,long) throws jakarta.jms.JMSException
meth public abstract void publish(jakarta.jms.Topic,jakarta.jms.Message) throws jakarta.jms.JMSException
meth public abstract void publish(jakarta.jms.Topic,jakarta.jms.Message,int,int,long) throws jakarta.jms.JMSException

CLSS public jakarta.jms.TopicRequestor
cons public init(jakarta.jms.TopicSession,jakarta.jms.Topic) throws jakarta.jms.JMSException
meth public jakarta.jms.Message request(jakarta.jms.Message) throws jakarta.jms.JMSException
meth public void close() throws jakarta.jms.JMSException
supr java.lang.Object
hfds publisher,session,subscriber,tempTopic

CLSS public abstract interface jakarta.jms.TopicSession
intf jakarta.jms.Session
meth public abstract jakarta.jms.TemporaryTopic createTemporaryTopic() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Topic createTopic(java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TopicPublisher createPublisher(jakarta.jms.Topic) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TopicSubscriber createDurableSubscriber(jakarta.jms.Topic,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TopicSubscriber createDurableSubscriber(jakarta.jms.Topic,java.lang.String,java.lang.String,boolean) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TopicSubscriber createSubscriber(jakarta.jms.Topic) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.TopicSubscriber createSubscriber(jakarta.jms.Topic,java.lang.String,boolean) throws jakarta.jms.JMSException
meth public abstract void unsubscribe(java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.TopicSubscriber
intf jakarta.jms.MessageConsumer
meth public abstract boolean getNoLocal() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Topic getTopic() throws jakarta.jms.JMSException

CLSS public jakarta.jms.TransactionInProgressException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.TransactionInProgressRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public jakarta.jms.TransactionRolledBackException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Exception)
supr jakarta.jms.JMSException
hfds serialVersionUID

CLSS public jakarta.jms.TransactionRolledBackRuntimeException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.String,java.lang.Throwable)
supr jakarta.jms.JMSRuntimeException
hfds serialVersionUID

CLSS public abstract interface jakarta.jms.XAConnection
intf jakarta.jms.Connection
meth public abstract jakarta.jms.Session createSession(boolean,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.XASession createXASession() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.XAConnectionFactory
meth public abstract jakarta.jms.XAConnection createXAConnection() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.XAConnection createXAConnection(java.lang.String,java.lang.String) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.XAJMSContext createXAContext()
meth public abstract jakarta.jms.XAJMSContext createXAContext(java.lang.String,java.lang.String)

CLSS public abstract interface jakarta.jms.XAJMSContext
intf jakarta.jms.JMSContext
meth public abstract boolean getTransacted()
meth public abstract jakarta.jms.JMSContext getContext()
meth public abstract javax.transaction.xa.XAResource getXAResource()
meth public abstract void commit()
meth public abstract void rollback()

CLSS public abstract interface jakarta.jms.XAQueueConnection
intf jakarta.jms.QueueConnection
intf jakarta.jms.XAConnection
meth public abstract jakarta.jms.QueueSession createQueueSession(boolean,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.XAQueueSession createXAQueueSession() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.XAQueueConnectionFactory
intf jakarta.jms.QueueConnectionFactory
intf jakarta.jms.XAConnectionFactory
meth public abstract jakarta.jms.XAQueueConnection createXAQueueConnection() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.XAQueueConnection createXAQueueConnection(java.lang.String,java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.XAQueueSession
intf jakarta.jms.XASession
meth public abstract jakarta.jms.QueueSession getQueueSession() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.XASession
intf jakarta.jms.Session
meth public abstract boolean getTransacted() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.Session getSession() throws jakarta.jms.JMSException
meth public abstract javax.transaction.xa.XAResource getXAResource()
meth public abstract void commit() throws jakarta.jms.JMSException
meth public abstract void rollback() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.XATopicConnection
intf jakarta.jms.TopicConnection
intf jakarta.jms.XAConnection
meth public abstract jakarta.jms.TopicSession createTopicSession(boolean,int) throws jakarta.jms.JMSException
meth public abstract jakarta.jms.XATopicSession createXATopicSession() throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.XATopicConnectionFactory
intf jakarta.jms.TopicConnectionFactory
intf jakarta.jms.XAConnectionFactory
meth public abstract jakarta.jms.XATopicConnection createXATopicConnection() throws jakarta.jms.JMSException
meth public abstract jakarta.jms.XATopicConnection createXATopicConnection(java.lang.String,java.lang.String) throws jakarta.jms.JMSException

CLSS public abstract interface jakarta.jms.XATopicSession
intf jakarta.jms.XASession
meth public abstract jakarta.jms.TopicSession getTopicSession() throws jakarta.jms.JMSException

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

CLSS public abstract interface java.lang.Runnable
 anno 0 java.lang.FunctionalInterface()
meth public abstract void run()

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

