package lan.s40907.protopubStorm;

import lan.s40907.protopubStorm.bolts.CountPerSecondBolt;
import lan.s40907.protopubStorm.bolts.PrintBolt;
import lan.s40907.protopubStorm.spouts.IntegerGeneratorSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class AppIntegerCounter {
	 
    public static void main( String[] args ) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
    	TopologyBuilder builder = new TopologyBuilder();
    	builder.setSpout("integerGenerator", new IntegerGeneratorSpout(), 8);
    	builder.setBolt("countPerSecond", new CountPerSecondBolt(), 1).shuffleGrouping("integerGenerator");
    	builder.setBolt("printWord", new PrintBolt(), 1).shuffleGrouping("countPerSecond");
    	
    	Config conf = new Config();
    	conf.setDebug(false);

    	if (args != null && args.length > 0) {
    		conf.setNumWorkers(1);
    		StormSubmitter.submitTopology("integerCounter", conf, builder.createTopology());
    	} else {
    		conf.setNumWorkers(1);
    		LocalCluster cluster = new LocalCluster();
    		cluster.submitTopology("integerCounter", conf, builder.createTopology() );
    		//Thread.sleep(10000);
    		//cluster.shutdown();
    	}
    }
}
