package com.f139.shop.admin.common.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ServerConfig implements InitializingBean {

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    private String webUrl;

    public String getUrl(){
        return this.webUrl;
    };

    @Override
    public void afterPropertiesSet() throws Exception {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //this.webUrl =  "http://" + address.getHostAddress() + ":" +  + this.serverPort+"/"+this.applicationName ;
        this.webUrl =  "http://" + address.getHostAddress() + ":" +  + this.serverPort+"/" ;

    }
}
