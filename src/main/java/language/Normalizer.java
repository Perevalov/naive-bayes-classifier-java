/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author urcn1_000
 */
public class Normalizer {
    List<String> stopWords;
    private final File stopWordsFile = new File ("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\com.mycompany_VoiceHelper_jar_1.0-SNAPSHOT_4\\src\\main\\java\\resources\\StopWords.txt");

    public Normalizer() {
        this.stopWords = textToWords(fileToText(stopWordsFile));
    }
    
    public List<String> prepareText(File file){
        String text = fileToText(file);
        List <String> words = textToWords(text);
        words = removeStopWords(words);
        PorterStemmer ps = new PorterStemmer();
        for (int i=0;i<words.size();i++){
            words.set(i, ps.stem(words.get(i)));
        }
        return words;
    }
    public List<String> prepareTextFromString(String text){
        List <String> words = textToWords(text);
        words = removeStopWords(words);
        PorterStemmer ps = new PorterStemmer();
        for (int i=0;i<words.size();i++){
            words.set(i, ps.stem(words.get(i)));
        }
        return words;
    }
    public String fileToText(File file){
        String text="";
        
         BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1251"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Normalizer.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Normalizer.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
 
  StringBuffer stringBuffer = new StringBuffer();
  String line = null;
 
        try {
            while((line =bufferedReader.readLine())!=null){
                
                stringBuffer.append(line).append(" ");
            }     } catch (IOException ex) {
            Logger.getLogger(Normalizer.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        return stringBuffer.toString();
    }
    public List<String> textToWords(String text){
        List <String> words = new ArrayList<String>();
        String[] wordsArray = text.split("\\s+");
        for (int i = 0; i < wordsArray.length; i++) {
            wordsArray[i] = wordsArray[i].replaceAll("[^\\p{L}\\p{Nd}]+", ":");
            String[] tmp = wordsArray[i].split(":");
            for (String s : tmp){
                words.add(s.toLowerCase());
            }
        }
        return words;
    }
    public List<String> removeStopWords(List<String>words){
        for (int i=0;i<words.size();i++){
            for (int j=0;j<stopWords.size();j++){
                if (words.get(i).equals(stopWords.get(j))){
                    words.remove(i);
                    break;
                }
            }
        }
        return words;
    }
}
