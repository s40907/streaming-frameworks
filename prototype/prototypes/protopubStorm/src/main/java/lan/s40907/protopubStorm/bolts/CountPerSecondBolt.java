package lan.s40907.protopubStorm.bolts;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import lan.s40907.protopubStorm.helper.DescendingComparator;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Time;

import com.sun.management.OperatingSystemMXBean;

public class CountPerSecondBolt implements IRichBolt {
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;	
	private int countPerSecond;
	private int normalizedSecond;
	private int currentTimeSecondsInt;
	private HashMap<String, Integer> counter;
	
	public CountPerSecondBolt() {		
		this.countPerSecond = 0;
		this.normalizedSecond = 0;
		this.currentTimeSecondsInt = Time.currentTimeSecs();
		this.counter = new HashMap<String, Integer>();
	}
	
	@Override
	public void cleanup() {}

	@Override
	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");		
		this.collector.ack(tuple);
		
		if (this.counter.containsKey(word)) {
			Integer amount = counter.get(word);
			counter.put(word, ++amount);
		} else {
			counter.put(word, 1);
		}
		
		if ((this.currentTimeSecondsInt + 1) < Time.currentTimeSecs()) {
			String takeFirstHeighest = takeFirstHighest(5);			
			OperatingSystemMXBean operatingSystem = (com.sun.management.OperatingSystemMXBean)
					ManagementFactory.getOperatingSystemMXBean();
			this.collector.emit(tuple, new Values(
					this.normalizedSecond, 
					this.countPerSecond,
					operatingSystem.getProcessCpuLoad(), 
					operatingSystem.getSystemCpuLoad(),
					takeFirstHeighest));
			this.normalizedSecond = this.normalizedSecond + 1;
			this.countPerSecond = 1;			
			this.currentTimeSecondsInt = Time.currentTimeSecs();
		}
		this.countPerSecond++;
	}

	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("timeNumber", "countPerSecond", "processCpu", "systemCpu", "wordCount"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
	private String takeFirstHighest(int amount) {
		DescendingComparator descendingComparator = new DescendingComparator(counter);
		TreeMap<String,Integer> treeMap = new TreeMap<String, Integer>(descendingComparator);
		treeMap.putAll(counter);
				
		Iterator<Entry<String, Integer>> iterator = treeMap.entrySet().iterator();
		StringBuilder toJsonStringBuilder = new StringBuilder(); 
		int take = amount;
		int takeIndex = 0;
		toJsonStringBuilder.append("\"wordCount\": [");
		while(iterator.hasNext() && takeIndex < take) {
			Entry<String, Integer> entry = iterator.next();
			String item = String.format("{\"word\": \"%s\", \"count\": \"%s\"}", entry.getKey(), entry.getValue());			
			toJsonStringBuilder.append(item);			
			if (iterator.hasNext() && takeIndex != take - 1) {
				toJsonStringBuilder.append(",");
			}
			takeIndex++;
		}
		toJsonStringBuilder.append("]");
		return toJsonStringBuilder.toString();
	}
}
