package lan.s40907.protopubStorm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class CountWordsBolt implements IRichBolt {
	private HashMap<String, Integer> counter;
	private OutputCollector collector;

	@Override
	public void cleanup() {}

	@Override
	public void execute(Tuple tupel) {
		String word = tupel.getString(0);
		if (this.counter.containsKey(word)) {
			Integer amount = counter.get(word);
			counter.put(word, ++amount);
		} else {
			counter.put(word, 1);
		}
		DescendingComparator descendingComparator = new DescendingComparator(counter);
		TreeMap<String,Integer> treeMap = new TreeMap<String, Integer>(descendingComparator);
		treeMap.putAll(counter);
		
		HashMap<String,Integer> hashMap = new HashMap<String, Integer>();		
		Iterator<Entry<String, Integer>> iterator = treeMap.entrySet().iterator();
		int take = 1;
		int takeIndex = 0;
		while(iterator.hasNext() && takeIndex < take) {
			Entry<String, Integer> entry = iterator.next();
			hashMap.put(entry.getKey(), entry.getValue());
			takeIndex++;
		}
		this.collector.emit(new Values(hashMap.toString()));
		this.collector.ack(tupel);
	}

	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.counter = new HashMap<String, Integer>();
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("words"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
