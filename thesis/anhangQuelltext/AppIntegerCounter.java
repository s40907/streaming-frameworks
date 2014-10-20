TopologyBuilder builder = new TopologyBuilder();
builder.setSpout("integerGenerator", new IntegerGeneratorSpout(), 8);
builder.setBolt("countPerSecond", new CountPerSecondBolt(), 1).shuffleGrouping("integerGenerator");
builder.setBolt("printWord", new PrintBolt(), 1).shuffleGrouping("countPerSecond");