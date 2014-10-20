var MainComponent = (function () {
 "use strict";
 var logger = log4javascript.getLogger("UI.main");
 
 function start () {
  logger.debug('{"message": "starting websockets"}');
  startApacheStorm();
  startApacheKafka();
  startApacheFlume();
  startApacheS4();
 } 

 function startApacheStorm () { 
  var stormWebSocketManager = new WebSocketManager(webSocketConfiguration.storm);
  var stormGraph = new Graph(graphConfiguration.uiSetup, graphConfiguration.storm.chartIdentity);

  stormWebSocketManager.start();
  stormGraph.start();

  $('#eventNode').on('messageIncomeStorm', function (event) {
   var json = JSON.parse($(this).data('eventsData'));
   stormGraph.update(json.wordCount, json.countPerSecond);
  });
 };

 function startApacheKafka () {
  var kafkaWebSocketManager = new WebSocketManager(webSocketConfiguration.kafka);
  var kafkaGraph = new Graph(graphConfiguration.uiSetup, graphConfiguration.kafka.chartIdentity);

  kafkaWebSocketManager.start();
  kafkaGraph.start();

  $('#eventNode').on('messageIncomeKafka', function (event) {
   var json = JSON.parse($(this).data('eventsData'));
   kafkaGraph.update(json.wordCount, json.countPerSecond);
  });

 }

 function startApacheFlume () {
  var flumeWebSocketManager = new WebSocketManager(webSocketConfiguration.flume);
  var flumeGraph = new Graph(graphConfiguration.uiSetup, graphConfiguration.flume.chartIdentity);

  flumeWebSocketManager.start();
  flumeGraph.start();

  $('#eventNode').on('messageIncomeFlume', function (event) {
   var json = JSON.parse($(this).data('eventsData'));
   flumeGraph.update(json.wordCount, json.countPerSecond);
  });

 }

 function startApacheS4 () {
  var s4WebSocketManager = new WebSocketManager(webSocketConfiguration.ssss);
  var s4Graph = new Graph(graphConfiguration.uiSetup, graphConfiguration.ssss.chartIdentity);

  s4WebSocketManager.start();
  s4Graph.start();

  $('#eventNode').on('messageIncomeSsss', function (event) {
   var json = JSON.parse($(this).data('eventsData'));
   s4Graph.update(json.wordCount, json.countPerSecond);
  });

 }

 return {
  Execute: function () {
   start();
  }
 };
})();

MainComponent.Execute();
