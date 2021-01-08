/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmodelling.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author ANTONIO
 */
public class ArrayUtils {

    public static boolean areIndicesInRange(List<?> list, List<Integer> indices) {
        return indices.stream().noneMatch((index) -> (index < 0 || index >= list.size()));
    }

    public static boolean areIndicesInRange(List<?> list, Integer... indices) {
        return areIndicesInRange(list, Arrays.asList(indices));
    }

    public static boolean hasDuplicates(List<?> list) {
        HashSet<?> set = new HashSet<>(list);
        return set.size() != list.size();
    }

    public static <T> boolean hasDuplicates(T... list) {
        return hasDuplicates(Arrays.asList(list));
    }

    public static <T> List<T> getSubList(List<T> list, List<Integer> indices) {
        List<T> subList = new ArrayList<>(indices.size());
        indices.forEach((index) -> {
            subList.add(list.get(index));
        });
        return subList;
    }

    public static <T> List<T> getSubList(List<T> list, Integer... indices) {
        return getSubList(list, Arrays.asList(indices));
    }

    
}
