# 定时：Spring Boot环境下使用定时
[参考视频](https://www.bilibili.com/video/BV1KW411F7oX?p=25)

## 两个关键注解
1. @EnabledSchedule
2. @Schedule  

## CRON
|   字段    |   允许值    |   允许的特殊字符  |
|---|---|---|
| 秒        | 0-59                  | ,-*/             |
| 分        | 0-59                  | ,-*/             |
| 小时      | 0-23                  | ,-*/             |
| 日期      | 1-31                  | ,-*?/LWC         |
| 月份      | 1-12                  | ,-*/             |
| 星期      | 0-7或SUN-SAT 0,7是SUN | ,-*?/LC#         |
>注： Quartz中1-7是周日到周六， 1代表周日，2代表周一以此类推。

## CRON 特殊字符的意义
|   特殊字符   |  代表含义  |
|---|---|
|,|枚举|
|-|区间|
|*|任意|
|/|步长|
|?|日/星期冲突匹配|
|L|最后|
|W|工作日|
|C|和Calendar联系后计算过的值|
|#|星期，4#2,第2个星期四|  


## 示例
```
# 已测试的
    // @Scheduled(cron = "* * * * * *") // 每秒运行一次
    // @Scheduled(cron = "0 * * * * MON-SAT") // 周一到周六每分钟的第一秒打印
    // @Scheduled(cron = "0,1,2,3,4 * * * * 1-6") // 周一到周六每分钟的第1，2，3，4，5秒执行 // 现象(2021/3/31): 很奇怪 0的哪一项会被连续调用两次？！ 答：上面的注解并没有被注释，同时生效了！
    @Scheduled(cron = "0/4 * * * * 1-6") // 周一到周六每4秒执行一次
```

```
#未测试的  
- 0 0/5 14,18 * * ?     每天14点整，18点整，每隔5分钟执行一次
- 0 15 10 ? * 1-6       每个月的周一至周六10:15分执行一次
- 0 0 2 ? * 6L          每个月的最后一个周六凌晨2点执行一次
- 0 0 2 LW * ?          每个月的最后一个工作日凌晨2点执行一次
- 0 0 2-4 ? * 1#1       每个月的第一个周一凌晨2点到4点期间，每个整点都执行一次。
```

## Spring 提供的两个接口
- TaskExecutor
- TaskScheduler

## 其他定时选择
1. [JDK Timer](https://www.imooc.com/learn/841)
2. [Quartz](https://www.bilibili.com/video/BV1zz4y1X71Z)
