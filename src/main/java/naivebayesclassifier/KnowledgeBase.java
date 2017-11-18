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
public class KnowledgeBase {
    List<Class> classes = new ArrayList<Class>();

    public KnowledgeBase() {
    }
    
    public void add(Class c){
        classes.add(c);
    }
    public float countClassProbability(Class c){
        if (classes.size()==0)
            return (float) 0.0;
        int allWords = 0;
        for (Class cl : classes){
            allWords+=cl.getWords().size();
        }
        return (float) ((c.getWords().size()*1.0)/(allWords*1.0));
    }
    
}
