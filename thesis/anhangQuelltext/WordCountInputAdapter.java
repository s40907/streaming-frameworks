private void sendWord(String word) {				
	Event event = new Event();
	event.put("word", String.class, word);
	if (getRemoteStream() != null) {
		getRemoteStream().put(event);
	} else
	{
		logger.error("Cannot get remotestream.");
	}
}