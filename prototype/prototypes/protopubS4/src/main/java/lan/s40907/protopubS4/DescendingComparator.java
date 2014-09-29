package lan.s40907.protopubS4;

import java.util.Comparator;
import java.util.Map;

public class DescendingComparator implements Comparator<String> {
	Map<String, Integer> map;
	
	public DescendingComparator(Map<String, Integer> map) {
		this.map = map;
	}
	
	@Override
	public int compare(String key1, String key2) {
		if (map.get(key1) >= map.get(key2)) {
			return -1;
		} else {
			return 1;
		}
	}
}
