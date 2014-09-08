package lan.s40907.protopubStorm;

import lan.s40907.websocketclient.IWebSocketClient;
import lan.s40907.websocketclient.WebSocketClientFactory;

import org.json.simple.JSONObject;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class App 
{
    public static void main( String[] args ) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
    	TopologyBuilder builder = new TopologyBuilder();    	
    	builder.setSpout("readSentence", new FileReaderSpout(), 1);
    	builder.setBolt("splitSentence", new SplitSentenceBolt()).shuffleGrouping("readSentence");
    	builder.setBolt("countWords", new CountWordsBolt()).shuffleGrouping("splitSentence");
    	builder.setBolt("printCountWords", new PrintBolt()).shuffleGrouping("countWords");
    	builder.setBolt("sendCountWords", new WebSocketBolt()).shuffleGrouping("countWords");
    	
    	Config conf = new Config();
    	conf.setDebug(true);    	
    	conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
    	
    	if (args != null && args.length > 0) {
    		conf.setNumWorkers(3);
    		conf.put("fileToRead", args[1]);
    		StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
    	} else {
    		//conf.setMaxTaskParallelism(3);
    		conf.put("fileToRead", "shaks12.txt");
    		//conf.put("fileToRead", "small100.txt");
    		LocalCluster cluster = new LocalCluster();
    		cluster.submitTopology("wordCounter", conf, builder.createTopology() );
    		//Thread.sleep(10000);
    		//cluster.shutdown();
    	}
    }
}
