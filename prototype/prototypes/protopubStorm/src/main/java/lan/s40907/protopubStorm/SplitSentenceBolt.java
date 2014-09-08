package lan.s40907.protopubStorm;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitSentenceBolt implements IRichBolt {
	private OutputCollector collector;

	@Override
	public void cleanup() {}

	@Override
	public void execute(Tuple tupel) {
		String sentence = tupel.getString(0);
		for (String word: sentence.split(" ")) {
			if (IsNotEmpty(trim(word))) {
				this.collector.emit(new Values(prepare(word)));
			}
		}
		this.collector.ack(tupel);
	}

	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
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
	
	private String prepare(String word) {
		return trim(word).toLowerCase();
	}
	
	private String trim(String word) {
		return word.trim();
	}
	
	private boolean IsNotEmpty(String word) {
		return !word.isEmpty();
	}
}
