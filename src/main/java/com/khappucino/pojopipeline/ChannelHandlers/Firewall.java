package com.khappucino.pojopipeline.ChannelHandlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * Firewall is used to close connections when certain conditions are not met.
 * The Firewall class reads Channel Attributes and uses them decide whether or
 * not to close the channel. This class does decide if something should be allowed, it just
 * reacts to the decisions of other handlers (via Channel Attributes)
 */
public class Firewall extends ChannelInboundHandlerAdapter {
  static final AttributeKey<Boolean> FIREWALL_ALLOW_KEY =
    AttributeKey.newInstance("FirewallBlockKey");

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    Boolean shouldAllow = ctx.channel().attr(FIREWALL_ALLOW_KEY).get();
    if (shouldAllow == null || shouldAllow == false) {
      System.out.println("We are going to close the connection without telling them why because we like being mean");
      ctx.channel().close();
    }
    else {
      // forward message to next inbound handler
      ctx.fireChannelRead(msg);
    }
  }
}
