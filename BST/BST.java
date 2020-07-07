package ȭ��ó��;
import java.util.Scanner;

public class BST {
	public Node root;
	public BST() {	
		root=null;
	}
	public BST(Node n) {
		root=n;
	}
	public Node getRoot() {
		return this.root;
	}
	public Node getNode(int key) {
		return new Node(key);
	}
	public void printNode(Node n) {
		System.out.println(n.getKey());
	}
	

	public class Node {
		private Node leftChild;
		private Node rightChild;
		private int key;
		private Node() {
			leftChild=rightChild=null;
		}
		private Node(int key) {
			this.key=key;
			leftChild=rightChild=null;
		}
		public int getKey() {
			return this.key;
		}
		public Node setLeftChild() {
			return this.leftChild==null? null : this.leftChild;
		}
		public Node setRightChild() {
			return this.rightChild==null? null : this.rightChild;
		}

		public boolean HasLeftChild() {
			return this.leftChild!=null?true:false;
		}
		public boolean HasRightChild() {
			return this.rightChild!=null?true:false;
		}
		
		public boolean HasNoChild() {
			return !(this.HasLeftChild()||this.HasRightChild());
		}
	}
	
	public void insertBST(int newKey) {
		Node p,q;
		p=this.getRoot();
		q=null;
		while(p!=null) {
			if(newKey<p.getKey()) {
				q=p; 
				p=p.leftChild;
			}
			else {
				q=p;
				p=p.rightChild;
			}
		}
		if(this.getRoot()==null) {
			this.root=this.getNode(newKey);
		}
		else if(newKey<q.getKey()) {
			q.leftChild=getNode(newKey);
		}
		else if(newKey>q.getKey()){
			q.rightChild=getNode(newKey);
		}
	}

	
	public void deleteBST(BST t,int deleteKey) {
		Node p,q=null;
		p=t.getRoot();
		q=null;
		while(p!=null) {
			if(deleteKey==p.getKey()) break;
			q=p;
			//System.out.println("q����");
			if(deleteKey<p.getKey()) {
				p=p.leftChild;
				//System.out.println("�� ������ Ű�� :"+p.getKey());
			}
			else {
				p=p.rightChild;
			//	System.out.println("���� ������ Ű��:"+p.getKey());
			}
		}
		//System.out.println("������ ���� ã�� ����");
		if(q==null&&p.HasNoChild()) {
			p=null;
			return;
		}
		if(p==null&&q==null) {
			return;
		}
		
		//System.out.println("���� ���");
		if(!(p.HasLeftChild()||p.HasRightChild())) {
			//System.out.println("�ڽ� ���� ��� ����");
			if(q.leftChild==p) {
				q.leftChild=null;
				//System.out.println("������ ��: "+p.getKey());
			}
			else {
				q.rightChild=null;
				//System.out.println("������ ��:"+p.getKey());
			}
			return;
		}
		else if(p.HasLeftChild()^p.HasRightChild()) {
			if(p.HasLeftChild()) {
				
				if(q.leftChild==p) {
					q.leftChild=p.leftChild;
				}
				else {
					q.rightChild=p.leftChild;
				}
				return;
			}
			else {
				if(q.leftChild==p) {
					q.leftChild=p.rightChild;
				}
				else {
					q.rightChild=p.rightChild;
				}
				return;
			}
		}
		
		//������ ����� ���� 2
		//System.out.println("������ ����� �ڽ� 2��"+p.getKey());
		BST lT=new BST(p.leftChild);
		//System.out.println("lT�� top :"+lT.getRoot().getKey());
		BST rT=new BST(p.rightChild);
		//System.out.println("rT�� top :"+rT.getRoot().getKey());
		Boolean flag = false;
		Node n;
		if(height(lT)>height(rT)) {
			//System.out.println("���� st�� ���Ƽ� maxnode �ø�");
			n=maxNode(lT);
			//System.out.println("�ø� maxnode: "+n.getKey());
		}
		else if(height(lT)<height(rT)) {
			flag=true;
			//System.out.println("������ st�� ���Ƽ� minnode�� �ø���");
			n=minNode(rT);
			//System.out.println("�ø� minNode: "+n.getKey());

		}
		else {
			//System.out.println("���� ���̰� ������");
			if(noNodes(lT)>=noNodes(rT)) {
				//System.out.println("��尳�� L>=R");
				n=maxNode(lT);
				//System.out.println(n.getKey());

			}
			else {
				flag=true;
				//System.out.println("��尳�� L<R");
				n=minNode(rT);
				System.out.println(n.getKey());				
			}
		}
		p.key=n.getKey();
		if(!flag) {
			//System.out.println("���ʿ��� ��ü�� ��� ����");
			deleteBST(lT,p.key);
		}
		else {
			//System.out.println("�����ʿ��� ��ü�� ��� ����");
			deleteBST(rT,p.key);
		}
		
	}
	
	public int height(BST t) {
		return nodeHeight(t.getRoot());
	}
	private int nodeHeight(Node n) {
		if(n==null) return 0;
		else {
			return Math.max(nodeHeight(n.leftChild),nodeHeight(n.rightChild))+1;
		}
	}
	
	public int noNodes(BST t) {
		int cnt=0;
		if(t.getRoot()!=null) {
			cnt=Nodes(t.getRoot());
		}
		return cnt;
	}
	private int Nodes(Node n) {
		int cnt=1;
		if(n.HasLeftChild()) cnt+=Nodes(n.leftChild);
		if(n.HasRightChild()) cnt+=Nodes(n.rightChild);
		return cnt;
	}
	public Node maxNode(BST t) {
		Node n=t.getRoot();
		while(n.HasRightChild()) {
			n=n.rightChild;
		}
		return n;
	}
	public Node minNode(BST t) {
		Node n=t.getRoot();
		while(n.HasLeftChild()) {
			n=n.leftChild;
		}
		return n;
	}
	public void inOrder(Node n) {
		if(n==null) return;
		inOrder(n.leftChild);
		System.out.print(n.getKey()+" ");
		inOrder(n.rightChild);
	}
}

