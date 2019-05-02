package technology.sys.pilis.netty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import technology.sys.pilis.comon.netty.PiLiSConstants;

import java.util.ArrayList;
import java.util.List;

public class MessageClient {


	private final String host;
	private final int port;

	public MessageClient(String host, int port){
		this.host = host;
		this.port = port;
	}

	public void sendMessage(String message) throws InterruptedException {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new MessageClientInitializer());

			// Start the connection attempt.
			Channel ch = b.connect(host, port).sync().channel();
			ChannelFuture lastWriteFuture = ch.writeAndFlush(message + PiLiSConstants.NETTY_DELIMITER);
			lastWriteFuture.channel().read().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
}
