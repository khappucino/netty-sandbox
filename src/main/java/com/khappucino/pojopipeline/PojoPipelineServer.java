package com.khappucino.pojopipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * PojoPipelineServer is used to build the application server and begin listening on port 8000
 */
public class PojoPipelineServer {

  // Application entry point
  public static void main(String args[]) throws InterruptedException {
    PojoPipelineServer server = new PojoPipelineServer();
    server.start();
  }

  // Starts the server and tries to bind to port 8000
  private void start() throws InterruptedException {
    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      final ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .localAddress(new InetSocketAddress("127.0.0.1", 8000))
        .childHandler(new PojoBasePipeline()) // <---- This is the layer cake of application handlers
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);
      System.out.println("Binding to port 8000");
      ChannelFuture channelFuture = serverBootstrap.bind().sync();
      channelFuture.channel().closeFuture().sync();
    }
    catch (Exception e) {
      System.out.println("Something went wrong: " + e.toString());
    }
    finally {
      bossGroup.shutdownGracefully().sync();
      workerGroup.shutdownGracefully().sync();
    }
  }
}
