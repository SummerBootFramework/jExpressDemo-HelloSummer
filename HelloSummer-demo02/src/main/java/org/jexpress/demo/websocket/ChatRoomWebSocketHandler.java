package org.jexpress.demo.websocket;

import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.nio.server.BootWebSocketHandler;
import org.summerboot.jexpress.security.auth.Caller;
import org.summerboot.jexpress.security.auth.User;

/**
 * client - /run/websocket_client.html
 * @author DuXiao
 */
@ChannelHandler.Sharable
@Singleton
@Service(binding = ChannelHandler.class, named = "/mywebsocket/demo")
public class ChatRoomWebSocketHandler extends BootWebSocketHandler {

    private final StringBuilder history = new StringBuilder();

    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private static String getId(Caller caller) {
        return OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DTF) + " [" + caller.getUid() + "]: ";
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof HandshakeComplete) {
            HandshakeComplete event = (HandshakeComplete) evt;
            String p = event.selectedSubprotocol();
            System.out.println("selectedSubprotocol=" + p);
        }
    }

    @Override
    protected Caller auth(String token) {
        return new User(null, token);
    }

    @Override
    protected String onCallerConnected(ChannelHandlerContext ctx, Caller caller) {
        sendToChannel(ctx, history.toString());
        String msg = getId(caller) + "joined from " + ctx.channel().remoteAddress();
        history.append(msg).append("\n");
        return msg;
    }

    @Override
    protected String onMessage(ChannelHandlerContext ctx, Caller caller, String txt) {
        log.debug(() -> caller + " sent: " + txt);
        String msg = getId(caller) + txt;
        history.append(msg).append("\n");
        sendToAllChannels(msg, true);
        return null;
    }

    @Override
    protected String onMessage(ChannelHandlerContext ctx, Caller caller, byte[] data) {
        File outputFile = new File("aaa").getAbsoluteFile();
        System.out.println(outputFile);
        try {
            Files.write(outputFile.toPath(), data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sendToAllChannels(data, false);
        return null;
    }

}
