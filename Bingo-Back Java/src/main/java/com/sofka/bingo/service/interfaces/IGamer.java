package com.sofka.bingo.service.interfaces;

import com.sofka.bingo.domain.Gamer;
import com.sofka.bingo.domain.Status;
import java.util.List;
import java.util.Optional;

/**
 * La interfaz IGamer define los métodos para manejar los jugadores de bingo.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
public interface IGamer {

    /**
     * Retorna una lista de todos los gamers.
     * @return una lista de objetos Gamer
     */
    public List<Gamer> getGamerList();


    /**
     * Encuentra y retorna un Gamer por su ID.
     * @param id el ID del Gamer a buscar
     * @return un objeto Optional que contiene el Gamer, o un objeto vacío si no se encuentra
     */
    Optional<Gamer> findGamer(Long id);

    /**
     * Crea un nuevo Gamer con el estado especificado y lo agrega a la lista de gamers.
     * @param gamer el nuevo Gamer a crear
     * @param status el estado inicial del Gamer
     * @return el objeto Gamer recién creado
     */
    Gamer postGamer(Gamer gamer, Status status);

    /**
     * Actualiza un Gamer existente con un nuevo objeto Gamer.
     * @param id el ID del Gamer a actualizar
     * @param gamer el nuevo objeto Gamer
     * @return el objeto Gamer actualizado
     */
    Gamer putGamer(Long id, Gamer gamer);

    /**
     * Elimina un Gamer por su ID.
     * @param id el ID del Gamer a eliminar
     */
    void deleteGamer(Long id);
}
