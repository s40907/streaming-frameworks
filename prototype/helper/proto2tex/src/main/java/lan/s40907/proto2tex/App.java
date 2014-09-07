package lan.s40907.proto2tex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class App {	
	private static Logger logger = Logger.getLogger(App.class.getName());
	
    public static void main( String[] args ) {
    	if (args.length < 1) {
    		logger.info("Please supply filename to traverse.");
    		return;
    	}    	
    	String filename = args[0];
    	//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	int currentSecond = 0;    	
    	List<Measure> measures = getMeasures(filename);
    	long currentMeasureTime = measures.get(0).sendTime;
    	int measureCount = 0;
    	logger.debug(String.format("Time, Mps", currentMeasureTime, measureCount));
    	for(Measure measure : measures) {    		
    		if ((currentMeasureTime + 1000) < measure.sendTime) {    			
    			//logger.debug(String.format("%s, %s", dateFormat.format(currentMeasureTime), measureCount));
    			logger.debug(String.format("%s, %s", currentSecond, measureCount));
    			measureCount = 1;
    			currentSecond++;
    			currentMeasureTime = measure.sendTime;
    		}
    		measureCount++;
    	}    	
    }
    
    private static List<Measure> getMeasures(String filename) {
    	try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
			String readLine = bufferedReader.readLine();
			List<Measure> measures = new ArrayList<Measure>();
			
			while (readLine != null) {
				Measure parsedMeasure = parse(readLine);							
				measures.add(parsedMeasure);
				readLine = bufferedReader.readLine();
			}
			return measures;
			
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
    }
    
    private static Measure parse(String measureString) {
    	String[] split = measureString.split(",");
    	Measure measure = new Measure();    	
		measure.sendTime = Long.parseLong(trim(split[0]));
		measure.recieveTime = Long.parseLong(trim(split[1]));
		measure.word = trim(split[2]);
		measure.count = Long.parseLong(trim(split[3]));    	
    	return measure;
    }
    
    private static String trim(String value) {
    	return value.trim();
    }
}
