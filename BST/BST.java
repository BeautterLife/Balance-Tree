package 화일처리;
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
			//System.out.println("q변경");
			if(deleteKey<p.getKey()) {
				p=p.leftChild;
				//System.out.println("왼 삭제할 키값 :"+p.getKey());
			}
			else {
				p=p.rightChild;
			//	System.out.println("오른 삭제할 키값:"+p.getKey());
			}
		}
		//System.out.println("삭제할 원소 찾고 시작");
		if(q==null&&p.HasNoChild()) {
			p=null;
			return;
		}
		if(p==null&&q==null) {
			return;
		}
		
		//System.out.println("예외 통과");
		if(!(p.HasLeftChild()||p.HasRightChild())) {
			//System.out.println("자식 없는 노드 삭제");
			if(q.leftChild==p) {
				q.leftChild=null;
				//System.out.println("삭제할 값: "+p.getKey());
			}
			else {
				q.rightChild=null;
				//System.out.println("삭제할 값:"+p.getKey());
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
		
		//삭제할 노드의 차수 2
		//System.out.println("삭제할 노드의 자식 2개"+p.getKey());
		BST lT=new BST(p.leftChild);
		//System.out.println("lT의 top :"+lT.getRoot().getKey());
		BST rT=new BST(p.rightChild);
		//System.out.println("rT의 top :"+rT.getRoot().getKey());
		Boolean flag = false;
		Node n;
		if(height(lT)>height(rT)) {
			//System.out.println("왼쪽 st가 높아서 maxnode 올린");
			n=maxNode(lT);
			//System.out.println("올릴 maxnode: "+n.getKey());
		}
		else if(height(lT)<height(rT)) {
			flag=true;
			//System.out.println("오른쪽 st가 높아서 minnode를 올린다");
			n=minNode(rT);
			//System.out.println("올릴 minNode: "+n.getKey());

		}
		else {
			//System.out.println("높이 차이가 없으면");
			if(noNodes(lT)>=noNodes(rT)) {
				//System.out.println("노드개수 L>=R");
				n=maxNode(lT);
				//System.out.println(n.getKey());

			}
			else {
				flag=true;
				//System.out.println("노드개수 L<R");
				n=minNode(rT);
				System.out.println(n.getKey());				
			}
		}
		p.key=n.getKey();
		if(!flag) {
			//System.out.println("왼쪽에서 대체한 노드 삭제");
			deleteBST(lT,p.key);
		}
		else {
			//System.out.println("오른쪽에서 대체한 노드 삭제");
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

