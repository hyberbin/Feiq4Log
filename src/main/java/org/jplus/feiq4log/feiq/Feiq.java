package org.jplus.feiq4log.feiq;


import org.jplus.jfeiq.feiq.FeiqServer;

public class Feiq {

    FeiqServer feiqServer;

    public Feiq() {
        feiqServer = new FeiqServer();
        feiqServer.setServerName("飞秋日志代理系统");
        feiqServer.setReceiveHandler(new Robot(feiqServer));
        feiqServer.start();
    }

    public FeiqServer getFeiqServer() {
        return feiqServer;
    }
    
    
}
