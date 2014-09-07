package lan.s40907.protocon;

import org.apache.log4j.Level;

public class MeasureLog4jLevel extends Level {

	private static final long serialVersionUID = 1L;
	public static final int MEASURE_INT = FATAL_INT - 10;
	public static final Level MEASURE = new MeasureLog4jLevel(MEASURE_INT, "MEASURE", 10);
	
	protected MeasureLog4jLevel(int level, String levelStr, int syslogEquivalent) {
		super(level, levelStr, syslogEquivalent);
	}
	
	public static Level toLever(String level) {
		if (level != null && level.toUpperCase().equals("MEASURE")) {
			return MEASURE;
		}
		return (Level)toLevel(level);
	}
	
	public static Level toLevel(int value) {
		if (value == MEASURE_INT) {
			return MEASURE;
		}
		return (Level)toLevel(value, Level.OFF);
	}
	
	public static Level toLevel(int value, Level defaultLevel) {
		if (value == MEASURE_INT) {
			return MEASURE;
		}
		return Level.toLevel(value, defaultLevel);
	}
	
	public static Level toLevel(String level, Level defaultLevel) {
		if (level != null && level.toUpperCase().equals("MEASURE")) {
			return MEASURE;
		}
		return Level.toLevel(level, defaultLevel);
	}
}
