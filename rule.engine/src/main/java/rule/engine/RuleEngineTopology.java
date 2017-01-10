package rule.engine;

import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.topology.TopologyBuilder;

import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.StormSubmitter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class RuleEngineTopology {

	private static final Logger LOG = LogManager.getLogger();

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException, AuthorizationException {

                try {
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
		} catch (Exception e) {
			LOG.info("Error in RuleEngineTopology.  Returning.");
			return;
		}
	}
}
