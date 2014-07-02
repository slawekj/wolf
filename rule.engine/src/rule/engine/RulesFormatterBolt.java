package rule.engine;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class RulesFormatterBolt extends BaseRichBolt {
	OutputCollector _collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String message = null;
		try {
			message = new String((byte[]) input.getValue(0), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// TODO add better verification

		String[] fields;
		if (message != null && (fields = message.split(" ")).length == 8) {
			String symbol = fields[0];
			Integer type = Integer.parseInt(fields[1]);
			Long id = Long.parseLong(fields[2]);
			Long issuedAt = Long.parseLong(fields[3]);
			String mod = fields[4];
			String com = fields[5];
			Double thr = Double.parseDouble(fields[6]);
			String url = fields[7];
			_collector.emit(new Values(symbol, type, id, issuedAt, mod, com,
					thr, url));
		}
		_collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("symbol", "type", "id", "issued-at",
				"modifier", "comparator", "threshold", "url"));
	}
}
