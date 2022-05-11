#Signature file v4.1
#Version 2.0

CLSS public abstract jakarta.enterprise.util.AnnotationLiteral<%0 extends java.lang.annotation.Annotation>
cons protected init()
intf java.io.Serializable
intf java.lang.annotation.Annotation
meth public boolean equals(java.lang.Object)
meth public int hashCode()
meth public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType()
meth public java.lang.String toString()
supr java.lang.Object
hfds annotationType,cachedHashCode,members,serialVersionUID

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
intf java.io.Serializable
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
innr public final static Literal
intf java.lang.annotation.Annotation

CLSS public final static jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession$Literal
 outer jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession
cons public init()
fld public final static jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession$Literal INSTANCE
intf jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession>
hfds serialVersionUID

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

CLSS public jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanismWrapper
cons public init()
cons public init(jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism)
intf jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism
meth public jakarta.security.enterprise.AuthenticationStatus secureResponse(jakarta.servlet.http.HttpServletRequest,jakarta.servlet.http.HttpServletResponse,jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext) throws jakarta.security.enterprise.AuthenticationException
meth public jakarta.security.enterprise.AuthenticationStatus validateRequest(jakarta.servlet.http.HttpServletRequest,jakarta.servlet.http.HttpServletResponse,jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext) throws jakarta.security.enterprise.AuthenticationException
meth public jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism getWrapped()
meth public void cleanSubject(jakarta.servlet.http.HttpServletRequest,jakarta.servlet.http.HttpServletResponse,jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext)
supr java.lang.Object
hfds httpAuthenticationMechanism

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
innr public final static Literal
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean useForwardToLogin()
meth public abstract !hasdefault java.lang.String errorPage()
meth public abstract !hasdefault java.lang.String loginPage()
meth public abstract !hasdefault java.lang.String useForwardToLoginExpression()

CLSS public final static jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue$Literal
 outer jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue
fld public final static jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue$Literal INSTANCE
intf jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue
meth public boolean useForwardToLogin()
meth public java.lang.String errorPage()
meth public java.lang.String loginPage()
meth public java.lang.String useForwardToLoginExpression()
meth public static jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue$Literal of(java.lang.String,boolean,java.lang.String,java.lang.String)
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue>
hfds errorPage,loginPage,useForwardToLogin,useForwardToLoginExpression

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.OpenIdAuthenticationMechanismDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean redirectToOriginalResource()
meth public abstract !hasdefault boolean tokenAutoRefresh()
meth public abstract !hasdefault boolean useNonce()
meth public abstract !hasdefault boolean useSession()
meth public abstract !hasdefault int jwksConnectTimeout()
meth public abstract !hasdefault int jwksReadTimeout()
meth public abstract !hasdefault int tokenMinValidity()
meth public abstract !hasdefault jakarta.security.enterprise.authentication.mechanism.http.openid.ClaimsDefinition claimsDefinition()
meth public abstract !hasdefault jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType display()
meth public abstract !hasdefault jakarta.security.enterprise.authentication.mechanism.http.openid.LogoutDefinition logout()
meth public abstract !hasdefault jakarta.security.enterprise.authentication.mechanism.http.openid.OpenIdProviderMetadata providerMetadata()
meth public abstract !hasdefault jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType[] prompt()
meth public abstract !hasdefault java.lang.String clientId()
meth public abstract !hasdefault java.lang.String clientSecret()
meth public abstract !hasdefault java.lang.String displayExpression()
meth public abstract !hasdefault java.lang.String extraParametersExpression()
meth public abstract !hasdefault java.lang.String jwksConnectTimeoutExpression()
meth public abstract !hasdefault java.lang.String jwksReadTimeoutExpression()
meth public abstract !hasdefault java.lang.String promptExpression()
meth public abstract !hasdefault java.lang.String providerURI()
meth public abstract !hasdefault java.lang.String redirectToOriginalResourceExpression()
meth public abstract !hasdefault java.lang.String redirectURI()
meth public abstract !hasdefault java.lang.String responseMode()
meth public abstract !hasdefault java.lang.String responseType()
meth public abstract !hasdefault java.lang.String scopeExpression()
meth public abstract !hasdefault java.lang.String tokenAutoRefreshExpression()
meth public abstract !hasdefault java.lang.String tokenMinValidityExpression()
meth public abstract !hasdefault java.lang.String useNonceExpression()
meth public abstract !hasdefault java.lang.String useSessionExpression()
meth public abstract !hasdefault java.lang.String[] extraParameters()
meth public abstract !hasdefault java.lang.String[] scope()

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.RememberMe
 anno 0 jakarta.interceptor.InterceptorBinding()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
innr public final static Literal
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

CLSS public final static jakarta.security.enterprise.authentication.mechanism.http.RememberMe$Literal
 outer jakarta.security.enterprise.authentication.mechanism.http.RememberMe
fld public final static jakarta.security.enterprise.authentication.mechanism.http.RememberMe$Literal INSTANCE
intf jakarta.security.enterprise.authentication.mechanism.http.RememberMe
meth public boolean cookieHttpOnly()
meth public boolean cookieSecureOnly()
meth public boolean isRememberMe()
meth public int cookieMaxAgeSeconds()
meth public java.lang.String cookieHttpOnlyExpression()
meth public java.lang.String cookieMaxAgeSecondsExpression()
meth public java.lang.String cookieName()
meth public java.lang.String cookieSecureOnlyExpression()
meth public java.lang.String isRememberMeExpression()
meth public static jakarta.security.enterprise.authentication.mechanism.http.RememberMe$Literal of(int,java.lang.String,boolean,java.lang.String,boolean,java.lang.String,java.lang.String,boolean,java.lang.String)
supr jakarta.enterprise.util.AnnotationLiteral<jakarta.security.enterprise.authentication.mechanism.http.RememberMe>
hfds cookieHttpOnly,cookieHttpOnlyExpression,cookieMaxAgeSeconds,cookieMaxAgeSecondsExpression,cookieName,cookieSecureOnly,cookieSecureOnlyExpression,isRememberMe,isRememberMeExpression,serialVersionUID

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.openid.ClaimsDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String callerGroupsClaim()
meth public abstract !hasdefault java.lang.String callerNameClaim()

CLSS public final !enum jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType
fld public final static jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType PAGE
fld public final static jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType POPUP
fld public final static jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType TOUCH
fld public final static jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType WAP
meth public static jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType fromString(java.lang.String)
meth public static jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType valueOf(java.lang.String)
meth public static jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType[] values()
supr java.lang.Enum<jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType>

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.openid.LogoutDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean accessTokenExpiry()
meth public abstract !hasdefault boolean identityTokenExpiry()
meth public abstract !hasdefault boolean notifyProvider()
meth public abstract !hasdefault java.lang.String accessTokenExpiryExpression()
meth public abstract !hasdefault java.lang.String identityTokenExpiryExpression()
meth public abstract !hasdefault java.lang.String notifyProviderExpression()
meth public abstract !hasdefault java.lang.String redirectURI()

CLSS public abstract interface jakarta.security.enterprise.authentication.mechanism.http.openid.OpenIdConstant
fld public final static java.lang.String ACCESS_TOKEN = "access_token"
fld public final static java.lang.String ACCESS_TOKEN_HASH = "at_hash"
fld public final static java.lang.String ACR_VALUES = "acr_values"
fld public final static java.lang.String ADDRESS = "address"
fld public final static java.lang.String AUDIENCE = "aud"
fld public final static java.lang.String AUTHORIZATION_CODE = "authorization_code"
fld public final static java.lang.String AUTHORIZATION_ENDPOINT = "authorization_endpoint"
fld public final static java.lang.String AUTHORIZED_PARTY = "azp"
fld public final static java.lang.String BIRTHDATE = "birthdate"
fld public final static java.lang.String CLAIMS_LOCALES = "claims_locales"
fld public final static java.lang.String CLAIMS_SUPPORTED = "claims_supported"
fld public final static java.lang.String CLAIM_TYPES_SUPPORTED = "claim_types_supported"
fld public final static java.lang.String CLIENT_ID = "client_id"
fld public final static java.lang.String CLIENT_SECRET = "client_secret"
fld public final static java.lang.String CODE = "code"
fld public final static java.lang.String DEFAULT_HASH_ALGORITHM = "SHA-256"
fld public final static java.lang.String DEFAULT_JWT_SIGNED_ALGORITHM = "RS256"
fld public final static java.lang.String DISPLAY = "display"
fld public final static java.lang.String DISPLAY_VALUES_SUPPORTED = "display_values_supported"
fld public final static java.lang.String EMAIL = "email"
fld public final static java.lang.String EMAIL_SCOPE = "email"
fld public final static java.lang.String EMAIL_VERIFIED = "email_verified"
fld public final static java.lang.String END_SESSION_ENDPOINT = "end_session_endpoint"
fld public final static java.lang.String ERROR_DESCRIPTION_PARAM = "error_description"
fld public final static java.lang.String ERROR_PARAM = "error"
fld public final static java.lang.String EXPIRATION_IDENTIFIER = "exp"
fld public final static java.lang.String EXPIRES_IN = "expires_in"
fld public final static java.lang.String FAMILY_NAME = "family_name"
fld public final static java.lang.String GENDER = "gender"
fld public final static java.lang.String GIVEN_NAME = "given_name"
fld public final static java.lang.String GRANT_TYPE = "grant_type"
fld public final static java.lang.String GROUPS = "groups"
fld public final static java.lang.String IDENTITY_TOKEN = "id_token"
fld public final static java.lang.String ID_TOKEN_HINT = "id_token_hint"
fld public final static java.lang.String ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED = "id_token_signing_alg_values_supported"
fld public final static java.lang.String ISSUER = "issuer"
fld public final static java.lang.String ISSUER_IDENTIFIER = "iss"
fld public final static java.lang.String JWKS_URI = "jwks_uri"
fld public final static java.lang.String LOCALE = "locale"
fld public final static java.lang.String LOGIN_HINT = "login_hint"
fld public final static java.lang.String MAX_AGE = "max_age"
fld public final static java.lang.String MIDDLE_NAME = "middle_name"
fld public final static java.lang.String NAME = "name"
fld public final static java.lang.String NICKNAME = "nickname"
fld public final static java.lang.String NONCE = "nonce"
fld public final static java.lang.String OFFLINE_ACCESS_SCOPE = "offline_access"
fld public final static java.lang.String OPENID_SCOPE = "openid"
fld public final static java.lang.String ORIGINAL_REQUEST = "oidc.original.request"
fld public final static java.lang.String PHONE_NUMBER = "phone_number"
fld public final static java.lang.String PHONE_NUMBER_VERIFIED = "phone_number_verified"
fld public final static java.lang.String PHONE_SCOPE = "phone"
fld public final static java.lang.String PICTURE = "picture"
fld public final static java.lang.String POST_LOGOUT_REDIRECT_URI = "post_logout_redirect_uri"
fld public final static java.lang.String PREFERRED_USERNAME = "preferred_username"
fld public final static java.lang.String PROFILE = "profile"
fld public final static java.lang.String PROFILE_SCOPE = "profile"
fld public final static java.lang.String PROMPT = "prompt"
fld public final static java.lang.String REDIRECT_URI = "redirect_uri"
fld public final static java.lang.String REFRESH_TOKEN = "refresh_token"
fld public final static java.lang.String REGISTRATION_ENDPOINT = "registration_endpoint"
fld public final static java.lang.String RESPONSE_MODE = "response_mode"
fld public final static java.lang.String RESPONSE_MODES_SUPPORTED = "response_modes_supported"
fld public final static java.lang.String RESPONSE_TYPE = "response_type"
fld public final static java.lang.String RESPONSE_TYPES_SUPPORTED = "response_types_supported"
fld public final static java.lang.String SCOPE = "scope"
fld public final static java.lang.String SCOPES_SUPPORTED = "scopes_supported"
fld public final static java.lang.String STATE = "state"
fld public final static java.lang.String SUBJECT_IDENTIFIER = "sub"
fld public final static java.lang.String SUBJECT_TYPES_SUPPORTED = "subject_types_supported"
fld public final static java.lang.String TOKEN_ENDPOINT = "token_endpoint"
fld public final static java.lang.String TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED = "token_endpoint_auth_methods_supported"
fld public final static java.lang.String TOKEN_ENDPOINT_AUTH_SIGNING_ALG_VALUES_SUPPORTED = "token_endpoint_auth_signing_alg_values_supported"
fld public final static java.lang.String TOKEN_TYPE = "token_type"
fld public final static java.lang.String UI_LOCALES = "ui_locales"
fld public final static java.lang.String UPDATED_AT = "updated_at"
fld public final static java.lang.String USERINFO_ENDPOINT = "userinfo_endpoint"
fld public final static java.lang.String WEBSITE = "website"
fld public final static java.lang.String ZONEINFO = "zoneinfo"
fld public final static java.util.List<java.lang.String> AUTHORIZATION_CODE_FLOW_TYPES
fld public final static java.util.List<java.lang.String> HYBRID_FLOW_TYPES
fld public final static java.util.List<java.lang.String> IMPLICIT_FLOW_TYPES

CLSS public abstract interface !annotation jakarta.security.enterprise.authentication.mechanism.http.openid.OpenIdProviderMetadata
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String authorizationEndpoint()
meth public abstract !hasdefault java.lang.String endSessionEndpoint()
meth public abstract !hasdefault java.lang.String idTokenSigningAlgorithmsSupported()
meth public abstract !hasdefault java.lang.String issuer()
meth public abstract !hasdefault java.lang.String jwksURI()
meth public abstract !hasdefault java.lang.String responseTypeSupported()
meth public abstract !hasdefault java.lang.String subjectTypeSupported()
meth public abstract !hasdefault java.lang.String tokenEndpoint()
meth public abstract !hasdefault java.lang.String userinfoEndpoint()

CLSS public final !enum jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType
fld public final static jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType CONSENT
fld public final static jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType LOGIN
fld public final static jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType NONE
fld public final static jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType SELECT_ACCOUNT
meth public static jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType fromString(java.lang.String)
meth public static jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType valueOf(java.lang.String)
meth public static jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType[] values()
supr java.lang.Enum<jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType>

CLSS abstract interface jakarta.security.enterprise.authentication.mechanism.http.package-info

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

CLSS abstract interface jakarta.security.enterprise.credential.package-info

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

CLSS public abstract interface jakarta.security.enterprise.identitystore.openid.AccessToken
innr public final static !enum Type
meth public abstract boolean isExpired()
meth public abstract boolean isJWT()
meth public abstract jakarta.security.enterprise.identitystore.openid.AccessToken$Type getType()
meth public abstract jakarta.security.enterprise.identitystore.openid.JwtClaims getJwtClaims()
meth public abstract jakarta.security.enterprise.identitystore.openid.Scope getScope()
meth public abstract java.lang.Long getExpirationTime()
meth public abstract java.lang.Object getClaim(java.lang.String)
meth public abstract java.lang.String getToken()
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getClaims()

CLSS public final static !enum jakarta.security.enterprise.identitystore.openid.AccessToken$Type
 outer jakarta.security.enterprise.identitystore.openid.AccessToken
fld public final static jakarta.security.enterprise.identitystore.openid.AccessToken$Type BEARER
fld public final static jakarta.security.enterprise.identitystore.openid.AccessToken$Type MAC
meth public static jakarta.security.enterprise.identitystore.openid.AccessToken$Type valueOf(java.lang.String)
meth public static jakarta.security.enterprise.identitystore.openid.AccessToken$Type[] values()
supr java.lang.Enum<jakarta.security.enterprise.identitystore.openid.AccessToken$Type>

CLSS public abstract interface jakarta.security.enterprise.identitystore.openid.Claims
meth public abstract java.util.List<java.lang.String> getArrayStringClaim(java.lang.String)
meth public abstract java.util.Optional<jakarta.security.enterprise.identitystore.openid.Claims> getNested(java.lang.String)
meth public abstract java.util.Optional<java.lang.String> getStringClaim(java.lang.String)
meth public abstract java.util.Optional<java.time.Instant> getNumericDateClaim(java.lang.String)
meth public abstract java.util.OptionalDouble getDoubleClaim(java.lang.String)
meth public abstract java.util.OptionalInt getIntClaim(java.lang.String)
meth public abstract java.util.OptionalLong getLongClaim(java.lang.String)

CLSS public abstract interface jakarta.security.enterprise.identitystore.openid.IdentityToken
meth public abstract boolean isExpired()
meth public abstract jakarta.security.enterprise.identitystore.openid.JwtClaims getJwtClaims()
meth public abstract java.lang.String getToken()
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getClaims()

CLSS public abstract interface jakarta.security.enterprise.identitystore.openid.JwtClaims
fld public final static jakarta.security.enterprise.identitystore.openid.JwtClaims NONE
intf jakarta.security.enterprise.identitystore.openid.Claims
meth public boolean isBeforeValidity(java.time.Clock,boolean,java.time.Duration)
meth public boolean isExpired(java.time.Clock,boolean,java.time.Duration)
meth public boolean isValid()
meth public java.util.List<java.lang.String> getAudience()
meth public java.util.Optional<java.lang.String> getIssuer()
meth public java.util.Optional<java.lang.String> getJwtId()
meth public java.util.Optional<java.lang.String> getSubject()
meth public java.util.Optional<java.time.Instant> getExpirationTime()
meth public java.util.Optional<java.time.Instant> getIssuedAt()
meth public java.util.Optional<java.time.Instant> getNotBeforeTime()

CLSS public abstract interface jakarta.security.enterprise.identitystore.openid.OpenIdClaims
intf jakarta.security.enterprise.identitystore.openid.Claims
meth public java.lang.String getSubject()
meth public java.util.Optional<java.lang.String> getAddress()
meth public java.util.Optional<java.lang.String> getBirthdate()
meth public java.util.Optional<java.lang.String> getEmail()
meth public java.util.Optional<java.lang.String> getEmailVerified()
meth public java.util.Optional<java.lang.String> getFamilyName()
meth public java.util.Optional<java.lang.String> getGender()
meth public java.util.Optional<java.lang.String> getGivenName()
meth public java.util.Optional<java.lang.String> getLocale()
meth public java.util.Optional<java.lang.String> getMiddleName()
meth public java.util.Optional<java.lang.String> getName()
meth public java.util.Optional<java.lang.String> getNickname()
meth public java.util.Optional<java.lang.String> getPhoneNumber()
meth public java.util.Optional<java.lang.String> getPhoneNumberVerified()
meth public java.util.Optional<java.lang.String> getPicture()
meth public java.util.Optional<java.lang.String> getPreferredUsername()
meth public java.util.Optional<java.lang.String> getProfile()
meth public java.util.Optional<java.lang.String> getUpdatedAt()
meth public java.util.Optional<java.lang.String> getWebsite()
meth public java.util.Optional<java.lang.String> getZoneinfo()

CLSS public abstract interface jakarta.security.enterprise.identitystore.openid.OpenIdContext
intf java.io.Serializable
meth public abstract <%0 extends java.lang.Object> java.util.Optional<{%%0}> getStoredValue(jakarta.servlet.http.HttpServletRequest,jakarta.servlet.http.HttpServletResponse,java.lang.String)
meth public abstract jakarta.json.JsonObject getClaimsJson()
meth public abstract jakarta.json.JsonObject getProviderMetadata()
meth public abstract jakarta.security.enterprise.identitystore.openid.AccessToken getAccessToken()
meth public abstract jakarta.security.enterprise.identitystore.openid.IdentityToken getIdentityToken()
meth public abstract jakarta.security.enterprise.identitystore.openid.OpenIdClaims getClaims()
meth public abstract java.lang.String getSubject()
meth public abstract java.lang.String getTokenType()
meth public abstract java.util.Optional<jakarta.security.enterprise.identitystore.openid.RefreshToken> getRefreshToken()
meth public abstract java.util.Optional<java.lang.Long> getExpiresIn()

CLSS public abstract interface jakarta.security.enterprise.identitystore.openid.RefreshToken
meth public abstract java.lang.String getToken()

CLSS public jakarta.security.enterprise.identitystore.openid.Scope
cons public init()
cons public init(java.util.List<java.lang.String>)
meth public java.lang.String toString()
meth public static jakarta.security.enterprise.identitystore.openid.Scope parse(java.lang.String)
supr java.util.LinkedHashSet<java.lang.String>
hfds serialVersionUID

CLSS abstract interface jakarta.security.enterprise.identitystore.package-info

CLSS abstract interface jakarta.security.enterprise.package-info

CLSS public abstract interface java.io.Serializable

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

CLSS public abstract java.util.AbstractCollection<%0 extends java.lang.Object>
cons protected init()
intf java.util.Collection<{java.util.AbstractCollection%0}>
meth public <%0 extends java.lang.Object> {%%0}[] toArray({%%0}[])
meth public abstract int size()
meth public abstract java.util.Iterator<{java.util.AbstractCollection%0}> iterator()
meth public boolean add({java.util.AbstractCollection%0})
meth public boolean addAll(java.util.Collection<? extends {java.util.AbstractCollection%0}>)
meth public boolean contains(java.lang.Object)
meth public boolean containsAll(java.util.Collection<?>)
meth public boolean isEmpty()
meth public boolean remove(java.lang.Object)
meth public boolean removeAll(java.util.Collection<?>)
meth public boolean retainAll(java.util.Collection<?>)
meth public java.lang.Object[] toArray()
meth public java.lang.String toString()
meth public void clear()
supr java.lang.Object

CLSS public abstract java.util.AbstractSet<%0 extends java.lang.Object>
cons protected init()
intf java.util.Set<{java.util.AbstractSet%0}>
meth public boolean equals(java.lang.Object)
meth public boolean removeAll(java.util.Collection<?>)
meth public int hashCode()
supr java.util.AbstractCollection<{java.util.AbstractSet%0}>

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

CLSS public java.util.HashSet<%0 extends java.lang.Object>
cons public init()
cons public init(int)
cons public init(int,float)
cons public init(java.util.Collection<? extends {java.util.HashSet%0}>)
intf java.io.Serializable
intf java.lang.Cloneable
intf java.util.Set<{java.util.HashSet%0}>
meth public boolean add({java.util.HashSet%0})
meth public boolean contains(java.lang.Object)
meth public boolean isEmpty()
meth public boolean remove(java.lang.Object)
meth public int size()
meth public java.lang.Object clone()
meth public java.util.Iterator<{java.util.HashSet%0}> iterator()
meth public java.util.Spliterator<{java.util.HashSet%0}> spliterator()
meth public void clear()
supr java.util.AbstractSet<{java.util.HashSet%0}>

CLSS public java.util.LinkedHashSet<%0 extends java.lang.Object>
cons public init()
cons public init(int)
cons public init(int,float)
cons public init(java.util.Collection<? extends {java.util.LinkedHashSet%0}>)
intf java.io.Serializable
intf java.lang.Cloneable
intf java.util.Set<{java.util.LinkedHashSet%0}>
meth public java.util.Spliterator<{java.util.LinkedHashSet%0}> spliterator()
supr java.util.HashSet<{java.util.LinkedHashSet%0}>

CLSS public abstract interface java.util.Set<%0 extends java.lang.Object>
intf java.util.Collection<{java.util.Set%0}>
meth public !varargs static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0}[])
 anno 0 java.lang.SafeVarargs()
meth public abstract <%0 extends java.lang.Object> {%%0}[] toArray({%%0}[])
meth public abstract boolean add({java.util.Set%0})
meth public abstract boolean addAll(java.util.Collection<? extends {java.util.Set%0}>)
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
meth public abstract java.util.Iterator<{java.util.Set%0}> iterator()
meth public abstract void clear()
meth public java.util.Spliterator<{java.util.Set%0}> spliterator()
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> copyOf(java.util.Collection<? extends {%%0}>)
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of()
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0})
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0})
meth public static <%0 extends java.lang.Object> java.util.Set<{%%0}> of({%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0},{%%0})

