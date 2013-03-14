/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vegas.interview.packagemaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

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
        MergeSort sort = new MergeSort();
        
        if (args.length != 5){
            System.out.println("You do not have the right number of arguments.");
            System.exit(1);
        } else if(!validate(args)) { // validate is a workhorse function that makes sure that user input is accurate
            System.out.println("Could not process. Your command is not valid.");
            System.exit(1);
        }
        // If our file has the wrong format, packstructure will let us know
        if(packStructures()){// Pack Hotels, Shows, Tours   
            packageMaker();     // Makes packages 
            Packages = sort.sortByColumn(Packages,order);   //Sorts our packages
            writeToFile();      // Write to our file
        }
        
        System.setOut(consoleStream); //Since my System.out is printing to the file rather than console, I need to redirect it back
        
        try{
            readFile.close();   // We don't need our file reader anymore
            fos.close();        // We need to save and close out the file
            writeFile.close();
        } catch(IllegalStateException ex){   // Error handling. 
            System.out.println("Couldn't close file.");
        } catch(IOException ex){
            System.out.println("Couldn't close file output stream.");
        }
        
        
    }
    
    private static void writeToFile(){
        
        String printM;
        int index_p = 0;
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
            
            if((Packages.get(index_p)[0].doubleValue() >= (double)minPackagePrice - .001) && (Packages.get(index_p)[0].doubleValue() <= (double)maxPackagePrice + .001)){
                printM = "PACKAGE\t" + df.format(Packages.get(index_p)[0]) + "\t";
                if(displayHotel){
                    printM += "Hotels\t"+ Packages.get(index_p)[1] + "\t"+ df.format(Packages.get(index_p)[2]) +"\t";
                }else{
                    printM +="\t\t\t\t\t"; // If column is not to be included
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
    }
    
    private static void packageMaker(){ // Creates our packages
        
   /*
    * These for loops are used to make our packages.
    * Since our packages consists of ..
    * Package Price : Hotel Id : Hotel Price : Show Id : Show Price : Ticket Id : Ticket Price
    * We use an arraylist that can hold a Numbers[] object.
    * This allows us to type cast to the child classes later, yaaay for polymorphism!
    */
        
        int index_p = 0;
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
                        Packages.get(index_p)[5] = Tours.get(k)[0];  // This is the id of the tour
                        Packages.get(index_p)[6] = Tours.get(k)[1];  // This is the price for ticket
                    }else{
                        k = Tours.size(); // We want to skip display tours, this helps prevent duplicates in the file
                    }
                    
                    index_p ++;
                }
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
    
    private static boolean packStructures(){
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
                try{
                    Hotels.get(h_index)[0] = Integer.parseInt(parser.nextToken());  // Pack our ID
                    Hotels.get(h_index)[1] = Double.parseDouble(parser.nextToken());// Pack our Price
                }catch(Exception ex){ // This will generalize to some problem associated with the file format
                    System.setOut(consoleStream);
                    System.out.println("Your file does not have the right format.");
                    return false;
                }
                h_index ++;
            }
           else if(processLine.contains("SHOW")){
                Shows.add(new Number[2]);
                parser = new StringTokenizer(processLine,"\t ");
                parser.nextToken();
                try{
                Shows.get(s_index)[0] = Integer.parseInt(parser.nextToken());   // Pack our ID
                Shows.get(s_index)[1] = Double.parseDouble(parser.nextToken()); // Pack our Price
                } catch(Exception ex){
                    System.setOut(consoleStream);
                    System.out.println("Your file does not have the right format.");
                    return false;                
                }
                s_index ++;
            }
            else if(processLine.contains("TOUR")){
                try{
                Tours.add(new Number[2]);
                parser = new StringTokenizer(processLine,"\t ");
                parser.nextToken();
                Tours.get(t_index)[0] = Integer.parseInt(parser.nextToken());   // Pack our ID
                Tours.get(t_index)[1] = Double.parseDouble(parser.nextToken()); // Pack our Price
                } catch(Exception ex){
                    System.setOut(consoleStream);
                    System.out.println("Your file does not have the right format.");
                    return false;                
                }
                t_index ++;
            } else {
                System.setOut(consoleStream);
                System.out.println("Your file does not have the right format.");
                return false;
            }
          
        }
         return true; // If we made it out of the while loop then everything went well
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
            
        } catch (FileNotFoundException ex) {
            System.setOut(consoleStream);
            System.out.println("Could Not Find File");
            flag = false;
        } catch (SecurityException ex){
            System.setOut(consoleStream);
            System.out.println("Security Exception Thrown.");
        } catch (NumberFormatException ex){
            System.setOut(consoleStream);
            System.out.println("Your input parameter is not an integer.");
        }
        
        //Incase the user mixed up the package price order
        if(minPackagePrice > maxPackagePrice){
            swap = minPackagePrice;
            minPackagePrice = maxPackagePrice;
            maxPackagePrice = swap;
        }
        
        if(minPackagePrice == maxPackagePrice){
            System.setOut(consoleStream);
            System.out.println("You cannot make minPackagePrice == maxPackagePrice");
            flag = false;
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
                        if(!unique.add(readChar)){  // We use a set to flag duplicates
                            System.setOut(consoleStream);
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;           // Normally I would just return from the function, however for readability I instead set a flag
                        }
                        order.add(readChar);
                        break;
                    case 'h':
                        if(!unique.add(readChar)){
                            System.setOut(consoleStream);
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;
                        }
                        displayHotel = true;
                        order.add(readChar);
                        break;
                    case 's':
                        if(!unique.add(readChar)){
                            System.setOut(consoleStream);
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;
                        }
                        displayShow = true;
                        order.add(readChar);
                        break;
                    case 't':
                        if(!unique.add(readChar)){
                            System.setOut(consoleStream);
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;
                        }
                        displayTour = true;
                        order.add(readChar);
                        break;
                    default:
                        return false;
                
                }
            
            }
        }
        
        return flag;
    }

}
