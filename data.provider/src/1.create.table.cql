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

INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ('usdjpy_monday','2011-02-03 04:05:00.020',12,13);
INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ('usdjpy_monday','2011-02-03 04:05:00.040',12,13);
INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ('usdjpy_monday','2011-02-03 04:05:00.060',12,13);
INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ('usdjpy_monday','2011-02-03 04:05:00.080',12,13);
INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ('usdjpy_monday','2011-02-03 04:05:00.100',12,13);
