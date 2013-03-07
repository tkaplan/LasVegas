/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vegas.interview.packagemaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author na
 */
public class ComVegasInterviewPackageMaker {

    /**
     * @param args the command line arguments
     */
   private static Queue<Character> order = new LinkedList<>();
   private static File file;
   private static Scanner readFile;
   private static FileOutputStream fos;
   private static PrintStream writeFile;
   private static PrintStream consoleStream = System.out;
   private static int minPackagePrice;
   private static int maxPackagePrice;
   
   private static ArrayList<Number[]> Hotels = new ArrayList<>();
   private static ArrayList<Number[]> Shows = new ArrayList<>();
   private static ArrayList<Number[]> Tours = new ArrayList<>();
   private static ArrayList<Number[]> Packages = new ArrayList<>();
   
   private static boolean displayHotel = false;
   private static boolean displayShow = false;
   private static boolean displayTour = false;
   
   final static String PACKAGE="PACKAGE";
    
    public static void main(String[] args) {
        
        if (args.length != 5){
            System.out.println("You do not have the right number of arguments.");
            System.exit(1);
        } else if(!validate(args)) { // validate is a workhorse function that makes sure that user input is accurate
            System.out.println("Your command is not valid.");
            System.exit(1);
        }
        
        packStructures();
        
        readFile.close();
        
        String printM;
        int index_p = 0;
        
        /*
         * These for loops are used to make our packages.
         * Since our packages consists of ..
         * Package Price : Hotel Id : Hotel Price : Show Id : Show Price : Ticket Id : Ticket Price
         * We use an arraylist that can hold a Numbers[] object.
         * This allows us to type cast to the child classes later, yaaay for polymorphism!
         */
        
        for(int i = 0; i < Hotels.size();i++){
            for(int j = 0; j < Shows.size(); j++){
                for(int k = 0; k < Tours.size(); k ++){
                    
                    // We need to add a new number object to our Package arraylist
                    
                    Packages.add(new Number[7]);
                    
                    if(displayHotel){
                        Packages.get(index_p)[0] = (double)Hotels.get(i)[1];
                        Packages.get(index_p)[1] = Hotels.get(i)[0]; // This is the id of the hotel
                        Packages.get(index_p)[2] = Hotels.get(i)[1]; // This is the price for hotel
                    }else{
                        i = Hotels.size(); // This skips hotels, keeps us from entering duplicate records
                    }
                    if(displayShow){
                        if(Packages.get(index_p)[0] != null){
                            Packages.get(index_p)[0] = Packages.get(index_p)[0].doubleValue() + (double)Shows.get(j)[1];
                        }else{
                            Packages.get(index_p)[0] = (double)Shows.get(j)[1];
                        }
                        Packages.get(index_p)[3] = Shows.get(j)[0];  // This is the id of the show
                        Packages.get(index_p)[4] = Shows.get(j)[1];  // This is the price for show
                    }else{
                        j = Shows.size(); // We want to skip shows, this stops duplicates in our file
                    }
                    if(displayTour){
                        if(Packages.get(index_p)[0] != null){
                            Packages.get(index_p)[0] = Packages.get(index_p)[0].doubleValue() + (double)Tours.get(k)[1];
                        }else{
                            Packages.get(index_p)[0] = (double)Tours.get(k)[1];
                        }
                        Packages.get(index_p)[0] = Packages.get(index_p)[0].doubleValue() + (double)Tours.get(k)[1];
                        Packages.get(index_p)[5] = Tours.get(k)[0];  // This is the id of the tour
                        Packages.get(index_p)[6] = Tours.get(k)[1];  // This is the price for ticket
                    }else{
                        k = Tours.size(); // We want to skip display tours, this helps prevent duplicates in the file
                    }
                    
                    index_p ++;
                }
            }

        }
        
        /*
         * sortByColumn() does exactly what it sounds like. It takes in my Package arraylists
         * and then it begins to sort the packages by priority of columns. A column that is sorted first
         * cannot be scrambled. We then to group or block out parts of the column that was sorted.
         */
        
        sortByColumn();
        
        /*
         * I was having problems with display my doubles since they go out so far. Honestly speaking, floats would
         * have been more appropriate however, when tackling this problem I initially used doubles. I simply solved
         * the problem of display when using DecimalFormat object. Sorry for wasting space.
         */
        
        DecimalFormat df = new DecimalFormat("#####.00");
        for(index_p = 0; index_p < Packages.size(); index_p ++){
            
            /*
             * Here we need to print our to the file. I am use the same format as you specified in the problem.
             * I use 3 boolean variables to tell the program which column to display.
             * displayHote, displayShow, and displayTour all tell the computer what to display.
             */
            
            if((Packages.get(index_p)[0].doubleValue() >= minPackagePrice) && (Packages.get(index_p)[0].doubleValue() <= maxPackagePrice)){
                printM = "PACKAGE\t" + df.format(Packages.get(index_p)[0]) + "\t";
                if(displayHotel){
                    printM += "Hotels\t"+ Packages.get(index_p)[1] + "\t"+ df.format(Packages.get(index_p)[2]) +"\t";
                }else{
                    printM +="\t\t\t\t\t";
                }
                if(displayShow){
                    printM += "Shows\t"+ Packages.get(index_p)[3] + "\t"+ df.format(Packages.get(index_p)[4]) + "\t";
                }else{
                    printM +="\t\t\t\t\t";
                }
                if(displayTour){
                    printM += "Tours\t"+ Packages.get(index_p)[5] + "\t"+ df.format(Packages.get(index_p)[6]);
                }else{
                    printM +="\t\t\t\t\t";
                }
                System.out.println(printM);
            }
        }
        
        try{
            fos.close(); // We need to save and close out the file
            writeFile.close();
        } catch(Exception e){ // Error handling. 
            System.setOut(consoleStream); //Since my System.out is printing to the file rather than console, I need to redirect it back
            System.out.println("Couldn't close file.");
        }
        
    }
    
    private static void sortByColumn(){
        
        /*
         * The way this function works is that it uses two values.
         * operation and groupOperator.
         * operation refers to the current column that we are trying to sort.
         * groupOperator refers to the column we want to constrain.
         * Given the two operators, which sort by priority.
         */
        
        char operation;
        char groupOperator = '\0'; // This groupOperator is used to keep track of the last colomn sorted
        
        MergeSort sortObject = new MergeSort(); // I thought mergesort was a fairly appropriate sort for this problem
        
        /*
         * Cool this about MergeSorts is that they can be optimized to compute in parallel.
         */
        
        /*
         * Our order object is actually a queue. This was the most appropriate structure to use
         * since we are sorting FIFO.
         */
        
        while(order.peek() != null){
            operation = order.remove();
            switch (operation){
                case'p':
                    if(groupOperator == '\0'){ // If groupOperator is this, we need not contstrain any columns.
                        Packages = sortObject.sort(Packages, 0);
                        groupOperator = 'p';
                    } else {
                        // orderGrpCol() is a function that allows us to sort with constrained columns
                        // the groupOperator tells the function which column we are constraining while the
                        // 'p' literal tells us which column we want to sort.
                        orderGrpCol(groupOperator,'p');
                        groupOperator = 'p';
                    }
                    break;
                case'h':
                    if(groupOperator == '\0'){
                        Packages = sortObject.sort(Packages, 2);
                        groupOperator = 'h';
                    } else {
                        orderGrpCol(groupOperator,'h');
                        groupOperator = 'h';
                    }
                    break;
                case's':
                    if(groupOperator == '\0'){
                        Packages = sortObject.sort(Packages, 4);
                        groupOperator = 's';
                    } else {
                        orderGrpCol(groupOperator,'s');
                        groupOperator = 's';
                    }
                    break;
                case't':
                    if(groupOperator == '\0'){
                        Packages = sortObject.sort(Packages, 6);
                        groupOperator = 't';
                    } else {
                        orderGrpCol(groupOperator,'t');
                        groupOperator = 't';
                    }
                    break;
            }
        }
    }
    
    /*
     * packStructures allows us to read the file and pack our arraylists ...
     * Hotels, Shows, and Tours
     * Each array lists consists of Elements Number[].
     * The Number[] array is size 2 with the struct as...
     * Item Id : Item Price
     */
    
    private static void packStructures(){
        // We want to process each line of data, processLine helps us
        String processLine;
        int h_index = 0,s_index = 0,t_index = 0;
        
        /*
         * The stringtokenizer allows us to easily seperate our information.
         * I believe it works like an iterator so that you just call it again
         * to get the next element.
         */
        StringTokenizer parser;
        
         while(readFile.hasNext()){
            processLine = readFile.nextLine();
            if(processLine.contains("HOTEL")){
                Hotels.add(new Number[2]);
                parser = new StringTokenizer(processLine,"\t ");
                parser.nextToken();
                Hotels.get(h_index)[0] = Integer.parseInt(parser.nextToken());  // Pack our ID
                Hotels.get(h_index)[1] = Double.parseDouble(parser.nextToken());// Pack our Price
                h_index ++;
            }
           else if(processLine.contains("SHOW")){
                Shows.add(new Number[2]);
                parser = new StringTokenizer(processLine,"\t ");
                parser.nextToken();
                Shows.get(s_index)[0] = Integer.parseInt(parser.nextToken());   // Pack our ID
                Shows.get(s_index)[1] = Double.parseDouble(parser.nextToken()); // Pack our Price
                s_index ++;
            }
            else if(processLine.contains("TOUR")){
                Tours.add(new Number[2]);
                parser = new StringTokenizer(processLine,"\t ");
                parser.nextToken();
                Tours.get(t_index)[0] = Integer.parseInt(parser.nextToken());   // Pack our ID
                Tours.get(t_index)[1] = Double.parseDouble(parser.nextToken()); // Pack our Price
                t_index ++;
            }
          
        } 
    }

    /*
     * This function will save the day with command line validation.
     * It does everything for us which includes ...
     *  -Determining the right sort order and which columns to display.
     *  -Finding the range of accepted package price values.
     *  -Flagging duplicate commands
     *  -Sets up our file readers/writers
     *  -Determines if we have right amount of parameters
     */
    
    private static boolean validate(String[] args) {
        
        String parameters;
        boolean flag = true;
        char readChar;      // This is used to hold column info for packing our order queue
        int swap, paramLength;
        Set<Character> unique = new HashSet<>();
        
        try {
            //Setting up our file readers and writers
            file = new File(args[0]);
            readFile = new Scanner(file,"UTF-8");
            fos = new FileOutputStream(args[4]);
            writeFile = new PrintStream(fos);
            System.setOut(writeFile);
            
            //Finding package price range
            minPackagePrice = Integer.parseInt(args[1]);
            maxPackagePrice = Integer.parseInt(args[2]);
            
        } catch (Exception ex) {
            Logger.getLogger(ComVegasInterviewPackageMaker.class.getName()).log(Level.SEVERE, null, ex);
            flag = false;
        }
        
        //Incase the user mixed up the package price order
        if(minPackagePrice > maxPackagePrice){
            swap = minPackagePrice;
            minPackagePrice = maxPackagePrice;
            maxPackagePrice = swap;
        }
        
        parameters = args[3];
        paramLength = parameters.length();
        parameters = parameters.toLowerCase(); // we read in lower case
        
        if( paramLength > 5){
            flag = false;
            System.out.println("You have too many package parameters");
        } else {
            for(int index = 0; index < paramLength; index ++){
                
                switch(readChar = parameters.charAt(index)){
                
                    case 'p':
                        if(!unique.add(readChar)){ // We use a set to flag duplicates
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;
                        }
                        order.add(readChar);
                        break;
                    case 'h':
                        if(!unique.add(readChar)){
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;
                        }
                        displayHotel = true;
                        order.add(readChar);
                        break;
                    case 's':
                        if(!unique.add(readChar)){
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;
                        }
                        displayShow = true;
                        order.add(readChar);
                        break;
                    case 't':
                        if(!unique.add(readChar)){  // Normally I would just return from the function, however for readability I instead set a flag
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;
                        }
                        displayTour = true;
                        order.add(readChar);
                        break;
                    default:
                        flag = false;
                        break;
                
                }
            
            }
        }
        
        return flag;
    }

    // Because we are sorting by priority, this makes the problem more difficult
    // This function takes care of it all
    private static void orderGrpCol(char groupOperator, char sortCol) {
        
        ArrayList<Number[]> sortList = new ArrayList<>();
        int packageIndex,groupOpIndex = 0, sortColIndex = 0, offset, listIndex;
        
        MergeSort sort = new MergeSort();
        
        double currentPrice;
        
        switch(groupOperator){
            case 'p':
                groupOpIndex = 0; // package price
                break;
            case 'h':
                groupOpIndex = 2; // hotel price
                break;
            case 's':
                groupOpIndex = 4; // show price
                break;
            case 't':
                groupOpIndex = 6; // tour price
                break;
        
        }
        
        switch(sortCol){
            case 'p':
                sortColIndex = 0; // package price
                break;
            case 'h':
                sortColIndex = 2; // hotel price
                break;
            case 's':
                sortColIndex = 4; // show price
                break;
            case 't':
                sortColIndex = 6; // tour price
                break;
        
        }
        
        currentPrice = (double)Packages.get(0)[groupOpIndex];
        // This for loop allows us to sort by group
        for(packageIndex = 0;  packageIndex < Packages.size(); packageIndex ++){
            // Since I'm using double percision, I would like to approximate the difference to get
            // the same answer as lasvegas.com
            if(Math.abs(currentPrice - (double)Packages.get(packageIndex)[groupOpIndex])<.001){
                sortList.add(Packages.get(packageIndex));
            } else {
                sortList = sort.sort(sortList, sortColIndex);
                offset = packageIndex - sortList.size();
                for(listIndex = 0; listIndex < sortList.size(); listIndex++){
                    Packages.set(listIndex + offset,sortList.get(listIndex));
                }
                sortList.clear();
                currentPrice = (double)Packages.get(packageIndex)[groupOpIndex];
                sortList.add(Packages.get(packageIndex));
            }
        }
        
        if(sortList.size()>0){ // Lets not forget about sorting the last group
            sortList = sort.sort(sortList, sortColIndex);
            offset = packageIndex - sortList.size();
            for(listIndex = 0; listIndex < sortList.size(); listIndex++){
                Packages.set(listIndex + offset,sortList.get(listIndex));
            }
        }
    }
}
