package lan.s40907.protopubStorm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class FileReaderSpout implements IRichSpout {
	private SpoutOutputCollector collector;
	private BufferedReader bufferedReader;

	@Override
	public void ack(Object arg0) {}

	@Override
	public void activate() {}

	@Override
	public void close() {

	}

	@Override
	public void deactivate() {}

	@Override
	public void fail(Object object) {
		System.err.println("FileReader fail with: " + object.toString());
	}

	@Override
	public void nextTuple() {		
		try {
			if (this.bufferedReader != null && this.bufferedReader.ready()) {				
				String readLine = this.bufferedReader.readLine();
				if (readLine != null) {
					this.collector.emit(new Values(readLine));
				} else {
					System.out.println("FILE END");
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		String fileToReadPath = "/" + conf.get("fileToRead").toString();
		this.collector = collector;
		InputStream inputStream = getClass().getResourceAsStream(fileToReadPath);		
		bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
