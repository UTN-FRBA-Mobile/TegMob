const db = require('_helpers/db');
const Match = db.Match;
const users = require('users/user.service');
const utils = require('_helpers/utils');


module.exports = {
    getAll,
    getById,
    create,
    join,
    leave,
    delete: _delete,
    start
};

async function getAll() {
    return await Match.find();
}

async function getById(id) {
    return await Match.findById(id);
}

async function create(matchParam) {
    if (await Match.findOne({ matchname: matchParam.matchname })) {
        throw 'Matchname "' + matchParam.matchname + '" is already taken';
    }

    const user = await users.getById(matchParam.owner);
    
    if (!user) throw 'Invalid Owner'

    matchParam.players = [ { color: "black", user: matchParam.owner, armies: 0 } ]
    const match = new Match(matchParam);

    await match.save();

    return match;
}

async function join(id, matchParam) {
    const match = await Match.findById(id);

    if (!match) throw 'Match not found';

    if (match.stage !== "CREATED") throw 'Match already started'

    if (match.players.size() = match.size) throw 'Match full';

    const user = await users.getById(matchParam.userToAdd);
    
    if (!user) throw 'Invalid User';

    if (match.players.some(p => p.user === userToAdd)) throw 'User already in match';

    switch (match.players.size()) {
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
}

async function leave(id, matchParam) {
    const match = await Match.findById(id);

    if (!match) throw 'Match not found';

    if (match.stage !== "CREATED") throw 'Match already started'

    if (match.players.size() = match.size) throw 'Match full';

    const user = await users.getById(matchParam.userToRemove);
    
    if (!user) throw 'Invalid User';

    if (matchParam.userToRemove === match.owner) throw "Owner can't leave match"

    const _players = match.players.filter(p => p.user !== userToRemove)

    match.players = _players;

    await match.save();
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

    const countriestemp = Object.entries(match.countries);
    const countriestemp2 = Object.entries(match.countries);

    const promedio = Math.trunc(countriestemp.size()/match.players.size());

    match.players.forEach(p => {
        for (let i = 0; i < promedio; i++) {
            const num = utils.getRndInteger(0,countriestemp.size()-1);
            const name = countriestemp[num][0]
            const where = countriestemp2.findIndex(c => c[0] === name);
            countriestemp2[where][1].owner = p.username;
            countriestemp2[where][1].armies = 3;
            countriestemp.splice(num,1);
        }
    });

    match.players.forEach(p => {
        if (countriestemp.size()>0) {
            const name = countriestemp[0][0]
            const where = countriestemp2.findIndex(c => c[0] === name);
            countriestemp2[where][1].owner = p.username;
            countriestemp2[where][1].armies = 3;
            countriestemp.splice(num,1);
        } else {
            break;
        }
    })

    match.countries = Object.fromEntries(countriestemp2);

    match.currentPlayer = match.players[utils.getRndInteger(0,countriestemp2.size()-1)].username;

    await match.save();

    return match;
}