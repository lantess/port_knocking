package com.s19215;

import java.util.Arrays;

public class PortSequence {
    private int[] ports;

    public PortSequence(int no){
        ports = new int[no];
        for(int i = 0; i < no; i++)
            ports[i] = -1;
    }

    public boolean addPort(int no, int port){
        if(ports.length<no || ports[no] != -1)
            return false;
        ports[no] = port;
        return true;
    }

    public boolean checkOrder(int[] correct){
        if(hasEmptyFields())
            return false;
        boolean res = Arrays.equals(correct, ports);
        reset();
        return res;

    }

    private boolean hasEmptyFields(){
        for(int i : ports)
            if(i==-1)
                return true;
        return false;
    }

    public void reset(){
        for(int i = 0; i < ports.length; i++)
            ports[i] = -1;
    }
}
