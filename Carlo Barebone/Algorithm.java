import java.lang.*;
import java.io.*;
import java.lang.Math.*;

// Esempio di classe con algoritmo che riempie un array
public class Algorithm{
	protected int data[][];
	protected int counter;
	
	public Algorithm(){
		data=new int[64][64];
		counter=0;
	}
	
	public String toString(){
		return "Algorithm status: "+counter+"/"+(64*64);
	}

	public void reset(){
		counter=0;
		data=new int[64][64];
	}
	
	public boolean hasNext(){
		if(counter<64*64)return true;
		else return false;
	}
	
	public void next(){
		data[counter/64][counter%64]=(int)(Math.random() * 255);
		counter++;
	}
	
	public enum LABEL{SIZE_X, SIZE_Y, COUNTER}
	//public class LabelNotFoundException extends Exception{}
	
	public Object getVar(LABEL c){
		switch(c){
			case SIZE_X:
				return (Integer)64;
			case SIZE_Y:
				return (Integer)64;
			case COUNTER:
				return (Integer)counter;
			default:
				return null;
		}
	}
		
	public int getData(int a, int b){return data[a][b];}
}
