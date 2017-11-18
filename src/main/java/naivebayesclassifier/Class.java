/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayesclassifier;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author urcn1_000
 */
public class Class {
    private String name;
    private List<String> words;
    private int uniqueWordsSize;

    public Class(String name, List<String> words) {
        this.name = name;
        this.words = words;
        this.uniqueWordsSize = countUniqueWordsSize(this.words);
    }
    
    private int countUniqueWordsSize(List<String> words){
        int count = 0;
        List<String> unique = new ArrayList<String>();
        
        for (String s : words){
            if (!unique.contains(s)){
                unique.add(s);
            }
        }
        return unique.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public int getUniqueWordsSize() {
        return uniqueWordsSize;
    }

    public void setUniqueWordsSize(int uniqueWordsSize) {
        this.uniqueWordsSize = uniqueWordsSize;
    }
    
    
}
