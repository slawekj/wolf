package rule.engine;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class RuleEngineTopology {
	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		TopologyBuilder builder = new TopologyBuilder();

		// Zookeeper that serves for Kafka queue
		ZkHosts zk = new ZkHosts(
				"ec2-54-183-118-187.us-west-1.compute.amazonaws.com");

		SpoutConfig configTicks = new SpoutConfig(zk, "forex",
				"/kafkastormforex2", "kafkastormforex2");
		configTicks.scheme = new SchemeAsMultiScheme(new TickScheme());

		SpoutConfig configRules = new SpoutConfig(zk, "rules",
				"/kafkastormrules2", "kafkastormrules2");
		configRules.scheme = new SchemeAsMultiScheme(new RuleScheme());

		builder.setSpout("TicksStream", new KafkaSpout(configTicks));
		builder.setSpout("RulesStream", new KafkaSpout(configRules));

		builder.setBolt("ExecutionBolt", new ExecutionBolt(), 3)
				.fieldsGrouping("TicksStream", new Fields("symbol"))
				.fieldsGrouping("RulesStream", new Fields("symbol"));

		Config conf = new Config();
		StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
	}

}
