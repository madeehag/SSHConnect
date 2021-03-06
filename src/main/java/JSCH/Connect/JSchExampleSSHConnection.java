package JSCH.Connect;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JSchExampleSSHConnection {
	//private static Logger log = LoggerFactory.getLogger(JSchExampleSSHConnection.class);
	
	public static void main(String[] args) {
		JSch.setLogger(new MyLogger());
		String host="dedwdprsrmc001.de.neustar.com";
	    String user="dnartdd";
	    //String password="sshpwd";
	    StringBuffer cmd = new StringBuffer();
	    cmd =  cmd.append("ssh dnartdd@dedwdprsrmc001 -i .ssh/jenkins  ").append(" sh /opt/dnartdd/environment-files/ssConfRestbkp.sh ");
		String command = cmd.toString();
	   // String command1="ls -ltr";
	    try{
	    	
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	config.put("PreferredAuthentications", "publickey");
	    	JSch jsch = new JSch();
	    	jsch.addIdentity("/home/dnasssd/.ssh/id_rsa".toString(),"12345");
	    	jsch.setKnownHosts("/home/dnasssd/.ssh/known_hosts".toString());
	    	Session session=jsch.getSession(user, host, 22);
	    	
	    	session.setConfig(config);
	    	session.connect();
	    	System.out.println("Connected");
	    	
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        channel.setInputStream(System.in);
	        channel.setOutputStream(System.out);
	        
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
	    }catch (JSchException e) {
	    	System.out.println("JSCH Exception"+e);
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }

	}
	}


