require('rootpath')();
require('dotenv').config();
const app = require('express')(),
	cors = require('cors'),
	bodyParser = require('body-parser'),
	jwt = require('src/_helpers/jwt'),
	errorHandler = require('src/_helpers/error-handler'),
	games = require('src/game/service');

app.use(bodyParser.urlencoded({ extended: false })).use(bodyParser.json());
app.use(cors()); //.use(jwt());

app.use('/api/users', require('src/users/user.controller'));
app.use('/api/matchs', require('src/matchs/match.controller'));

app.use(errorHandler);

const http = require('http').createServer(app);
const io = require('socket.io')(http);

var port = (process.env.NODE_ENV === 'production') ? 80 : 3000;

app.get('/', (req, res) => {
	res.sendFile(`${__dirname}/public/index.html`);
});

http.listen(port, () => { console.log(`Server started on port ${port}`); });

var conectados = [] //[{'id_user': 'da87s7', 'socket': {}}]

io.on("connection", (socket) => {
	var this_conn = null

	console.log("Jugador conectado");
	socket.emit('WHORU');

	socket.on('IAM', (userid) => {
		this_conn = conectados.push({ 'id_user': userid, 'socket': socket }) - 1
		console.log('Jugador identificado')
	})

	socket.on('MATCH_INIT', (match_id) => {
		games.startMatch(match_id)
			.then(v => {
				sendMultipleMessage(v.players, 'MATCH_START', {'countries': v.countries, 'currentPlayerColor': v.players[1].color, 'players': v.players})
				console.log('MATCH START ENVIADO')
			})
			.catch(e => console.log(e))
		games.getCurrentTurn(match_id)
			.then(v => {
				sendMultipleMessage(v.players, 'START_TURN', v.currentColor)
			})
			.catch(e => console.log(e))
	})

	socket.on('TRY_ATTACK', (attack) => {
		console.log('Recibido try attack, me enviaste: ' + attack)
		if( typeof attack === 'string' || attack instanceof String )
			attack = JSON.parse(attack)
		games.tryAttack(conectados[this_conn].id_user, attack)
			.then(resp => {
				sendMultipleMessage(resp.start_turn.players, 'MAP_CHANGE', resp.map_change)
				games.getCurrentTurn(resp.start_turn.id_match).then(proximo_turno => {
					sendMultipleMessage(proximo_turno.players, 'START_TURN', proximo_turno.currentColor)
				})
			})
			.catch(e => console.log(e))
	})

	socket.on("disconnect", () => {
		console.log("Jugador desconectado");
		conectados.splice(this_conn, 1)
		console.log("Jugador desconectado. Identificados solo: " + conectados.length)
	});

	/*	
	io.on('connection', (socket) => {
		console.log(socket);
		socket.emit('whoru');
		socket.on('iam', (data) => {
			games.addPlayerSocket(data.userid,socket);
		});
		socket.on('MATCH_INIT', (data) => {
			io.to(data.matchid).emit('MATCH_START', games.startMatch(data.matchid));
		});
		socket.on('disconnect', () => {
			games.removePlayerSocket(socket);
		});
	});
	*/

	/***
	 * Objeto de Conexi�n
	 * {
	 *  userid: userid
	 * }
	 * 
	 * Match Init
	 * {
	 * 	userid: userid,
	 *  matchid: matchid
	 * }
	 * 
	 * Attack
	 * {
	 * 	userid: userid,
	 *  matchid: matchid
	 * 	from: "Argentina"
	 * 	to: "Uruguay"
	 * }
	 * <--
	 * attack_result
	 * {
	 * 	dados: {
	 * 	from: [6, 3 , 2]
	 * 	to: [2, 1]
	 * 	countries: {}
	 * }
	 * 
	 * Regroup
	 * {
	 * 	userid,
	 * 	matchid,
	 * 	countries: [argentina: 3, brazil: 3, uruguay: 2] -->> Cantidad a agregar por pais
	 * }
	 * <--
	 * regroup_result
	 * {
	 * 	countries: {}
	 *  
	 * }
	 * 
	 */

});

// id_users: es el campo players del match sacado de la BD
function sendMultipleMessage(id_users, subject, body) {
	var match_players = id_users.map(function (x) {
		return x.user
	});
	conectados.forEach(conectado => {
		if (match_players.includes(conectado.id_user))
			conectado.socket.emit(subject, body)
	});
}