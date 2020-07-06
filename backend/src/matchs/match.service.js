const db = require('../_helpers/db');
const Match = db.Match;
const users = require('../users/user.service');
const paises = require('../funciones/paises')

module.exports = {
    getAll,
    getById,
    getByUser,
    create,
    join,
    leave,
    delete: _delete,
    start,
    getByName,
    getCurrentTurn
};

async function getAll() {
    return await Match.find();
}

async function getByUser(id) {
    var consulta = { "players.user": id }
    return await Match.find(consulta);
}

async function getById(id) {
    return await Match.findById(id);
}

async function getByName(name) {
    return await Match.findOne(m => m.matchname === name);
}

async function create(matchParam) {
    if (await Match.findOne({ matchname: matchParam.matchname })) {
        throw 'Matchname "' + matchParam.matchname + '" is already taken';
    }

    const user = await users.getById(matchParam.owner);

    if (!user) throw 'Invalid Owner'

    matchParam.players = [{ color: "black", user: matchParam.owner, armies: 0 }]
    const match = new Match(matchParam);

    await match.save();

    return match;
}

async function join(id, matchParam) {
    const match = await Match.findById(id);

    if (!match) throw 'Match not found';

    if (match.stage !== "CREATED") throw 'Match already started'

    if (match.players.length === match.size) throw 'Match full';

    const user = await users.getById(matchParam.userToAdd);

    if (!user) throw 'Invalid User';

    if (match.players.some(p => p.user === matchParam.userToAdd)) throw 'User already in match';

    switch (match.players.length) {
        case 1:
            match.players.push({ color: "green", user: matchParam.userToAdd, armies: 0 });
            break;
        case 2:
            match.players.push({ color: "yellow", user: matchParam.userToAdd, armies: 0 });
            break;
        case 3:
            match.players.push({ color: "red", user: matchParam.userToAdd, armies: 0 });
            break;
        case 4:
            match.players.push({ color: "magenta", user: matchParam.userToAdd, armies: 0 });
            break;
        case 5:
            match.players.push({ color: "cyan", user: matchParam.userToAdd, armies: 0 });
            break;
    }

    await match.save();

    return match;
}

async function leave(id, matchParam) {
    const match = await Match.findById(id);

    if (!match) throw 'Match not found';

    if (match.stage !== "CREATED") throw 'Match already started'

    const user = await users.getById(matchParam.userToRemove);

    if (!user) throw 'Invalid User';

    if (matchParam.userToRemove === match.owner) throw "Owner can't leave match"

    const _players = match.players.filter(p => p.user !== matchParam.userToRemove)

    match.players = _players;

    await match.save();

    return match;
}

async function _delete(id) {
    const match = await Match.findById(id);

    if (!match) throw 'Wrong match id'

    if (match.stage !== "CREATED") throw 'Match already started'

    await Match.findByIdAndRemove(id);
}

async function start(id) {
    const match = await Match.findById(id);

    if (!match) throw 'Match not found';
    if (match.stage !== "CREATED") throw 'Match already started'

    match.stage = "STARTED";

    var colores = paises.getNColoresPosibles(match.players.length)
    var p_temp = [] // si no lo actualizas por completo no escribe
    for (let i = 0; i < match.players.length; i++) {
        p_temp[i] = { 'color': colores[i], 'user': match.players[i].user, 'armies': match.players[i].armies }
    };
    match.players = p_temp
    match.countries = paises.getMapaInicial(colores)

    match.currentPlayer = match.players[1].user;
    match.turn = 1;

    await match.save();

    return match;
}

async function getCurrentTurn(id) {
    let partida = await getById(id)
    return {'id': id, 'turn': partida.turn, 'currentColor': partida.players[partida.turn % partida.players.length].color, 'players': partida.players}
}