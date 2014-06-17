rm(list=ls())

setwd("/home/janusz/Study/Insight/Project")

# we have 10000 time points
T=10000
t=1:T

# we have 1000 companies
C=1000
c=1:C

# let's create some random parameters to extension
P=sample((-0.5*C):(0.5*C),C,replace=TRUE)
tempT=matrix(rep(1:T,C),T,C)
tempP=matrix(rep(P,T),T,C,byrow=TRUE)

D= 2 + sin(tempT/C+tempP) * sin(tempT/T) + matrix(runif(T*C,
                                                   min=-0.1,max=0.1),
                                             T,C)
colnames(D)=paste("C_",1:C,":PRICE",sep="")

plot(D[,1])
plot(D[,2])
plot(D[,3])
plot(D[,4])

write.csv(D,"independent.data.csv")
save(D,file="independent.data.RData")