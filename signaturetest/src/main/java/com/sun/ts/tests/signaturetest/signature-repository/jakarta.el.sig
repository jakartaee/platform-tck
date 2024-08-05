#Signature file v4.1
#Version 5.0

CLSS public jakarta.el.ArrayELResolver
cons public init()
cons public init(boolean)
meth public boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
meth public void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
supr jakarta.el.ELResolver
hfds isReadOnly

CLSS public jakarta.el.BeanELResolver
cons public init()
cons public init(boolean)
meth public boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object invoke(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Class<?>[],java.lang.Object[])
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
meth public void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
supr jakarta.el.ELResolver
hfds isReadOnly,properties
hcls BPSoftReference,BeanProperties,BeanProperty,SoftConcurrentHashMap

CLSS public jakarta.el.BeanNameELResolver
cons public init(jakarta.el.BeanNameResolver)
meth public boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
meth public void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
supr jakarta.el.ELResolver
hfds beanNameResolver

CLSS public abstract jakarta.el.BeanNameResolver
cons public init()
meth public boolean canCreateBean(java.lang.String)
meth public boolean isNameResolved(java.lang.String)
meth public boolean isReadOnly(java.lang.String)
meth public java.lang.Object getBean(java.lang.String)
meth public void setBeanValue(java.lang.String,java.lang.Object)
supr java.lang.Object

CLSS public jakarta.el.CompositeELResolver
cons public init()
meth public <%0 extends java.lang.Object> {%%0} convertToType(jakarta.el.ELContext,java.lang.Object,java.lang.Class<{%%0}>)
meth public boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object invoke(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Class<?>[],java.lang.Object[])
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
meth public void add(jakarta.el.ELResolver)
meth public void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
supr jakarta.el.ELResolver
hfds elResolvers,size
hcls CompositeIterator

CLSS public jakarta.el.ELClass
cons public init(java.lang.Class<?>)
meth public java.lang.Class<?> getKlass()
supr java.lang.Object
hfds klass

CLSS public abstract jakarta.el.ELContext
cons public init()
meth public <%0 extends java.lang.Object> {%%0} convertToType(java.lang.Object,java.lang.Class<{%%0}>)
meth public abstract jakarta.el.ELResolver getELResolver()
meth public abstract jakarta.el.FunctionMapper getFunctionMapper()
meth public abstract jakarta.el.VariableMapper getVariableMapper()
meth public boolean isLambdaArgument(java.lang.String)
meth public boolean isPropertyResolved()
meth public jakarta.el.ImportHandler getImportHandler()
meth public java.lang.Object getContext(java.lang.Class<?>)
meth public java.lang.Object getLambdaArgument(java.lang.String)
meth public java.util.List<jakarta.el.EvaluationListener> getEvaluationListeners()
meth public java.util.Locale getLocale()
meth public void addEvaluationListener(jakarta.el.EvaluationListener)
meth public void enterLambdaScope(java.util.Map<java.lang.String,java.lang.Object>)
meth public void exitLambdaScope()
meth public void notifyAfterEvaluation(java.lang.String)
meth public void notifyBeforeEvaluation(java.lang.String)
meth public void notifyPropertyResolved(java.lang.Object,java.lang.Object)
meth public void putContext(java.lang.Class<?>,java.lang.Object)
meth public void setLocale(java.util.Locale)
meth public void setPropertyResolved(boolean)
meth public void setPropertyResolved(java.lang.Object,java.lang.Object)
supr java.lang.Object
hfds importHandler,lambdaArgs,listeners,locale,map,resolved

CLSS public jakarta.el.ELContextEvent
cons public init(jakarta.el.ELContext)
meth public jakarta.el.ELContext getELContext()
supr java.util.EventObject
hfds serialVersionUID

CLSS public abstract interface jakarta.el.ELContextListener
intf java.util.EventListener
meth public abstract void contextCreated(jakarta.el.ELContextEvent)

CLSS public jakarta.el.ELException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public jakarta.el.ELManager
cons public init()
meth public jakarta.el.ELContext setELContext(jakarta.el.ELContext)
meth public jakarta.el.StandardELContext getELContext()
meth public java.lang.Object defineBean(java.lang.String,java.lang.Object)
meth public static jakarta.el.ExpressionFactory getExpressionFactory()
meth public void addBeanNameResolver(jakarta.el.BeanNameResolver)
meth public void addELResolver(jakarta.el.ELResolver)
meth public void addEvaluationListener(jakarta.el.EvaluationListener)
meth public void importClass(java.lang.String)
meth public void importPackage(java.lang.String)
meth public void importStatic(java.lang.String)
meth public void mapFunction(java.lang.String,java.lang.String,java.lang.reflect.Method)
meth public void setVariable(java.lang.String,jakarta.el.ValueExpression)
supr java.lang.Object
hfds elContext

CLSS public jakarta.el.ELProcessor
cons public init()
meth public <%0 extends java.lang.Object> {%%0} eval(java.lang.String)
meth public <%0 extends java.lang.Object> {%%0} getValue(java.lang.String,java.lang.Class<{%%0}>)
meth public jakarta.el.ELManager getELManager()
meth public void defineBean(java.lang.String,java.lang.Object)
meth public void defineFunction(java.lang.String,java.lang.String,java.lang.String,java.lang.String) throws java.lang.ClassNotFoundException,java.lang.NoSuchMethodException
meth public void defineFunction(java.lang.String,java.lang.String,java.lang.reflect.Method) throws java.lang.NoSuchMethodException
meth public void setValue(java.lang.String,java.lang.Object)
meth public void setVariable(java.lang.String,java.lang.String)
supr java.lang.Object
hfds elManager,factory

CLSS public abstract jakarta.el.ELResolver
cons public init()
fld public final static java.lang.String RESOLVABLE_AT_DESIGN_TIME = "resolvableAtDesignTime"
fld public final static java.lang.String TYPE = "type"
meth public <%0 extends java.lang.Object> {%%0} convertToType(jakarta.el.ELContext,java.lang.Object,java.lang.Class<{%%0}>)
meth public abstract boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public abstract java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public abstract java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public abstract java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public abstract void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
meth public java.lang.Object invoke(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Class<?>[],java.lang.Object[])
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
supr java.lang.Object

CLSS public abstract jakarta.el.EvaluationListener
cons public init()
meth public void afterEvaluation(jakarta.el.ELContext,java.lang.String)
meth public void beforeEvaluation(jakarta.el.ELContext,java.lang.String)
meth public void propertyResolved(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
supr java.lang.Object

CLSS public abstract jakarta.el.Expression
cons public init()
intf java.io.Serializable
meth public abstract boolean equals(java.lang.Object)
meth public abstract boolean isLiteralText()
meth public abstract int hashCode()
meth public abstract java.lang.String getExpressionString()
supr java.lang.Object
hfds serialVersionUID

CLSS public abstract jakarta.el.ExpressionFactory
cons public init()
meth public abstract <%0 extends java.lang.Object> {%%0} coerceToType(java.lang.Object,java.lang.Class<{%%0}>)
meth public abstract jakarta.el.MethodExpression createMethodExpression(jakarta.el.ELContext,java.lang.String,java.lang.Class<?>,java.lang.Class<?>[])
meth public abstract jakarta.el.ValueExpression createValueExpression(jakarta.el.ELContext,java.lang.String,java.lang.Class<?>)
meth public abstract jakarta.el.ValueExpression createValueExpression(java.lang.Object,java.lang.Class<?>)
meth public jakarta.el.ELResolver getStreamELResolver()
meth public java.util.Map<java.lang.String,java.lang.reflect.Method> getInitFunctionMap()
meth public static jakarta.el.ExpressionFactory newInstance()
meth public static jakarta.el.ExpressionFactory newInstance(java.util.Properties)
supr java.lang.Object

CLSS public abstract jakarta.el.FunctionMapper
cons public init()
meth public abstract java.lang.reflect.Method resolveFunction(java.lang.String,java.lang.String)
meth public void mapFunction(java.lang.String,java.lang.String,java.lang.reflect.Method)
supr java.lang.Object

CLSS public jakarta.el.ImportHandler
cons public init()
meth public java.lang.Class<?> resolveClass(java.lang.String)
meth public java.lang.Class<?> resolveStatic(java.lang.String)
meth public void importClass(java.lang.String)
meth public void importPackage(java.lang.String)
meth public void importStatic(java.lang.String)
supr java.lang.Object
hfds classMap,classNameMap,notAClass,packages,staticNameMap

CLSS public jakarta.el.LambdaExpression
cons public init(java.util.List<java.lang.String>,jakarta.el.ValueExpression)
meth public !varargs java.lang.Object invoke(jakarta.el.ELContext,java.lang.Object[])
meth public !varargs java.lang.Object invoke(java.lang.Object[])
meth public void setELContext(jakarta.el.ELContext)
supr java.lang.Object
hfds context,envirArgs,expression,formalParameters

CLSS public jakarta.el.ListELResolver
cons public init()
cons public init(boolean)
meth public boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
meth public void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
supr jakarta.el.ELResolver
hfds isReadOnly,theUnmodifiableListClass

CLSS public jakarta.el.MapELResolver
cons public init()
cons public init(boolean)
meth public boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
meth public void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
supr jakarta.el.ELResolver
hfds isReadOnly,theUnmodifiableMapClass

CLSS public abstract jakarta.el.MethodExpression
cons public init()
meth public abstract jakarta.el.MethodInfo getMethodInfo(jakarta.el.ELContext)
meth public abstract java.lang.Object invoke(jakarta.el.ELContext,java.lang.Object[])
meth public boolean isParametersProvided()
meth public jakarta.el.MethodReference getMethodReference(jakarta.el.ELContext)
supr jakarta.el.Expression
hfds serialVersionUID

CLSS public jakarta.el.MethodInfo
cons public init(java.lang.String,java.lang.Class<?>,java.lang.Class<?>[])
meth public boolean equals(java.lang.Object)
meth public int hashCode()
meth public java.lang.Class<?> getReturnType()
meth public java.lang.Class<?>[] getParamTypes()
meth public java.lang.String getName()
supr java.lang.Object
hfds name,paramTypes,returnType

CLSS public jakarta.el.MethodNotFoundException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.el.ELException
hfds serialVersionUID

CLSS public jakarta.el.MethodReference
cons public init(java.lang.Object,jakarta.el.MethodInfo,java.lang.annotation.Annotation[],java.lang.Object[])
meth public boolean equals(java.lang.Object)
meth public int hashCode()
meth public jakarta.el.MethodInfo getMethodInfo()
meth public java.lang.Object getBase()
meth public java.lang.Object[] getEvaluatedParameters()
meth public java.lang.annotation.Annotation[] getAnnotations()
supr java.lang.Object
hfds annotations,base,evaluatedParameters,methodInfo

CLSS public jakarta.el.PropertyNotFoundException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.el.ELException
hfds serialVersionUID

CLSS public jakarta.el.PropertyNotWritableException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.el.ELException
hfds serialVersionUID

CLSS public jakarta.el.ResourceBundleELResolver
cons public init()
meth public boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
meth public void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
supr jakarta.el.ELResolver

CLSS public jakarta.el.StandardELContext
cons public init(jakarta.el.ELContext)
cons public init(jakarta.el.ExpressionFactory)
meth public jakarta.el.ELResolver getELResolver()
meth public jakarta.el.FunctionMapper getFunctionMapper()
meth public jakarta.el.VariableMapper getVariableMapper()
meth public java.lang.Object getContext(java.lang.Class<?>)
meth public void addELResolver(jakarta.el.ELResolver)
meth public void putContext(java.lang.Class<?>,java.lang.Object)
supr jakarta.el.ELContext
hfds beans,customResolvers,delegate,elResolver,functionMapper,initFunctionMap,streamELResolver,variableMapper
hcls DefaultFunctionMapper,DefaultVariableMapper,LocalBeanNameResolver

CLSS public jakarta.el.StaticFieldELResolver
cons public init()
meth public boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object invoke(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Class<?>[],java.lang.Object[])
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
meth public void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
supr jakarta.el.ELResolver

CLSS public abstract jakarta.el.TypeConverter
cons public init()
meth public abstract <%0 extends java.lang.Object> {%%0} convertToType(jakarta.el.ELContext,java.lang.Object,java.lang.Class<{%%0}>)
meth public boolean isReadOnly(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Class<?> getCommonPropertyType(jakarta.el.ELContext,java.lang.Object)
meth public java.lang.Class<?> getType(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.lang.Object getValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object)
meth public java.util.Iterator<java.beans.FeatureDescriptor> getFeatureDescriptors(jakarta.el.ELContext,java.lang.Object)
 anno 0 java.lang.Deprecated(boolean forRemoval=true, java.lang.String since="5.0")
meth public void setValue(jakarta.el.ELContext,java.lang.Object,java.lang.Object,java.lang.Object)
supr jakarta.el.ELResolver

CLSS public abstract jakarta.el.ValueExpression
cons public init()
meth public abstract <%0 extends java.lang.Object> {%%0} getValue(jakarta.el.ELContext)
meth public abstract boolean isReadOnly(jakarta.el.ELContext)
meth public abstract java.lang.Class<?> getExpectedType()
meth public abstract java.lang.Class<?> getType(jakarta.el.ELContext)
meth public abstract void setValue(jakarta.el.ELContext,java.lang.Object)
meth public jakarta.el.ValueReference getValueReference(jakarta.el.ELContext)
supr jakarta.el.Expression
hfds serialVersionUID

CLSS public jakarta.el.ValueReference
cons public init(java.lang.Object,java.lang.Object)
intf java.io.Serializable
meth public java.lang.Object getBase()
meth public java.lang.Object getProperty()
supr java.lang.Object
hfds base,property,serialVersionUID

CLSS public abstract jakarta.el.VariableMapper
cons public init()
meth public abstract jakarta.el.ValueExpression resolveVariable(java.lang.String)
meth public abstract jakarta.el.ValueExpression setVariable(java.lang.String,jakarta.el.ValueExpression)
supr java.lang.Object

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

CLSS public abstract interface java.util.EventListener

CLSS public java.util.EventObject
cons public init(java.lang.Object)
fld protected java.lang.Object source
intf java.io.Serializable
meth public java.lang.Object getSource()
meth public java.lang.String toString()
supr java.lang.Object

