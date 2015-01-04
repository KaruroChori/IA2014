import java.lang.*;
import java.io.*;

public class Example{
	public static void main(String[] args){
		Algorithm ciao=new Algorithm();
		Displayer test=new Displayer(ciao);
		
		test.render();
		System.out.println("Ciao");
		return;
	}
}
