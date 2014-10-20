var graphConfiguration = {
 "uiSetup": {
  "width": 545, 
  "height": 200, 
  "margin": { "top": 20, "left": 100, "bottom": 30, "right": 20},
  "colorRange": ["green", "yellow"]
 },
 "storm": { "chartIdentity": "#chartStorm" },
 "kafka": { "chartIdentity": "#chartKafka" },
 "flume": { "chartIdentity": "#chartFlume" },
 "ssss": { "chartIdentity": "#chartSsss" }
};


var webSocketConfiguration = {
 "storm": { "name": "Storm", "ipAddress": "192.168.1.60", "port": "8080" },
 "kafka": { "name": "Kafka", "ipAddress": "192.168.1.60", "port": "8081" },
 "flume": { "name": "Flume", "ipAddress": "192.168.1.60", "port": "8082" },
 "ssss": { "name": "Ssss", "ipAddress": "192.168.1.60", "port": "8083" }
};

var PageBlockAppender = (function () {
 this.blockElementName = null;
 var ctor = function (blockElementName) {
  this.blockElementName = blockElementName;
 };

 ctor.prototype = new log4javascript.Appender();
 ctor.prototype.layout = new log4javascript.NullLayout();
 ctor.prototype.threshold = log4javascript.Level.DEBUG;
 ctor.prototype.append = function (loggingEvent) {
  var appender = this;

  var getFormattedMessage = function () {
   var layout = appender.getLayout();
   var logMessage = JSON.parse(loggingEvent.messages[0]);
   loggingEvent.messages[0] = logMessage.message;
   var formattedMessage = layout.format(loggingEvent);
   if (layout.ignoresThrowable() && loggingEvent.exception) {
    formattedMessage += loggingEvent.getThrowableStrRep();
   }
   /*if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)) {
    formattedMessage += ' Only optimized for Firefox > Version 24';
   }*/
   return formattedMessage;
  };

  $(this.blockElementName).prepend($('<p>', {text: getFormattedMessage() } ));
 };
 
 return ctor;
})();


var ConfigurationComponent = (function () {
 "use strict";
 function init () {
  var browserConsoleAppender = new log4javascript.BrowserConsoleAppender();
  browserConsoleAppender.setLayout(new log4javascript.PatternLayout("[%d{yyyy-MM-dd HH:mm:ss,SSS}] %c - %m"));
  browserConsoleAppender.setThreshold(log4javascript.Level.DEBUG);

  var blockAppender = new PageBlockAppender('[name="logContent"]');
  blockAppender.setLayout(new log4javascript.PatternLayout("[%d{yyyy-MM-dd HH:mm:ss,SSS}] %c - %m"));
  blockAppender.setThreshold(log4javascript.Level.DEBUG);

  log4javascript.getRootLogger().addAppender(blockAppender);
  log4javascript.getLogger("UI").setLevel(log4javascript.Level.TRACE);
 }

 return {
  Start: function () {
   init();
   var logger = log4javascript.getLogger("UI.configuration");
   logger.info('{"message": "Initialized", "userAgent": "' + navigator.userAgent + '", "stackTrace": ""}'); 
  }
 };
})();

ConfigurationComponent.Start();
