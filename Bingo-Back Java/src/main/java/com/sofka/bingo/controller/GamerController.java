package com.sofka.bingo.controller;

import com.sofka.bingo.domain.Gamer;
import com.sofka.bingo.service.GamerService;
import com.sofka.bingo.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;


import java.util.List;

/**
 * Clase GamerController contiene los controladores que se encargarán de recibir, orquestar
 * el proceso necesario y responder las solicitudes de la API, del juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 *
 */
@Slf4j
@RestController
public class GamerController {

    /**
     * Servicio para el manejo del jugador (gamer)
     */
    @Autowired
    private GamerService gamerService;

    /**
     * Servicio para el manejo de los estados (status)
     */
    @Autowired
    private StatusService statusService;

    /**
     * Manejo del código HTTP que se responde en las API
     */
    private HttpStatus httpStatus = HttpStatus.OK;

    /**
     * Obtener la lista de todos los jugadores registrados.
     * @return ResponseEntity con una lista de objetos Gamer y un código de estado HTTP
     *         200 si se encontraron los jugadores/as, o un código de estado HTTP 404 si no
     *         se encontraron.
     */
    @GetMapping(path = "/gamer/all")
    public ResponseEntity<List<Gamer>> gamerList(){
        try {
            List<Gamer> gamers =  gamerService.getGamerList();
            return new ResponseEntity<>(gamers, httpStatus);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene un jugador específico según el ID proporcionado.
     *
     * @param id El ID del jugador a buscar.
     * @return ResponseEntity con un objeto GAME y un código de estado HTTP 200 si se
     *         encontró el jugador, o un código de estado HTTP 404 si no se encontró.
     */
    @GetMapping(path = "/gamer/get/{id}")
    public ResponseEntity<Gamer> getOneGamer(@PathVariable("id") Long id){
        try {
            if (!gamerService.existsByIdGamer(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Gamer gamer = gamerService.findGamer(id).get();
            return new ResponseEntity<>(gamer, httpStatus);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de obtener un jugador en particular a través de una solicitud HTTP GET.
     * La ruta de acceso es "/gamer/get/user/{user}", donde {user} es el nombre de usuario del jugador a buscar.
     * @param user El nombre de usuario del jugador a buscar.
     * @return Un objeto ResponseEntity que contiene el jugador correspondiente y el estado HTTP correspondiente.
     *         Si el jugador no existe, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @GetMapping(path = "/gamer/get/user/{user}")
    public ResponseEntity<Gamer> getOneGamerByUser(@PathVariable("user") String user){
        try {
            Gamer gamer = gamerService.existUser(user);
            log.info("este es el gamer"+ gamer);
            return new ResponseEntity<>(gamer, httpStatus);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de agregar un nuevo jugador a través de una solicitud HTTP POST.
     * La ruta de acceso es "/gamer/save".
     * @param gamer El objeto Gamer que representa al jugador a agregar.
     * @return Un objeto ResponseEntity que contiene el jugador agregado y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @PostMapping(path = "/gamer/save")
    public ResponseEntity<Gamer> insertGamer(@RequestBody Gamer gamer){
        try{
            log.info("Jugador nuevo: {}", gamer);
            gamerService.postGamer(gamer,statusService.findStatus(7L).get());
            return new ResponseEntity<>(gamer, HttpStatus.CREATED);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de actualizar un jugador existente a través de una solicitud HTTP PUT.
     * La ruta de acceso es "/gamer/update/{id}", donde {id} es el ID del jugador a actualizar.
     * @param gamer El objeto Gamer que representa al jugador actualizado.
     * @param id El ID del jugador a actualizar.
     * @return Un objeto ResponseEntity que contiene el jugador actualizado y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @PutMapping(path = "/gamer/update/{id}")
    public ResponseEntity<Gamer> updateGamer(@RequestBody Gamer gamer, @PathVariable("id") Long id){
        try {
            gamerService.putGamer(id, gamer);
            return new ResponseEntity<>(gamer, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de actualizar el estado de un jugador existente a través de una solicitud HTTP PATCH.
     * La ruta de acceso es "/gamer/update/status/{id}", donde {id} es el ID del jugador a actualizar.
     * @param gamer El objeto Gamer que representa al jugador con el estado actualizado.
     * @param id El ID del jugador a actualizar.
     * @return Un objeto ResponseEntity que contiene el jugador actualizado y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @PatchMapping(path = "/gamer/update/status/{id}")
    public ResponseEntity<Gamer> updateStatusGamer(@RequestBody Gamer gamer, @PathVariable("id") Long id){
        try {
            gamerService.updateStatusGamerS(id, gamer);
            return new ResponseEntity<>(gamer, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de actualizar el tablero de un jugador existente a través de una solicitud HTTP PATCH.
     * La ruta de acceso es "/gamer/update/board/{id}", donde {id} es el ID del jugador a actualizar.
     * @param gamer El objeto Gamer que representa al jugador con el tablero actualizado.
     * @param id El ID del jugador a actualizar.
     * @return Un objeto ResponseEntity que contiene el jugador actualizado y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @PatchMapping(path = "/gamer/update/board/{id}")
    public ResponseEntity<Gamer> updateBoardGamer(@RequestBody Gamer gamer, @PathVariable("id") Long id){
        try {
            gamerService.updateBoardGamerS(id, gamer);
            return new ResponseEntity<>(gamer, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de actualizar los juegos de un jugador existente a través de una solicitud HTTP PATCH.
     * La ruta de acceso es "/gamer/update/game/{id}", donde {id} es el ID del jugador a actualizar.
     * @param gamer El objeto Gamer que representa al jugador con los juegos actualizados.
     * @param id El ID del jugador a actualizar.
     * @return Un objeto ResponseEntity que contiene el jugador actualizado y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @PatchMapping(path = "/gamer/update/game/{id}")
    public ResponseEntity<Gamer> updateGameGamer(@RequestBody Gamer gamer, @PathVariable("id") Long id){
        try {
            gamerService.updateGameGamerS(id, gamer);
            return new ResponseEntity<>(gamer, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de eliminar un jugador en particular a través de una solicitud HTTP DELETE.
     * La ruta de acceso es "/gamer/delete/{id}", donde {id} es el ID del jugador/a eliminar.
     * @param id El ID del jugador/a a eliminar.
     * @return Un objeto HttpEntity que contiene el ID del jugador/a eliminado y el estado HTTP correspondiente.
     */
    @DeleteMapping(path = "/gamer/delete/{id}")
    public HttpEntity<Long> deleteOneGamer(@PathVariable("id") Long id){
        try {
            gamerService.deleteGamer(id);
            return new ResponseEntity<>(id, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

