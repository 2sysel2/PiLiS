package technology.sys.pirec.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import technology.sys.pilis.comon.GpioRequest;
import technology.sys.pilis.comon.GpioResponse;
import technology.sys.pilis.comon.helper.XmlHelper;
import technology.sys.pilis.comon.netty.PiLiSConstants;
import technology.sys.pirec.Main;

@Sharable
public class PiReCServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOG = LogManager.getLogger(PiReCServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String requestString) throws Exception {

        LOG.info("Starting request processing");

        LOG.info("Received request[{}]", requestString);
        GpioRequest request = XmlHelper.unmarshallRequest(requestString);
        GpioResponse response = Main.GPIO_HELPER.handleRequest(request);

        LOG.info("Request handled starting response");
        String responseString = XmlHelper.marshallResponse(response);
        LOG.info("Response string :[{}]", responseString);

        ChannelFuture future = ctx.writeAndFlush(responseString + PiLiSConstants.NETTY_DELIMITER);

        LOG.info("response written to socket");

        future.addListener(ChannelFutureListener.CLOSE);
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
