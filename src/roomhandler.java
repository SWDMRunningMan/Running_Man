import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Vector;

public class roomhandler{
	private Vector<room> Room=new Vector<room>();
	private void createRoom(int id,SocketChannel sc,String userid) {
		room R=new room(id,sc,userid);
		Room.add(R);
	}
	//방생성
	private void deleteRoom(int id){
		int I=findRoom(id);
		Room.remove(I);
	}
	//방삭제
	private void enterRoom(int id,SocketChannel sc,String userid) {
		int I=findRoom(id);
		room R=Room.get(I);
		R.addUser(sc);
		R.addUserID(userid);
		Room.set(I,R);
	}
	//유저입장
	private void quitRoom(int id,SocketChannel sc) {
		int I=findRoom(id);
		room R=Room.get(I);
		int i=R.findUserS(sc);
		if(i!=-1)
			R.deleteSeeker(i);
		i=R.findUserH(sc);
		if(i!=-1)
			R.deleteHider(i);
		i=R.findUser(sc);
		if(i==-1)
			return;
		R.deleteUser(i);
		R.deleteUserID(i);
		R.deleteFeet(i);
		Room.set(I,R);
	}
	//유저퇴장
	private void setRoom(int id,int num,int numS,int numH, int time,int hint) {
		int I=findRoom(id);
		room R=Room.get(I);
		R.setNum(num);
		R.setNumS(numS);
		R.setNumH(numH);
		R.setTime(time);
		R.setHint(hint);
		Room.set(I,R);
	}
	//방설정
	private void broadcast(int id,ByteBuffer buffer) throws IOException {
		buffer.flip();
		int I=findRoom(id);
		Iterator<SocketChannel> iter=Room.get(I).userList().iterator();
		while(iter.hasNext()){
			SocketChannel sc = (SocketChannel)iter.next();
			if(sc != null){
				sc.write(buffer);
				buffer.rewind();
			}
		}
	}
	//브로드케스트
	private void sendSeeker(int id,ByteBuffer buffer) throws IOException {
		buffer.flip();
		int I=findRoom(id);
		Iterator<SocketChannel> iterS=Room.get(I).seekerList().iterator();
		while(iterS.hasNext()){
			SocketChannel sc = (SocketChannel)iterS.next();
			if(sc != null){
				sc.write(buffer);
				buffer.rewind();
			}
		}
	}
	private void sendHider(int id,ByteBuffer buffer) throws IOException {
		buffer.flip();
		int I=findRoom(id);
		Iterator<SocketChannel> iterH=Room.get(I).hiderList().iterator();
		while(iterH.hasNext()){
			SocketChannel sc = (SocketChannel)iterH.next();
			if(sc != null){
				sc.write(buffer);
				buffer.rewind();
			}
		}
	}
	private int findRoom(int id) {
		int I=-1;
		for(int i=0;i<Room.size();i++) {
			if(Room.get(i).getId()==id) {
				I=i;
				break;
			}
		}
		return I;
	}
	private int findRoomID(SocketChannel sc) {
		int I=-1;
		for(int i=0;i<Room.size();i++) {
			Vector<SocketChannel> user=Room.get(I).userList();
			for(int j=0;j<user.size();j++) {
				if(user.get(j)==sc) {
					I=Room.get(j).getId();
					break;
				}
			}
		}
		return I;
	}
	public int[] roomList() {
		int[] i ;
		if(Room.size()==0) {
			i=new int[0];
			return i;
		}
		i =new int[Room.size()];
		for(int j=0;j<i.length;j++) {
			i[j]=Room.get(j).getId();
		}
		return i;
	}
	//방찾기
	//게임진행 - 게임종료 - 결과전송
	public boolean make(SocketChannel sc,int id,int num,int numS,int numH, int time,int hint,String userid) {
		if(findRoom(id)==-1) {
			createRoom(id, sc, userid);
			setRoom(id,num,numS,numH,time,hint);
			return true;
		}
		else
			return false;
	}
	public boolean add(SocketChannel sc,int id,String userid) {
		if(findRoom(id)==-1) {
			return false;
		}else {
			enterRoom(id, sc, userid);
			return true;
		}
	}
	public boolean remove(SocketChannel sc) {
		int id=findRoomID(sc);
		if(id==-1)
			return false;
		if(findRoom(id)==-1) {
			return false;
		}else {
			quitRoom(id, sc);
			if(Room.get(findRoom(id)).userList().size()==0)
				deleteRoom(id);
			return true;
		}
	}
	public boolean gameStart(SocketChannel sc) throws IOException{
		int id=findRoomID(sc);
		if(id==-1)
			return false;
		if(findRoom(id)==-1) {
			return false;
		}else {
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			String msg="GS";
			buffer.put(msg.getBytes());
			broadcast(id, buffer);
			return true;
		}
	}
	public boolean gameOver(SocketChannel sc) throws IOException{
		int id=findRoomID(sc);
		if(id==-1)
			return false;
		if(findRoom(id)==-1) {
			return false;
		}else {
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			String msg="GO";
			buffer.put(msg.getBytes());
			broadcast(id, buffer);
			return true;
		}
	}
	public room inform(SocketChannel sc) {
		int id=findRoomID(sc);
		return Room.get(findRoom(id));
	}
	public boolean receiveHint(SocketChannel sc,ByteBuffer buffer) throws IOException {
		int id=findRoomID(sc);
		if(id==-1)
			return false;
		if(findRoom(id)==-1) {
			return false;
		}else {
			sendSeeker(id, buffer);
			return true;
		}
	}
	public boolean sendHint(SocketChannel sc) throws IOException{
		int id=findRoomID(sc);
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		String msg="NH";
		buffer.put(msg.getBytes());
		if(id==-1)
			return false;
		if(findRoom(id)==-1) {
			return false;
		}else {
			sendHider(id, buffer);
			return true;
		}
	}
	public boolean getFeet(SocketChannel sc,ByteBuffer buffer){
		int id=findRoomID(sc);
		if(id==-1)
			return false;
		if(findRoom(id)==-1) {
			return false;
		}else {
			int feet=buffer.getInt();
			int index=Room.get(findRoom(id)).findUser(sc);
			Room.get(findRoom(id)).addFeet(index,feet);
			return true;
		}
	}
	public boolean sendFeet(SocketChannel sc) throws IOException{
		ByteBuffer buffer;
		int id=findRoomID(sc);
		if(id==-1)
			return false;
		if(findRoom(id)==-1) {
			return false;
		}else {
			Vector<Integer>feet= Room.get(findRoom(id)).feetList();
			int[] feets=new int[feet.size()];
			for(int i=0;i<feets.length;i++) {
				feets[i]=feet.get(i);
			}
			buffer = ByteBuffer.allocateDirect(Integer.SIZE*feets.length);
			for(int i=0;i<feets.length;i++) {
				buffer.putInt(feets[i]);
			}
			broadcast(id, buffer);
			return true;
		}
	}
	public boolean moveToSeeker(SocketChannel sc) {
		int id=findRoomID(sc);
		if(id==-1)
			return false;
		if(findRoom(id)==-1) {
			return false;
		}else if(Room.get(findRoom(id)).getNumS()!=Room.get(findRoom(id)).seekerList().size()){
			int index=Room.get(findRoom(id)).findUserH(sc);
			if(index!=-1)	
				Room.get(findRoom(id)).deleteHider(index);
			Room.get(findRoom(id)).addSeeker(sc);
			return true;
		}else
			return false;
	}
	public boolean moveToHider(SocketChannel sc) {
		int id=findRoomID(sc);
		if(id==-1)
			return false;
		if(findRoom(id)==-1) {
			return false;
		}else if(Room.get(findRoom(id)).getNumH()!=Room.get(findRoom(id)).hiderList().size()){
			int index=Room.get(findRoom(id)).findUserS(sc);
			if(index!=-1)	
				Room.get(findRoom(id)).deleteSeeker(index);
			Room.get(findRoom(id)).addHider(sc);
			return true;
		}else
			return false;
	}
}
