@Override
public Status process() throws EventDeliveryException {
	Status status = Status.READY;
	int currentIndex = 0;
	while (currentIndex < lines.size()) {				
		splitLineAndSendWord(lines.get(currentIndex));									
		currentIndex++;
	}
	return status;
}

private void splitLineAndSendWord(String line) {
	for (String word: line.split(" ")) {
		String preparedWord = convert.prepare(word);
		if (!preparedWord.equals("")) {
			sendWord(preparedWord);				
		}
	}
}

private void sendWord(String word) {
	Map<String, byte[]> hashMap = new HashMap<String, byte[]>();
	hashMap.put("word", word.getBytes());
	try {
		Event event = EventBuilder.withBody(convert.toByteFrom(hashMap));
		getChannelProcessor().processEvent(event);
	} catch (IOException e) {
		e.printStackTrace();
	}		
}