package com.vk.dwzkf.alertor.socket_server_core.listeners;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListenerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ExceptionListener extends ExceptionListenerAdapter {
    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        log.error("exceptionCaught!", e);
        return super.exceptionCaught(ctx, e);
    }

    public ExceptionListener() {
        super();
    }

    @Override
    public void onEventException(Exception e, List<Object> data, SocketIOClient client) {
        log.error("onEventException!", e);
        super.onEventException(e, data, client);
    }

    @Override
    public void onDisconnectException(Exception e, SocketIOClient client) {
        log.error("onDisconnectException!", e);
        super.onDisconnectException(e, client);
    }

    @Override
    public void onConnectException(Exception e, SocketIOClient client) {
        log.error("onConnectException!", e);
        super.onConnectException(e, client);
    }

    @Override
    public void onPingException(Exception e, SocketIOClient client) {
        log.error("onPingException!", e);
        super.onPingException(e, client);
    }
}
