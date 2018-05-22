import java.net.Socket;
import java.util.ArrayList;

public class room {
	private  ArrayList<String> user=new  ArrayList<String>();
	private  ArrayList<Integer> userid=new  ArrayList<Integer>();
	private  ArrayList<Integer> feet=new  ArrayList<Integer>();
	private  ArrayList<Integer> seeker=new  ArrayList<Integer>();
	private ArrayList<Integer> hider=new ArrayList<Integer>();
	private Integer owner;
	private String ID;
	private int num,num_S,num_H;
	private int time;
	private int hint;
	public room(String name,int id,String rid ,int n,int s,int t,int h) {
		user.add(name);
		userid.add(id);
		ID=rid;
		owner=id;
		time=t;
		hint=h;
		num_S=n;
	}
	public String getRId() {
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
	public  ArrayList<String> userList() {
		return user;
	}
	public  ArrayList<Integer> userIdList() {
		return userid;
	}
	public Integer getOwner() {
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
	public void setOwner(int id) {
		owner=id;
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
	public void addSeeker(int id) {
		seeker.add(id);
	}
	public void addHider(int id) {
		hider.add(id);
	}
	public int findUser(int id) {
		int i=-1;
		i=user.indexOf(id);
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
}