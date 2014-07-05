package rule.engine;

import java.io.UnsupportedEncodingException;
import java.util.List;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class RuleScheme implements Scheme {

	public List<Object> deserialize(byte[] arg0) {
		try {
			String tick = new String(arg0, "UTF-8");
			String[] fields = tick.split(" ");
			return new Values(fields[0], Integer.parseInt(fields[1]),
					Long.parseLong(fields[2]), Long.parseLong(fields[3]),
					fields[4], fields[5], Double.parseDouble(fields[6]),
					fields[7]);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public Fields getOutputFields() {
		return new Fields("symbol", "type", "id", "issued-at", "modifier",
				"comparator", "threshold", "url");
	}
}
