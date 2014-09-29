package lan.s40907.protopubS4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class Convert {

	public <T> byte[] toByteFrom(Map<T, T> hashMap) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(hashMap);
		return byteArrayOutputStream.toByteArray();
	}
	
	public <T> Map<T, T> toMapFrom(byte[] byteArray) throws ClassNotFoundException, IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		return (Map<T, T>) objectInputStream.readObject();
	}
	
	public String prepare(String word) {
		return trim(word).toLowerCase();
	}
	
	public String trim(String word) {		
		return word.replaceAll("[^a-zA-Z]+", "").trim();
	}
}
