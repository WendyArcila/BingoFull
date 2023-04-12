SELECT * FROM bingo.gamer;

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("Iniciado", "El juego comenzó");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("En espera", "En espera de jugadores");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("En curso", "El juego está en curso");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("Evaluando", "Un jugador pulsó el botón de gané");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("Finalizado", "El juego finalizó");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("Conectado", "Gamer ha iniciado sesión");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("En espera", "Gamer se encuentra en sala de espera");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("Jugando", "Gamer está dentro del juego");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("Ganador", "Gamer ha ganado");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("Descalificado", "Gamer no ha cumplido las reglas");

INSERT INTO bingo.status (statu_name, statu_description)
VALUES("Desconectado", "Gamer no se encuentra en sesión");