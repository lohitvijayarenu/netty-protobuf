package org.demo.nettyprotobuf;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.demo.nettyprotobuf.proto.DemoProtocol.DemoRequest;
import org.demo.nettyprotobuf.proto.DemoProtocol.DemoResponse;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class DemoClientHandler extends SimpleChannelInboundHandler<DemoResponse> { 

  private Channel channel;
  private DemoResponse resp;
  BlockingQueue<DemoResponse> resps = new LinkedBlockingQueue<DemoResponse>();
  public DemoResponse sendRequest() {
    DemoRequest req = DemoRequest.newBuilder()
                            .setRequestMsg("From Client").build();
    
    // Send request
    channel.writeAndFlush(req);
    
    // Now wait for response from server
    boolean interrupted = false;
    for (;;) {
        try {
            resp = resps.take();
            break;
        } catch (InterruptedException ignore) {
            interrupted = true;
        }
    }

    if (interrupted) {
        Thread.currentThread().interrupt();
    }
    
    return resp;
  }

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) {
      channel = ctx.channel();
  }
  
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, DemoResponse msg)
      throws Exception {
    resps.add(msg);
  }
  
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      cause.printStackTrace();
      ctx.close();
  }
}
