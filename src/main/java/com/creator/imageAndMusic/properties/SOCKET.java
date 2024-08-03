package com.creator.imageAndMusic.properties;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SOCKET {

    public static  String REQ_PATH;
    public static  String MUSIC_REQ_PATH;
    @Value("${socket.req.path}")
    private String reqPath;
    @Value("${socket.req.music.path}")
    private String reqMusicPath;

    @PostConstruct
    public void init() {
        SOCKET.REQ_PATH = reqPath;
        SOCKET.MUSIC_REQ_PATH = reqMusicPath;
    }

    public static Long max = 5L;


}
