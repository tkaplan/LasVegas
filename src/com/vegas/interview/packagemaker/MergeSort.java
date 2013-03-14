/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vegas.interview.packagemaker;

import java.util.ArrayList;
import java.util.Queue;

/**
 *
 * @author na
 */
public class MergeSort {
    
    //This is just a merge sort. I used it because it can be ran concurrently (although not in this app)
    //and it is fast
    public ArrayList<Number[]> sort(ArrayList<Number[]> sortList, int compareIndex){
        
        ArrayList<Number[]> left = new ArrayList<>();
        ArrayList<Number[]> right = new ArrayList<>();
        
        if(sortList.size() <= 1){
            return sortList;
        }
        
        int middle = sortList.size()/2;
        
        for(int index = 0; index < sortList.size(); index ++){
            if(index >= middle){
                right.add(sortList.get(index));
            } else {
                left.add(sortList.get(index));
            }
        }
        
        left = sort(left, compareIndex);
        right = sort(right, compareIndex);
        
        return merge(left,right, compareIndex);
    }
    
    private ArrayList<Number[]> merge(ArrayList<Number[]> left, ArrayList<Number[]> right, int compareIndex){
        ArrayList<Number[]> result = new ArrayList<>();
        int left_i = 0, right_i = 0;
        
        while(left.size() > left_i || right.size() > right_i){
            if(left.size() > left_i && right.size() > right_i){
                if((double)left.get(left_i)[compareIndex] <= (double)right.get(right_i)[compareIndex]){
                    result.add(left.get(left_i++));
                } else {
                    result.add(right.get(right_i++));
                }
            } else if(left.size() > left_i){
                    result.add(left.get(left_i++));
            } else {
                    result.add(right.get(right_i++));
            }
        }
        
        return result;
    }
    
    /*
     * sortByColumn() does exactly what it sounds like. It takes in my Package arraylists
     * and then it begins to sort the packages by priority of columns. A column that is sorted first
     * cannot be scrambled. We then to group or block out parts of the column that was sorted.
     */
    
    public static ArrayList<Number[]> sortByColumn(ArrayList<Number[]> Packages, Queue<Character> order){
        
        /*
         * The way this function works is that it uses two values.
         * operation and groupOperator.
         * operation refers to the current column that we are trying to sort.
         * groupOperator refers to the column we want to constrain.
         * Given the two operators, which sort by priority.
         */
        
        char operation;
        char groupOperator = '\0';              // This groupOperator is used to keep track of the last colomn sorted
        MergeSort sortObject = new MergeSort(); // I could have made the method static so this wouldn't have been necessary.
        
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
                        orderGrpCol(groupOperator,'p',Packages);
                        groupOperator = 'p';
                    }
                    break;
                case'h':
                    if(groupOperator == '\0'){
                        Packages = sortObject.sort(Packages, 2);
                        groupOperator = 'h';
                    } else {
                        orderGrpCol(groupOperator,'h',Packages);
                        groupOperator = 'h';
                    }
                    break;
                case's':
                    if(groupOperator == '\0'){
                        Packages = sortObject.sort(Packages, 4);
                        groupOperator = 's';
                    } else {
                        orderGrpCol(groupOperator,'s',Packages);
                        groupOperator = 's';
                    }
                    break;
                case't':
                    if(groupOperator == '\0'){
                        Packages = sortObject.sort(Packages, 6);
                        groupOperator = 't';
                    } else {
                        orderGrpCol(groupOperator,'t',Packages);
                        groupOperator = 't';
                    }
                    break;
            }
        }
        return Packages;
    }
    
    // Because we are sorting by priority, this makes the problem more difficult
    // This function takes care of it all
    private static void orderGrpCol(char groupOperator, char sortCol, ArrayList<Number[]> Packages) {
        
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
