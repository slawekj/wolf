CREATE KEYSPACE IF NOT EXISTS janusz_forex_rt_demo WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '2'
};

CREATE KEYSPACE IF NOT EXISTS oanda_ticks WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '2'
};

CREATE KEYSPACE IF NOT EXISTS hist_ticks WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '2'
};

CREATE KEYSPACE IF NOT EXISTS market_data WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '2'
};

USE market_data;

CREATE TABLE IF NOT EXISTS ticks (
   pair_day text, 
   issued_at timestamp, 
   bid float, 
   ask float, 
   PRIMARY KEY (pair_day, issued_at) 
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

CREATE TABLE IF NOT EXISTS ticks_avg_s (
   pair_day text, 
   issued_at timestamp, 
   bid float, 
   ask float, 
   PRIMARY KEY (pair_day, issued_at) 
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

USE janusz_forex_rt_demo;

CREATE TABLE IF NOT EXISTS ticks (
   pair_day text, 
   issued_at timestamp, 
   bid float, 
   ask float, 
   PRIMARY KEY (pair_day, issued_at) 
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ('usdjpy:2011-02-03','2011-02-03 04:05:13.020+0000',0.12,0.13) USING TTL 3600;

CREATE TABLE IF NOT EXISTS ticks_avg_s (
   pair_day text, 
   issued_at timestamp, 
   bid float, 
   ask float, 
   PRIMARY KEY (pair_day, issued_at) 
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

INSERT INTO ticks (pair_day,issued_at,bid,ask) VALUES ('usdjpy:2011-02-03','2011-02-03 04:05:00.000+0000',0.12,0.13) USING TTL 3600;

USE oanda_ticks;

CREATE TABLE IF NOT EXISTS eurusd_ticks (
   day   date,
   nanos time, 
   bid   float, 
   ask   float, 
   PRIMARY KEY (day, nanos)
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

CREATE TABLE IF NOT EXISTS eurusd_ticks_tps (
   day  date,
   hour int,
   min  int,
   sec  int,
   tps  int,
   PRIMARY KEY (day, hour, min, sec)
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

USE hist_ticks;

CREATE TABLE IF NOT EXISTS eurusd_ticks (
   day   date,
   nanos time, 
   bid   float, 
   ask   float, 
   PRIMARY KEY (day, nanos)
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

CREATE TABLE IF NOT EXISTS eurusd_ticks_tps (
   day  date,
   hour int,
   min  int,
   sec  int,
   tps  int,
   PRIMARY KEY (day, hour, min, sec)
) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

