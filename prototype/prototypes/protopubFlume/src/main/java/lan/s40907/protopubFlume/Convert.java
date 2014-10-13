package lan.s40907.protopubFlume;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class Convert {

	public <T> byte[] toByteFrom(Map<String, byte[]> hashMap) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(hashMap);
		return byteArrayOutputStream.toByteArray();
	}
	
	public <T> Map<String, byte[]> toMapFrom(byte[] byteArray) throws ClassNotFoundException, IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		return (Map<String, byte[]>) objectInputStream.readObject();
	}
	
	public String prepare(String word) {
		return trim(word).toLowerCase();
	}
	
	public String trim(String word) {		
		return word.replaceAll("[^a-zA-Z]+", "").trim();
	}
}
