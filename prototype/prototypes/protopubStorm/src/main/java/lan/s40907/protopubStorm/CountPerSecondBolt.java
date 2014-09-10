package lan.s40907.protopubStorm;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class CountPerSecondBolt implements IRichBolt {
	private OutputCollector collector;
	private long currentSecond;
	private int count;
	private int normalizedSecond;
	
	public CountPerSecondBolt() {
		this.currentSecond = System.currentTimeMillis();
		this.count = 0;
		this.normalizedSecond = 0;
	}
	
	@Override
	public void cleanup() {}

	@Override
	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");
		Integer amount = tuple.getIntegerByField("amount");
		Long time = tuple.getLongByField("time");
		
		long actualSeconds = System.currentTimeMillis();
		if ((this.currentSecond + 1000) < actualSeconds) {
			
			//System.out.println(String.format("%s, %s", this.normalizedSecond, this.count));
			this.collector.emit(new Values(this.normalizedSecond, this.count));
			this.normalizedSecond = this.normalizedSecond + 1;
			this.count = 1;
			this.currentSecond = actualSeconds;
			
		}
		this.count++;
		this.collector.ack(tuple);
	}

	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("secondNumer", "count"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
