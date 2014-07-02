package rule.engine;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class ExecutionBolt extends BaseRichBolt implements SymbolBank {
	/**
	 * 
	 */
	private static final Logger LOG = Logger.getLogger(ExecutionBolt.class);
	OutputCollector _collector;
	ApiExecutor ae = new ApiExecutor();

	// Bank will retain rules for at most 15 minutes
	// by default

	private static long ttl = 1000 * 60 * 15;
	private Map<String, Long> timestamps;
	private Map<String, Double> askPrice;
	private Map<String, Double> bidPrice;
	private Set<Long> rulesIdsInSystem;
	private Set<RuleTuple> rulesToExecute;
	private Date date = new Date();

	public ExecutionBolt() {
		timestamps = new HashMap<String, Long>();
		askPrice = new HashMap<String, Double>();
		bidPrice = new HashMap<String, Double>();
		rulesIdsInSystem = new HashSet<Long>();
		rulesToExecute = new HashSet<RuleTuple>();
	}

	@Override
	public void execute(Tuple t) {
		if (t.getInteger(1) == 0) {
			// it's a tick
			TickTuple tick = new TickTuple(this, t);

			// 1. update SymbolBank
			if (timestamps.containsKey(tick.getSymbol())) {
				if (timestamps.get(tick.getSymbol()) < tick.getIssuedAt()) {
					timestamps.put(tick.getSymbol(), tick.getIssuedAt());
					bidPrice.put(tick.getSymbol(), tick.getBid());
					askPrice.put(tick.getSymbol(), tick.getAsk());
				}
			} else {
				timestamps.put(tick.getSymbol(), tick.getIssuedAt());
				bidPrice.put(tick.getSymbol(), tick.getBid());
				askPrice.put(tick.getSymbol(), tick.getAsk());
			}
			// 2. try to execute rules
			long currentTimestamp = date.getTime();
			for (Iterator<RuleTuple> it = rulesToExecute.iterator(); it
					.hasNext();) {
				RuleTuple rule = it.next();
				// check if rule is not expired
				if (rule.getIssuedAt() + ttl > currentTimestamp) {
					// execute rule
					if (rule.evaluateRule()) {
						// fire up the rule
						LOG.info("rule is ready to be executed- after waiting in queue");
						ae.sendGet(rule.getUrl());
						// now remove the rule
						rulesIdsInSystem.remove(rule.getId());
						rulesToExecute.remove(rule);
					}
				} else {
					// remove rule from pool, it's expired
					rulesIdsInSystem.remove(rule.getId());
					rulesToExecute.remove(rule);
				}
			}
		} else if (t.getInteger(1) == 1) {
			// it's a rule
			// let's add it to the system
			RuleTuple rule = new RuleTuple(this, t);
			if (!rulesIdsInSystem.contains(rule.getId())) {
				// TODO let's try to execute that rule now
				if (rule.evaluateRule()) {
					// fire up the rule
					LOG.info("rule is ready to be executed- right away");
					ae.sendGet(rule.getUrl());
				} else {
					// i can't execute now, put to queue
					LOG.info("rule goes to queue");
					rulesIdsInSystem.add(rule.getId());
					rulesToExecute.add(rule);
				}
			}
		}
		_collector.ack(t);
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		_collector = arg2;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("execution"));
	}

	@Override
	public Double evaluateSymbol(String symbol, String symbolModifier) {
		switch (symbolModifier) {
		case "BID":
			if (bidPrice.containsKey(symbol)) {
				return bidPrice.get(symbol);
			} else {
				return -1.0;
			}
		case "ASK":
			if (askPrice.containsKey(symbol)) {
				return askPrice.get(symbol);
			} else {
				return -1.0;
			}
		}
		return -1.0;
	}
}