package org.demo.nettyprotobuf;

import org.demo.nettyprotobuf.proto.DemoProtocol.DemoRequest;
import org.demo.nettyprotobuf.proto.DemoProtocol.DemoResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DemoProtocolServerHandler extends SimpleChannelInboundHandler<DemoRequest> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, DemoRequest msg)
      throws Exception {
    
    DemoResponse.Builder builder = DemoResponse.newBuilder();
    builder.setResponseMsg("Accepted from Server, returning response")
           .setRet(0);
    ctx.write(builder.build());
    
  }
  
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
      ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      cause.printStackTrace();
      ctx.close();
  }

}
