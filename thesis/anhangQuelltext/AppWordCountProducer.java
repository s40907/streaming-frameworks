public void start() throws IOException {
	List<String> lines = transpose.readAllLines("/" + fileToRead);
	KafkaProducer kafkaProducer = new KafkaProducer();
	Producer<Integer, byte[]> producer = kafkaProducer.GetProducer();
	int currentRetry = 1;
	int currentIndex = 0;
	while (currentRetry <= retries) {		
		while (currentIndex < lines.size()) {				
			splitLineAndSendWord(producer, lines.get(currentIndex));									
			currentIndex++;
		}
		currentIndex = 0;
		currentRetry++;
	}		
	producer.close();
}

private void splitLineAndSendWord(Producer<Integer, byte[]> producer, String line) {
	for (String word: line.split(" ")) {
		String preparedWord = convert.prepare(word);
		if (!preparedWord.equals("")) {
			sendWord(producer, preparedWord);				
		}
	}
}

private void sendWord(Producer<Integer, byte[]> producer, String word) {
	Map<String, byte[]> hashMap = new HashMap<String, byte[]>();
	hashMap.put("word", word.getBytes());
	try {
		KeyedMessage<Integer, byte[]> keyedMessage = 
		new	KeyedMessage<Integer, byte[]>(this.topicName, convert.toByteFrom(hashMap));			
		producer.send(keyedMessage);
	} catch (IOException e) {
		e.printStackTrace();
	}		
}