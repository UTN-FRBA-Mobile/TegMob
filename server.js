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

// La catedra dijo q no separemos jugadores por sala, todos juntos como si fuera un solo juego
var paises = require('./funciones/paises')
var jugadores = []
var juego = {'countries': {}, 'currentPlayer': '', 'currentRound': 'attack'}
var colores = paises.getNColoresPosibles();

socket_server.on("connection", (socket) => {
	
    console.log("Nuevo jugador conectado");
	var index_jugador = jugadores.push({'color': colores.shift(),'socket': socket}) - 1
	// si se conecta luego de iniciada la partida se le tiene q pasar el juego

	//Owner inicia la partida
	//Devolvemos mapa inicial
    socket.on("MATCH_INIT", id =>{
		console.log("Nuevo juego:" + id);
		console.log("Usuarios en la partida: " + jugadores.length)
		// seteo primer turno
		juego.currentPlayer = jugadores[0].color
		// genero mapa inicial
		juego.countries = paises.getMapaInicial( jugadores.length )
		// Informo a los conectados el inicio del juego
		jugadores.forEach(jugador => {
			jugador.socket.emit("MATCH_START", juego );
		});
    });

    // when socket disconnects, remove it from the list:
    socket.on("disconnect", () => {
		console.log("Client desconectado");
		// Saco el socket de la lista de jugadores conectados
		jugadores.splice(index_jugador, 1);
		// SI NO HAY MAS JUGADORES RENOVAR LISTA DE COLORES!!!
		if(jugadores.length < 1)
			colores = paises.getNColoresPosibles();
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