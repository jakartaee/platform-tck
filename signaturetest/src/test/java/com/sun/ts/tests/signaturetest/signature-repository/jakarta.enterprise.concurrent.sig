#Signature file v4.1
#Version 3.0

CLSS public jakarta.enterprise.concurrent.AbortedException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
intf java.io.Serializable
supr java.util.concurrent.ExecutionException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.enterprise.concurrent.Asynchronous
 anno 0 jakarta.interceptor.InterceptorBinding()
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, TYPE])
innr public final static Result
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String executor()

CLSS public final static jakarta.enterprise.concurrent.Asynchronous$Result
 outer jakarta.enterprise.concurrent.Asynchronous
meth public static <%0 extends java.lang.Object> java.util.concurrent.CompletableFuture<{%%0}> complete({%%0})
meth public static <%0 extends java.lang.Object> java.util.concurrent.CompletableFuture<{%%0}> getFuture()
meth public static <%0 extends java.lang.Object> void setFuture(java.util.concurrent.CompletableFuture<{%%0}>)
supr java.lang.Object
hfds FUTURES

CLSS public abstract interface jakarta.enterprise.concurrent.ContextService
meth public abstract !varargs java.lang.Object createContextualProxy(java.lang.Object,java.lang.Class<?>[])
meth public abstract !varargs java.lang.Object createContextualProxy(java.lang.Object,java.util.Map<java.lang.String,java.lang.String>,java.lang.Class<?>[])
meth public abstract <%0 extends java.lang.Object, %1 extends java.lang.Object, %2 extends java.lang.Object> java.util.function.BiFunction<{%%0},{%%1},{%%2}> contextualFunction(java.util.function.BiFunction<{%%0},{%%1},{%%2}>)
meth public abstract <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.function.BiConsumer<{%%0},{%%1}> contextualConsumer(java.util.function.BiConsumer<{%%0},{%%1}>)
meth public abstract <%0 extends java.lang.Object, %1 extends java.lang.Object> java.util.function.Function<{%%0},{%%1}> contextualFunction(java.util.function.Function<{%%0},{%%1}>)
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.Callable<{%%0}> contextualCallable(java.util.concurrent.Callable<{%%0}>)
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletableFuture<{%%0}> withContextCapture(java.util.concurrent.CompletableFuture<{%%0}>)
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletionStage<{%%0}> withContextCapture(java.util.concurrent.CompletionStage<{%%0}>)
meth public abstract <%0 extends java.lang.Object> java.util.function.Consumer<{%%0}> contextualConsumer(java.util.function.Consumer<{%%0}>)
meth public abstract <%0 extends java.lang.Object> java.util.function.Supplier<{%%0}> contextualSupplier(java.util.function.Supplier<{%%0}>)
meth public abstract <%0 extends java.lang.Object> {%%0} createContextualProxy({%%0},java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> {%%0} createContextualProxy({%%0},java.util.Map<java.lang.String,java.lang.String>,java.lang.Class<{%%0}>)
meth public abstract java.lang.Runnable contextualRunnable(java.lang.Runnable)
meth public abstract java.util.Map<java.lang.String,java.lang.String> getExecutionProperties(java.lang.Object)
meth public abstract java.util.concurrent.Executor currentContextExecutor()

CLSS public abstract interface !annotation jakarta.enterprise.concurrent.ContextServiceDefinition
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.enterprise.concurrent.ContextServiceDefinition$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
fld public final static java.lang.String ALL_REMAINING = "Remaining"
fld public final static java.lang.String APPLICATION = "Application"
fld public final static java.lang.String SECURITY = "Security"
fld public final static java.lang.String TRANSACTION = "Transaction"
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String[] cleared()
meth public abstract !hasdefault java.lang.String[] propagated()
meth public abstract !hasdefault java.lang.String[] unchanged()
meth public abstract java.lang.String name()

CLSS public abstract interface static !annotation jakarta.enterprise.concurrent.ContextServiceDefinition$List
 outer jakarta.enterprise.concurrent.ContextServiceDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.enterprise.concurrent.ContextServiceDefinition[] value()

CLSS public jakarta.enterprise.concurrent.CronTrigger
cons public init(java.lang.String,java.time.ZoneId)
cons public init(java.time.ZoneId)
intf jakarta.enterprise.concurrent.ZonedTrigger
meth protected java.time.ZonedDateTime next(java.time.ZonedDateTime)
meth public !varargs jakarta.enterprise.concurrent.CronTrigger daysOfMonth(int[])
meth public !varargs jakarta.enterprise.concurrent.CronTrigger daysOfWeek(java.time.DayOfWeek[])
meth public !varargs jakarta.enterprise.concurrent.CronTrigger hours(int[])
meth public !varargs jakarta.enterprise.concurrent.CronTrigger minutes(int[])
meth public !varargs jakarta.enterprise.concurrent.CronTrigger months(java.time.Month[])
meth public !varargs jakarta.enterprise.concurrent.CronTrigger seconds(int[])
meth public final java.time.ZoneId getZoneId()
meth public jakarta.enterprise.concurrent.CronTrigger daysOfMonth(java.lang.String)
meth public jakarta.enterprise.concurrent.CronTrigger daysOfWeek(java.lang.String)
meth public jakarta.enterprise.concurrent.CronTrigger hours(java.lang.String)
meth public jakarta.enterprise.concurrent.CronTrigger minutes(java.lang.String)
meth public jakarta.enterprise.concurrent.CronTrigger months(java.lang.String)
meth public jakarta.enterprise.concurrent.CronTrigger seconds(java.lang.String)
meth public java.lang.String toString()
meth public java.time.ZonedDateTime getNextRunTime(jakarta.enterprise.concurrent.LastExecution,java.time.ZonedDateTime)
supr java.lang.Object
hfds ALL_DAYS_OF_MONTH,ALL_DAYS_OF_WEEK,ALL_MONTHS,DAYS_OF_WEEK,LAST,MONTHS,ZERO,daysOfMonth,daysOfWeek,hours,minutes,months,seconds,zone

CLSS public abstract interface jakarta.enterprise.concurrent.LastExecution
meth public abstract java.lang.Object getResult()
meth public abstract java.lang.String getIdentityName()
meth public abstract java.time.ZonedDateTime getRunEnd(java.time.ZoneId)
meth public abstract java.time.ZonedDateTime getRunStart(java.time.ZoneId)
meth public abstract java.time.ZonedDateTime getScheduledStart(java.time.ZoneId)
meth public java.util.Date getRunEnd()
meth public java.util.Date getRunStart()
meth public java.util.Date getScheduledStart()

CLSS public abstract interface jakarta.enterprise.concurrent.ManageableThread
meth public abstract boolean isShutdown()

CLSS public abstract interface !annotation jakarta.enterprise.concurrent.ManagedExecutorDefinition
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.enterprise.concurrent.ManagedExecutorDefinition$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int maxAsync()
meth public abstract !hasdefault java.lang.String context()
meth public abstract !hasdefault long hungTaskThreshold()
meth public abstract java.lang.String name()

CLSS public abstract interface static !annotation jakarta.enterprise.concurrent.ManagedExecutorDefinition$List
 outer jakarta.enterprise.concurrent.ManagedExecutorDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.enterprise.concurrent.ManagedExecutorDefinition[] value()

CLSS public abstract interface jakarta.enterprise.concurrent.ManagedExecutorService
intf java.util.concurrent.ExecutorService
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletableFuture<{%%0}> completedFuture({%%0})
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletableFuture<{%%0}> copy(java.util.concurrent.CompletableFuture<{%%0}>)
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletableFuture<{%%0}> failedFuture(java.lang.Throwable)
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletableFuture<{%%0}> newIncompleteFuture()
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletableFuture<{%%0}> supplyAsync(java.util.function.Supplier<{%%0}>)
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletionStage<{%%0}> completedStage({%%0})
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletionStage<{%%0}> copy(java.util.concurrent.CompletionStage<{%%0}>)
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.CompletionStage<{%%0}> failedStage(java.lang.Throwable)
meth public abstract jakarta.enterprise.concurrent.ContextService getContextService()
meth public abstract java.util.concurrent.CompletableFuture<java.lang.Void> runAsync(java.lang.Runnable)

CLSS public final jakarta.enterprise.concurrent.ManagedExecutors
meth public static <%0 extends java.lang.Object> java.util.concurrent.Callable<{%%0}> managedTask(java.util.concurrent.Callable<{%%0}>,jakarta.enterprise.concurrent.ManagedTaskListener)
meth public static <%0 extends java.lang.Object> java.util.concurrent.Callable<{%%0}> managedTask(java.util.concurrent.Callable<{%%0}>,java.util.Map<java.lang.String,java.lang.String>,jakarta.enterprise.concurrent.ManagedTaskListener)
meth public static boolean isCurrentThreadShutdown()
meth public static java.lang.Runnable managedTask(java.lang.Runnable,jakarta.enterprise.concurrent.ManagedTaskListener)
meth public static java.lang.Runnable managedTask(java.lang.Runnable,java.util.Map<java.lang.String,java.lang.String>,jakarta.enterprise.concurrent.ManagedTaskListener)
supr java.lang.Object
hfds NULL_TASK_ERROR_MSG
hcls Adapter,CallableAdapter,RunnableAdapter

CLSS public abstract interface !annotation jakarta.enterprise.concurrent.ManagedScheduledExecutorDefinition
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.enterprise.concurrent.ManagedScheduledExecutorDefinition$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int maxAsync()
meth public abstract !hasdefault java.lang.String context()
meth public abstract !hasdefault long hungTaskThreshold()
meth public abstract java.lang.String name()

CLSS public abstract interface static !annotation jakarta.enterprise.concurrent.ManagedScheduledExecutorDefinition$List
 outer jakarta.enterprise.concurrent.ManagedScheduledExecutorDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.enterprise.concurrent.ManagedScheduledExecutorDefinition[] value()

CLSS public abstract interface jakarta.enterprise.concurrent.ManagedScheduledExecutorService
intf jakarta.enterprise.concurrent.ManagedExecutorService
intf java.util.concurrent.ScheduledExecutorService
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.ScheduledFuture<{%%0}> schedule(java.util.concurrent.Callable<{%%0}>,jakarta.enterprise.concurrent.Trigger)
meth public abstract java.util.concurrent.ScheduledFuture<?> schedule(java.lang.Runnable,jakarta.enterprise.concurrent.Trigger)

CLSS public abstract interface jakarta.enterprise.concurrent.ManagedTask
fld public final static java.lang.String IDENTITY_NAME = "jakarta.enterprise.concurrent.IDENTITY_NAME"
fld public final static java.lang.String LONGRUNNING_HINT = "jakarta.enterprise.concurrent.LONGRUNNING_HINT"
fld public final static java.lang.String SUSPEND = "SUSPEND"
fld public final static java.lang.String TRANSACTION = "jakarta.enterprise.concurrent.TRANSACTION"
fld public final static java.lang.String USE_TRANSACTION_OF_EXECUTION_THREAD = "USE_TRANSACTION_OF_EXECUTION_THREAD"
meth public abstract jakarta.enterprise.concurrent.ManagedTaskListener getManagedTaskListener()
meth public abstract java.util.Map<java.lang.String,java.lang.String> getExecutionProperties()

CLSS public abstract interface jakarta.enterprise.concurrent.ManagedTaskListener
meth public abstract void taskAborted(java.util.concurrent.Future<?>,jakarta.enterprise.concurrent.ManagedExecutorService,java.lang.Object,java.lang.Throwable)
meth public abstract void taskDone(java.util.concurrent.Future<?>,jakarta.enterprise.concurrent.ManagedExecutorService,java.lang.Object,java.lang.Throwable)
meth public abstract void taskStarting(java.util.concurrent.Future<?>,jakarta.enterprise.concurrent.ManagedExecutorService,java.lang.Object)
meth public abstract void taskSubmitted(java.util.concurrent.Future<?>,jakarta.enterprise.concurrent.ManagedExecutorService,java.lang.Object)

CLSS public abstract interface jakarta.enterprise.concurrent.ManagedThreadFactory
intf java.util.concurrent.ForkJoinPool$ForkJoinWorkerThreadFactory
intf java.util.concurrent.ThreadFactory

CLSS public abstract interface !annotation jakarta.enterprise.concurrent.ManagedThreadFactoryDefinition
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.enterprise.concurrent.ManagedThreadFactoryDefinition$List)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
innr public abstract interface static !annotation List
intf java.lang.annotation.Annotation
meth public abstract !hasdefault int priority()
meth public abstract !hasdefault java.lang.String context()
meth public abstract java.lang.String name()

CLSS public abstract interface static !annotation jakarta.enterprise.concurrent.ManagedThreadFactoryDefinition$List
 outer jakarta.enterprise.concurrent.ManagedThreadFactoryDefinition
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.enterprise.concurrent.ManagedThreadFactoryDefinition[] value()

CLSS public jakarta.enterprise.concurrent.SkippedException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
intf java.io.Serializable
supr java.util.concurrent.ExecutionException
hfds serialVersionUID

CLSS public abstract interface jakarta.enterprise.concurrent.Trigger
meth public abstract boolean skipRun(jakarta.enterprise.concurrent.LastExecution,java.util.Date)
meth public abstract java.util.Date getNextRunTime(jakarta.enterprise.concurrent.LastExecution,java.util.Date)

CLSS public abstract interface jakarta.enterprise.concurrent.ZonedTrigger
intf jakarta.enterprise.concurrent.Trigger
meth public abstract java.time.ZonedDateTime getNextRunTime(jakarta.enterprise.concurrent.LastExecution,java.time.ZonedDateTime)
meth public boolean skipRun(jakarta.enterprise.concurrent.LastExecution,java.time.ZonedDateTime)
meth public boolean skipRun(jakarta.enterprise.concurrent.LastExecution,java.util.Date)
meth public java.time.ZoneId getZoneId()
meth public java.util.Date getNextRunTime(jakarta.enterprise.concurrent.LastExecution,java.util.Date)

CLSS public abstract interface jakarta.enterprise.concurrent.spi.ThreadContextProvider
meth public abstract jakarta.enterprise.concurrent.spi.ThreadContextSnapshot clearedContext(java.util.Map<java.lang.String,java.lang.String>)
meth public abstract jakarta.enterprise.concurrent.spi.ThreadContextSnapshot currentContext(java.util.Map<java.lang.String,java.lang.String>)
meth public abstract java.lang.String getThreadContextType()

CLSS public abstract interface jakarta.enterprise.concurrent.spi.ThreadContextRestorer
 anno 0 java.lang.FunctionalInterface()
meth public abstract void endContext()

CLSS public abstract interface jakarta.enterprise.concurrent.spi.ThreadContextSnapshot
 anno 0 java.lang.FunctionalInterface()
meth public abstract jakarta.enterprise.concurrent.spi.ThreadContextRestorer begin()

CLSS public abstract interface !annotation jakarta.interceptor.InterceptorBinding
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface java.io.Serializable

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

CLSS public abstract java.util.concurrent.AbstractExecutorService
cons public init()
intf java.util.concurrent.ExecutorService
meth protected <%0 extends java.lang.Object> java.util.concurrent.RunnableFuture<{%%0}> newTaskFor(java.lang.Runnable,{%%0})
meth protected <%0 extends java.lang.Object> java.util.concurrent.RunnableFuture<{%%0}> newTaskFor(java.util.concurrent.Callable<{%%0}>)
meth public <%0 extends java.lang.Object> java.util.List<java.util.concurrent.Future<{%%0}>> invokeAll(java.util.Collection<? extends java.util.concurrent.Callable<{%%0}>>) throws java.lang.InterruptedException
meth public <%0 extends java.lang.Object> java.util.List<java.util.concurrent.Future<{%%0}>> invokeAll(java.util.Collection<? extends java.util.concurrent.Callable<{%%0}>>,long,java.util.concurrent.TimeUnit) throws java.lang.InterruptedException
meth public <%0 extends java.lang.Object> java.util.concurrent.Future<{%%0}> submit(java.lang.Runnable,{%%0})
meth public <%0 extends java.lang.Object> java.util.concurrent.Future<{%%0}> submit(java.util.concurrent.Callable<{%%0}>)
meth public <%0 extends java.lang.Object> {%%0} invokeAny(java.util.Collection<? extends java.util.concurrent.Callable<{%%0}>>) throws java.lang.InterruptedException,java.util.concurrent.ExecutionException
meth public <%0 extends java.lang.Object> {%%0} invokeAny(java.util.Collection<? extends java.util.concurrent.Callable<{%%0}>>,long,java.util.concurrent.TimeUnit) throws java.lang.InterruptedException,java.util.concurrent.ExecutionException,java.util.concurrent.TimeoutException
meth public java.util.concurrent.Future<?> submit(java.lang.Runnable)
supr java.lang.Object

CLSS public java.util.concurrent.ExecutionException
cons protected init()
cons protected init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Exception

CLSS public abstract interface java.util.concurrent.Executor
meth public abstract void execute(java.lang.Runnable)

CLSS public abstract interface java.util.concurrent.ExecutorService
intf java.util.concurrent.Executor
meth public abstract <%0 extends java.lang.Object> java.util.List<java.util.concurrent.Future<{%%0}>> invokeAll(java.util.Collection<? extends java.util.concurrent.Callable<{%%0}>>) throws java.lang.InterruptedException
meth public abstract <%0 extends java.lang.Object> java.util.List<java.util.concurrent.Future<{%%0}>> invokeAll(java.util.Collection<? extends java.util.concurrent.Callable<{%%0}>>,long,java.util.concurrent.TimeUnit) throws java.lang.InterruptedException
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.Future<{%%0}> submit(java.lang.Runnable,{%%0})
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.Future<{%%0}> submit(java.util.concurrent.Callable<{%%0}>)
meth public abstract <%0 extends java.lang.Object> {%%0} invokeAny(java.util.Collection<? extends java.util.concurrent.Callable<{%%0}>>) throws java.lang.InterruptedException,java.util.concurrent.ExecutionException
meth public abstract <%0 extends java.lang.Object> {%%0} invokeAny(java.util.Collection<? extends java.util.concurrent.Callable<{%%0}>>,long,java.util.concurrent.TimeUnit) throws java.lang.InterruptedException,java.util.concurrent.ExecutionException,java.util.concurrent.TimeoutException
meth public abstract boolean awaitTermination(long,java.util.concurrent.TimeUnit) throws java.lang.InterruptedException
meth public abstract boolean isShutdown()
meth public abstract boolean isTerminated()
meth public abstract java.util.List<java.lang.Runnable> shutdownNow()
meth public abstract java.util.concurrent.Future<?> submit(java.lang.Runnable)
meth public abstract void shutdown()

CLSS public java.util.concurrent.ForkJoinPool
cons public init()
cons public init(int)
cons public init(int,java.util.concurrent.ForkJoinPool$ForkJoinWorkerThreadFactory,java.lang.Thread$UncaughtExceptionHandler,boolean)
cons public init(int,java.util.concurrent.ForkJoinPool$ForkJoinWorkerThreadFactory,java.lang.Thread$UncaughtExceptionHandler,boolean,int,int,int,java.util.function.Predicate<? super java.util.concurrent.ForkJoinPool>,long,java.util.concurrent.TimeUnit)
fld public final static java.util.concurrent.ForkJoinPool$ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory
innr public abstract interface static ForkJoinWorkerThreadFactory
innr public abstract interface static ManagedBlocker
meth protected <%0 extends java.lang.Object> java.util.concurrent.RunnableFuture<{%%0}> newTaskFor(java.lang.Runnable,{%%0})
meth protected <%0 extends java.lang.Object> java.util.concurrent.RunnableFuture<{%%0}> newTaskFor(java.util.concurrent.Callable<{%%0}>)
meth protected int drainTasksTo(java.util.Collection<? super java.util.concurrent.ForkJoinTask<?>>)
meth protected java.util.concurrent.ForkJoinTask<?> pollSubmission()
meth public <%0 extends java.lang.Object> java.util.List<java.util.concurrent.Future<{%%0}>> invokeAll(java.util.Collection<? extends java.util.concurrent.Callable<{%%0}>>)
meth public <%0 extends java.lang.Object> java.util.concurrent.ForkJoinTask<{%%0}> submit(java.lang.Runnable,{%%0})
meth public <%0 extends java.lang.Object> java.util.concurrent.ForkJoinTask<{%%0}> submit(java.util.concurrent.Callable<{%%0}>)
meth public <%0 extends java.lang.Object> java.util.concurrent.ForkJoinTask<{%%0}> submit(java.util.concurrent.ForkJoinTask<{%%0}>)
meth public <%0 extends java.lang.Object> {%%0} invoke(java.util.concurrent.ForkJoinTask<{%%0}>)
meth public boolean awaitQuiescence(long,java.util.concurrent.TimeUnit)
meth public boolean awaitTermination(long,java.util.concurrent.TimeUnit) throws java.lang.InterruptedException
meth public boolean getAsyncMode()
meth public boolean hasQueuedSubmissions()
meth public boolean isQuiescent()
meth public boolean isShutdown()
meth public boolean isTerminated()
meth public boolean isTerminating()
meth public int getActiveThreadCount()
meth public int getParallelism()
meth public int getPoolSize()
meth public int getQueuedSubmissionCount()
meth public int getRunningThreadCount()
meth public java.lang.String toString()
meth public java.lang.Thread$UncaughtExceptionHandler getUncaughtExceptionHandler()
meth public java.util.List<java.lang.Runnable> shutdownNow()
meth public java.util.concurrent.ForkJoinPool$ForkJoinWorkerThreadFactory getFactory()
meth public java.util.concurrent.ForkJoinTask<?> submit(java.lang.Runnable)
meth public long getQueuedTaskCount()
meth public long getStealCount()
meth public static int getCommonPoolParallelism()
meth public static java.util.concurrent.ForkJoinPool commonPool()
meth public static void managedBlock(java.util.concurrent.ForkJoinPool$ManagedBlocker) throws java.lang.InterruptedException
meth public void execute(java.lang.Runnable)
meth public void execute(java.util.concurrent.ForkJoinTask<?>)
meth public void shutdown()
supr java.util.concurrent.AbstractExecutorService

CLSS public abstract interface static java.util.concurrent.ForkJoinPool$ForkJoinWorkerThreadFactory
 outer java.util.concurrent.ForkJoinPool
meth public abstract java.util.concurrent.ForkJoinWorkerThread newThread(java.util.concurrent.ForkJoinPool)

CLSS public abstract interface java.util.concurrent.ScheduledExecutorService
intf java.util.concurrent.ExecutorService
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.ScheduledFuture<{%%0}> schedule(java.util.concurrent.Callable<{%%0}>,long,java.util.concurrent.TimeUnit)
meth public abstract java.util.concurrent.ScheduledFuture<?> schedule(java.lang.Runnable,long,java.util.concurrent.TimeUnit)
meth public abstract java.util.concurrent.ScheduledFuture<?> scheduleAtFixedRate(java.lang.Runnable,long,long,java.util.concurrent.TimeUnit)
meth public abstract java.util.concurrent.ScheduledFuture<?> scheduleWithFixedDelay(java.lang.Runnable,long,long,java.util.concurrent.TimeUnit)

CLSS public abstract interface java.util.concurrent.ThreadFactory
meth public abstract java.lang.Thread newThread(java.lang.Runnable)

