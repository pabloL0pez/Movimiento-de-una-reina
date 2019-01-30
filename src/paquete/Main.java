package paquete;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Tablero t = new Tablero();
        try {
            t.resolver();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}