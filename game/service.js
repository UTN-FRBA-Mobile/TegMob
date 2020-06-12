const users = require('users/user.service');
const match = require('match/match.service');

module.exports = {
    addPlayerSocket,
    startMatch,
    removePlayerSocket
};

async function addPlayerSocket(id, _socket) {
    const user = users.getById(id);
    if (!user) throw `No user with id ${id}`
    const matches = match.getAll().filter(m => m.players.some(p => p.user === id));
    matches.map(m => _socket.join(m.matchname))
    return await users.update(id,{socket: _socket});
}

async function startMatch(match_id) {
    return match.start(match_id);
}

async function removePlayerSocket(_socket) {
    const userall = users.getAll();
    const id = userall.find(u => u.socket.id === _socket.id);
    return await users.update(id,{socket: null});
}