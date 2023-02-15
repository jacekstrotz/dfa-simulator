/* Author: Jacek Strotz
 * Date: 1/12/23
 * Description: This class implements Runnable (Java's implementation of 
 * an asynchronous thread class) to create start, stop, and single-step
 * methods.
 */
package dfa01;

/**
 *
 * @author strotz
 */
public class DFA implements Runnable {
    private int count; 
    private int sleep_time;
    private boolean pause;
    private Thread thread;
    
    DFA() {
        count = 0;
        sleep_time = 500;
        pause = true;
        thread = new Thread(this);
        thread.start();
    }
    
    public void reset() {
        pause = true;
        count = 0;
    }
    
    @Override
    public void run() {
        while (true) {
            if (!pause) 
                singleStep();
            
            try {
                thread.sleep(sleep_time);
            }
            catch (Exception ex) {}
        }
    }
    
    public void singleStep() {
        count += 1; 
        System.out.println("count: " + count);
    }
    
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    
    public boolean getPause() {
        return this.pause;
    }
}
