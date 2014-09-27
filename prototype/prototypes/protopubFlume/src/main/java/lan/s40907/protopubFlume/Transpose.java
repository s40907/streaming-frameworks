package lan.s40907.protopubFlume;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Transpose {
	
	public String takeFirstHighest(int amount, HashMap<String, Integer> counter) {
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
	
	public List<String> readAllLines(String fileToRead) {
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
}
