import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class server {
	private static final String HOST = "192.9.88.20";
	private static final int PORT = 9090;
	
	private static FileHandler fileHandler;
	private static Logger  logger = Logger.getLogger("app");
	
	private Selector selector = null;
	private ServerSocketChannel ssc = null;
	private ServerSocket ssk = null;
	private Charset charset = Charset.forName("EUC-KR");
	private CharsetDecoder decoder = charset.newDecoder();
	private Vector<SocketChannel>  vector = new Vector<SocketChannel>();
	roomhandler R=new roomhandler();
	public void initSever(){
		try{
			//셀렉터 열기
			selector = Selector.open();
			
			// 서버 소켓 채널 생성
			ssc = ServerSocketChannel.open();
			
			// 비블록킹 모드로 설정
			ssc.configureBlocking(false);
			
			// 서버 소켓 채널과 연결된 서버 소켓 가져 옴
			ssk = ssc.socket();
			
			// 주어진 파라미터에 해당하는 주소임. 포트로 서버 소켓일 바인드함
			InetSocketAddress isa = new InetSocketAddress(HOST,PORT);
			ssk.bind(isa);
			
			// 서버 소켓 채널을 셀렉터에 등록함
			ssc.register(selector, SelectionKey.OP_ACCEPT);
		} catch(IOException e){
			log(Level.WARNING, "SimpleChatServer.initServer()",e);
		}
	}
	
	public void startServer(){
		info("Server is started...");
		try{
			while(true){
				info("요청을 기다리는 중..");
				// 셀렉터의 select() 메소드로 준비된 이벤트가 있는지 확인함
				selector.select();
				
				// 셀렉터의 SelectedSet에 저장된 준비된 이벤트 들이 (SelectionKey들)을 하나씩 처리함
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey key = (SelectionKey) it.next();
					if(key.isAcceptable()){
						// 서버 소켓 채널에 클라이언트가 접속을 시도한 경우
						accpet(key);
					} else if(key.isReadable()){
						//이미 연결된 클라이언트가 메시지를 보낸 경우
						read(key);
					}
					// 이미 처리한 이벤트 이므로 반드시 삭제함
					it.remove();
				}
			}
		} catch (Exception e) {
			log(Level.WARNING,"SimpleChatServer.startServer()",e);
		}
	}
	
	private void accpet(SelectionKey key) {
		ServerSocketChannel server = (ServerSocketChannel)key.channel();
		SocketChannel sc;
		try{
			// 서버 소켓 채널의 accept() 로 서버 소켓을 생성함
			sc = server.accept();
			// 생성된 소켓채널을 비블록킹과 읽기 모드로 셀렉터에 등록함
			registerChannel(selector,sc,SelectionKey.OP_READ);
			info(sc.toString() + " 클라이언트가 접속 했습니다.");
		
		} catch(ClosedChannelException e){
			log(Level.WARNING,"SimpleChatServer.accept()",e);
		}catch(IOException e){
			log(Level.WARNING,"SimpleChatServer.accept()",e);
		}
	}

	private void registerChannel(Selector selector, SocketChannel sc,int ops) throws ClosedChannelException, IOException {
		if(sc == null){
			info("Invalid  Connection");
			return;
		}
		sc.configureBlocking(false);
		sc.register(selector, ops);
		// 채팅방에 추가함
		addUser(sc);
	}
	
	
	private void read(SelectionKey key) throws IOException{
		// SelectionKey로부터 소켓 채널을 얻어옴
		SocketChannel sc = (SocketChannel)key.channel();
		// ByteBuffer를 생성함
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		try{
			// 요청한 클라이언트의 소켓채널로부터 데이터를 읽어 들임
			int read = sc.read(buffer);
			info(read + " byte를 읽었습니다.");
		}catch(IOException e){
			try{
				sc.close();
			}catch(IOException e1){}
			removeUser(sc);
			info(sc.toString() + " 클라이언트가 접속을 해제했습니다.");
		}
		protocol(sc,buffer);
		/*
		try{
			// 클라이언트가 보낸 메시지를 채팅방 안에서 모든 사용자에게 브로드 캐스트 해줌
			broadcast(buffer);
			
		}catch(IOException e){
			log(Level.WARNING, "SimpleChatServer.read()",e);
		}
		*/
		// 버퍼 메모리를 해제함
		clearBuffer(buffer);
	}
	private void protocol(SocketChannel sc,ByteBuffer buffer) throws IOException{
		String msg = "";
		try{
			msg = decoder.decode(buffer).toString();
		}catch(CharacterCodingException e){
			log(Level.WARNING,"SimpleChatServer.protocol()",e);
		}
		System.out.println(msg);
		String[] str=msg.split(" ");
		// 프로토콜
		boolean result=false;
		if(str[0].equals("toS")) {
			result=R.moveToSeeker(sc);
		}
		// seeker 전환
		if(str[0].equals("toH")) {
			result=R.moveToHider(sc);
		}
		// hider 전환
		if(str[0].equals("NH")) {
			result=R.sendHint(sc);
		}
		// 힌트 알람
		if(str[0].equals("SH")) {
			msg=msg.substring(3, msg.length());
			ByteBuffer Buffer = ByteBuffer.allocateDirect(1024);
			Buffer.put(msg.getBytes());
			result=R.receiveHint(sc,Buffer);
		}
		// 힌트 전송
		if(str[0].equals("GS")) {
			result=R.gameStart(sc);
		}
		// 게임 시작
		if(str[0].equals("GO")) {
			result=R.gameOver(sc);
		}
		// 게임 종료
		if(str[0].equals("MR")) {
			result=R.make(sc, Integer.valueOf(str[1]), Integer.valueOf(str[2]), Integer.valueOf(str[3]), Integer.valueOf(str[4]), Integer.valueOf(str[5]), Integer.valueOf(str[6]), str[7]);
		}
		// 방 생성
		if(str[0].equals("SR")) {
			int[] index=R.roomList();
			if(index.length==0) {
				ByteBuffer Buffer = ByteBuffer.allocateDirect(1024);
				Buffer.putInt(-1);
				sc.write(Buffer);
				Buffer.rewind();
				return;
			}else {
				ByteBuffer Buffer = ByteBuffer.allocateDirect(Integer.SIZE*index.length);
				Buffer.put("SR ".getBytes());
				for(int i=0;i<index.length;i++) {
					Buffer.putInt(index[i]);
				}
				sc.write(Buffer);
				Buffer.rewind();
				return;
			}
		}
		// 방 검색
		if(str[0].equals("ER")) {
			result=R.add(sc, Integer.valueOf(str[1]), str[2]);
		}
		// 방 입장
		if(str[0].equals("QR")) {
			result=R.remove(sc);
		}
		// 방 퇴장
		ByteBuffer Buffer = ByteBuffer.allocateDirect(1024);
		if(result==true)
			Buffer.putInt(1);
		else
			Buffer.putInt(-1);
		sc.write(Buffer);
		Buffer.rewind();
	}
	
	private void clearBuffer(ByteBuffer buffer) {
		if(buffer != null){
			buffer.clear();
			buffer = null;
		}
	}

	private void addUser(SocketChannel sc) {
		vector.add(sc);
		
	}
	
	private void removeUser(SocketChannel sc) {
		vector.remove(sc);
	}

	


	//----------------- Log part ------------------------//
	public void initLog(){
		try{
			fileHandler = new FileHandler("SimpleChatServer.log");
		}catch(IOException ee){
			logger.addHandler(fileHandler);
			logger.setLevel(Level.ALL);
		}
	}
	
	public void log(Level level, String msg, Throwable error){
		logger.log(level, msg, error);
	}
	
	public void info(String msg){
		logger.info(msg);
	}
	//------------ Main -------------------------//
	public static void main(String[] args){
		server scs = new server();
		
		scs.initLog();
		scs.initSever();
		scs.startServer();		
	}
}
