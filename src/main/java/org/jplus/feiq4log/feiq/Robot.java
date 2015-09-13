/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.feiq4log.feiq;


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.jplus.feiq4log.appender.FeiqLoggerAppender;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hyberbin
 */
public class Robot {
    private static final String IP_REGEX="((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";;
    private static final Pattern pattern = Pattern.compile(IP_REGEX);

    public static void dealWith(IPMSGData messageBean) throws Exception {
        String msg = messageBean.getAdditionalSection();
        String ip = messageBean.getIp();
        if(msg.equals("1")){//添加日志用户
            msg=Message.getAllMsg(Message.addLogger);
        }else if(msg.equals("2")){//设置日志级别
            msg=Message.getAllMsg(Message.setLevel);
        }else if(msg.equals("3")){//查看所有用户
            Object[] all = FeiqLoggerAppender.getUsers("ALL");
            if(all!=null&&all.length>0){
                msg="";
                for (Object o:all){
                    Map.Entry entry=(Map.Entry)o;
                    msg+=entry.getKey()+"@"+entry.getValue();
                }
            }else{
                msg="没有用户!";
            }
        }else if(msg.startsWith("+")){//具体的添加用户
            msg=addUser(msg,ip);
        }else if(msg.startsWith("-")){//具体的添加用户
            msg=removeUser(msg,ip);
        }else if(msg.startsWith("level@")){//具体的设置日志级别
            msg=setLevel(msg);
        }else{
            msg=Message.getAllMsg(Message.menus);
        }
        IPMSGData back = new IPMSGData(32, msg,ip);
        Feiq.sendMsg(back);
    }

    private static boolean checkIp(String ip){
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    private static boolean checkLevel(String level){
        return !level.contains("@")&&"ERROR@WARN@INFO@DEBUG@TRACE".contains(level);
    }

    private static String addUser(String msg,String currentIp){
        if(msg.equals("+")){
            msg="+me@INFO";
        }
        msg=msg.substring(1);
        String[] split = msg.split("@");
        if(split.length==2){
            String ip=split[0];
            String level=split[1];
            if(ip.equalsIgnoreCase("me")){
                FeiqLoggerAppender.addUser(currentIp,level);
            }else if(checkIp(ip)&&checkLevel(level)){
                FeiqLoggerAppender.addUser(ip,level);
            }
            return "添加成功!";
        }
        return "输入格式不正确,请重新输入!";
    }

    private static String removeUser(String msg,String currentIp){
        if(msg.equals("-")){
            msg="-me";
        }
        String ip=msg.substring(1);
        if(ip.equalsIgnoreCase("me")){
            FeiqLoggerAppender.removeUser(currentIp);
        }else if(checkIp(ip)){
            FeiqLoggerAppender.removeUser(ip);
        }else{
            return "输入格式不正确,请重新输入!";
        }
        return "删除成功!";
    }

    private static String setLevel(String msg){
        String[] split = msg.split("@");
        if(split.length==3){
            if(checkLevel(split[2])){
                LogManager.getLogger(split[1]).setLevel(Level.toLevel(split[2]));
                return "设置成功!";
            }
        }
        return "输入格式不正确,请重新输入!";
    }
}
