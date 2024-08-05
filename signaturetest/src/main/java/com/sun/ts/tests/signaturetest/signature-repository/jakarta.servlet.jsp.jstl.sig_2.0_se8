#Signature file v4.1
#Version 3.0

CLSS public abstract jakarta.el.Expression
cons public init()
intf java.io.Serializable
meth public abstract boolean equals(java.lang.Object)
meth public abstract boolean isLiteralText()
meth public abstract int hashCode()
meth public abstract java.lang.String getExpressionString()
supr java.lang.Object
hfds serialVersionUID

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

CLSS public abstract jakarta.servlet.jsp.jstl.core.ConditionalTagSupport
cons public init()
meth protected abstract boolean condition() throws jakarta.servlet.jsp.JspTagException
meth public int doStartTag() throws jakarta.servlet.jsp.JspException
meth public void release()
meth public void setScope(java.lang.String)
meth public void setVar(java.lang.String)
supr jakarta.servlet.jsp.tagext.TagSupport
hfds result,scope,var

CLSS public jakarta.servlet.jsp.jstl.core.Config
cons public init()
fld public final static java.lang.String FMT_FALLBACK_LOCALE = "jakarta.servlet.jsp.jstl.fmt.fallbackLocale"
fld public final static java.lang.String FMT_LOCALE = "jakarta.servlet.jsp.jstl.fmt.locale"
fld public final static java.lang.String FMT_LOCALIZATION_CONTEXT = "jakarta.servlet.jsp.jstl.fmt.localizationContext"
fld public final static java.lang.String FMT_TIME_ZONE = "jakarta.servlet.jsp.jstl.fmt.timeZone"
fld public final static java.lang.String SQL_DATA_SOURCE = "jakarta.servlet.jsp.jstl.sql.dataSource"
fld public final static java.lang.String SQL_MAX_ROWS = "jakarta.servlet.jsp.jstl.sql.maxRows"
meth public static java.lang.Object find(jakarta.servlet.jsp.PageContext,java.lang.String)
meth public static java.lang.Object get(jakarta.servlet.ServletContext,java.lang.String)
meth public static java.lang.Object get(jakarta.servlet.ServletRequest,java.lang.String)
meth public static java.lang.Object get(jakarta.servlet.http.HttpSession,java.lang.String)
meth public static java.lang.Object get(jakarta.servlet.jsp.PageContext,java.lang.String,int)
meth public static void remove(jakarta.servlet.ServletContext,java.lang.String)
meth public static void remove(jakarta.servlet.ServletRequest,java.lang.String)
meth public static void remove(jakarta.servlet.http.HttpSession,java.lang.String)
meth public static void remove(jakarta.servlet.jsp.PageContext,java.lang.String,int)
meth public static void set(jakarta.servlet.ServletContext,java.lang.String,java.lang.Object)
meth public static void set(jakarta.servlet.ServletRequest,java.lang.String,java.lang.Object)
meth public static void set(jakarta.servlet.http.HttpSession,java.lang.String,java.lang.Object)
meth public static void set(jakarta.servlet.jsp.PageContext,java.lang.String,java.lang.Object,int)
supr java.lang.Object
hfds APPLICATION_SCOPE_SUFFIX,PAGE_SCOPE_SUFFIX,REQUEST_SCOPE_SUFFIX,SESSION_SCOPE_SUFFIX

CLSS public final jakarta.servlet.jsp.jstl.core.IndexedValueExpression
cons public init(jakarta.el.ValueExpression,int)
fld protected final jakarta.el.ValueExpression orig
fld protected final java.lang.Integer i
meth public boolean equals(java.lang.Object)
meth public boolean isLiteralText()
meth public boolean isReadOnly(jakarta.el.ELContext)
meth public int hashCode()
meth public java.lang.Class getExpectedType()
meth public java.lang.Class getType(jakarta.el.ELContext)
meth public java.lang.Object getValue(jakarta.el.ELContext)
meth public java.lang.String getExpressionString()
meth public void setValue(jakarta.el.ELContext,java.lang.Object)
supr jakarta.el.ValueExpression
hfds serialVersionUID

CLSS public final jakarta.servlet.jsp.jstl.core.IteratedExpression
cons public init(jakarta.el.ValueExpression,java.lang.String)
fld protected final jakarta.el.ValueExpression orig
fld protected final java.lang.String delims
meth public jakarta.el.ValueExpression getValueExpression()
meth public java.lang.Object getItem(jakarta.el.ELContext,int)
supr java.lang.Object
hfds base,index,iter,serialVersionUID

CLSS public final jakarta.servlet.jsp.jstl.core.IteratedValueExpression
cons public init(jakarta.servlet.jsp.jstl.core.IteratedExpression,int)
fld protected final int i
fld protected final jakarta.servlet.jsp.jstl.core.IteratedExpression iteratedExpression
meth public boolean equals(java.lang.Object)
meth public boolean isLiteralText()
meth public boolean isReadOnly(jakarta.el.ELContext)
meth public int hashCode()
meth public java.lang.Class getExpectedType()
meth public java.lang.Class getType(jakarta.el.ELContext)
meth public java.lang.Object getValue(jakarta.el.ELContext)
meth public java.lang.String getExpressionString()
meth public void setValue(jakarta.el.ELContext,java.lang.Object)
supr jakarta.el.ValueExpression
hfds serialVersionUID

CLSS public abstract interface jakarta.servlet.jsp.jstl.core.LoopTag
intf jakarta.servlet.jsp.tagext.Tag
meth public abstract jakarta.servlet.jsp.jstl.core.LoopTagStatus getLoopStatus()
meth public abstract java.lang.Object getCurrent()

CLSS public abstract interface jakarta.servlet.jsp.jstl.core.LoopTagStatus
meth public abstract boolean isFirst()
meth public abstract boolean isLast()
meth public abstract int getCount()
meth public abstract int getIndex()
meth public abstract java.lang.Integer getBegin()
meth public abstract java.lang.Integer getEnd()
meth public abstract java.lang.Integer getStep()
meth public abstract java.lang.Object getCurrent()

CLSS public abstract jakarta.servlet.jsp.jstl.core.LoopTagSupport
cons public init()
fld protected boolean beginSpecified
fld protected boolean endSpecified
fld protected boolean stepSpecified
fld protected int begin
fld protected int end
fld protected int step
fld protected jakarta.el.ValueExpression deferredExpression
fld protected java.lang.String itemId
fld protected java.lang.String statusId
intf jakarta.servlet.jsp.jstl.core.LoopTag
intf jakarta.servlet.jsp.tagext.IterationTag
intf jakarta.servlet.jsp.tagext.TryCatchFinally
meth protected abstract boolean hasNext() throws jakarta.servlet.jsp.JspTagException
meth protected abstract java.lang.Object next() throws jakarta.servlet.jsp.JspTagException
meth protected abstract void prepare() throws jakarta.servlet.jsp.JspTagException
meth protected java.lang.String getDelims()
meth protected void validateBegin() throws jakarta.servlet.jsp.JspTagException
meth protected void validateEnd() throws jakarta.servlet.jsp.JspTagException
meth protected void validateStep() throws jakarta.servlet.jsp.JspTagException
meth public int doAfterBody() throws jakarta.servlet.jsp.JspException
meth public int doStartTag() throws jakarta.servlet.jsp.JspException
meth public jakarta.servlet.jsp.jstl.core.LoopTagStatus getLoopStatus()
meth public java.lang.Object getCurrent()
meth public void doCatch(java.lang.Throwable) throws java.lang.Throwable
meth public void doFinally()
meth public void release()
meth public void setVar(java.lang.String)
meth public void setVarStatus(java.lang.String)
supr jakarta.servlet.jsp.tagext.TagSupport
hfds count,index,item,iteratedExpression,last,oldMappedValue,status

CLSS public jakarta.servlet.jsp.jstl.fmt.LocaleSupport
cons public init()
meth public static java.lang.String getLocalizedMessage(jakarta.servlet.jsp.PageContext,java.lang.String)
meth public static java.lang.String getLocalizedMessage(jakarta.servlet.jsp.PageContext,java.lang.String,java.lang.Object[])
meth public static java.lang.String getLocalizedMessage(jakarta.servlet.jsp.PageContext,java.lang.String,java.lang.Object[],java.lang.String)
meth public static java.lang.String getLocalizedMessage(jakarta.servlet.jsp.PageContext,java.lang.String,java.lang.String)
supr java.lang.Object
hfds EMPTY_LOCALE,HYPHEN,REQUEST_CHAR_SET,UNDEFINED_KEY,UNDERSCORE

CLSS public jakarta.servlet.jsp.jstl.fmt.LocalizationContext
cons public init()
cons public init(java.util.ResourceBundle)
cons public init(java.util.ResourceBundle,java.util.Locale)
meth public java.util.Locale getLocale()
meth public java.util.ResourceBundle getResourceBundle()
supr java.lang.Object
hfds bundle,locale

CLSS public abstract interface jakarta.servlet.jsp.jstl.sql.Result
meth public abstract boolean isLimitedByMaxRows()
meth public abstract int getRowCount()
meth public abstract java.lang.Object[][] getRowsByIndex()
meth public abstract java.lang.String[] getColumnNames()
meth public abstract java.util.SortedMap[] getRows()

CLSS public jakarta.servlet.jsp.jstl.sql.ResultSupport
cons public init()
meth public static jakarta.servlet.jsp.jstl.sql.Result toResult(java.sql.ResultSet)
meth public static jakarta.servlet.jsp.jstl.sql.Result toResult(java.sql.ResultSet,int)
supr java.lang.Object

CLSS public abstract interface jakarta.servlet.jsp.jstl.sql.SQLExecutionTag
meth public abstract void addSQLParameter(java.lang.Object)

CLSS public jakarta.servlet.jsp.jstl.tlv.PermittedTaglibsTLV
cons public init()
meth public jakarta.servlet.jsp.tagext.ValidationMessage[] validate(java.lang.String,java.lang.String,jakarta.servlet.jsp.tagext.PageData)
meth public void release()
supr jakarta.servlet.jsp.tagext.TagLibraryValidator
hfds JSP_ROOT_NAME,JSP_ROOT_QN,JSP_ROOT_URI,PERMITTED_TAGLIBS_PARAM,failed,permittedTaglibs,uri
hcls PermittedTaglibsHandler

CLSS public jakarta.servlet.jsp.jstl.tlv.ScriptFreeTLV
cons public init()
meth public jakarta.servlet.jsp.tagext.ValidationMessage[] validate(java.lang.String,java.lang.String,jakarta.servlet.jsp.tagext.PageData)
meth public void setInitParameters(java.util.Map<java.lang.String,java.lang.Object>)
supr jakarta.servlet.jsp.tagext.TagLibraryValidator
hfds allowDeclarations,allowExpressions,allowRTExpressions,allowScriptlets,factory
hcls MyContentHandler

CLSS public abstract interface jakarta.servlet.jsp.tagext.IterationTag
fld public final static int EVAL_BODY_AGAIN = 2
intf jakarta.servlet.jsp.tagext.Tag
meth public abstract int doAfterBody() throws jakarta.servlet.jsp.JspException

CLSS public abstract interface jakarta.servlet.jsp.tagext.JspTag

CLSS public abstract interface jakarta.servlet.jsp.tagext.Tag
fld public final static int EVAL_BODY_INCLUDE = 1
fld public final static int EVAL_PAGE = 6
fld public final static int SKIP_BODY = 0
fld public final static int SKIP_PAGE = 5
intf jakarta.servlet.jsp.tagext.JspTag
meth public abstract int doEndTag() throws jakarta.servlet.jsp.JspException
meth public abstract int doStartTag() throws jakarta.servlet.jsp.JspException
meth public abstract jakarta.servlet.jsp.tagext.Tag getParent()
meth public abstract void release()
meth public abstract void setPageContext(jakarta.servlet.jsp.PageContext)
meth public abstract void setParent(jakarta.servlet.jsp.tagext.Tag)

CLSS public abstract jakarta.servlet.jsp.tagext.TagLibraryValidator
cons public init()
meth public jakarta.servlet.jsp.tagext.ValidationMessage[] validate(java.lang.String,java.lang.String,jakarta.servlet.jsp.tagext.PageData)
meth public java.util.Map<java.lang.String,java.lang.Object> getInitParameters()
meth public void release()
meth public void setInitParameters(java.util.Map<java.lang.String,java.lang.Object>)
supr java.lang.Object
hfds initParameters

CLSS public jakarta.servlet.jsp.tagext.TagSupport
cons public init()
fld protected jakarta.servlet.jsp.PageContext pageContext
fld protected java.lang.String id
intf jakarta.servlet.jsp.tagext.IterationTag
intf java.io.Serializable
meth public final static jakarta.servlet.jsp.tagext.Tag findAncestorWithClass(jakarta.servlet.jsp.tagext.Tag,java.lang.Class<?>)
meth public int doAfterBody() throws jakarta.servlet.jsp.JspException
meth public int doEndTag() throws jakarta.servlet.jsp.JspException
meth public int doStartTag() throws jakarta.servlet.jsp.JspException
meth public jakarta.servlet.jsp.tagext.Tag getParent()
meth public java.lang.Object getValue(java.lang.String)
meth public java.lang.String getId()
meth public java.util.Enumeration<java.lang.String> getValues()
meth public void release()
meth public void removeValue(java.lang.String)
meth public void setId(java.lang.String)
meth public void setPageContext(jakarta.servlet.jsp.PageContext)
meth public void setParent(jakarta.servlet.jsp.tagext.Tag)
meth public void setValue(java.lang.String,java.lang.Object)
supr java.lang.Object
hfds parent,serialVersionUID,values

CLSS public abstract interface jakarta.servlet.jsp.tagext.TryCatchFinally
meth public abstract void doCatch(java.lang.Throwable) throws java.lang.Throwable
meth public abstract void doFinally()

CLSS public abstract interface java.io.Serializable

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

