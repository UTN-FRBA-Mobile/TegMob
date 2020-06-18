const users = require('users/user.service');
const match = require('match/match.service');

module.exports = {
    removePlayerSocket,
    startMatch
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