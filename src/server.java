import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
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
		public Handler(Socket sc) {
			socket = sc;
		}


		public void run()  {
			try {
				
				String msg = null;
				while(true) {
					in = socket.getInputStream();
					out =socket.getOutputStream();
					dis =new DataInputStream(in);
					dos =new DataOutputStream(out);
					msg=dis.readUTF();
					if(msg==null) {
						return;
					}
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
					}
					else if(str[0].equals("0")) {//로그인
						String ID=str[1];
						System.out.println("ID = "+ID);
					}
					else if(str[0].equals("1")) {//방생성
						//ID, 이름,방id,방이름, 전체인원, 술래수, 시간, 힌트
						System.out.println(msg);
						R.make( Integer.valueOf(str[1]),str[2],Integer.valueOf(str[3]),str[4],Integer.valueOf(str[5]),Integer.valueOf(str[6]),Integer.valueOf(str[7]),Integer.valueOf(str[8]),socket);
						R.sitting(Integer.valueOf(str[3]), Integer.valueOf(str[1]), socket);
						System.out.println("방생성 "+ str[1]);
					}else if(str[0].equals("100")) {//방정보
						System.out.println(msg);
						System.out.println("broad cast ");
						if(str.length==3) {
							R.setSocket(Integer.valueOf(str[1]), Integer.valueOf(str[2]),socket);
						}
						room Room= R.inform(Integer.valueOf(str[1]));
						Iterator<Socket> iter=Room.userscList().iterator();
						while(iter.hasNext()){
							Socket S = (Socket)iter.next();
							OutputStream o=S.getOutputStream();
							DataOutputStream d =new DataOutputStream(o);
							if(S != null){
								d.writeInt(100);
								d.flush();
								R.write(Integer.valueOf(str[1]),S);
								System.out.println("데이터 전송");
							}
						}
					}else if(str[0].equals("101")) {//자리이동
						System.out.println(msg);
						System.out.println("move to seek ");
						R.moveToSeeker(Integer.valueOf(str[1]), Integer.valueOf(str[2]), socket);
						
					}else if(str[0].equals("102")) {//자리이동
						System.out.println(msg);
						System.out.println("move to hide ");
						R.moveToHider(Integer.valueOf(str[1]), Integer.valueOf(str[2]), socket);
					}else if(str[0].equals("200")) {//방목록
						
						System.out.println(msg);
						System.out.println("room list ");
						String[] S=R.roomList();
						dos.writeInt(S.length);
						dos.flush();
						for(int i=0;i<S.length;i++) {
							dos.writeUTF(S[i]);
							dos.flush();
						}
						
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				removeClient(this);
				try {
					socket.close();
				} catch (Exception e) {
				}
			}
		}
		
	}
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

