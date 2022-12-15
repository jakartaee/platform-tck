#Signature file v4.1
#Version 3.0

CLSS public abstract interface jakarta.validation.BootstrapConfiguration
meth public abstract boolean isExecutableValidationEnabled()
meth public abstract java.lang.String getClockProviderClassName()
meth public abstract java.lang.String getConstraintValidatorFactoryClassName()
meth public abstract java.lang.String getDefaultProviderClassName()
meth public abstract java.lang.String getMessageInterpolatorClassName()
meth public abstract java.lang.String getParameterNameProviderClassName()
meth public abstract java.lang.String getTraversableResolverClassName()
meth public abstract java.util.Map<java.lang.String,java.lang.String> getProperties()
meth public abstract java.util.Set<jakarta.validation.executable.ExecutableType> getDefaultValidatedExecutableTypes()
meth public abstract java.util.Set<java.lang.String> getConstraintMappingResourcePaths()
meth public abstract java.util.Set<java.lang.String> getValueExtractorClassNames()

CLSS public abstract interface jakarta.validation.ClockProvider
meth public abstract java.time.Clock getClock()

CLSS public abstract interface jakarta.validation.Configuration<%0 extends jakarta.validation.Configuration<{jakarta.validation.Configuration%0}>>
meth public abstract jakarta.validation.BootstrapConfiguration getBootstrapConfiguration()
meth public abstract jakarta.validation.ClockProvider getDefaultClockProvider()
meth public abstract jakarta.validation.ConstraintValidatorFactory getDefaultConstraintValidatorFactory()
meth public abstract jakarta.validation.MessageInterpolator getDefaultMessageInterpolator()
meth public abstract jakarta.validation.ParameterNameProvider getDefaultParameterNameProvider()
meth public abstract jakarta.validation.TraversableResolver getDefaultTraversableResolver()
meth public abstract jakarta.validation.ValidatorFactory buildValidatorFactory()
meth public abstract {jakarta.validation.Configuration%0} addMapping(java.io.InputStream)
meth public abstract {jakarta.validation.Configuration%0} addProperty(java.lang.String,java.lang.String)
meth public abstract {jakarta.validation.Configuration%0} addValueExtractor(jakarta.validation.valueextraction.ValueExtractor<?>)
meth public abstract {jakarta.validation.Configuration%0} clockProvider(jakarta.validation.ClockProvider)
meth public abstract {jakarta.validation.Configuration%0} constraintValidatorFactory(jakarta.validation.ConstraintValidatorFactory)
meth public abstract {jakarta.validation.Configuration%0} ignoreXmlConfiguration()
meth public abstract {jakarta.validation.Configuration%0} messageInterpolator(jakarta.validation.MessageInterpolator)
meth public abstract {jakarta.validation.Configuration%0} parameterNameProvider(jakarta.validation.ParameterNameProvider)
meth public abstract {jakarta.validation.Configuration%0} traversableResolver(jakarta.validation.TraversableResolver)

CLSS public abstract interface !annotation jakarta.validation.Constraint
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy()

CLSS public jakarta.validation.ConstraintDeclarationException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.validation.ValidationException

CLSS public jakarta.validation.ConstraintDefinitionException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.validation.ValidationException

CLSS public final !enum jakarta.validation.ConstraintTarget
fld public final static jakarta.validation.ConstraintTarget IMPLICIT
fld public final static jakarta.validation.ConstraintTarget PARAMETERS
fld public final static jakarta.validation.ConstraintTarget RETURN_VALUE
meth public static jakarta.validation.ConstraintTarget valueOf(java.lang.String)
meth public static jakarta.validation.ConstraintTarget[] values()
supr java.lang.Enum<jakarta.validation.ConstraintTarget>

CLSS public abstract interface jakarta.validation.ConstraintValidator<%0 extends java.lang.annotation.Annotation, %1 extends java.lang.Object>
meth public abstract boolean isValid({jakarta.validation.ConstraintValidator%1},jakarta.validation.ConstraintValidatorContext)
meth public void initialize({jakarta.validation.ConstraintValidator%0})

CLSS public abstract interface jakarta.validation.ConstraintValidatorContext
innr public abstract interface static ConstraintViolationBuilder
meth public abstract <%0 extends java.lang.Object> {%%0} unwrap(java.lang.Class<{%%0}>)
meth public abstract jakarta.validation.ClockProvider getClockProvider()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder buildConstraintViolationWithTemplate(java.lang.String)
meth public abstract java.lang.String getDefaultConstraintMessageTemplate()
meth public abstract void disableDefaultConstraintViolation()

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
 outer jakarta.validation.ConstraintValidatorContext
innr public abstract interface static ContainerElementNodeBuilderCustomizableContext
innr public abstract interface static ContainerElementNodeBuilderDefinedContext
innr public abstract interface static ContainerElementNodeContextBuilder
innr public abstract interface static LeafNodeBuilderCustomizableContext
innr public abstract interface static LeafNodeBuilderDefinedContext
innr public abstract interface static LeafNodeContextBuilder
innr public abstract interface static NodeBuilderCustomizableContext
innr public abstract interface static NodeBuilderDefinedContext
innr public abstract interface static NodeContextBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderCustomizableContext addContainerElementNode(java.lang.String,java.lang.Class<?>,java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext addBeanNode()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addPropertyNode(java.lang.String)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext addNode(java.lang.String)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext addParameterNode(int)

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderCustomizableContext
 outer jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderCustomizableContext addContainerElementNode(java.lang.String,java.lang.Class<?>,java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeContextBuilder inIterable()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext addBeanNode()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addPropertyNode(java.lang.String)

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderDefinedContext
 outer jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderCustomizableContext addContainerElementNode(java.lang.String,java.lang.Class<?>,java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext addBeanNode()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addPropertyNode(java.lang.String)

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeContextBuilder
 outer jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderCustomizableContext addContainerElementNode(java.lang.String,java.lang.Class<?>,java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderDefinedContext atIndex(java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderDefinedContext atKey(java.lang.Object)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext addBeanNode()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addPropertyNode(java.lang.String)

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext
 outer jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext inContainer(java.lang.Class<?>,java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeContextBuilder inIterable()

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderDefinedContext
 outer jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeContextBuilder
 outer jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderDefinedContext atIndex(java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderDefinedContext atKey(java.lang.Object)

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext
 outer jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderCustomizableContext addContainerElementNode(java.lang.String,java.lang.Class<?>,java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext addBeanNode()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addNode(java.lang.String)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addPropertyNode(java.lang.String)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext inContainer(java.lang.Class<?>,java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeContextBuilder inIterable()

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext
 outer jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderCustomizableContext addContainerElementNode(java.lang.String,java.lang.Class<?>,java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext addBeanNode()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addNode(java.lang.String)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addPropertyNode(java.lang.String)

CLSS public abstract interface static jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeContextBuilder
 outer jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder
meth public abstract jakarta.validation.ConstraintValidatorContext addConstraintViolation()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$ContainerElementNodeBuilderCustomizableContext addContainerElementNode(java.lang.String,java.lang.Class<?>,java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext addBeanNode()
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addNode(java.lang.String)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext addPropertyNode(java.lang.String)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext atIndex(java.lang.Integer)
meth public abstract jakarta.validation.ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext atKey(java.lang.Object)

CLSS public abstract interface jakarta.validation.ConstraintValidatorFactory
meth public abstract <%0 extends jakarta.validation.ConstraintValidator<?,?>> {%%0} getInstance(java.lang.Class<{%%0}>)
meth public abstract void releaseInstance(jakarta.validation.ConstraintValidator<?,?>)

CLSS public abstract interface jakarta.validation.ConstraintViolation<%0 extends java.lang.Object>
meth public abstract <%0 extends java.lang.Object> {%%0} unwrap(java.lang.Class<{%%0}>)
meth public abstract jakarta.validation.Path getPropertyPath()
meth public abstract jakarta.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor()
meth public abstract java.lang.Class<{jakarta.validation.ConstraintViolation%0}> getRootBeanClass()
meth public abstract java.lang.Object getExecutableReturnValue()
meth public abstract java.lang.Object getInvalidValue()
meth public abstract java.lang.Object getLeafBean()
meth public abstract java.lang.Object[] getExecutableParameters()
meth public abstract java.lang.String getMessage()
meth public abstract java.lang.String getMessageTemplate()
meth public abstract {jakarta.validation.ConstraintViolation%0} getRootBean()

CLSS public jakarta.validation.ConstraintViolationException
cons public init(java.lang.String,java.util.Set<? extends jakarta.validation.ConstraintViolation<?>>)
cons public init(java.util.Set<? extends jakarta.validation.ConstraintViolation<?>>)
meth public java.util.Set<jakarta.validation.ConstraintViolation<?>> getConstraintViolations()
supr jakarta.validation.ValidationException
hfds constraintViolations

CLSS public final !enum jakarta.validation.ElementKind
fld public final static jakarta.validation.ElementKind BEAN
fld public final static jakarta.validation.ElementKind CONSTRUCTOR
fld public final static jakarta.validation.ElementKind CONTAINER_ELEMENT
fld public final static jakarta.validation.ElementKind CROSS_PARAMETER
fld public final static jakarta.validation.ElementKind METHOD
fld public final static jakarta.validation.ElementKind PARAMETER
fld public final static jakarta.validation.ElementKind PROPERTY
fld public final static jakarta.validation.ElementKind RETURN_VALUE
meth public static jakarta.validation.ElementKind valueOf(java.lang.String)
meth public static jakarta.validation.ElementKind[] values()
supr java.lang.Enum<jakarta.validation.ElementKind>

CLSS public jakarta.validation.GroupDefinitionException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.validation.ValidationException

CLSS public abstract interface !annotation jakarta.validation.GroupSequence
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<?>[] value()

CLSS public abstract interface jakarta.validation.MessageInterpolator
innr public abstract interface static Context
meth public abstract java.lang.String interpolate(java.lang.String,jakarta.validation.MessageInterpolator$Context)
meth public abstract java.lang.String interpolate(java.lang.String,jakarta.validation.MessageInterpolator$Context,java.util.Locale)

CLSS public abstract interface static jakarta.validation.MessageInterpolator$Context
 outer jakarta.validation.MessageInterpolator
meth public abstract <%0 extends java.lang.Object> {%%0} unwrap(java.lang.Class<{%%0}>)
meth public abstract jakarta.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor()
meth public abstract java.lang.Object getValidatedValue()

CLSS public jakarta.validation.NoProviderFoundException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.validation.ValidationException

CLSS public abstract interface !annotation jakarta.validation.OverridesAttribute
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.OverridesAttribute$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int constraintIndex()
meth public abstract !hasdefault java.lang.String name()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> constraint()

CLSS public abstract interface static !annotation jakarta.validation.OverridesAttribute$List
 outer jakarta.validation.OverridesAttribute
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.OverridesAttribute[] value()

CLSS public abstract interface jakarta.validation.ParameterNameProvider
meth public abstract java.util.List<java.lang.String> getParameterNames(java.lang.reflect.Constructor<?>)
meth public abstract java.util.List<java.lang.String> getParameterNames(java.lang.reflect.Method)

CLSS public abstract interface jakarta.validation.Path
innr public abstract interface static BeanNode
innr public abstract interface static ConstructorNode
innr public abstract interface static ContainerElementNode
innr public abstract interface static CrossParameterNode
innr public abstract interface static MethodNode
innr public abstract interface static Node
innr public abstract interface static ParameterNode
innr public abstract interface static PropertyNode
innr public abstract interface static ReturnValueNode
intf java.lang.Iterable<jakarta.validation.Path$Node>
meth public abstract java.lang.String toString()

CLSS public abstract interface static jakarta.validation.Path$BeanNode
 outer jakarta.validation.Path
intf jakarta.validation.Path$Node
meth public abstract java.lang.Class<?> getContainerClass()
meth public abstract java.lang.Integer getTypeArgumentIndex()

CLSS public abstract interface static jakarta.validation.Path$ConstructorNode
 outer jakarta.validation.Path
intf jakarta.validation.Path$Node
meth public abstract java.util.List<java.lang.Class<?>> getParameterTypes()

CLSS public abstract interface static jakarta.validation.Path$ContainerElementNode
 outer jakarta.validation.Path
intf jakarta.validation.Path$Node
meth public abstract java.lang.Class<?> getContainerClass()
meth public abstract java.lang.Integer getTypeArgumentIndex()

CLSS public abstract interface static jakarta.validation.Path$CrossParameterNode
 outer jakarta.validation.Path
intf jakarta.validation.Path$Node

CLSS public abstract interface static jakarta.validation.Path$MethodNode
 outer jakarta.validation.Path
intf jakarta.validation.Path$Node
meth public abstract java.util.List<java.lang.Class<?>> getParameterTypes()

CLSS public abstract interface static jakarta.validation.Path$Node
 outer jakarta.validation.Path
meth public abstract <%0 extends jakarta.validation.Path$Node> {%%0} as(java.lang.Class<{%%0}>)
meth public abstract boolean isInIterable()
meth public abstract jakarta.validation.ElementKind getKind()
meth public abstract java.lang.Integer getIndex()
meth public abstract java.lang.Object getKey()
meth public abstract java.lang.String getName()
meth public abstract java.lang.String toString()

CLSS public abstract interface static jakarta.validation.Path$ParameterNode
 outer jakarta.validation.Path
intf jakarta.validation.Path$Node
meth public abstract int getParameterIndex()

CLSS public abstract interface static jakarta.validation.Path$PropertyNode
 outer jakarta.validation.Path
intf jakarta.validation.Path$Node
meth public abstract java.lang.Class<?> getContainerClass()
meth public abstract java.lang.Integer getTypeArgumentIndex()

CLSS public abstract interface static jakarta.validation.Path$ReturnValueNode
 outer jakarta.validation.Path
intf jakarta.validation.Path$Node

CLSS public abstract interface jakarta.validation.Payload

CLSS public abstract interface !annotation jakarta.validation.ReportAsSingleViolation
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface jakarta.validation.TraversableResolver
meth public abstract boolean isCascadable(java.lang.Object,jakarta.validation.Path$Node,java.lang.Class<?>,jakarta.validation.Path,java.lang.annotation.ElementType)
meth public abstract boolean isReachable(java.lang.Object,jakarta.validation.Path$Node,java.lang.Class<?>,jakarta.validation.Path,java.lang.annotation.ElementType)

CLSS public jakarta.validation.UnexpectedTypeException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.validation.ConstraintDeclarationException

CLSS public abstract interface !annotation jakarta.validation.Valid
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation

CLSS public jakarta.validation.Validation
cons public init()
meth public static <%0 extends jakarta.validation.Configuration<{%%0}>, %1 extends jakarta.validation.spi.ValidationProvider<{%%0}>> jakarta.validation.bootstrap.ProviderSpecificBootstrap<{%%0}> byProvider(java.lang.Class<{%%1}>)
meth public static jakarta.validation.ValidatorFactory buildDefaultValidatorFactory()
meth public static jakarta.validation.bootstrap.GenericBootstrap byDefaultProvider()
supr java.lang.Object
hcls DefaultValidationProviderResolver,GenericBootstrapImpl,GetValidationProviderListAction,NewProviderInstance,ProviderSpecificBootstrapImpl

CLSS public jakarta.validation.ValidationException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.RuntimeException

CLSS public abstract interface jakarta.validation.ValidationProviderResolver
meth public abstract java.util.List<jakarta.validation.spi.ValidationProvider<?>> getValidationProviders()

CLSS public abstract interface jakarta.validation.Validator
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<jakarta.validation.ConstraintViolation<{%%0}>> validate({%%0},java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<jakarta.validation.ConstraintViolation<{%%0}>> validateProperty({%%0},java.lang.String,java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<jakarta.validation.ConstraintViolation<{%%0}>> validateValue(java.lang.Class<{%%0}>,java.lang.String,java.lang.Object,java.lang.Class<?>[])
meth public abstract <%0 extends java.lang.Object> {%%0} unwrap(java.lang.Class<{%%0}>)
meth public abstract jakarta.validation.executable.ExecutableValidator forExecutables()
meth public abstract jakarta.validation.metadata.BeanDescriptor getConstraintsForClass(java.lang.Class<?>)

CLSS public abstract interface jakarta.validation.ValidatorContext
meth public abstract jakarta.validation.Validator getValidator()
meth public abstract jakarta.validation.ValidatorContext addValueExtractor(jakarta.validation.valueextraction.ValueExtractor<?>)
meth public abstract jakarta.validation.ValidatorContext clockProvider(jakarta.validation.ClockProvider)
meth public abstract jakarta.validation.ValidatorContext constraintValidatorFactory(jakarta.validation.ConstraintValidatorFactory)
meth public abstract jakarta.validation.ValidatorContext messageInterpolator(jakarta.validation.MessageInterpolator)
meth public abstract jakarta.validation.ValidatorContext parameterNameProvider(jakarta.validation.ParameterNameProvider)
meth public abstract jakarta.validation.ValidatorContext traversableResolver(jakarta.validation.TraversableResolver)

CLSS public abstract interface jakarta.validation.ValidatorFactory
intf java.lang.AutoCloseable
meth public abstract <%0 extends java.lang.Object> {%%0} unwrap(java.lang.Class<{%%0}>)
meth public abstract jakarta.validation.ClockProvider getClockProvider()
meth public abstract jakarta.validation.ConstraintValidatorFactory getConstraintValidatorFactory()
meth public abstract jakarta.validation.MessageInterpolator getMessageInterpolator()
meth public abstract jakarta.validation.ParameterNameProvider getParameterNameProvider()
meth public abstract jakarta.validation.TraversableResolver getTraversableResolver()
meth public abstract jakarta.validation.Validator getValidator()
meth public abstract jakarta.validation.ValidatorContext usingContext()
meth public abstract void close()

CLSS public abstract interface jakarta.validation.bootstrap.GenericBootstrap
meth public abstract jakarta.validation.Configuration<?> configure()
meth public abstract jakarta.validation.bootstrap.GenericBootstrap providerResolver(jakarta.validation.ValidationProviderResolver)

CLSS public abstract interface jakarta.validation.bootstrap.ProviderSpecificBootstrap<%0 extends jakarta.validation.Configuration<{jakarta.validation.bootstrap.ProviderSpecificBootstrap%0}>>
meth public abstract jakarta.validation.bootstrap.ProviderSpecificBootstrap<{jakarta.validation.bootstrap.ProviderSpecificBootstrap%0}> providerResolver(jakarta.validation.ValidationProviderResolver)
meth public abstract {jakarta.validation.bootstrap.ProviderSpecificBootstrap%0} configure()

CLSS public abstract interface !annotation jakarta.validation.constraints.AssertFalse
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.AssertFalse$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.AssertFalse$List
 outer jakarta.validation.constraints.AssertFalse
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.AssertFalse[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.AssertTrue
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.AssertTrue$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.AssertTrue$List
 outer jakarta.validation.constraints.AssertTrue
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.AssertTrue[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.DecimalMax
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.DecimalMax$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean inclusive()
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract java.lang.String value()

CLSS public abstract interface static !annotation jakarta.validation.constraints.DecimalMax$List
 outer jakarta.validation.constraints.DecimalMax
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.DecimalMax[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.DecimalMin
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.DecimalMin$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean inclusive()
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract java.lang.String value()

CLSS public abstract interface static !annotation jakarta.validation.constraints.DecimalMin$List
 outer jakarta.validation.constraints.DecimalMin
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.DecimalMin[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Digits
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Digits$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract int fraction()
meth public abstract int integer()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Digits$List
 outer jakarta.validation.constraints.Digits
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Digits[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Email
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Email$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.validation.constraints.Pattern$Flag[] flags()
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract !hasdefault java.lang.String regexp()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Email$List
 outer jakarta.validation.constraints.Email
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Email[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Future
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Future$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Future$List
 outer jakarta.validation.constraints.Future
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Future[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.FutureOrPresent
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.FutureOrPresent$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.FutureOrPresent$List
 outer jakarta.validation.constraints.FutureOrPresent
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.FutureOrPresent[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Max
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Max$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract long value()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Max$List
 outer jakarta.validation.constraints.Max
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Max[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Min
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Min$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract long value()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Min$List
 outer jakarta.validation.constraints.Min
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Min[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Negative
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Negative$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Negative$List
 outer jakarta.validation.constraints.Negative
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Negative[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.NegativeOrZero
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.NegativeOrZero$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.NegativeOrZero$List
 outer jakarta.validation.constraints.NegativeOrZero
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.NegativeOrZero[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.NotBlank
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.NotBlank$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.NotBlank$List
 outer jakarta.validation.constraints.NotBlank
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.NotBlank[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.NotEmpty
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.NotEmpty$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.NotEmpty$List
 outer jakarta.validation.constraints.NotEmpty
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.NotEmpty[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.NotNull
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.NotNull$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.NotNull$List
 outer jakarta.validation.constraints.NotNull
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.NotNull[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Null
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Null$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Null$List
 outer jakarta.validation.constraints.Null
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Null[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Past
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Past$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Past$List
 outer jakarta.validation.constraints.Past
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Past[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.PastOrPresent
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.PastOrPresent$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.PastOrPresent$List
 outer jakarta.validation.constraints.PastOrPresent
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.PastOrPresent[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Pattern
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Pattern$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
innr public final static !enum Flag
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.validation.constraints.Pattern$Flag[] flags()
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()
meth public abstract java.lang.String regexp()

CLSS public final static !enum jakarta.validation.constraints.Pattern$Flag
 outer jakarta.validation.constraints.Pattern
fld public final static jakarta.validation.constraints.Pattern$Flag CANON_EQ
fld public final static jakarta.validation.constraints.Pattern$Flag CASE_INSENSITIVE
fld public final static jakarta.validation.constraints.Pattern$Flag COMMENTS
fld public final static jakarta.validation.constraints.Pattern$Flag DOTALL
fld public final static jakarta.validation.constraints.Pattern$Flag MULTILINE
fld public final static jakarta.validation.constraints.Pattern$Flag UNICODE_CASE
fld public final static jakarta.validation.constraints.Pattern$Flag UNIX_LINES
meth public int getValue()
meth public static jakarta.validation.constraints.Pattern$Flag valueOf(java.lang.String)
meth public static jakarta.validation.constraints.Pattern$Flag[] values()
supr java.lang.Enum<jakarta.validation.constraints.Pattern$Flag>
hfds value

CLSS public abstract interface static !annotation jakarta.validation.constraints.Pattern$List
 outer jakarta.validation.constraints.Pattern
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Pattern[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Positive
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Positive$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Positive$List
 outer jakarta.validation.constraints.Positive
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Positive[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.PositiveOrZero
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.PositiveOrZero$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.PositiveOrZero$List
 outer jakarta.validation.constraints.PositiveOrZero
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.PositiveOrZero[] value()

CLSS public abstract interface !annotation jakarta.validation.constraints.Size
 anno 0 jakarta.validation.Constraint(java.lang.Class<? extends jakarta.validation.ConstraintValidator<?,?>>[] validatedBy=[])
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.constraints.Size$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int max()
meth public abstract !hasdefault int min()
meth public abstract !hasdefault java.lang.Class<? extends jakarta.validation.Payload>[] payload()
meth public abstract !hasdefault java.lang.Class<?>[] groups()
meth public abstract !hasdefault java.lang.String message()

CLSS public abstract interface static !annotation jakarta.validation.constraints.Size$List
 outer jakarta.validation.constraints.Size
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraints.Size[] value()

CLSS public abstract interface !annotation jakarta.validation.constraintvalidation.SupportedValidationTarget
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.constraintvalidation.ValidationTarget[] value()

CLSS public final !enum jakarta.validation.constraintvalidation.ValidationTarget
fld public final static jakarta.validation.constraintvalidation.ValidationTarget ANNOTATED_ELEMENT
fld public final static jakarta.validation.constraintvalidation.ValidationTarget PARAMETERS
meth public static jakarta.validation.constraintvalidation.ValidationTarget valueOf(java.lang.String)
meth public static jakarta.validation.constraintvalidation.ValidationTarget[] values()
supr java.lang.Enum<jakarta.validation.constraintvalidation.ValidationTarget>

CLSS public final !enum jakarta.validation.executable.ExecutableType
fld public final static jakarta.validation.executable.ExecutableType ALL
fld public final static jakarta.validation.executable.ExecutableType CONSTRUCTORS
fld public final static jakarta.validation.executable.ExecutableType GETTER_METHODS
fld public final static jakarta.validation.executable.ExecutableType IMPLICIT
fld public final static jakarta.validation.executable.ExecutableType NONE
fld public final static jakarta.validation.executable.ExecutableType NON_GETTER_METHODS
meth public static jakarta.validation.executable.ExecutableType valueOf(java.lang.String)
meth public static jakarta.validation.executable.ExecutableType[] values()
supr java.lang.Enum<jakarta.validation.executable.ExecutableType>

CLSS public abstract interface jakarta.validation.executable.ExecutableValidator
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<jakarta.validation.ConstraintViolation<{%%0}>> validateConstructorParameters(java.lang.reflect.Constructor<? extends {%%0}>,java.lang.Object[],java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<jakarta.validation.ConstraintViolation<{%%0}>> validateConstructorReturnValue(java.lang.reflect.Constructor<? extends {%%0}>,{%%0},java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<jakarta.validation.ConstraintViolation<{%%0}>> validateParameters({%%0},java.lang.reflect.Method,java.lang.Object[],java.lang.Class<?>[])
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<jakarta.validation.ConstraintViolation<{%%0}>> validateReturnValue({%%0},java.lang.reflect.Method,java.lang.Object,java.lang.Class<?>[])

CLSS public abstract interface !annotation jakarta.validation.executable.ValidateOnExecution
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[CONSTRUCTOR, METHOD, TYPE, PACKAGE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.validation.executable.ExecutableType[] type()

CLSS public abstract interface !annotation jakarta.validation.groups.ConvertGroup
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.validation.groups.ConvertGroup$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<?> from()
meth public abstract java.lang.Class<?> to()

CLSS public abstract interface static !annotation jakarta.validation.groups.ConvertGroup$List
 outer jakarta.validation.groups.ConvertGroup
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.validation.groups.ConvertGroup[] value()

CLSS public abstract interface jakarta.validation.groups.Default

CLSS public abstract interface jakarta.validation.metadata.BeanDescriptor
intf jakarta.validation.metadata.ElementDescriptor
meth public abstract !varargs jakarta.validation.metadata.ConstructorDescriptor getConstraintsForConstructor(java.lang.Class<?>[])
meth public abstract !varargs jakarta.validation.metadata.MethodDescriptor getConstraintsForMethod(java.lang.String,java.lang.Class<?>[])
meth public abstract !varargs java.util.Set<jakarta.validation.metadata.MethodDescriptor> getConstrainedMethods(jakarta.validation.metadata.MethodType,jakarta.validation.metadata.MethodType[])
meth public abstract boolean isBeanConstrained()
meth public abstract jakarta.validation.metadata.PropertyDescriptor getConstraintsForProperty(java.lang.String)
meth public abstract java.util.Set<jakarta.validation.metadata.ConstructorDescriptor> getConstrainedConstructors()
meth public abstract java.util.Set<jakarta.validation.metadata.PropertyDescriptor> getConstrainedProperties()

CLSS public abstract interface jakarta.validation.metadata.CascadableDescriptor
meth public abstract boolean isCascaded()
meth public abstract java.util.Set<jakarta.validation.metadata.GroupConversionDescriptor> getGroupConversions()

CLSS public abstract interface jakarta.validation.metadata.ConstraintDescriptor<%0 extends java.lang.annotation.Annotation>
meth public abstract <%0 extends java.lang.Object> {%%0} unwrap(java.lang.Class<{%%0}>)
meth public abstract boolean isReportAsSingleViolation()
meth public abstract jakarta.validation.ConstraintTarget getValidationAppliesTo()
meth public abstract jakarta.validation.metadata.ValidateUnwrappedValue getValueUnwrapping()
meth public abstract java.lang.String getMessageTemplate()
meth public abstract java.util.List<java.lang.Class<? extends jakarta.validation.ConstraintValidator<{jakarta.validation.metadata.ConstraintDescriptor%0},?>>> getConstraintValidatorClasses()
meth public abstract java.util.Map<java.lang.String,java.lang.Object> getAttributes()
meth public abstract java.util.Set<jakarta.validation.metadata.ConstraintDescriptor<?>> getComposingConstraints()
meth public abstract java.util.Set<java.lang.Class<? extends jakarta.validation.Payload>> getPayload()
meth public abstract java.util.Set<java.lang.Class<?>> getGroups()
meth public abstract {jakarta.validation.metadata.ConstraintDescriptor%0} getAnnotation()

CLSS public abstract interface jakarta.validation.metadata.ConstructorDescriptor
intf jakarta.validation.metadata.ExecutableDescriptor

CLSS public abstract interface jakarta.validation.metadata.ContainerDescriptor
meth public abstract java.util.Set<jakarta.validation.metadata.ContainerElementTypeDescriptor> getConstrainedContainerElementTypes()

CLSS public abstract interface jakarta.validation.metadata.ContainerElementTypeDescriptor
intf jakarta.validation.metadata.CascadableDescriptor
intf jakarta.validation.metadata.ContainerDescriptor
intf jakarta.validation.metadata.ElementDescriptor
meth public abstract java.lang.Class<?> getContainerClass()
meth public abstract java.lang.Integer getTypeArgumentIndex()

CLSS public abstract interface jakarta.validation.metadata.CrossParameterDescriptor
intf jakarta.validation.metadata.ElementDescriptor
meth public abstract java.lang.Class<?> getElementClass()

CLSS public abstract interface jakarta.validation.metadata.ElementDescriptor
innr public abstract interface static ConstraintFinder
meth public abstract boolean hasConstraints()
meth public abstract jakarta.validation.metadata.ElementDescriptor$ConstraintFinder findConstraints()
meth public abstract java.lang.Class<?> getElementClass()
meth public abstract java.util.Set<jakarta.validation.metadata.ConstraintDescriptor<?>> getConstraintDescriptors()

CLSS public abstract interface static jakarta.validation.metadata.ElementDescriptor$ConstraintFinder
 outer jakarta.validation.metadata.ElementDescriptor
meth public abstract !varargs jakarta.validation.metadata.ElementDescriptor$ConstraintFinder declaredOn(java.lang.annotation.ElementType[])
meth public abstract !varargs jakarta.validation.metadata.ElementDescriptor$ConstraintFinder unorderedAndMatchingGroups(java.lang.Class<?>[])
meth public abstract boolean hasConstraints()
meth public abstract jakarta.validation.metadata.ElementDescriptor$ConstraintFinder lookingAt(jakarta.validation.metadata.Scope)
meth public abstract java.util.Set<jakarta.validation.metadata.ConstraintDescriptor<?>> getConstraintDescriptors()

CLSS public abstract interface jakarta.validation.metadata.ExecutableDescriptor
intf jakarta.validation.metadata.ElementDescriptor
meth public abstract boolean hasConstrainedParameters()
meth public abstract boolean hasConstrainedReturnValue()
meth public abstract boolean hasConstraints()
meth public abstract jakarta.validation.metadata.CrossParameterDescriptor getCrossParameterDescriptor()
meth public abstract jakarta.validation.metadata.ElementDescriptor$ConstraintFinder findConstraints()
meth public abstract jakarta.validation.metadata.ReturnValueDescriptor getReturnValueDescriptor()
meth public abstract java.lang.String getName()
meth public abstract java.util.List<jakarta.validation.metadata.ParameterDescriptor> getParameterDescriptors()
meth public abstract java.util.Set<jakarta.validation.metadata.ConstraintDescriptor<?>> getConstraintDescriptors()

CLSS public abstract interface jakarta.validation.metadata.GroupConversionDescriptor
meth public abstract java.lang.Class<?> getFrom()
meth public abstract java.lang.Class<?> getTo()

CLSS public abstract interface jakarta.validation.metadata.MethodDescriptor
intf jakarta.validation.metadata.ExecutableDescriptor

CLSS public final !enum jakarta.validation.metadata.MethodType
fld public final static jakarta.validation.metadata.MethodType GETTER
fld public final static jakarta.validation.metadata.MethodType NON_GETTER
meth public static jakarta.validation.metadata.MethodType valueOf(java.lang.String)
meth public static jakarta.validation.metadata.MethodType[] values()
supr java.lang.Enum<jakarta.validation.metadata.MethodType>

CLSS public abstract interface jakarta.validation.metadata.ParameterDescriptor
intf jakarta.validation.metadata.CascadableDescriptor
intf jakarta.validation.metadata.ContainerDescriptor
intf jakarta.validation.metadata.ElementDescriptor
meth public abstract int getIndex()
meth public abstract java.lang.String getName()

CLSS public abstract interface jakarta.validation.metadata.PropertyDescriptor
intf jakarta.validation.metadata.CascadableDescriptor
intf jakarta.validation.metadata.ContainerDescriptor
intf jakarta.validation.metadata.ElementDescriptor
meth public abstract java.lang.String getPropertyName()

CLSS public abstract interface jakarta.validation.metadata.ReturnValueDescriptor
intf jakarta.validation.metadata.CascadableDescriptor
intf jakarta.validation.metadata.ContainerDescriptor
intf jakarta.validation.metadata.ElementDescriptor

CLSS public final !enum jakarta.validation.metadata.Scope
fld public final static jakarta.validation.metadata.Scope HIERARCHY
fld public final static jakarta.validation.metadata.Scope LOCAL_ELEMENT
meth public static jakarta.validation.metadata.Scope valueOf(java.lang.String)
meth public static jakarta.validation.metadata.Scope[] values()
supr java.lang.Enum<jakarta.validation.metadata.Scope>

CLSS public final !enum jakarta.validation.metadata.ValidateUnwrappedValue
fld public final static jakarta.validation.metadata.ValidateUnwrappedValue DEFAULT
fld public final static jakarta.validation.metadata.ValidateUnwrappedValue SKIP
fld public final static jakarta.validation.metadata.ValidateUnwrappedValue UNWRAP
meth public static jakarta.validation.metadata.ValidateUnwrappedValue valueOf(java.lang.String)
meth public static jakarta.validation.metadata.ValidateUnwrappedValue[] values()
supr java.lang.Enum<jakarta.validation.metadata.ValidateUnwrappedValue>

CLSS public abstract interface jakarta.validation.spi.BootstrapState
meth public abstract jakarta.validation.ValidationProviderResolver getDefaultValidationProviderResolver()
meth public abstract jakarta.validation.ValidationProviderResolver getValidationProviderResolver()

CLSS public abstract interface jakarta.validation.spi.ConfigurationState
meth public abstract boolean isIgnoreXmlConfiguration()
meth public abstract jakarta.validation.ClockProvider getClockProvider()
meth public abstract jakarta.validation.ConstraintValidatorFactory getConstraintValidatorFactory()
meth public abstract jakarta.validation.MessageInterpolator getMessageInterpolator()
meth public abstract jakarta.validation.ParameterNameProvider getParameterNameProvider()
meth public abstract jakarta.validation.TraversableResolver getTraversableResolver()
meth public abstract java.util.Map<java.lang.String,java.lang.String> getProperties()
meth public abstract java.util.Set<jakarta.validation.valueextraction.ValueExtractor<?>> getValueExtractors()
meth public abstract java.util.Set<java.io.InputStream> getMappingStreams()

CLSS public abstract interface jakarta.validation.spi.ValidationProvider<%0 extends jakarta.validation.Configuration<{jakarta.validation.spi.ValidationProvider%0}>>
meth public abstract jakarta.validation.Configuration<?> createGenericConfiguration(jakarta.validation.spi.BootstrapState)
meth public abstract jakarta.validation.ValidatorFactory buildValidatorFactory(jakarta.validation.spi.ConfigurationState)
meth public abstract {jakarta.validation.spi.ValidationProvider%0} createSpecializedConfiguration(jakarta.validation.spi.BootstrapState)

CLSS public abstract interface !annotation jakarta.validation.valueextraction.ExtractedValue
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE_USE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<?> type()

CLSS public abstract interface !annotation jakarta.validation.valueextraction.UnwrapByDefault
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface jakarta.validation.valueextraction.Unwrapping
innr public abstract interface static Skip
innr public abstract interface static Unwrap

CLSS public abstract interface static jakarta.validation.valueextraction.Unwrapping$Skip
 outer jakarta.validation.valueextraction.Unwrapping
intf jakarta.validation.Payload

CLSS public abstract interface static jakarta.validation.valueextraction.Unwrapping$Unwrap
 outer jakarta.validation.valueextraction.Unwrapping
intf jakarta.validation.Payload

CLSS public abstract interface jakarta.validation.valueextraction.ValueExtractor<%0 extends java.lang.Object>
innr public abstract interface static ValueReceiver
meth public abstract void extractValues({jakarta.validation.valueextraction.ValueExtractor%0},jakarta.validation.valueextraction.ValueExtractor$ValueReceiver)

CLSS public abstract interface static jakarta.validation.valueextraction.ValueExtractor$ValueReceiver
 outer jakarta.validation.valueextraction.ValueExtractor
meth public abstract void indexedValue(java.lang.String,int,java.lang.Object)
meth public abstract void iterableValue(java.lang.String,java.lang.Object)
meth public abstract void keyedValue(java.lang.String,java.lang.Object,java.lang.Object)
meth public abstract void value(java.lang.String,java.lang.Object)

CLSS public jakarta.validation.valueextraction.ValueExtractorDeclarationException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.validation.ValidationException

CLSS public jakarta.validation.valueextraction.ValueExtractorDefinitionException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr jakarta.validation.ValidationException

CLSS public abstract interface java.io.Serializable

CLSS public abstract interface java.lang.AutoCloseable
meth public abstract void close() throws java.lang.Exception

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

