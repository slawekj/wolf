rm(list=ls())

setwd("/home/janusz/Study/Insight/Project")

# load historical data
load("independent.data.RData")

# initialize framework
source("2.boot.simulation.engine.R")

# get user-defined functions
rules = parse("3.define.user.rules.R")

# initialize simulation
setPenalty(10)
setBalance(1000)

market=rep(0,ncol(D))
names(market)=colnames(D)
setMarket(market)

assets=rep(0,ncol(D))
names(assets)=colnames(D)
setAssets(assets)

# run simulation step-by-step
# no other way than for loop

# run simulation for M iterations
M = 10000
initTotalValue(rep(0,M))

for (i in 1:M) {
  setMarket(D[i,])
  for (j in 1:length(rules)) {
    eval(rules[[j]])
    
    # calculate total value of all the stocks
    temp = sum(getMarket() * getAssets()) + getBalance()
    setTotalValue(temp,i)
  }
}

plot(getTotalValue())
#getBalance()
