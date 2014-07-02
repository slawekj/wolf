package rule.engine;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class RuleEngineTopology {
	public static void main(String[] args) throws Exception {

		TopologyBuilder builder = new TopologyBuilder();

		ZkHosts zk = new ZkHosts(
				"ec2-54-183-118-187.us-west-1.compute.amazonaws.com");

		SpoutConfig configTicks = new SpoutConfig(zk, "forex",
				"/kafkastormforex", "kafkastormforex");
		SpoutConfig configRules = new SpoutConfig(zk, "rules",
				"/kafkastormrules", "kafkastormrules");

		builder.setSpout("TicksStream", new KafkaSpout(configTicks));
		builder.setSpout("RulesStream", new KafkaSpout(configRules));

		builder.setBolt("TicksFormatter", new TicksFormatterBolt(), 1)
				.shuffleGrouping("TicksStream");
		builder.setBolt("RulesFormatter", new RulesFormatterBolt(), 1)
				.shuffleGrouping("RulesStream");

		builder.setBolt("ExecutionBolt", new ExecutionBolt(), 3)
				.fieldsGrouping("TicksFormatter", new Fields("symbol"))
				.fieldsGrouping("RulesFormatter", new Fields("symbol"));

		Config conf = new Config();
		// conf.setDebug(true);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(args[0], conf, builder.createTopology());
		//StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		

		//Utils.sleep(60 * 60 * 1000);
		//cluster.killTopology("RuleEngine");
		//cluster.shutdown();
	}
}
