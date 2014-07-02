package rule.engine;

import backtype.storm.tuple.Tuple;

public class TickTuple {
	private SymbolBank sb;

	private String symbol;

	private Integer type;

	private Long id;

	private Long issuedAt;

	private Double bid;

	private Double ask;

	public TickTuple(SymbolBank sb, Tuple t) {
		this.sb = sb;
		this.symbol = t.getString(0);
		this.type = t.getInteger(1);
		this.id = t.getLong(2);
		this.issuedAt = t.getLong(3);
		this.bid = t.getDouble(4);
		this.ask = t.getDouble(5);
	}

	public String getSymbol() {
		return this.symbol;
	}

	public Long getIssuedAt() {
		return this.issuedAt;
	}

	public Double getBid() {
		return this.bid;
	}

	public Double getAsk() {
		return this.ask;
	}
}
