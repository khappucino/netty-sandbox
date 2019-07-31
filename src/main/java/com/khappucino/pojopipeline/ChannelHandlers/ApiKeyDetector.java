package com.khappucino.pojopipeline.ChannelHandlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

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
