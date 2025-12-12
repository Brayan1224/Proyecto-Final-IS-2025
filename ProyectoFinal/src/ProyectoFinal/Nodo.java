package ProyectoFinal;

// Clase Nodo para el algoritmo A*
public class Nodo implements Comparable<Nodo> {
    private Cuadricula cuadricula;
    private Nodo padre;
    private double g; // Costo desde el inicio
    private double h; // Heurística (estimación al destino)
    private double f; // f = g + h
    
    public Nodo(Cuadricula cuadricula, Nodo padre, double g, double h) {
        this.cuadricula = cuadricula;
        this.padre = padre;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }
    
    public Cuadricula getCuadricula() {
        return cuadricula;
    }
    
    public Nodo getPadre() {
        return padre;
    }
    
    public double getG() {
        return g;
    }
    
    public double getH() {
        return h;
    }
    
    public double getF() {
        return f;
    }
    
    public void setG(double g) {
        this.g = g;
        this.f = g + h;
    }
    
    public void setPadre(Nodo padre) {
        this.padre = padre;
    }
    
    @Override
    public int compareTo(Nodo otro) {
        return Double.compare(this.f, otro.f);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Nodo nodo = (Nodo) obj;
        return cuadricula.getId() == nodo.cuadricula.getId();
    }
    
    @Override
    public int hashCode() {
        return cuadricula.getId();
    }
}