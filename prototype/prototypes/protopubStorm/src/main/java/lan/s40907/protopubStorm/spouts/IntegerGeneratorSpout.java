package lan.s40907.protopubStorm.spouts;

import java.util.Arrays;
import java.util.Map;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class IntegerGeneratorSpout implements IRichSpout {
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private byte[] staticPayload ;
	
	public IntegerGeneratorSpout() {
		staticPayload = new byte[100];
		Arrays.fill(staticPayload, (byte) 0);
	}
	
	@Override
	public void ack(Object arg0) {}

	@Override
	public void activate() {}

	@Override
	public void close() {}

	@Override
	public void deactivate() {}

	@Override
	public void fail(Object object) {}

	@Override
	public void nextTuple() {		
		this.collector.emit(new Values(new String(staticPayload)));
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}