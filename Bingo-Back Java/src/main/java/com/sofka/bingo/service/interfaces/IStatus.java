package com.sofka.bingo.service.interfaces;

import com.sofka.bingo.domain.Status;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones para interactuar con los estados de los jugadores en un juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
public interface IStatus {

    /**
     * Retorna una lista de todos los estados de los jugadores en la base de datos.
     * @return una lista de objetos Status que representan todos los estados de los jugadores en la base de datos
     */
    public List<Status> getStatusList();

    /**
     * Busca y retorna un estado de jugador por su ID.
     * @param id el ID del estado de jugador a buscar
     * @return el objeto Status correspondiente al ID especificado, o null si no se encuentra
     */
    Optional<Status> findStatus(Long id);

    /**
     * Agrega un nuevo estado de jugador a la base de datos.
     * @param status el objeto Status a agregar a la base de datos
     * @return el objeto Status que se ha agregado a la base de datos
     */
    Status postStatus(Status status);

    /**
     * Actualiza un estado de jugador existente en la base de datos.
     * @param id el ID del estado de jugador a actualizar
     * @param status el objeto Status actualizado
     * @return el objeto Status actualizado que se ha guardado en la base de datos
     */
    Status putStatus(Long id, Status status);


    /**
     * Elimina un estado de jugador existente de la base de datos.
     * @param status el objeto Status a eliminar de la base de datos
     */
    void deleteStatus(Status status);
}
