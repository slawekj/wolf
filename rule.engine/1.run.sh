#!/bin/bash

./storm-0.9.0.1/bin/storm jar myjob.jar rule.engine.RuleEngineTopology RuleEngine &>> ./log/server.log &
