package 화일처리;
import java.util.Scanner;
public class BTreeMain {
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		System.out.println("m값을 입력하시오 : ");
		int m=in.nextInt(); //트리의 분기 수
		System.out.println("입력된 m은 : "+m);
		BTree t=new BTree();		
		int keys[]=new int[] {40,11,77,33,20,90,99,70,88,80,66,10,22,30,44,55,50,60,100,28,18,9,5,17,6,3,1,4,2,7,8,73,12,13,14,16,15,25,24,28,45,49,42,43,41,47,48,46,63,68,61,62,64,69,67,65,54,59,58,51,53,57,52,56,83,81,82,84,75,89};
		for(int i=0;i<keys.length;i++) {
			t.insertBT(t,m,keys[i]);
			//t.inorderBT(t, m);
			//System.out.println();
		}
		//t.inorderBT(t, m);
		
		/*
		t.deleteBT(t, m, 20);
		System.out.println();
		t.inorderBT(t, m);
		*
		/*
		t.deleteBT(t, m, 100);
		System.out.println();
		t.inorderBT(t, m);
		t.deleteBT(t, m, 99);
		t.inorderBT(t, m);

		t.deleteBT(t, m, 80);
		t.inorderBT(t, m);
	*/
		//t.inorderBT(t, m);
		//System.out.println();
		
		for(int i=10;i<20;i++) {
			t.deleteBT(t, m, keys[i]);
			t.inorderBT(t, m);
			System.out.println();
		}
		
		for(int i=20;i<30;i++) {
			t.deleteBT(t, m, keys[i]);
			t.inorderBT(t, m);
			System.out.println();
		}
		for(int i=30;i<40;i++) {
			t.deleteBT(t, m, keys[i]);
			t.inorderBT(t, m);
			System.out.println();
		}
		for(int i=0;i<10;i++) {
			t.deleteBT(t, m, keys[i]);
			t.inorderBT(t, m);
			System.out.println();

		}
		for(int i=40;i<50;i++) {
			t.deleteBT(t, m, keys[i]);
			t.inorderBT(t, m);
			System.out.println();

		}
		for(int i=60;i<70;i++) {
			t.deleteBT(t, m, keys[i]);
			t.inorderBT(t, m);
			System.out.println();

		}
		for(int i=50;i<60;i++) {
			t.deleteBT(t, m, keys[i]);
			t.inorderBT(t, m);
			System.out.println();

		}
		
	}
}
