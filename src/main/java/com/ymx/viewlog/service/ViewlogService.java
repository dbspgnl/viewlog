package com.ymx.viewlog.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ViewlogService {

	private String host = "";
	private String username = "";
	private String password = "";
	private int port = 22;

    JSch jsch = new JSch();
    Session session = null;
    Channel channel = null;

    public ViewlogService() {
        host = System.getProperty("ssh.host");
        username = System.getProperty("ssh.name");
        password = System.getProperty("ssh.pw");
        try {
            System.out.println(host);
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException e) {
            log.error(e.getMessage());
        }
    }

    // private void jschDisconnect(){ // 세션 종료
    //     if (channel != null) channel.disconnect();
    //     if (session != null) session.disconnect();
    // }

	public String ssh(){
        String line = null;
        StringBuffer sb = new StringBuffer();
        try {
            jschSendCommand("tasklist | findstr svc");
            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            while ((line = reader.readLine()) != null) 
            {
				sb.append(line);
                if(sb != null){System.out.println("데이터를 받아왔습니다."); break;}
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } 
        return sb.toString();
    }

	public Boolean isRemoting() {
		Boolean result = false;
        String line = null;
        StringBuffer sb = new StringBuffer();
        try {
            jschSendCommand("tasklist | findstr svc");
            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            while ((line = reader.readLine()) != null) 
            {	
				sb.append(line);
				if(line.contains("RDP-Tcp")){
					// System.out.println("현재 원격 접속중입니다.");
					result = true;
					break;
				}
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } 
		return result;
	}

    private Channel jschSendCommand(String command){
        try {
            channel = session.openChannel("exec");
            ChannelExec channelExec = (ChannelExec) channel;
            channelExec.setPty(true);
            channelExec.setCommand(command);
            ((ChannelExec) channel).setErrStream(System.err);        
            channel.connect();
        } catch (JSchException e) {
            log.error(e.getMessage());
        }
        return channel;
    }

    public List<String> log() {
        String line = null;
        List<String> list = new ArrayList<String>();
        try {
            jschSendCommand("mode con:cols=400 lines=20 & type D:\\jenkins\\mxspace_yt_prod\\workspace\\MXWorksWebBack_Prod\\log_prod\\mxspace_web_back.log");
            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            while ((line = reader.readLine()) != null) 
            {	
                if(line.contains("[K")) continue;
                list.add(line);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return list;
    }
	
}
