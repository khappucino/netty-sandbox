package com.khappucino.pojopipeline.ChannelHandlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * ApiKeyDetector is used to check for api key headers in the request.
 * If there are no apikey headers in the request then it will set a
 * Channel Attribute. This class simply decides if the connection should be close or not.
 * It is not responsible for actually closing the channel.
 */
public class ApiKeyDetector extends ChannelInboundHandlerAdapter {
  public final String apiKeyKey = "apikey";
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if ( !(msg instanceof FullHttpRequest)) {
      System.out.println("ApiKeyDetector received an invalid object: " + msg);
      ctx.channel().close();
    }

    FullHttpRequest request = (FullHttpRequest)msg;
    processApiKey(ctx, request);

    // forward message to next inbound handler
    ctx.fireChannelRead(msg);
  }

  // Parse the inbound FullHttpRequest and set the Channel Attribute based on the existance of an apikey header
  private void processApiKey(ChannelHandlerContext ctx, FullHttpRequest request) {
    String apikey = request.headers().get(apiKeyKey);
    if (apikey == null || apikey.isEmpty()) {
      System.out.println("Did not detect api key in request header");
      ctx.channel().attr(Firewall.FIREWALL_ALLOW_KEY).set(false);
    }
    else {
      System.out.println("Did detect api key in request header");
      ctx.channel().attr(Firewall.FIREWALL_ALLOW_KEY).set(true);
    }
  }
}
