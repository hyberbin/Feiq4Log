/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.feiq4log.feiq;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

/**
 * @author hyberbin
 */
public class Feiq extends Thread {
    private  static  final  Queue<IPMSGData> mq = new LinkedList<IPMSGData>();
    private static DatagramSocket socket = null;
    private static int port=2425;
    private static final Timer timer=new Timer();


    static {
        try {
            socket = new DatagramSocket(2425);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        new receiveThread().start();
        timer.schedule(new TimerTask() {//发送消息的定时器
            @Override
            public void run() {//定时500毫秒检查一下消息队列中有没有要发送的消息
                if (mq.size() > 0) {
                    try {
                        IPMSGData ipmsgData = mq.poll();
                        byte[] buffer = ipmsgData.toBytes();
                        InetAddress address = InetAddress.getByName(ipmsgData.getIp()); // 发送给消息的地址
                        final DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                        socket.send(packet); // 发送报文
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        },new Date(), 500);
        IPMSGData messageBean=new IPMSGData(32,"飞秋日志系统已经启动,回复+查看系统日志","255.255.255.255");
        mq.add(messageBean);
    }

    public synchronized static void sendMsg(IPMSGData messageBean) {
        mq.add(messageBean);
    }

    static class receiveThread extends Thread {
        public void run() {
            while (true) {
                try {
                    final DatagramPacket receviepacket = new DatagramPacket(new byte[5000], 5000);
                    socket.receive(receviepacket);// 接收回应
                    String content = new String(receviepacket.getData(), 0, receviepacket.getLength(),"GBK");
                    IPMSGData data = new IPMSGData(content);
                    data.setIp(receviepacket.getAddress().getHostAddress());
                    if((data.getCommandNo() & IPMSGData.IPMSG_SENDCHECKOPT) == IPMSGData.IPMSG_SENDCHECKOPT){//当别人给我发消息并且需要回执的时候
                        IPMSGData ipmsgData = new IPMSGData(IPMSGData.IPMSG_RECVMSG, data.getPacketNo(),receviepacket.getAddress().getHostAddress());
                        mq.add(ipmsgData);//告诉他我已经收到了他的消息
                        Robot.dealWith(data);//机器人识别他是要做什么操作
                    }else if(data.getCommandNo()==0){//有人问我在不在线
                        IPMSGData ipmsgData = new IPMSGData(IPMSGData.IPMSG_ANSENTRY, data.getPacketNo(),receviepacket.getAddress().getHostAddress());
                        mq.add(ipmsgData);//回复他我在线
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


