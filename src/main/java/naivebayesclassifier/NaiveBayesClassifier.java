/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayesclassifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import language.Normalizer;
import language.PorterStemmer;


/**
 *
 * @author urcn1_000
 */
public class NaiveBayesClassifier {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        File f = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\NaiveBayesClassifier\\src\\resources\\hello.txt");
        File f1 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\NaiveBayesClassifier\\src\\resources\\howareyou.txt");
        File f2 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\NaiveBayesClassifier\\src\\resources\\whatareyoudoing.txt");
        File f3 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\NaiveBayesClassifier\\src\\resources\\whatdoyoulike.txt");
        File f4 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\NaiveBayesClassifier\\src\\resources\\name.txt");
        File f5 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\NaiveBayesClassifier\\src\\resources\\bye.txt");
        Normalizer n = new Normalizer();
        KnowledgeBase kb = new KnowledgeBase();
        Class hello = new Class("Приветствие",n.prepareText(f));
        kb.add(hello);
        Class how = new Class("Как дела",n.prepareText(f1));
        kb.add(how);
        Class doing = new Class("Что делаешь",n.prepareText(f2));
        kb.add(doing);
        Class like = new Class("Предпочтения",n.prepareText(f3));
        kb.add(like);
        Class name = new Class("Имя",n.prepareText(f4));
        kb.add(name);
        Class bye = new Class("Прощание",n.prepareText(f5));
        kb.add(bye);
        
        while (true){
           System.out.println("Введите что нибудь:");
           Scanner in = new Scanner(System.in,"Cp1251");
           String s = in.nextLine();
           System.out.println("Это: "+ classify(s,kb));//ps.stepOne(s));
        }
    }
    public static String classify(String s,KnowledgeBase kb){
        Normalizer n = new Normalizer();
        List<String> text = n.prepareTextFromString(s);
        Map probabilities = new HashMap<Float,String>();
        
        for (Class c : kb.classes){
            float classProbability = kb.countClassProbability(c);
            float multiply = (float) 1.0;
            
            for (String sWord: text){
            int counter = 0;
                for (String classWord: c.getWords()){
                    if (sWord.equals(classWord)){
                        counter++;
                    }
                }
                multiply=(float) (multiply*((counter + 0.7)/(classProbability + c.getUniqueWordsSize()*0.7))*1.0);
            }
            float result = multiply*classProbability;
            probabilities.put(result,c.getName());
        }
        String result ="";
        float maxValue = (float) probabilities.keySet().toArray()[0];
        
        for (int i=0;i<probabilities.size();i++){
            if ((float)probabilities.keySet().toArray()[i]>maxValue){
                maxValue = (float)probabilities.keySet().toArray()[i];
            }
        }
        result = probabilities.get(maxValue).toString();
        return result;
    }
    
}
