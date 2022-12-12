import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.security.KeyStore;
import java.util.*;

public class Gui extends JFrame {

    // *******************************************************************************
    // *******************************************************************************
    // Variables
    private static int tries = 8;
    private static int codelength = 4;

    private JList list;
    private static Color[] colors = {Color.YELLOW, Color.ORANGE, Color.CYAN, Color.BLUE, Color.PINK, new Color(85, 34, 132)};
    private static String[] colornames = {"yellow", "orange", "cyan", "blue", "pink", "purple"};
    private static Character[] colortags = {'y', 'o', 'c', 'b', 'p', 'm'};
    private static String[] colortags2 = {"y", "o", "c", "b", "p", "m"};
    public static JButton[][] buttons;



    // Constructor
    public Gui(){

        super("----  y = yellow  ----  o = orange  ----  c = cyan  ----  b = blue  ----  p = pink  ----  m = purple  ----");
        setLayout(new GridLayout(tries, 2*codelength+1));
        buttons = new JButton[tries][2*codelength+1];

        list = new JList(colornames);
        list.setVisibleRowCount(4);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);    // Select only 1 at a time

        for(int i=0; i<tries; i++){
            for(int j=0; j<2*codelength+1; j++){
                JButton b;
                if(j == codelength){
                    b = new JButton("Check!");
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                    });
                }
                else{
                    b = new JButton();

                }
                buttons[i][j] = b;
                add(b);
                // Container container = b;
                // container.add(list);
            }
        }




    }   // END OF CONSTRUCTOR



    // Auxiliary Functions
    public static int uniqueChars(String str){

        String temp="";
        for (int i = 0; i < str.length(); i++) {
            if(temp.indexOf(str.charAt(i)) == -1 ){
                temp = temp + str.charAt(i);
            }
        }
       return temp.length();

    }

    public static boolean hasValidChars(String str){

        for(int i=0; i<str.length(); i++){
            Character ch = str.charAt(i);
            boolean flag = false;
            for(int j=0; j<colortags.length; j++){
                Character ch2 = colortags[j];
                if(ch == ch2){
                    flag = true;
                    break;
                }
            }
            if(flag == false) {
                // Flag 'stayed' false ----> invalid characters
                return false;
            }
        }
        return true;
    }

    public static String createCode(){
        // Yellow-Orange-Cyan-Blue ----> yocb
        String code = "aaaa";
        Random r = new Random();
        while(uniqueChars(code) != codelength){
            code = "";
            for(int i=0; i<codelength; i++){
                code += colortags[r.nextInt(colortags.length)];
            }
        }
        return code;
    }



    public static ArrayList<Color> convertIntoColors(String guess){
        // Guess is valid
        ArrayList<Color> guessColors = new ArrayList<Color>();
        for(int i=0; i<guess.length(); i++){
            Character ch = guess.charAt(i);
            for(int j=0; j<colortags.length; j++){
                if(ch == colortags[j]){
                    // j is the desired index
                    guessColors.add(colors[j]);
                }
            }
        }
        return guessColors;
    }



    public static void makeColors(ArrayList<Color> guessColors, int round){
        // I will the the row = round - 1 in buttons and columns 0 until codelength - 1
        int row = round - 1;
        for(int column=0; column<codelength; column++){
            buttons[row][column].setBackground(guessColors.get(column));
        }
    }


    public static ArrayList<Color> check(String guess, String code){
        ArrayList<Color> hints = new ArrayList<Color>();
        int reds = 0;
        int whites = 0;
        int blacks = 0;

        // 1. Find how many reds we have
        for(int i=0; i<guess.length(); i++){
            Character ch1 = guess.charAt(i);
            Character ch2 = code.charAt(i);
            if(ch1 == ch2){
                reds += 1;
                // JOptionPane.showMessageDialog(null, "Red at pos = " + (i+1));
            }
        }
        for(int i=0; i<reds; i++){
            hints.add(Color.RED);
        }

        // 2. Find how many whites we have
        for(int i=0; i<guess.length(); i++){
            for(int j=0; j<code.length(); j++){
                if(guess.charAt(i) == code.charAt(j) && i != j){
                    whites += 1;
                    // JOptionPane.showMessageDialog(null, "White at pos = " + (i+1));
                }
            }
        }
        for(int i=0; i<whites; i++){
            hints.add(Color.WHITE);
        }

        // 3. Find the blacks
        for(int i=0; i<codelength-reds-whites; i++){
            hints.add(Color.BLACK);
        }

        return hints;
    }


    public static void makeHints(ArrayList<Color> hints, int round){
        int row = round - 1;
        for(int i=0; i<hints.size(); i++){
            int column = codelength + 1 + i;
            buttons[row][column].setBackground(hints.get(i));
        }
    }














    // *******************************************************************************
    // *******************************************************************************
    // Main Function
    public static void main(String[] args){


        // 1. Basics
        Gui gui = new Gui();
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gui.setSize(800, 800);
        gui.setVisible(true);


        // 2. Displays
        String display1 = "Welcome to the famous board game: 'Decoding' or 'Mastermind'\n";
        String display2 = "You have to try to guess the secret color code. The code consists of\n";
        String display3 = codelength + " different colors: The available colors are " + colors.length + ":\n";
        String display4 = (String.join(", ", colornames)) + "\n";
        String display5 = (String.join(" -- ", colortags2)) + "\n";
        String displays = display1 + display2 + display3 + display4 + display5 + "\n\n";
        System.out.println(displays);
        JOptionPane.showMessageDialog(null, displays, "Decoding - Introduction", JOptionPane.PLAIN_MESSAGE);


        // 3. Rules about input
        String rules1 = "You have " + tries + " tries to decode the message. Your input must be in this form: \n";
        String rules2 = "abcd, where a, b, c and d are the colortags (y, o, c, b, p, m)";
        String rules = rules1 + rules2 + "\n\n";
        System.out.println(rules);
        JOptionPane.showMessageDialog(null, rules, "Decoding - The rules", JOptionPane.PLAIN_MESSAGE);


        // 4. Random selection of code - colors
        String code = createCode();
        System.out.println(code);
        JOptionPane.showMessageDialog(null, code, "Decoding - The code", JOptionPane.PLAIN_MESSAGE);




        // 5. User tries via JOptionPane input messages (String)
        int round = 1;
        String guess = "aaaa";

        while(round <= tries && !guess.equals(code)){
            String title = "Decoding - Try " + round + "\n";
            System.out.println(title);
            boolean flag1, flag2, flag3;

            // guess is a 4-char string ----> example:      guess = 'yocb'
            // I have to check 3 conditions: length = 4, 4 unique characters, 4 valid characters
            guess = JOptionPane.showInputDialog("Guess " + round + " ----> ");
            flag1 = false;
            flag2 = false;
            flag3 = false;
            if(guess.length() == codelength){
                flag1 = true;
            }
            if(uniqueChars(guess) == codelength){
                flag2 = true;
            }
            if(hasValidChars(guess)){
                flag3 = true;
            }

            // Logs about errors
            if(!flag1){
                String error1 = "Length must be " + codelength + "\n";
                System.out.println(error1);
                JOptionPane.showMessageDialog(null, error1, "Error in try " + round, JOptionPane.PLAIN_MESSAGE);
            }
            if(!flag2){
                String error2 = "Unique characters must be " + codelength + "\n" ;
                System.out.println(error2);
                JOptionPane.showMessageDialog(null, error2, "Error in try " + round, JOptionPane.PLAIN_MESSAGE);
            }
            if(!flag3){
                String error3 = "You must have insrted some invalid characters (valid = y, o, c, b, p, m) \n";
                System.out.println(error3);
                JOptionPane.showMessageDialog(null, error3, "Error in try " + round, JOptionPane.PLAIN_MESSAGE);
            }




            // **********************************************************************************************
            // *************************    * CORRECT INPUT *****************************************************
            // **********************************************************************************************
            // COPY IN NEXT LINES
            if(flag1 && flag2 && flag3){

                ArrayList<Color> guessColors = convertIntoColors(guess);
                makeColors(guessColors, round);
                // !!!! Until this line OK !!!!
                ArrayList<Color> hints = check(guess, code);
                makeHints(hints, round);

            }




            // **********************************************************************************************
            // *************************   WRONG INPUT   ****************************************************
            // **********************************************************************************************
            else{

                // guess is a 4-char string ----> example:      guess = 'yocb'
                // I have to check 3 conditions: length = 4, 4 unique characters, 4 valid characters
                guess = JOptionPane.showInputDialog("Guess " + round + " ----> ");
                flag1 = false;
                flag2 = false;
                flag3 = false;
                if(guess.length() == codelength){
                    flag1 = true;
                }
                if(uniqueChars(guess) == codelength){
                    flag2 = true;
                }
                if(hasValidChars(guess)){
                    flag3 = true;
                }

                // Logs about errors
                if(!flag1){
                    String error1 = "Length must be " + codelength + "\n";
                    System.out.println(error1);
                    JOptionPane.showMessageDialog(null, error1, "Error in try " + round, JOptionPane.PLAIN_MESSAGE);
                }
                if(!flag2){
                    String error2 = "Unique characters must be " + codelength + "\n" ;
                    System.out.println(error2);
                    JOptionPane.showMessageDialog(null, error2, "Error in try " + round, JOptionPane.PLAIN_MESSAGE);
                }
                if(!flag3){
                    String error3 = "You must have insrted some invalid characters (valid = y, o, c, b, p, m) \n";
                    System.out.println(error3);
                    JOptionPane.showMessageDialog(null, error3, "Error in try " + round, JOptionPane.PLAIN_MESSAGE);
                }

                if(flag1 && flag2 && flag3){

                    ArrayList<Color> guessColors = convertIntoColors(guess);
                    makeColors(guessColors, round);
                    // !!!! Until this line OK !!!!
                    ArrayList<Color> hints = check(guess, code);
                    makeHints(hints, round);

                }

            }


            round++;
        }


        // Here we have 2 possibilities: User won or tries left are 0
        if(guess.equals(code)){
            String bravo = "Congratulations! You needed " + (round-1) + " tries to win the game! \n";
            System.out.println(bravo);
            JOptionPane.showMessageDialog(null, bravo, "Congratulations", JOptionPane.PLAIN_MESSAGE);
        }
        else{
            String bravo2 = "You wasted all your tries (" + tries + "). Do you want to see the correct? \n";
            System.out.println(bravo2);
            JOptionPane.showMessageDialog(null, bravo2, "Lost...", JOptionPane.PLAIN_MESSAGE);
        }

    }   // END OF MAIN FUNCTION



}