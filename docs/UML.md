# Diagramas de clases

GitHub renderiza estos diagramas (Mermaid) directamente.

## Arquitectura general

```mermaid
classDiagram
    class Jugable {
        <<interface>>
        +start() Hazana
    }
    class HazanaComparable {
        <<interface>>
        +comparar(otra) int
        +getPuntos() int
    }
    class Hazana {
        -jugador String
        -puntos int
        -duracionSegundos long
        -detalles Map
        +comparar(otra) int
    }
    class OperacionSuperTierra {
        +NOMBRE String
        +start() Hazana
    }
    class MinasDeMoria {
        +NOMBRE String
        +start() Hazana
    }
    class DueloDelCapitulo {
        +NOMBRE String
        +start() Hazana
    }
    class FichaDeJuego {
        -nombre String
        -descripcion String
        -arteVictoria String
        +start() Hazana
    }
    class PuestoDeMando {
        +iniciar()
    }
    Jugable <|.. OperacionSuperTierra
    Jugable <|.. MinasDeMoria
    Jugable <|.. DueloDelCapitulo
    HazanaComparable <|.. Hazana
    Jugable ..> Hazana : devuelve
    FichaDeJuego o-- Jugable
    PuestoDeMando o-- FichaDeJuego
```

## Torre de Super Tierra (Helldivers 2)

```mermaid
classDiagram
    class OperacionSuperTierra {
        -NIVELES Despliegue[3]
        +start() Hazana
    }
    class Despliegue {
        -pisos int
        -anchoBase int
        -bpm int
        -rebote double
    }
    class Baluarte {
        -campo int
        +soltar(izquierda) Resultado
        +getAltura() int
    }
    class Contenedor {
        -izquierda int
        -ancho int
    }
    OperacionSuperTierra --> Despliegue : 3 torres
    OperacionSuperTierra --> Baluarte : una por torre
    Baluarte *-- Contenedor : pisos
```

## Minas de Moria (El Señor de los Anillos)

```mermaid
classDiagram
    class MinasDeMoria {
        +start() Hazana
    }
    class GaleriasDeMoria {
        -muro boolean[][]
        +esMuro(x, y) boolean
        +dibujar(a, b) String
    }
    class Aventurero {
        -nombre String
        -simbolo char
        -color String
        +mover(dx, dy)
        +chocar()
        +puntaje() int
    }
    MinasDeMoria --> GaleriasDeMoria : un mapa
    MinasDeMoria --> Aventurero : dos jugadores
```

## Estadísticas

```mermaid
classDiagram
    class LibroDeHonor {
        +TOP int = 3
        +registrar(registro) int
        +top(juego, n) List
        +informe(juego) InformeDeCampana
    }
    class RegistroDeBatalla {
        -juego String
        -jugador String
        -puntaje int
        +comparar(otra) int
        +getRango() RangoImperial
    }
    class InformeDeCampana {
        +de(juego, registros)$ InformeDeCampana
        +imprimir()
    }
    class RangoImperial {
        <<enumeration>>
    }
    HazanaComparable <|.. RegistroDeBatalla
    LibroDeHonor o-- RegistroDeBatalla : records.txt
    LibroDeHonor ..> InformeDeCampana
    RegistroDeBatalla --> RangoImperial
```
