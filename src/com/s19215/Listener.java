package com.s19215;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Listener {
    //private final int timeout = 2500; //useless piece of code
    private final int knocksize = 8;
    private boolean isAlive;
    private List<DatagramSocket> sockets;
    private List<Thread> listeners;
    private HashMap<InetAddress, PortSequence> access;
    private HashMap<InetAddress, Long> lastAccessTime;

    public Listener(String[] args) throws Exception{
        isAlive = true;
        sockets = new ArrayList<>();
        listeners = new ArrayList<>();
        access = new HashMap<>();
        lastAccessTime = new HashMap<>();
        initializeSockets(args);
        initializeListeners();
        startListeners();
    }
    private void initializeSockets(String[] args) throws SocketException {
        for(int p : parseStringToPortNumbers(args)){
            DatagramSocket sck = new DatagramSocket(p);
            //sck.setSoTimeout(timeout); //w sumie to po co mam cokolwiek timeoutowac, glupi ja
            sockets.add(sck);
        }
    }

    private List<Integer> parseStringToPortNumbers(String[] args) {
        return Arrays.asList(args)
                .stream()
                .map(n -> Integer.parseInt(n))
                .collect(Collectors.toList());
    }

    private void initializeListeners(){
        for(DatagramSocket s : sockets)
            addListener(s);
    }

    private void addListener(DatagramSocket s) {
        listeners.add(new Thread(() ->{
            byte[] data = new byte[knocksize];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            while(isAlive){
                try {
                    s.receive(packet);
                    int no = ByteEncoder.bytesToInt(packet.getData());
                    registerKnock(packet.getAddress(), no, s.getLocalPort());
                    if(checkAddress(packet.getAddress())){
                        int rPort = getRandomPort();
                        byte[] portData = ByteEncoder.intToBytes(rPort);
                        DatagramPacket rPck = new DatagramPacket(portData, portData.length,
                                packet.getAddress(), packet.getPort());
                        sockets.get(sockets.size()-1).send(rPck);
                        TCPButOnUDPSender sender;
                        try {
                            sender = new TCPButOnUDPSender(rPort, packet.getAddress());
                            sender.send();
                        } catch (Exception e) {
                            System.out.println("Nie udało się wysłać wiadomości do klienta. "+e.getMessage());
                            // nic nie będę robił
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }));
    }

    private void registerKnock(InetAddress addr, int no, int port){
        synchronized (access) {
            if (!access.containsKey(addr))
                access.put(addr, new PortSequence(sockets.size()));
            access.get(addr).addPort(no, port);
            lastAccessTime.put(addr, System.currentTimeMillis());
        }
    }

    private boolean checkAddress(InetAddress address) {
        synchronized (access) {
            return access.get(address)
                    .checkOrder(getPortsArray());
        }
    }

    private int[] getPortsArray(){
        synchronized (sockets){
            int[] res = new int[sockets.size()];
            for(int i = 0; i < sockets.size(); i++)
                res[i] = sockets.get(i).getLocalPort();
            return res;
        }
    }

    private int getRandomPort(){
        return 1025+(int)(Math.random()*3096);
    }

    private void startListeners(){
        for(Thread t: listeners)
            t.start();
    }
}
