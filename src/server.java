package server;
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
	Vector<Socket> user;
	roomhandler R=new roomhandler();
	public server() throws Exception {
		System.out.println("The server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		//plus user handler stack
 		vc = new Vector<Handler>();
		try {
			while (true) {
				Socket s=listener.accept();
				Handler hd = new Handler(s);
				hd.start();
				System.out.println("서버연결");
				vc.add(hd);	
				//user.add(s);
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
		R.user.remove(hd.socket);
		//user.remove(hd.socket);
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
			R.user.add(sc);
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
							if(str.length==3) {
								R.setSocket(Integer.valueOf(str[2]),str[1],socket);
							}
							dos.writeUTF(str[1]);
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
						R.make( str[1],str[2],Integer.valueOf(str[3]),str[4],Integer.valueOf(str[5]),Integer.valueOf(str[6]),Integer.valueOf(str[7]),Integer.valueOf(str[8]),socket);
						R.sitting(Integer.valueOf(str[3]),str[2], str[1], socket);
						Iterator<Socket> iter=R.user.iterator();
						while(iter.hasNext()){
							Socket S = (Socket)iter.next();
							OutputStream o=S.getOutputStream();
							DataOutputStream d =new DataOutputStream(o);
							if(S != null){
								d.writeInt(11);
								d.flush();
								System.out.println("데이터 전송");
							}
						}
						System.out.println("방생성 "+ str[1]);
					}else if(str[0].equals("100")) {//방정보
						System.out.println(msg);
						System.out.println("broad cast ");
						
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
						R.moveToSeeker(Integer.valueOf(str[1]),str[2], socket,str[3]);
						
					}else if(str[0].equals("102")) {//자리이동
						System.out.println(msg);
						System.out.println("move to hide ");
						R.moveToHider(Integer.valueOf(str[1]), str[2], socket,str[3]);
					}else if(str[0].equals("103")) {//나감
						System.out.println(msg);
						System.out.println("exit ");
						R.remove(Integer.valueOf(str[1]), str[2],socket);
						dos.writeInt(11);
					}
					else if(str[0].equals("104")) {//방접속
		                  System.out.println(msg);
		                  System.out.println("enter ");
		                  room Room= R.inform(Integer.valueOf(str[1]));
		                  if(Room.getNum()<=Room.userList().size()) {
		                     dos.writeInt(-1);
		                     dos.flush();
		                  }else{
							R.add(str[2], str[3],Integer.valueOf(str[1]), socket);
							dos.writeInt(1);
							dos.flush();
							dos.writeUTF(Room.getOwner());
							dos.flush();
							dos.writeInt(Room.getNum());
							dos.flush();
							dos.writeInt(Room.getNumS());
							dos.flush();
							dos.writeInt(Room.getTime());
							dos.flush();
							dos.writeInt(Room.getHint());
							dos.flush();
							
						}
					}
					else if(str[0].equals("200")) {//방목록
						
						System.out.println(msg);
						System.out.println("room list ");
						String[] S=R.roomList();
						
						dos.writeInt(S.length);
						dos.flush();
						for(int i=0;i<S.length;i++) {
							dos.writeUTF(S[i]);
							dos.flush();
						}
						
					}else if(str[0].equals("300")) {//게임시작
						
						System.out.println(msg);
						System.out.println("game start ");
						room Room= R.inform(Integer.valueOf(str[1]));
						Iterator<Socket> iter=Room.userscList().iterator();
						while(iter.hasNext()){
							Socket S = (Socket)iter.next();
							OutputStream o=S.getOutputStream();
							DataOutputStream d =new DataOutputStream(o);
							if(S != null){
								d.writeInt(300);
								d.flush();
								System.out.println("게임시작");
							}
						}
						R.gameStart(Integer.valueOf(str[1]));
					}else if(str[0].equals("400")) {//사진전송      400 rID ID 크기 rotated
						//System.out.println(msg);
						System.out.println("picture send ");
						room Room= R.inform(Integer.valueOf(str[1]));
						int size=Integer.valueOf(str[3]);
						System.out.println(size);
						System.out.println(str[2]);
						byte[] data=new byte[size];
						for(int i=0;i<size;i++) {
							data[i]=dis.readByte();
						}
						Iterator<Socket> iter=Room.userscList().iterator();
						while(iter.hasNext()){
							Socket S = (Socket)iter.next();
							OutputStream o=S.getOutputStream();
							DataOutputStream d =new DataOutputStream(o);
							if(S != null){
								d.writeInt(400);
								d.flush();
								d.writeUTF(str[2]);
								d.flush();
								d.writeInt(size);
								d.flush();
								for(int i=0;i<size;i++) {
									d.writeByte(data[i]);
									d.flush();
								}
								System.out.println("사진 전송");
							}
						}
					}else if(str[0].equals("401")) {//잡힌거 전송 401 rID ID 
						System.out.println(msg);
						System.out.println("out send ");
						room Room= R.inform(Integer.valueOf(str[1]));
						Iterator<Socket> iter=Room.userscList().iterator();
						while(iter.hasNext()){
							Socket S = (Socket)iter.next();
							OutputStream o=S.getOutputStream();
							DataOutputStream d =new DataOutputStream(o);
							if(S != null){
								System.out.println("401 : " + str[2]);
								d.writeInt(401);
								d.flush();
								d.writeUTF(str[2]);
								d.flush();

								System.out.println("아웃");
							}
						}
					}else if(str[0].equals("500")) {//게임끝
						System.out.println(msg);
						System.out.println("game end ");
						room Room= R.inform(Integer.valueOf(str[1]));
						Iterator<Socket> iter=Room.userscList().iterator();
							while(iter.hasNext()){
								Socket S = (Socket)iter.next();
								OutputStream o=S.getOutputStream();
								DataOutputStream d =new DataOutputStream(o);
								if(S != null){
									d.writeInt(500);
									d.flush();
								System.out.println("게임 종료");
								}
							}
						}
					else if(str[0].equals("501")) {//걸음수 전달
						System.out.println(msg);
						System.out.println("game end ");
			
						
						int feet=dis.readInt();
						R.setFeet(Integer.valueOf(str[1]),str[2],feet);
		                  room Room= R.inform(Integer.valueOf(str[1]));
		                  int size=Room.userList().size();
		                  int count=Room.count;
		                 System.out.println("count: " + count + " size : " + size);
						if(count==size) {
						Iterator<Socket> iter=Room.userscList().iterator();
							while(iter.hasNext()){
								System.out.println("count: " + count);
								Socket S = (Socket)iter.next();
								OutputStream o=S.getOutputStream();
								DataOutputStream d =new DataOutputStream(o);
								if(S != null){
									d.writeInt(501);
									d.flush();
									d.writeInt(size);
									System.out.println(size);
									d.flush();
  								    for(int i=0;i<size;i++) {
										d.writeInt(Room.feetList().get(i));
									}
  								    
								System.out.println("데이터 전송");
								}
							}
							R.gameOver(Integer.valueOf(str[1]));
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

	
	public static void main(String[] args) throws Exception {
		//start server
		new server();
	}
}

