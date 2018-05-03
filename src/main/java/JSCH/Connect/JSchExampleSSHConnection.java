package JSCH.Connect;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class JSchExampleSSHConnection {

	public static void main(String[] args) {
		String host="dedwfprsapp01.de.neustar.com";
	    String user="dnasssd";
	    //String password="sshpwd";
	    String command1="ls -ltr";
	    try{
	    	
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
	    	JSch jsch = new JSch();
	    	jsch.addIdentity("/home/dnasssd/.ssh/id_rsa");
	    	jsch.setKnownHosts("/home/dnasssd/.ssh/known_hosts");
	    	Session session=jsch.getSession(user, host, 22);
	    	
	    	session.setConfig(config);
	    	session.connect();
	    	System.out.println("Connected");
	    	
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command1);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            System.out.print(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        channel.disconnect();
	        session.disconnect();
	        System.out.println("DONE");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }

	}
	}


