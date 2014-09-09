package lan.s40907.protopubStorm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONObject;

import lan.s40907.websocketclient.IWebSocketClient;
import lan.s40907.websocketclient.WebSocketClientFactory;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WebSocketBolt implements IRichBolt {
	private OutputCollector collector;

	@Override
	public void cleanup() {}

	@Override
	public void execute(Tuple tuple) {
		
		// testing
		String word = tuple.getString(0);
		word = word.replace("{", "");
		word= word.replace("}", "");
		String[] kvPair = word.split("=");
		
		try {
			IWebSocketClient client = WebSocketClientFactory.getInstance();
	   		JSONObject json = new JSONObject();
	       	json.put("remoteType", "backend");
	       	json.put("sendTime", System.currentTimeMillis());
	       		 
	       	JSONObject wordCount = new JSONObject();
	       	wordCount.put("word", kvPair[0]);
	       	wordCount.put("count", kvPair[1]);
	       	json.put("wordCount", wordCount);
	   		client.send(json.toJSONString());
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   	}
		this.collector.ack(tuple);
	}

	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
