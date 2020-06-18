require('rootpath')();
require('dotenv').config();
const app = require('express')();
const cors = require('cors');
const bodyParser = require('body-parser');
const jwt = require('_helpers/jwt');
const errorHandler = require('_helpers/error-handler');
const games = require('game/service');

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(cors());
//app.use(jwt());
app.use(errorHandler);

app.use('/users', require('./users/user.controller'));
app.use('/match', require('./match/match.controller'));

const server = require('http').createServer(app);
const io = require('socket.io');

const port = process.env.NODE_ENV === 'production' ? (process.env.PORT || 80) : 4000;
var socket_server = io.listen(port);
console.log('Recibiendo conexion socket por puerto: ' + port)

app.get('/', (req, res) => {
	res.sendFile(`${__dirname}/public/index.html`);
  });

var conectados = []

socket_server.on("connection", (socket) => {
	var this_conn = null

	console.log("Nuevo jugador conectado");
	// Ya no se crea un juego nuevo, ya esta creado desde antes
	// var index_jugador = jugadores.push({'color': colores.shift(),'socket': socket}) - 1
	// si se conecta luego de iniciada la partida se le tiene q pasar el juego

	// El socket tiene que identificarse, asociarse a un usuario
	socket.on('IAM', (data) => {
		this_conn =	conectados.push({'id_user': data.userid, 'socket': socket}) - 1
	})

	//Owner inicia la partida
	//Devolvemos mapa inicial
	socket.on('MATCH_INIT', (data) => {
		games.startMatch(data.match_id)
			.then(v =>{
				sendMultipleMessage(v.players, 'MATCH_START', {'countries': v.countries})
			})
			.catch(e => console.log(e))
	})


    // socket.on("MATCH_INIT", id =>{
	// 	console.log("Nuevo juego:" + id);
	// 	console.log("Usuarios en la partida: " + jugadores.length)
	// 	// seteo primer turno
	// 	juego.currentPlayer = jugadores[0].color
	// 	// genero mapa inicial
	// 	juego.countries = paises.getMapaInicial( jugadores.length )
	// 	// Informo a los conectados el inicio del juego
	// 	jugadores.forEach(jugador => {
	// 		jugador.socket.emit("MATCH_START", juego );
	// 	});
    // });

    // when socket disconnects, remove it from the list:
    socket.on("disconnect", () => {
		console.log("Client desconectado");
		conectados.splice(this_conn, 1)
		//games.removePlayerSocket(conectados); // NO FUNCIONAAAAAAAAAAAAAAAAAA
		// Saco el socket de la lista de jugadores conectados
		//jugadores.splice(index_jugador, 1);
		// SI NO HAY MAS JUGADORES RENOVAR LISTA DE COLORES!!!
		//if(jugadores.length < 1)
		//	colores = paises.getNColoresPosibles();
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
	 * Objeto de Conexión
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
function sendMultipleMessage( id_users, subject, body){
	var match_players = id_users.map(function(x){
		return x.user
	});
	conectados.forEach(conectado => {
		if(match_players.includes(conectado.id_user))
			conectado.socket.emit(subject, body)
	});
}