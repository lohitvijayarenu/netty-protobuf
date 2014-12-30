package org.demo.nettyprotobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class DemoServer {

  static final int PORT = Integer.parseInt(System.getProperty("port", "8463"));

  public static void main(String[] args) throws InterruptedException {
    
    // Create event loop groups. One for incoming connections handling and 
    // second for handling actual event by workers
    EventLoopGroup serverGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    
    try {
      ServerBootstrap bootStrap = new ServerBootstrap();
      bootStrap.group(serverGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new DemoServerChannelInitializer());
      
      // Bind to port 
      bootStrap.bind(PORT).sync().channel().closeFuture().sync();
    } finally {
      serverGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
