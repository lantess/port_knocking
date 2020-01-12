package com.s19215;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileBufforedLoader {
    private File file;
    long off;


    public FileBufforedLoader(String filename){
        file = new File(filename);
        off=0;
    }

    public byte[] readWitHFirstByteEmpty(int n){
        try {
            InputStream in = new FileInputStream(file);
            in.skip(off);
            byte[] res = new byte[n+1];
            in.read(res,1,n);
            in.close();
            return res;
        }catch (IOException e){
            System.out.println("problem z odczytem pliku");
            return null;
        }

    }

    public void move(long n){
        off+=n;
    }

    public boolean hasAvailable(){
        System.out.println(off-file.length());
        return off<file.length();
    }
}
