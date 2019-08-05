package com.khappucino.pojopipeline.ChannelHandlers;

import java.util.concurrent.CompletableFuture;
import com.khappucino.pojopipeline.DataModels.PojoResponse;
import com.khappucino.pojopipeline.Clients.ServiceClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * SimpleApplicationHandler is used build the Pojo response from any requests
 * that pass the firewall checks.  This service has a single dependency (ServiceClient).
 * The service client is used to asynchronously fetch a random number.
 * When the random number is returned this class will compose it with various request header
 * data and create a Pojo response to send back to the original caller.
*/
public class SimpleApplicationHandler extends ChannelInboundHandlerAdapter {
  private ServiceClient serviceClient;

  public SimpleApplicationHandler(ServiceClient serviceClient) {
    this.serviceClient = serviceClient;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if ( !(msg instanceof FullHttpRequest)) {
      System.out.println("SimpleApplicationHandler received an invalid object: " + msg);
      ctx.channel().close();
    }

    // fetch the random number future from the ServiceClient
    CompletableFuture<Integer> randomNumberFuture = serviceClient.computeRandomNumber();

    // when the future completes build a pojo response and fire it back up the channel towards the original caller
    randomNumberFuture.thenAccept(randomInteger -> {
        // Send response back towards the remote caller after we get an async response from our ServiceClient
        FullHttpRequest request = (FullHttpRequest)msg;
        PojoResponse response = buildResponse(request, randomInteger);
        ctx.writeAndFlush(response);
      });
  }

  // Build a PojoResponse based on the initial request and a random number fetched from the ServiceClient
  private PojoResponse buildResponse(FullHttpRequest request, int randomNumber) {
    StringBuilder builder = new StringBuilder();
    String user = request.headers().get("user");
    if (user == null || user.isEmpty()) {
      user = "noone";
    }
    builder.append("Hello ");
    builder.append(user);
    builder.append("\n");
    builder.append("Here is a random number ");
    builder.append(randomNumber);
    builder.append("\n");
    return new PojoResponse(builder.toString());
  }
}
