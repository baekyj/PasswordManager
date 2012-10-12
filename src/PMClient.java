import java.io.*;
import java.net.*;
	
public class PMClient {
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
 	PMClient() {}
	void run() {
		try {
			//1. creating a socket to connect to the server
			requestSocket = new Socket("localhost", 2004);
			requestSocket.setSoTimeout(5000);
			System.out.println("Connected to localhost in port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			String[] msgs = {};
			msgs[0] = "doChange -path \"C:\\temp 2\\conf\" -file Wfmversion.ini -type ini -elname DBPassword -elvalue 098765";
			msgs[1] = "doChange -path \"C:\\temp 2\\conf\" -file Wfmversion.ini -type ini -elname DBPassword -elvalue 098765";
			//3: Communicating with the server
			//do {
			//for (int i = 0; i < 2; i++) {
				try {
					message = (String)in.readObject();
					System.out.println("from server > " + message);
					
					//TODO : modify this for Health Checking.
					sendMessage("Hi my server");
					showRecvMsg(message);
					//message="doXML";
					//sendMessage(message);
					
					//TODO : below value is handed by args[0] (from batch file argument)
					//message="doChange -path \"C:\\temp 2\\conf\" -file Wfmversion.ini -type ini -elname DBPassword -elvalue 098765";
					//sendMessage(message);
					
					sendMessage("bye");
					showRecvMsg(message);
				} catch(ClassNotFoundException classNot) {
					System.err.println("data received in unknown format");
				}
			//}
			//} while(!message.equals("bye"));
		} catch(UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch(IOException ioException) {
			ioException.printStackTrace();
		} finally {
			//4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
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
	public String getRest(String strCmd) { //test
		return strCmd.substring(strCmd.indexOf(" ")+1);
	}
	public String chkSep(String path, String sep) {
		return String.valueOf(path.indexOf(sep));
	}
	public void showRecvMsg(String recvMsg) {
		System.out.println("from server > " + message);
	}
	public static void main(String args[]) {
		PMClient client = new PMClient();
		client.run();
	}
}
