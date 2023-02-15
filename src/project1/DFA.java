/* Author: Jacek Strotz
 * Date: 2/15/23
 * Description: This class implements Runnable (Java's implementation of 
 * an asynchronous thread class) to create start, stop, and single-step
 * methods. Additionally, it implements general DFA functionality by 
 * parsing inputs into the 5-part definition of a DFA. Then, it provides
 * the functionality to execute a DFA with a given input string, and 
 * classifying the result as accepted or rejected.
 */
package project1;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class DFA implements Runnable {
    private int count; 
    private int sleep_time;
    private boolean pause;
    private Thread thread;
    
    private String[] states;
    private String[] alphabet;
    private String start_state;
    private String[] accept_state;
    private String[] delta;
    
    private boolean valid;
    
    private String[] regex = {
        "(([a-zA-Z][0-9]*){1,}\\s?){1,}([a-zA-Z][0-9]*)*",
        "[a-zA-Z0-9][ ]?", 
        "(.{1,}\\n?){1,}",
        " {2,}",
        "\\/\\/.*\\n"}; 
    
    private JTextArea output;
    
    public DFA(JTextArea j) {
        count = 0;
        sleep_time = 500;
        pause = true;
        thread = new Thread(this);
        thread.start();
        
        output = j;
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
        output.append("count: " + count + "\n");
    }
    
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    
    public boolean getPause() {
        return this.pause;
    }
    
    public void setSpeed(int ms) {
        sleep_time = 1000 - ms;
    }
    
    private int getIndexOfContains(String[] input, String in) {
        for (int j = 0; j < input.length; j++)
            if (input[j].contains(in))
                return j;
        
        return -1;
    }
    
    private boolean arrayContains(String[] input, char in) {
        for (String input1 : input)
            if (input1.contains(""+in)) // simple cast char to string
                return true;
        
        return false;
    }
    
    private boolean arrayContains(String[] input, String in) {
        for (String input1 : input) 
            if (input1.contains(in)) 
                return true;
        
        return false;
    }
    
    public int executeDFA(String input) {
        if (!valid) return 2; // sanity check
        
        String current_state = start_state;

        for (int i = 0; i < input.length(); i++) {
            // if input string contains a character not in alphabet,
            // DFA cannot continue
            if (!arrayContains(alphabet, input.charAt(i))) {
                JOptionPane.showMessageDialog(output.getParent(), "Input contains non-alphabet"
                        + " character");
                return 3;
            } 
            
            // create a comparator in the format "sa|"
            // where s - current state, a - input character
            String comp = current_state + input.charAt(i) + "|";
            // if we can locate the index of "sa|", we can check
            // the letter after the "|" at that index to find the next state
            current_state = delta[getIndexOfContains(delta, comp)].replace(comp, "");
        }
        // check if final state is within accepted states
        return arrayContains(accept_state, current_state) ? 1 : 0;
    }
    
    public String validate(String i) {
        String fail; // error handler string
        valid = false;
        
        // regex[2] catches repeating whitespace
        // regex[3] checks the format of the definition
        // regex[4] catches comments
        // thus, delete comments,
        // replace repetitive whitespace with single-space
        // and make sure that definition is in 5-line single-space format
        i = i.trim().replaceAll(regex[4], "").replaceAll(regex[3], " ");
        if (!i.matches(regex[2]))
            return "Invalid format.";
        
        // parse definition into lines and check length
        String[] lines = i.split("\n");
        for (int j = 0; j < lines.length; j++) 
            lines[j] = lines[j].trim();
        if (lines.length != 5) 
            return "Invalid definition length.";
        
        // regex test state set for exact validation
        fail = lines[0].replaceAll(regex[0],"");
        if (fail.length() > 0)
            return "Invalid state name \"" + fail + "\".";
        
        // regex test alphabet set for exact validation
        fail = lines[1].replaceAll(regex[1],"");
        if (fail.length() > 0) 
            return "Invalid alphabet character \"" + fail.charAt(0) + "\".";
        
        // catch-all for errors that will not show up down the line
        fail = lines[2].replaceAll(regex[0],"");
        if (fail.length() > 0)
            return "Issue located at \"" + fail.charAt(0) + "\"";
        
        // parse each line by single-space
        states = lines[0].split(" ");
        alphabet = lines[1].split(" ");
        start_state = lines[2]; // only 1 state
        accept_state = lines[3].split(" ");
        
        // validate state names and check for duplicates
        for (int j = 0; j < states.length; j++) {
            if (states[j].length() >= 10) return "State name \"" + 
                    states[j] + "\" must be less than 10 characters.";
            
            for (int k = 0; k < states.length; k++) 
                if (j != k && states[j].equals(states[k]))
                    return "Duplicate letter \"" + states[j] + "\" in state set.";
        }
        
        // check alphabet for invalid/duplicate characters
        for (int j = 0; j < alphabet.length; j++) {
            if (alphabet[j].length() > 1) 
                return "Invalid alphabet character \"" + alphabet[j] + "\".";
            
            for (int k = 0; k < alphabet.length; k++) 
                if (j != k && alphabet[j].equals(alphabet[k]))
                    return "Duplicate letter \"" + alphabet[k] + 
                            "\" in alphabet.";
            
            for (String b : states)
                if (alphabet[j].equals(b)) 
                    return "Alphabet letter \"" + b + "\" defined as a state.";
        }
        
        // validate start state
        if (lines[2].split(" ").length != 1) 
            return "Function must contain only one start state.";
        else if (!lines[0].contains(start_state)) 
            return "Start state \"" + start_state + "\" not in state list.";
        
        // check accept states for invalid states or duplicates
        for (int j = 0; j < accept_state.length; j++) {
            if (!lines[0].contains(accept_state[j])) 
                return "Accept state \"" + accept_state[j] + "\" not in state list.";
            
            for (int k = 0; k < accept_state.length; k++) 
                if (j != k && accept_state[j].equals(accept_state[k]))
                        return "Duplicate state \"" + accept_state[j] + "\" in F.";
        }
        
        // create a copy to manipulate data
        delta = lines[4].split(" ");
        String[] temp = delta.clone();
        
        // check for proper length of delta function
        if (states.length * alphabet.length != delta.length) 
            return "Invalid delta function length.";
        
        // search for states in delta that do not exist in Q
        for (String s : delta)
            if (!lines[0].contains(s)) return "Issue in delta function: state \""
                    + s + "\" not in state set";
        
        valid = true;
        // build a table for the parsed DFA
        String ret = "-----------------------------------------------------------------"
                + "\nStates\t| {" + states[0];
        for (int j = 1; j < states.length; j++)
            ret += (", " + states[j]);
        ret += "}\n-----------------------------------------------------------------"
               + "\nAlphabet\t| {" + alphabet[0];
        for (int j = 1; j < alphabet.length; j++)
            ret += (", " + alphabet[j]);
        ret += "}\n-----------------------------------------------------------------"
                + "\nStart state\t| " + start_state 
                + "\n-----------------------------------------------------------------"
                + "\n𝛿\t| ";
        for (String a : alphabet) {
            ret += (a + " | ");
        }
        ret +="\n----------------------";
        for (int j = 0; j < states.length; j++) {
            ret += ("\n"+states[j] + "\t| ");
            for (int k = 0; k < alphabet.length; k++) {
                ret += (delta[j*alphabet.length + k] + " | ");
            }
        }
        
        // map delta function into format "sa|b", where
        // s - input state, a - input string, b - output state
        for (int j = 0; j < temp.length / alphabet.length; j++) 
            for (int k = 0; k < alphabet.length; k++) 
                delta[(alphabet.length)*j+k] = states[j] + alphabet[k] + '|' + temp[j*alphabet.length+k];
  
        return ret;
        
    }
    
    public boolean getValid() {
        return valid;
    }
}
