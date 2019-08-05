package com.khappucino.pojopipeline;

import com.khappucino.pojopipeline.ChannelHandlers.*;
import com.khappucino.pojopipeline.Clients.ServiceClient;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * PojoBasePipeline is used to build and layer the various handlers that will be invoked
 * when a message is sent into the server.
 */
public class PojoBasePipeline extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    System.out.println("initChannel");
    // Top of the pipeline
    //--------------------

    // This handler converts ByteBuf from the wire to HttpObjects and vice versa
    ch.pipeline().addLast(new HttpServerCodec()); // <---- Bi-Directional

    // This handler blocks until a complete HttpRequest/HttpResponse is ready to send down to the next stage
    // This lets us delegate the handling of chunkced http requests/responses to the aggregator
    ch.pipeline().addLast(new HttpObjectAggregator(8192)); // <---- Inbound

    // This handler is used to detect if the incoming request has the right headers
    ch.pipeline().addLast(new ApiKeyDetector()); // <---- Inbound

    // This handler applies firewall decisions made by the ApiKeyDetector
    ch.pipeline().addLast(new Firewall()); // <---- Inbound

    // This handler takes PojoResponse objects and converts them into Http Objects
    ch.pipeline().addLast(new PojoToFullHttpResponseConvertor()); // <---- Outbound

    // This handler takes PojoResponse objects and converts them into new PojoResponse objects
    ch.pipeline().addLast(new EnglishToSpanishPojoHandler()); // <---- Outbound

    // This handler takes a HttpRequest object and sends a PojoResponse back towards the top of the pipeline
    ch.pipeline().addLast(new SimpleApplicationHandler(new ServiceClient())); // <<---- Inbound and Final Handler

    // Bottom of the pipeline
    //-----------------------
  }
}
