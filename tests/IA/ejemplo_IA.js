//var paises = require('./paises');
var u_acciones = require('../../funciones/teg_user_acciones');

// Entiendo que solo hay 3 posibles acciones:
// - Atacar si tengo un pais con 2 o mas ejercitos al lado de un enemigo, caso ideal,
//   se puede hacer mas de una vez pero lo hago una sola para q sea simple
// - Mover tropas, lo uso si no puedo atacar pero tengo 2 mas ejercitos en algun lugar
// - Pasar el turno, si no puedo hacer nada

// ejemplo de estado de juego en curso
var juego = {
                "Brasil":{"owner":"red","armies": "1"},
                "Chile":{"owner":"green","armies": "2"},
                "Peru":{"owner":"green","armies": "1"},
                "Argentina":{"owner":"black","armies": "1"},
                "Uruguay":{"owner":"black","armies": "5"}
            }

var id_IA = 'black'

// Es turno de un jugador IA, en el mapa se los identifica por color, digamos que es "black"
// Evaluo si puedo atacar
var posibles_ataques = u_acciones.getPosiblesAtaques(id_IA, juego)

if( posibles_ataques.length > 0 ){
    // Puedo atacar, elijo la primer opcion, atacamos siempre con el maximo de ejercito posible
    var ataque = posibles_ataques.shift()
    var dados_atacante = []
    var dados_defensor = []
    
    
    console.log('Es posible atacar, generando batalla')
    
    // Se pueden usar 3 dados como maximo
    if(juego[ataque.atacante].armies -1 > 3)
        dados_atacante = u_acciones.generarNDadosOrdenados( 3 )
    else
        dados_atacante = u_acciones.generarNDadosOrdenados( juego[ataque.atacante].armies -1 )
    if(juego[ataque.defensor].armies > 3)
        dados_defensor = u_acciones.generarNDadosOrdenados( 3 )
    else
        dados_defensor = u_acciones.generarNDadosOrdenados( juego[ataque.defensor].armies )
    

    console.log(dados_atacante)
    console.log(dados_defensor)    
    
    var resuldatoB = u_acciones.getResultadoBatalla(dados_atacante, dados_defensor)
    
    // aca tenes en ataque la info de los paises, y en resultadoB como tenes que actualizar el mapa
    console.log('Paises que intervienen en la batalla:')
    console.log(ataque)
    console.log('Resultado de la batalla:')
    console.log(resuldatoB)
    // Actualizamos el mapa
    juego[ataque.defensor].armies -= resuldatoB.perdidaDefensor
    juego[ataque.atacante].armies -= resuldatoB.perdidaAtacante

    // Si el enemigo esta derrotado, tenemos que ocupar el pais, lo hacemos con el maximo de ejercito posible
    if(juego[ataque.defensor].armies < 1){
       var ejercito_moviendose = juego[ataque.atacante].armies - 1 
       juego[ataque.atacante].armies -= ejercito_moviendose
       juego[ataque.defensor].armies = ejercito_moviendose
       juego[ataque.defensor].owner = id_IA
    }

    console.log('El mapa despues del ataque')
    console.log(juego)
}
else{
    // No es posible atacar, o movemos ejercitos o pasamos del turno
}