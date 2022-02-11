#Signature file v4.1
#Version 1.0

CLSS public jakarta.enterprise.concurrent.AbortedException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
intf java.io.Serializable
supr java.util.concurrent.ExecutionException
hfds serialVersionUID

CLSS public abstract interface jakarta.enterprise.concurrent.ContextService
meth public abstract !varargs java.lang.Object createContextualProxy(java.lang.Object,java.lang.Class<?>[])
meth public abstract !varargs java.lang.Object createContextualProxy(java.lang.Object,java.util.Map<java.lang.String,java.lang.String>,java.lang.Class<?>[])
meth public abstract <%0 extends java.lang.Object> {%%0} createContextualProxy({%%0},java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> {%%0} createContextualProxy({%%0},java.util.Map<java.lang.String,java.lang.String>,java.lang.Class<{%%0}>)
meth public abstract java.util.Map<java.lang.String,java.lang.String> getExecutionProperties(java.lang.Object)

CLSS public abstract interface jakarta.enterprise.concurrent.LastExecution
meth public abstract java.lang.Object getResult()
meth public abstract java.lang.String getIdentityName()
meth public abstract java.util.Date getRunEnd()
meth public abstract java.util.Date getRunStart()
meth public abstract java.util.Date getScheduledStart()

CLSS public abstract interface jakarta.enterprise.concurrent.ManageableThread
meth public abstract boolean isShutdown()

CLSS public abstract interface jakarta.enterprise.concurrent.ManagedExecutorService
intf java.util.concurrent.ExecutorService

CLSS public jakarta.enterprise.concurrent.ManagedExecutors
meth public static <%0 extends java.lang.Object> java.util.concurrent.Callable<{%%0}> managedTask(java.util.concurrent.Callable<{%%0}>,jakarta.enterprise.concurrent.ManagedTaskListener)
meth public static <%0 extends java.lang.Object> java.util.concurrent.Callable<{%%0}> managedTask(java.util.concurrent.Callable<{%%0}>,java.util.Map<java.lang.String,java.lang.String>,jakarta.enterprise.concurrent.ManagedTaskListener)
meth public static boolean isCurrentThreadShutdown()
meth public static java.lang.Runnable managedTask(java.lang.Runnable,jakarta.enterprise.concurrent.ManagedTaskListener)
meth public static java.lang.Runnable managedTask(java.lang.Runnable,java.util.Map<java.lang.String,java.lang.String>,jakarta.enterprise.concurrent.ManagedTaskListener)
supr java.lang.Object
hfds NULL_TASK_ERROR_MSG
hcls Adapter,CallableAdapter,RunnableAdapter

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
intf java.util.concurrent.ThreadFactory

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

CLSS public abstract interface java.util.concurrent.ScheduledExecutorService
intf java.util.concurrent.ExecutorService
meth public abstract <%0 extends java.lang.Object> java.util.concurrent.ScheduledFuture<{%%0}> schedule(java.util.concurrent.Callable<{%%0}>,long,java.util.concurrent.TimeUnit)
meth public abstract java.util.concurrent.ScheduledFuture<?> schedule(java.lang.Runnable,long,java.util.concurrent.TimeUnit)
meth public abstract java.util.concurrent.ScheduledFuture<?> scheduleAtFixedRate(java.lang.Runnable,long,long,java.util.concurrent.TimeUnit)
meth public abstract java.util.concurrent.ScheduledFuture<?> scheduleWithFixedDelay(java.lang.Runnable,long,long,java.util.concurrent.TimeUnit)

CLSS public abstract interface java.util.concurrent.ThreadFactory
meth public abstract java.lang.Thread newThread(java.lang.Runnable)

