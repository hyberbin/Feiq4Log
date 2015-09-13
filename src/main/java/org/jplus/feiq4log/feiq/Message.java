/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.feiq4log.feiq;

/**
 *
 * @author hyberbin
 */
public class Message {
    private static final String CHANGE_LINE="\n";
    public static final String[] menus=new String[]{
            "请输入操作代码:(只能识别数字)",
            "1.添加/删除日志接收者",
            "2.设置日志输出级别",
            "3.查看用户"
    };

    public static final String[] addLogger=new String[]{
            "添加/删除日志接收者操作格式如下:",
            "(只输入+/-表示只对自己)",
            "+/-ip@日志级别:例如",
            "+192.168.1.9@ERROR",
            "+192.168.1.9@WARN",
            "+192.168.1.9@INFO",
            "-192.168.1.9",
            "-me",
            "+me",
            "-",
            "+"
    };

    public static final String[] setLevel=new String[]{
            "设置日志输出级别操作格式如下:",
            "level@包名@日志级别:例如",
            "level@com.gohighedu.platform@ERROR",
            "level@com.gohighedu.platform.framework@INFO",
            "level@org.springframework@ERROR"
    };


    public static String getAllMsg(String[] msg){
        StringBuilder m=new StringBuilder();
        for (String msgString : msg) {
            m.append(msgString).append(CHANGE_LINE);
        }
        return m.toString();
    }
}
