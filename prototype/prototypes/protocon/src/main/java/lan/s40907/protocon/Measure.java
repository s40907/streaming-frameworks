package lan.s40907.protocon;

public class Measure {
	public long sendTime;
	public long recieveTime;
	public String word;
	public long count;
	
	@Override
	public String toString() {
		return String.format("%s, %s, %s, %s", sendTime, recieveTime, word.trim(), count);		
	}
}
