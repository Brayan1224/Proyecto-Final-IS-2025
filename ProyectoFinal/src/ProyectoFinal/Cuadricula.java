/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoFinal;

/**
 *
 * @author braya
 */
// Clase Cuadricula como objeto independiente
public class Cuadricula {
    private int id;
    private int tipo; // 0 = Camino, 1 = Obstáculo
    private int fila;
    private int columna;
    
    public Cuadricula(int id, int fila, int columna) {
        this.id = id;
        this.fila = fila;
        this.columna = columna;
        this.tipo = 0; // Camino por defecto
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public int getTipo() {
        return tipo;
    }
    
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    public int getFila() {
        return fila;
    }
    
    public int getColumna() {
        return columna;
    }
    
    public boolean esObstaculo() {
        return tipo == 1;
    }
    
    public boolean esCamino() {
        return tipo == 0;
    }
    
    public void alternarTipo() {
        tipo = (tipo == 0) ? 1 : 0;
    }
    
    @Override
    public String toString() {
        return "Cuadricula{id=" + id + 
               ", fila=" + fila + 
               ", columna=" + columna + 
               ", tipo=" + (tipo == 0 ? "Camino" : "Obstáculo") + "}";
    }
}