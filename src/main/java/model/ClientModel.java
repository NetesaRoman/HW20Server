package model;

import java.net.Socket;
import java.time.LocalDateTime;

/*
 *
 * @author Roman Netesa
 *
 */
public class ClientModel {
   private int count;
    private String name;
    private Socket socket;
    private LocalDateTime joinTime;

    public ClientModel(Socket socket, int count){
        this.socket = socket;
        name = "client - " + count;
        joinTime = LocalDateTime.now();

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }
}
