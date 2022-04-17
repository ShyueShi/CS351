import java.util.Random;


public class test {
	public static void main(String args[]){
		for(int i=0; i<100; i++){
			Random rnd = new Random();
			int seed = rnd.nextInt(9)+1;
			System.out.println(seed);
		}
	}
}
