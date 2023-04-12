package com.sofka.bingo.controller;

import com.sofka.bingo.domain.Game;
import com.sofka.bingo.service.GameService;
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
 * Clase GameController contiene los controladores que se encargarán de recibir, orquestar
 * el proceso necesario y responder las solicitudes de la API, del juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 *
 */
@Slf4j
@RestController
public class GameController {

    /**
     * Servicio para el manejo del juego (game)
     */
    @Autowired
    private GameService gameService;

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
     * Obtener la lista de todos los juegos disponibles.
     * @return ResponseEntity con una lista de objetos Game y un código de estado HTTP
     *         200 si se encontraron los tableros, o un código de estado HTTP 404 si no
     *         se encontraron.
     */
    @GetMapping(path = "/game/all")
    public ResponseEntity<List<Game>> gameList(){
        try {
            List<Game> games =  gameService.getGameList();
            return new ResponseEntity<>(games, httpStatus);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene un juego específico según el ID proporcionado.
     *
     * @param id El ID del juego a buscar.
     * @return ResponseEntity con un objeto GAME y un código de estado HTTP 200 si se
     *         encontró el juego, o un código de estado HTTP 404 si no se encontró.
     */
    @GetMapping(path = "/game/get/{id}")
    public ResponseEntity<Game> getOneGame(@PathVariable("id") Long id){
        try {
            if (!gameService.existsByIdGame(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Game game = gameService.findGame(id).get();
            return new ResponseEntity<>(game, httpStatus);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea e inserta un nuevo juego en la base de datos con los datos proporcionados.
     *
     * @return ResponseEntity con el objeto Board insertado y un código de estado HTTP 201.
     *         Si hay algún error, se devuelve un código de estado HTTP 404.
     */
    @PostMapping(path = "/game/save")
    public ResponseEntity<Game> insertGame(){
        try{
            Game game = new Game();
            game = gameService.postGame(game,statusService.findStatus(2L).get());
            return new ResponseEntity<>(game, HttpStatus.CREATED);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza juego existente en la base de datos según el ID proporcionado.
     * @param game El objeto Game que contiene los nuevos datos del juego.
     * @param id El ID del juego a actualizar.
     * @return ResponseEntity con el objeto Game actualizado y un código de estado HTTP 200 si fue actualizado
     */
    @PutMapping(path = "/game/update/{id}")
    public ResponseEntity<Game> updateGame(@RequestBody Game game, @PathVariable("id") Long id){
        try {
            gameService.putGame(id, game);
            return new ResponseEntity<>(game, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Actualiza el estado de un juego específico según el ID proporcionado.
     *
     * @param game El objeto Game con el nuevo estado del juego.
     * @param id El ID del juego a actualizar.
     * @return ResponseEntity con el objeto Game actualizado y un código de estado HTTP 200 si
     *         la actualización fue exitosa, o un código de estado HTTP 404 si no se encontró el juego.
     */
    @PatchMapping(path = "/game/update/status/{id}")
    public ResponseEntity<Game> updateStatusGame(@RequestBody Game game, @PathVariable("id") Long id) {
        try {
            // Actualizar el estado del juego usando el servicio correspondiente.
            gameService.updateStatusG(id, game);

            // Devolver una respuesta con el juego actualizado y un código de estado HTTP 200.
            return new ResponseEntity<>(game, httpStatus);
        } catch (Exception exc) {
            // Imprimir la excepción en caso de que ocurra algún error.
            System.out.println(exc);

            // Devolver una respuesta con un código de estado HTTP 404.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza el ganador o ganadora de un juego específico según el ID proporcionado.
     *
     * @param game El objeto Game con el nuevo winner del juego.
     * @param id El ID del juego a actualizar.
     * @return ResponseEntity con el objeto Game actualizado y un código de estado HTTP 200 si
     *         la actualización fue exitosa, o un código de estado HTTP 404 si no se encontró el juego.
     */
    @PatchMapping(path = "/game/update/winner/{id}")
    public ResponseEntity<Game> updateWinnerGame(@RequestBody Game game, @PathVariable("id") Long id){
        try {
            gameService.updateWinnerG(id, game);
            return new ResponseEntity<>(game, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de eliminar un juego en particular a través de una solicitud HTTP DELETE.
     * La ruta de acceso es "/game/delete/{id}", donde {id} es el ID del juego a eliminar.
     * @param id El ID del juego a eliminar.
     * @return Un objeto HttpEntity que contiene el ID del juego eliminado y el estado HTTP correspondiente.
     */

    @DeleteMapping(path = "/game/delete/{id}")
    public HttpEntity<Long> deleteOneGame(@PathVariable("id") Long id){
        try {
            gameService.deleteGame(id);
            return new ResponseEntity<>(id, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
