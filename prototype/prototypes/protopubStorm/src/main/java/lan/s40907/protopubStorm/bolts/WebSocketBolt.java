package lan.s40907.protopubStorm.bolts;

import java.util.Map;
import lan.s40907.websocketclient.IWebSocketClient;
import lan.s40907.websocketclient.WebSocketClientFactory;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WebSocketBolt implements IRichBolt {
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;

	@Override
	public void cleanup() {}

	@Override
	public void execute(Tuple tuple) {
		Integer countPerSecond = tuple.getIntegerByField("countPerSecond");
		String wordCount = tuple.getStringByField("wordCount");
		this.collector.ack(tuple);
		String json = String.format("{\"remoteType\": \"backend\", "
				+ "\"countPerSecond\": \"%s\", %s}", countPerSecond, wordCount);
		
		try {
			IWebSocketClient client = WebSocketClientFactory.getInstance();
	   		client.send(json);
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   	}		
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