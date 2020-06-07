// Importar para ejecutar funciones: var tools = require('<archivo>');
// ejecute: tools.<funcion>(<variable>);
// Requiere tambien incluir las funciones de paises.js --> 
var f_paises = require('./paises');

// Entiendo que solo hay 3 posibles acciones:
// - Atacar si tengo un pais con 2 o mas ejercitos al lado de un enemigo, caso ideal,
//   se puede hacer mas de una vez pero lo hago una sola para q sea simple
// - Mover tropas, lo uso si no puedo atacar pero tengo 2 mas ejercitos en algun lugar
// - Pasar el turno, si no puedo hacer nada

// El formato del mapa en juego es el siguiente
// {
//     "brazil":{"owner":"lightblue","armies": "5"},
//     "peru":{"owner":"green","armies": "3"},
//     "sahara":{"owner":"green","armies": "4"}
// }


module.exports = {
    
    /**
     * @param {Array} dadosAtaque Dados ordenados atacante
     * @param {Array} dadosDefensa Dados ordenados defensor
     */
    getResultadoBatalla: function( dadosAtaque, dadosDefensa ){
        // Recibe los dados ya ordenados:
        //     atacante: [4,3,2],
        //     defensor: [6,1]
        var resultado = {perdidaAtacante: 0, perdidaDefensor: 0}

        for( var i = 0; i < Math.min.apply(null,[dadosAtaque.length,dadosDefensa.length]); i++){
            if (dadosAtaque[i] > dadosDefensa[i]){
                resultado.perdidaDefensor += 1
            }
            else{
                resultado.perdidaAtacante += 1
            }
        }
        return resultado
    },

    generarNDadosOrdenados: function (n){
        var dados = []
        
        for(var i=0; i < n; i++){
            dados.push(Math.floor(Math.random()*6) + 1);
        }
        return dados.sort();
    },

    // indico color del pais y el estado del mapa para obtener posibles convinaciones de ataque
    // Salida: [{atacante: Uruguay, defensor: Brasil},{atacante: Uruguay, defensor: Peru}]
    getPosiblesAtaques: function (id_user, mapa){
        var posiblesAtaques = []

        this.getPaisesConCapacidadDeAtaque(id_user, mapa).forEach(pais => {
            f_paises.get_limitrofes(pais).forEach(victima => {
                if (mapa[victima].owner != id_user){
                    posiblesAtaques.push({'atacante': pais, 'defesor': victima});
                }
            });
        });
        return posiblesAtaques;
    },

    getPaisesConCapacidadDeAtaque: function ( id_User, mapa ){
        // recorremos los paises ocupados por mi, sacamos los que tienen 2 ejercitos o mas 
        var mis_paises_cca = []
        this.getPaisesOcupados(id_User, mapa).forEach(pais => {
            if(mapa[pais].armies > 1)
                mis_paises_cca.push(pais)
        });
        return mis_paises_cca
    },

    getPaisesOcupados: function ( id_user, mapa){
        // recorremos el mapa, obtenemos los paises que estan ocupados por mi
        var mis_paises_ocupados = []
        Object.entries(mapa).forEach(element => {
            if(element[1].owner == id_user){
                mis_paises_ocupados.push(element[0])
            }
        });
        return mis_paises_ocupados;
    }

}
