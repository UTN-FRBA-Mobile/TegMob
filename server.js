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
const io = require('socket.io')(server);

const port = process.env.NODE_ENV === 'production' ? (process.env.PORT || 80) : 4000;
server.listen(port);
console.log('Recibiendo conexion socket por puerto: ' + port)

app.get('/', (req, res) => {
	res.sendFile(`${__dirname}/public/index.html`);
  });

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

/*
server.on("connection", (socket) => {

    console.log("New client connected");
		//console.log(socket);
		console.log("");
		player = new Player(7,socket)
		//match was created
    socket.on("MATCH_INIT", id =>{
			match = new Match(id, 2, 0, []);
			console.log("new match:");
			console.log(match);
			console.log("");
    });

		//player added to match
		socket.on("NEW_PLAYER", new_player => {
			player = new Player(new_player.id, new_player.socket);
			console.log("New player: " + player);
			match.players.push(player);
			console.log("Match status: ");
			console.log(match);
			console.log("");
		});

		//skynet added to match
		socket.on("NEW_AI_PLAYER", data =>{
			skynet = new Skynet(data.ai_player);

			console.log("Received new IA player on match number " + data.match_id + ":");
			console.log(skynet);
			console.log("");


			//server checks match is full and orders start command
			startMatch()
		});

		socket.on("TRY_ATTACK", data => {
			console.log("Trying attack on country "+ data.country +
			" with dices " + data.dices);
			console.log("");
			//FIGHT_ATTACK should go here

		});

		socket.on("MAP_CHANGE", data => {
			nextTurn()
		});

		socket.on("INVASION", data => {
			console.log("Trying to invade " + data.player);
			console.log("");
		});

    //timer1 = setInterval(something, 1000);

    //timer2 = setInterval(startMatch, 2000);

    // when socket disconnects, remove it from the list:
    socket.on("disconnect", () => {
      //clearInterval(timer1);
      //clearInterval(timer2);
      console.log("Client gone");
    });
});
*/
