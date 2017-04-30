/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reminder.dataobjects;

import java.util.Comparator;

/**
 *
 * @author Dariusz
 */
public class NameDatePairComparator implements Comparator<NameDatePair>{
    @Override
    public int compare(NameDatePair o1, NameDatePair o2) {
        return o1.getDate().compareTo(o2.getDate());
    } 
}
