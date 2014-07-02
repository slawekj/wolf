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

public class TestTickSpout extends BaseRichSpout {
	/**
	 * 
	 */
	SpoutOutputCollector _collector;
	Random rand = new Random();
	Date date = new Date();

	@Override
	public void nextTuple() {
		Utils.sleep(1000);
		String[] symbols = { "NZDUSD", "USDJPY", "EURUSD" };
		_collector.emit(new Values(symbols[rand.nextInt(symbols.length)], 0,
				rand.nextLong(), date.getTime(), rand.nextDouble(), rand
						.nextDouble()));
	}

	@Override
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector arg2) {
		_collector = arg2;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("symbol", "type", "id", "issued-at", "bid",
				"ask"));
	}
}
