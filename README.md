###Feiq4Log
自动将系统日志通过飞秋发送到指定用户，需要结合Log4j使用，可以通过会话方式动态改变日志级别和接收内容。
本系统可能解决测试指定环境时不方便查看日志的麻烦.
操作简单
##使用说明
1.配置log4j的Appender为org.jplus.feiq4log.appender.FeiqLoggerAppender
    例如:
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
    <appender name="CONSOLE" class="org.jplus.feiq4log.appender.FeiqLoggerAppender">
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern"
                   value="%p [%t] %c{1}.%M(%L) | %m%n"/>
        </layout>
    </appender>
    <root>
        <level value="DEBUG"/>
        <appender-ref ref="CONSOLE"/>
    </root>
</log4j:configuration>
```
2.启动系统后,内置飞秋会自己启动.用户在自己的飞秋好友列表中可以看到有一个用户为"飞秋日志系统";

3.发送任意消息到"飞秋日志系统"会收到回复:
     请输入操作代码:(只能识别数字)
     1.添加/删除日志接收者
     2.设置日志输出级别
     3.查看用户

    输入1,会收到回复:
     添加/删除日志接收者操作格式如下:
     (只输入+/-表示只对自己)
     +/-ip@日志级别:例如
     +192.168.1.9@ERROR
     +192.168.1.9@WARN
     +192.168.1.9@INFO
     -192.168.1.9",
     -me",
     +me",
     -",
     +"

     输入+即可将自己设置为INFO级别日志接收者
     输入+ip@Level可将指定ip的用户设置为Level级别的日志接收者
     输入-表示自己不再接收日志内容

     其他操作依此类推
