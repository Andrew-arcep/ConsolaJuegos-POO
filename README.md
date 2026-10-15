# Sala de Juegos (consola)

Aplicación de **consola en Java** (sin interfaz gráfica) hecha para POO. Es una sala de juegos con un menú,
varios juegos modelados como objetos y un registro de estadísticas que guarda las partidas y mantiene el
**top 3** de cada juego.

Los textos y nombres de clases están ambientados en tres universos: **Helldivers 2**, **Warhammer 40k** y
**El Señor de los Anillos**.

## Cómo ejecutarlo

Requisitos: **JDK 17 o superior** (el juego descargado usa `record`).

**En IntelliJ IDEA**
1. Abrir la carpeta del proyecto y marcar `src` como *Sources Root*.
2. Ejecutar la clase `consola.Main`.
3. Para que la pantalla se limpie y se redibuje en su sitio: *Run → Edit Configurations → Modify options →
   Emulate terminal in output console* (o agregar `-Dpantalla=ansi` en *VM options*).
   Sin eso el juego funciona en un modo compacto.

**Desde la terminal**
```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -Dfile.encoding=UTF-8 -Dpantalla=ansi -cp out consola.Main
```

## Menú

```
========== SALA DE JUEGOS ==========
 1. Lanzar juego          juegos propios
 2. Juegos descargados    juegos de otras personas
 3. Ver stats             estadísticas y top 3 por juego
 0. Salir
```

Cada juego abre un mini menú: **Jugar / Leer instrucciones / Salir al menú**.

## Juegos

| Juego | Universo | Descripción |
|---|---|---|
| **Torre de Super Tierra** (`OperacionSuperTierra`) | Helldivers 2 | Juego rítmico de apilar bloques. Un contenedor recorre la fila de arriba al compás de un metrónomo (TUM / tak) y al pulsar ENTER cae sobre la torre. Hay 3 torres; la primera es un tutorial y cada una es más difícil (más pisos, base más angosta, más BPM, movimiento errático). |
| **Minas de Moria** (`MinasDeMoria`) | El Señor de los Anillos | Carrera de **2 jugadores** por turnos en un laberinto generado al azar. Cada jugador mueve hasta 3 habitaciones por turno con W/A/S/D. Gana quien llegue primero a la Puerta Este. |
| **Piedra, Papel o Tijera** (`DueloDelCapitulo`) | Warhammer 40k | Juego **descargado** de otra persona (paquete `descargados.piedrapapel`), integrado con un adaptador. |

## Arquitectura

```
consola/
├── Main.java                      punto de entrada
├── menu/    PuestoDeMando, FichaDeJuego
├── games/   Jugable, Hazana, HazanaComparable
│   ├── apilador/   OperacionSuperTierra, Baluarte, Contenedor, Despliegue
│   ├── laberinto/  MinasDeMoria, GaleriasDeMoria, Aventurero
│   └── duelo/      DueloDelCapitulo (adaptador del juego descargado)
├── stats/   LibroDeHonor, RegistroDeBatalla, InformeDeCampana, RangoImperial
└── util/    Cogitador, Heraldo, Vestibulo
descargados/piedrapapel/   paquete del juego descargado (Piedra, Papel o Tijera), sin dependencias de `consola`
```

### Interfaces

- **`Jugable`** — contrato de todo juego. Un único método: `Hazana start()`.
- **`HazanaComparable`** — un resultado que sabe compararse con otro: `int comparar(HazanaComparable otra)`.
  Lo implementan `Hazana` y `RegistroDeBatalla`; el ranking ordena con él sin conocer cómo puntúa cada juego.

### Flujo de una partida

```
Juego --start()--> Hazana --> PuestoDeMando --crea--> RegistroDeBatalla --> LibroDeHonor --> records.txt
                                                       (compara contra el top 3)
```

1. El juego se juega y devuelve una `Hazana` (jugador, puntos, duración, detalle).
2. El menú la convierte en un `RegistroDeBatalla` (agrega el nombre del juego y la fecha).
3. `LibroDeHonor.registrar(...)` lo **compara contra el top 3** de ese juego, lo guarda en `records.txt` y
   devuelve el puesto (1 a 3, o 0 si no entra). Ante un empate conserva el puesto quien ya estaba.
4. Si queda en el puesto 1 se muestra la ilustración de victoria del juego.

### Estadísticas

`InformeDeCampana` calcula por juego: partidas, jugadores distintos, puntaje promedio, mejor y peor
puntaje (con el jugador) y duración promedio. "Ver stats" muestra ese informe y el **top 3 actual** de cada juego.

## Cómo agregar un juego nuevo

1. Crear una clase que implemente `Jugable` (o un adaptador si el juego viene de otra persona).
   Debe tener `NOMBRE` y `DESCRIPCION` y devolver una `Hazana` desde `start()`.
2. Registrarla en `Main` creando su `FichaDeJuego`, en la lista `propios` o en `descargados`.

No hay que tocar el menú, el ranking ni las estadísticas.

## Juegos descargados

El juego de otra persona se copia a su propio paquete (`descargados.<nombre>`, para que no choque con otros) y un adaptador (por ejemplo
`DueloDelCapitulo`) lo envuelve para cumplir `Jugable`. Su código **no depende** de la consola.
Cambios hechos al original: se le agregó un getter con el nombre del jugador y se le quitó el ranking propio
en archivo, porque todas las estadísticas se registran en la consola.

## Documentación

- Diagramas de clases: [`docs/UML.md`](docs/UML.md)
- Registro de cambios: [`CHANGELOG.md`](CHANGELOG.md)
- Javadoc: `javadoc -encoding UTF-8 -d docs/api -sourcepath src consola.games consola.stats consola.menu`
