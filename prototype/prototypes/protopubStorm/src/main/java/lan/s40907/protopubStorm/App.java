package lan.s40907.protopubStorm;

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
    	/*
    	builder.setSpout("readSentence", new FileReaderSpout(), 6);
    	builder.setBolt("splitSentence", new SplitSentenceBolt()).shuffleGrouping("readSentence");
    	builder.setBolt("countWords", new CountWordsBolt()).shuffleGrouping("splitSentence");
    	builder.setBolt("sendCountWords", new WebSocketBolt()).shuffleGrouping("countWords");
    	*/
    	
    	builder.setSpout("wordGenerator", new WordGeneratorSpout(50000000), 3);
    	builder.setBolt("countPerSecond", new CountPerSecondBolt(), 1).shuffleGrouping("wordGenerator");
    	builder.setBolt("printWord", new PrintBolt(), 1).shuffleGrouping("countPerSecond");
    	
    	Config conf = new Config();
    	conf.setDebug(false);
    	/*
    	conf.put(Config.TOPOLOGY_EXECUTOR_SEND_BUFFER_SIZE, 16384);
     	conf.put(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE, 16384);
     	conf.put(Config.TOPOLOGY_RECEIVER_BUFFER_SIZE, 8);
     	conf.put(Config.TOPOLOGY_TRANSFER_BUFFER_SIZE, 32);
     	*/
    	
    	if (args != null && args.length > 0) {
    		conf.setNumWorkers(3);
    		//conf.put("fileToRead", args[1]);
    		StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
    	} else {
    		conf.setMaxTaskParallelism(3);
    		conf.put("fileToRead", "shaks12.txt");
    		//conf.put("fileToRead", "small100.txt");
    		LocalCluster cluster = new LocalCluster();
    		cluster.submitTopology("wordCounter", conf, builder.createTopology() );
    		//Thread.sleep(10000);
    		//cluster.shutdown();
    	}
    }
}
