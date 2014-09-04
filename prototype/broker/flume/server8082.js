"use strict";

var wsSocketPort = 8082;
process.title = 'protod' + wsSocketPort;
var wsServer = require('websocket').server;
var http = require('http');
var clients = [];
var server = http.createServer(function (request, response) {
 response.writeHead(404);
 response.end();
});

server.listen(wsSocketPort, function () {
 console.log((new Date()) + ' listening on port ' + wsSocketPort);
});

var ws = new wsServer({
 httpServer: server,
 autoAcceptConnections: false
});

function originIsAllowed (origin) {
 return true;
};

ws.on('connect', function (request) { });

ws.on('request', function (request) {
 if (!originIsAllowed(request.origin)) {
  request.reject();
  console.log((new Date()) + ' Rejected: ' + request.origin);
 }

 var connection = request.accept('echo-protocol', request.origin);
 var mainClient = {
  key: request.key,
  connection: connection,
  remoteAddress : request.remoteAddress,
  remoteType: ''
 };
 clients.push(mainClient);

 connection.on('message', function (message) {
  if (message.type === 'utf8') {
   var json = JSON.parse(message.utf8Data);

   clients.forEach(function (client) {
    if (client.key === request.key) {
     console.log(client.key + ' ' + request.key + ' ' + client.remoteType + ' ' + json.remoteType);
     client.remoteType = json.remoteType;
    }
   });
   var remoteTypeUiClients = clients.filter(function (client) {
    return client.remoteType === 'ui';
   });
   remoteTypeUiClients.forEach(function (client) {
    console.log('sending message to ' + client.remoteAddress + ' ' + message.utf8Data);
    client.connection.send(message.utf8Data);
   });
  } else if (message.type === 'binary') {
   console.log('Received binary length: ' + message.binaryData.length);
  }
 });

 connection.on('close', function (connection) {
  var client = {
   key: request.key,
   connection: connection
  };
  var index = clients.indexOf(client);
  for (var i in clients) {
   if (clients[i].key === request.key) {
    index = i;
   }
  };
  console.log((new Date()) + ' Disconnected client: ' + client.key + ' at index: ' + index);
  clients.splice(index, 1);
  console.log('Clients amount after disconnect: ' + clients.length);
 });
});
