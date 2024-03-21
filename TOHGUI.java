import java.awt.*;
import java.applet.Applet;

/*
<html>
<applet code ="TowersOfHanoiGUI.class" height=1200 width=1200>
</applet>
</html>
*/ 

public class TOHGUI extends Applet implements Runnable {

   private static Color BACKGROUND_COLOR = new Color(255,255,180);
   private static Color BORDER_COLOR = new Color(100,0,0);
   private static Color DISK_COLOR = new Color(0,0,180);
   private static Color MOVE_DISK_COLOR = new Color(180,180,255);

   private Image OSC;   // The off-screen canvas.

   private static final int GO = 1, SUSPEND = 2,  TERMINATE = 3;  // Values for status.
                                                   
   private int status = GO;   // Controls the execution of the thread.
   
   private Thread runner;     // A thread to run the animation.
   

   
   private int[][] tower;
   private int[] towerHeight;
   private int moveDisk;
   private int moveTower;
   

   public void init() {
         // Initialize the applet by setting the background color.
      setBackground(BACKGROUND_COLOR);
   }
   

   public void run() {

       try {
          while (true) {
             tower = null;
             if (OSC != null) {
                Graphics g = OSC.getGraphics();
                drawCurrentFrame(g);
                g.dispose();
             }
             repaint();
             delay(2000);
             synchronized(this) {
                tower = new int[3][10];
                for (int i = 0; i < 10; i++)
                   tower[0][i] = 10 - i;
                towerHeight = new int[3];
                towerHeight[0] = 10;
                if (OSC != null) {
                   Graphics g = OSC.getGraphics();
                   drawCurrentFrame(g);
                   g.dispose();
                }
                repaint();
                delay(2000);
             }
             solve(10,0,1,2);
             delay(4000);
          }
       }
       catch (IllegalArgumentException e) {
       }
   }
   

   private void solve(int disks, int from, int to, int spare) {
      
      if (disks == 1)
         moveOne(from,to);
      else {
         solve(disks-1, from, spare, to);
         moveOne(from,to);
         solve(disks-1, spare, to, from);
      }
   }
   

   synchronized private void moveOne(int fromStack, int toStack) {

      moveDisk = tower[fromStack][towerHeight[fromStack]-1];
      moveTower = fromStack;
      delay(120);
      towerHeight[fromStack]--;
      putDisk(MOVE_DISK_COLOR,moveDisk,moveTower);
      delay(80);
      putDisk(BACKGROUND_COLOR,moveDisk,moveTower);
      delay(80);
      moveTower = toStack;
      putDisk(MOVE_DISK_COLOR,moveDisk,moveTower);
      delay(80);
      putDisk(DISK_COLOR,moveDisk,moveTower);
      tower[toStack][towerHeight[toStack]] = moveDisk;
      towerHeight[toStack]++;
      moveDisk = 0;
   }
   

   private void putDisk(Color color, int disk, int t) {
 
      if (OSC != null) {
         Graphics g = OSC.getGraphics();
         g.setColor(color);
         g.fillRoundRect(75+140*t - 5*disk - 5, 116-12*towerHeight[t], 10*disk+10, 10, 10, 10);
         g.dispose();
      }
      repaint();
   }
                                                   

   synchronized void drawCurrentFrame(Graphics g) {
          
      g.setColor(BACKGROUND_COLOR);
      g.fillRect(0,0,430,143);
      g.setColor(BORDER_COLOR);
      g.drawRect(0,0,429,142);
      g.drawRect(1,1,427,140);
      if (tower == null)
         return;
      g.fillRect(10,128,130,5);
      g.fillRect(150,128,130,5);
      g.fillRect(290,128,130,5);
      g.setColor(DISK_COLOR);
      for (int t = 0; t < 3; t++) {
         for (int i = 0; i < towerHeight[t]; i++) {
            int disk = tower[t][i];
            g.fillRoundRect(75+140*t - 5*disk - 5, 116-12*i, 10*disk+10, 10, 10, 10);
         }
      }
      if (moveDisk > 0) {
         g.setColor(MOVE_DISK_COLOR);
         g.fillRoundRect(75+140*moveTower - 5*moveDisk - 5, 116-12*towerHeight[moveTower], 
                                             10*moveDisk+10, 10, 10, 10);
      }
   }
   

   synchronized void delay(int milliseconds) {
 
       if (status == TERMINATE)
          throw new IllegalArgumentException("Terminated");
       try {
           wait(milliseconds);
       }
       catch (InterruptedException e) {
       }
       while (status == SUSPEND) {
          try {
             wait();
          }
          catch (InterruptedException e) {
          }
       }
       if (status == TERMINATE)
          throw new IllegalArgumentException("Terminated");
   }
                                                   

   synchronized public void start() {
         // When applet is started or restarted, start or
         // restart the thread.
      status = GO;  // Indicates that the applet is running.
      if (runner == null || !runner.isAlive()) {
          tower = null;
          runner = new Thread(this);
          runner.start();
      }
      else {
         notify();
      }
   }
   

   synchronized public void stop() {

      status = SUSPEND;
      notify();
   }
   

   synchronized public void destroy() {

      status = TERMINATE;
      notify();
   }
   

   synchronized public void paint(Graphics g) {
 
       if (OSC == null)
          setupOSC();
       if (OSC == null)
          drawCurrentFrame(g);
       else
          g.drawImage(OSC,0,0,this);
   }


   public void update(Graphics g) {
         
       paint(g);
   }
   

   synchronized private void setupOSC() {

       OSC = null;  // Free memory currently used by OSC.
       try {
          OSC = createImage(430,143);
       }
       catch (OutOfMemoryError e) {
          OSC = null;
          return;
       }
       Graphics OSG = OSC.getGraphics();
       drawCurrentFrame(OSG);
       OSG.dispose();
   }


}  // end class TowersOfHanoiGUI