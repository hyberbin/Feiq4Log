package org.jplus.feiq4log.appender;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.jplus.feiq4log.feiq.Feiq;
import org.jplus.feiq4log.feiq.IPMSGData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by hyberbin on 15/9/13.
 */
public class FeiqLoggerAppender extends org.apache.log4j.ConsoleAppender {

    private static final Map<String,String> loggerUser=new HashMap<>();
    private static final Set<String> ERRORUser=new HashSet();
    private static final Set<String> WARNUser=new HashSet();
    private static final Set<String> INFOUser=new HashSet();
    private static final Set<String> DEBUGUser=new HashSet();
    private static final Set<String> TRACEUser=new HashSet();
    private static final Feiq feiq=new Feiq();//只是为了启动飞秋

    private static final Object[] EMPTY=new Object[]{};

    public static synchronized void addUser(String ip,String level){
        removeUser(ip);
        switch (level){
            case "ERROR":
                ERRORUser.add(ip);
                break;
            case "WARN":
                ERRORUser.add(ip);
                WARNUser.add(ip);
                break;
            case "INFO":
                ERRORUser.add(ip);
                WARNUser.add(ip);
                INFOUser.add(ip);
                break;
            case "DEBUG":
                ERRORUser.add(ip);
                WARNUser.add(ip);
                INFOUser.add(ip);
                DEBUGUser.add(ip);
                break;
            case "TRACE":
                ERRORUser.add(ip);
                WARNUser.add(ip);
                INFOUser.add(ip);
                DEBUGUser.add(ip);
                TRACEUser.add(ip);
                break;
        }
        loggerUser.put(ip,level);
    }

    public static Object[] getUsers(String level){
        switch (level){
            case "ERROR":
                return ERRORUser.toArray();
            case "WARN":
                return WARNUser.toArray();
            case "INFO":
                return INFOUser.toArray();
            case "DEBUG":
                return DEBUGUser.toArray();
            case "TRACE":
                return TRACEUser.toArray();
            case "ALL":
                return loggerUser.entrySet().toArray();
        }
        return EMPTY;
    }

    public static synchronized void removeUser(String ip){
        loggerUser.remove(ip);
        ERRORUser.remove(ip);
        WARNUser.remove(ip);
        INFOUser.remove(ip);
        DEBUGUser.remove(ip);
        TRACEUser.remove(ip);
    }

    @Override
    public void append(LoggingEvent event) {
        Object[] users = getUsers(event.getLevel().toString());
        if(users!=null&&users.length>0){
            StringBuilder msg=new StringBuilder();
            msg.append(this.layout.format(event));
            if (layout.ignoresThrowable()) {
                String[] s = event.getThrowableStrRep();
                if (s != null) {
                    int len = s.length;
                    for (int i = 0; i < len; i++) {
                        msg.append(s[i]);
                        msg.append(Layout.LINE_SEP);
                    }
                }
            }
            String msgStr = msg.toString();
            for(Object user:users){
                IPMSGData ipmsgData = new IPMSGData(32, msgStr,user.toString());
                Feiq.sendMsg(ipmsgData);
            }
        }
        super.append(event);
    }
}
