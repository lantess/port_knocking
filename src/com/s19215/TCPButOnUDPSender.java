package com.s19215;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TCPButOnUDPSender {
    private final int datasize = 65534;
    private DatagramSocket s;
    private FileBufforedLoader file;
    private int port;
    private InetAddress addr;

    public TCPButOnUDPSender(int port, InetAddress addr) throws Exception{
        this.port = port;
        s = new DatagramSocket();
        s.setSoTimeout(2000);
        file = new FileBufforedLoader("file");
    }

    public void send(){
        new Thread(()->{
            byte b = 0;
            while(file.hasAvailable()){
                byte[] data = file.readWitHFirstByteEmpty(65534);
                data[0] = b;
                DatagramPacket pck = new DatagramPacket(data, data.length, addr, port);
                try{
                    s.send(pck);
                    s.receive(pck);
                    if(pck.getData()[0]==b) {
                        b++;
                        file.move(datasize);

                    }
                } catch (Exception e){
                    //System.out.println(e.getMessage());
                }

            }
        }).start();
    }
}
