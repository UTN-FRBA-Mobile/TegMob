const db = require('../_helpers/db');
const User = db.User;
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');

module.exports = {
    authenticate,
    getAll,
    getById,
    create,
    update,
    delete: _delete,
    getByName,
    setSocket
};

async function setSocket(id_user, socket) {
    var xuser = await User.findById(id_user)
    xuser.socket = socket
    // no funciona, hay q tocar el objeto socket
    await xuser.save()
}

async function authenticate({ username, password }) {
    const user = await User.findOne({ username });
    if (user && bcrypt.compareSync(password, user.hash)) {
        const token = jwt.sign({ sub: user.id }, process.env.JWT_SECRET);
        return {
            ...user.toJSON(),
            token
        };
    }
}

async function getAll() {
    return await User.find();
}

async function getById(id) {
    return await User.findById(id);
}

async function getByName(name) {
    return await User.findOne(u => u.username === name);
}

async function create(userParam) {
    if (await User.findOne({ username: userParam.username })) {
        throw 'Username "' + userParam.username + '" is already taken';
    }

    const user = new User(userParam);

    if (userParam.password) {
        user.hash = bcrypt.hashSync(userParam.password, 10);
    }

    await user.save();

    return user;
}

async function update(id, userParam) {
    const user = await User.findById(id);

    if (!user) throw 'User not found';
    if (user.username !== userParam.username && await User.findOne({ username: userParam.username })) {
        throw 'Username "' + userParam.username + '" is already taken';
    }

    if (userParam.password) {
        userParam.hash = bcrypt.hashSync(userParam.password, 10);
    }

    Object.assign(user, userParam);

    await user.save();
}

async function _delete(id) {
    await User.findByIdAndRemove(id);
}