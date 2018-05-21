import java.nio.channels.SocketChannel;
import java.util.Vector;

public class room {
	private Vector<SocketChannel> user=new Vector<SocketChannel>();
	private Vector<String> userID=new Vector<String>();
	private Vector<Integer> feet=new Vector<Integer>();
	private Vector<SocketChannel> seeker=new Vector<SocketChannel>();
	private Vector<SocketChannel> hider=new Vector<SocketChannel>();
	private SocketChannel owner;
	private int ID;
	private int num,num_S,num_H;
	private int time;
	private int hint;
	public room(int id,SocketChannel sc,String userid) {
		user.add(sc);
		userID.add(userid);
		ID=id;
		owner=sc;
	}
	public int getId() {
		return ID;
	}
	public int getNum() {
		return num;
	}
	public int getNumS() {
		return num_S;
	}
	public int getNumH() {
		return num_H;
	}
	public int getTime() {
		return time;
	}
	public int getHint() {
		return hint;
	}
	public Vector<SocketChannel> userList() {
		return user;
	}
	public SocketChannel getOwner() {
		return owner;
	}
	public Vector<String> userIDList() {
		return userID;
	}
	public Vector<Integer> feetList() {
		return feet;
	}
	public Vector<SocketChannel> seekerList() {
		return seeker;
	}
	public Vector<SocketChannel> hiderList() {
		return hider;
	}
	public void setOwner(SocketChannel sc) {
		owner=sc;
	}
	public void setNum(int n) {
		num=n;
	}
	public void setNumS(int n) {
		num_S=n;
	}
	public void setNumH(int n) {
		num_H=n;
	}
	public void setTime(int n) {
		time=n;
	}
	public void setHint(int n) {
		hint=n;
	}
	public void addUser(SocketChannel sc) {
		user.add(sc);
	}
	public void addUserID(String S) {
		userID.add(S);
	}
	public void addFeet(int i,int f) {
		feet.add(i, f);
	}
	public void addSeeker(SocketChannel sc) {
		seeker.add(sc);
	}
	public void addHider(SocketChannel sc) {
		hider.add(sc);
	}
	public int findUser(SocketChannel sc) {
		int i=-1;
		i=user.indexOf(sc);
		return i;
	}
	public int findUserS(SocketChannel sc) {
		int i=-1;
		i=seeker.indexOf(sc);
		return i;
	}
	public int findUserH(SocketChannel sc) {
		int i=-1;
		i=hider.indexOf(sc);
		return i;
	}
	public void deleteUser(int i) {
		user.remove(i);
	}
	public void deleteUserID(int i) {
		userID.remove(i);
	}
	public void deleteFeet(int i) {
		feet.remove(i);
	}
	public void deleteSeeker(int i) {
		seeker.remove(i);
	}
	public void deleteHider(int i) {
		hider.remove(i);
	}
}