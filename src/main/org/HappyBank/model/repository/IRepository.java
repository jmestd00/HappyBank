package org.HappyBank.model.repository;

import java.util.ArrayList;

/**
 * Interfaz que define los métodos que debe implementar el acceso a la base de datos.
 *
 * @param <T> Tipo de objeto que se almacenará en el repositorio.
 */
public interface IRepository<T> {
    /**
     * Añade un objeto al repositorio.
     *
     * @param object Objeto a añadir.
     */
    void add(T object);
    
    /**
     * Elimina un objeto del repositorio.
     *
     * @param object Objeto a eliminar.
     */
    void remove(T object);
    
    /**
     * Actualiza un objeto del repositorio.
     *
     * @param object Objeto a actualizar.
     */
    void update(T object);
    
    /**
     * Obtiene un objeto del repositorio.
     *
     * @param ID Identificador del objeto a obtener.
     * @return Objeto obtenido.
     */
    T get(String ID);
    
    /**
     * Obtiene todos los objetos del repositorio.
     *
     * @return Lista con todos los objetos del repositorio.
     */
    ArrayList<T> getAll();
}
