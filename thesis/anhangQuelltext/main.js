 // ... AUSZUG ...

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