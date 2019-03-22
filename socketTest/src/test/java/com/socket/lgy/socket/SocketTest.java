package com.socket.lgy.socket;

import com.socket.lgy.SocketApplicaton;
import com.socket.lgy.SocketTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SocketTestApplication.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SocketTest {

    @Autowired
    SocketUtil socketUtil;

    @Test
    public void socket(){
        try {
            System.out.println("inter socket");
            socketUtil.connectDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
