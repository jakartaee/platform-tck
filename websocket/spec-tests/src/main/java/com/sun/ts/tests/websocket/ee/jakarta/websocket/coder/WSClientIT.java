package com.sun.ts.tests.websocket.ee.jakarta.websocket.coder;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.websocket.common.client.ByteBufferClientEndpoint;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryStreamDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryStreamEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextStreamDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextStreamEncoder;
import com.sun.ts.tests.websocket.common.util.IOUtil;

@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	protected static final String ECHO = "Echo message";

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_ee_coder_web.war");
		archive.addClasses(StringBean.class, StringBeanBinaryDecoder.class, StringBeanBinaryEncoder.class,
				StringBeanBinaryStreamEncoder.class, StringBeanBinaryStreamDecoder.class, StringBeanTextEncoder.class,
				StringBeanTextDecoder.class, StringBeanTextStreamEncoder.class, StringBeanTextStreamDecoder.class);
		archive.addClasses(IOUtil.class);
		archive.addPackages(false, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.coder");

		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_coder_web");
	}

	/* Run test */
	/*
	 * @testName: textEncoderInitDestroyTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
	 * WebSocket:JAVADOC:61; WebSocket:JAVADOC:190; WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test text encoder init and destroy were called The websocket
	 * implementation creates a new instance of the encoder per endpoint instance
	 * per connection.
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 *
	 * encode ServerEndpoint.encoders
	 */
	@Test
	public void textEncoderInitDestroyTest() throws Exception {
		invokeClear();
		invokeEcho("textencoder");
		invokeGetInit(InitDestroyTextEncoder.class);
		invokeGetDestroy(InitDestroyTextEncoder.class);
	}

	/*
	 * @testName: textStreamEncoderInitDestroyTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
	 * WebSocket:JAVADOC:63; WebSocket:JAVADOC:190; WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test text stream encoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 *
	 * encode ServerEndpoint.encoders
	 */
	@Test
	public void textStreamEncoderInitDestroyTest() throws Exception {
		invokeClear();
		invokeEcho("textstreamencoder");
		invokeGetInit(InitDestroyTextStreamEncoder.class);
		invokeGetDestroy(InitDestroyTextStreamEncoder.class);
	}

	/*
	 * @testName: binaryEncoderInitDestroyTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
	 * WebSocket:JAVADOC:56; WebSocket:JAVADOC:190; WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test binary encoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 *
	 * encode ServerEndpoint.encoders
	 */
	@Test
	public void binaryEncoderInitDestroyTest() throws Exception {
		invokeClear();
		invokeBinaryEncoderEcho("binaryencoder");
		invokeGetInit(InitDestroyBinaryEncoder.class);
		invokeGetDestroy(InitDestroyBinaryEncoder.class);
	}

	/*
	 * @testName: binaryStreamEncoderInitDestroyTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
	 * WebSocket:JAVADOC:58; WebSocket:JAVADOC:190; WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test binary stream encoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 *
	 * encode ServerEndpoint.encoders
	 */
	@Test
	public void binaryStreamEncoderInitDestroyTest() throws Exception {
		invokeClear();
		invokeBinaryEncoderEcho("binarystreamencoder");
		invokeGetInit(InitDestroyBinaryStreamEncoder.class);
		invokeGetDestroy(InitDestroyBinaryStreamEncoder.class);
	}

	// ----------------------------------------------------------------------

	/*
	 * @testName: textDecoderInitDestroyTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
	 * WebSocket:JAVADOC:43; WebSocket:JAVADOC:45; WebSocket:JAVADOC:189;
	 * WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test text decoder init and destroy were called The websocket
	 * implementation creates a new instance of the encoder per endpoint instance
	 * per connection.
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 *
	 * decode, willdecode ServerEndpoint.decoders
	 */
	@Test
	public void textDecoderInitDestroyTest() throws Exception {
		invokeClear();
		invokeEcho("textdecoder");
		invokeGetInit(InitDestroyTextDecoder.class);
		invokeGetDestroy(InitDestroyTextDecoder.class);
	}

	/*
	 * @testName: textStreamDecoderInitDestroyTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
	 * WebSocket:JAVADOC:46; WebSocket:JAVADOC:189; WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test text stream decoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 *
	 * decode ServerEndpoint.decoders
	 */
	@Test
	public void textStreamDecoderInitDestroyTest() throws Exception {
		invokeClear();
		invokeEcho("textstreamdecoder");
		invokeGetInit(InitDestroyTextStreamDecoder.class);
		invokeGetDestroy(InitDestroyTextStreamDecoder.class);
	}

	/*
	 * @testName: binaryDecoderInitDestroyTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
	 * WebSocket:JAVADOC:37; WebSocket:JAVADOC:39; WebSocket:JAVADOC:189;
	 * WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test binary decoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 *
	 * decode, willDecode ServerEndpoint.decoders
	 */
	@Test
	public void binaryDecoderInitDestroyTest() throws Exception {
		invokeClear();
		invokeBinaryDecoderEcho("binarydecoder");
		invokeGetInit(InitDestroyBinaryDecoder.class);
		invokeGetDestroy(InitDestroyBinaryDecoder.class);
	}

	/*
	 * @testName: binaryStreamDecoderInitDestroyTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
	 * WebSocket:JAVADOC:40; WebSocket:JAVADOC:189; WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test binary stream decoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 *
	 * decode ServerEndpoint.decoders
	 */
	@Test
	public void binaryStreamDecoderInitDestroyTest() throws Exception {
		invokeClear();
		invokeBinaryDecoderEcho("binarystreamdecoder");
		invokeGetInit(InitDestroyBinaryStreamDecoder.class);
		invokeGetDestroy(InitDestroyBinaryStreamDecoder.class);
	}

	// ----------------------------------------------------------------------

	/*
	 * @testName: textEncoderEncodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:61; WebSocket:JAVADOC:190;
	 * WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test text encoder encode was called
	 * 
	 * ServerEndpoint.encoders
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 */
	@Test
	public void textEncoderEncodeTest() throws Exception {
		invokeClear();
		invokeEcho("textencoder");
		invokeGetCode(InitDestroyTextEncoder.class);
	}

	/*
	 * @testName: textStreamEncoderEncodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:63; WebSocket:JAVADOC:190;
	 * WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test text stream encode was called
	 * 
	 * ServerEndpoint.encoders
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 */
	@Test
	public void textStreamEncoderEncodeTest() throws Exception {
		invokeClear();
		invokeEcho("textstreamencoder");
		invokeGetCode(InitDestroyTextStreamEncoder.class);
	}

	/*
	 * @testName: binaryEncoderEncodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:56; WebSocket:JAVADOC:190;
	 * WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test binary encoder encode was called
	 * 
	 * ServerEndpoint.encoders
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 */
	@Test
	public void binaryEncoderEncodeTest() throws Exception {
		invokeClear();
		invokeBinaryEncoderEcho("binaryencoder");
		invokeGetCode(InitDestroyBinaryEncoder.class);
	}

	/*
	 * @testName: binaryStreamEncoderEncodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:58; WebSocket:JAVADOC:190;
	 * WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test binary stream encoder encode was called
	 * 
	 * ServerEndpoint.encoders
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 */
	@Test
	public void binaryStreamEncoderEncodeTest() throws Exception {
		invokeClear();
		invokeBinaryEncoderEcho("binarystreamencoder");
		invokeGetCode(InitDestroyBinaryStreamEncoder.class);
	}

	// -----------------------------------------------------------------------

	/*
	 * @testName: textDecoderDecodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:43; WebSocket:JAVADOC:45;
	 * WebSocket:JAVADOC:189; WebSocket:SPEC:WSC-4.1.3-1;
	 * WebSocket:SPEC:WSC-4.1.3-2;
	 * 
	 * @test_Strategy: test text decoder decode and willDecode were called
	 * 
	 * ServerEndpoint.decoders
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 */
	@Test
	public void textDecoderDecodeTest() throws Exception {
		invokeClear();
		invokeEcho("textdecoder");
		invokeGetCode(InitDestroyTextDecoder.class);
		invokeGetWillCode(InitDestroyTextDecoder.class);
	}

	/*
	 * @testName: textStreamDecoderDecodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:46; WebSocket:JAVADOC:189;
	 * WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test text stream decoder decode was called
	 * ServerEndpoint.decoders
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 */
	@Test
	public void textStreamDecoderDecodeTest() throws Exception {
		invokeClear();
		invokeEcho("textstreamdecoder");
		invokeGetCode(InitDestroyTextStreamDecoder.class);
	}

	/*
	 * @testName: binaryDecoderDecodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:37; WebSocket:JAVADOC:39;
	 * WebSocket:JAVADOC:189; WebSocket:SPEC:WSC-4.1.3-1;
	 * WebSocket:SPEC:WSC-4.1.3-2;
	 * 
	 * @test_Strategy: test binary decoder decode and willDecode were called
	 * 
	 * ServerEndpoint.decoders
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 */
	@Test
	public void binaryDecoderDecodeTest() throws Exception {
		invokeClear();
		invokeBinaryDecoderEcho("binarydecoder");
		invokeGetCode(InitDestroyBinaryDecoder.class);
		invokeGetWillCode(InitDestroyBinaryDecoder.class);
	}

	/*
	 * @testName: binaryStreamDecoderDecodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:40; WebSocket:JAVADOC:189;
	 * WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test binary stream decoder decode was called
	 * 
	 * ServerEndpoint.decoders
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 */
	@Test
	public void binaryStreamDecoderDecodeTest() throws Exception {
		invokeClear();
		invokeBinaryDecoderEcho("binarystreamdecoder");
		invokeGetCode(InitDestroyBinaryStreamDecoder.class);
	}

	/*
	 * @testName: binaryDecoderWillDecodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:37; WebSocket:JAVADOC:39;
	 * WebSocket:JAVADOC:189; WebSocket:SPEC:WSC-4.1.3-1;
	 * WebSocket:SPEC:WSC-4.1.3-2;
	 * 
	 * @test_Strategy: test binary decoder with willDecode returning false will not
	 * be used
	 * 
	 * ServerEndpoint.decoders
	 */
	@Test
	public void binaryDecoderWillDecodeTest() throws Exception {
		invokeClear();
		invokeBinaryDecoderEcho("binarywilldecode");
		invokeGetWillCode(WillDecodeFirstBinaryDecoder.class, WillDecodeSecondBinaryDecoder.class);
		setProperty(Property.UNEXPECTED_RESPONSE_MATCH, WillDecodeFirstBinaryDecoder.class.getName());
		invokeGetCode(WillDecodeSecondBinaryDecoder.class);
	}

	/*
	 * @testName: textDecoderWillDecodeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:43; WebSocket:JAVADOC:45;
	 * WebSocket:JAVADOC:189; WebSocket:SPEC:WSC-4.1.3-1;
	 * WebSocket:SPEC:WSC-4.1.3-2;
	 * 
	 * @test_Strategy: test text decoder with willDecode returning false will not be
	 * used
	 * 
	 * ServerEndpoint.decoders
	 */
	@Test
	public void textDecoderWillDecodeTest() throws Exception {
		invokeClear();
		invokeEcho("textwilldecode");
		invokeGetWillCode(WillDecodeFirstTextDecoder.class, WillDecodeSecondTextDecoder.class);
		setProperty(Property.UNEXPECTED_RESPONSE_MATCH, WillDecodeFirstTextDecoder.class.getName());
		invokeGetCode(WillDecodeSecondTextDecoder.class);
	}

	// ====================================================================
	/*
	 * @testName: textEncoderInitDestroyOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
	 * WebSocket:JAVADOC:61; WebSocket:JAVADOC:3; WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test text encoder init and destroy were called The websocket
	 * implementation creates a new instance of the encoder per endpoint instance
	 * per connection.
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 *
	 * encode ClientEndpoint.encoders
	 */
	@Test
	public void textEncoderInitDestroyOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithTextEncoder());
		invoke("simpleecho", new StringBean(ECHO), ECHO);
		assertInit(InitDestroyTextEncoder.class);
		assertDestroy(InitDestroyTextEncoder.class);
	}

	/*
	 * @testName: textStreamEncoderInitDestroyOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
	 * WebSocket:JAVADOC:63; WebSocket:JAVADOC:3; WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test text stream encoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 *
	 * encode ClientEndpoint.encoders
	 */
	@Test
	public void textStreamEncoderInitDestroyOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithTextStreamEncoder());
		invoke("simpleecho", new StringBean(ECHO), ECHO);
		assertInit(InitDestroyTextStreamEncoder.class);
		assertDestroy(InitDestroyTextStreamEncoder.class);
	}

	/*
	 * @testName: binaryEncoderInitDestroyOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
	 * WebSocket:JAVADOC:56; WebSocket:JAVADOC:3; WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test binary encoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 *
	 * encode ClientEndpoint.encoders
	 */
	@Test
	public void binaryEncoderInitDestroyOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithBinaryEncoder());
		invoke("simpleecho", new StringBean(ECHO), ECHO);
		assertInit(InitDestroyBinaryEncoder.class);
		assertDestroy(InitDestroyBinaryEncoder.class);
	}

	/*
	 * @testName: binaryStreamEncoderInitDestroyOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
	 * WebSocket:JAVADOC:58; WebSocket:JAVADOC:3; WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test binary stream encoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 *
	 * encode ClientEndpoint.encoders
	 */
	@Test
	public void binaryStreamEncoderInitDestroyOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithBinaryStreamEncoder());
		invoke("simpleecho", new StringBean(ECHO), ECHO);
		assertInit(InitDestroyBinaryStreamEncoder.class);
		assertDestroy(InitDestroyBinaryStreamEncoder.class);
	}

	// ---------------------------------------------------------------------

	/*
	 * @testName: textDecoderInitDestroyOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
	 * WebSocket:JAVADOC:43; WebSocket:JAVADOC:45; WebSocket:JAVADOC:2;
	 * WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test text decoder init and destroy were called The websocket
	 * implementation creates a new instance of the encoder per endpoint instance
	 * per connection.
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 *
	 * decode, willdecode ClientEndpoint.decoders
	 */
	@Test
	public void textDecoderInitDestroyOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithTextDecoder());
		invoke("simpleecho", ECHO, ECHO);
		assertInit(InitDestroyTextDecoder.class);
		assertDestroy(InitDestroyTextDecoder.class);
	}

	/*
	 * @testName: textStreamDecoderInitDestroyOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
	 * WebSocket:JAVADOC:46; WebSocket:JAVADOC:2; WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test text stream decoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 *
	 * decode ClientEndpoint.decoders
	 */
	@Test
	public void textStreamDecoderInitDestroyOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithTextStreamDecoder());
		invoke("simpleecho", ECHO, ECHO);
		assertInit(InitDestroyTextStreamDecoder.class);
		assertDestroy(InitDestroyTextStreamDecoder.class);
	}

	/*
	 * @testName: binaryDecoderInitDestroyOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
	 * WebSocket:JAVADOC:37; WebSocket:JAVADOC:39; WebSocket:JAVADOC:2;
	 * WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test binary decoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 *
	 * decode, willDecode ClientEndpoint.decoders
	 */
	@Test
	public void binaryDecoderInitDestroyOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithBinaryDecoder());
		invoke("simplebin", ECHO, ECHO);
		assertInit(InitDestroyBinaryDecoder.class);
		assertDestroy(InitDestroyBinaryDecoder.class);
	}

	/*
	 * @testName: binaryStreamDecoderInitDestroyOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
	 * WebSocket:JAVADOC:40; WebSocket:JAVADOC:2; WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test binary stream decoder init and destroy were called The
	 * websocket implementation creates a new instance of the encoder per endpoint
	 * instance per connection.
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 *
	 * decode ClientEndpoint.decoders
	 */
	@Test
	public void binaryStreamDecoderInitDestroyOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithBinaryStreamDecoder());
		invoke("simplebin", ECHO, ECHO);
		assertInit(InitDestroyBinaryStreamDecoder.class);
		assertDestroy(InitDestroyBinaryStreamDecoder.class);
	}

	// ----------------------------------------------------------------------

	/*
	 * @testName: textEncoderEncodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:61; WebSocket:JAVADOC:3;
	 * WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test text encoder encode was called ClientEndpoint.encoders
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 */
	@Test
	public void textEncoderEncodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithTextEncoder());
		invoke("simpleecho", new StringBean(ECHO), ECHO);
		assertCode(InitDestroyTextEncoder.class);
	}

	/*
	 * @testName: textStreamEncoderEncodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:63; WebSocket:JAVADOC:3;
	 * WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test text stream encode was called ClientEndpoint.encoders
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 */
	@Test
	public void textStreamEncoderEncodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithTextStreamEncoder());
		invoke("simpleecho", new StringBean(ECHO), ECHO);
		assertCode(InitDestroyTextStreamEncoder.class);
	}

	/*
	 * @testName: binaryEncoderEncodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:56; WebSocket:JAVADOC:3;
	 * WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test binary encoder encode was called ClientEndpoint.encoders
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 */
	@Test
	public void binaryEncoderEncodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithBinaryEncoder());
		invoke("simpleecho", new StringBean(ECHO), ECHO);
		assertCode(InitDestroyBinaryEncoder.class);
	}

	/*
	 * @testName: binaryStreamEncoderEncodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:58; WebSocket:JAVADOC:3;
	 * WebSocket:SPEC:WSC-4.1.2-1;
	 * 
	 * @test_Strategy: test binary stream encoder encode was called
	 * ClientEndpoint.encoders
	 *
	 * The implementation must attempt to encode application objects of matching
	 * parametrized type as the encoder when they are attempted to be sent
	 */
	@Test
	public void binaryStreamEncoderEncodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithBinaryStreamEncoder());
		invoke("simpleecho", new StringBean(ECHO), ECHO);
		assertCode(InitDestroyBinaryStreamEncoder.class);
	}

	// ----------------------------------------------------------------------

	/*
	 * @testName: textDecoderDecodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:43; WebSocket:JAVADOC:45;
	 * WebSocket:JAVADOC:2; WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
	 * 
	 * @test_Strategy: test text decoder decode and willDecode were called
	 * ClientEndpoint.decoders
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 */
	@Test
	public void textDecoderDecodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithTextDecoder());
		invoke("simpleecho", ECHO, ECHO);
		assertCode(InitDestroyTextDecoder.class);
		assertWillCode(InitDestroyTextDecoder.class);
	}

	/*
	 * @testName: textStreamDecoderDecodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:46; WebSocket:JAVADOC:2;
	 * WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test text stream decoder decode was called
	 * ClientEndpoint.decoders
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 */
	@Test
	public void textStreamDecoderDecodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithTextStreamDecoder());
		invoke("simpleecho", ECHO, ECHO);
		assertCode(InitDestroyTextStreamDecoder.class);
	}

	/*
	 * @testName: binaryDecoderDecodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:37; WebSocket:JAVADOC:39;
	 * WebSocket:JAVADOC:2; WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
	 * 
	 * @test_Strategy: test binary decoder decode and willDecode were called
	 * ClientEndpoint.decoders
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 */
	@Test
	public void binaryDecoderDecodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithBinaryDecoder());
		invoke("simplebin", ECHO, ECHO);
		assertCode(InitDestroyBinaryDecoder.class);
		assertWillCode(InitDestroyBinaryDecoder.class);
	}

	/*
	 * @testName: binaryStreamDecoderDecodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:40; WebSocket:JAVADOC:2;
	 * WebSocket:SPEC:WSC-4.1.3-1;
	 * 
	 * @test_Strategy: test binary stream decoder decode was called
	 * ClientEndpoint.decoders
	 *
	 * The implementation must attempt to decode websocket messages using the
	 * decoder in the list appropriate to the native websocket message type and pass
	 * the message in decoded object form to the websocket endpoint
	 */
	@Test
	public void binaryStreamDecoderDecodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithBinaryStreamDecoder());
		invoke("simplebin", ECHO, ECHO);
		assertCode(InitDestroyBinaryStreamDecoder.class);
	}

	/*
	 * @testName: binaryDecoderWillDecodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:37; WebSocket:JAVADOC:39;
	 * WebSocket:JAVADOC:2; WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
	 * 
	 * @test_Strategy: test binary decoder with willDecode returning false will not
	 * be used ClientEndpoint.decoders
	 */
	@Test
	public void binaryDecoderWillDecodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithBinaryDecoders());
		invoke("simplebin", ECHO, ECHO);
		assertWillCode(WillDecodeFirstBinaryDecoder.class);
		assertWillCode(WillDecodeSecondBinaryDecoder.class);
		assertNotCode(WillDecodeFirstBinaryDecoder.class);
		assertCode(WillDecodeSecondBinaryDecoder.class);
	}

	/*
	 * @testName: textDecoderWillDecodeOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:43; WebSocket:JAVADOC:45;
	 * WebSocket:JAVADOC:2; WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
	 * 
	 * @test_Strategy: test text decoder with willDecode returning false will not be
	 * used ClientEndpoint.decoders
	 */
	@Test
	public void textDecoderWillDecodeOnClientTest() throws Exception {
		clientClear();
		setAnnotatedClientEndpoint(new WSCEndpointWithTextDecoders());
		invoke("simpleecho", ECHO, ECHO);
		assertWillCode(WillDecodeFirstTextDecoder.class);
		assertWillCode(WillDecodeSecondTextDecoder.class);
		assertNotCode(WillDecodeFirstTextDecoder.class);
		assertCode(WillDecodeSecondTextDecoder.class);
	}

	// //////////////////////////////////////////////////////////////////
	private void invokeClear() throws Exception {
		invokeLogger("clearall", "clearall");
	}

	private void invokeEcho(String endpoint) throws Exception {
		invoke(endpoint, ECHO, ECHO);
	}

	private void invokeBinaryEncoderEcho(String endpoint) throws Exception {
		setClientEndpoint(ByteBufferClientEndpoint.class);
		invokeEcho(endpoint);
	}

	private void invokeBinaryDecoderEcho(String endpoint) throws Exception {
		setEntity(ByteBuffer.wrap(ECHO.getBytes()));
		setProperty(Property.REQUEST, buildRequest(endpoint));
		setProperty(Property.SEARCH_STRING, ECHO);
		invoke();
	}

	private void invokeGetInit(Class<?> searchClazz) throws Exception {
		invokeLogger("getinit", searchClazz.getName());
	}

	private void invokeGetDestroy(Class<?> searchClazz) throws Exception {
		// Sleep current tread to give server time to call destroy() should not
		// be needed, just for sake
		TestUtil.sleepMsec(500);
		invokeLogger("getdestroy", searchClazz.getName());
	}

	private void invokeGetCode(Class<?>... searchClasses) throws Exception {
		for (Class<?> searchClazz : searchClasses)
			invokeLogger("getcode", searchClazz.getName());
	}

	private void invokeGetWillCode(Class<?>... searchClasses) throws Exception {
		for (Class<?> searchClazz : searchClasses)
			invokeLogger("getwillcode", searchClazz.getName());
	}

	private void invokeLogger(String method, String search) throws Exception {
		invoke("logger", method, search);
	}

	// //////////////////////////// Client //////////////////////////////////
	protected void clientClear() {
		WSCLoggerServer.operation("clearall");
	}

	protected void assertInit(Class<?> searchClazz) throws Exception {
		String log = WSCLoggerServer.operation("getinit");
		assertContains(log, searchClazz.getName(), searchClazz.getName(),
				"has not been found in a list of classes calling the init() method, only [", log, "] has been found");
		logMsg("init() has been called on", searchClazz, "as expected");
	}

	protected void assertDestroy(Class<?> searchClazz) throws Exception {
		System.gc();
		// Sleep current tread to give server time to call destroy() should not
		// be needed, just for sake
		TestUtil.sleepMsec(500);
		String log = WSCLoggerServer.operation("getdestroy");
		assertContains(log, searchClazz.getName(), searchClazz.getName(),
				"has not been found in a list of classes calling the destroy() method, only [", log,
				"] has been found");
		logMsg("destroy() has been called on", searchClazz, "as expected");
	}

	protected void assertCode(Class<?> searchClass) throws Exception {
		String log = WSCLoggerServer.operation("getcode");
		assertContains(log, searchClass.getName(), searchClass.getName(),
				"has not been found in a list of classes calling the code() method, only [", log, "] has been found");
		logMsg("code() has been called on", searchClass, "as expected");
	}

	protected void assertWillCode(Class<?> searchClass) throws Exception {
		String log = WSCLoggerServer.operation("getwillcode");
		assertContains(log, searchClass.getName(), searchClass.getName(),
				"has not been found in a list of classes calling the willCode() method, only [", log,
				"] has been found");
		logMsg("willCode() has been called on", searchClass, "as expected");
	}

	protected void assertNotCode(Class<?> searchClass) throws Exception {
		String log = WSCLoggerServer.operation("getcode");
		assertNotContains(log, searchClass.getName(), searchClass.getName(),
				"has unexpectedly been found in a list of classes calling the code() method");
		logMsg("code() has not been called on", searchClass, "as expected");
	}

}
