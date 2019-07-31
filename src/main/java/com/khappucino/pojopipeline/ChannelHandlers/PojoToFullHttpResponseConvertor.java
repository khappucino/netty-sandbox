package com.khappucino.pojopipeline.ChannelHandlers;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

import com.khappucino.pojopipeline.DataModels.PojoResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.StandardCharsets;

public class PojoToFullHttpResponseConvertor extends ChannelOutboundHandlerAdapter {
  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
    if ( !(msg instanceof PojoResponse) ) {
      System.out.println("PojoToFullHttpResponseConvertor received an invalid object: " + msg);
      ctx.channel().close();
    }
    FullHttpResponse response = createResponse((PojoResponse) msg);
    // forward response to next outbound handler
    ctx.write(response);
  }

  private DefaultFullHttpResponse createResponse(PojoResponse pojoResponse) {
    ByteBuf responseByteBuf = extractByteBuf(pojoResponse);
    DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, responseByteBuf);
    response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
    response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
    return response;
  }

  private ByteBuf extractByteBuf(PojoResponse pojoResponse) {
    byte[] rawBytes = pojoResponse.getPayloadBytes();
    return Unpooled.wrappedBuffer(rawBytes);
  }

}
