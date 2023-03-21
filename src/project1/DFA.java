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
    private boolean pause;
    private boolean complete;
    private Thread thread;
    private int sleep_time;
    
    private String[] states;
    private String[] alphabet;
    private String start_state;
    private String[] accept_state;
    private String[] delta;
    
    private String input; // single input string
    private String[] input2; // array of all input strings
    // iterator[0] is for the current input
    // iterator[1] is for the input array
    private int iterator[];
    
    private String current_state;
    
    private boolean valid;
    private boolean inp_valid;
    
    // make use of bitwise operations to set flags
    public int flags;
    
    public String ret;
    
    private String[] regex = {
        "(([a-zA-Z][0-9]*)*\\s?){1,}([a-zA-Z][0-9]*)*",
        "[a-zA-Z0-9][ ]?", 
        "(.{1,}\\n?){1,}",
        " {2,}",
        "\\/\\/.*\\n",
        "\\w+\\n*"}; 
    
    private final JTextArea output;
    
    public DFA(JTextArea j) {
        pause = true;
        output = j;
        valid = false;
        complete = true;
        sleep_time = 5;
        
        thread = new Thread(this);
        thread.start();
        
        iterator = new int[]{0, 0};
        flags = 0;
    }
    
    public DFA(JTextArea j, String i) {
        pause = true;
        valid = false;
        complete = true;
        sleep_time = 5;
        
        output = j;
        input = i;
        
        thread = new Thread(this);
        
        iterator = new int[]{0, 0};
        flags = 0;
    }
    
    public DFA(JTextArea j, String d, String i) {
        pause = true;
        input = i;
        output = j;
        valid = false;
        complete = true;
        sleep_time = 5;
        
        String e = validate(d);
        if (!valid) {
            JOptionPane.showMessageDialog(null, e, 
                    "Alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        pause = false;
        thread = new Thread(this);
  
        iterator = new int[]{0, 0};
        flags = 0;
    }
    
    public void reset() {
        pause = true;
    }
    
    public void setSpeed(int ms) {
        sleep_time = 1000 - ms;
    }
    
    public int getSpeed() {
        return sleep_time;
    }
    
    @Override
    public void run() {
        while (true) {
            if (!pause) {
                if ((flags & 8) == 8) { // asynchronous
                    switch(singleStep()) {
                        case 0:
                        case 3:
                        default:
                            break;
                        case 1:
                        case 2:
                        case 4:
                            pause = true;
                            complete = true;
                            FrameMain.runButton.setText("Run");
                            break;
                    }
                    try {
                       Thread.sleep(sleep_time);
                    }
                    catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                else {
                    //System.out.println("test");
                    switch(singleStepA()) {
                        case 0:
                        case 3:
                        default:
                            break;
                        case 1:
                        case 2:
                        case 4:
                            output.append(ret);
                            pause = true;
                            complete = true;
                            FrameMain.runButton.setText("Run");
                            break;
                    }
                }
            }
            else {
                try {
                    Thread.sleep(500);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public int singleStep() {
        if (!valid) return 1; // sanity check
        if (iterator[0] >= input.length()) {
            boolean cont = arrayContains(accept_state, current_state);
            
            if ((flags & 4) == 4) {
                if (cont) 
                    output.append(input + " - accepted [line " + (iterator[1]+1) + "]\n");
            }
            else
                output.append((input.length() == 0 ? "empty string" : input)
                        + " - " + (cont ? "accepted\n" : "rejected\n"));
                
            if ((flags & 1) == 1)
                output.append("\n");

            if (iterator[1] < input2.length-1) {
                input = input2[++iterator[1]];
                iterator[0] = 0;
                current_state = start_state;
                
                if ((flags & 1) == 1) 
                    output.append("Current String: " + input + "\n");
                
                return 0;
            }
            else if (iterator[1] >= input2.length-1) {
                pause = true;
                return 2;
            }
        }
        
        // if input string contains a character not in alphabet,
        // DFA cannot continue
        if (!arrayContains(alphabet, input.charAt(iterator[0]))) {
            JOptionPane.showMessageDialog(null, "Input contains non-alphabet"
                    + " character", "Alert", JOptionPane.ERROR_MESSAGE);
            return 4;
        } 

        // create a comparator in the format "sa|"
        // where s - current state, a - input character
        String comp = '-' + current_state + input.charAt(iterator[0]++) + "|";
        // print delta function
        if ((flags & 1) == 1) output.append("𝛿(" + current_state + ", " + 
                input.charAt(iterator[0]-1) + ") = " + 
                    delta[getIndexOfContains(delta, comp)].replace(comp, "") + "\n");
        // if we can locate the index of "-sa|", we can check
        // the letter after the "|" at that index to find the next state
        current_state = delta[getIndexOfContains(delta, comp)].replace(comp, "");
        return 0;
    }
    
    public int singleStepA() {
        if (!valid) return 1; // sanity check
        if (iterator[0] >= input.length()) {
            boolean cont = arrayContains(accept_state, current_state);

            if ((flags & 4) == 4) {
                if (cont)
                    ret = ret.concat(input + " - accepted [line " + (iterator[1]+1) + "]\n");
            }
            else
                ret = ret.concat((input.length() == 0 ? "empty string" : input)
                    + " - " + (cont ? "accepted\n" : "rejected\n"));
                
            if ((flags & 1) == 1)
                ret = ret.concat("\n");

            if (iterator[1] < input2.length-1) {
                input = input2[++iterator[1]];
                iterator[0] = 0;
                current_state = start_state;
                
                if ((flags & 1) == 1) 
                    ret = ret.concat("Current String: " + input + "\n");
                
                return 0;
            }
            else if (iterator[1] >= input2.length-1) {
                pause = true;
                return 2;
            }
        }
        
        // if input string contains a character not in alphabet,
        // DFA cannot continue
        if (!arrayContains(alphabet, input.charAt(iterator[0]))) {
            JOptionPane.showMessageDialog(null, "Input contains non-alphabet"
                    + " character", "Alert", JOptionPane.ERROR_MESSAGE);
            return 4;
        } 

        // create a comparator in the format "sa|"
        // where s - current state, a - input character
        String comp = '-' + current_state + input.charAt(iterator[0]++) + "|";
        // print delta function
        if ((flags & 1) == 1) ret = ret.concat("𝛿(" + current_state + ", " + 
                input.charAt(iterator[0]-1) + ") = " + 
                    delta[getIndexOfContains(delta, comp)].replace(comp, "") + "\n");
        // if we can locate the index of "-sa|", we can check
        // the letter after the "|" at that index to find the next state
        current_state = delta[getIndexOfContains(delta, comp)].replace(comp, "");
        return 0;
    }
    
    public void initialize(String[] lines) {
        input2 = lines;
        input = lines[0];
        
        iterator = new int[]{0,0};
        
        current_state = start_state;
        
        ret = "";
        
        complete = false;
        pause = false;
    }
    
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    
    public boolean getPause() {
        return this.pause;
    }

    private int getIndexOfContains(String[] input, String in) {
        for (int j = 0; j < input.length; j++)
            if (input[j].contains(in))
                return j;
        
        return -1;
    }
    
    public boolean arrayContains(String[] input, char in) {
        for (String input1 : input)
            if (input1.equals(""+in)) // simple cast char to string
                return true;
        
        return false;
    }
    
    public boolean arrayContains(String[] input, String in) {
        for (String input1 : input) 
            if (input1.equals(in)) 
                return true;
        
        return false;
    }
    
    public void execute() {
        thread = new Thread(this);
        thread.start();
    }
    
    public int executeDFA(String input) {
        if (!valid || !inp_valid) return 2; // sanity check
        
        //String current_state = start_state;

        output.append("Current String: " + input + "\n");
        for (int i = 0; i < input.length(); i++) {
            // if input string contains a character not in alphabet,
            // DFA cannot continue
            if (!arrayContains(alphabet, input.charAt(i))) {
                JOptionPane.showMessageDialog(null, "Input contains non-alphabet"
                        + " character", "Alert", JOptionPane.ERROR_MESSAGE);
                return 3;
            } 
            
            // create a comparator in the format "sa|"
            // where s - current state, a - input character
            String comp = '-' + current_state + input.charAt(i) + "|";
            // print delta function
            //if (show_details) output.append("𝛿(" + current_state + ", " + 
             //   input.charAt(i) + ") = " + 
             //       delta[getIndexOfContains(delta, comp)].replace(comp, "") + "\n");
            // if we can locate the index of "sa|", we can check
            // the letter after the "|" at that index to find the next state
            current_state = delta[getIndexOfContains(delta, comp)].replace(comp, "");
        }
        output.append("\n");
        // check if final state is within accepted states
        return arrayContains(accept_state, current_state) ? 1 : 0;
    }
    
    public final String validate(String i) {
        String fail; // error handler string
        valid = false;
        inp_valid = false;
        
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
            if (!arrayContains(states, s)) return "Issue in delta function: state \""
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
        // put a '-' before string to indicate beginning (for contains)
        for (int j = 0; j < temp.length / alphabet.length; j++) 
            for (int k = 0; k < alphabet.length; k++) 
                delta[(alphabet.length)*j+k] = '-' + states[j] + alphabet[k] + '|' + temp[j*alphabet.length+k];
  
        return ret;
        
    }
    
    public final String validate(String d, String inp) {
        String ret = validate(d);
        if (!valid) return ret;
        
        //if (!inp.matches(regex[5]))
        //    return "Invalid input data format.";
        
        String[] lines = inp.split("\n");
        
        for (String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                if (!arrayContains(alphabet, line.charAt(j))) {
                    //JOptionPane.showMessageDialog(null, "Input contains non-alphabet"
                    // + " character", "Alert", JOptionPane.ERROR_MESSAGE);
                    return "Input contains non-alphabet"
                            + " character";
                } 
            }
        }
        inp_valid = true;
        return ret;
    }
    
    public boolean getValid() {
        return valid;
    }
    
    public boolean getInpValid() {
        return inp_valid;
    }
    
    public void setInput(String i) {
        input = i;
    }
    
    public boolean getComplete() {
        return complete;
    }
    
    public void setValid(boolean v) {
        valid = v;
    }
    
    public void setInpValid(boolean i) {
        inp_valid = i;
    }
    
    public void setComplete(boolean c) {
        complete = c;
    }
}
