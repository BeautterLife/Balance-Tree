package 화일처리;
public class AVL {
	private Node root;
	private Node p, q;
	private String rotType;
	public AVL() {
		rotType="NO";
	}
	public AVL(Node n) {
		root=n;
		rotType="NO";
	}
	public void setRoot(int key) {
		root=new Node(key);
	}
	public void setNewRoot(Node n) {
		root=n;
	}
	public Node getRoot() {
		return this.root;
	}
	public Node getNode(int key) {
		return new Node(key);
	}
	public void setPNull(){
		p=null;
	}
	public void setP(Node n) {
		p=n;
	}
	public void	setQNull() {
		q=null;
	}
	public void setQ(Node n) {
		q=n;
	}
	
	public void setRotType(String s) {
		rotType=s;
	}
	public String getRotType() {
		return this.rotType;
	}
	public void insertAVL(AVL t,int newKey) {	
		Node r=t.getRoot();
		Node s=null;
		while(r!=null) {
			if(newKey==r.getKey()) {
				setRotType("중복");
				return;
			}
			s=r;
			if(newKey<r.getKey()) {
				r=r.getLeftChild();
			}
			else r=r.getRightChild();
		}
		//tree가 비었을때 root에 삽입
		if(s==null) {
			t.setRoot(newKey);
			return;
		}
		//일단 삽입
		else if(newKey<s.getKey()) {
			s.setLeftChild(new Node(newKey));
		}
		else s.setRightChild(new Node(newKey));
		setBF(t);
		checkBalance(t,newKey);
		if(getRotType()=="NO") return;
		else {
			rotateTree(t,newKey);
		}
		setBF(t);
	}
	
	public void checkBalance(AVL t, int newKey) {
		boolean flag=false;
		Node n=t.getRoot();
		setPNull();
		setQNull();
		setP(findNode(t,newKey));
		setQ(findPrevious(t,newKey));
		while(true) {
			if(IsUnbalNode(p)) {
				flag=true;
				if(p==t.getRoot()) q=null;
				break;
			}
			if(p==t.getRoot()) {
				q=null;
				break;
			}
			p=q;
				q=findPrevious(t,q.getKey());
		}
	
		
		if(flag==true) {
			if(newKey<p.getKey()) {
				if(newKey<p.getLeftChild().getKey()) setRotType("LL");
				else setRotType("LR");
			}
			else {
				if(newKey>p.getRightChild().getKey()) setRotType("RR");
				else setRotType("RL");
			}
		}
		else {
			setRotType("NO");
		}
	}
	public boolean IsUnbalNode(Node n) {
		if(Math.abs(n.getBF())>1) return true;
		else return false;
	}
	
	public void setBF(AVL t) {
		Node n=t.getRoot();
		if(n==null) return;
		if(n.HasNoChild()) {
			return;
		}
		setBF(new AVL(n.getLeftChild()));
		setBF(new AVL(n.getRightChild()));
		n.setBF(height(new AVL(n.getLeftChild()))-height(new AVL(n.getRightChild())));
	}
	public Node findNode(AVL t, int key) {
		Node n=t.getRoot();
		while(n.getKey()!=key) {
			if(n.getKey()>key) n=n.getLeftChild();
			else n=n.getRightChild();
		}
		return n;
	}
	public Node findPrevious(AVL t,int key) {
		Node n=t.getRoot();
		Node prev=null;
		while(n.getKey()!=key) {
			prev=n;
			if(n.getKey()>key) n=n.getLeftChild();
			else n=n.getRightChild();
		}
		return prev;
	}
	public void rotateTree(AVL t, int newKey) {
		Node subRoot=null;
		switch(rotType) {
			case "LL":
				subRoot=p.getLeftChild();
				if(q==null) {
					this.setNewRoot(subRoot);
				}
				else if(p==q.getLeftChild()) {
					q.setLeftChild(subRoot);
				}
				else {
					q.setRightChild(subRoot);
				}
				
				p.setLeftChild(subRoot.getRightChild());
				subRoot.setRightChild(p);
				break;
			case "LR":
				subRoot=p.getLeftChild().getRightChild();
				if(q==null) {
					this.setNewRoot(subRoot);
				}
				else if(p==q.getLeftChild()) {
					q.setLeftChild(subRoot);

				}
				else {
					q.setRightChild(subRoot);
				}
				p.getLeftChild().setRightChild(subRoot.getLeftChild());
				subRoot.setLeftChild(p.getLeftChild());		
				p.setLeftChild(subRoot.getRightChild());
				subRoot.setRightChild(p);
				break;
			case "RR":
				subRoot=p.getRightChild();
				if(q==null) {
					this.setNewRoot(subRoot);
				}
				else if(p==q.getLeftChild()) {
					q.setLeftChild(subRoot);
				}
				else {
					q.setRightChild(subRoot);
				}
				p.setRightChild(subRoot.getLeftChild());
				subRoot.setLeftChild(p);
				break;
			case "RL":
				subRoot=p.getRightChild().getLeftChild();

				if(q==null) {
					this.setNewRoot(subRoot);
				}
				else if(p==q.getLeftChild()) {
					q.setLeftChild(subRoot);
				}
				else {
					q.setRightChild(subRoot);
				}
				p.getRightChild().setLeftChild(subRoot.getRightChild());
				subRoot.setRightChild(p.getRightChild());
				p.setRightChild(subRoot.getLeftChild());
				subRoot.setLeftChild(p);
				break;
		}
	
	}
	public int height(AVL t) {
		Node n=t.getRoot();
		if(n==null) return 0;
		 return Math.max(height(new AVL(n.getLeftChild())), height(new AVL(n.getRightChild())))+1;
	 }

	public void inorderAVL(AVL t) {
		Node n=t.getRoot();
		if(n==null) { 
			return;
		}			  
		inorderAVL(new AVL(n.getLeftChild()));
		System.out.print(n.getKey()+" ");
		inorderAVL(new AVL(n.getRightChild()));		 
	}
}
