package ru.levelup.battleship;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.JsonPathExpectationsHelper;
import ru.levelup.battleship.web_socket.MessagingController;
import ru.levelup.battleship.web_socket.messages.ReadyMessage;

/**
 * Tests for PortfolioController that instantiate directly the minimum
 * infrastructure necessary to test annotated controller methods and do not load
 * Spring configuration.
 *
 * Tests can create a Spring {@link org.springframework.messaging.Message} that
 * represents a STOMP frame and send it directly to the
 * SimpAnnotationMethodMessageHandler responsible for invoking annotated controller
 * methods.
 *
 * Test message channels can be used to detect any messages the controller may send.
 * It's also easy to inject the controller with a test-specific TradeService to
 * verify what trades are getting executed.
 *
 * The test strategy here is to test the behavior of controllers taking into
 * account controller annotations and nothing more. The tests are simpler to write
 * and faster to executed. They provide the most amount of control and that is good
 * for writing as many controller tests as needed. Separate tests are still required
 * to verify the Spring configuration but those tests should be fewer overall.
 *
 * @author Rossen Stoyanchev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TestMvcConfiguration.class)
public class StandalonePortfolioControllerTests {

    @Autowired
    private MessagingController controller;

    private TestMessageChannel clientOutboundChannel;

    private TestAnnotationMethodHandler annotationMethodHandler;


    @Before
    public void setup() {

        clientOutboundChannel = new TestMessageChannel();

        annotationMethodHandler = new TestAnnotationMethodHandler(
                new TestMessageChannel(), clientOutboundChannel, new SimpMessagingTemplate(new TestMessageChannel()));

        this.annotationMethodHandler.registerHandler(controller);
        this.annotationMethodHandler.setDestinationPrefixes(Arrays.asList("/app"));
        this.annotationMethodHandler.setMessageConverter(new MappingJackson2MessageConverter());
        this.annotationMethodHandler.setApplicationContext(new StaticApplicationContext());
        this.annotationMethodHandler.afterPropertiesSet();
    }


    @Test
    public void getPositions() throws Exception {
        ReadyMessage ready = new ReadyMessage("user001", true);
        byte[] payload = new ObjectMapper().writeValueAsBytes(ready);

        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
        headers.setSubscriptionId("0");
        headers.setDestination("/app/ready/27");
        headers.setSessionId("0");
        headers.setSessionAttributes(new HashMap<>());
        Message<byte[]> message = MessageBuilder.withPayload(payload).setHeaders(headers).build();

        annotationMethodHandler.handleMessage(message);

        assertEquals(1, this.clientOutboundChannel.getMessages().size());
        Message<?> reply = this.clientOutboundChannel.getMessages().get(0);

        StompHeaderAccessor replyHeaders = StompHeaderAccessor.wrap(reply);
        assertEquals("0", replyHeaders.getSessionId());
        assertEquals("0", replyHeaders.getSubscriptionId());
        assertEquals("/ready/27", replyHeaders.getDestination());

        String json = new String((byte[]) reply.getPayload(), Charset.forName("UTF-8"));
        new JsonPathExpectationsHelper("$[0].company").assertValue(json, "Citrix Systems, Inc.");
        new JsonPathExpectationsHelper("$[1].company").assertValue(json, "Dell Inc.");
        new JsonPathExpectationsHelper("$[2].company").assertValue(json, "Microsoft");
        new JsonPathExpectationsHelper("$[3].company").assertValue(json, "Oracle");
    }

    @Test
    public void executeTrade() throws Exception {

//        Trade trade = new Trade();
//        trade.setAction(Trade.TradeAction.Buy);
//        trade.setTicker("DELL");
//        trade.setShares(25);

        byte[] payload = new ObjectMapper().writeValueAsBytes("trade");

        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
        headers.setDestination("/app/trade");
        headers.setSessionId("0");
        headers.setSessionAttributes(new HashMap<>());
        Message<byte[]> message = MessageBuilder.withPayload(payload).setHeaders(headers).build();

        this.annotationMethodHandler.handleMessage(message);

//        assertEquals(1, this.tradeService.getTrades().size());
//        Trade actual = this.tradeService.getTrades().get(0);
//
//        assertEquals(Trade.TradeAction.Buy, actual.getAction());
//        assertEquals("DELL", actual.getTicker());
//        assertEquals(25, actual.getShares());
//        assertEquals("fabrice", actual.getUsername());
    }


    /**
     * An extension of SimpAnnotationMethodMessageHandler that exposes a (public)
     * method for manually registering a controller, rather than having it
     * auto-discovered in the Spring ApplicationContext.
     */
    private static class TestAnnotationMethodHandler extends SimpAnnotationMethodMessageHandler {

        public TestAnnotationMethodHandler(SubscribableChannel inChannel, MessageChannel outChannel,
                                           SimpMessageSendingOperations brokerTemplate) {

            super(inChannel, outChannel, brokerTemplate);
        }

        public void registerHandler(Object handler) {
            super.detectHandlerMethods(handler);
        }
    }

}

class TestMessageChannel extends AbstractSubscribableChannel {

    private final List<Message<?>> messages = new ArrayList<>();


    public List<Message<?>> getMessages() {
        return this.messages;
    }

    @Override
    protected boolean sendInternal(Message<?> message, long timeout) {
        this.messages.add(message);
        return true;
    }

}