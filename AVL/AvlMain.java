package ȭ��ó��;

public class AvlMain {
	public static void main(String[] args) {
		AVL t=new AVL();
		int nodes[]=new int[]{40,11,77,33,20,90,99,70,88,80,66,10,22,30,44,55,50,60,100,28,18,9,5,17,6,3,1,4,2,7,8,73,12,13,14,16,15,25,24,28,45,49,42,43,41,47,48,46,63,68,61,62,64,69,67,65,54,59,58,51,53,57,52,56,83,81,82,84,75,89};
		int arrLen=nodes.length;
		for(int i=0;i<arrLen;i++) {
	         t.insertAVL(t, nodes[i]);
	         System.out.print(t.getRotType()+" ");
	         t.inorderAVL(t);
	         System.out.println();
	      } 
	}
}
