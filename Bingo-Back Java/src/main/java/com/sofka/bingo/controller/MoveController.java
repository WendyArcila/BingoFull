package com.sofka.bingo.controller;

import com.sofka.bingo.domain.Game;
import com.sofka.bingo.domain.Move;
import com.sofka.bingo.service.MoveService;
import com.sofka.bingo.utility.CreateMove;
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



import java.util.List;

/**
 * Clase MoveController contiene los controladores que se encargarán de recibir, orquestar
 * el proceso necesario y responder las solicitudes de la API, del juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 *
 */
@Slf4j
@RestController
public class MoveController {

    /**
     * Servicio para el manejo de la jugada (move)
     */
    @Autowired
    private MoveService moveService;


    /**
     * Manejo del código HTTP que se responde en las API
     */
    private HttpStatus httpStatus = HttpStatus.OK;


    /**
     * Obtener la lista de las jugadas registradas.
     * @return ResponseEntity con una lista de objetos Move y un código de estado HTTP
     *         200 si se encontraron las jugadas, o un código de estado HTTP 404 si no
     *         se encontraron.
     */
    @GetMapping(path = "/move/all")
    public ResponseEntity<List<Move>> moveList(){
        try {
            List<Move> moves =  moveService.getMoveList();
            return new ResponseEntity<>(moves, httpStatus);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene una jugada específica según el ID proporcionado.
     *
     * @param id El ID de la jugada a buscar.
     * @return ResponseEntity con un objeto MOVE y un código de estado HTTP 200 si se
     *         encontró la jugada, o un código de estado HTTP 404 si no se encontró.
     */
    @GetMapping(path = "/move/get/{id}")
    public ResponseEntity<Move> getOneMove (@PathVariable("id") Long id){
        try {
            if (!moveService.existsByIdMove (id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Move move = moveService.findMove (id).get();
            return new ResponseEntity<>(move, httpStatus);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de guardar un movimiento en una partida a través de una solicitud HTTP POST.
     * La ruta de acceso es "/move/save".
     * @param game El objeto Game que representa la partida en la que se realizará el movimiento.
     * @return Un objeto ResponseEntity que contiene el movimiento guardado y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @PostMapping(path = "/move/save")
    public ResponseEntity<Move> insertMove (@RequestBody Game game){
        try{
            Move move = new Move();
            CreateMove createMove= new CreateMove();
            int number = createMove.randomNumber();
            String letter = createMove.letter(number);
            moveService.postMove (move, number,letter, game);
            return new ResponseEntity<>(move, HttpStatus.CREATED);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de actualizar una jugada existente a través de una solicitud HTTP PUT.
     * La ruta de acceso es "/move/update/{id}", donde {id} es el ID de la jugada a actualizar.
     * @param move El objeto Move que representa la jugada actualizada.
     * @param id El ID de la jugada a actualizar.
     * @return Un objeto ResponseEntity que contiene la jugada actualizada y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @PutMapping(path = "/move/update/{id}")
    public ResponseEntity<Move> updateMove (@RequestBody Move move, @PathVariable("id") Long id){
        try {
            moveService.putMove (id, move);
            return new ResponseEntity<>( move, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de eliminar un movimiento específico mediante una solicitud HTTP DELETE.
     * La ruta de acceso es "/move/delete/{id}", donde {id} es el ID del movimiento a eliminar.
     * @param move El objeto Move que representa el movimiento a eliminar.
     * @return Un objeto HttpEntity que contiene el movimiento eliminado y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @DeleteMapping(path = "/move/delete/{id}")
    public HttpEntity<Move> deleteOneMove (Move move){
        try {
            moveService.deleteMove (move);
            return new ResponseEntity<>( move, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
