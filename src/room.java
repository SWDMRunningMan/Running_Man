import java.net.Socket;
import java.util.ArrayList;

public class room {
	private  ArrayList<String> user=new  ArrayList<String>();
	private  ArrayList<Socket> usersc=new  ArrayList<Socket>();
	private  ArrayList<Integer> userid=new  ArrayList<Integer>();
	private  ArrayList<Integer> feet=new  ArrayList<Integer>();
	private  ArrayList<Integer> seeker=new  ArrayList<Integer>();
	private ArrayList<Integer> hider=new ArrayList<Integer>();
	private  ArrayList<Socket> seekersc=new  ArrayList<Socket>();
	private ArrayList<Socket> hidersc=new ArrayList<Socket>();
	private String owner;
	private int ID;
	private String name;
	private int num,num_S,num_H;
	private int time;
	private int hint;
	public room(int id,String Name,int rid,String rname ,int n,int s,int t,int h,Socket sc) {
		user.add(Name);
		userid.add(id);
		usersc.add(sc);
		ID=rid;
		name=rname;
		owner=Name;
		time=t;
		hint=h;
		num=n;
		num_S=s;
		num_H=num-num_S;
		feet.add(0);
	}
	public int getRId() {
		return ID;
	}
	public String getRname() {
		return name;
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
	public  ArrayList<String> userList() {
		return user;
	}
	public  ArrayList<Socket> userscList() {
		return usersc;
	}
	public  ArrayList<Integer> userIdList() {
		return userid;
	}
	public String getOwner() {
		return owner;
	}
	public  ArrayList<Integer> feetList() {
		return feet;
	}
	public  ArrayList<Integer> seekerList() {
		return seeker;
	}
	public  ArrayList<Integer> hiderList() {
		return hider;
	}
	public  ArrayList<Socket> seekerscList() {
		return seekersc;
	}
	public  ArrayList<Socket> hiderscList() {
		return hidersc;
	}
	public void setOwner(String name) {
		owner=name;
	}
	public void setusersc(int i,Socket sc) {
		usersc.set(i,sc);
	}
	public void setseekersc(int i,Socket sc) {
		seekersc.set(i,sc);
	}
	public void sethidersc(int i,Socket sc) {
		hidersc.set(i,sc);
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
	public void addUser(String name) {
		user.add(name);
	}
	public void addUser(int id) {
		userid.add(id);
	}
	public void addFeet(int i,int f) {
		feet.add(i, f);
	}
	public void addusersc(int i,Socket s) {
		usersc.add(i, s);
	}
	public void addSeeker(int id) {
		seeker.add(id);
	}
	public void addHider(int id) {
		hider.add(id);
	}
	public void addSeekersc(int i,Socket s) {
		seekersc.add(i,s);
	}
	public void addHidersc(int i,Socket s) {
		hidersc.add(i,s);
	}
	public int findUser(int id) {
		int i=-1;
		i=userid.indexOf(id);
		return i;
	}
	public int findUserS(int id) {
		int i=-1;
		i=seeker.indexOf(id);
		return i;
	}
	public int findUserH(int id) {
		int i=-1;
		i=hider.indexOf(id);
		return i;
	}
	public void deleteUser(int i) {
		user.remove(i);
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
	public void deleteUsersc(int i) {
		usersc.remove(i);
	}
}