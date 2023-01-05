package cn.edu.bit.BITKill.config;

import cn.edu.bit.BITKill.handler.GameHandler;
import cn.edu.bit.BITKill.service.GameService;
import cn.edu.bit.BITKill.service.LoginService;
import cn.edu.bit.BITKill.service.RegisterService;
import cn.edu.bit.BITKill.service.RoomService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final RegisterService registerService;

    private final LoginService loginService;

    private final RoomService roomService;

    private final GameService gameService;

    public WebSocketConfig(RegisterService registerService, LoginService loginService, RoomService roomService, GameService gameService) {
        this.registerService = registerService;
        this.loginService = loginService;
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameHandler(), "/game")
                //.setAllowedOrigins("https://mydomain.com")
        ;
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer(){
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

    @Bean
    public WebSocketHandler gameHandler() {
        return new GameHandler(registerService,loginService,roomService,gameService);
    }


}
