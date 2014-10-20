TopologyBuilder builder = new TopologyBuilder();
builder.setSpout("wordGenerator", new WordGeneratorSpout(1000), 8);    	
builder.setBolt("countPerSecond", new CountPerSecondBolt(), 1).fieldsGrouping("wordGenerator", new Fields("word"));    	
builder.setBolt("printWord", new PrintBolt(), 1).shuffleGrouping("countPerSecond");
builder.setBolt("sendCountWords", new WebSocketBolt()).shuffleGrouping("printWord");

Config conf = new Config();
conf.setDebug(false);

[...]

conf.setNumWorkers(1);
conf.put("fileToRead", "shaks12.txt");
LocalCluster cluster = new LocalCluster();
cluster.submitTopology("wordCounter", conf, builder.createTopology() );