const users = require('users/user.service');
const match = require('match/match.service');
const acciones = require('funciones/teg_user_acciones');

module.exports = {
    removePlayerSocket,
    startMatch,
    tryAttack,
    getCurrentTurn
};

//async function addPlayerSocket(conectados, id_user, _socket) {
    //const user = await users.getById(id); El control ya se hace adentro
    //if (!user) throw `No user with id ${id}`
    //const matches = await match.getByUser(id)
    //await users.setSocket(id_user,_socket);
//}


async function startMatch(match_id) {
    return await match.start(match_id)
}

async function removePlayerSocket(_socket) {
    const userall = users.getAll();
    const id = userall.find(u => u.socket.id === _socket.id);
    return await users.update(id,{socket: null});
}

async function tryAttack( id_user, atack ){
    let juego = await match.getById(atack.id_match)
    const index_turno = juego.turn % juego.players.length
    const posibles_ataques = acciones.getPosiblesAtaques(juego.players[index_turno].color, juego.countries)
    var res_atk = {   'attacker_id': id_user,
                        'defender_id': null,
                        'attack_result':{
                            'dados': {
                                'attacker': [],
                                'defender': []
                            },
                            'map_change': {},
                            'message': atack.atacante + ' ataca ' + atack.defensor
                        }
                    }
    
    juego.players.forEach(jugador => {
        if(jugador.color == juego.countries[atack.defensor].owner)
            res_atk.defender_id = jugador.user.slice()
    });
    if( juego.stage != 'STARTED') throw ('Intentan atacar en un juego no empezado');
    if( id_user != juego.players[index_turno].user ) throw('Jugador intenta atacar y no es su turno');
    if( posibles_ataques.some(pb =>{ return pb.atacante == atack.atacante && pb.defensor == atack.defensor }) ){
        //generamos los dados, siempre al maximo
        if(juego.countries[atack.atacante].armies - 1 > 2)
            res_atk.attack_result.dados.attacker = acciones.generarNDadosOrdenados(3)
        else
            res_atk.attack_result.dados.attacker = acciones.generarNDadosOrdenados(juego.countries[atack.atacante].armies - 1)
        if(juego.countries[atack.defensor].armies > 2)
            res_atk.attack_result.dados.defender = acciones.generarNDadosOrdenados(3)
        else
            res_atk.attack_result.dados.defender = acciones.generarNDadosOrdenados(juego.countries[atack.defensor].armies)
        // Se da la batalla
        const res_batalla = acciones.getResultadoBatalla(res_atk.attack_result.dados.attacker, res_atk.attack_result.dados.defender)
        // rearmar el mapa
        var map_novedad = res_atk.attack_result.map_change
        map_novedad[atack.atacante] = juego.countries[atack.atacante]
        map_novedad[atack.defensor] = juego.countries[atack.defensor]
        map_novedad[atack.atacante].armies -= res_batalla.perdidaAtacante
        map_novedad[atack.defensor].armies -= res_batalla.perdidaDefensor
        // Ocupacion
        if( map_novedad[atack.defensor].armies < 1){
            map_novedad[atack.defensor].owner = map_novedad[atack.atacante].owner
            map_novedad[atack.defensor].armies = map_novedad[atack.atacante].armies -1
            map_novedad[atack.atacante].armies = 1
            res_atk.attack_result.message = 'Jugador ' + map_novedad[atack.atacante].owner + ' a ocupado ' + atack.defensor
        }
    } else {
        throw('no es posible el ataque')
    }
    
    // Actualizar proximo turno
    juego.turn += 1
    juego.currentPlayer = juego.players[juego.turn % juego.players.length].user
    // Actualizar match
    juego.countries = Object.create(juego.countries)
    await juego.save()
    
    return {'map_change': res_atk, 'start_turn': {'players': juego.players, 'id_match': juego._id}}
}

async function getCurrentTurn( id_match ){
    return await match.getCurrentTurn(id_match)
}