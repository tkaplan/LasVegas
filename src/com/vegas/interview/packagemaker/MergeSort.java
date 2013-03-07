/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vegas.interview.packagemaker;

import java.util.ArrayList;

/**
 *
 * @author na
 */
public class MergeSort {
    
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
}
