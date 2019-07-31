package com.khappucino.pojopipeline.ChannelHandlers;

import com.khappucino.pojopipeline.DataModels.PojoResponse;
import com.khappucino.pojopipeline.PojoPipelineServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class EnglishToSpanishPojoHandler extends ChannelOutboundHandlerAdapter {

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
    if ( !(msg instanceof PojoResponse) ) {
      System.out.println("EnglishToSpanishPojoHandler received an invalid object: " + msg);
      ctx.channel().close();
    }

    PojoResponse response = (PojoResponse)msg;
    PojoResponse translatedResponse = translateResponse(response);

    // forward response to next outbound handler
    ctx.write(translatedResponse);
  }

  private PojoResponse translateResponse(PojoResponse response) {
    String input = response.getPayload();
    String translatedInput = input.replace("Hello", "Hola");
    return new PojoResponse(translatedInput);
  }
}
