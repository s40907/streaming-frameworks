package lan.s40907.protopubStorm;

import java.util.Comparator;
import java.util.Map;

public class DescendingComparator implements Comparator<Object> {
	Map<String, Integer> map;
	
	public DescendingComparator(Map<String, Integer> map) {
		this.map = map;
	}
	
	@Override
	public int compare(Object o1, Object o2) {
		return ((Integer) map.get(o2)).compareTo((Integer) map.get(o1));
	}
}
