package lan.s40907.protopubStorm.spouts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import lan.s40907.protopubStorm.bolts.PrintBolt;

import org.apache.log4j.Logger;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordGeneratorSpout implements IRichSpout {
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private AtomicLong index;
	private List<String> textList;
	private int retries;
	private int retryCounter;
	
	public WordGeneratorSpout(int retries) {
		this.retries = retries;
		this.retryCounter = 0;
		this.index = new AtomicLong(0);
		this.textList = new ArrayList<String>();
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
		if (this.textList.size() > (int)this.index.get() ) {			
			String line = this.textList.get((int)this.index.getAndIncrement());
			for (String word: line.split(" ")) {
				String preparedWord = prepare(word);
				if (!preparedWord.equals("")) {					
					this.collector.emit(new Values(preparedWord));
				}
			}			
		} else {
			if (this.retryCounter < this.retries) {
				this.index.set(0);				
				this.retryCounter++;
			}			
		}
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		this.textList = readAllLines("/" + conf.get("fileToRead").toString());
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
	private List<String> readAllLines(String fileToRead) {
		List<String> list = new ArrayList<String>();
		InputStream inputStream = getClass().getResourceAsStream(fileToRead);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			while (bufferedReader != null && bufferedReader.ready()) {
				String readLine = bufferedReader.readLine();
				if (!readLine.trim().isEmpty()) {
					list.add(readLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private String prepare(String word) {
		return trim(word).toLowerCase();
	}
	
	private String trim(String word) {		
		return word.replaceAll("[^a-zA-Z]+", "").trim();
	}
}
