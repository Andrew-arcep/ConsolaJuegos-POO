# Registro de cambios

Formato basado en *Keep a Changelog*. Fechas en AAAA-MM-DD.

## [1.0.1] - 2026-10-15

### Cambiado
- El juego descargado se movio del paquete generico `games` a `descargados.piedrapapel`, para que no choque
  con el paquete de otro juego descargado al compartir el proyecto.
- Se corrigio la indentacion de las constantes `NOMBRE` en los juegos.

## [1.0.0] - 2026-10-08

### Agregado
- Consola de juegos en Java por línea de comandos con menú: lanzar juego, juegos descargados y ver stats.
- **Torre de Super Tierra** (Helldivers 2): apilador rítmico con metrónomo, caída tipo Tetris, 3 torres de
  dificultad creciente (la primera es tutorial) y mensajes en letra grande al completar cada torre.
- **Minas de Moria** (El Señor de los Anillos): laberinto de 2 jugadores por turnos, con muros `···` y `|`,
  jugadores representados por su inicial y un color por equipo.
- **Piedra, Papel o Tijera**: juego descargado de otra persona, integrado con el adaptador `DueloDelCapitulo`
  en la categoría "Juegos descargados".
- Mini menú dentro de cada juego (jugar / leer instrucciones / salir) con instrucciones redactadas en el
  lenguaje de cada universo.
- Ilustración ASCII de victoria cuando se rompe el récord de un juego.
- Limpieza de pantalla entre escenas para que la consola no crezca (`Cogitador`).
- Interfaz `Jugable` con un único método `start()`, que devuelve una `Hazana` (el resultado de la partida).
- Interfaz `HazanaComparable` con `comparar(...)`, usada por el ranking.
- Registro de estadísticas en `records.txt`: tras cada partida se compara contra el **top 3** del juego y se
  informa el puesto.
- "Ver stats" muestra, por juego, el informe calculado (`InformeDeCampana`) y el top 3 actual.
- Rangos ambientados en Warhammer 40k según el puntaje (`RangoImperial`).
- README, diagramas UML y este registro de cambios.

### Cambiado
- Las clases usan nombres de los tres universos en lugar de nombres genéricos.
- Cada juego es autónomo: implementa `Jugable` y no hereda de ninguna clase base externa.
- Los juegos devuelven una `Hazana` en lugar de guardar o conocer los records.
- Todas las estadísticas se registran únicamente en la consola; se eliminó el ranking en archivo propio
  del juego descargado.

### Corregido
- Los jugadores se paraban en las aberturas entre habitaciones y su letra desaparecía; ahora se mueven
  de habitación en habitación.
- Una derrota con 0 puntos ya no muestra la ilustración de "nuevo récord".
