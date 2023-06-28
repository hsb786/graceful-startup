# graceful-startup
服务刚启动时，因为需要初始化和预热，所以这时的请求rt会很高

为了给服务器预热时间，服务启动时，动态调整nacos weight，如： 0.2 —> 0.4 -> 0.6 -> 0.8 -> 1.0, 缓慢的将流量转发给新机器

配置
```
graceful:
  enabled: true  #开关
  increaseWeight: 0.2 #每次增加的权重
  eachStepSleepSecond: 10 #每次间隔的时间
```


