  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author urcn1_000
 */
public class PorterStemmer {
    private final char[] VOWELS = {'а','е','и', 'о','у','ы','э','ю','я'};
    private final char[] CONSONANTS = {'б','в','г','д','ж','з','й','к','л','м','н','п','р','с','т','ф','х','ц','ч','ш','щ'};
    private final String [] PERFECTIVE_GERUND_G1 = {"ав","авши","авшись"};
    private final String [] PERFECTIVE_GERUND_G11 = {"яв","явши","явшись"};
    private final String [] PERFECTIVE_GERUND_G2 = {"ив","ивши","ившись","ыв","ывши","ывшись"};
    private final String [] ADJECTIVES = {"ее", "ие", "ые", "ое", "ими", "ыми", "ей", "ий", "ый", "ой", "ем", "им", "ым", 
        "ом", "его", "ого", "ему", "ому", "их", "ых", "ую", "юю", "ая", "яя", "ою", "ею"};
    private final String [] PARTICIPLE_G1 = {"аем", "анн", "авш", "ающ", "ащ"};
    private final String [] PARTICIPLE_G11 = {"яем", "янн", "явш", "яющ", "ящ"};
    private final String [] PARTICIPLE_G2 = {"ивш", "ывш", "ующ"};
    private final String [] REFLEXIVE = {"ся","сь"};
    private final String [] VERB_G1 = {"ала", "ана", "аете", "айте", "али", "ай", "ал", "аем", "ан", "ало", "ано", "ает", "ают", "аны", "ать", "аешь", "анно"};
    private final String [] VERB_G11 = {"яла", "яна", "яете", "яйте", "яли", "яй", "ял", "яем", "ян", "яло", "яно", "яет", "яют", "яны", "ять", "яешь", "янно"};
    private final String [] VERB_G2 = {"ила", "ыла", "ена", "ейте", "уйте", "ите", "или", "ыли", "ей", "уй", "ил", "ыл", 
        "им", "ым", "ен", "ило", "ыло", "ено", "ят", "ует", "уют", "ит", "ыт", "ены", "ить", "ыть", "ишь", "ую", "ю"};
    private final String [] NOUN = {"а", "ев", "ов", "ие", "ье", "е", "иями", "ями", "ами", "еи", "ии", "и", "ией", "ей",
        "ой", "ий", "й", "иям", "ям", "ием", "ем", "ам", "ом", "о", "у", "ах", "иях", "ях", "ы", "ь", "ию", "ью", "ю", "ия", "ья", "я","ин"};
    private final String [] SUPERLATIVE = {"ейш","ейше"};
    private final String [] DERIVATIONAL = {"ост","ость"};
    private String [] ADJECTIVAL = null;
    
    public String getRv(String word){
        int vowelCount = 0;
        String rv = "";
        for (int i=0;i<word.length();i++){
            if (vowelCount>0){
                rv+= "" + word.charAt(i);
            }
            for (int j=0;j<VOWELS.length;j++){
                if (word.charAt(i)==VOWELS[j]){
                    vowelCount++;
                }
            }
        }
        if (vowelCount==0){
            return word;
        }else{
            return rv;
        }
    }
    private String getR1(String word){
        int vowelCount =0;
        int consonantCount = 0;
        String r1 = "";
        boolean check = false;
        for (int i =0;i<word.length();i++){
            if (vowelCount>0 && consonantCount>0){
                r1+=""+word.charAt(i);
            }
            for (int j=0;j<VOWELS.length;j++){
                if(check)
                    break;
                if (word.charAt(i)==VOWELS[j]){
                    if (vowelCount==0)
                        vowelCount++;
                    else
                        vowelCount=0;
                }else{
                    if (vowelCount>0)
                        consonantCount++;
                }
            }
        }
        if (!check)
            return word;
        else 
            return r1;
    }
    private String getR2(String r1){
        return getR1(r1);
    }
    private void initializeAdjectival(){
        List<String> tmp = new ArrayList<String>();
        
        for (int i=0;i<ADJECTIVES.length;i++){
            tmp.add(ADJECTIVES[i]);
        }
        for (int i=0;i<PARTICIPLE_G1.length;i++){
            for (int j=0;j<ADJECTIVES.length;j++){
                tmp.add(PARTICIPLE_G1[i]+ADJECTIVES[j]);
            }
        }
        for (int i=0;i<PARTICIPLE_G11.length;i++){
            for (int j=0;j<ADJECTIVES.length;j++){
                tmp.add(PARTICIPLE_G11[i]+ADJECTIVES[j]);
            }
        }
        for (int i=0;i<PARTICIPLE_G2.length;i++){
            for (int j=0;j<ADJECTIVES.length;j++){
                tmp.add(PARTICIPLE_G2[i]+ADJECTIVES[j]);
            }
        }
        ADJECTIVAL = new String[tmp.size()];
        for (int i=0;i<tmp.size();i++){
            ADJECTIVAL[i]=tmp.get(i);
        }
    }
    public PorterStemmer() {
        initializeAdjectival();
    }
    public String stepTwo(String word){
         if (Pattern.compile(".*и$").matcher(getRv(word)).matches()){
                 String replace = word.replaceAll("и$", "");
                 return replace;
            }
         return word;
    }
    public String stem(String word){
        String stem = word;
        stem = stepOne(stem);
        stem = stepTwo(stem);
        stem = stepThree(stem);
        stem = stepFour (stem);
        return stem;
    }
    public String stepThree(String word){
        List<String> replacedResults = new ArrayList<String>();
        
        for (int i=0;i<DERIVATIONAL.length;i++){
            if (Pattern.compile(".*"+DERIVATIONAL[i]+"$").matcher(getRv(word)).matches()){
                String replace = word.replaceAll(DERIVATIONAL[i]+"$", "");
                replacedResults.add(replace);
            }
        }
        if (replacedResults.size()>0)
            return findShortestString(replacedResults);
        else
            return word;
    }
    public String stepFour(String word){
        if (Pattern.compile(".*нн$").matcher(getRv(word)).matches()){
                 String replace = word.replaceAll("н$", "");
                 return replace;
            }
        else{
                List<String> replacedResults = new ArrayList<String>();
        
                for (int i=0;i<SUPERLATIVE.length;i++){
                    if (Pattern.compile(".*"+SUPERLATIVE[i]+"$").matcher(getRv(word)).matches()){
                        String replace = word.replaceAll(SUPERLATIVE[i]+"$", "");
                        replacedResults.add(replace);
                    }
                }
                if (replacedResults.size()>0){
                    if (Pattern.compile(".*нн$").matcher(getRv(findShortestString(replacedResults))).matches()){
                            String replace = findShortestString(replacedResults).replaceAll("н$", "");
                            return replace;
                       }
                    else
                        return findShortestString(replacedResults);
                }
            }
        if (Pattern.compile(".*ь$").matcher(getRv(word)).matches()){
                 String replace = word.replaceAll("ь$", "");
                 return replace;
            }
        return word;
    }
    public String stepOne(String word){
        String stemmed = removePerfetiveGerundIfExists(word);
        String tmp = "";
        if (!stemmed.equals("false"))
            return stemmed;
        
        stemmed = removeReflexiveIfExists(word);
        if (stemmed.equals("false"))
            stemmed = word;
       
        tmp = stemmed;
        stemmed = removeAdjectivalIfExists(stemmed);
        if (!stemmed.equals("false"))
            return stemmed;
        else
            stemmed = tmp;
        
        stemmed = removeVerbIfExists(stemmed);
        if (!stemmed.equals("false"))
            return stemmed;
        else
            stemmed = tmp;
        
        stemmed = removeNounIfExists(stemmed);
        if (stemmed.equals("false"))
            return tmp;
        else
            return stemmed;   
    }
    public String removePerfetiveGerundIfExists(String word){
        int index=0;
        String replace = "false";
        List<String> replacedResults = new ArrayList<String>();
        for (int i=0;i<PERFECTIVE_GERUND_G1.length;i++){
            if (Pattern.compile(".*"+PERFECTIVE_GERUND_G1[i]+"$").matcher(getRv(word)).matches()){
                 replace = word.replaceAll(PERFECTIVE_GERUND_G1[i]+"$", "");
                 replacedResults.add(replace);
            }
        }
        
        for (int i=0;i<PERFECTIVE_GERUND_G11.length;i++){
            if (Pattern.compile(".*"+PERFECTIVE_GERUND_G11[i]+"$").matcher(getRv(word)).matches()){
                 replace = word.replaceAll(PERFECTIVE_GERUND_G11[i]+"$", "");
                 replacedResults.add(replace);
            }
        }
        for (int i=0;i<PERFECTIVE_GERUND_G2.length;i++){
            if (Pattern.compile(".*"+PERFECTIVE_GERUND_G2[i]+"$").matcher(getRv(word)).matches()){
                 replace = word.replaceAll(PERFECTIVE_GERUND_G2[i]+"$", "");
                 replacedResults.add(replace);
            }
        }
        if (replacedResults.size()>0)
            return findShortestString(replacedResults);
        else
            return replace;
    }
    private String removeVerbIfExists(String word){
        List<String> replacedResults = new ArrayList<String>();
        
        for (int i=0;i<VERB_G1.length;i++){
            if (Pattern.compile(".*"+VERB_G1[i]+"$").matcher(getRv(word)).matches()){
                String replace = word.replaceAll(VERB_G1[i]+"$", "");
                replacedResults.add(replace);
            }
        }
        for (int i=0;i<VERB_G11.length;i++){
            if (Pattern.compile(".*"+VERB_G11[i]+"$").matcher(getRv(word)).matches()){
                String replace = word.replaceAll(VERB_G11[i]+"$", "");
                replacedResults.add(replace);
            }
        }
        for (int i=0;i<VERB_G2.length;i++){
            if (Pattern.compile(".*"+VERB_G2[i]+"$").matcher(getRv(word)).matches()){
                String replace = word.replaceAll(VERB_G2[i]+"$", "");
                replacedResults.add(replace);
            }
        }
        if (replacedResults.size()>0)
            return findShortestString(replacedResults);
        else
            return "false";
    }
    private String findShortestString(List<String> replaced){
        String first = replaced.get(0);
        for (int i = 1 ; i < replaced.size() ; i++) {
            if (replaced.get(i).length() < first.length()) {
                first = replaced.get(i);
            } // if
        } // for
        return first;
    }
    private String removeReflexiveIfExists(String word){
        List<String> replacedResults = new ArrayList<String>();
        
        for (int i =0;i<REFLEXIVE.length;i++){
            if (Pattern.compile(".*"+REFLEXIVE[i]+"$").matcher(getRv(word)).matches()){
                String replace = word.replaceAll(REFLEXIVE[i]+"$", "");
                replacedResults.add(replace);
            }
        }
        if (replacedResults.size()>0)
            return findShortestString(replacedResults);
        else
            return "false";
    }
    private String removeAdjectivalIfExists(String word){
        List<String> replacedResults = new ArrayList<String>();
        
        for (int i=0;i<ADJECTIVAL.length;i++){
            if (Pattern.compile(".*"+ADJECTIVAL[i]+"$").matcher(getRv(word)).matches()){
                String replace = word.replaceAll(ADJECTIVAL[i]+"$", "");
                replacedResults.add(replace);
            }
        }
        if (replacedResults.size()>0)
            return findShortestString(replacedResults);
        else
            return "false";
    }
    private String removeNounIfExists(String word){
        List<String> replacedResults = new ArrayList<String>();
        
        for (int i=0;i<NOUN.length;i++){
            if (Pattern.compile(".*"+NOUN[i]+"$").matcher(getRv(word)).matches()){
                String replace = word.replaceAll(NOUN[i]+"$", "");
                replacedResults.add(replace);
            }
        }
        if (replacedResults.size()>0)
            return findShortestString(replacedResults);
        else
            return "false";
    }
}
