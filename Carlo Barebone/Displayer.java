import java.lang.*;
import java.io.*;
import java.lang.Math.*;
import java.awt.*;
import javax.swing.*;

public class Displayer extends JFrame{
	private Algorithm ref;
	
	public Displayer(Algorithm ref){
		super("Algorithm displayer!");
		this.ref=ref;
        
        setContentPane(new DrawPane());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize((int)ref.getVar(Algorithm.LABEL.SIZE_X), (int)ref.getVar(Algorithm.LABEL.SIZE_Y));
		setVisible(true); 
	}
	
	public void render(){
		while(ref.hasNext()){
			ref.next();
			getContentPane().repaint();
			
			//Soluzione sporca, meglio sincronizzare in modo diverso.
			try {
				Thread.sleep(10);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public void reset(){ref.reset();}
	
      class DrawPane extends JPanel{
        public void paintComponent(Graphics g){
			//Pulisce tutto!
			g.clearRect(0, 0, getWidth(), getHeight());
			
			//Disegna un punto
			int counter=(int)ref.getVar(Algorithm.LABEL.COUNTER);
			g.drawLine(counter%64,counter/64,counter%64,counter/64);
         }
     }

}
