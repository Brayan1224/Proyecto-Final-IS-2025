/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ProyectoFinal;

/**
 *
 * @author braya
 */
public interface CRUD {
    public void crear();
    public void leer();
    public void actualizar(String columna, String valor, int tipo_dato);
    public void eliminar();
}