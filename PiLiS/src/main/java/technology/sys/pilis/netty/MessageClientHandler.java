package technology.sys.pilis.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import technology.sys.pilis.SpringContext;
import technology.sys.pilis.comon.GpioResponse;
import technology.sys.pilis.comon.helper.XmlHelper;
import technology.sys.pilis.services.LightService;

@Sharable
public class MessageClientHandler extends SimpleChannelInboundHandler<String> {


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		GpioResponse response = XmlHelper.unmarshallResponse(msg);

		LightService lightControllerPlugin = SpringContext.getBean(LightService.class);
		lightControllerPlugin.applyStatesFromResponse(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
