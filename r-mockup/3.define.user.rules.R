# an example of user-defined rules

# first rule

if (getMarket()["C_1:PRICE"] > getMarket()["C_2:PRICE"]) {
  buy("C_1",10)
}

# second rule

if (getMarket()["C_2:PRICE"] > getMarket()["C_3:PRICE"]) {
  buy("C_2",10)
}
