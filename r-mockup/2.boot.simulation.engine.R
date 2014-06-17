# this script will simply define a bunch of useful structures

toolbox = new.env()
assign("penalty",0,env=toolbox)
assign("balance",0,env=toolbox)
assign("assets",0,env=toolbox)
assign("market",0,env=toolbox)
assign("totalValue",0,env=toolbox)

getPenalty = function() {
  return(get("penalty",toolbox))
}

setPenalty = function(x) {
  assign("penalty",x,env=toolbox)
}

setBalance = function(x) {
  assign("balance",x,env=toolbox)
}

getBalance = function() {
  return(get("balance",toolbox))
}

setMarket = function(x) {
  assign("market",x,env=toolbox)
}

getMarket = function() {
  return(get("market",toolbox))
}

setAssets = function(x) {
  assign("assets",x,env=toolbox)
}

getAssets = function() {
  return(get("assets",toolbox))
}

getTotalValue = function() {
  return(get("totalValue",toolbox))
}

initTotalValue = function(x) {
  assign("totalValue",x,env=toolbox)
}

setTotalValue = function(x,i) {
  temp = getTotalValue()
  temp[i] = x
  assign("totalValue",temp,env=toolbox)
}

buy = function(company,count) {
  I = paste(company,"PRICE",sep=":")
  to.spend=count * getMarket()[I] + getPenalty()
  #write(paste("count",count),stdout())
  #write(paste("price",getMarket()[I]),stdout())
  #write(paste("to spend",to.spend),stdout())
  if (getBalance()>=to.spend) {
    #write(paste("buying",count,"stocks of",company),stdout())
    #write(paste("before",getBalance()),stdout())
    setBalance(getBalance() - to.spend)
    #write(paste("after",getBalance()),stdout())
    assets=getAssets()
    assets[I] = assets[I] + count
    setAssets(assets)
  }
}

sell = function(company,count) {
  I = paste(company,"PRICE",sep=":")
  to.make=count * getMarket()[I] - getPenalty()
  if (getAssets()[I]>=count) {
    #write(paste("selling",count,"stocks of",company),stdout())
    setBalance(getBalance() + to.make)
    assets=getAssets()
    assets[I] = assets[I] - count
    setAssets(assets)
  }
}
