import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
//server receive message, search data from SQL, send result to client.
public class server {
	private   final int PORT = 7777;
	Vector<Handler> vc;
	roomhandler R=new roomhandler();
	public server() throws Exception {
		System.out.println("The server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		//plus user handler stack
 		vc = new Vector<Handler>();
		try {
			while (true) {
				Handler hd = new Handler(listener.accept());
				hd.start();
				System.out.println("서버연결");
				vc.add(hd);
				for(int i=0;i<vc.size();i++) {
					System.out.println(vc.get(i));
				}
			}
		} finally {
			listener.close();
			System.out.println("연결종료");
		}
	}
	
	public void removeClient(Handler hd) {
		// Remove client to vector.
		vc.remove(hd);
	}
	
	//handler connect with client.
	private class Handler extends Thread {
		private Socket socket;
		private InputStream in;
		private DataInputStream dis;
		private OutputStream out;
		private DataOutputStream dos;
		public Handler(Socket socket) {
			this.socket = socket;
		}


		public void run()  {
			try {
				in = socket.getInputStream();
				out =socket.getOutputStream();
				dis =new DataInputStream(in);
				dos =new DataOutputStream(out);
				String msg = null;
						msg=dis.readUTF();
					String str[]=msg.split(" ");
					if(str[0].equals("-1")) {
						if(str.length>1) {
							System.out.println(str[1]);
							dos.writeInt(Integer.valueOf(str[1]));
							dos.flush();
						}
						else {
							System.out.println("-1");
							Date d=new Date();
							long i= (d.getTime()%1000000000)/100;
							dos.writeInt((int)i);
							dos.flush();
						}
						msg=dis.readUTF();
						str=msg.split(" ");
					}
					if(str[0].equals("0")) {//로그인
						String ID=str[1];
						System.out.println("ID = "+ID);
					}
					if(str[0].equals("1")) {//방생성
						//ID, 이름,방id,방이름, 전체인원, 술래수, 시간, 힌트
						System.out.println(msg);
						R.make( Integer.valueOf(str[1]),str[2],Integer.valueOf(str[3]),str[4],Integer.valueOf(str[5]),Integer.valueOf(str[6]),Integer.valueOf(str[7]),Integer.valueOf(str[8]));
						System.out.println("방생성 "+ str[1]);
					}if(str[0].equals("2")) {//방목록
						
					}
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				removeClient(this);
				try {
					dis.close();
					dos.close();
					in.close();
					out.close();
					socket.close();
				} catch (Exception e) {
				}
			}
		}
	}
	public   Scanner in = new Scanner(System.in);

	public   Connection getConnection() throws ClassNotFoundException, SQLException {
		// connect SQL database.
		String url = "jdbc:mysql://localhost:3306/sparegtime";// 경로
		String user = "root";// ID
		String pass = "1234";// password

		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url, user, pass);
		System.out.println("접속");

		return conn;
	}

	public static void main(String[] args) throws Exception {
		//start server
		new server();
	}
}

