const io = require("socket.io"),
      server = io.listen(8000);

var client, timer1, timer2;
var match_id = 0
var countries_colours = {
  "countries": {
        "brazil": {
           "owner": "cyan",
           "armies": "5"
        },
        "colombia": {
           "owner": "green",
           "armies": "1"
        },
        "chile": {
           "owner": "black",
           "armies": "1"
        },
        "peru": {
           "owner": "green",
           "armies": "3"
        },
        "argentina": {
           "owner": "cyan",
           "armies": "4"
        },
        "uruguay": {
           "owner": "red",
           "armies": "6"
        },
        "egypt": {
           "owner": "cyan",
           "armies": "2"
        },
        "ethiopia": {
           "owner": "black",
           "armies": "1"
        },
        "zaire": {
           "owner": "black",
           "armies": "3"
        },
        "madagascar": {
           "owner": "yellow",
           "armies": "1"
        },
        "southafrica": {
           "owner": "red",
           "armies": "1"
        },
        "sahara": {
           "owner": "green",
           "armies": "4"
        }
     }
}



var players_colours = {
  "Bob":"green",
  "Karl":"yellow",
  "Skynet":"black",
  "MrRoboto":"red",
  "Juli":"magenta",
  "Anonymus":"cyan"
}

// event fired every time a new client connects:
server.on("connection", (socket) => {

    client = socket

    console.log("New client connected");

    socket.on("match_init", id =>{
      console.log("new match id: "+id);
    });


    timer1 = setInterval(something, 1000);

    timer2 = setInterval(startMatch, 2000);

    // when socket disconnects, remove it from the list:
    socket.on("disconnect", () => {
      clearInterval(timer1);
      clearInterval(timer2);
      console.log("Client gone");
    });
});

function startMatch(){
  client.emit("match_start", {
    countries: countries_colours,
    players: players_colours
  });
  console.log("emiting match initialization to client");
}

function something(){
  client.emit("Something happened", { msg : "something is going on" });
  console.log("emiting to client");
}
