package 화일처리;
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
		//System.out.println(key+"삽입");
		if(t.getRoot()==null) {
			BNode node=t.getNode(m);
			t.setRoot(node);
			node.setKey(0, key);
			node.increaseNumKey();
			return;
		}
		BNode walk=t.getRoot();
		Stack<BNode> st=new Stack<BNode>();
		//Stack ist : 전단계에서, 각 노드로 들어가면서 그 때의 삽입키의 위치 
		//이번 과제에서는 사용하지 않았지만 스택에서 pop하면서 그때마다 반복문을 이용하여
		//삽입키의 경로를 구하는 것보다 효율적일것.
		//Stack<Integer> ist=new Stack<Integer>();

		do {
			st.push(walk);	
			if(key>walk.getKey(walk.getNumKey()-1)) {
				//ist.push(walk.getNumKey());
				walk=walk.getChild(walk.getNumKey());
			}
			else {
				for(int i=0;i<walk.getNumKey();i++) {
					//삽입할 키가 이미 트리에 있으면 insertBT() 종료.
					if(key==walk.getKey(i)) {
						System.out.println("중복키"+key+" 가 삽입됨 ");
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
		
		BNode spNode=null; //split된 node를 저장할 변수
		BNode parent;	   //pop된 원소의 부모(stack.peek())
		int middleKey=0;
		do {
			walk=st.pop();
			parent= st.isEmpty()?null:st.peek();
			//중간키를 부모 노드에 잘 삽입한 경우(노드가 full이 아닐 때) 
			if((middleKey=this.insertKey(walk, m, key))==0) {
				//노드의 맨 마지막 child를, 삽입키 전까지, 오른쪽으로 땡김
				//단말이 아니면 이전에 분할된 오른쪽 노드를 child[i+1]로 설정.
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
			//노드가 꽉차서 노드를 분할해야 하는경우이다.
			//루트일때를 따로 처리함.
			else {
				//단말이 아니고, 루트도 아닐때
				//중간키의 오른쪽child와, splited node를 이어줘야함.
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
				//위로 올라간 중간값을 삽입 키로 바꿔준다.
				key=middleKey;
				//꽉찬 노드를 분할하고 기존 노드도 갱신.
				spNode=this.splitBNode(walk,m,middleKey);
				this.removeRightPart(walk, middleKey);

				//루트를 분할하는 경우, 새 루트를 만들고 , 0번재 키는 middleKey/ 0,1번재 child 설정.
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
				//루트가 아닐때 분할 : 위에서 처리
			}
		} while(!st.isEmpty());
	}
	
	//해당 노드에 삽입할 수 있으면 0리턴
	//노드가 꽉 찼다면 일단 삽입후 , 그 상태에서의 중간 키 값 리턴.
	public int insertKey(BNode n, int m,int key){
		if(!n.isFullNode(m)) {
			for(int i=0;i<n.getNumKey();i++) {
				//삽입위치 찾음.
				if(key<n.getKey(i)) {
					for(int j=n.getNumKey();j>i;j--) {
						n.setKey(j,n.getKey(j-1));
						//단말이 아니면 포인터 조정.
						if(!n.hasNoChild()) n.setChild(n.getChild(j), j+1);
					}
					n.setKey(i,key);
					n.increaseNumKey();
					return 0;
				}	
			}
			//마지막에 삽입하는 경우
			n.setKey(n.getNumKey(),key);
			n.increaseNumKey();
			return 0;
		}
		//System.out.print("노드 꽉참 ");
		//노드가 꽉차있으면 상위 level로 올라갈 키를 구한다.;
		//현재 중간값과 그것의 전 값, 삽입 키 중 전체의 중간키가 있음.
		//왼쪽이 오른쪽보다 많거나 같은 키를 가지게함.
		//키가 교체된 인덱스를 리턴.
		if(key>n.getMiddleKey()) {
			for(int i=m/2;i<n.getNumKey();i++) {
				if(key<n.getKey(i)) {
					for(int j=n.getNumKey();j>i;j--) {
						n.setKey(j, n.getKey(j-1));
						//단말이 아니면 포인터 조정.
						if(!n.hasNoChild()) n.setChild(n.getChild(j), j+1);
					}
					//삽입 키는 이전 키와 키값만 교체
					n.setKey(i, key);
					n.increaseNumKey();					
					return n.getMiddleKey();
				}
			}
			//삽입키가 제일 큰 경우
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
						//단말이 아니면 포인터 조정.
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
			//삽입키가 제일 큰 경우
			n.setKey(m/2 , key);
			n.increaseNumKey();
			return n.getMiddleKey();
		}
		//삽입키가 중간값
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
	
	//중간키 오른쪽을 초기화하는 함수.
	public void removeRightPart(BNode n, int middleKey) {
		//n.setChild(null,n.getNumKey());
		for(int i=n.getNumKey()-1;n.getKey(i)>=middleKey;i--) {
			n.setKey(i, 0);
			n.setChild(null,i+1);
			n.decreaseNumKey();
		}
	}
	//중칸키 오른쪽 값들과 child를 새로운 노드에 저장하여 반환하는 함수.
	public BNode splitBNode(BNode n, int m, int middleKey) {
		//System.out.print("분할시 중간키는 "+middleKey);
		BNode newNode=this.getNode(m);
		for(int i=0;i<m;i++) {
			if(middleKey==n.getKey(i)) {
				newNode.makeChild(0,m);
				newNode.setChild(n.getChild(i+1), 0);
		
				//System.out.println("split노드는"+n.getKey(i+1)+" ");
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
	//middle키의 인덱스 반환 함수.
	
	
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
			System.out.println("트리가 비었음");
			return;
		}
		//System.out.println(oldKey+"//");
		Stack<BNode> st=new Stack<BNode>();
		//track : 삭제키와 비교한 키의 인덱스
		Stack<Integer> track = new Stack<Integer>();
		
		//삭제할 키 찾기.
		int oldKeyPos=0;
		int followKey=0;
		//삭제키를 찾는 경로를 스택에 저장.
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
			System.out.println(oldKey+"는 트리에 없는 키 또는 이미 삭제된 키");
			return;
		}
		
		BNode oldKeyNode=walk; //삭제키가 있는 노드
		BNode leafNode=walk; //단말노드
	
		//oldKey가 내부에 있는 경우 - 단말의 후행키와 교체.
		//후행키는 삭제키의 오른쪽 서브트리에서 제일 작은 원소임.
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
			//단말에 있는 후행키와 삭제할 키 교환
			int temp=leafNode.getKey(0); //후행키
			//System.out.println(temp);
			oldKeyNode.setKey(oldKeyPos,temp);
			
		}
		else leafPath=oldKeyPos;
		
		
		//System.out.print(leafPath+" ");
		//단말에 있는 oldKey 삭제.
		for(int i=leafPath;i<leafNode.getNumKey();i++) {
			leafNode.setKey(i, leafNode.getKey(i+1));
		}
		
		
		leafNode.decreaseNumKey();
		
		do{
			if(walk==t.getRoot()||!isUnderflow(walk,m)) {
				
			//	System.out.print("문제없이제거");
				return;
			}
			walk=st.pop();
			BNode parent=st.peek();
			int nodePos=track.pop(); // walk가 부모의 몇번째 child인지 저장하는 변수.
			//System.out.println("track경로"+nodePos);
			//키 재분배 되면 종료
			int bestSiblingPos=availableSibling(parent,walk,m,nodePos);
			//	System.out.println("형제Pos"+bestSiblingPos);
			//키 재분배.
			if(bestSiblingPos!=0){
				//왼쪽에서 빌려옴
				if(bestSiblingPos==-1) {
					//System.out.println("왼쪽에서빌림");
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
				//오른쪽에서 빌려옴
				else {
					//System.out.println("R에서 빌리기");
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
			//합병
			else{
				//System.out.println("합병");
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
			//System.out.println("중간");
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
		//합병할 형제는, 좌/우측 형제중 키가 많은 노드 먼저 합병하다가
		//
		int parentKey=0;
		//좌측 형제와 합병
		BNode sibling=null;
		if(findMergeSibling(parent, walk, m, nodePos)<0) {
			//System.out.println(nodePos);
			parentKey=parent.getKey(nodePos-1);
			//System.out.println("합병부모키"+parentKey);
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
			//System.out.println("좌측형제와 합병후의 노드값 ");
			//for(int i=0;i<sibling.getNumKey();i++) System.out.print(sibling.getKey(i)+" ");

			//부모노드를 왼쪽으로 한칸씩 당긴다.
			//parent.child(nodePos
			for(int i=nodePos;i<parent.getNumKey();i++) {
				parent.setKey(i-1, parent.getKey(i));
				parent.setChild(parent.getChild(i+1),i);
			}
			parent.decreaseNumKey();
			
		}
		//우측 형제와 합병
		else {
			
			parentKey=parent.getKey(nodePos);
			sibling=parent.getChild(nodePos+1);
			//기존 노드 뒤에 부모 키와 sibling의 키, 자식 넣기
			walk.setKey(walk.getNumKey(), parentKey);
			walk.increaseNumKey();
			walk.setChild(sibling.getChild(0), walk.getNumKey());
			//기존노드에 우측형제 키, 포인터 이동.
			for(int i=0;i<sibling.getNumKey();i++) {
				walk.setKey(walk.getNumKey()+i, sibling.getKey(i));
				walk.setChild(sibling.getChild(i), walk.getNumKey()+1);
				//sibling.decreaseNumKey();
				walk.increaseNumKey();
			}
			//System.out.println("우측형제와 합병후의 노드값");
			//for(int i=0;i<walk.getNumKey();i++) System.out.print(walk.getKey(i)+" ");
			//walk.setChild(sibling.getChild(sibling.getNumKey()), walk.getNumKey());
			walk.setChild(sibling.getChild(sibling.getNumKey()), walk.getNumKey());
			//부모노드에서 nodePos+1~ 를 왼쪽으로 1칸씩 당기기
			//parent.child(nodePos+1)은 무시해야됨.
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
