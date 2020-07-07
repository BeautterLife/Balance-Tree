package ȭ��ó��;
import java.util.Stack;
public class BTree {
	private BNode root;
	public BTree() {
		this.root=null;
	}
	public BTree(BNode n) {
		this.root=n;
	}
	/*
	public BNode copyNode(BNode n,int m) {
		BNode copy=new BNode(m);
		for(int i=0;i<n.getNumKey();i++) {
			copy.setKey(i, n.getKey(i));
			copy.setChild(n.getChild(i), i);
		}
		copy.setChild(n.getChild(n.getNumKey()), n.getNumKey());
		return copy;
	}
	*/
	
	public void insertBT(BTree t, int m, int key) {
		//System.out.println(key+"����");
		if(t.getRoot()==null) {
			BNode node=t.getNode(m);
			t.setRoot(node);
			node.setKey(0, key);
			node.increaseNumKey();
			return;
		}
		BNode walk=t.getRoot();
		Stack<BNode> st=new Stack<BNode>();
		//Stack ist : ���ܰ迡��, �� ���� ���鼭 �� ���� ����Ű�� ��ġ 
		//�̹� ���������� ������� �ʾ����� ���ÿ��� pop�ϸ鼭 �׶����� �ݺ����� �̿��Ͽ�
		//����Ű�� ��θ� ���ϴ� �ͺ��� ȿ�����ϰ�.
		//Stack<Integer> ist=new Stack<Integer>();

		do {
			st.push(walk);	
			if(key>walk.getKey(walk.getNumKey()-1)) {
				//ist.push(walk.getNumKey());
				walk=walk.getChild(walk.getNumKey());
			}
			else {
				for(int i=0;i<walk.getNumKey();i++) {
					//������ Ű�� �̹� Ʈ���� ������ insertBT() ����.
					if(key==walk.getKey(i)) {
						System.out.println("�ߺ�Ű"+key+" �� ���Ե� ");
						return;
					}
					if(key<walk.getKey(i)) {
						//ist.push(i);
						//System.out.println(walk.getKey(i));
						walk=walk.getChild(i);
						
						break;
						
					}
				}
			}

		}while(walk!=null);
		
		BNode spNode=null; //split�� node�� ������ ����
		BNode parent;	   //pop�� ������ �θ�(stack.peek())
		int middleKey=0;
		do {
			walk=st.pop();
			parent= st.isEmpty()?null:st.peek();
			//�߰�Ű�� �θ� ��忡 �� ������ ���(��尡 full�� �ƴ� ��) 
			if((middleKey=this.insertKey(walk, m, key))==0) {
				//����� �� ������ child��, ����Ű ������, ���������� ����
				//�ܸ��� �ƴϸ� ������ ���ҵ� ������ ��带 child[i+1]�� ����.
				if(walk.getChild(0)!=null) {
					for(int i=0;i<walk.getNumKey();i++) {
						if(key==walk.getKey(i)) {
							//walk.makeChild(i+1, m);
							walk.setChild(spNode, i+1);
						}
					}
				}
				return;
			}
			//��尡 ������ ��带 �����ؾ� �ϴ°���̴�.
			//��Ʈ�϶��� ���� ó����.
			else {
				//�ܸ��� �ƴϰ�, ��Ʈ�� �ƴҶ�
				//�߰�Ű�� ������child��, splited node�� �̾������.
				if(walk.getChild(0)!=null) {
					for(int i=0;i<walk.getNumKey();i++) {
						if(key==walk.getKey(i)) {
							if(spNode.getKey(0)<key) {
								walk.setChild(spNode, i);
								break;
							}
							else {
								walk.setChild(spNode, i+1);
							}
						}
					}
				}
				//���� �ö� �߰����� ���� Ű�� �ٲ��ش�.
				key=middleKey;
				//���� ��带 �����ϰ� ���� ��嵵 ����.
				spNode=this.splitBNode(walk,m,middleKey);
				this.removeRightPart(walk, middleKey);

				//��Ʈ�� �����ϴ� ���, �� ��Ʈ�� ����� , 0���� Ű�� middleKey/ 0,1���� child ����.
				if(parent==null) {					
					BNode newRoot=this.getNode(m);
					newRoot.setKey(0, key);
					newRoot.increaseNumKey();
					newRoot.makeChild(0,m);
					newRoot.makeChild(1,m);
					newRoot.setChild(walk, 0);
					newRoot.setChild(spNode, 1);
					t.setRoot(newRoot);
					return;
				}
				//��Ʈ�� �ƴҶ� ���� : ������ ó��
			}
		} while(!st.isEmpty());
	}
	
	//�ش� ��忡 ������ �� ������ 0����
	//��尡 �� á�ٸ� �ϴ� ������ , �� ���¿����� �߰� Ű �� ����.
	public int insertKey(BNode n, int m,int key){
		if(!n.isFullNode(m)) {
			for(int i=0;i<n.getNumKey();i++) {
				//������ġ ã��.
				if(key<n.getKey(i)) {
					for(int j=n.getNumKey();j>i;j--) {
						n.setKey(j,n.getKey(j-1));
						//�ܸ��� �ƴϸ� ������ ����.
						if(!n.hasNoChild()) n.setChild(n.getChild(j), j+1);
					}
					n.setKey(i,key);
					n.increaseNumKey();
					return 0;
				}	
			}
			//�������� �����ϴ� ���
			n.setKey(n.getNumKey(),key);
			n.increaseNumKey();
			return 0;
		}
		//System.out.print("��� ���� ");
		//��尡 ���������� ���� level�� �ö� Ű�� ���Ѵ�.;
		//���� �߰����� �װ��� �� ��, ���� Ű �� ��ü�� �߰�Ű�� ����.
		//������ �����ʺ��� ���ų� ���� Ű�� ��������.
		//Ű�� ��ü�� �ε����� ����.
		if(key>n.getMiddleKey()) {
			for(int i=m/2;i<n.getNumKey();i++) {
				if(key<n.getKey(i)) {
					for(int j=n.getNumKey();j>i;j--) {
						n.setKey(j, n.getKey(j-1));
						//�ܸ��� �ƴϸ� ������ ����.
						if(!n.hasNoChild()) n.setChild(n.getChild(j), j+1);
					}
					//���� Ű�� ���� Ű�� Ű���� ��ü
					n.setKey(i, key);
					n.increaseNumKey();					
					return n.getMiddleKey();
				}
			}
			//����Ű�� ���� ū ���
			n.setKey(n.getNumKey(), key);
			n.increaseNumKey();
			return n.getMiddleKey();
			
		}
		else if(key<n.getMiddleKey()) {
			for(int i=0;i<=m/2;i++) {
				if(key<n.getKey(i)) {
					//for(int j=n.getNumKey();j>i;j--){
					for(int j=n.getNumKey();j>i;j--){
						n.setKey(j, n.getKey(j-1));
						//System.out.print(n.getKey(j-1)+" ");
						//�ܸ��� �ƴϸ� ������ ����.
						if(!n.hasNoChild()) {
							n.setChild(n.getChild(j), j+1);
						}
					}
					//System.out.println("d");
					n.setKey(i, key);
					n.increaseNumKey();
					return n.getMiddleKey();
				}
			}
			//����Ű�� ���� ū ���
			n.setKey(m/2 , key);
			n.increaseNumKey();
			return n.getMiddleKey();
		}
		//����Ű�� �߰���
		else {
			for(int i=0;i<n.getNumKey();i++) {
			}
			for(int i=n.getNumKey();n.getKey(i-1)>key;i--)	{
				n.setKey(i, n.getKey(i-1));
				n.setChild(n.getChild(i), i+1);
			}
			n.setKey(n.getNumKey()/2, key);
		}
		n.increaseNumKey();
		return n.getMiddleKey();
	}
	
	//�߰�Ű �������� �ʱ�ȭ�ϴ� �Լ�.
	public void removeRightPart(BNode n, int middleKey) {
		//n.setChild(null,n.getNumKey());
		for(int i=n.getNumKey()-1;n.getKey(i)>=middleKey;i--) {
			n.setKey(i, 0);
			n.setChild(null,i+1);
			n.decreaseNumKey();
		}
	}
	//��ĭŰ ������ ����� child�� ���ο� ��忡 �����Ͽ� ��ȯ�ϴ� �Լ�.
	public BNode splitBNode(BNode n, int m, int middleKey) {
		//System.out.print("���ҽ� �߰�Ű�� "+middleKey);
		BNode newNode=this.getNode(m);
		for(int i=0;i<m;i++) {
			if(middleKey==n.getKey(i)) {
				newNode.makeChild(0,m);
				newNode.setChild(n.getChild(i+1), 0);
		
				//System.out.println("split����"+n.getKey(i+1)+" ");
				for(int j=0;j<m-1-m/2;j++) {
					newNode.setKey(j, n.getKey(i+1+j));
					newNode.makeChild(j+1,m);
					newNode.setChild(n.getChild(i+2+j), j+1);
					newNode.increaseNumKey();
				}
				break;
			}
		}
		return newNode;
	}
	public BNode getNode(int m) {
		return new BNode(m);
	}
	public BNode getRoot(){
		return this.root;
	}
	public void setRoot(BNode n) {
		this.root=n;
	}
	//middleŰ�� �ε��� ��ȯ �Լ�.
	
	
	public void inorderBT(BTree t,int m) {
		BNode n=t.getRoot();
		if(n.getChild(0)==null) {
			for(int i=0;i<n.getNumKey();i++) System.out.print(n.getKey(i)+" ");
			return;
		}
		for(int i=0;i<=n.getNumKey();i++) {
			inorderBT(new BTree(n.getChild(i)),m);
			if(i<n.getNumKey())System.out.print(n.getKey(i)+" ");
		}
	}	
	
	
	/*
	public void inorderBT(BTree t,int m) {
		BNode n=t.getRoot();
		if(n.getChild(0)==null) {
			for(int i=0;i<n.getNumKey();i++) System.out.print(n.getKey(i)+" ");
			System.out.println();
			return;
		}
		for(int i=0;i<=n.getNumKey();i++) {
			inorderBT(new BTree(n.getChild(i)),m);
			if(i<n.getNumKey())System.out.println(n.getKey(i)+" ");
		}
	}	
	*/
	public void deleteBT(BTree t, int m, int oldKey) {
		BNode walk=t.getRoot();
		if(walk==null) {
			System.out.println("Ʈ���� �����");
			return;
		}
		//System.out.println(oldKey+"//");
		Stack<BNode> st=new Stack<BNode>();
		//track : ����Ű�� ���� Ű�� �ε���
		Stack<Integer> track = new Stack<Integer>();
		
		//������ Ű ã��.
		int oldKeyPos=0;
		int followKey=0;
		//����Ű�� ã�� ��θ� ���ÿ� ����.
		st.push(walk);
		boolean findOldKey=false;
		do {
			if(oldKey>walk.getKey(walk.getNumKey()-1)) {
				//System.out.println(walk.getNumKey());
				//System.out.print(walk.getKey(walk.getNumKey()-1)+" ");
				track.push(walk.getNumKey());

				walk=walk.getChild(walk.getNumKey());
				st.push(walk);
				//track.push(walk.getNumKey());
			}
			else {
				for(int i=0;i<walk.getNumKey();i++) {			
					if(oldKey==walk.getKey(i)) {
						oldKeyPos=i;	
						findOldKey=true;
						break;
					}
					else if(oldKey<walk.getKey(i)) {
						walk=walk.getChild(i);
						st.push(walk);
						track.push(i);
						break;
					}
					
				}
			}
		}while(walk!=null&&findOldKey==false);
		
		if(walk==null) {
			System.out.println(oldKey+"�� Ʈ���� ���� Ű �Ǵ� �̹� ������ Ű");
			return;
		}
		
		BNode oldKeyNode=walk; //����Ű�� �ִ� ���
		BNode leafNode=walk; //�ܸ����
	
		//oldKey�� ���ο� �ִ� ��� - �ܸ��� ����Ű�� ��ü.
		//����Ű�� ����Ű�� ������ ����Ʈ������ ���� ���� ������.
		int leafPath=0;
		if(walk.getChild(0)!=null) {
			//for(;walk.getKey(leafPath)!=oldKey;leafPath++);
			leafPath=oldKeyPos;	
			
			st.push(walk=walk.getChild(leafPath+1));
			//st.push(leafNode=leafNode.getChild(leafPath+1));
			track.push(leafPath+1);
			//System.out.println(leafPath+1);
			/*
			do {
				track.push(0);
				if(walk.getChild(0)!=null) st.push(walk.getChild(0));
				else break;
			}while(true);
			*/
			
			while(walk.getChild(0)!=null) {
				st.push(walk=walk.getChild(0));
				track.push(0);
			}
			
			leafNode=st.peek();
			leafPath=track.peek();
			leafPath=0;
			//�ܸ��� �ִ� ����Ű�� ������ Ű ��ȯ
			int temp=leafNode.getKey(0); //����Ű
			//System.out.println(temp);
			oldKeyNode.setKey(oldKeyPos,temp);
			
		}
		else leafPath=oldKeyPos;
		
		
		//System.out.print(leafPath+" ");
		//�ܸ��� �ִ� oldKey ����.
		for(int i=leafPath;i<leafNode.getNumKey();i++) {
			leafNode.setKey(i, leafNode.getKey(i+1));
		}
		
		
		leafNode.decreaseNumKey();
		
		do{
			if(walk==t.getRoot()||!isUnderflow(walk,m)) {
				
			//	System.out.print("������������");
				return;
			}
			walk=st.pop();
			BNode parent=st.peek();
			int nodePos=track.pop(); // walk�� �θ��� ���° child���� �����ϴ� ����.
			//System.out.println("track���"+nodePos);
			//Ű ��й� �Ǹ� ����
			int bestSiblingPos=availableSibling(parent,walk,m,nodePos);
			//	System.out.println("����Pos"+bestSiblingPos);
			//Ű ��й�.
			if(bestSiblingPos!=0){
				//���ʿ��� ������
				if(bestSiblingPos==-1) {
					//System.out.println("���ʿ�������");
					for(int i=walk.getNumKey()-1;i>0;i--) {
						//System.out.println(walk.getKey(i));
						walk.setKey(i+1, walk.getKey(i));
						walk.setChild(walk.getChild(i+1),i+2);
					}
					walk.setChild(walk.getChild(0), 1);
					walk.setKey(0, parent.getKey(nodePos-1));
					//System.out.println(parent.getKey(nodePos-1));
					walk.increaseNumKey();
					BNode sibling=parent.getChild(nodePos-1);
				
					
					parent.setKey(nodePos-1,sibling.getKey(sibling.getNumKey()-1));
					walk.setChild(sibling.getChild(sibling.getNumKey()), 0);
					sibling.decreaseNumKey();
				}
				//�����ʿ��� ������
				else {
					//System.out.println("R���� ������");
					BNode sibling=parent.getChild(nodePos+1);

					walk.setKey(walk.getNumKey(), parent.getKey(nodePos));
					walk.increaseNumKey();
					walk.setChild(sibling.getChild(0), walk.getNumKey());
					parent.setKey(nodePos, sibling.getKey(0));
					for(int i=1;i<sibling.getNumKey();i++) {
						sibling.setKey(i-1, sibling.getKey(i));
						sibling.setChild(sibling.getChild(i), i-1);
					}
					sibling.setChild(sibling.getChild(sibling.getNumKey()),sibling.getNumKey()-1 );
					sibling.decreaseNumKey();
					//for(int i=0;i<sibling.)
				}
			}
			//�պ�
			else{
				//System.out.println("�պ�");
				mergeNode(parent, walk, m, nodePos);
			}
			
			if(parent==t.getRoot()&&parent.getNumKey()==0) {
				if(parent.getChild(0).getNumKey()==0) t.setRoot(parent.getChild(1));
				else t.setRoot(parent.getChild(0));
				return;
			}
			
			walk=parent;
		}while(true);		
	}
	

	
	public int availableSibling(BNode parent, BNode walk,int m, int nodePos) {
		if(nodePos==0) {
			if(parent.getChild(1).getNumKey()>=((m-1)/2)+1) return 1;
			
		}
		else if(nodePos==parent.getNumKey()) {
			if(parent.getChild(nodePos-1).getNumKey()>=((m-1)/2)+1) return -1;
		}
		else if(parent.getChild(nodePos-1).getNumKey()>=((m-1)/2)+1 || parent.getChild(nodePos+1).getNumKey()>=((m-1)/2)+1) {
			//System.out.println("�߰�");
			return parent.getChild(nodePos-1).getNumKey()>=parent.getChild(nodePos+1).getNumKey() ? -1 :1;
		}
		return 0;
	}
	
	public int findMergeSibling(BNode parent, BNode walk, int m, int nodePos) {
		if(nodePos==0) return 1;
		else if(nodePos==parent.getNumKey()) return -1;
		return parent.getChild(nodePos-1).getNumKey() >= parent.getChild(nodePos+1).getNumKey() ? -1 : 1;
	}
	

	public void mergeNode(BNode parent,BNode walk, int m, int nodePos) {
		//�պ��� ������, ��/���� ������ Ű�� ���� ��� ���� �պ��ϴٰ�
		//
		int parentKey=0;
		//���� ������ �պ�
		BNode sibling=null;
		if(findMergeSibling(parent, walk, m, nodePos)<0) {
			//System.out.println(nodePos);
			parentKey=parent.getKey(nodePos-1);
			//System.out.println("�պ��θ�Ű"+parentKey);
			sibling=parent.getChild(nodePos-1);
			sibling.setKey(sibling.getNumKey(),parentKey);
			sibling.increaseNumKey();
			//sibling.setChild(n, i);
			for(int i=0;i<walk.getNumKey();i++) {
				sibling.setKey(i+sibling.getNumKey(), walk.getKey(i));
				sibling.setChild(walk.getChild(i), i+sibling.getNumKey());
				sibling.increaseNumKey();
				walk.decreaseNumKey();
			}
			sibling.setChild(walk.getChild(0), sibling.getNumKey());
			//System.out.println("���������� �պ����� ��尪 ");
			//for(int i=0;i<sibling.getNumKey();i++) System.out.print(sibling.getKey(i)+" ");

			//�θ��带 �������� ��ĭ�� ����.
			//parent.child(nodePos
			for(int i=nodePos;i<parent.getNumKey();i++) {
				parent.setKey(i-1, parent.getKey(i));
				parent.setChild(parent.getChild(i+1),i);
			}
			parent.decreaseNumKey();
			
		}
		//���� ������ �պ�
		else {
			
			parentKey=parent.getKey(nodePos);
			sibling=parent.getChild(nodePos+1);
			//���� ��� �ڿ� �θ� Ű�� sibling�� Ű, �ڽ� �ֱ�
			walk.setKey(walk.getNumKey(), parentKey);
			walk.increaseNumKey();
			walk.setChild(sibling.getChild(0), walk.getNumKey());
			//������忡 �������� Ű, ������ �̵�.
			for(int i=0;i<sibling.getNumKey();i++) {
				walk.setKey(walk.getNumKey()+i, sibling.getKey(i));
				walk.setChild(sibling.getChild(i), walk.getNumKey()+1);
				//sibling.decreaseNumKey();
				walk.increaseNumKey();
			}
			//System.out.println("���������� �պ����� ��尪");
			//for(int i=0;i<walk.getNumKey();i++) System.out.print(walk.getKey(i)+" ");
			//walk.setChild(sibling.getChild(sibling.getNumKey()), walk.getNumKey());
			walk.setChild(sibling.getChild(sibling.getNumKey()), walk.getNumKey());
			//�θ��忡�� nodePos+1~ �� �������� 1ĭ�� ����
			//parent.child(nodePos+1)�� �����ؾߵ�.
			for(int i=nodePos+1;i<parent.getNumKey();i++) {
				parent.setKey(i-1, parent.getKey(i));
				parent.setChild(parent.getChild(i+1), i);
			}
			parent.decreaseNumKey();
			
		}
		
	}
	
	public boolean isUnderflow(BNode n,int m) {
		return n.getNumKey()<(m-1)/2?true:false;
	}
	
	
	
	
}
