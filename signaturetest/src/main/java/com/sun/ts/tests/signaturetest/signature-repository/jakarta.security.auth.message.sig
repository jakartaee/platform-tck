#Signature file v4.1
#Version 2.0

CLSS public jakarta.security.auth.message.AuthException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr javax.security.auth.login.LoginException
hfds serialVersionUID

CLSS public jakarta.security.auth.message.AuthStatus
fld public final static jakarta.security.auth.message.AuthStatus FAILURE
fld public final static jakarta.security.auth.message.AuthStatus SEND_CONTINUE
fld public final static jakarta.security.auth.message.AuthStatus SEND_FAILURE
fld public final static jakarta.security.auth.message.AuthStatus SEND_SUCCESS
fld public final static jakarta.security.auth.message.AuthStatus SUCCESS
meth public java.lang.String toString()
supr java.lang.Object
hfds value

CLSS public abstract interface jakarta.security.auth.message.ClientAuth
meth public abstract jakarta.security.auth.message.AuthStatus secureRequest(jakarta.security.auth.message.MessageInfo,javax.security.auth.Subject) throws jakarta.security.auth.message.AuthException
meth public jakarta.security.auth.message.AuthStatus validateResponse(jakarta.security.auth.message.MessageInfo,javax.security.auth.Subject,javax.security.auth.Subject) throws jakarta.security.auth.message.AuthException
meth public void cleanSubject(jakarta.security.auth.message.MessageInfo,javax.security.auth.Subject) throws jakarta.security.auth.message.AuthException

CLSS public abstract interface jakarta.security.auth.message.MessageInfo
meth public abstract java.lang.Object getRequestMessage()
meth public abstract java.lang.Object getResponseMessage()
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getMap()
meth public abstract void setRequestMessage(java.lang.Object)
meth public abstract void setResponseMessage(java.lang.Object)

CLSS public jakarta.security.auth.message.MessagePolicy
cons public init(jakarta.security.auth.message.MessagePolicy$TargetPolicy[],boolean)
innr public abstract interface static ProtectionPolicy
innr public abstract interface static Target
innr public static TargetPolicy
meth public boolean isMandatory()
meth public jakarta.security.auth.message.MessagePolicy$TargetPolicy[] getTargetPolicies()
supr java.lang.Object
hfds mandatory,targetPolicies

CLSS public abstract interface static jakarta.security.auth.message.MessagePolicy$ProtectionPolicy
 outer jakarta.security.auth.message.MessagePolicy
fld public final static java.lang.String AUTHENTICATE_CONTENT = "#authenticateContent"
fld public final static java.lang.String AUTHENTICATE_RECIPIENT = "#authenticateRecipient"
fld public final static java.lang.String AUTHENTICATE_SENDER = "#authenticateSender"
meth public abstract java.lang.String getID()

CLSS public abstract interface static jakarta.security.auth.message.MessagePolicy$Target
 outer jakarta.security.auth.message.MessagePolicy
meth public abstract java.lang.Object get(jakarta.security.auth.message.MessageInfo)
meth public abstract void put(jakarta.security.auth.message.MessageInfo,java.lang.Object)
meth public abstract void remove(jakarta.security.auth.message.MessageInfo)

CLSS public static jakarta.security.auth.message.MessagePolicy$TargetPolicy
 outer jakarta.security.auth.message.MessagePolicy
cons public init(jakarta.security.auth.message.MessagePolicy$Target[],jakarta.security.auth.message.MessagePolicy$ProtectionPolicy)
meth public jakarta.security.auth.message.MessagePolicy$ProtectionPolicy getProtectionPolicy()
meth public jakarta.security.auth.message.MessagePolicy$Target[] getTargets()
supr java.lang.Object
hfds protectionPolicy,targets

CLSS public abstract interface jakarta.security.auth.message.ServerAuth
meth public abstract jakarta.security.auth.message.AuthStatus validateRequest(jakarta.security.auth.message.MessageInfo,javax.security.auth.Subject,javax.security.auth.Subject) throws jakarta.security.auth.message.AuthException
meth public jakarta.security.auth.message.AuthStatus secureResponse(jakarta.security.auth.message.MessageInfo,javax.security.auth.Subject) throws jakarta.security.auth.message.AuthException
meth public void cleanSubject(jakarta.security.auth.message.MessageInfo,javax.security.auth.Subject) throws jakarta.security.auth.message.AuthException

CLSS public jakarta.security.auth.message.callback.CallerPrincipalCallback
cons public init(javax.security.auth.Subject,java.lang.String)
cons public init(javax.security.auth.Subject,java.security.Principal)
intf javax.security.auth.callback.Callback
meth public java.lang.String getName()
meth public java.security.Principal getPrincipal()
meth public javax.security.auth.Subject getSubject()
supr java.lang.Object
hfds name,principal,subject

CLSS public jakarta.security.auth.message.callback.CertStoreCallback
cons public init()
intf javax.security.auth.callback.Callback
meth public java.security.cert.CertStore getCertStore()
meth public void setCertStore(java.security.cert.CertStore)
supr java.lang.Object
hfds certStore

CLSS public jakarta.security.auth.message.callback.GroupPrincipalCallback
cons public init(javax.security.auth.Subject,java.lang.String[])
intf javax.security.auth.callback.Callback
meth public java.lang.String[] getGroups()
meth public javax.security.auth.Subject getSubject()
supr java.lang.Object
hfds groups,subject

CLSS public jakarta.security.auth.message.callback.PasswordValidationCallback
cons public init(javax.security.auth.Subject,java.lang.String,char[])
intf javax.security.auth.callback.Callback
meth public boolean getResult()
meth public char[] getPassword()
meth public java.lang.String getUsername()
meth public javax.security.auth.Subject getSubject()
meth public void clearPassword()
meth public void setResult(boolean)
supr java.lang.Object
hfds password,result,subject,username

CLSS public jakarta.security.auth.message.callback.PrivateKeyCallback
cons public init(jakarta.security.auth.message.callback.PrivateKeyCallback$Request)
innr public abstract interface static Request
innr public static AliasRequest
innr public static DigestRequest
innr public static IssuerSerialNumRequest
innr public static SubjectKeyIDRequest
intf javax.security.auth.callback.Callback
meth public jakarta.security.auth.message.callback.PrivateKeyCallback$Request getRequest()
meth public java.security.PrivateKey getKey()
meth public java.security.cert.Certificate[] getChain()
meth public void setKey(java.security.PrivateKey,java.security.cert.Certificate[])
supr java.lang.Object
hfds chain,key,request

CLSS public static jakarta.security.auth.message.callback.PrivateKeyCallback$AliasRequest
 outer jakarta.security.auth.message.callback.PrivateKeyCallback
cons public init(java.lang.String)
intf jakarta.security.auth.message.callback.PrivateKeyCallback$Request
meth public java.lang.String getAlias()
supr java.lang.Object
hfds alias

CLSS public static jakarta.security.auth.message.callback.PrivateKeyCallback$DigestRequest
 outer jakarta.security.auth.message.callback.PrivateKeyCallback
cons public init(byte[],java.lang.String)
intf jakarta.security.auth.message.callback.PrivateKeyCallback$Request
meth public byte[] getDigest()
meth public java.lang.String getAlgorithm()
supr java.lang.Object
hfds algorithm,digest

CLSS public static jakarta.security.auth.message.callback.PrivateKeyCallback$IssuerSerialNumRequest
 outer jakarta.security.auth.message.callback.PrivateKeyCallback
cons public init(javax.security.auth.x500.X500Principal,java.math.BigInteger)
intf jakarta.security.auth.message.callback.PrivateKeyCallback$Request
meth public java.math.BigInteger getSerialNum()
meth public javax.security.auth.x500.X500Principal getIssuer()
supr java.lang.Object
hfds issuer,serialNum

CLSS public abstract interface static jakarta.security.auth.message.callback.PrivateKeyCallback$Request
 outer jakarta.security.auth.message.callback.PrivateKeyCallback

CLSS public static jakarta.security.auth.message.callback.PrivateKeyCallback$SubjectKeyIDRequest
 outer jakarta.security.auth.message.callback.PrivateKeyCallback
cons public init(byte[])
intf jakarta.security.auth.message.callback.PrivateKeyCallback$Request
meth public byte[] getSubjectKeyID()
supr java.lang.Object
hfds id

CLSS public jakarta.security.auth.message.callback.SecretKeyCallback
cons public init(jakarta.security.auth.message.callback.SecretKeyCallback$Request)
innr public abstract interface static Request
innr public static AliasRequest
intf javax.security.auth.callback.Callback
meth public jakarta.security.auth.message.callback.SecretKeyCallback$Request getRequest()
meth public javax.crypto.SecretKey getKey()
meth public void setKey(javax.crypto.SecretKey)
supr java.lang.Object
hfds key,request

CLSS public static jakarta.security.auth.message.callback.SecretKeyCallback$AliasRequest
 outer jakarta.security.auth.message.callback.SecretKeyCallback
cons public init(java.lang.String)
intf jakarta.security.auth.message.callback.SecretKeyCallback$Request
meth public java.lang.String getAlias()
supr java.lang.Object
hfds alias

CLSS public abstract interface static jakarta.security.auth.message.callback.SecretKeyCallback$Request
 outer jakarta.security.auth.message.callback.SecretKeyCallback

CLSS public jakarta.security.auth.message.callback.TrustStoreCallback
cons public init()
intf javax.security.auth.callback.Callback
meth public java.security.KeyStore getTrustStore()
meth public void setTrustStore(java.security.KeyStore)
supr java.lang.Object
hfds trustStore

CLSS public abstract interface jakarta.security.auth.message.config.AuthConfig
meth public abstract boolean isProtected()
meth public abstract java.lang.String getAppContext()
meth public abstract java.lang.String getAuthContextID(jakarta.security.auth.message.MessageInfo)
meth public abstract java.lang.String getMessageLayer()
meth public abstract void refresh()

CLSS public abstract jakarta.security.auth.message.config.AuthConfigFactory
cons public init()
fld public final static java.lang.String DEFAULT_FACTORY_SECURITY_PROPERTY = "authconfigprovider.factory"
fld public final static java.lang.String GET_FACTORY_PERMISSION_NAME = "getProperty.authconfigprovider.factory"
fld public final static java.lang.String PROVIDER_REGISTRATION_PERMISSION_NAME = "setProperty.authconfigfactory.provider"
fld public final static java.lang.String SET_FACTORY_PERMISSION_NAME = "setProperty.authconfigprovider.factory"
fld public final static java.security.SecurityPermission getFactorySecurityPermission
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="")
fld public final static java.security.SecurityPermission providerRegistrationSecurityPermission
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="")
fld public final static java.security.SecurityPermission setFactorySecurityPermission
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="")
innr public abstract interface static RegistrationContext
meth public abstract boolean removeRegistration(java.lang.String)
meth public abstract jakarta.security.auth.message.config.AuthConfigFactory$RegistrationContext getRegistrationContext(java.lang.String)
meth public abstract jakarta.security.auth.message.config.AuthConfigProvider getConfigProvider(java.lang.String,java.lang.String,jakarta.security.auth.message.config.RegistrationListener)
meth public abstract java.lang.String registerConfigProvider(jakarta.security.auth.message.config.AuthConfigProvider,java.lang.String,java.lang.String,java.lang.String)
meth public abstract java.lang.String registerConfigProvider(java.lang.String,java.util.Map<java.lang.String,java.lang.String>,java.lang.String,java.lang.String,java.lang.String)
meth public abstract java.lang.String registerServerAuthModule(jakarta.security.auth.message.module.ServerAuthModule,java.lang.Object)
meth public abstract java.lang.String[] detachListener(jakarta.security.auth.message.config.RegistrationListener,java.lang.String,java.lang.String)
meth public abstract java.lang.String[] getRegistrationIDs(jakarta.security.auth.message.config.AuthConfigProvider)
meth public abstract void refresh()
meth public abstract void removeServerAuthModule(java.lang.Object)
meth public static jakarta.security.auth.message.config.AuthConfigFactory getFactory()
meth public static void setFactory(jakarta.security.auth.message.config.AuthConfigFactory)
supr java.lang.Object
hfds PROVIDER_SECURITY_PROPERTY,factory

CLSS public abstract interface static jakarta.security.auth.message.config.AuthConfigFactory$RegistrationContext
 outer jakarta.security.auth.message.config.AuthConfigFactory
meth public abstract boolean isPersistent()
meth public abstract java.lang.String getAppContext()
meth public abstract java.lang.String getDescription()
meth public abstract java.lang.String getMessageLayer()

CLSS public abstract interface jakarta.security.auth.message.config.AuthConfigProvider
meth public abstract jakarta.security.auth.message.config.ClientAuthConfig getClientAuthConfig(java.lang.String,java.lang.String,javax.security.auth.callback.CallbackHandler) throws jakarta.security.auth.message.AuthException
meth public abstract jakarta.security.auth.message.config.ServerAuthConfig getServerAuthConfig(java.lang.String,java.lang.String,javax.security.auth.callback.CallbackHandler) throws jakarta.security.auth.message.AuthException
meth public abstract void refresh()

CLSS public abstract interface jakarta.security.auth.message.config.ClientAuthConfig
intf jakarta.security.auth.message.config.AuthConfig
meth public abstract jakarta.security.auth.message.config.ClientAuthContext getAuthContext(java.lang.String,javax.security.auth.Subject,java.util.Map<java.lang.String,java.lang.Object>) throws jakarta.security.auth.message.AuthException

CLSS public abstract interface jakarta.security.auth.message.config.ClientAuthContext
intf jakarta.security.auth.message.ClientAuth

CLSS public abstract interface jakarta.security.auth.message.config.RegistrationListener
meth public abstract void notify(java.lang.String,java.lang.String)

CLSS public abstract interface jakarta.security.auth.message.config.ServerAuthConfig
intf jakarta.security.auth.message.config.AuthConfig
meth public abstract jakarta.security.auth.message.config.ServerAuthContext getAuthContext(java.lang.String,javax.security.auth.Subject,java.util.Map<java.lang.String,java.lang.Object>) throws jakarta.security.auth.message.AuthException

CLSS public abstract interface jakarta.security.auth.message.config.ServerAuthContext
intf jakarta.security.auth.message.ServerAuth

CLSS public abstract interface jakarta.security.auth.message.module.ClientAuthModule
intf jakarta.security.auth.message.ClientAuth
meth public abstract java.lang.Class<?>[] getSupportedMessageTypes()
meth public abstract void initialize(jakarta.security.auth.message.MessagePolicy,jakarta.security.auth.message.MessagePolicy,javax.security.auth.callback.CallbackHandler,java.util.Map<java.lang.String,java.lang.Object>) throws jakarta.security.auth.message.AuthException

CLSS public abstract interface jakarta.security.auth.message.module.ServerAuthModule
intf jakarta.security.auth.message.ServerAuth
meth public abstract java.lang.Class<?>[] getSupportedMessageTypes()
meth public abstract void initialize(jakarta.security.auth.message.MessagePolicy,jakarta.security.auth.message.MessagePolicy,javax.security.auth.callback.CallbackHandler,java.util.Map<java.lang.String,java.lang.Object>) throws jakarta.security.auth.message.AuthException

CLSS public abstract interface java.io.Serializable

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

CLSS public java.security.GeneralSecurityException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Exception

CLSS public abstract interface javax.security.auth.callback.Callback

CLSS public javax.security.auth.login.LoginException
cons public init()
cons public init(java.lang.String)
supr java.security.GeneralSecurityException

