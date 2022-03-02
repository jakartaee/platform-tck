#Signature file v4.1
#Version 2.1

CLSS public abstract interface !annotation jakarta.resource.AdministeredObjectDefinition
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.resource.AdministeredObjectDefinitions)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String description()
meth public abstract !hasdefault java.lang.String interfaceName()
meth public abstract !hasdefault java.lang.String[] properties()
meth public abstract java.lang.String className()
meth public abstract java.lang.String name()
meth public abstract java.lang.String resourceAdapter()

CLSS public abstract interface !annotation jakarta.resource.AdministeredObjectDefinitions
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.resource.AdministeredObjectDefinition[] value()

CLSS public abstract interface !annotation jakarta.resource.ConnectionFactoryDefinition
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.resource.ConnectionFactoryDefinitions)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int maxPoolSize()
meth public abstract !hasdefault int minPoolSize()
meth public abstract !hasdefault jakarta.resource.spi.TransactionSupport$TransactionSupportLevel transactionSupport()
meth public abstract !hasdefault java.lang.String description()
meth public abstract !hasdefault java.lang.String[] properties()
meth public abstract java.lang.String interfaceName()
meth public abstract java.lang.String name()
meth public abstract java.lang.String resourceAdapter()

CLSS public abstract interface !annotation jakarta.resource.ConnectionFactoryDefinitions
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.resource.ConnectionFactoryDefinition[] value()

CLSS public jakarta.resource.NotSupportedException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.Referenceable
intf javax.naming.Referenceable
meth public abstract void setReference(javax.naming.Reference)

CLSS public jakarta.resource.ResourceException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
meth public java.lang.Exception getLinkedException()
meth public java.lang.String getErrorCode()
meth public java.lang.String getMessage()
meth public void setErrorCode(java.lang.String)
meth public void setLinkedException(java.lang.Exception)
supr java.lang.Exception
hfds errorCode,linkedException

CLSS public abstract interface jakarta.resource.cci.Connection
meth public abstract jakarta.resource.cci.ConnectionMetaData getMetaData() throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.cci.Interaction createInteraction() throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.cci.LocalTransaction getLocalTransaction() throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.cci.ResultSetInfo getResultSetInfo() throws jakarta.resource.ResourceException
meth public abstract void close() throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.cci.ConnectionFactory
intf jakarta.resource.Referenceable
intf java.io.Serializable
meth public abstract jakarta.resource.cci.Connection getConnection() throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.cci.Connection getConnection(jakarta.resource.cci.ConnectionSpec) throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.cci.RecordFactory getRecordFactory() throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.cci.ResourceAdapterMetaData getMetaData() throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.cci.ConnectionMetaData
meth public abstract java.lang.String getEISProductName() throws jakarta.resource.ResourceException
meth public abstract java.lang.String getEISProductVersion() throws jakarta.resource.ResourceException
meth public abstract java.lang.String getUserName() throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.cci.ConnectionSpec

CLSS public abstract interface jakarta.resource.cci.IndexedRecord<%0 extends java.lang.Object>
intf jakarta.resource.cci.Record
intf java.io.Serializable
intf java.util.List<{jakarta.resource.cci.IndexedRecord%0}>

CLSS public abstract interface jakarta.resource.cci.Interaction
meth public abstract boolean execute(jakarta.resource.cci.InteractionSpec,jakarta.resource.cci.Record,jakarta.resource.cci.Record) throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.cci.Connection getConnection()
meth public abstract jakarta.resource.cci.Record execute(jakarta.resource.cci.InteractionSpec,jakarta.resource.cci.Record) throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.cci.ResourceWarning getWarnings() throws jakarta.resource.ResourceException
meth public abstract void clearWarnings() throws jakarta.resource.ResourceException
meth public abstract void close() throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.cci.InteractionSpec
fld public final static int SYNC_RECEIVE = 2
fld public final static int SYNC_SEND = 0
fld public final static int SYNC_SEND_RECEIVE = 1
intf java.io.Serializable

CLSS public abstract interface jakarta.resource.cci.LocalTransaction
meth public abstract void begin() throws jakarta.resource.ResourceException
meth public abstract void commit() throws jakarta.resource.ResourceException
meth public abstract void rollback() throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.cci.MappedRecord<%0 extends java.lang.Object, %1 extends java.lang.Object>
intf jakarta.resource.cci.Record
intf java.io.Serializable
intf java.util.Map<{jakarta.resource.cci.MappedRecord%0},{jakarta.resource.cci.MappedRecord%1}>

CLSS public abstract interface jakarta.resource.cci.MessageListener
meth public abstract jakarta.resource.cci.Record onMessage(jakarta.resource.cci.Record) throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.cci.Record
intf java.io.Serializable
intf java.lang.Cloneable
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()
meth public abstract java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth public abstract java.lang.String getRecordName()
meth public abstract java.lang.String getRecordShortDescription()
meth public abstract void setRecordName(java.lang.String)
meth public abstract void setRecordShortDescription(java.lang.String)

CLSS public abstract interface jakarta.resource.cci.RecordFactory
meth public abstract <%0 extends java.lang.Object, %1 extends java.lang.Object> jakarta.resource.cci.MappedRecord<{%%0},{%%1}> createMappedRecord(java.lang.String) throws jakarta.resource.ResourceException
meth public abstract <%0 extends java.lang.Object> jakarta.resource.cci.IndexedRecord<{%%0}> createIndexedRecord(java.lang.String) throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.cci.ResourceAdapterMetaData
meth public abstract boolean supportsExecuteWithInputAndOutputRecord()
meth public abstract boolean supportsExecuteWithInputRecordOnly()
meth public abstract boolean supportsLocalTransactionDemarcation()
meth public abstract java.lang.String getAdapterName()
meth public abstract java.lang.String getAdapterShortDescription()
meth public abstract java.lang.String getAdapterVendorName()
meth public abstract java.lang.String getAdapterVersion()
meth public abstract java.lang.String getSpecVersion()
meth public abstract java.lang.String[] getInteractionSpecsSupported()

CLSS public jakarta.resource.cci.ResourceWarning
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
meth public jakarta.resource.cci.ResourceWarning getLinkedWarning()
meth public void setLinkedWarning(jakarta.resource.cci.ResourceWarning)
supr jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.cci.ResultSet
intf jakarta.resource.cci.Record
intf java.sql.ResultSet

CLSS public abstract interface jakarta.resource.cci.ResultSetInfo
meth public abstract boolean deletesAreDetected(int) throws jakarta.resource.ResourceException
meth public abstract boolean insertsAreDetected(int) throws jakarta.resource.ResourceException
meth public abstract boolean othersDeletesAreVisible(int) throws jakarta.resource.ResourceException
meth public abstract boolean othersInsertsAreVisible(int) throws jakarta.resource.ResourceException
meth public abstract boolean othersUpdatesAreVisible(int) throws jakarta.resource.ResourceException
meth public abstract boolean ownDeletesAreVisible(int) throws jakarta.resource.ResourceException
meth public abstract boolean ownInsertsAreVisible(int) throws jakarta.resource.ResourceException
meth public abstract boolean ownUpdatesAreVisible(int) throws jakarta.resource.ResourceException
meth public abstract boolean supportsResultSetType(int) throws jakarta.resource.ResourceException
meth public abstract boolean supportsResultTypeConcurrency(int,int) throws jakarta.resource.ResourceException
meth public abstract boolean updatesAreDetected(int) throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.cci.Streamable
meth public abstract void read(java.io.InputStream) throws java.io.IOException
meth public abstract void write(java.io.OutputStream) throws java.io.IOException

CLSS public abstract interface !annotation jakarta.resource.spi.Activation
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class[] messageListeners()

CLSS public abstract interface jakarta.resource.spi.ActivationSpec
intf jakarta.resource.spi.ResourceAdapterAssociation
meth public abstract void validate() throws jakarta.resource.spi.InvalidPropertyException

CLSS public abstract interface !annotation jakarta.resource.spi.AdministeredObject
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class[] adminObjectInterfaces()

CLSS public jakarta.resource.spi.ApplicationServerInternalException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public abstract interface !annotation jakarta.resource.spi.AuthenticationMechanism
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[])
innr public final static !enum CredentialInterface
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.resource.spi.AuthenticationMechanism$CredentialInterface credentialInterface()
meth public abstract !hasdefault java.lang.String authMechanism()
meth public abstract !hasdefault java.lang.String[] description()

CLSS public final static !enum jakarta.resource.spi.AuthenticationMechanism$CredentialInterface
 outer jakarta.resource.spi.AuthenticationMechanism
fld public final static jakarta.resource.spi.AuthenticationMechanism$CredentialInterface GSSCredential
fld public final static jakarta.resource.spi.AuthenticationMechanism$CredentialInterface GenericCredential
fld public final static jakarta.resource.spi.AuthenticationMechanism$CredentialInterface PasswordCredential
meth public static jakarta.resource.spi.AuthenticationMechanism$CredentialInterface valueOf(java.lang.String)
meth public static jakarta.resource.spi.AuthenticationMechanism$CredentialInterface[] values()
supr java.lang.Enum<jakarta.resource.spi.AuthenticationMechanism$CredentialInterface>

CLSS public abstract interface jakarta.resource.spi.BootstrapContext
meth public abstract boolean isContextSupported(java.lang.Class<? extends jakarta.resource.spi.work.WorkContext>)
meth public abstract jakarta.resource.spi.XATerminator getXATerminator()
meth public abstract jakarta.resource.spi.work.WorkManager getWorkManager()
meth public abstract jakarta.transaction.TransactionSynchronizationRegistry getTransactionSynchronizationRegistry()
meth public abstract java.util.Timer createTimer() throws jakarta.resource.spi.UnavailableException

CLSS public jakarta.resource.spi.CommException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public abstract interface !annotation jakarta.resource.spi.ConfigProperty
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[FIELD, METHOD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean confidential()
meth public abstract !hasdefault boolean ignore()
meth public abstract !hasdefault boolean supportsDynamicUpdates()
meth public abstract !hasdefault java.lang.Class type()
meth public abstract !hasdefault java.lang.String defaultValue()
meth public abstract !hasdefault java.lang.String[] description()

CLSS public abstract interface !annotation jakarta.resource.spi.ConnectionDefinition
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.resource.spi.ConnectionDefinitions)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class connection()
meth public abstract java.lang.Class connectionFactory()
meth public abstract java.lang.Class connectionFactoryImpl()
meth public abstract java.lang.Class connectionImpl()

CLSS public abstract interface !annotation jakarta.resource.spi.ConnectionDefinitions
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.resource.spi.ConnectionDefinition[] value()

CLSS public jakarta.resource.spi.ConnectionEvent
cons public init(jakarta.resource.spi.ManagedConnection,int)
cons public init(jakarta.resource.spi.ManagedConnection,int,java.lang.Exception)
fld protected int id
fld public final static int CONNECTION_CLOSED = 1
fld public final static int CONNECTION_ERROR_OCCURRED = 5
fld public final static int LOCAL_TRANSACTION_COMMITTED = 3
fld public final static int LOCAL_TRANSACTION_ROLLEDBACK = 4
fld public final static int LOCAL_TRANSACTION_STARTED = 2
meth public int getId()
meth public java.lang.Exception getException()
meth public java.lang.Object getConnectionHandle()
meth public void setConnectionHandle(java.lang.Object)
supr java.util.EventObject
hfds connectionHandle,exception

CLSS public abstract interface jakarta.resource.spi.ConnectionEventListener
intf java.util.EventListener
meth public abstract void connectionClosed(jakarta.resource.spi.ConnectionEvent)
meth public abstract void connectionErrorOccurred(jakarta.resource.spi.ConnectionEvent)
meth public abstract void localTransactionCommitted(jakarta.resource.spi.ConnectionEvent)
meth public abstract void localTransactionRolledback(jakarta.resource.spi.ConnectionEvent)
meth public abstract void localTransactionStarted(jakarta.resource.spi.ConnectionEvent)

CLSS public abstract interface jakarta.resource.spi.ConnectionManager
intf java.io.Serializable
meth public abstract java.lang.Object allocateConnection(jakarta.resource.spi.ManagedConnectionFactory,jakarta.resource.spi.ConnectionRequestInfo) throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.ConnectionRequestInfo
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()

CLSS public abstract interface !annotation jakarta.resource.spi.Connector
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean licenseRequired()
meth public abstract !hasdefault boolean reauthenticationSupport()
meth public abstract !hasdefault jakarta.resource.spi.AuthenticationMechanism[] authMechanisms()
meth public abstract !hasdefault jakarta.resource.spi.SecurityPermission[] securityPermissions()
meth public abstract !hasdefault jakarta.resource.spi.TransactionSupport$TransactionSupportLevel transactionSupport()
meth public abstract !hasdefault java.lang.Class<? extends jakarta.resource.spi.work.WorkContext>[] requiredWorkContexts()
meth public abstract !hasdefault java.lang.String eisType()
meth public abstract !hasdefault java.lang.String vendorName()
meth public abstract !hasdefault java.lang.String version()
meth public abstract !hasdefault java.lang.String[] description()
meth public abstract !hasdefault java.lang.String[] displayName()
meth public abstract !hasdefault java.lang.String[] largeIcon()
meth public abstract !hasdefault java.lang.String[] licenseDescription()
meth public abstract !hasdefault java.lang.String[] smallIcon()

CLSS public abstract interface jakarta.resource.spi.DissociatableManagedConnection
meth public abstract void dissociateConnections() throws jakarta.resource.ResourceException

CLSS public jakarta.resource.spi.EISSystemException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public jakarta.resource.spi.IllegalStateException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public jakarta.resource.spi.InvalidPropertyException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
meth public java.beans.PropertyDescriptor[] getInvalidPropertyDescriptors()
meth public void setInvalidPropertyDescriptors(java.beans.PropertyDescriptor[])
supr jakarta.resource.ResourceException
hfds invalidProperties

CLSS public abstract interface jakarta.resource.spi.LazyAssociatableConnectionManager
meth public abstract void associateConnection(java.lang.Object,jakarta.resource.spi.ManagedConnectionFactory,jakarta.resource.spi.ConnectionRequestInfo) throws jakarta.resource.ResourceException
meth public abstract void inactiveConnectionClosed(java.lang.Object,jakarta.resource.spi.ManagedConnectionFactory)

CLSS public abstract interface jakarta.resource.spi.LazyEnlistableConnectionManager
meth public abstract void lazyEnlist(jakarta.resource.spi.ManagedConnection) throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.LazyEnlistableManagedConnection

CLSS public abstract interface jakarta.resource.spi.LocalTransaction
meth public abstract void begin() throws jakarta.resource.ResourceException
meth public abstract void commit() throws jakarta.resource.ResourceException
meth public abstract void rollback() throws jakarta.resource.ResourceException

CLSS public jakarta.resource.spi.LocalTransactionException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.ManagedConnection
meth public abstract jakarta.resource.spi.LocalTransaction getLocalTransaction() throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.spi.ManagedConnectionMetaData getMetaData() throws jakarta.resource.ResourceException
meth public abstract java.io.PrintWriter getLogWriter() throws jakarta.resource.ResourceException
meth public abstract java.lang.Object getConnection(javax.security.auth.Subject,jakarta.resource.spi.ConnectionRequestInfo) throws jakarta.resource.ResourceException
meth public abstract javax.transaction.xa.XAResource getXAResource() throws jakarta.resource.ResourceException
meth public abstract void addConnectionEventListener(jakarta.resource.spi.ConnectionEventListener)
meth public abstract void associateConnection(java.lang.Object) throws jakarta.resource.ResourceException
meth public abstract void cleanup() throws jakarta.resource.ResourceException
meth public abstract void destroy() throws jakarta.resource.ResourceException
meth public abstract void removeConnectionEventListener(jakarta.resource.spi.ConnectionEventListener)
meth public abstract void setLogWriter(java.io.PrintWriter) throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.ManagedConnectionFactory
intf java.io.Serializable
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()
meth public abstract jakarta.resource.spi.ManagedConnection createManagedConnection(javax.security.auth.Subject,jakarta.resource.spi.ConnectionRequestInfo) throws jakarta.resource.ResourceException
meth public abstract jakarta.resource.spi.ManagedConnection matchManagedConnections(java.util.Set,javax.security.auth.Subject,jakarta.resource.spi.ConnectionRequestInfo) throws jakarta.resource.ResourceException
meth public abstract java.io.PrintWriter getLogWriter() throws jakarta.resource.ResourceException
meth public abstract java.lang.Object createConnectionFactory() throws jakarta.resource.ResourceException
meth public abstract java.lang.Object createConnectionFactory(jakarta.resource.spi.ConnectionManager) throws jakarta.resource.ResourceException
meth public abstract void setLogWriter(java.io.PrintWriter) throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.ManagedConnectionMetaData
meth public abstract int getMaxConnections() throws jakarta.resource.ResourceException
meth public abstract java.lang.String getEISProductName() throws jakarta.resource.ResourceException
meth public abstract java.lang.String getEISProductVersion() throws jakarta.resource.ResourceException
meth public abstract java.lang.String getUserName() throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.ResourceAdapter
meth public abstract javax.transaction.xa.XAResource[] getXAResources(jakarta.resource.spi.ActivationSpec[]) throws jakarta.resource.ResourceException
meth public abstract void endpointActivation(jakarta.resource.spi.endpoint.MessageEndpointFactory,jakarta.resource.spi.ActivationSpec) throws jakarta.resource.ResourceException
meth public abstract void endpointDeactivation(jakarta.resource.spi.endpoint.MessageEndpointFactory,jakarta.resource.spi.ActivationSpec)
meth public abstract void start(jakarta.resource.spi.BootstrapContext) throws jakarta.resource.spi.ResourceAdapterInternalException
meth public abstract void stop()

CLSS public abstract interface jakarta.resource.spi.ResourceAdapterAssociation
meth public abstract jakarta.resource.spi.ResourceAdapter getResourceAdapter()
meth public abstract void setResourceAdapter(jakarta.resource.spi.ResourceAdapter) throws jakarta.resource.ResourceException

CLSS public jakarta.resource.spi.ResourceAdapterInternalException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public jakarta.resource.spi.ResourceAllocationException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.RetryableException
intf java.io.Serializable

CLSS public jakarta.resource.spi.RetryableUnavailableException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
intf jakarta.resource.spi.RetryableException
supr jakarta.resource.spi.UnavailableException
hfds serialVersionUID

CLSS public jakarta.resource.spi.SecurityException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public abstract interface !annotation jakarta.resource.spi.SecurityPermission
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String permissionSpec()
meth public abstract !hasdefault java.lang.String[] description()

CLSS public jakarta.resource.spi.SharingViolationException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.TransactionSupport
innr public final static !enum TransactionSupportLevel
intf java.io.Serializable
meth public abstract jakarta.resource.spi.TransactionSupport$TransactionSupportLevel getTransactionSupport()

CLSS public final static !enum jakarta.resource.spi.TransactionSupport$TransactionSupportLevel
 outer jakarta.resource.spi.TransactionSupport
fld public final static jakarta.resource.spi.TransactionSupport$TransactionSupportLevel LocalTransaction
fld public final static jakarta.resource.spi.TransactionSupport$TransactionSupportLevel NoTransaction
fld public final static jakarta.resource.spi.TransactionSupport$TransactionSupportLevel XATransaction
meth public static jakarta.resource.spi.TransactionSupport$TransactionSupportLevel valueOf(java.lang.String)
meth public static jakarta.resource.spi.TransactionSupport$TransactionSupportLevel[] values()
supr java.lang.Enum<jakarta.resource.spi.TransactionSupport$TransactionSupportLevel>

CLSS public jakarta.resource.spi.UnavailableException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.ValidatingManagedConnectionFactory
meth public abstract java.util.Set getInvalidConnections(java.util.Set) throws jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.XATerminator
meth public abstract int prepare(javax.transaction.xa.Xid) throws javax.transaction.xa.XAException
meth public abstract javax.transaction.xa.Xid[] recover(int) throws javax.transaction.xa.XAException
meth public abstract void commit(javax.transaction.xa.Xid,boolean) throws javax.transaction.xa.XAException
meth public abstract void forget(javax.transaction.xa.Xid) throws javax.transaction.xa.XAException
meth public abstract void rollback(javax.transaction.xa.Xid) throws javax.transaction.xa.XAException

CLSS public abstract interface jakarta.resource.spi.endpoint.MessageEndpoint
meth public abstract void afterDelivery() throws jakarta.resource.ResourceException
meth public abstract void beforeDelivery(java.lang.reflect.Method) throws jakarta.resource.ResourceException,java.lang.NoSuchMethodException
meth public abstract void release()

CLSS public abstract interface jakarta.resource.spi.endpoint.MessageEndpointFactory
meth public abstract boolean isDeliveryTransacted(java.lang.reflect.Method) throws java.lang.NoSuchMethodException
meth public abstract jakarta.resource.spi.endpoint.MessageEndpoint createEndpoint(javax.transaction.xa.XAResource) throws jakarta.resource.spi.UnavailableException
meth public abstract jakarta.resource.spi.endpoint.MessageEndpoint createEndpoint(javax.transaction.xa.XAResource,long) throws jakarta.resource.spi.UnavailableException
meth public abstract java.lang.Class<?> getEndpointClass()
meth public abstract java.lang.String getActivationName()

CLSS public abstract interface jakarta.resource.spi.security.GenericCredential
meth public abstract boolean equals(java.lang.Object)
meth public abstract byte[] getCredentialData() throws jakarta.resource.spi.SecurityException
meth public abstract int hashCode()
meth public abstract java.lang.String getMechType()
meth public abstract java.lang.String getName()

CLSS public final jakarta.resource.spi.security.PasswordCredential
cons public init(java.lang.String,char[])
intf java.io.Serializable
meth public boolean equals(java.lang.Object)
meth public char[] getPassword()
meth public int hashCode()
meth public jakarta.resource.spi.ManagedConnectionFactory getManagedConnectionFactory()
meth public java.lang.String getUserName()
meth public void setManagedConnectionFactory(jakarta.resource.spi.ManagedConnectionFactory)
supr java.lang.Object
hfds mcf,password,userName

CLSS public abstract interface jakarta.resource.spi.work.DistributableWork
intf jakarta.resource.spi.work.Work
intf java.io.Serializable

CLSS public abstract interface jakarta.resource.spi.work.DistributableWorkManager
intf jakarta.resource.spi.work.WorkManager

CLSS public jakarta.resource.spi.work.ExecutionContext
cons public init()
meth public javax.transaction.xa.Xid getXid()
meth public long getTransactionTimeout()
meth public void setTransactionTimeout(long) throws jakarta.resource.NotSupportedException
meth public void setXid(javax.transaction.xa.Xid)
supr java.lang.Object
hfds transactionTimeout,xid

CLSS public jakarta.resource.spi.work.HintsContext
cons public init()
fld protected java.lang.String description
fld protected java.lang.String name
fld public final static java.lang.String LONGRUNNING_HINT = "jakarta.resource.LongRunning"
fld public final static java.lang.String NAME_HINT = "jakarta.resource.Name"
intf jakarta.resource.spi.work.WorkContext
meth public java.lang.String getDescription()
meth public java.lang.String getName()
meth public java.util.Map<java.lang.String,java.io.Serializable> getHints()
meth public void setDescription(java.lang.String)
meth public void setHint(java.lang.String,java.io.Serializable)
meth public void setName(java.lang.String)
supr java.lang.Object
hfds hints,serialVersionUID

CLSS public jakarta.resource.spi.work.RetryableWorkRejectedException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
intf jakarta.resource.spi.RetryableException
supr jakarta.resource.spi.work.WorkRejectedException
hfds serialVersionUID

CLSS public abstract jakarta.resource.spi.work.SecurityContext
cons public init()
intf jakarta.resource.spi.work.WorkContext
meth public abstract void setupSecurityContext(javax.security.auth.callback.CallbackHandler,javax.security.auth.Subject,javax.security.auth.Subject)
meth public java.lang.String getDescription()
meth public java.lang.String getName()
supr java.lang.Object
hfds serialVersionUID

CLSS public jakarta.resource.spi.work.TransactionContext
cons public init()
intf jakarta.resource.spi.work.WorkContext
meth public java.lang.String getDescription()
meth public java.lang.String getName()
supr jakarta.resource.spi.work.ExecutionContext
hfds serialVersionUID

CLSS public abstract interface jakarta.resource.spi.work.Work
intf java.lang.Runnable
meth public abstract void release()

CLSS public jakarta.resource.spi.work.WorkAdapter
cons public init()
intf jakarta.resource.spi.work.WorkListener
meth public void workAccepted(jakarta.resource.spi.work.WorkEvent)
meth public void workCompleted(jakarta.resource.spi.work.WorkEvent)
meth public void workRejected(jakarta.resource.spi.work.WorkEvent)
meth public void workStarted(jakarta.resource.spi.work.WorkEvent)
supr java.lang.Object

CLSS public jakarta.resource.spi.work.WorkCompletedException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.spi.work.WorkException

CLSS public abstract interface jakarta.resource.spi.work.WorkContext
intf java.io.Serializable
meth public abstract java.lang.String getDescription()
meth public abstract java.lang.String getName()

CLSS public jakarta.resource.spi.work.WorkContextErrorCodes
cons public init()
fld public final static java.lang.String CONTEXT_SETUP_FAILED = "3"
fld public final static java.lang.String CONTEXT_SETUP_UNSUPPORTED = "4"
fld public final static java.lang.String DUPLICATE_CONTEXTS = "2"
fld public final static java.lang.String UNSUPPORTED_CONTEXT_TYPE = "1"
supr java.lang.Object

CLSS public abstract interface jakarta.resource.spi.work.WorkContextLifecycleListener
meth public abstract void contextSetupComplete()
meth public abstract void contextSetupFailed(java.lang.String)

CLSS public abstract interface jakarta.resource.spi.work.WorkContextProvider
intf java.io.Serializable
meth public abstract java.util.List<jakarta.resource.spi.work.WorkContext> getWorkContexts()

CLSS public jakarta.resource.spi.work.WorkEvent
cons public init(java.lang.Object,int,jakarta.resource.spi.work.Work,jakarta.resource.spi.work.WorkException)
cons public init(java.lang.Object,int,jakarta.resource.spi.work.Work,jakarta.resource.spi.work.WorkException,long)
fld public final static int WORK_ACCEPTED = 1
fld public final static int WORK_COMPLETED = 4
fld public final static int WORK_REJECTED = 2
fld public final static int WORK_STARTED = 3
meth public int getType()
meth public jakarta.resource.spi.work.Work getWork()
meth public jakarta.resource.spi.work.WorkException getException()
meth public long getStartDuration()
supr java.util.EventObject
hfds exc,startDuration,type,work

CLSS public jakarta.resource.spi.work.WorkException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
fld public final static java.lang.String INTERNAL = "-1"
fld public final static java.lang.String START_TIMED_OUT = "1"
fld public final static java.lang.String TX_CONCURRENT_WORK_DISALLOWED = "2"
fld public final static java.lang.String TX_RECREATE_FAILED = "3"
fld public final static java.lang.String UNDEFINED = "0"
supr jakarta.resource.ResourceException

CLSS public abstract interface jakarta.resource.spi.work.WorkListener
intf java.util.EventListener
meth public abstract void workAccepted(jakarta.resource.spi.work.WorkEvent)
meth public abstract void workCompleted(jakarta.resource.spi.work.WorkEvent)
meth public abstract void workRejected(jakarta.resource.spi.work.WorkEvent)
meth public abstract void workStarted(jakarta.resource.spi.work.WorkEvent)

CLSS public abstract interface jakarta.resource.spi.work.WorkManager
fld public final static long IMMEDIATE = 0
fld public final static long INDEFINITE = 9223372036854775807
fld public final static long UNKNOWN = -1
meth public abstract long startWork(jakarta.resource.spi.work.Work) throws jakarta.resource.spi.work.WorkException
meth public abstract long startWork(jakarta.resource.spi.work.Work,long,jakarta.resource.spi.work.ExecutionContext,jakarta.resource.spi.work.WorkListener) throws jakarta.resource.spi.work.WorkException
meth public abstract void doWork(jakarta.resource.spi.work.Work) throws jakarta.resource.spi.work.WorkException
meth public abstract void doWork(jakarta.resource.spi.work.Work,long,jakarta.resource.spi.work.ExecutionContext,jakarta.resource.spi.work.WorkListener) throws jakarta.resource.spi.work.WorkException
meth public abstract void scheduleWork(jakarta.resource.spi.work.Work) throws jakarta.resource.spi.work.WorkException
meth public abstract void scheduleWork(jakarta.resource.spi.work.Work,long,jakarta.resource.spi.work.ExecutionContext,jakarta.resource.spi.work.WorkListener) throws jakarta.resource.spi.work.WorkException

CLSS public jakarta.resource.spi.work.WorkRejectedException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.resource.spi.work.WorkException

CLSS public abstract interface java.io.Serializable

CLSS public abstract interface java.lang.AutoCloseable
meth public abstract void close() throws java.lang.Exception

CLSS public abstract interface java.lang.Cloneable

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

CLSS public abstract interface java.lang.Iterable<%0 extends java.lang.Object>
meth public abstract java.util.Iterator<{java.lang.Iterable%0}> iterator()
meth public java.util.Spliterator<{java.lang.Iterable%0}> spliterator()
meth public void forEach(java.util.function.Consumer<? super {java.lang.Iterable%0}>)

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

CLSS public abstract interface java.sql.ResultSet
fld public final static int CLOSE_CURSORS_AT_COMMIT = 2
fld public final static int CONCUR_READ_ONLY = 1007
fld public final static int CONCUR_UPDATABLE = 1008
fld public final static int FETCH_FORWARD = 1000
fld public final static int FETCH_REVERSE = 1001
fld public final static int FETCH_UNKNOWN = 1002
fld public final static int HOLD_CURSORS_OVER_COMMIT = 1
fld public final static int TYPE_FORWARD_ONLY = 1003
fld public final static int TYPE_SCROLL_INSENSITIVE = 1004
fld public final static int TYPE_SCROLL_SENSITIVE = 1005
intf java.lang.AutoCloseable
intf java.sql.Wrapper
meth public abstract <%0 extends java.lang.Object> {%%0} getObject(int,java.lang.Class<{%%0}>) throws java.sql.SQLException
meth public abstract <%0 extends java.lang.Object> {%%0} getObject(java.lang.String,java.lang.Class<{%%0}>) throws java.sql.SQLException
meth public abstract boolean absolute(int) throws java.sql.SQLException
meth public abstract boolean first() throws java.sql.SQLException
meth public abstract boolean getBoolean(int) throws java.sql.SQLException
meth public abstract boolean getBoolean(java.lang.String) throws java.sql.SQLException
meth public abstract boolean isAfterLast() throws java.sql.SQLException
meth public abstract boolean isBeforeFirst() throws java.sql.SQLException
meth public abstract boolean isClosed() throws java.sql.SQLException
meth public abstract boolean isFirst() throws java.sql.SQLException
meth public abstract boolean isLast() throws java.sql.SQLException
meth public abstract boolean last() throws java.sql.SQLException
meth public abstract boolean next() throws java.sql.SQLException
meth public abstract boolean previous() throws java.sql.SQLException
meth public abstract boolean relative(int) throws java.sql.SQLException
meth public abstract boolean rowDeleted() throws java.sql.SQLException
meth public abstract boolean rowInserted() throws java.sql.SQLException
meth public abstract boolean rowUpdated() throws java.sql.SQLException
meth public abstract boolean wasNull() throws java.sql.SQLException
meth public abstract byte getByte(int) throws java.sql.SQLException
meth public abstract byte getByte(java.lang.String) throws java.sql.SQLException
meth public abstract byte[] getBytes(int) throws java.sql.SQLException
meth public abstract byte[] getBytes(java.lang.String) throws java.sql.SQLException
meth public abstract double getDouble(int) throws java.sql.SQLException
meth public abstract double getDouble(java.lang.String) throws java.sql.SQLException
meth public abstract float getFloat(int) throws java.sql.SQLException
meth public abstract float getFloat(java.lang.String) throws java.sql.SQLException
meth public abstract int findColumn(java.lang.String) throws java.sql.SQLException
meth public abstract int getConcurrency() throws java.sql.SQLException
meth public abstract int getFetchDirection() throws java.sql.SQLException
meth public abstract int getFetchSize() throws java.sql.SQLException
meth public abstract int getHoldability() throws java.sql.SQLException
meth public abstract int getInt(int) throws java.sql.SQLException
meth public abstract int getInt(java.lang.String) throws java.sql.SQLException
meth public abstract int getRow() throws java.sql.SQLException
meth public abstract int getType() throws java.sql.SQLException
meth public abstract java.io.InputStream getAsciiStream(int) throws java.sql.SQLException
meth public abstract java.io.InputStream getAsciiStream(java.lang.String) throws java.sql.SQLException
meth public abstract java.io.InputStream getBinaryStream(int) throws java.sql.SQLException
meth public abstract java.io.InputStream getBinaryStream(java.lang.String) throws java.sql.SQLException
meth public abstract java.io.InputStream getUnicodeStream(int) throws java.sql.SQLException
 anno 0 java.lang.Deprecated(boolean forRemoval=false, java.lang.String since="1.2")
meth public abstract java.io.InputStream getUnicodeStream(java.lang.String) throws java.sql.SQLException
 anno 0 java.lang.Deprecated(boolean forRemoval=false, java.lang.String since="1.2")
meth public abstract java.io.Reader getCharacterStream(int) throws java.sql.SQLException
meth public abstract java.io.Reader getCharacterStream(java.lang.String) throws java.sql.SQLException
meth public abstract java.io.Reader getNCharacterStream(int) throws java.sql.SQLException
meth public abstract java.io.Reader getNCharacterStream(java.lang.String) throws java.sql.SQLException
meth public abstract java.lang.Object getObject(int) throws java.sql.SQLException
meth public abstract java.lang.Object getObject(int,java.util.Map<java.lang.String,java.lang.Class<?>>) throws java.sql.SQLException
meth public abstract java.lang.Object getObject(java.lang.String) throws java.sql.SQLException
meth public abstract java.lang.Object getObject(java.lang.String,java.util.Map<java.lang.String,java.lang.Class<?>>) throws java.sql.SQLException
meth public abstract java.lang.String getCursorName() throws java.sql.SQLException
meth public abstract java.lang.String getNString(int) throws java.sql.SQLException
meth public abstract java.lang.String getNString(java.lang.String) throws java.sql.SQLException
meth public abstract java.lang.String getString(int) throws java.sql.SQLException
meth public abstract java.lang.String getString(java.lang.String) throws java.sql.SQLException
meth public abstract java.math.BigDecimal getBigDecimal(int) throws java.sql.SQLException
meth public abstract java.math.BigDecimal getBigDecimal(int,int) throws java.sql.SQLException
 anno 0 java.lang.Deprecated(boolean forRemoval=false, java.lang.String since="1.2")
meth public abstract java.math.BigDecimal getBigDecimal(java.lang.String) throws java.sql.SQLException
meth public abstract java.math.BigDecimal getBigDecimal(java.lang.String,int) throws java.sql.SQLException
 anno 0 java.lang.Deprecated(boolean forRemoval=false, java.lang.String since="1.2")
meth public abstract java.net.URL getURL(int) throws java.sql.SQLException
meth public abstract java.net.URL getURL(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.Array getArray(int) throws java.sql.SQLException
meth public abstract java.sql.Array getArray(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.Blob getBlob(int) throws java.sql.SQLException
meth public abstract java.sql.Blob getBlob(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.Clob getClob(int) throws java.sql.SQLException
meth public abstract java.sql.Clob getClob(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.Date getDate(int) throws java.sql.SQLException
meth public abstract java.sql.Date getDate(int,java.util.Calendar) throws java.sql.SQLException
meth public abstract java.sql.Date getDate(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.Date getDate(java.lang.String,java.util.Calendar) throws java.sql.SQLException
meth public abstract java.sql.NClob getNClob(int) throws java.sql.SQLException
meth public abstract java.sql.NClob getNClob(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.Ref getRef(int) throws java.sql.SQLException
meth public abstract java.sql.Ref getRef(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.ResultSetMetaData getMetaData() throws java.sql.SQLException
meth public abstract java.sql.RowId getRowId(int) throws java.sql.SQLException
meth public abstract java.sql.RowId getRowId(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.SQLWarning getWarnings() throws java.sql.SQLException
meth public abstract java.sql.SQLXML getSQLXML(int) throws java.sql.SQLException
meth public abstract java.sql.SQLXML getSQLXML(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.Statement getStatement() throws java.sql.SQLException
meth public abstract java.sql.Time getTime(int) throws java.sql.SQLException
meth public abstract java.sql.Time getTime(int,java.util.Calendar) throws java.sql.SQLException
meth public abstract java.sql.Time getTime(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.Time getTime(java.lang.String,java.util.Calendar) throws java.sql.SQLException
meth public abstract java.sql.Timestamp getTimestamp(int) throws java.sql.SQLException
meth public abstract java.sql.Timestamp getTimestamp(int,java.util.Calendar) throws java.sql.SQLException
meth public abstract java.sql.Timestamp getTimestamp(java.lang.String) throws java.sql.SQLException
meth public abstract java.sql.Timestamp getTimestamp(java.lang.String,java.util.Calendar) throws java.sql.SQLException
meth public abstract long getLong(int) throws java.sql.SQLException
meth public abstract long getLong(java.lang.String) throws java.sql.SQLException
meth public abstract short getShort(int) throws java.sql.SQLException
meth public abstract short getShort(java.lang.String) throws java.sql.SQLException
meth public abstract void afterLast() throws java.sql.SQLException
meth public abstract void beforeFirst() throws java.sql.SQLException
meth public abstract void cancelRowUpdates() throws java.sql.SQLException
meth public abstract void clearWarnings() throws java.sql.SQLException
meth public abstract void close() throws java.sql.SQLException
meth public abstract void deleteRow() throws java.sql.SQLException
meth public abstract void insertRow() throws java.sql.SQLException
meth public abstract void moveToCurrentRow() throws java.sql.SQLException
meth public abstract void moveToInsertRow() throws java.sql.SQLException
meth public abstract void refreshRow() throws java.sql.SQLException
meth public abstract void setFetchDirection(int) throws java.sql.SQLException
meth public abstract void setFetchSize(int) throws java.sql.SQLException
meth public abstract void updateArray(int,java.sql.Array) throws java.sql.SQLException
meth public abstract void updateArray(java.lang.String,java.sql.Array) throws java.sql.SQLException
meth public abstract void updateAsciiStream(int,java.io.InputStream) throws java.sql.SQLException
meth public abstract void updateAsciiStream(int,java.io.InputStream,int) throws java.sql.SQLException
meth public abstract void updateAsciiStream(int,java.io.InputStream,long) throws java.sql.SQLException
meth public abstract void updateAsciiStream(java.lang.String,java.io.InputStream) throws java.sql.SQLException
meth public abstract void updateAsciiStream(java.lang.String,java.io.InputStream,int) throws java.sql.SQLException
meth public abstract void updateAsciiStream(java.lang.String,java.io.InputStream,long) throws java.sql.SQLException
meth public abstract void updateBigDecimal(int,java.math.BigDecimal) throws java.sql.SQLException
meth public abstract void updateBigDecimal(java.lang.String,java.math.BigDecimal) throws java.sql.SQLException
meth public abstract void updateBinaryStream(int,java.io.InputStream) throws java.sql.SQLException
meth public abstract void updateBinaryStream(int,java.io.InputStream,int) throws java.sql.SQLException
meth public abstract void updateBinaryStream(int,java.io.InputStream,long) throws java.sql.SQLException
meth public abstract void updateBinaryStream(java.lang.String,java.io.InputStream) throws java.sql.SQLException
meth public abstract void updateBinaryStream(java.lang.String,java.io.InputStream,int) throws java.sql.SQLException
meth public abstract void updateBinaryStream(java.lang.String,java.io.InputStream,long) throws java.sql.SQLException
meth public abstract void updateBlob(int,java.io.InputStream) throws java.sql.SQLException
meth public abstract void updateBlob(int,java.io.InputStream,long) throws java.sql.SQLException
meth public abstract void updateBlob(int,java.sql.Blob) throws java.sql.SQLException
meth public abstract void updateBlob(java.lang.String,java.io.InputStream) throws java.sql.SQLException
meth public abstract void updateBlob(java.lang.String,java.io.InputStream,long) throws java.sql.SQLException
meth public abstract void updateBlob(java.lang.String,java.sql.Blob) throws java.sql.SQLException
meth public abstract void updateBoolean(int,boolean) throws java.sql.SQLException
meth public abstract void updateBoolean(java.lang.String,boolean) throws java.sql.SQLException
meth public abstract void updateByte(int,byte) throws java.sql.SQLException
meth public abstract void updateByte(java.lang.String,byte) throws java.sql.SQLException
meth public abstract void updateBytes(int,byte[]) throws java.sql.SQLException
meth public abstract void updateBytes(java.lang.String,byte[]) throws java.sql.SQLException
meth public abstract void updateCharacterStream(int,java.io.Reader) throws java.sql.SQLException
meth public abstract void updateCharacterStream(int,java.io.Reader,int) throws java.sql.SQLException
meth public abstract void updateCharacterStream(int,java.io.Reader,long) throws java.sql.SQLException
meth public abstract void updateCharacterStream(java.lang.String,java.io.Reader) throws java.sql.SQLException
meth public abstract void updateCharacterStream(java.lang.String,java.io.Reader,int) throws java.sql.SQLException
meth public abstract void updateCharacterStream(java.lang.String,java.io.Reader,long) throws java.sql.SQLException
meth public abstract void updateClob(int,java.io.Reader) throws java.sql.SQLException
meth public abstract void updateClob(int,java.io.Reader,long) throws java.sql.SQLException
meth public abstract void updateClob(int,java.sql.Clob) throws java.sql.SQLException
meth public abstract void updateClob(java.lang.String,java.io.Reader) throws java.sql.SQLException
meth public abstract void updateClob(java.lang.String,java.io.Reader,long) throws java.sql.SQLException
meth public abstract void updateClob(java.lang.String,java.sql.Clob) throws java.sql.SQLException
meth public abstract void updateDate(int,java.sql.Date) throws java.sql.SQLException
meth public abstract void updateDate(java.lang.String,java.sql.Date) throws java.sql.SQLException
meth public abstract void updateDouble(int,double) throws java.sql.SQLException
meth public abstract void updateDouble(java.lang.String,double) throws java.sql.SQLException
meth public abstract void updateFloat(int,float) throws java.sql.SQLException
meth public abstract void updateFloat(java.lang.String,float) throws java.sql.SQLException
meth public abstract void updateInt(int,int) throws java.sql.SQLException
meth public abstract void updateInt(java.lang.String,int) throws java.sql.SQLException
meth public abstract void updateLong(int,long) throws java.sql.SQLException
meth public abstract void updateLong(java.lang.String,long) throws java.sql.SQLException
meth public abstract void updateNCharacterStream(int,java.io.Reader) throws java.sql.SQLException
meth public abstract void updateNCharacterStream(int,java.io.Reader,long) throws java.sql.SQLException
meth public abstract void updateNCharacterStream(java.lang.String,java.io.Reader) throws java.sql.SQLException
meth public abstract void updateNCharacterStream(java.lang.String,java.io.Reader,long) throws java.sql.SQLException
meth public abstract void updateNClob(int,java.io.Reader) throws java.sql.SQLException
meth public abstract void updateNClob(int,java.io.Reader,long) throws java.sql.SQLException
meth public abstract void updateNClob(int,java.sql.NClob) throws java.sql.SQLException
meth public abstract void updateNClob(java.lang.String,java.io.Reader) throws java.sql.SQLException
meth public abstract void updateNClob(java.lang.String,java.io.Reader,long) throws java.sql.SQLException
meth public abstract void updateNClob(java.lang.String,java.sql.NClob) throws java.sql.SQLException
meth public abstract void updateNString(int,java.lang.String) throws java.sql.SQLException
meth public abstract void updateNString(java.lang.String,java.lang.String) throws java.sql.SQLException
meth public abstract void updateNull(int) throws java.sql.SQLException
meth public abstract void updateNull(java.lang.String) throws java.sql.SQLException
meth public abstract void updateObject(int,java.lang.Object) throws java.sql.SQLException
meth public abstract void updateObject(int,java.lang.Object,int) throws java.sql.SQLException
meth public abstract void updateObject(java.lang.String,java.lang.Object) throws java.sql.SQLException
meth public abstract void updateObject(java.lang.String,java.lang.Object,int) throws java.sql.SQLException
meth public abstract void updateRef(int,java.sql.Ref) throws java.sql.SQLException
meth public abstract void updateRef(java.lang.String,java.sql.Ref) throws java.sql.SQLException
meth public abstract void updateRow() throws java.sql.SQLException
meth public abstract void updateRowId(int,java.sql.RowId) throws java.sql.SQLException
meth public abstract void updateRowId(java.lang.String,java.sql.RowId) throws java.sql.SQLException
meth public abstract void updateSQLXML(int,java.sql.SQLXML) throws java.sql.SQLException
meth public abstract void updateSQLXML(java.lang.String,java.sql.SQLXML) throws java.sql.SQLException
meth public abstract void updateShort(int,short) throws java.sql.SQLException
meth public abstract void updateShort(java.lang.String,short) throws java.sql.SQLException
meth public abstract void updateString(int,java.lang.String) throws java.sql.SQLException
meth public abstract void updateString(java.lang.String,java.lang.String) throws java.sql.SQLException
meth public abstract void updateTime(int,java.sql.Time) throws java.sql.SQLException
meth public abstract void updateTime(java.lang.String,java.sql.Time) throws java.sql.SQLException
meth public abstract void updateTimestamp(int,java.sql.Timestamp) throws java.sql.SQLException
meth public abstract void updateTimestamp(java.lang.String,java.sql.Timestamp) throws java.sql.SQLException
meth public void updateObject(int,java.lang.Object,java.sql.SQLType) throws java.sql.SQLException
meth public void updateObject(int,java.lang.Object,java.sql.SQLType,int) throws java.sql.SQLException
meth public void updateObject(java.lang.String,java.lang.Object,java.sql.SQLType) throws java.sql.SQLException
meth public void updateObject(java.lang.String,java.lang.Object,java.sql.SQLType,int) throws java.sql.SQLException

CLSS public abstract interface java.sql.Wrapper
meth public abstract <%0 extends java.lang.Object> {%%0} unwrap(java.lang.Class<{%%0}>) throws java.sql.SQLException
meth public abstract boolean isWrapperFor(java.lang.Class<?>) throws java.sql.SQLException

CLSS public abstract interface java.util.Collection<%0 extends java.lang.Object>
intf java.lang.Iterable<{java.util.Collection%0}>
meth public <%0 extends java.lang.Object> {%%0}[] toArray(java.util.function.IntFunction<{%%0}[]>)
meth public abstract <%0 extends java.lang.Object> {%%0}[] toArray({%%0}[])
meth public abstract boolean add({java.util.Collection%0})
meth public abstract boolean addAll(java.util.Collection<? extends {java.util.Collection%0}>)
meth public abstract boolean contains(java.lang.Object)
meth public abstract boolean containsAll(java.util.Collection<?>)
meth public abstract boolean equals(java.lang.Object)
meth public abstract boolean isEmpty()
meth public abstract boolean remove(java.lang.Object)
meth public abstract boolean removeAll(java.util.Collection<?>)
meth public abstract boolean retainAll(java.util.Collection<?>)
meth public abstract int hashCode()
meth public abstract int size()
meth public abstract java.lang.Object[] toArray()
meth public abstract java.util.Iterator<{java.util.Collection%0}> iterator()
meth public abstract void clear()
meth public boolean removeIf(java.util.function.Predicate<? super {java.util.Collection%0}>)
meth public java.util.Spliterator<{java.util.Collection%0}> spliterator()
meth public java.util.stream.Stream<{java.util.Collection%0}> parallelStream()
meth public java.util.stream.Stream<{java.util.Collection%0}> stream()

CLSS public abstract interface java.util.EventListener

CLSS public java.util.EventObject
cons public init(java.lang.Object)
fld protected java.lang.Object source
intf java.io.Serializable
meth public java.lang.Object getSource()
meth public java.lang.String toString()
supr java.lang.Object

CLSS public abstract interface java.util.List<%0 extends java.lang.Object>
intf java.util.Collection<{java.util.List%0}>
meth public !varargs static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0}[])
 anno 0 java.lang.SafeVarargs()
meth public abstract <%0 extends java.lang.Object> {%%0}[] toArray({%%0}[])
meth public abstract boolean add({java.util.List%0})
meth public abstract boolean addAll(int,java.util.Collection<? extends {java.util.List%0}>)
meth public abstract boolean addAll(java.util.Collection<? extends {java.util.List%0}>)
meth public abstract boolean contains(java.lang.Object)
meth public abstract boolean containsAll(java.util.Collection<?>)
meth public abstract boolean equals(java.lang.Object)
meth public abstract boolean isEmpty()
meth public abstract boolean remove(java.lang.Object)
meth public abstract boolean removeAll(java.util.Collection<?>)
meth public abstract boolean retainAll(java.util.Collection<?>)
meth public abstract int hashCode()
meth public abstract int indexOf(java.lang.Object)
meth public abstract int lastIndexOf(java.lang.Object)
meth public abstract int size()
meth public abstract java.lang.Object[] toArray()
meth public abstract java.util.Iterator<{java.util.List%0}> iterator()
meth public abstract java.util.List<{java.util.List%0}> subList(int,int)
meth public abstract java.util.ListIterator<{java.util.List%0}> listIterator()
meth public abstract java.util.ListIterator<{java.util.List%0}> listIterator(int)
meth public abstract void add(int,{java.util.List%0})
meth public abstract void clear()
meth public abstract {java.util.List%0} get(int)
meth public abstract {java.util.List%0} remove(int)
meth public abstract {java.util.List%0} set(int,{java.util.List%0})
meth public java.util.Spliterator<{java.util.List%0}> spliterator()
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> copyOf(java.util.Collection<? extends {%%0}>)
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of()
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0})
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.List<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0})
meth public void replaceAll(java.util.function.UnaryOperator<{java.util.List%0}>)
meth public void sort(java.util.Comparator<? super {java.util.List%0}>)

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

CLSS public abstract interface javax.naming.Referenceable
meth public abstract javax.naming.Reference getReference() throws javax.naming.NamingException

