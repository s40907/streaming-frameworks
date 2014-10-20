var WebSocketManager = function (config) {
 'use strict';
 var hasWebSocketSupport = true;
 var _connection = '';
 var _self = this;
 var _logger = '';
 
 var logger = ''; //log4javascript.getLogger('UI.webSocketManager.' + configData.name);

 function init () {
  _logger = log4javascript.getLogger('UI.webSocketManager.' + config.name);
  _logger.info('{"message": "Initialization"}'); 
  if (!window.WebSocket) {
   logger.error('{"message": "Browser does not support WebSockets.", "userAgent": "' + navigator.userAgent + '", "stackTrace": ""}' );
   hasWebSocketSupport = false;
  }
 }

 this.start = function () {
  init();
  if (!hasWebSocketSupport) {
   logger.error('{"message": "Websocket not supported."}');
   return;
  }
 
  _logger.debug('{"message": "establishing connection.", "userAgent": "' + navigator.userAgent + '"}');
  _connection = new WebSocket('ws://' + config.ipAddress + ':' + config.port, 'echo-protocol');

  _connection.onopen = function () {
   _logger.info('{"message": "connection opened.", "userAgent": "' + navigator.userAgent + '"}');
   var message = {
    remoteType: 'ui',
    message: 'hello'
   };
   _connection.send(JSON.stringify(message));
  };

  _connection.onerror = function (error) {
   _logger.error('{"message": "Connection broken or server (' + config.ipAddress + ':' + config.port +  ') down.", "userAgent": "' + navigator.userAgent + '", "stackTrace": ' + JSON.stringify(error) + '}');
  };

  _connection.onmessage = function (message) {
   try {
    var json = JSON.parse(message.data);
    if (json.message === 'hello') {
     _logger.debug('{"message": "hello JSON message received", "userAgent": "' + navigator.userAgent + '", "stackTrace": ""}');
     return;
    }
    _logger.trace('{"message": "bubbling event to ' + config.name + '"}');
    $('#eventNode').data('eventsData', message.data).trigger('messageIncome' + config.name);
   } catch (error) {
    _logger.error('{"message": " WS01 ' + error.message + '", "userAgent": "' + navigator.userAgent + '", "stackTrace": ""}');
    return;
   }
  }
 };
}