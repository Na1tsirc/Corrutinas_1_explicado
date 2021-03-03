import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


//RECOMENDACIÓN:

var cubosActuales = 0
var lenaActual = 0
var ramasActuales = 0
var comidaActual = 0

const val CUBOS_NECESARIOS = 4
const val LENA_NECESARIA  = 2
const val RAMA_NECESARIA  = 1
const val COMIDA_NECESARIA  = 1

var hamaca = Mutex()
var hacha = Mutex()

fun main() {
    comenzar()
    Thread.sleep(80000)
}

//caes en la isla desierta
fun comenzar(){
    GlobalScope.launch {
        coroutineScope {
            // Amigo A
            //Tabajo del amigo a ir a por agua(dura 4 segundos) y descansar 1 segundo
            launch {
                repeat(CUBOS_NECESARIOS) {
                    irAPorAgua("Amigo A")
                    descansar(1000, "Amigo A")
                }
            }
            // Amigo B
            //solo hay un hacha entonces solo puede ir 1 a cortar leña(5 segundos 2 veces)
            // descanso de 3segundos
            launch {
                repeat(LENA_NECESARIA){
                    irAPorLena("Amigo B")
                    descansar(3000, "Amigo B")
                }
            }
            // Amigo C
            // va a por ramas y a cazar y no descansa
            launch {
                irAPorRamas("Amigo C")
                irACazar("Amigo C")
            }

        }
        //Cuando las 3 condiciones se cumplan con el autoincrento podran escapar
        if (cubosActuales == CUBOS_NECESARIOS && lenaActual == LENA_NECESARIA && ramasActuales == RAMA_NECESARIA && comidaActual == COMIDA_NECESARIA){
            println("Barca construida y aprovisionada con exito")
        } else {
            println("Algo ha fallado")
        }
    }
}
//necesitan comida , no hay descanso
suspend fun irACazar(nombre : String) {
    println("El amigo $nombre va a Cazar")
    hacha.withLock {
        println("El amigo $nombre coge el hacha")
        delay(4000)
        comidaActual++
        println("El amigo $nombre deja el hacha")
    }
    println("El amigo $nombre va a por leña")
}

//todos descansan pero solo hay una hamaca y el tiempo de descanso todos es distinto
suspend fun descansar(tiempo : Long, nombre : String) {
    println("El amigo $nombre, quiere descansar")
    hamaca.withLock {
        println("El amigo $nombre, se tumba en la hamaca")
        delay(tiempo)
        println("El amigo $nombre, se levanta de la hamaca")
    }
    println("El amigo $nombre, deja de descansar")
}

//va a por leña
//dura 5 segundos y se tiene que haer un minimo de 2 veces
suspend fun irAPorLena(nombre : String) {
    println("El amigo $nombre va a por leña")
    hacha.withLock {
        println("El amigo $nombre coge el hacha")
        delay(5000)
        lenaActual++
        println("El amigo $nombre deja el hacha")
    }
    println("El amigo $nombre vuelve con la leña")
}

//c va a por ramas , no descansa
suspend fun irAPorRamas(nombre : String) {
    println("El amigo $nombre va a por ramas")
    delay(3000)
    ramasActuales++
    println("El amigo $nombre vuelve con ramas")
}

//ir a por un cubo de agua que dura 4 segundos y aumenta el contador de cubos
//actuales
suspend fun irAPorAgua(nombre : String) {
    println("El amigo $nombre va a por un cubo de agua")
    delay(4000)
    cubosActuales++
    println("El amigo $nombre vuelve con un cubo de agua")
}