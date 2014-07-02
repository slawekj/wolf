package rule.engine;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class TestRuleSpout extends BaseRichSpout {
	SpoutOutputCollector _collector;
	Random rand = new Random();
	Date date = new Date();

	@Override
	public void nextTuple() {
		Utils.sleep(10000);
		String[] symbols = { "NZDUSD", "USDJPY", "EURUSD" };
		String[] compars = { "<", ">", "=" };
		_collector.emit(new Values(symbols[rand.nextInt(symbols.length)], 1,
				rand.nextLong(), date.getTime(), "ASK", compars[rand
						.nextInt(compars.length)], rand.nextDouble()));
	}

	@Override
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector arg2) {
		_collector = arg2;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("symbol", "type", "id", "issued-at",
				"modifier", "comparator", "threshold"));
	}

}
