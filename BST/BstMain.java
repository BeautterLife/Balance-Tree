package 화일처리;

import 화일처리.BST.Node;

public class BstMain {

	public static void main(String[] args) {
		BST t = new BST();
		int node[]= {40,11,77,33,20,90,99,70,88,80,66,10,22,30,44,55,50,60,100};
		/*
		for(int i=0;i<19;i++) {
			t.insertBST(node[i]);
			t.inOrder(t.getRoot());
			System.out.println();
		}
		System.out.println("삽입역순으로 삭제");
		for(int i=18;i>=0;i--) {
			t.deleteBST(t,node[i]);
			t.inOrder(t.getRoot());
			System.out.println();
		}
		*/
		for(int i=0;i<19;i++) {
			t.insertBST(node[i]);
			t.inOrder(t.getRoot());
			System.out.println();
		}
		System.out.println("삽입순서대로 삭제");
		for(int i=0;i<19;i++) {
			t.deleteBST(t,node[i]);
			t.inOrder(t.getRoot());
			System.out.println();
		}
	}
	

}
