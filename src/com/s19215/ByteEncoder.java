package com.s19215;

public class ByteEncoder {
    public static int byteToInt(byte[] bytes, int offset){
        int res = 0;
        for(int i = offset; i < offset+4; i++)
            res = res*256+bytes[i];
        return res;
    }
}
