package com.oracle.hpcm.utils;

import java.io.File;
import java.io.FileInputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPUtil {
	public static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("sftp");
    public static void transfer(File f) {
    	
        JSch jsch = new JSch();
        String filename = f.getName();
        Session session = null;
        try {

            session = jsch.getSession("mbanchho", System.getProperty("server"), 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("Orapwd06");
            session.connect();
            logger.info(session.getHost()+ " "+ session.getServerVersion());
            //Execute commands
            Channel channelExec=session.openChannel("exec");
            ((ChannelExec)channelExec).setCommand("rm " + filename);
            ((ChannelExec)channelExec).setCommand("sudo rm  /u03/data/profit/inbox/"+filename);
            channelExec.connect();
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            sftpChannel.put(new FileInputStream(f), filename);
            //System.out.println("exit-status: "+sftpChannel.getExitStatus());
            sftpChannel.exit();
            //channelExec.connect();
            /*InputStream in=channelExec.getInputStream();
            ((ChannelExec)channelExec).setErrStream(System.err);*/
            ((ChannelExec)channelExec).setCommand("sudo cp "+filename+" /u03/data/profit/inbox");
            channelExec.connect();
/*            out.write(("Orapwd06"+"\n").getBytes());
            out.flush();
            ((ChannelExec)channelExec).setCommand("sudo -u hpcmunix -s");*/
/*            byte[] tmp=new byte[1024];
            while(true){
              while(in.available()>0){
                int i=in.read(tmp, 0, 1024);
                if(i<0)break;
                System.out.print(new String(tmp, 0, i));
              }
              if(channelExec.isClosed()){
                System.out.println("exit-status: "+channelExec.getExitStatus());
                break;
              }
              try{Thread.sleep(1000);}catch(Exception ee){}
            }*/
            

            channelExec.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
}