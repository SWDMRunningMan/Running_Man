import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util. ArrayList;

public class roomhandler{
	private ArrayList<room> Room=new  ArrayList<room>();
	public ArrayList<Socket> user=new ArrayList<Socket>();
	private void createRoom(String id,String name,int rid,String rname ,int n,int s,int t,int h,Socket sc) {
		room R=new room(id,name,rid ,rname,n, s, t, h,sc);
		Room.add(R);
	}
	//방생성
	private void deleteRoom(int rid){
		int I=findRoom(rid);
		Room.remove(I);
	}
	//방삭제
	private boolean enterRoom(String id,String name,int rid,Socket sc) {
		int I=findRoom(rid);
		boolean b=sitting(rid,name, id, sc);
		if(b==true) {
			Room.get(I).addUser(name);
			Room.get(I).addUserid(id);
			Room.get(I).addusersc(sc);
			Room.get(I).addFeet(0);
		}
		return b;
	}
	//유저입장
	private void quitRoom(String id,int rid,Socket sc) {
		int I=findRoom(rid);
		room R=Room.get(I);
		int i=R.findUserS(id);
		if(i!=-1) {
			Room.get(I).deleteSeeker(i);
			Room.get(I).deleteSeekersc(i);
			Room.get(I).deleteSeeker2(i);
		}
		i=R.findUserH(id);
		if(i!=-1) {
			Room.get(I).deleteHider(i);
			Room.get(I).deleteHidersc(i);
			Room.get(I).deleteHider2(i);
		}
		i=R.findUser(id);
		if(i==-1)
			return;
		Room.get(I).deleteUser(i);
		Room.get(I).deleteFeet(i);
		Room.get(I).deleteUsersc(i);
		Room.get(I).deleteUserid(i);
	}
	//유저퇴장
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
		int c=0,n=0;
		for(int j=0;j<Room.size();j++) {
			if(Room.get(j).isStart==0)
				c++;
		}
		str =new String[c];
		for(int j=0;j<str.length;j++) {
			if(Room.get(j).isStart==0) {
				str[n]=Room.get(j).getRId()+" "+Room.get(j).getRname()+" "+Room.get(j).userList().size()+" "+Room.get(j).getNum();
				n++;
			}
		}
		return str;
	}
	//방찾기
	//게임진행 - 게임종료 - 결과전송
	public boolean make(String id,String name,int rid,String rname ,int n,int s,int t,int h,Socket sc) {
		if(findRoom(rid)==-1) {
			createRoom(id,name,rid,rname ,n,s,t,h,sc);
			return true;
		}
		else
			return false;
	}
	public boolean add(String id,String name,int rid,Socket sc) {
		if(findRoom(rid)==-1) {
			return false;
		}else {
			return enterRoom(id,name,rid,sc);
		}
	}
	public boolean remove(int rid,String id,Socket sc) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			quitRoom(id,rid,sc);
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
			Room.get(i).isStart=1;
			return true;
		}
	}
	public boolean gameOver(int rid) throws IOException{
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			Room.remove(i);
			return true;
		}
	}
	public room inform(int rid) {
		int i=findRoom(rid);
		System.out.println(i);
		return Room.get(i);
	}
	public boolean setFeet(int rid,String id,int feet){
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			int index=Room.get(i).findUser(id);
			Room.get(i).setfeet(index,feet);
			Room.get(i).count++;
			return true;
		}
	}
	public synchronized boolean moveToSeeker(int rid,String id,Socket sc,String name) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else if(Room.get(i).getNumS()!=Room.get(i).seekerList().size()){
			int index=Room.get(i).findUserH(id);
			if(index!=-1) {
				Room.get(i).deleteHider(index);
				Room.get(i).deleteHider2(index);
			}
			Room.get(i).addSeeker(id);
			Room.get(i).addSeeker2(name);
			int j=Room.get(i).findUserS(id);
			Room.get(i).addSeekersc(j,sc);
			return true;
		}else
			return false;
	}
	public synchronized boolean moveToHider(int rid,String id,Socket sc,String name) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else if(Room.get(i).getNumH()!=Room.get(i).hiderList().size()){
			int index=Room.get(i).findUserS(id);
			if(index!=-1)	{
				Room.get(i).deleteSeeker(index);Room.get(i).deleteSeeker2(index);
			}
			Room.get(i).addHider(id);
			Room.get(i).addHider2(name);
			int j=Room.get(i).findUserH(id);
			Room.get(i).addHidersc(j,sc);
			return true;
		}else
			return false;
	}
	public boolean sitting(int rid,String name,String id,Socket sc) {
		int i=findRoom(rid);
		if(i==-1) {
			return false;
		}else {
			int S=Room.get(i).getNumS();
			int H=Room.get(i).getNumH();
			if(Room.get(i).seekerList().size()<S) {
				Room.get(i).addSeeker(id);
				Room.get(i).addSeeker2(name);
				int j=Room.get(i).findUserS(id);
				Room.get(i).addSeekersc(j,sc);
				return true;
			}else if(Room.get(i).hiderList().size()<H){
				Room.get(i).addHider(id);
				Room.get(i).addHider2(name);
				int j=Room.get(i).findUserH(id);
				Room.get(i).addHidersc(j,sc);
				return true;
			}else {
				return false;
			}
		}
			
	}
	public boolean setSocket(int rid,String id,Socket sc) {
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
				ArrayList<String> userid=Room.get(i).userIdList();
				ArrayList<Integer> feet=Room.get(i).feetList();
				ArrayList<String> seeker=Room.get(i).seekerList();
				ArrayList<String> hider=Room.get(i).hiderList();
				ArrayList<String> seeker2=Room.get(i).seeker2List();
				ArrayList<String> hider2=Room.get(i).hider2List();
				String time=String.valueOf(Room.get(i).getTime());
				String chance = String.valueOf(Room.get(i).getHint());
				//인원수 seeker수 hider수
				dos.writeUTF(String.valueOf(user.size())+" "+String.valueOf(seeker.size())+" "+String.valueOf(hider.size()) + " " + time + " " + chance);
				dos.flush();
				//유저 목록 (이름 id 걸음수)
				for(int j=0;j<user.size();j++) {
					dos.writeUTF(user.get(j)+" "+userid.get(j)+" "+String.valueOf(feet.get(j)));
					dos.flush();
				}
				for(int j=0;j<seeker.size();j++) {
					//dos.writeUTF(seeker.get(j));
					dos.writeUTF(seeker.get(j)+" "+seeker2.get(j));
					dos.flush();
				}
				for(int j=0;j<hider.size();j++) {
					//dos.writeUTF(hider.get(j));
					dos.writeUTF(hider.get(j)+" "+hider2.get(j));
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
