package cn.edu.bit.BITKill.config;

import cn.edu.bit.BITKill.handler.GameHandler;
import cn.edu.bit.BITKill.handler.LoginHandler;
import cn.edu.bit.BITKill.handler.RegisterHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameHandler(), "/game");
        registry.addHandler(registerHandler(),"/register");
        registry.addHandler(loginHandler(),"login");
    }

    @Bean
    public WebSocketHandler gameHandler() {
        return new GameHandler();
    }

    @Bean
    public WebSocketHandler loginHandler(){return new LoginHandler();}

    @Bean
    public WebSocketHandler registerHandler(){return new RegisterHandler();}


}
