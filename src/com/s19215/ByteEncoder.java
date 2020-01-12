package com.s19215;

import java.math.BigInteger;

public class ByteEncoder {
    public static int bytesToInt(byte[] bytes){
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8 ) |
                ((bytes[3] & 0xFF) << 0 );
    }

    public static byte[] intToBytes(int i){
        return new byte[] {
                (byte)(i >> 24),
                (byte)(i >> 16),
                (byte)(i >> 8),
                (byte)i };
    }
}
