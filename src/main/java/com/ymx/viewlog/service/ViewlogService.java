package com.ymx.viewlog.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.*;
import com.ymx.viewlog.entity.Server;
import com.ymx.viewlog.repository.ServerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ViewlogService {

    @Autowired
    private ServerRepository serverRepository;

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

	public Object addServer(Map<String, String> formData) {
        log.info("addServer > name: " + formData.get("name") + " path: " + formData.get("path"), " port: " + formData.get("port"));
        Server server = Server.builder()
            .status(0)
            .name(formData.get("name"))
            .logPath(formData.get("path"))
            .date(new Timestamp(System.currentTimeMillis()))
            .port(formData.get("port"))
            .build();
        serverRepository.save(server);
		return null;
	}

    public Object getServerList() {
        return serverRepository.findAll();
    }

    public Object getConsoleLog(Integer index) {
        Optional<Server> serverOpt = serverRepository.findById(index);
        if(!serverOpt.isPresent()) return null;
        Server server = serverOpt.get();
        String line = null;
        List<String> list = new ArrayList<String>();
        try {
            jschSendCommand("mode con:cols=400 lines=20 & type "+server.getLogPath());
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

	public Object isRunServer() {
        String line = null;
        // List<String> list = new ArrayList<String>();
        List<Server> serverList = serverRepository.findAll();
        try {
            for (Server server : serverList) {
                Integer isRunning = 0;
                jschSendCommand("netstat -ano | findstr "+server.getPort());
                BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
                while ((line = reader.readLine()) != null) 
                {	
                    if(line.contains("[K")) continue;
                    if(line.contains("LISTENING")) isRunning = 1;
                    // list.add(line);
                }
                if(server.getStatus() != isRunning){ // 서버 상태와 체크 상태가 다르면 갱신해줌
                    Server newServer = Server.builder()
                        .index(server.getIndex())
                        .status(isRunning)
                        .name(server.getName())
                        .logPath(server.getLogPath())
                        .date(new Timestamp(System.currentTimeMillis()))
                        .port(server.getPort())
                        .build();
                    serverRepository.save(newServer);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
	}

    public String putServer(Map<String, String> formData) {
        log.info("putServer > name: " + formData.get("name") + " path: " + formData.get("path"), " port: " + formData.get("port"));
        Optional<Server> serverOpt = serverRepository.findById(Integer.parseInt(formData.get("index")));
        if(!serverOpt.isPresent()) return null;
        Server server = serverOpt.get();
        Server newServer = Server.builder()
            .index(server.getIndex())
            .status(server.getStatus())
            .name(formData.get("name"))
            .logPath(formData.get("path"))
            .date(new Timestamp(System.currentTimeMillis()))
            .port(formData.get("port"))
            .build();
        serverRepository.save(newServer);
        return "수정 완료";
    }
	
}
