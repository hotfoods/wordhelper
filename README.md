<font face="微软雅黑">wordhelper:zap: :pager:</font>
===


更加智能化


## 1单词的刷新策略

### 1.1基础数据库见表/变量

#### 1.1.1 相关变量 

- [ ] 单词名称  
>>单词词性
>>单词释义
>>单词词境

>个人变量       -
>>单词背诵频率
>>单词掌握等级
>>单词推送优先级
>>单词日志    
1. 日期化分表/ day / week /month /year  2. 时间化分表
>>单词注释
>>单词备忘录

                    单词池         - 近意单词
                                  - 外观近似单词
                                  - 近音单词
                                  - 单词池阈值 ，单词数
                                  - 随机单词数
                                 
                    总体阈值        -单词池段落指针
                                   - 单词高频阈值
                                   - 单词低频阈值
                                   - 总体单词高频阈值单词池指针
                                   - 单词频率平均值
                                  
                                  
#### 1.1.2 数据库表




### 1.2 单词表刷新策略


#### 1.2.1前后交替刷新
                   
                   -每次从前后 100 （/40 new / 60 old（include last 40 new））
                   
                   
#### 1.2.2 单词库随机刷新

#### 1.2.3 单词池高频刷新

#### 1.2.4 单词池低频刷新

## 2检测单词的熟练程度

### 2.1 总计变量

#### 2.1.1 个人单词等级统计表

#### 2.1.2  个人单词频率分布图

### 2.2判断等级手法

#### 2.2.1  判断策略 词境解析填空

#### 2.2.2  默写 中英反默

### 2.3 等级调度策略

                   低频+高错误=>高推送
                   低频+地错误 =>中推送
                           ...
                   数据表

## 补充：单词池分类详解 - 近形 -近语境 高频 低频 掌握程度 推送优先值
