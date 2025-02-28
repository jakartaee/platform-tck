#Signature file v4.1
#Version 2.1

CLSS public final jakarta.json.Json
meth public static jakarta.json.JsonArrayBuilder createArrayBuilder()
meth public static jakarta.json.JsonArrayBuilder createArrayBuilder(jakarta.json.JsonArray)
meth public static jakarta.json.JsonArrayBuilder createArrayBuilder(java.util.Collection<?>)
meth public static jakarta.json.JsonBuilderFactory createBuilderFactory(java.util.Map<java.lang.String,?>)
meth public static jakarta.json.JsonMergePatch createMergeDiff(jakarta.json.JsonValue,jakarta.json.JsonValue)
meth public static jakarta.json.JsonMergePatch createMergePatch(jakarta.json.JsonValue)
meth public static jakarta.json.JsonNumber createValue(double)
meth public static jakarta.json.JsonNumber createValue(int)
meth public static jakarta.json.JsonNumber createValue(java.lang.Number)
meth public static jakarta.json.JsonNumber createValue(java.math.BigDecimal)
meth public static jakarta.json.JsonNumber createValue(java.math.BigInteger)
meth public static jakarta.json.JsonNumber createValue(long)
meth public static jakarta.json.JsonObjectBuilder createObjectBuilder()
meth public static jakarta.json.JsonObjectBuilder createObjectBuilder(jakarta.json.JsonObject)
meth public static jakarta.json.JsonObjectBuilder createObjectBuilder(java.util.Map<java.lang.String,?>)
meth public static jakarta.json.JsonPatch createDiff(jakarta.json.JsonStructure,jakarta.json.JsonStructure)
meth public static jakarta.json.JsonPatch createPatch(jakarta.json.JsonArray)
meth public static jakarta.json.JsonPatchBuilder createPatchBuilder()
meth public static jakarta.json.JsonPatchBuilder createPatchBuilder(jakarta.json.JsonArray)
meth public static jakarta.json.JsonPointer createPointer(java.lang.String)
meth public static jakarta.json.JsonReader createReader(java.io.InputStream)
meth public static jakarta.json.JsonReader createReader(java.io.Reader)
meth public static jakarta.json.JsonReaderFactory createReaderFactory(java.util.Map<java.lang.String,?>)
meth public static jakarta.json.JsonString createValue(java.lang.String)
meth public static jakarta.json.JsonWriter createWriter(java.io.OutputStream)
meth public static jakarta.json.JsonWriter createWriter(java.io.Writer)
meth public static jakarta.json.JsonWriterFactory createWriterFactory(java.util.Map<java.lang.String,?>)
meth public static jakarta.json.stream.JsonGenerator createGenerator(java.io.OutputStream)
meth public static jakarta.json.stream.JsonGenerator createGenerator(java.io.Writer)
meth public static jakarta.json.stream.JsonGeneratorFactory createGeneratorFactory(java.util.Map<java.lang.String,?>)
meth public static jakarta.json.stream.JsonParser createParser(java.io.InputStream)
meth public static jakarta.json.stream.JsonParser createParser(java.io.Reader)
meth public static jakarta.json.stream.JsonParserFactory createParserFactory(java.util.Map<java.lang.String,?>)
meth public static java.lang.String decodePointer(java.lang.String)
meth public static java.lang.String encodePointer(java.lang.String)
supr java.lang.Object

CLSS public abstract interface jakarta.json.JsonArray
intf jakarta.json.JsonStructure
intf java.util.List<jakarta.json.JsonValue>
meth public <%0 extends java.lang.Object, %1 extends jakarta.json.JsonValue> java.util.List<{%%0}> getValuesAs(java.util.function.Function<{%%1},{%%0}>)
meth public abstract <%0 extends jakarta.json.JsonValue> java.util.List<{%%0}> getValuesAs(java.lang.Class<{%%0}>)
meth public abstract boolean getBoolean(int)
meth public abstract boolean getBoolean(int,boolean)
meth public abstract boolean isNull(int)
meth public abstract int getInt(int)
meth public abstract int getInt(int,int)
meth public abstract jakarta.json.JsonArray getJsonArray(int)
meth public abstract jakarta.json.JsonNumber getJsonNumber(int)
meth public abstract jakarta.json.JsonObject getJsonObject(int)
meth public abstract jakarta.json.JsonString getJsonString(int)
meth public abstract java.lang.String getString(int)
meth public abstract java.lang.String getString(int,java.lang.String)

CLSS public abstract interface jakarta.json.JsonArrayBuilder
meth public abstract jakarta.json.JsonArray build()
meth public abstract jakarta.json.JsonArrayBuilder add(boolean)
meth public abstract jakarta.json.JsonArrayBuilder add(double)
meth public abstract jakarta.json.JsonArrayBuilder add(int)
meth public abstract jakarta.json.JsonArrayBuilder add(jakarta.json.JsonArrayBuilder)
meth public abstract jakarta.json.JsonArrayBuilder add(jakarta.json.JsonObjectBuilder)
meth public abstract jakarta.json.JsonArrayBuilder add(jakarta.json.JsonValue)
meth public abstract jakarta.json.JsonArrayBuilder add(java.lang.String)
meth public abstract jakarta.json.JsonArrayBuilder add(java.math.BigDecimal)
meth public abstract jakarta.json.JsonArrayBuilder add(java.math.BigInteger)
meth public abstract jakarta.json.JsonArrayBuilder add(long)
meth public abstract jakarta.json.JsonArrayBuilder addNull()
meth public jakarta.json.JsonArrayBuilder add(int,boolean)
meth public jakarta.json.JsonArrayBuilder add(int,double)
meth public jakarta.json.JsonArrayBuilder add(int,int)
meth public jakarta.json.JsonArrayBuilder add(int,jakarta.json.JsonArrayBuilder)
meth public jakarta.json.JsonArrayBuilder add(int,jakarta.json.JsonObjectBuilder)
meth public jakarta.json.JsonArrayBuilder add(int,jakarta.json.JsonValue)
meth public jakarta.json.JsonArrayBuilder add(int,java.lang.String)
meth public jakarta.json.JsonArrayBuilder add(int,java.math.BigDecimal)
meth public jakarta.json.JsonArrayBuilder add(int,java.math.BigInteger)
meth public jakarta.json.JsonArrayBuilder add(int,long)
meth public jakarta.json.JsonArrayBuilder addAll(jakarta.json.JsonArrayBuilder)
meth public jakarta.json.JsonArrayBuilder addNull(int)
meth public jakarta.json.JsonArrayBuilder remove(int)
meth public jakarta.json.JsonArrayBuilder set(int,boolean)
meth public jakarta.json.JsonArrayBuilder set(int,double)
meth public jakarta.json.JsonArrayBuilder set(int,int)
meth public jakarta.json.JsonArrayBuilder set(int,jakarta.json.JsonArrayBuilder)
meth public jakarta.json.JsonArrayBuilder set(int,jakarta.json.JsonObjectBuilder)
meth public jakarta.json.JsonArrayBuilder set(int,jakarta.json.JsonValue)
meth public jakarta.json.JsonArrayBuilder set(int,java.lang.String)
meth public jakarta.json.JsonArrayBuilder set(int,java.math.BigDecimal)
meth public jakarta.json.JsonArrayBuilder set(int,java.math.BigInteger)
meth public jakarta.json.JsonArrayBuilder set(int,long)
meth public jakarta.json.JsonArrayBuilder setNull(int)

CLSS public abstract interface jakarta.json.JsonBuilderFactory
meth public abstract jakarta.json.JsonArrayBuilder createArrayBuilder()
meth public abstract jakarta.json.JsonObjectBuilder createObjectBuilder()
meth public abstract java.util.Map<java.lang.String,?> getConfigInUse()
meth public jakarta.json.JsonArrayBuilder createArrayBuilder(jakarta.json.JsonArray)
meth public jakarta.json.JsonArrayBuilder createArrayBuilder(java.util.Collection<?>)
meth public jakarta.json.JsonObjectBuilder createObjectBuilder(jakarta.json.JsonObject)
meth public jakarta.json.JsonObjectBuilder createObjectBuilder(java.util.Map<java.lang.String,java.lang.Object>)

CLSS public final jakarta.json.JsonConfig
fld public final static java.lang.String KEY_STRATEGY = "jakarta.json.JsonConfig.keyStrategy"
innr public final static !enum KeyStrategy
supr java.lang.Object

CLSS public final static !enum jakarta.json.JsonConfig$KeyStrategy
 outer jakarta.json.JsonConfig
fld public final static jakarta.json.JsonConfig$KeyStrategy FIRST
fld public final static jakarta.json.JsonConfig$KeyStrategy LAST
fld public final static jakarta.json.JsonConfig$KeyStrategy NONE
meth public static jakarta.json.JsonConfig$KeyStrategy valueOf(java.lang.String)
meth public static jakarta.json.JsonConfig$KeyStrategy[] values()
supr java.lang.Enum<jakarta.json.JsonConfig$KeyStrategy>

CLSS public jakarta.json.JsonException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public abstract interface jakarta.json.JsonMergePatch
meth public abstract jakarta.json.JsonValue apply(jakarta.json.JsonValue)
meth public abstract jakarta.json.JsonValue toJsonValue()

CLSS public abstract interface jakarta.json.JsonNumber
intf jakarta.json.JsonValue
meth public abstract boolean equals(java.lang.Object)
meth public abstract boolean isIntegral()
meth public abstract double doubleValue()
meth public abstract int hashCode()
meth public abstract int intValue()
meth public abstract int intValueExact()
meth public abstract java.lang.String toString()
meth public abstract java.math.BigDecimal bigDecimalValue()
meth public abstract java.math.BigInteger bigIntegerValue()
meth public abstract java.math.BigInteger bigIntegerValueExact()
meth public abstract long longValue()
meth public abstract long longValueExact()
meth public java.lang.Number numberValue()

CLSS public abstract interface jakarta.json.JsonObject
intf jakarta.json.JsonStructure
intf java.util.Map<java.lang.String,jakarta.json.JsonValue>
meth public abstract boolean getBoolean(java.lang.String)
meth public abstract boolean getBoolean(java.lang.String,boolean)
meth public abstract boolean isNull(java.lang.String)
meth public abstract int getInt(java.lang.String)
meth public abstract int getInt(java.lang.String,int)
meth public abstract jakarta.json.JsonArray getJsonArray(java.lang.String)
meth public abstract jakarta.json.JsonNumber getJsonNumber(java.lang.String)
meth public abstract jakarta.json.JsonObject getJsonObject(java.lang.String)
meth public abstract jakarta.json.JsonString getJsonString(java.lang.String)
meth public abstract java.lang.String getString(java.lang.String)
meth public abstract java.lang.String getString(java.lang.String,java.lang.String)

CLSS public abstract interface jakarta.json.JsonObjectBuilder
meth public abstract jakarta.json.JsonObject build()
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,boolean)
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,double)
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,int)
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,jakarta.json.JsonArrayBuilder)
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,jakarta.json.JsonObjectBuilder)
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,jakarta.json.JsonValue)
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,java.lang.String)
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,java.math.BigDecimal)
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,java.math.BigInteger)
meth public abstract jakarta.json.JsonObjectBuilder add(java.lang.String,long)
meth public abstract jakarta.json.JsonObjectBuilder addNull(java.lang.String)
meth public jakarta.json.JsonObjectBuilder addAll(jakarta.json.JsonObjectBuilder)
meth public jakarta.json.JsonObjectBuilder remove(java.lang.String)

CLSS public abstract interface jakarta.json.JsonPatch
innr public final static !enum Operation
meth public abstract <%0 extends jakarta.json.JsonStructure> {%%0} apply({%%0})
meth public abstract jakarta.json.JsonArray toJsonArray()

CLSS public final static !enum jakarta.json.JsonPatch$Operation
 outer jakarta.json.JsonPatch
fld public final static jakarta.json.JsonPatch$Operation ADD
fld public final static jakarta.json.JsonPatch$Operation COPY
fld public final static jakarta.json.JsonPatch$Operation MOVE
fld public final static jakarta.json.JsonPatch$Operation REMOVE
fld public final static jakarta.json.JsonPatch$Operation REPLACE
fld public final static jakarta.json.JsonPatch$Operation TEST
meth public java.lang.String operationName()
meth public static jakarta.json.JsonPatch$Operation fromOperationName(java.lang.String)
meth public static jakarta.json.JsonPatch$Operation valueOf(java.lang.String)
meth public static jakarta.json.JsonPatch$Operation[] values()
supr java.lang.Enum<jakarta.json.JsonPatch$Operation>
hfds operationName

CLSS public abstract interface jakarta.json.JsonPatchBuilder
meth public abstract jakarta.json.JsonPatch build()
meth public abstract jakarta.json.JsonPatchBuilder add(java.lang.String,boolean)
meth public abstract jakarta.json.JsonPatchBuilder add(java.lang.String,int)
meth public abstract jakarta.json.JsonPatchBuilder add(java.lang.String,jakarta.json.JsonValue)
meth public abstract jakarta.json.JsonPatchBuilder add(java.lang.String,java.lang.String)
meth public abstract jakarta.json.JsonPatchBuilder copy(java.lang.String,java.lang.String)
meth public abstract jakarta.json.JsonPatchBuilder move(java.lang.String,java.lang.String)
meth public abstract jakarta.json.JsonPatchBuilder remove(java.lang.String)
meth public abstract jakarta.json.JsonPatchBuilder replace(java.lang.String,boolean)
meth public abstract jakarta.json.JsonPatchBuilder replace(java.lang.String,int)
meth public abstract jakarta.json.JsonPatchBuilder replace(java.lang.String,jakarta.json.JsonValue)
meth public abstract jakarta.json.JsonPatchBuilder replace(java.lang.String,java.lang.String)
meth public abstract jakarta.json.JsonPatchBuilder test(java.lang.String,boolean)
meth public abstract jakarta.json.JsonPatchBuilder test(java.lang.String,int)
meth public abstract jakarta.json.JsonPatchBuilder test(java.lang.String,jakarta.json.JsonValue)
meth public abstract jakarta.json.JsonPatchBuilder test(java.lang.String,java.lang.String)

CLSS public abstract interface jakarta.json.JsonPointer
meth public abstract <%0 extends jakarta.json.JsonStructure> {%%0} add({%%0},jakarta.json.JsonValue)
meth public abstract <%0 extends jakarta.json.JsonStructure> {%%0} remove({%%0})
meth public abstract <%0 extends jakarta.json.JsonStructure> {%%0} replace({%%0},jakarta.json.JsonValue)
meth public abstract boolean containsValue(jakarta.json.JsonStructure)
meth public abstract jakarta.json.JsonValue getValue(jakarta.json.JsonStructure)
meth public abstract java.lang.String toString()

CLSS public abstract interface jakarta.json.JsonReader
intf java.io.Closeable
meth public abstract jakarta.json.JsonArray readArray()
meth public abstract jakarta.json.JsonObject readObject()
meth public abstract jakarta.json.JsonStructure read()
meth public abstract void close()
meth public jakarta.json.JsonValue readValue()

CLSS public abstract interface jakarta.json.JsonReaderFactory
meth public abstract jakarta.json.JsonReader createReader(java.io.InputStream)
meth public abstract jakarta.json.JsonReader createReader(java.io.InputStream,java.nio.charset.Charset)
meth public abstract jakarta.json.JsonReader createReader(java.io.Reader)
meth public abstract java.util.Map<java.lang.String,?> getConfigInUse()

CLSS public abstract interface jakarta.json.JsonString
intf jakarta.json.JsonValue
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()
meth public abstract java.lang.CharSequence getChars()
meth public abstract java.lang.String getString()

CLSS public abstract interface jakarta.json.JsonStructure
intf jakarta.json.JsonValue
meth public jakarta.json.JsonValue getValue(java.lang.String)

CLSS public abstract interface jakarta.json.JsonValue
fld public final static jakarta.json.JsonArray EMPTY_JSON_ARRAY
fld public final static jakarta.json.JsonObject EMPTY_JSON_OBJECT
fld public final static jakarta.json.JsonValue FALSE
fld public final static jakarta.json.JsonValue NULL
fld public final static jakarta.json.JsonValue TRUE
innr public final static !enum ValueType
meth public abstract jakarta.json.JsonValue$ValueType getValueType()
meth public abstract java.lang.String toString()
meth public jakarta.json.JsonArray asJsonArray()
meth public jakarta.json.JsonObject asJsonObject()

CLSS public final static !enum jakarta.json.JsonValue$ValueType
 outer jakarta.json.JsonValue
fld public final static jakarta.json.JsonValue$ValueType ARRAY
fld public final static jakarta.json.JsonValue$ValueType FALSE
fld public final static jakarta.json.JsonValue$ValueType NULL
fld public final static jakarta.json.JsonValue$ValueType NUMBER
fld public final static jakarta.json.JsonValue$ValueType OBJECT
fld public final static jakarta.json.JsonValue$ValueType STRING
fld public final static jakarta.json.JsonValue$ValueType TRUE
meth public static jakarta.json.JsonValue$ValueType valueOf(java.lang.String)
meth public static jakarta.json.JsonValue$ValueType[] values()
supr java.lang.Enum<jakarta.json.JsonValue$ValueType>

CLSS public abstract interface jakarta.json.JsonWriter
intf java.io.Closeable
meth public abstract void close()
meth public abstract void write(jakarta.json.JsonStructure)
meth public abstract void writeArray(jakarta.json.JsonArray)
meth public abstract void writeObject(jakarta.json.JsonObject)
meth public void write(jakarta.json.JsonValue)

CLSS public abstract interface jakarta.json.JsonWriterFactory
meth public abstract jakarta.json.JsonWriter createWriter(java.io.OutputStream)
meth public abstract jakarta.json.JsonWriter createWriter(java.io.OutputStream,java.nio.charset.Charset)
meth public abstract jakarta.json.JsonWriter createWriter(java.io.Writer)
meth public abstract java.util.Map<java.lang.String,?> getConfigInUse()

CLSS public abstract jakarta.json.spi.JsonProvider
cons protected init()
fld public final static java.lang.String JSONP_PROVIDER_FACTORY = "jakarta.json.provider"
meth public abstract jakarta.json.JsonArrayBuilder createArrayBuilder()
meth public abstract jakarta.json.JsonBuilderFactory createBuilderFactory(java.util.Map<java.lang.String,?>)
meth public abstract jakarta.json.JsonObjectBuilder createObjectBuilder()
meth public abstract jakarta.json.JsonReader createReader(java.io.InputStream)
meth public abstract jakarta.json.JsonReader createReader(java.io.Reader)
meth public abstract jakarta.json.JsonReaderFactory createReaderFactory(java.util.Map<java.lang.String,?>)
meth public abstract jakarta.json.JsonWriter createWriter(java.io.OutputStream)
meth public abstract jakarta.json.JsonWriter createWriter(java.io.Writer)
meth public abstract jakarta.json.JsonWriterFactory createWriterFactory(java.util.Map<java.lang.String,?>)
meth public abstract jakarta.json.stream.JsonGenerator createGenerator(java.io.OutputStream)
meth public abstract jakarta.json.stream.JsonGenerator createGenerator(java.io.Writer)
meth public abstract jakarta.json.stream.JsonGeneratorFactory createGeneratorFactory(java.util.Map<java.lang.String,?>)
meth public abstract jakarta.json.stream.JsonParser createParser(java.io.InputStream)
meth public abstract jakarta.json.stream.JsonParser createParser(java.io.Reader)
meth public abstract jakarta.json.stream.JsonParserFactory createParserFactory(java.util.Map<java.lang.String,?>)
meth public jakarta.json.JsonArrayBuilder createArrayBuilder(jakarta.json.JsonArray)
meth public jakarta.json.JsonArrayBuilder createArrayBuilder(java.util.Collection<?>)
meth public jakarta.json.JsonMergePatch createMergeDiff(jakarta.json.JsonValue,jakarta.json.JsonValue)
meth public jakarta.json.JsonMergePatch createMergePatch(jakarta.json.JsonValue)
meth public jakarta.json.JsonNumber createValue(double)
meth public jakarta.json.JsonNumber createValue(int)
meth public jakarta.json.JsonNumber createValue(java.lang.Number)
meth public jakarta.json.JsonNumber createValue(java.math.BigDecimal)
meth public jakarta.json.JsonNumber createValue(java.math.BigInteger)
meth public jakarta.json.JsonNumber createValue(long)
meth public jakarta.json.JsonObjectBuilder createObjectBuilder(jakarta.json.JsonObject)
meth public jakarta.json.JsonObjectBuilder createObjectBuilder(java.util.Map<java.lang.String,?>)
meth public jakarta.json.JsonPatch createDiff(jakarta.json.JsonStructure,jakarta.json.JsonStructure)
meth public jakarta.json.JsonPatch createPatch(jakarta.json.JsonArray)
meth public jakarta.json.JsonPatchBuilder createPatchBuilder()
meth public jakarta.json.JsonPatchBuilder createPatchBuilder(jakarta.json.JsonArray)
meth public jakarta.json.JsonPointer createPointer(java.lang.String)
meth public jakarta.json.JsonString createValue(java.lang.String)
meth public static jakarta.json.spi.JsonProvider provider()
supr java.lang.Object
hfds DEFAULT_PROVIDER,LOG,OSGI_SERVICE_LOADER_CLASS_NAME

CLSS public final jakarta.json.stream.JsonCollectors
meth public static <%0 extends jakarta.json.JsonArrayBuilder> java.util.stream.Collector<jakarta.json.JsonValue,java.util.Map<java.lang.String,{%%0}>,jakarta.json.JsonObject> groupingBy(java.util.function.Function<jakarta.json.JsonValue,java.lang.String>,java.util.stream.Collector<jakarta.json.JsonValue,{%%0},jakarta.json.JsonArray>)
meth public static java.util.stream.Collector<jakarta.json.JsonValue,jakarta.json.JsonArrayBuilder,jakarta.json.JsonArray> toJsonArray()
meth public static java.util.stream.Collector<jakarta.json.JsonValue,jakarta.json.JsonObjectBuilder,jakarta.json.JsonObject> toJsonObject(java.util.function.Function<jakarta.json.JsonValue,java.lang.String>,java.util.function.Function<jakarta.json.JsonValue,jakarta.json.JsonValue>)
meth public static java.util.stream.Collector<jakarta.json.JsonValue,java.util.Map<java.lang.String,jakarta.json.JsonArrayBuilder>,jakarta.json.JsonObject> groupingBy(java.util.function.Function<jakarta.json.JsonValue,java.lang.String>)
meth public static java.util.stream.Collector<java.util.Map$Entry<java.lang.String,jakarta.json.JsonValue>,jakarta.json.JsonObjectBuilder,jakarta.json.JsonObject> toJsonObject()
supr java.lang.Object

CLSS public jakarta.json.stream.JsonGenerationException
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
supr jakarta.json.JsonException
hfds serialVersionUID

CLSS public abstract interface jakarta.json.stream.JsonGenerator
fld public final static java.lang.String PRETTY_PRINTING = "jakarta.json.stream.JsonGenerator.prettyPrinting"
intf java.io.Closeable
intf java.io.Flushable
meth public abstract jakarta.json.stream.JsonGenerator write(boolean)
meth public abstract jakarta.json.stream.JsonGenerator write(double)
meth public abstract jakarta.json.stream.JsonGenerator write(int)
meth public abstract jakarta.json.stream.JsonGenerator write(jakarta.json.JsonValue)
meth public abstract jakarta.json.stream.JsonGenerator write(java.lang.String)
meth public abstract jakarta.json.stream.JsonGenerator write(java.lang.String,boolean)
meth public abstract jakarta.json.stream.JsonGenerator write(java.lang.String,double)
meth public abstract jakarta.json.stream.JsonGenerator write(java.lang.String,int)
meth public abstract jakarta.json.stream.JsonGenerator write(java.lang.String,jakarta.json.JsonValue)
meth public abstract jakarta.json.stream.JsonGenerator write(java.lang.String,java.lang.String)
meth public abstract jakarta.json.stream.JsonGenerator write(java.lang.String,java.math.BigDecimal)
meth public abstract jakarta.json.stream.JsonGenerator write(java.lang.String,java.math.BigInteger)
meth public abstract jakarta.json.stream.JsonGenerator write(java.lang.String,long)
meth public abstract jakarta.json.stream.JsonGenerator write(java.math.BigDecimal)
meth public abstract jakarta.json.stream.JsonGenerator write(java.math.BigInteger)
meth public abstract jakarta.json.stream.JsonGenerator write(long)
meth public abstract jakarta.json.stream.JsonGenerator writeEnd()
meth public abstract jakarta.json.stream.JsonGenerator writeKey(java.lang.String)
meth public abstract jakarta.json.stream.JsonGenerator writeNull()
meth public abstract jakarta.json.stream.JsonGenerator writeNull(java.lang.String)
meth public abstract jakarta.json.stream.JsonGenerator writeStartArray()
meth public abstract jakarta.json.stream.JsonGenerator writeStartArray(java.lang.String)
meth public abstract jakarta.json.stream.JsonGenerator writeStartObject()
meth public abstract jakarta.json.stream.JsonGenerator writeStartObject(java.lang.String)
meth public abstract void close()
meth public abstract void flush()

CLSS public abstract interface jakarta.json.stream.JsonGeneratorFactory
meth public abstract jakarta.json.stream.JsonGenerator createGenerator(java.io.OutputStream)
meth public abstract jakarta.json.stream.JsonGenerator createGenerator(java.io.OutputStream,java.nio.charset.Charset)
meth public abstract jakarta.json.stream.JsonGenerator createGenerator(java.io.Writer)
meth public abstract java.util.Map<java.lang.String,?> getConfigInUse()

CLSS public abstract interface jakarta.json.stream.JsonLocation
meth public abstract long getColumnNumber()
meth public abstract long getLineNumber()
meth public abstract long getStreamOffset()

CLSS public abstract interface jakarta.json.stream.JsonParser
innr public final static !enum Event
intf java.io.Closeable
meth public abstract boolean hasNext()
meth public abstract boolean isIntegralNumber()
meth public abstract int getInt()
meth public abstract jakarta.json.stream.JsonLocation getLocation()
meth public abstract jakarta.json.stream.JsonParser$Event next()
meth public abstract java.lang.String getString()
meth public abstract java.math.BigDecimal getBigDecimal()
meth public abstract long getLong()
meth public abstract void close()
meth public jakarta.json.JsonArray getArray()
meth public jakarta.json.JsonObject getObject()
meth public jakarta.json.JsonValue getValue()
meth public jakarta.json.stream.JsonParser$Event currentEvent()
meth public java.util.stream.Stream<jakarta.json.JsonValue> getArrayStream()
meth public java.util.stream.Stream<jakarta.json.JsonValue> getValueStream()
meth public java.util.stream.Stream<java.util.Map$Entry<java.lang.String,jakarta.json.JsonValue>> getObjectStream()
meth public void skipArray()
meth public void skipObject()

CLSS public final static !enum jakarta.json.stream.JsonParser$Event
 outer jakarta.json.stream.JsonParser
fld public final static jakarta.json.stream.JsonParser$Event END_ARRAY
fld public final static jakarta.json.stream.JsonParser$Event END_OBJECT
fld public final static jakarta.json.stream.JsonParser$Event KEY_NAME
fld public final static jakarta.json.stream.JsonParser$Event START_ARRAY
fld public final static jakarta.json.stream.JsonParser$Event START_OBJECT
fld public final static jakarta.json.stream.JsonParser$Event VALUE_FALSE
fld public final static jakarta.json.stream.JsonParser$Event VALUE_NULL
fld public final static jakarta.json.stream.JsonParser$Event VALUE_NUMBER
fld public final static jakarta.json.stream.JsonParser$Event VALUE_STRING
fld public final static jakarta.json.stream.JsonParser$Event VALUE_TRUE
meth public static jakarta.json.stream.JsonParser$Event valueOf(java.lang.String)
meth public static jakarta.json.stream.JsonParser$Event[] values()
supr java.lang.Enum<jakarta.json.stream.JsonParser$Event>

CLSS public abstract interface jakarta.json.stream.JsonParserFactory
meth public abstract jakarta.json.stream.JsonParser createParser(jakarta.json.JsonArray)
meth public abstract jakarta.json.stream.JsonParser createParser(jakarta.json.JsonObject)
meth public abstract jakarta.json.stream.JsonParser createParser(java.io.InputStream)
meth public abstract jakarta.json.stream.JsonParser createParser(java.io.InputStream,java.nio.charset.Charset)
meth public abstract jakarta.json.stream.JsonParser createParser(java.io.Reader)
meth public abstract java.util.Map<java.lang.String,?> getConfigInUse()

CLSS public jakarta.json.stream.JsonParsingException
cons public init(java.lang.String,jakarta.json.stream.JsonLocation)
cons public init(java.lang.String,java.lang.Throwable,jakarta.json.stream.JsonLocation)
meth public jakarta.json.stream.JsonLocation getLocation()
supr jakarta.json.JsonException
hfds location,serialVersionUID

CLSS public abstract interface java.io.Closeable
intf java.lang.AutoCloseable
meth public abstract void close() throws java.io.IOException

CLSS public abstract interface java.io.Flushable
meth public abstract void flush() throws java.io.IOException

CLSS public abstract interface java.io.Serializable

CLSS public abstract interface java.lang.AutoCloseable
meth public abstract void close() throws java.lang.Exception

CLSS public abstract interface java.lang.Comparable<%0 extends java.lang.Object>
meth public abstract int compareTo({java.lang.Comparable%0})

CLSS public abstract java.lang.Enum<%0 extends java.lang.Enum<{java.lang.Enum%0}>>
cons protected init(java.lang.String,int)
innr public final static EnumDesc
intf java.io.Serializable
intf java.lang.Comparable<{java.lang.Enum%0}>
intf java.lang.constant.Constable
meth protected final java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected final void finalize()
meth public final boolean equals(java.lang.Object)
meth public final int compareTo({java.lang.Enum%0})
meth public final int hashCode()
meth public final int ordinal()
meth public final java.lang.Class<{java.lang.Enum%0}> getDeclaringClass()
meth public final java.lang.String name()
meth public final java.util.Optional<java.lang.Enum$EnumDesc<{java.lang.Enum%0}>> describeConstable()
meth public java.lang.String toString()
meth public static <%0 extends java.lang.Enum<{%%0}>> {%%0} valueOf(java.lang.Class<{%%0}>,java.lang.String)
supr java.lang.Object
hfds name,ordinal

CLSS public java.lang.Exception
cons protected init(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public init()
cons public init(java.lang.String)
cons public init(java.lang.String,java.lang.Throwable)
cons public init(java.lang.Throwable)
supr java.lang.Throwable
hfds serialVersionUID

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
hfds serialVersionUID

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
hfds CAUSE_CAPTION,EMPTY_THROWABLE_ARRAY,NULL_CAUSE_MESSAGE,SELF_SUPPRESSION_MESSAGE,SUPPRESSED_CAPTION,SUPPRESSED_SENTINEL,UNASSIGNED_STACK,backtrace,cause,depth,detailMessage,serialVersionUID,stackTrace,suppressedExceptions
hcls PrintStreamOrWriter,SentinelHolder,WrappedPrintStream,WrappedPrintWriter

CLSS public abstract interface java.lang.constant.Constable
meth public abstract java.util.Optional<? extends java.lang.constant.ConstantDesc> describeConstable()

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

