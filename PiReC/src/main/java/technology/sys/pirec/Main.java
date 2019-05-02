package technology.sys.pirec;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import technology.sys.pilis.comon.GpioRequest;
import technology.sys.pilis.comon.GpioResponse;
import technology.sys.pilis.comon.helper.SocketHelper;
import technology.sys.pilis.comon.helper.XmlHelper;
import technology.sys.pirec.netty.PiReCServerInitializer;
import technology.sys.pirec.dtos.CmdLineParametersDto;

/**
 * @author Jaromir Sys
 */
public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static GpioHelper GPIO_HELPER;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CmdLineParametersDto parameters = CmdLineOptionHelper.getCmdLineConfig(args);

        int port = parameters.getPort();
        LOG.info("PiReC is starting on port[{}]", port);

        GPIO_HELPER = new GpioHelper(parameters.getConfigFilePath());

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)//
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))//
                    .childHandler(new PiReCServerInitializer());

            b.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
