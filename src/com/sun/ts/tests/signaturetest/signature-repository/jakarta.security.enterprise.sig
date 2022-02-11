#Signature file v4.1
#Version 1.0

CLSS public abstract interface !annotation jakarta.interceptor.InterceptorBinding
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public jakarta.security.enterprise.AuthenticationException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.security.GeneralSecurityException
hfds serialVersionUID

CLSS public final !enum jakarta.security.enterprise.AuthenticationStatus
fld public final static jakarta.security.enterprise.AuthenticationStatus NOT_DONE
fld public final static jakarta.security.enterprise.AuthenticationStatus SEND_CONTINUE
fld public final static jakarta.security.enterprise.AuthenticationStatus SEND_FAILURE
fld public final static jakarta.security.enterprise.AuthenticationStatus SUCCESS
meth public static jakarta.security.enterprise.AuthenticationStatus valueOf(java.lang.String)
meth public static jakarta.security.enterprise.AuthenticationStatus[] values()
supr java.lang.Enum<jakarta.security.enterprise.AuthenticationStatus>

CLSS public jakarta.security.enterprise.CallerPrincipal
cons public init(java.lang.String)
intf java.security.Principal
meth public java.lang.String getName()
supr java.lang.Object
hfds name,serialVersionUID

CLSS public abstract interface jakarta.security.enterprise.SecurityContext
meth public abstract !varargs boolean hasAccessToWebResource(java.lang.String,java.lang.String[])
meth public abstract <%0 extends java.security.Principal> java.util.Set<{%%0}> getPrincipalsByType(java.lang.Class<{%%0}>)
meth public abstract boolean isCallerInRole(java.lang.String)
meth public abstract jakarta.security.enterprise.AuthenticationStatus authenticate(jakarta.servlet.http.HttpServletRequest,jakarta.servlet.http.HttpServletResponse,jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters)
meth public abstract java.security.Principal getCallerPrincipal()

CLSS public jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters
cons public init()
meth public boolean isNewAuthentication()
meth public boolean isRememberMe()
meth public jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters credential(jakarta.security.enterprise.credential.Credential)
meth public jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters newAuthentication(boolean)
meth public jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters rememberMe(boolean)
meth public jakarta.security.enterprise.credential.Credential getCredential()
meth public static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters withParams()
meth public void setCredential(jakarta.security.enterprise.credential.Credential)
meth public void setNewAuthentication(boolean)
meth public void setRememberMe(boolean)
supr java.lang.Object
hfds credential,newAuthentication,rememberMe

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession
 anno 0 jakarta.interceptor.InterceptorBinding()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String realmName()

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue loginToContinue()

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue loginToContinue()

CLSS public abstract interface jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism
meth public abstract jakarta.security.enterprise.AuthenticationStatus validateRequest(jakarta.servlet.http.HttpServletRequest,jakarta.servlet.http.HttpServletResponse,jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext) throws jakarta.security.enterprise.AuthenticationException
meth public jakarta.security.enterprise.AuthenticationStatus secureResponse(jakarta.servlet.http.HttpServletRequest,jakarta.servlet.http.HttpServletResponse,jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext) throws jakarta.security.enterprise.AuthenticationException
meth public void cleanSubject(jakarta.servlet.http.HttpServletRequest,jakarta.servlet.http.HttpServletResponse,jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext)

CLSS public abstract interface jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext
meth public abstract boolean isAuthenticationRequest()
meth public abstract boolean isProtected()
meth public abstract boolean isRegisterSession()
meth public abstract jakarta.security.auth.message.MessageInfo getMessageInfo()
meth public abstract jakarta.security.enterprise.AuthenticationStatus doNothing()
meth public abstract jakarta.security.enterprise.AuthenticationStatus forward(java.lang.String)
meth public abstract jakarta.security.enterprise.AuthenticationStatus notifyContainerAboutLogin(jakarta.security.enterprise.identitystore.CredentialValidationResult)
meth public abstract jakarta.security.enterprise.AuthenticationStatus notifyContainerAboutLogin(java.lang.String,java.util.Set<java.lang.String>)
meth public abstract jakarta.security.enterprise.AuthenticationStatus notifyContainerAboutLogin(java.security.Principal,java.util.Set<java.lang.String>)
meth public abstract jakarta.security.enterprise.AuthenticationStatus redirect(java.lang.String)
meth public abstract jakarta.security.enterprise.AuthenticationStatus responseNotFound()
meth public abstract jakarta.security.enterprise.AuthenticationStatus responseUnauthorized()
meth public abstract jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters getAuthParameters()
meth public abstract jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext withRequest(jakarta.servlet.http.HttpServletRequest)
meth public abstract jakarta.servlet.http.HttpServletRequest getRequest()
meth public abstract jakarta.servlet.http.HttpServletResponse getResponse()
meth public abstract java.security.Principal getCallerPrincipal()
meth public abstract java.util.Set<java.lang.String> getGroups()
meth public abstract javax.security.auth.Subject getClientSubject()
meth public abstract javax.security.auth.callback.CallbackHandler getHandler()
meth public abstract void cleanClientSubject()
meth public abstract void setRegisterSession(java.lang.String,java.util.Set<java.lang.String>)
meth public abstract void setRequest(jakarta.servlet.http.HttpServletRequest)
meth public abstract void setResponse(jakarta.servlet.http.HttpServletResponse)

CLSS public jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContextWrapper
cons public init(jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext)
intf jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext
meth public boolean isAuthenticationRequest()
meth public boolean isProtected()
meth public boolean isRegisterSession()
meth public jakarta.security.auth.message.MessageInfo getMessageInfo()
meth public jakarta.security.enterprise.AuthenticationStatus doNothing()
meth public jakarta.security.enterprise.AuthenticationStatus forward(java.lang.String)
meth public jakarta.security.enterprise.AuthenticationStatus notifyContainerAboutLogin(jakarta.security.enterprise.identitystore.CredentialValidationResult)
meth public jakarta.security.enterprise.AuthenticationStatus notifyContainerAboutLogin(java.lang.String,java.util.Set<java.lang.String>)
meth public jakarta.security.enterprise.AuthenticationStatus notifyContainerAboutLogin(java.security.Principal,java.util.Set<java.lang.String>)
meth public jakarta.security.enterprise.AuthenticationStatus redirect(java.lang.String)
meth public jakarta.security.enterprise.AuthenticationStatus responseNotFound()
meth public jakarta.security.enterprise.AuthenticationStatus responseUnauthorized()
meth public jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters getAuthParameters()
meth public jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext getWrapped()
meth public jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext withRequest(jakarta.servlet.http.HttpServletRequest)
meth public jakarta.servlet.http.HttpServletRequest getRequest()
meth public jakarta.servlet.http.HttpServletResponse getResponse()
meth public java.security.Principal getCallerPrincipal()
meth public java.util.Set<java.lang.String> getGroups()
meth public javax.security.auth.Subject getClientSubject()
meth public javax.security.auth.callback.CallbackHandler getHandler()
meth public void cleanClientSubject()
meth public void setRegisterSession(java.lang.String,java.util.Set<java.lang.String>)
meth public void setRequest(jakarta.servlet.http.HttpServletRequest)
meth public void setResponse(jakarta.servlet.http.HttpServletResponse)
supr java.lang.Object
hfds httpMessageContext

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue
 anno 0 jakarta.interceptor.InterceptorBinding()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean useForwardToLogin()
meth public abstract !hasdefault java.lang.String errorPage()
meth public abstract !hasdefault java.lang.String loginPage()
meth public abstract !hasdefault java.lang.String useForwardToLoginExpression()

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.RememberMe
 anno 0 jakarta.interceptor.InterceptorBinding()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean cookieHttpOnly()
meth public abstract !hasdefault boolean cookieSecureOnly()
meth public abstract !hasdefault boolean isRememberMe()
meth public abstract !hasdefault int cookieMaxAgeSeconds()
meth public abstract !hasdefault java.lang.String cookieHttpOnlyExpression()
meth public abstract !hasdefault java.lang.String cookieMaxAgeSecondsExpression()
meth public abstract !hasdefault java.lang.String cookieName()
meth public abstract !hasdefault java.lang.String cookieSecureOnlyExpression()
meth public abstract !hasdefault java.lang.String isRememberMeExpression()

CLSS public abstract jakarta.security.enterprise.credential.AbstractClearableCredential
cons public init()
intf jakarta.security.enterprise.credential.Credential
meth protected abstract void clearCredential()
meth protected final void setCleared()
meth public final boolean isCleared()
meth public final void clear()
supr java.lang.Object
hfds cleared

CLSS public jakarta.security.enterprise.credential.BasicAuthenticationCredential
cons public init(java.lang.String)
supr jakarta.security.enterprise.credential.UsernamePasswordCredential

CLSS public jakarta.security.enterprise.credential.CallerOnlyCredential
cons public init(java.lang.String)
intf jakarta.security.enterprise.credential.Credential
meth public java.lang.String getCaller()
supr java.lang.Object
hfds caller

CLSS public abstract interface jakarta.security.enterprise.credential.Credential
meth public boolean isCleared()
meth public boolean isValid()
meth public void clear()

CLSS public jakarta.security.enterprise.credential.Password
cons public init(char[])
cons public init(java.lang.String)
meth public boolean compareTo(java.lang.String)
meth public char[] getValue()
meth public void clear()
supr java.lang.Object
hfds EMPTY_VALUE,value

CLSS public jakarta.security.enterprise.credential.RememberMeCredential
cons public init(java.lang.String)
intf jakarta.security.enterprise.credential.Credential
meth public java.lang.String getToken()
supr java.lang.Object
hfds token

CLSS public jakarta.security.enterprise.credential.UsernamePasswordCredential
cons public init(java.lang.String,jakarta.security.enterprise.credential.Password)
cons public init(java.lang.String,java.lang.String)
meth public boolean compareTo(java.lang.String,java.lang.String)
meth public jakarta.security.enterprise.credential.Password getPassword()
meth public java.lang.String getCaller()
meth public java.lang.String getPasswordAsString()
meth public void clearCredential()
supr jakarta.security.enterprise.credential.AbstractClearableCredential
hfds caller,password

CLSS public jakarta.security.enterprise.identitystore.CredentialValidationResult
cons public init(jakarta.security.enterprise.CallerPrincipal)
cons public init(jakarta.security.enterprise.CallerPrincipal,java.util.Set<java.lang.String>)
cons public init(java.lang.String)
cons public init(java.lang.String,jakarta.security.enterprise.CallerPrincipal,java.lang.String,java.lang.String,java.util.Set<java.lang.String>)
cons public init(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.util.Set<java.lang.String>)
cons public init(java.lang.String,java.util.Set<java.lang.String>)
fld public final static jakarta.security.enterprise.identitystore.CredentialValidationResult INVALID_RESULT
fld public final static jakarta.security.enterprise.identitystore.CredentialValidationResult NOT_VALIDATED_RESULT
innr public final static !enum Status
meth public jakarta.security.enterprise.CallerPrincipal getCallerPrincipal()
meth public jakarta.security.enterprise.identitystore.CredentialValidationResult$Status getStatus()
meth public java.lang.String getCallerDn()
meth public java.lang.String getCallerUniqueId()
meth public java.lang.String getIdentityStoreId()
meth public java.util.Set<java.lang.String> getCallerGroups()
supr java.lang.Object
hfds callerDn,callerPrincipal,callerUniqueId,groups,status,storeId

CLSS public final static !enum jakarta.security.enterprise.identitystore.CredentialValidationResult$Status
 outer jakarta.security.enterprise.identitystore.CredentialValidationResult
fld public final static jakarta.security.enterprise.identitystore.CredentialValidationResult$Status INVALID
fld public final static jakarta.security.enterprise.identitystore.CredentialValidationResult$Status NOT_VALIDATED
fld public final static jakarta.security.enterprise.identitystore.CredentialValidationResult$Status VALID
meth public static jakarta.security.enterprise.identitystore.CredentialValidationResult$Status valueOf(java.lang.String)
meth public static jakarta.security.enterprise.identitystore.CredentialValidationResult$Status[] values()
supr java.lang.Enum<jakarta.security.enterprise.identitystore.CredentialValidationResult$Status>

CLSS public abstract interface !annotation jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int priority()
meth public abstract !hasdefault jakarta.security.enterprise.identitystore.IdentityStore$ValidationType[] useFor()
meth public abstract !hasdefault java.lang.Class<? extends jakarta.security.enterprise.identitystore.PasswordHash> hashAlgorithm()
meth public abstract !hasdefault java.lang.String callerQuery()
meth public abstract !hasdefault java.lang.String dataSourceLookup()
meth public abstract !hasdefault java.lang.String groupsQuery()
meth public abstract !hasdefault java.lang.String priorityExpression()
meth public abstract !hasdefault java.lang.String useForExpression()
meth public abstract !hasdefault java.lang.String[] hashAlgorithmParameters()

CLSS public abstract interface jakarta.security.enterprise.identitystore.IdentityStore
fld public final static java.util.Set<jakarta.security.enterprise.identitystore.IdentityStore$ValidationType> DEFAULT_VALIDATION_TYPES
innr public final static !enum ValidationType
meth public int priority()
meth public jakarta.security.enterprise.identitystore.CredentialValidationResult validate(jakarta.security.enterprise.credential.Credential)
meth public java.util.Set<jakarta.security.enterprise.identitystore.IdentityStore$ValidationType> validationTypes()
meth public java.util.Set<java.lang.String> getCallerGroups(jakarta.security.enterprise.identitystore.CredentialValidationResult)

CLSS public final static !enum jakarta.security.enterprise.identitystore.IdentityStore$ValidationType
 outer jakarta.security.enterprise.identitystore.IdentityStore
fld public final static jakarta.security.enterprise.identitystore.IdentityStore$ValidationType PROVIDE_GROUPS
fld public final static jakarta.security.enterprise.identitystore.IdentityStore$ValidationType VALIDATE
meth public static jakarta.security.enterprise.identitystore.IdentityStore$ValidationType valueOf(java.lang.String)
meth public static jakarta.security.enterprise.identitystore.IdentityStore$ValidationType[] values()
supr java.lang.Enum<jakarta.security.enterprise.identitystore.IdentityStore$ValidationType>

CLSS public abstract interface jakarta.security.enterprise.identitystore.IdentityStoreHandler
meth public abstract jakarta.security.enterprise.identitystore.CredentialValidationResult validate(jakarta.security.enterprise.credential.Credential)

CLSS public jakarta.security.enterprise.identitystore.IdentityStorePermission
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.String)
supr java.security.BasicPermission
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
innr public final static !enum LdapSearchScope
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int maxResults()
meth public abstract !hasdefault int priority()
meth public abstract !hasdefault int readTimeout()
meth public abstract !hasdefault jakarta.security.enterprise.identitystore.IdentityStore$ValidationType[] useFor()
meth public abstract !hasdefault jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition$LdapSearchScope callerSearchScope()
meth public abstract !hasdefault jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition$LdapSearchScope groupSearchScope()
meth public abstract !hasdefault java.lang.String bindDn()
meth public abstract !hasdefault java.lang.String bindDnPassword()
meth public abstract !hasdefault java.lang.String callerBaseDn()
meth public abstract !hasdefault java.lang.String callerNameAttribute()
meth public abstract !hasdefault java.lang.String callerSearchBase()
meth public abstract !hasdefault java.lang.String callerSearchFilter()
meth public abstract !hasdefault java.lang.String callerSearchScopeExpression()
meth public abstract !hasdefault java.lang.String groupMemberAttribute()
meth public abstract !hasdefault java.lang.String groupMemberOfAttribute()
meth public abstract !hasdefault java.lang.String groupNameAttribute()
meth public abstract !hasdefault java.lang.String groupSearchBase()
meth public abstract !hasdefault java.lang.String groupSearchFilter()
meth public abstract !hasdefault java.lang.String groupSearchScopeExpression()
meth public abstract !hasdefault java.lang.String maxResultsExpression()
meth public abstract !hasdefault java.lang.String priorityExpression()
meth public abstract !hasdefault java.lang.String readTimeoutExpression()
meth public abstract !hasdefault java.lang.String url()
meth public abstract !hasdefault java.lang.String useForExpression()

CLSS public final static !enum jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition$LdapSearchScope
 outer jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition
fld public final static jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition$LdapSearchScope ONE_LEVEL
fld public final static jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition$LdapSearchScope SUBTREE
meth public static jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition$LdapSearchScope valueOf(java.lang.String)
meth public static jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition$LdapSearchScope[] values()
supr java.lang.Enum<jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition$LdapSearchScope>

CLSS public abstract interface jakarta.security.enterprise.identitystore.PasswordHash
meth public abstract boolean verify(char[],java.lang.String)
meth public abstract java.lang.String generate(char[])
meth public void initialize(java.util.Map<java.lang.String,java.lang.String>)

CLSS public abstract interface jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash
intf jakarta.security.enterprise.identitystore.PasswordHash

CLSS public abstract interface jakarta.security.enterprise.identitystore.RememberMeIdentityStore
meth public abstract jakarta.security.enterprise.identitystore.CredentialValidationResult validate(jakarta.security.enterprise.credential.RememberMeCredential)
meth public abstract java.lang.String generateLoginToken(jakarta.security.enterprise.CallerPrincipal,java.util.Set<java.lang.String>)
meth public abstract void removeLoginToken(java.lang.String)

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

CLSS public java.security.GeneralSecurityException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Exception

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

CLSS public abstract interface java.security.Principal
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()
meth public abstract java.lang.String getName()
meth public abstract java.lang.String toString()
meth public boolean implies(javax.security.auth.Subject)

