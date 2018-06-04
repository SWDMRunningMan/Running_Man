import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util. ArrayList;

public class roomhandler{
	private  ArrayList<room> Room=new  ArrayList<room>();
	private void createRoom(int id,String name,int rid,String rname ,int n,int s,int t,int h,Socket sc) {
		room R=new room(id,name,rid ,rname,n, s, t, h,sc);
		Room.add(R);
	}
	//방생성
	private void deleteRoom(int rid){
		int I=findRoom(rid);
		Room.remove(I);
	}
	//방삭제
	private void enterRoom(int id,int rid) {
		int I=findRoom(rid);
		room R=Room.get(I);
		R.addUser(id);
		R.addHider(id);
		Room.set(I,R);
	}
	//유저입장
	private void quitRoom(int id,int rid) {
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
		R.deleteUsersc(i);
		Room.set(I,R);
	}
	//유저퇴장
	private void broadcast(int rid,String msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).userscList().iterator();
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
	
	//브로드케스트
	private void sendSeeker(int rid,String msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).seekerscList().iterator();
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
	private void sendSeeker(int rid,int msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).seekerscList().iterator();
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
	private void sendHider(int rid,String msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).hiderscList().iterator();
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
	private void sendHider(int rid,int msg) throws IOException {
		int I=findRoom(rid);
		OutputStream out;
		DataOutputStream dos;
		Iterator<Socket> iter=Room.get(I).hiderscList().iterator();
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
	private int findRoom(int rid) {
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
		String[] str ;
		if(Room.size()==0) {
			str=new String[0];
			return str;
		}
		str =new String[Room.size()];
		for(int j=0;j<str.length;j++) {
			str[j]=Room.get(j).getRId()+" "+Room.get(j).getRname()+" "+Room.get(j).userList().size()+" "+Room.get(j).getNum();
		}
		return str;
	}
	//방찾기
	//게임진행 - 게임종료 - 결과전송
	public boolean make(int id,String name,int rid,String rname ,int n,int s,int t,int h,Socket sc) {
		if(findRoom(rid)==-1) {
			createRoom(id,name,rid,rname ,n,s,t,h,sc);
			return true;
		}
		else
			return false;
	}
	public boolean add(int id,int rid) {
		if(findRoom(rid)==-1) {
			return false;
		}else {
			enterRoom(id,rid);
			return true;
		}
	}
	public boolean remove(int id,int rid) {
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
	public boolean gameStart(int rid) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			String msg="GS";
			broadcast(rid, msg);
			return true;
		}
	}
	public boolean gameOver(int rid) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			String msg="GO";
			broadcast(rid, msg);
			return true;
		}
	}
	public room inform(int rid) {
		int i=findRoom(rid);
		return Room.get(i);
	}
	public boolean receiveHint(int rid,String msg) throws IOException {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			sendSeeker(rid, msg);
			return true;
		}
	}
	public boolean receiveHint(int rid,int msg) throws IOException {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			sendSeeker(rid, msg);
			return true;
		}
	}
	public boolean sendHint(int rid,String msg) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			sendHider(rid, msg);
			return true;
		}
	}
	public boolean sendHint(int rid,int msg) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			sendHider(rid, msg);
			return true;
		}
	}
	public boolean setFeet(int rid,int sc,int feet){
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			int index=Room.get(i).findUser(sc);
			Room.get(i).addFeet(index,feet);
			return true;
		}
	}
	public boolean sendFeet(int rid) throws IOException{
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
	public synchronized boolean moveToSeeker(int rid,int id,Socket sc) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else if(Room.get(i).getNumS()!=Room.get(i).seekerList().size()){
			int index=Room.get(i).findUserH(id);
			if(index!=-1)	
				Room.get(i).deleteHider(index);
			Room.get(i).addSeeker(id);
			int j=Room.get(i).findUserS(id);
			Room.get(i).addSeekersc(j,sc);
			return true;
		}else
			return false;
	}
	public synchronized boolean moveToHider(int rid,int id,Socket sc) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else if(Room.get(i).getNumH()!=Room.get(i).hiderList().size()){
			int index=Room.get(i).findUserS(id);
			if(index!=-1)	
				Room.get(i).deleteSeeker(index);
			Room.get(i).addHider(id);
			int j=Room.get(i).findUserH(id);
			Room.get(i).addHidersc(j,sc);
			return true;
		}else
			return false;
	}
	public boolean update(int rid,int id,Socket sc) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else
			return false;
	}
	public boolean sitting(int rid,int id,Socket sc) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			int S=Room.get(i).getNumS();
			int H=Room.get(i).getNumH();
			if(Room.get(i).seekerList().size()<S) {
				Room.get(i).addSeeker(id);
				int j=Room.get(i).findUserS(id);
				Room.get(i).addSeekersc(j,sc);
				return true;
			}else if(Room.get(i).seekerList().size()<H){
				Room.get(i).addHider(id);
				int j=Room.get(i).findUserH(id);
				Room.get(i).addHidersc(j,sc);
				return true;
			}else {
				return false;
			}
		}
			
	}
	public boolean setSocket(int rid,int id,Socket sc) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			int u=Room.get(i).findUser(id);
			int s=Room.get(i).findUserS(id);
			int h=Room.get(i).findUserH(id);
			if(u!=-1)
				Room.get(i).setusersc(u, sc);
			if(s!=-1)
				Room.get(i).setseekersc(s, sc);
			if(h!=-1)
				Room.get(i).sethidersc(h, sc);
			return true;
		}
	}
	public boolean write(int rid,Socket sc) {
		Socket socket=sc;
		OutputStream out;
		DataOutputStream dos;
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			try {
				out =socket.getOutputStream();
				dos =new DataOutputStream(out);
				ArrayList<String> user=Room.get(i).userList();
				ArrayList<Integer> userid=Room.get(i).userIdList();
				ArrayList<Integer> feet=Room.get(i).feetList();
				ArrayList<Integer> seeker=Room.get(i).seekerList();
				ArrayList<Integer> hider=Room.get(i).hiderList();
				//인원수 seeker수 hider수
				dos.writeUTF(String.valueOf(user.size())+" "+String.valueOf(seeker.size())+" "+String.valueOf(hider.size()));
				dos.flush();
				//유저 목록 (이름 id 걸음수)
				for(int j=0;j<user.size();j++) {
					dos.writeUTF(user.get(j)+" "+String.valueOf(userid.get(j))+" "+String.valueOf(feet.get(j)));
					dos.flush();
				}
				for(int j=0;j<seeker.size();j++) {
					dos.writeUTF(String.valueOf(seeker.get(j)));
					dos.flush();
				}
				for(int j=0;j<hider.size();j++) {
					dos.writeUTF(String.valueOf(hider.get(j)));
					dos.flush();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		}
	}
}
