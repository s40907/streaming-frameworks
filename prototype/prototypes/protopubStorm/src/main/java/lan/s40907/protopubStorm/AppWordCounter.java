package lan.s40907.protopubStorm;

import lan.s40907.protopubStorm.bolts.CountPerSecondBolt;
import lan.s40907.protopubStorm.bolts.PrintBolt;
import lan.s40907.protopubStorm.bolts.WebSocketBolt;
import lan.s40907.protopubStorm.spouts.WordGeneratorSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class AppWordCounter {
    public static void main( String[] args ) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
    	TopologyBuilder builder = new TopologyBuilder();
    	
    	builder.setSpout("wordGenerator", new WordGeneratorSpout(1000), 8);    	
    	builder.setBolt("countPerSecond", new CountPerSecondBolt(), 1).fieldsGrouping("wordGenerator", new Fields("word"));    	
    	builder.setBolt("printWord", new PrintBolt(), 1).shuffleGrouping("countPerSecond");
    	builder.setBolt("sendCountWords", new WebSocketBolt()).shuffleGrouping("printWord");
    	
    	Config conf = new Config();
    	conf.setDebug(false);
    	
    	if (args != null && args.length > 0) {
    		conf.setNumWorkers(1);
    		conf.put("fileToRead", "shaks12.txt");    		
    		StormSubmitter.submitTopology("wordCounter", conf, builder.createTopology());
    	} else {
    		conf.setNumWorkers(1);
    		conf.put("fileToRead", "shaks12.txt");
    		LocalCluster cluster = new LocalCluster();
    		cluster.submitTopology("wordCounter", conf, builder.createTopology() );
    		//Thread.sleep(10000);
    		//cluster.shutdown();
    	}
    }
}
