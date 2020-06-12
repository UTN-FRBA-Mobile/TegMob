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

		//player added to match
		// socket.on("NEW_PLAYER", new_player => {
		// 	player = new Player(new_player.id, new_player.socket);
		// 	console.log("New player: " + player);
		// 	match.players.push(player);
		// 	console.log("Match status: ");
		// 	console.log(match);
		// 	console.log("");
		// });

		//skynet added to match
		// socket.on("NEW_AI_PLAYER", data =>{
		// 	skynet = new Skynet(data.ai_player);

		// 	console.log("Received new IA player on match number " + data.match_id + ":");
		// 	console.log(skynet);
		// 	console.log("");


			//server checks match is full and orders start command
		// 	startMatch()
		// });

		// socket.on("TRY_ATTACK", data => {
		// 	console.log("Trying attack on country "+ data.country +
		// 	" with dices " + data.dices);
		// 	console.log("");
		// 	//FIGHT_ATTACK should go here

		// });

		// socket.on("MAP_CHANGE", data => {
		// 	nextTurn()
		// });

		// socket.on("INVASION", data => {
		// 	console.log("Trying to invade " + data.player);
		// 	console.log("");
		// });

    // when socket disconnects, remove it from the list:
    socket.on("disconnect", () => {
		console.log("Client desconectado");
		// Saco el socket de la lista de jugadores conectados
		jugadores.splice(index_jugador, 1);
		// SI NO HAY MAS JUGADORES RENOVAR LISTA DE COLORES!!!
		if(jugadores.length < 1)
			colores = paises.getNColoresPosibles();
    });
});