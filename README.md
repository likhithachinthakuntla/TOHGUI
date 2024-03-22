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
