package 화일처리;
public class BNode {
	private int key[];
	private BNode child[];
	private int numKey;
	public int getKey(int i) {
		return key[i];
	}
	public int getNumKey() {
		return this.numKey;
	}
	//노드의 키의 개수가 m-1일때(full) 삽입명령이 일어나면
	//일단 삽입 후 split하는 방식으로 구현하기 위해서 
	//key와 child 배열을 이론값보다 각각 1씩 크게 정의함.e
	
	public BNode(int m) {
		key=new int[m]; 
		child=new BNode[m+1];
		numKey=0;
		
	}
	public void makeChild(int i,int m) {
		this.child[i]=new BNode(m);
	}
	public BNode getChild(int i) {
		return this.child[i];
	}
	public void setChild(BNode n,int i) {	
		this.child[i]=n;
	}
	public void setKey(int i, int key) {
		this.key[i]=key;
	}
	public void increaseNumKey() {
		this.numKey++;
	}
	public void decreaseNumKey() {
		this.numKey--;
	}
	public void copyChild(BNode n) {
		for(int i=0;i<n.getNumKey();i++) {
			this.key[i]=n.getKey(i);
			this.child[i]=n.getChild(i);
		}
		this.child[n.getNumKey()]=n.getChild(n.getNumKey());
	}
	public int getMiddleKey() {
		return key[numKey/2];
	}	
	//해당 노드의 키가 m-1개(full node)인지 bool값을 리턴하는 함수.
	public boolean isFullNode(int m) {
		return numKey==m-1?true:false;
	}
	//해당 노드가 리프노드인지 bool값을 리턴하는 함수.
	public boolean hasNoChild() {
		return numKey>0?false:true;
	}
	public int findPos(int key) {
		int i;
		for(i=0;key!=this.getKey(i);i++);
		return i;
	}
}
