package com.s19215;

import java.net.SocketException;

public class Main {

    public static void main(String[] args) {
        if(args.length<1){
            System.out.println("Nie podano portów.");
            return;
        }
        try {
            Listener listener = new Listener(args);
        } catch (NumberFormatException e){
            System.out.println("Podano nieprawidłowy numer portu: "+e.getMessage());
            return;
        } catch (SocketException e){
            System.out.println("Problemy z socketem: "+e.getMessage());
        } catch (Exception e) {
            System.out.println("Inny błąd: "+e.getMessage());
        }
    }
}
