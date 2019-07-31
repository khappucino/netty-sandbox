package com.khappucino.pojopipeline;

import com.khappucino.pojopipeline.ChannelHandlers.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class PojoBasePipeline extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    System.out.println("initChannel");
    ch.pipeline().addLast(new HttpServerCodec()); // <---- Bi-Directional
    ch.pipeline().addLast(new HttpObjectAggregator(8192)); // <---- Inbound
    ch.pipeline().addLast(new ApiKeyDetector()); // <---- Inbound
    ch.pipeline().addLast(new Firewall()); // <---- Inbound
    ch.pipeline().addLast(new PojoToFullHttpResponseConvertor()); // <---- Outbound
    ch.pipeline().addLast(new EnglishToSpanishPojoHandler()); // <---- Outbound
    ch.pipeline().addLast(new SimpleApplicationHandler()); // <<---- Inbound and Final Handler
  }

}
