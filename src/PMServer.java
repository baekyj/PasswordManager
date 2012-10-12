import java.io.*;
import java.net.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.tools.ant.types.Commandline;
	
public class PMServer {
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	String[] arMsg;
	String strCommand;
	XMLController xmlcon = null;
	INIControllerStandard iniConS = null;
	String path;
	String file;
	String type;
	String elname;
	String elvalue;
	String strSeparator;
	boolean isJobCompleted = false;
	PMServer() {}
	void run() {
		try {
			//1. creating a server socket
			providerSocket = new ServerSocket(2004, 10);
			//2. Wait for connection
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			System.out.println("Connection received from " + connection.getInetAddress().getHostName());
			//3. get Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Connection successful");
			//4. The two parts communicate via the input and output streams
			do {
				try {
					message = (String)in.readObject();
					
					arMsg = message.split(" ");
					strCommand = arMsg[0];
					System.out.println("receive > " + message);
					xmlcon = new XMLController();
					iniConS = new INIControllerStandard();
					if (message.equals("doXML")) {
						xmlcon.doChange();
					}
					//sendMessage("doXML Completed!");
					if (strCommand.equals("doChange")) {
						getArgs(getRest(message));
						System.out.println("Change it!");
						if (type.equals("ini")) {
							if (path.indexOf("\\") > 0) {
								strSeparator = "\\";
							} else if (path.indexOf("/") > 0) {
								strSeparator = "/";
							}
							iniConS.doChange(path + strSeparator + file, elname, elvalue);
						}
					}
					if (strCommand.equals("bye")) {
						sendMessage("bye");
					}
				} catch (ClassNotFoundException classnot) {
					System.err.println("Data received in unknown format");
				} catch (Exception e) {
					System.err.println("error:" + e.toString());	
				}
			} while(!message.equals("bye"));
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			//4: Closing connection
			try {
				in.close();
				out.close();
				providerSocket.close();
			} catch(IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("send > " + msg);
		} catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	private void getArgs(String cmd) {
		String[] myArgs = Commandline.translateCommandline(cmd);
		Options options = new Options();
		options.addOption("path", true, "");
		options.addOption("file", true, "");
		options.addOption("type", true, "");
		options.addOption("elname", true, "");
		options.addOption("elvalue", true, "");
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine line = parser.parse(options, myArgs);
			if (line.hasOption("path")) {
				path = line.getOptionValue("path");
			}
			if (line.hasOption("file")) {
				file = line.getOptionValue("file");
			}
			if (line.hasOption("type")) {
				type = line.getOptionValue("type");
			}
			if (line.hasOption("elname")) {
				elname = line.getOptionValue("elname");
			}
			if (line.hasOption("elvalue")) {
				elvalue = line.getOptionValue("elvalue");
			}
		} catch (Exception e) {
			
		}
	}
	
	public String getRest(String strCmd) {
		return strCmd.substring(strCmd.indexOf(" ")+1);
	}
	
	public static void main(String args[]) {
		PMServer server = new PMServer();
		while(true) {
			server.run();
		}
	}
}

