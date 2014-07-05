package rule.engine;

import org.apache.log4j.Logger;

import backtype.storm.tuple.Tuple;

public class RuleTuple {
	private static final Logger LOG = Logger.getLogger(RuleTuple.class);

	private SymbolBank sb;

	private String symbol;

	private Integer type;

	private Long id;

	private Long issuedAt;

	private String modifier;

	private String comparator;

	private Double threshold;

	private String url;

	public RuleTuple(SymbolBank sb, Tuple t) {
		this.sb = sb;
		this.symbol = t.getString(0);
		this.type = t.getInteger(1);
		this.id = t.getLong(2);
		this.issuedAt = t.getLong(3);
		this.modifier = t.getString(4);
		this.comparator = t.getString(5);
		this.threshold = t.getDouble(6);
		this.url = t.getString(7);
	}

	public boolean evaluateRule() {
		Double operandA = sb.evaluateSymbol(symbol, modifier);
		if (operandA > 0.0) {
			Double operandB = threshold;

			if ("<".equals(comparator)) {
				return operandA < operandB;
			} else if (">".equals(comparator)) {
				return operandA > operandB;
			} else if ("=".equals(comparator)) {
				return operandA == operandB;
			} else
				return false;
		} else {
			return false;
		}
	}

	public Long getId() {
		return this.id;
	}

	public Long getIssuedAt() {
		return this.issuedAt;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public String getUrl() {
		return this.url;
	}
}
