package com.khappucino.pojopipeline.ChannelHandlers;

import com.khappucino.pojopipeline.DataModels.PojoResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;


public class SimpleApplicationHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if ( !(msg instanceof FullHttpRequest)) {
      System.out.println("SimpleApplicationHandler received an invalid object: " + msg);
      ctx.channel().close();
    }

    FullHttpRequest request = (FullHttpRequest)msg;
    PojoResponse response = buildResponse(request);

    // Send response back towards the remote caller
    ctx.writeAndFlush(response);
  }

  private PojoResponse buildResponse(FullHttpRequest request) {
    StringBuilder builder = new StringBuilder();
    String message = "Hello " + request.headers().get("user") + "\n";
    return new PojoResponse(message);
  }
}
