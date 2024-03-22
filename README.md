The project employs a Graphical User Interface (GUI) as an applet, showcasing the Towers of Hanoi puzzle using AWT components. 
Threads drive the animation, guiding disc movement adhering to the game's rules. 
The applet starts with a built tower on the first rod, progressing to construct one on the third.
Closing the applet halts the process, while reopening initiates the construction anew.

solve(int disks, int from, int to, int spare)
{
    if (disks == 1)
        moveOne(from,to);
    else {
        solve(disks-1, from, spare, to);
        moveOne(from,to);
        solve(disks-1, spare, to, from);
        }
}

Commands:
compile: javac TOHGUI.java
run : appletviewer TOHGUI.java
<img width="359" alt="image" src="https://github.com/likhithachinthakuntla/TOHGUI/assets/32333151/c1b3c520-4488-4622-b0ca-85f19dffeccf">
<img width="351" alt="image" src="https://github.com/likhithachinthakuntla/TOHGUI/assets/32333151/02820570-f4dd-402c-8ca3-0782516c0ed0">
<img width="385" alt="image" src="https://github.com/likhithachinthakuntla/TOHGUI/assets/32333151/5fcbd718-bb3f-402c-8019-83717f94668e">


