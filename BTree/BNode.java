package ȭ��ó��;
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
	//����� Ű�� ������ m-1�϶�(full) ���Ը���� �Ͼ��
	//�ϴ� ���� �� split�ϴ� ������� �����ϱ� ���ؼ� 
	//key�� child �迭�� �̷а����� ���� 1�� ũ�� ������.e
	
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
	//�ش� ����� Ű�� m-1��(full node)���� bool���� �����ϴ� �Լ�.
	public boolean isFullNode(int m) {
		return numKey==m-1?true:false;
	}
	//�ش� ��尡 ����������� bool���� �����ϴ� �Լ�.
	public boolean hasNoChild() {
		return numKey>0?false:true;
	}
	public int findPos(int key) {
		int i;
		for(i=0;key!=this.getKey(i);i++);
		return i;
	}
}
