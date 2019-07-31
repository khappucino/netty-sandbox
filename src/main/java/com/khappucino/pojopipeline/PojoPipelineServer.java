package com.khappucino.pojopipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class PojoPipelineServer {

  public static void main(String args[]) throws InterruptedException {
    PojoPipelineServer server = new PojoPipelineServer();
    server.start();
  }

  private void start() throws InterruptedException {
    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      final ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .localAddress(new InetSocketAddress("127.0.0.1", 8000))
        .childHandler(new PojoBasePipeline())
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);
      System.out.println("Trying to bind to port 8000");
      ChannelFuture channelFuture = serverBootstrap.bind().sync();
      channelFuture.channel().closeFuture().sync();
    }
    catch (Exception e) {
      System.out.println("Something went wrong: " + e.toString());
    }
    finally {
      System.out.println("kill bossy");
      bossGroup.shutdownGracefully().sync();
      workerGroup.shutdownGracefully().sync();
    }
  }
}
