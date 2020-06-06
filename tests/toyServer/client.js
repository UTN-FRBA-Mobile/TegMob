const io = require("socket.io-client"),
      ioClient = io.connect("http://localhost:8000");

ioClient.on("Something happened", (msg) => console.log(msg));
ioClient.on("match_start", (msg) => console.log(msg));
