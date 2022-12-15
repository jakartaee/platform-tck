#Signature file v4.1
#Version 2.0

CLSS public abstract interface !annotation jakarta.enterprise.context.NormalScope
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean passivating()

CLSS public abstract interface !annotation jakarta.interceptor.InterceptorBinding
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public jakarta.transaction.HeuristicCommitException
cons public init()
cons public init(java.lang.String)
supr java.lang.Exception
hfds serialVersionUID

CLSS public jakarta.transaction.HeuristicMixedException
cons public init()
cons public init(java.lang.String)
supr java.lang.Exception
hfds serialVersionUID

CLSS public jakarta.transaction.HeuristicRollbackException
cons public init()
cons public init(java.lang.String)
supr java.lang.Exception
hfds serialVersionUID

CLSS public jakarta.transaction.InvalidTransactionException
cons public init()
cons public init(java.lang.String)
supr java.rmi.RemoteException
hfds serialVersionUID

CLSS public jakarta.transaction.NotSupportedException
cons public init()
cons public init(java.lang.String)
supr java.lang.Exception
hfds serialVersionUID

CLSS public jakarta.transaction.RollbackException
cons public init()
cons public init(java.lang.String)
supr java.lang.Exception
hfds serialVersionUID

CLSS public abstract interface jakarta.transaction.Status
fld public final static int STATUS_ACTIVE = 0
fld public final static int STATUS_COMMITTED = 3
fld public final static int STATUS_COMMITTING = 8
fld public final static int STATUS_MARKED_ROLLBACK = 1
fld public final static int STATUS_NO_TRANSACTION = 6
fld public final static int STATUS_PREPARED = 2
fld public final static int STATUS_PREPARING = 7
fld public final static int STATUS_ROLLEDBACK = 4
fld public final static int STATUS_ROLLING_BACK = 9
fld public final static int STATUS_UNKNOWN = 5

CLSS public abstract interface jakarta.transaction.Synchronization
meth public abstract void afterCompletion(int)
meth public abstract void beforeCompletion()

CLSS public jakarta.transaction.SystemException
cons public init()
cons public init(int)
cons public init(java.lang.String)
fld public int errorCode
supr java.lang.Exception
hfds serialVersionUID

CLSS public abstract interface jakarta.transaction.Transaction
meth public abstract boolean delistResource(javax.transaction.xa.XAResource,int) throws jakarta.transaction.SystemException
meth public abstract boolean enlistResource(javax.transaction.xa.XAResource) throws jakarta.transaction.RollbackException,jakarta.transaction.SystemException
meth public abstract int getStatus() throws jakarta.transaction.SystemException
meth public abstract void commit() throws jakarta.transaction.HeuristicMixedException,jakarta.transaction.HeuristicRollbackException,jakarta.transaction.RollbackException,jakarta.transaction.SystemException
meth public abstract void registerSynchronization(jakarta.transaction.Synchronization) throws jakarta.transaction.RollbackException,jakarta.transaction.SystemException
meth public abstract void rollback() throws jakarta.transaction.SystemException
meth public abstract void setRollbackOnly() throws jakarta.transaction.SystemException

CLSS public abstract interface jakarta.transaction.TransactionManager
meth public abstract int getStatus() throws jakarta.transaction.SystemException
meth public abstract jakarta.transaction.Transaction getTransaction() throws jakarta.transaction.SystemException
meth public abstract jakarta.transaction.Transaction suspend() throws jakarta.transaction.SystemException
meth public abstract void begin() throws jakarta.transaction.NotSupportedException,jakarta.transaction.SystemException
meth public abstract void commit() throws jakarta.transaction.HeuristicMixedException,jakarta.transaction.HeuristicRollbackException,jakarta.transaction.RollbackException,jakarta.transaction.SystemException
meth public abstract void resume(jakarta.transaction.Transaction) throws jakarta.transaction.InvalidTransactionException,jakarta.transaction.SystemException
meth public abstract void rollback() throws jakarta.transaction.SystemException
meth public abstract void setRollbackOnly() throws jakarta.transaction.SystemException
meth public abstract void setTransactionTimeout(int) throws jakarta.transaction.SystemException

CLSS public jakarta.transaction.TransactionRequiredException
cons public init()
cons public init(java.lang.String)
supr java.rmi.RemoteException
hfds serialVersionUID

CLSS public jakarta.transaction.TransactionRolledbackException
cons public init()
cons public init(java.lang.String)
supr java.rmi.RemoteException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.transaction.TransactionScoped
 anno 0 jakarta.enterprise.context.NormalScope(boolean passivating=true)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
intf java.lang.annotation.Annotation

CLSS public abstract interface jakarta.transaction.TransactionSynchronizationRegistry
meth public abstract boolean getRollbackOnly()
meth public abstract int getTransactionStatus()
meth public abstract java.lang.Object getResource(java.lang.Object)
meth public abstract java.lang.Object getTransactionKey()
meth public abstract void putResource(java.lang.Object,java.lang.Object)
meth public abstract void registerInterposedSynchronization(jakarta.transaction.Synchronization)
meth public abstract void setRollbackOnly()

CLSS public abstract interface !annotation jakarta.transaction.Transactional
 anno 0 jakarta.interceptor.InterceptorBinding()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD])
innr public final static !enum TxType
intf java.lang.annotation.Annotation
meth public abstract !hasdefault jakarta.transaction.Transactional$TxType value()
meth public abstract !hasdefault java.lang.Class[] dontRollbackOn()
meth public abstract !hasdefault java.lang.Class[] rollbackOn()

CLSS public final static !enum jakarta.transaction.Transactional$TxType
 outer jakarta.transaction.Transactional
fld public final static jakarta.transaction.Transactional$TxType MANDATORY
fld public final static jakarta.transaction.Transactional$TxType NEVER
fld public final static jakarta.transaction.Transactional$TxType NOT_SUPPORTED
fld public final static jakarta.transaction.Transactional$TxType REQUIRED
fld public final static jakarta.transaction.Transactional$TxType REQUIRES_NEW
fld public final static jakarta.transaction.Transactional$TxType SUPPORTS
meth public static jakarta.transaction.Transactional$TxType valueOf(java.lang.String)
meth public static jakarta.transaction.Transactional$TxType[] values()
supr java.lang.Enum<jakarta.transaction.Transactional$TxType>

CLSS public jakarta.transaction.TransactionalException
cons public init(java.lang.String,java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public abstract interface jakarta.transaction.UserTransaction
meth public abstract int getStatus() throws jakarta.transaction.SystemException
meth public abstract void begin() throws jakarta.transaction.NotSupportedException,jakarta.transaction.SystemException
meth public abstract void commit() throws jakarta.transaction.HeuristicMixedException,jakarta.transaction.HeuristicRollbackException,jakarta.transaction.RollbackException,jakarta.transaction.SystemException
meth public abstract void rollback() throws jakarta.transaction.SystemException
meth public abstract void setRollbackOnly() throws jakarta.transaction.SystemException
meth public abstract void setTransactionTimeout(int) throws jakarta.transaction.SystemException

CLSS public java.io.IOException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Exception

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

CLSS public java.rmi.RemoteException
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
fld public java.lang.Throwable detail
meth public java.lang.String getMessage()
meth public java.lang.Throwable getCause()
supr java.io.IOException

