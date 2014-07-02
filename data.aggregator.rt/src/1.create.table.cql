CREATE KEYSPACE janusz_forex_rt_demo WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '2'
};

USE janusz_forex_rt_demo;

CREATE TABLE ticks (
pair_day text, 
issued_at timestamp, 
bid float, 
ask float, 
PRIMARY KEY (pair_day, issued_at) 
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ('usdjpy:2011-02-03','2011-02-03 04:05:13.020+0000',0.12,0.13) USING TTL 3600;

CREATE TABLE ticks_avg_s (
pair_day text, 
issued_at timestamp, 
bid float, 
ask float, 
PRIMARY KEY (pair_day, issued_at) 
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ('usdjpy:2011-02-03','2011-02-03 04:05:00.000+0000',0.12,0.13) USING TTL 3600;


