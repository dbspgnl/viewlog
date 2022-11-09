package com.ymx.viewlog.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.*;

@Service
public class ViewlogService {

	private String host = "{아이피}";
	private String username = "{계정}";
	private String password = "{비밀번호}";
	private int port = 22;

	public String ssh(){

		System.out.println(host);
		System.out.println(username);
		System.out.println(password);
		System.out.println(port);
        
        JSch jsch = new JSch();
        Session session = null;
        Channel channel = null;
        String line = null;
        StringBuffer sb = new StringBuffer();

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();  //연결

            channel = session.openChannel("exec");  //채널접속
            ChannelExec channelExec = (ChannelExec) channel; //명령 전송 채널사용
            channelExec.setPty(true);
            channelExec.setCommand("tasklist | findstr svc"); // windows : svc process search
            
            ((ChannelExec) channel).setErrStream(System.err);        
            channel.connect();  //실행

            // 결과값 담기
            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            while ((line = reader.readLine()) != null) 
            {
				sb.append(line);
				if(line.contains("RDP-Tcp")){
					System.out.println("현재 원격 접속중입니다.");
					break;
				}
				else{
					System.out.println("원격을 사용하고 있지 않습니다.");
					break;
				}
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    
        return sb.toString();
    }

	public Boolean isRemoting() {
		Boolean result = false;

		JSch jsch = new JSch();
        Session session = null;
        Channel channel = null;
        String line = null;
        StringBuffer sb = new StringBuffer();

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();  //연결

            channel = session.openChannel("exec");  //채널접속
            ChannelExec channelExec = (ChannelExec) channel; //명령 전송 채널사용
            channelExec.setPty(true);
            channelExec.setCommand("tasklist | findstr svc"); // windows : svc process search
            
            ((ChannelExec) channel).setErrStream(System.err);        
            channel.connect();  //실행

            // 결과값 담기
            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            while ((line = reader.readLine()) != null) 
            {	
				sb.append(line);
				if(line.contains("RDP-Tcp")){
					System.out.println("현재 원격 접속중입니다.");
					result = true;
					break;
				}
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
		return result;
	}
	
}
