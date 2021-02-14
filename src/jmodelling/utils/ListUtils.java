/*
 * MIT License
 * 
 * Copyright (c) 2021 Antonio GE
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package jmodelling.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ANTONIO
 */
public class ListUtils {

    public static boolean areIndicesInRange(List<?> list, List<Integer> indices) {
        return indices.stream().noneMatch((index) -> (index < 0 || index >= list.size()));
    }

    public static boolean areIndicesInRange(List<?> list, Integer... indices) {
        return areIndicesInRange(list, Arrays.asList(indices));
    }

    public static boolean hasDuplicatedValues(List<?> list) {
        HashSet<?> set = new HashSet<>(list);
        return set.size() != list.size();
    }

    public static <T> boolean hasDuplicatedValues(T... list) {
        return hasDuplicatedValues(Arrays.asList(list));
    }

    public static <T> boolean hasDuplicates(List<T> list){
        Set<T> set = Collections.newSetFromMap(new IdentityHashMap<>());
        set.addAll(list);
        return list.size() != set.size();
    }
    
    public static <T> boolean hasDuplicates(T... list){
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

    public static void removeAll(List<?> list, List<Integer> indices) {
        indices.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(list::remove);
    }
    
    public static void removeAll(List<?> list, Integer... indices){
        removeAll(list, Arrays.asList(indices));
    }

    public static boolean areSameSize(List<?>... lists){
        int size = lists[0].size();
        for(List list : lists){
            if(list.size() != size){
                return false;
            }
        }
        return true;
    }
    
    
}
