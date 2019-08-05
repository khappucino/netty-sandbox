package com.khappucino.ChannelHandlers;

import com.khappucino.pojopipeline.ChannelHandlers.EnglishToSpanishPojoHandler;
import com.khappucino.pojopipeline.DataModels.PojoResponse;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * EnglishToSpanishPojoHandlerTest is an example of how you can unit test a single
 * Netty ChannelHandler in an isolated and controlled environment.
 */
public class EnglishToSpanishPojoHandlerTest {
  @Test
  public void testEncoded() {
    PojoResponse testInput = new PojoResponse("Hello Proxymon");
    PojoResponse expectedOutput = new PojoResponse("Hola Proxymon");

    // Set up a test bed channel that we can manipulate
    EmbeddedChannel channel = new EmbeddedChannel(new EnglishToSpanishPojoHandler());
    // Start at the bottom of the pipeline and send a PojoResponse UP into the channel handler
    assertTrue(channel.writeOutbound(testInput));
    // Finish the action
    assertTrue(channel.finish());

    // This channel is only doing outbound work. Thus we should check the channel's outbound data
    // Inbound requests come into the TOP of the pipeline and propagate DOWN towards the BOTTOM
    // Responses tend to start from the BOTTOM of the pipeline and propagate UP towards the TOP
    PojoResponse receivedOutput = channel.readOutbound();

    // Check to make sure that the output we got from the Embedded Channel matches our expectations
    assertEquals(expectedOutput.getPayload(), receivedOutput.getPayload());
  }
}
