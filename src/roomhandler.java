import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util. ArrayList;

public class roomhandler{
	private  ArrayList<room> Room=new  ArrayList<room>();
	private void createRoom(String name,int id,String rid ,int n,int s,int t,int h) {
		room R=new room(name,id,rid ,n, s, t, h);
		Room.add(R);
	}
	//방생성
	private void deleteRoom(String rid){
		int I=findRoom(rid);
		Room.remove(I);
	}
	//방삭제
	private void enterRoom(int id,String rid) {
		int I=findRoom(rid);
		room R=Room.get(I);
		R.addUser(id);
		R.addHider(id);
		Room.set(I,R);
	}
	//유저입장
	private void quitRoom(int id,String rid) {
		int I=findRoom(rid);
		room R=Room.get(I);
		int i=R.findUserS(id);
		if(i!=-1)
			R.deleteSeeker(i);
		i=R.findUserH(id);
		if(i!=-1)
			R.deleteHider(i);
		i=R.findUser(id);
		if(i==-1)
			return;
		R.deleteUser(i);
		R.deleteFeet(i);
		Room.set(I,R);
	}
	//유저퇴장
/*
	private void broadcast(String rid,int msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).userList().iterator();
		while(iter.hasNext()){
			Socket socket = (Socket)iter.next();
			out=socket.getOutputStream();
			dos =new DataOutputStream(out);
			if(socket != null){
				dos.writeInt(msg);
				System.out.println("데이터 전송");
				dos.close();
			}
		}
	}
	//브로드케스트
	private void sendSeeker(String rid,String msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).seekerList().iterator();
		while(iter.hasNext()){
			Socket socket = (Socket)iter.next();
			out=socket.getOutputStream();
			dos =new DataOutputStream(out);
			if(socket != null){
				dos.writeUTF(msg);
				System.out.println("데이터 전송");
				dos.close();
			}
		}
	}
	private void sendSeeker(String rid,int msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).seekerList().iterator();
		while(iter.hasNext()){
			Socket socket = (Socket)iter.next();
			out=socket.getOutputStream();
			dos =new DataOutputStream(out);
			if(socket != null){
				dos.writeInt(msg);
				System.out.println("데이터 전송");
				dos.close();
			}
		}
	}
	private void sendHider(String rid,String msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).hiderList().iterator();
		while(iter.hasNext()){
			Socket socket = (Socket)iter.next();
			out=socket.getOutputStream();
			dos =new DataOutputStream(out);
			if(socket != null){
				dos.writeUTF(msg);
				System.out.println("데이터 전송");
				dos.close();
			}
		}
	}
	private void sendHider(String rid,int msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).hiderList().iterator();
		while(iter.hasNext()){
			Socket socket = (Socket)iter.next();
			out=socket.getOutputStream();
			dos =new DataOutputStream(out);
			if(socket != null){
				dos.writeInt(msg);
				System.out.println("데이터 전송");
				dos.close();
			}
		}
	}
	*/
	private int findRoom(String rid) {
		int I=-1;
		for(int i=0;i<Room.size();i++) {
			if(Room.get(i).getRId()==rid) {
				I=i;
				break;
			}
		}
		return I;
	}
	public String[] roomList() {
		String[] i ;
		if(Room.size()==0) {
			i=new String[0];
			return i;
		}
		i =new String[Room.size()];
		for(int j=0;j<i.length;j++) {
			i[j]=Room.get(j).getRId();
		}
		return i;
	}
	//방찾기
	//게임진행 - 게임종료 - 결과전송
	public boolean make(String name,int id,String rid ,int n,int s,int t,int h) {
		if(findRoom(rid)==-1) {
			createRoom(name,id,rid , n,s, t, h);
			return true;
		}
		else
			return false;
	}
	public boolean add(int id,String rid) {
		if(findRoom(rid)==-1) {
			return false;
		}else {
			enterRoom(id,rid);
			return true;
		}
	}
	public boolean remove(int id,String rid) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			quitRoom(id,rid);
			if(Room.get(i).userList().size()==0)
				deleteRoom(rid);
			return true;
		}
	}
	public boolean gameStart(String rid) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			String msg="GS";
			//broadcast(rid, msg);
			return true;
		}
	}
	public boolean gameOver(String rid) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			String msg="GO";
			//broadcast(rid, msg);
			return true;
		}
	}
	public room inform(String rid) {
		int i=findRoom(rid);
		return Room.get(i);
	}
	public boolean receiveHint(String rid,String msg) throws IOException {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			//sendSeeker(rid, msg);
			return true;
		}
	}
	public boolean receiveHint(String rid,int msg) throws IOException {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			//sendSeeker(rid, msg);
			return true;
		}
	}
	public boolean sendHint(String rid,String msg) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			//sendHider(rid, msg);
			return true;
		}
	}
	public boolean sendHint(String rid,int msg) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			//sendHider(rid, msg);
			return true;
		}
	}
	public boolean setFeet(String rid,int sc,int feet){
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			int index=Room.get(i).findUser(sc);
			Room.get(i).addFeet(index,feet);
			return true;
		}
	}
	public boolean sendFeet(String rid) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			 ArrayList<Integer>feet= Room.get(i).feetList();
			for(int j=0;j<feet.size();j++) {
				//broadcast(rid, feet.get(j));
			}
			return true;
		}
	}
	public boolean moveToSeeker(String rid,int id) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else if(Room.get(i).getNumS()!=Room.get(i).seekerList().size()){
			int index=Room.get(i).findUserH(id);
			if(index!=-1)	
				Room.get(i).deleteHider(index);
			Room.get(i).addSeeker(id);
			return true;
		}else
			return false;
	}
	public boolean moveToHider(String rid,int id) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else if(Room.get(i).getNumH()!=Room.get(i).hiderList().size()){
			int index=Room.get(i).findUserS(id);
			if(index!=-1)	
				Room.get(i).deleteSeeker(index);
			Room.get(i).addHider(id);
			return true;
		}else
			return false;
	}
}
