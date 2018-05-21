import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class client {
	private static final String HOST = "localhost";
	private static final int PORT = 9090;
	private static FileHandler fileHandler;
	private static Logger logger = Logger.getLogger("app");
	
	private Selector selector = null;
	private SocketChannel sc = null;
	
	private Charset charset = null;
	private CharsetDecoder decoder = null;
	public client(){
		charset = Charset.forName("EUC-KR");
		decoder = charset.newDecoder();
	}
	
	public void initServer(){
		try{
			// 셀렉터를 연다
			selector = Selector.open();
			
			// 소켓 채널을 생성함
			sc = SocketChannel.open(new InetSocketAddress(HOST,PORT));
			// 비블록킹 모드로 설정함 
			sc.configureBlocking(false);
			
			// 서버 소켓 채널을 셀렉터에 등록함
			sc.register(selector, SelectionKey.OP_READ);
		}catch(IOException e){
			log(Level.WARNING,"SimpleChatClient.initServer()",e);
		}
	}
	
	
	private void startReader() {
		// TODO Auto-generated method stub
		info("Reader is started....");
		MyThread thread1=new MyThread();
		thread1.start();
	}
	private void read(SelectionKey key){
		// SelectionKey로 부터 소켓 채널을 얻어 옴
		SocketChannel sc = (SocketChannel)key.channel();
		// ByteBuffer를 생성함
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		int read = 0;
		
		try{
			// 요청한 클라이언트의 소켓 채널로 부터 데이터를 읽어 들임
			read = sc.read(buffer);
			info(read + " byte를 읽었습니다.");
		}catch(IOException e){
			try{
				sc.close();
			}catch(IOException e1){}
		}
		
		buffer.flip();
		String data = "";
		try{
			data = decoder.decode(buffer).toString();
		}catch(CharacterCodingException e){
			log(Level.WARNING,"SimpleChatClient.read()",e);
		}
		System.out.println("Message - " + data);
		
		// 버퍼 메모리를 해제함 
		clearBuffer(buffer);
	}
	private void write(String msg){
		/* 프로토콜
		boolean result=false;
		if(str[0].equals("toS")) {
		}
		// seeker 전환
		if(str[0].equals("toH")) {
		}
		// hider 전환
		if(str[0].equals("NH")) {
		}
		// 힌트 알람
		if(str[0].equals("SH")) {
		}
		// 힌트 전송
		if(str[0].equals("GS")) {
		}
		// 게임 시작
		if(str[0].equals("GO")) {
		}
		// 게임 종료
		if(str[0].equals("MR")) {
		}
		// 방 생성
		if(str[0].equals("SR")) {
		}
		// 방 검색
		if(str[0].equals("ER")) {
		}
		// 방 입장
		if(str[0].equals("QR")) {
		}
		// 방 퇴장
		 * */
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		try{
			buffer.clear();
			if(msg.equals("quit") || msg.equals("shutdown")){
				System.exit(0);
			}
			
			buffer.put(msg.getBytes());
			buffer.flip();
			
			sc.write(buffer);
		
		}catch(Exception e){
			log(Level.WARNING,"MyThread.run()",e);
		}finally{
			clearBuffer(buffer);
		}
	}
	
		private void clearBuffer(ByteBuffer buffer){
			if(buffer != null){
				buffer.clear();
				buffer = null;
			}
		}
	//----------------------------- Log part -------------------------//
	public void initLog(){
		try{
			fileHandler = new FileHandler("SimpleChatClient.log");
		} catch (IOException e) {}
		logger.addHandler(fileHandler);
		logger.setLevel(Level.ALL);
	}
	
	public void log(Level level, String msg, Throwable error){
		logger.log(level,msg,error);
	}
	
	public void info(String msg){
		logger.info(msg);
	}
	
	class MyThread extends Thread{
		public void run(){
			try{
				while(!this.isInterrupted()){
					info("요청을 가다리는 중.... ");
					// 셀렉터의 select() 메소드로 준비된 이벤트가 있는지 확인함
					selector.select();
					
					//셀렉터의 SelectedSet에 저장된 준비된 이벤트들 (SelectionKey 들)을
					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					while(it.hasNext()){
						SelectionKey key = (SelectionKey)it.next();
						if(key.isReadable()){
							// 이미 여결된 클라이언트가 메세지를 보낸 경우
							read(key);
						}
						// 이미 처리한 이벤트 이므로 반드시 삭제함
						it.remove();
					}
					
				}
			}catch(Exception e){
				log(Level.WARNING,"SimpleChatClient.startServer",e);
			}
		}
	}
	
	
	
	//---------------------- Main -----------------------//
	public static void main(String[] args){
		client scc = new client();
		scc.initLog();
		scc.initServer();
		scc.startReader();
	}
	
}
