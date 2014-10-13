package lan.s40907.protopubStorm.bolts;

import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class PrintBolt implements IRichBolt {	
	private static final long serialVersionUID = 1L;
	private static Logger logger;
	private OutputCollector collector;
	
	@Override
	public void cleanup() {}

	@Override
	public void execute(Tuple tuple) {		
		Integer timeNumber = tuple.getIntegerByField("timeNumber");
		Integer countPerSecond = tuple.getIntegerByField("countPerSecond");
		String wordCount = tuple.getStringByField("wordCount");
		Double systemCpu = tuple.getDoubleByField("systemCpu");
		this.collector.ack(tuple);
		this.logger.info(String.format("%s, %s, %s", timeNumber, countPerSecond, systemCpu));
		this.collector.emit(tuple, new Values(countPerSecond, wordCount));
	}	

	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.logger = Logger.getLogger(PrintBolt.class);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("countPerSecond", "wordCount"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}