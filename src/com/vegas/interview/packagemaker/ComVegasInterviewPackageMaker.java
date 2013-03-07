/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vegas.interview.packagemaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
   private static OutputStreamWriter writeFile;
   private static int minPackagePrice;
   private static int maxPackagePrice;
   
   private static ArrayList<Number[]> Hotels = new ArrayList<>();
   private static ArrayList<Number[]> Shows = new ArrayList<>();
   private static ArrayList<Number[]> Tours = new ArrayList<>();
   private static ArrayList<Number[]> Packages = new ArrayList<>();
   
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
        
        String printM;
        int index_p = 0;
        
        for(int i = 0; i < Hotels.size();i++){
            for(int j = 0; j < Shows.size(); j++){
                for(int k = 0; k < Tours.size(); k ++){
                    Packages.add(new Number[7]);
                    Packages.get(index_p)[0] = (double)Hotels.get(i)[1] + (double)Shows.get(j)[1] + (double)Tours.get(k)[1]; // This is total package price
                    Packages.get(index_p)[1] = Hotels.get(i)[0]; // This is the id of the hotel
                    Packages.get(index_p)[3] = Shows.get(j)[0];  // This is the id of the show
                    Packages.get(index_p)[5] = Tours.get(k)[0];  // This is the id of the tour
                    Packages.get(index_p)[2] = Hotels.get(i)[1]; // This is the price for hotel
                    Packages.get(index_p)[4] = Shows.get(j)[1];  // This is the price for show
                    Packages.get(index_p)[6] = Tours.get(k)[1];  // This is the price for ticket
                    index_p ++;
                }
            }
        }
        
        sortByColumn();
        
        DecimalFormat df = new DecimalFormat("####0.00");
        for(index_p = 0; index_p < Packages.size(); index_p ++){
            if((Packages.get(index_p)[0].doubleValue() >= minPackagePrice) && (Packages.get(index_p)[0].doubleValue() <= maxPackagePrice)){
                printM = "PACKAGE\t" + df.format(Packages.get(index_p)[0]) + "\t";
                printM += "Hotels\t"+ Packages.get(index_p)[1] + "\t"+ Packages.get(index_p)[2] +"\t";
                printM += "Shows\t"+ Packages.get(index_p)[3] + "\t"+ Packages.get(index_p)[4] + "\t";
                printM += "Tours\t"+ Packages.get(index_p)[5] + "\t"+ Packages.get(index_p)[6];
                System.out.println(printM);
            }
        }
        
    }
    
    private static void sortByColumn(){
        
        char operation;
        char groupOperator = '\0'; // This groupOperator is used to keep track of the last colomn sorted
        
        MergeSort sortObject = new MergeSort();
        
        while(order.peek() != null){
            operation = order.remove();
            switch (operation){
                case'p':
                    if(groupOperator == '\0'){
                        Packages = sortObject.sort(Packages, 0);
                        groupOperator = 'p';
                    } else {
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
    
    private static void packStructures(){
        String processLine;
        int h_index = 0,s_index = 0,t_index = 0;
        StringTokenizer parser;
        
         while(readFile.hasNext()){
            processLine = readFile.nextLine();
            if(processLine.contains("HOTEL")){
                Hotels.add(new Number[2]);
                parser = new StringTokenizer(processLine,"\t ");
                parser.nextToken();
                Hotels.get(h_index)[0] = Integer.parseInt(parser.nextToken());
                Hotels.get(h_index)[1] = Double.parseDouble(parser.nextToken());
                h_index ++;
            }
           else if(processLine.contains("SHOW")){
                Shows.add(new Number[2]);
                parser = new StringTokenizer(processLine,"\t ");
                parser.nextToken();
                Shows.get(s_index)[0] = Integer.parseInt(parser.nextToken());
                Shows.get(s_index)[1] = Double.parseDouble(parser.nextToken());
                s_index ++;
            }
            else if(processLine.contains("TOUR")){
                Tours.add(new Number[2]);
                parser = new StringTokenizer(processLine,"\t ");
                parser.nextToken();
                Tours.get(t_index)[0] = Integer.parseInt(parser.nextToken());
                Tours.get(t_index)[1] = Double.parseDouble(parser.nextToken());
                t_index ++;
            }
          
        } 
    }

    private static boolean validate(String[] args) {
        
        String parameters;
        boolean flag = true;
        char readChar;
        int swap, paramLength;
        Set<Character> unique = new HashSet<>();
        
        try {
            
            file = new File(args[0]);
            readFile = new Scanner(file,"UTF-8");
            fos = new FileOutputStream(args[4]);
            writeFile = new OutputStreamWriter(fos,"UTF-8");
            
            minPackagePrice = Integer.parseInt(args[1]);
            maxPackagePrice = Integer.parseInt(args[2]);
            
        } catch (Exception ex) {
            Logger.getLogger(ComVegasInterviewPackageMaker.class.getName()).log(Level.SEVERE, null, ex);
            flag = false;
        }
        
        if(minPackagePrice > maxPackagePrice){
            swap = minPackagePrice;
            minPackagePrice = maxPackagePrice;
            maxPackagePrice = swap;
        }
        
        parameters = args[3];
        paramLength = parameters.length();
        parameters = parameters.toLowerCase();
        
        if( paramLength > 5){
            flag = false;
            System.out.println("You have too many package parameters");
        } else {
            for(int index = 0; index < paramLength; index ++){
                
                switch(readChar = parameters.charAt(index)){
                
                    case 'p':
                        if(!unique.add(readChar)){
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
                        order.add(readChar);
                        break;
                    case 's':
                        if(!unique.add(readChar)){
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;
                        }
                        order.add(readChar);
                        break;
                    case 't':
                        if(!unique.add(readChar)){  // Normally I would just return from the function, however for readability I instead set a flag
                            System.out.println("Duplicate order parameters. Cannot process");
                            flag = false;
                        }
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
