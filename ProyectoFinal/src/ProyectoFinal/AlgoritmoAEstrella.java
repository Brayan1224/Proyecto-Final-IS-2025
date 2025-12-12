package ProyectoFinal;

import java.util.*;

public class AlgoritmoAEstrella {
    private int filas;
    private int columnas;
    private Cuadricula[][] mapa;
    
    public AlgoritmoAEstrella(int filas, int columnas, Cuadricula[][] mapa) {
        this.filas = filas;
        this.columnas = columnas;
        this.mapa = mapa;
    }
    
    // Encuentra el camino usando A* hacia un obstáculo (destino)
    // El camión llegará a la cuadrícula adyacente más cercana al obstáculo
    public List<Cuadricula> encontrarCamino(Cuadricula inicio, Cuadricula obstaculoDestino) {
        if (inicio == null || obstaculoDestino == null) {
            System.out.println("Error: Inicio o destino es null");
            return null;
        }
        
        if (inicio.esObstaculo()) {
            System.out.println("Error: El inicio no puede ser un obstáculo");
            return null;
        }
        
        if (!obstaculoDestino.esObstaculo()) {
            System.out.println("Error: El destino debe ser un obstáculo");
            return null;
        }
        
        // Encontrar cuadrículas adyacentes al obstáculo destino
        List<Cuadricula> adyacentes = obtenerAdyacentesLibres(obstaculoDestino);
        
        if (adyacentes.isEmpty()) {
            System.out.println("Error: El obstáculo destino está completamente bloqueado");
            return null;
        }
        
        // Buscar el camino más corto a cualquiera de las cuadrículas adyacentes
        List<Cuadricula> mejorCamino = null;
        double mejorDistancia = Double.MAX_VALUE;
        
        for (Cuadricula adyacente : adyacentes) {
            List<Cuadricula> camino = encontrarCaminoADestino(inicio, adyacente);
            if (camino != null && camino.size() < mejorDistancia) {
                mejorCamino = camino;
                mejorDistancia = camino.size();
            }
        }
        
        return mejorCamino;
    }
    
    // Obtener cuadrículas adyacentes libres (no obstáculos) al obstáculo destino
    private List<Cuadricula> obtenerAdyacentesLibres(Cuadricula obstaculo) {
        List<Cuadricula> adyacentes = new ArrayList<>();
        int fila = obstaculo.getFila();
        int col = obstaculo.getColumna();
        
        // Revisar las 4 direcciones
        if (fila > 0 && !mapa[fila - 1][col].esObstaculo()) {
            adyacentes.add(mapa[fila - 1][col]); // Arriba
        }
        if (fila < filas - 1 && !mapa[fila + 1][col].esObstaculo()) {
            adyacentes.add(mapa[fila + 1][col]); // Abajo
        }
        if (col > 0 && !mapa[fila][col - 1].esObstaculo()) {
            adyacentes.add(mapa[fila][col - 1]); // Izquierda
        }
        if (col < columnas - 1 && !mapa[fila][col + 1].esObstaculo()) {
            adyacentes.add(mapa[fila][col + 1]); // Derecha
        }
        
        return adyacentes;
    }
    
    // Método interno para encontrar camino a un destino específico (no obstáculo)
    private List<Cuadricula> encontrarCaminoADestino(Cuadricula inicio, Cuadricula destino) {
        if (inicio == null || destino == null || inicio.esObstaculo() || destino.esObstaculo()) {
            return null;
        }
        
        PriorityQueue<Nodo> listaAbierta = new PriorityQueue<>();
        Set<Integer> listaCerrada = new HashSet<>();
        Map<Integer, Nodo> nodos = new HashMap<>();
        
        // Crear nodo inicial
        Nodo nodoInicio = new Nodo(inicio, null, 0, calcularHeuristica(inicio, destino));
        listaAbierta.add(nodoInicio);
        nodos.put(inicio.getId(), nodoInicio);
        
        while (!listaAbierta.isEmpty()) {
            Nodo actual = listaAbierta.poll();
            
            // Si llegamos al destino, reconstruir el camino
            if (actual.getCuadricula().getId() == destino.getId()) {
                return reconstruirCamino(actual);
            }
            
            listaCerrada.add(actual.getCuadricula().getId());
            
            // Explorar vecinos
            for (Cuadricula vecino : obtenerVecinos(actual.getCuadricula())) {
                if (listaCerrada.contains(vecino.getId()) || vecino.esObstaculo()) {
                    continue;
                }
                
                double nuevoG = actual.getG() + 1; // Costo de movimiento = 1
                
                Nodo nodoVecino = nodos.get(vecino.getId());
                
                if (nodoVecino == null) {
                    // Nodo no visitado
                    nodoVecino = new Nodo(vecino, actual, nuevoG, calcularHeuristica(vecino, destino));
                    nodos.put(vecino.getId(), nodoVecino);
                    listaAbierta.add(nodoVecino);
                } else if (nuevoG < nodoVecino.getG()) {
                    // Encontramos un mejor camino
                    listaAbierta.remove(nodoVecino);
                    nodoVecino.setG(nuevoG);
                    nodoVecino.setPadre(actual);
                    listaAbierta.add(nodoVecino);
                }
            }
        }
        
        System.out.println("No se encontró camino");
        return null; // No hay camino
    }
    
    // Calcula la distancia Manhattan como heurística
    private double calcularHeuristica(Cuadricula actual, Cuadricula destino) {
        return Math.abs(actual.getFila() - destino.getFila()) + 
               Math.abs(actual.getColumna() - destino.getColumna());
    }
    
    // Obtiene los vecinos válidos (arriba, abajo, izquierda, derecha)
    private List<Cuadricula> obtenerVecinos(Cuadricula cuadricula) {
        List<Cuadricula> vecinos = new ArrayList<>();
        int fila = cuadricula.getFila();
        int col = cuadricula.getColumna();
        
        // Arriba
        if (fila > 0) {
            vecinos.add(mapa[fila - 1][col]);
        }
        // Abajo
        if (fila < filas - 1) {
            vecinos.add(mapa[fila + 1][col]);
        }
        // Izquierda
        if (col > 0) {
            vecinos.add(mapa[fila][col - 1]);
        }
        // Derecha
        if (col < columnas - 1) {
            vecinos.add(mapa[fila][col + 1]);
        }
        
        return vecinos;
    }
    
    // Reconstruye el camino desde el nodo destino hasta el inicio
    private List<Cuadricula> reconstruirCamino(Nodo nodoFinal) {
        List<Cuadricula> camino = new ArrayList<>();
        Nodo actual = nodoFinal;
        
        while (actual != null) {
            camino.add(0, actual.getCuadricula());
            actual = actual.getPadre();
        }
        
        return camino;
    }
}