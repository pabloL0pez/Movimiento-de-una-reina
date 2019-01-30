package paquete;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tablero {

    private static final String ARCHIVO_ENTRADA = "completar.in";
    private static final String ARCHIVO_SALIDA = "completar.out";

    private static final int REINA = 9;
    private static final int NOPE = 1;

    private int dimension;
    private int cantReinas;
    private int[][] tablero;
    private boolean resuelto = true;
    
    private ArrayList<Coordenada> reinas = new ArrayList<Coordenada>();
    private ArrayList<Coordenada> reinasFaltantes = new ArrayList<Coordenada>();

    private void levantarArchivo() throws FileNotFoundException {
        Scanner scan = new Scanner(new File(ARCHIVO_ENTRADA));
        this.dimension = scan.nextInt();
        this.cantReinas = scan.nextInt();

        this.tablero = new int[this.dimension][this.dimension];

        for (int i = 0; i < this.cantReinas; i++) {
            int fila = scan.nextInt() - 1;
            int columna = scan.nextInt() - 1;
            this.tablero[fila][columna] = REINA;
            this.reinas.add(new Coordenada(fila, columna));
        }

        scan.close();
    }

    private void marcarDiagonales(int x, int y) {
        int i = x - 1;
        int j = y - 1;

        while(i > 0 && j > 0) { // Diagonal izquierda hacia arriba
            this.tablero[i][j] = NOPE;
            i--;
            j--;
        }

        i = x - 1;
        j = y + 1;
        while(i > 0 && j < this.dimension) { // Diagonal derecha hacia arriba
            this.tablero[i][j] = NOPE;
            i--;
            j++;
        }

        i = x + 1;
        j = y - 1;
        while(i < this.dimension && j > 0) { // Diagonal izquierda hacia abajo
            this.tablero[i][j] = NOPE;
            i++;
            j--;
        }

        i = x + 1;
        j = y + 1;
        while(i < this.dimension && j < this.dimension) {
            this.tablero[i][j] = NOPE;
            i++;
            j++;
        }
    }

    private void marcarFila(int fila) {
        for(int i = 0; i < this.dimension ; i++) {
            this.tablero[fila][i] = NOPE;
        }
    }

    private void marcarColumna(int columna) {
        for(int i = 0; i < this.dimension ; i++) {
            this.tablero[i][columna] = NOPE;
        }
    }

    private void marcarCeldas() {
        for(Coordenada c : this.reinas) {
            marcarFila(c.getX());
            marcarColumna(c.getY());
            marcarDiagonales(c.getX(), c.getY());
        }
    }

    private void ubicarReinasFaltantes() {
        int cantReinasFaltantes = this.dimension - this.cantReinas;
        int reinasUbicadas = 0;
        
        for(int i = 0 ; i < this.dimension && reinasUbicadas < cantReinasFaltantes ; i ++) {
            for(int j = 0 ; j < this.dimension && reinasUbicadas < cantReinasFaltantes ; j++) {
                if(this.tablero[i][j] == 0) {
                    ubicarReina(i, j);
                    reinasUbicadas++;
                }
            }
        }

        if(reinasUbicadas < cantReinasFaltantes) {
            resuelto = false;
        }
    }

    private void ubicarReina(int fila, int columna) {
        this.reinasFaltantes.add(new Coordenada(fila, columna));
        this.tablero[fila][columna] = REINA;
        marcarFila(fila);
        marcarColumna(columna);
        marcarDiagonales(fila, columna);
    }

    public void resolver() throws IOException {
        this.levantarArchivo();
        this.marcarCeldas();
        this.ubicarReinasFaltantes();
        this.escribirSolucion();
    }

    private void escribirSolucion() throws IOException {
        BufferedWriter buffer = new BufferedWriter(new FileWriter(ARCHIVO_SALIDA));

        if(this.resuelto) {
            for(Coordenada c : this.reinasFaltantes) {
                buffer.write((c.getX()+1) + " " + (c.getY()+1));
                buffer.newLine();
            }
        } else {
            buffer.write("NO HAY SOLUCION.");
        }
        
        buffer.close();
    }
}