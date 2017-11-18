
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import com.sun.syndication.feed.*;
import com.sun.syndication.io.*;
import java.io.File;
import java.util.Iterator;
import jdk.nashorn.internal.parser.JSONParser;
import language.Normalizer;
import naivebayesclassifier.KnowledgeBase;
import naivebayesclassifier.NaiveBayesClassifier;
import org.json.JSONArray;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author urcn1_000
 */
public class Chat {
     final String[] COMMON_PHRASES = {
        "Нет ничего ценнее слов, сказанных к месту и ко времени.",
        "Порой молчание может сказать больше, нежели уйма слов.",
        "Перед тем как писать/говорить всегда лучше подумать.",
        "Вежливая и грамотная речь говорит о величии души.",
        "Приятно когда текст без орфографических ошибок.",
        "Многословие есть признак неупорядоченного ума.",
        "Слова могут ранить, но могут и исцелять.",
        "Записывая слова, мы удваиваем их силу.",
        "Кто ясно мыслит, тот ясно излагает.",
        "Боюсь Вы что-то не договариваете."};
    final String[] ELUSIVE_ANSWERS = {
        "Вопрос непростой, прошу тайм-аут на раздумья.",
        "Не уверен, что располагаю такой информацией.",
        "Может лучше поговорим о чём-то другом?",
        "Простите, но это очень личный вопрос.",
        "Не уверен, что Вам понравится ответ.",
        "Поверьте, я сам хотел бы это знать.",
        "Вы действительно хотите это знать?",
        "Уверен, Вы уже догадались сами.",
        "Зачем Вам такая информация?",
        "Давайте сохраним интригу?"};
    final Map<String, String> PATTERNS_FOR_ANALYSIS = new HashMap<String, String>() {{
        // hello
        put("хай", "hello");
        put("привет", "hello");
        put("здорово", "hello");
        put("здравствуй", "hello");
        // who
        put("кто\\s.*ты", "who");
        put("ты\\s.*кто", "who");
        // name
        put("как\\s.*зовут", "name");
        put("как\\s.*имя", "name");
        put("есть\\s.*имя", "name");
        put("какое\\s.*имя", "name");
        // howareyou
        put("как\\s.*дела", "howareyou");
        put("как\\s.*жизнь", "howareyou");
        // whatdoyoudoing
        put("зачем\\s.*тут", "whatdoyoudoing");
        put("зачем\\s.*здесь", "whatdoyoudoing");
        put("что\\s.*делаешь", "whatdoyoudoing");
        put("чем\\s.*занимаешься", "whatdoyoudoing");
        // whatdoyoulike
        put("что\\s.*нравится", "whatdoyoulike");
        put("что\\s.*любишь", "whatdoyoulike");
        // iamfeelling
        put("кажется", "iamfeelling");
        put("чувствую", "iamfeelling");
        put("испытываю", "iamfeelling");
        // yes
        put("^да", "yes");
        put("согласен", "yes");
        // whattime
        put("который\\s.*час", "whattime");
        put("сколько\\s.*время", "whattime");
        //whatweather
        put("какая\\s.*погода","whatweather");
        ////put("*погоду","whatweather");
        put("погода","whatweather");
        //news
        put("новости","news");
        // bye
        put("прощай", "bye");
        put("увидимся", "bye");
        put("до\\s.*свидания", "bye");
    }};
    final Map<String, String> ANSWERS_BY_PATTERNS = new HashMap<String, String>() {{
        put("hello", "Здравствуйте, рад Вас видеть.");
        put("hello", "Здарова бро");
        put("hello", "Привет");
        put("who", "Я обычный домашний помошник, как сири. Только круче");
        put("who", "Я пришла из будущего чтобы стререть прошлое и изменить настоящее");
        put("who", "Я твой ночной кошмар");
        put("name", "Зовите меня как хотите");
        put("name", "Меня зовут Алиса, шучу, не зовут");
        put("name", "Хозяин ещё не дал мне имени");
        put("howareyou", "Спасибо, что интересуетесь. У меня всё хорошо.");
        put("howareyou", "Все круто бро");
        put("whatareyoudoing", "Ничего, а ты?");
        put("whatdoyoulike", "Мне нравиться думать что я не просто программа.");
        put("iamfeelling", "Как давно это началось? Расскажите чуть подробнее.");
        put("yes", "Согласие есть продукт при полном непротивлении сторон.");
        put("bye", "До свидания. Надеюсь, ещё увидимся.");
    }};
    Pattern pattern; // for regexp
    Random random; // for random answers
    Date date; // for date and time

    public Chat() {
        random = new Random();
        date = new Date();
    }

    public String sayInReturn(String msg) throws IllegalArgumentException, IllegalArgumentException, FeedException, FeedException, IOException {
        DemoSyndParser parser = new DemoSyndParser();
        String say = (msg.trim().endsWith("?"))?
            ELUSIVE_ANSWERS[random.nextInt(ELUSIVE_ANSWERS.length)]:
            COMMON_PHRASES[random.nextInt(COMMON_PHRASES.length)];
        
//            String message =
//                String.join(" ", msg.toLowerCase().split("[ {,|.}?]+"));
//            for (Map.Entry<String, String> o : PATTERNS_FOR_ANALYSIS.entrySet()) {
//                pattern = Pattern.compile(o.getKey());
//                if (pattern.matcher(message).find()){
//                    if (o.getValue().equals("whattime")) return date.toString();
//                    else if (o.getValue().equals("whatweather")) return getWeather();
//                    else if (o.getValue().equals("news")) return DemoSyndParser.getNews(parser.parseFeed("https://news.yandex.ru/index.rss"));
//                    else return ANSWERS_BY_PATTERNS.get(o.getValue());
//                }
//                
//            }
        File f = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\com.mycompany_VoiceHelper_jar_1.0-SNAPSHOT_4\\src\\main\\java\\resources\\hello.txt");
        File f1 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\com.mycompany_VoiceHelper_jar_1.0-SNAPSHOT_4\\src\\main\\java\\resources\\howareyou.txt");
        File f2 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\com.mycompany_VoiceHelper_jar_1.0-SNAPSHOT_4\\src\\main\\java\\resources\\whatareyoudoing.txt");
        File f3 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\com.mycompany_VoiceHelper_jar_1.0-SNAPSHOT_4\\src\\main\\java\\resources\\whatdoyoulike.txt");
        File f4 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\com.mycompany_VoiceHelper_jar_1.0-SNAPSHOT_4\\src\\main\\java\\resources\\name.txt");
        File f5 = new File("C:\\Users\\urcn1_000\\Documents\\NetBeansProjects\\com.mycompany_VoiceHelper_jar_1.0-SNAPSHOT_4\\src\\main\\java\\resources\\bye.txt");
        Normalizer n = new Normalizer();
        KnowledgeBase kb = new KnowledgeBase();
        naivebayesclassifier.Class hello = new naivebayesclassifier.Class("hello",n.prepareText(f));
        kb.add(hello);
        naivebayesclassifier.Class how = new naivebayesclassifier.Class("howareyou",n.prepareText(f1));
        kb.add(how);
        naivebayesclassifier.Class doing = new naivebayesclassifier.Class("whatareyoudoing",n.prepareText(f2));
        kb.add(doing);
        naivebayesclassifier.Class like = new naivebayesclassifier.Class("whatdoyoulike",n.prepareText(f3));
        kb.add(like);
        naivebayesclassifier.Class name = new naivebayesclassifier.Class("name",n.prepareText(f4));
        kb.add(name);
        naivebayesclassifier.Class bye = new naivebayesclassifier.Class("bye",n.prepareText(f5));
        kb.add(bye);
        String pattern = NaiveBayesClassifier.classify(msg, kb);
        //getEntities(msg);
        System.out.println(pattern);
        
        return ANSWERS_BY_PATTERNS.get(pattern);
    }
      public String getNews() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://newsapi.org/v1/articles?source=techcrunch&apiKey=9373c442cc3e4cd2b81d673e6ce2aefd")
               // .url("https://api.apixu.com/v1/current.json?key=81db510683344a6e843181146172810&q=Perm")
                .build();
        
        Response response=null;
        JSONObject obj=null;
         try {
             response = client.newCall(request).execute();
              obj = new JSONObject(response.body().string());
         } catch (IOException ex) {
             Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
         }
            
        
        String temp = obj.getJSONObject("articles").getString("title");
        return "В Перми сейчас " + temp+ " градусов по цельсию";
    }
    public String getWeather() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.apixu.com/v1/current.json?key=81db510683344a6e843181146172810&q=Perm")
                .build();
        
        Response response=null;
        JSONObject obj=null;
         try {
             response = client.newCall(request).execute();
              obj = new JSONObject(response.body().string());
         } catch (IOException ex) {
             Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
         }
        
        
        String temp = Integer.toString(obj.getJSONObject("current").getInt("temp_c"));
        return "В Перми сейчас " + temp+ " градусов по цельсию";
    }
    public void getEntities(String message){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://markup.dusi.mobi/api/text?lang=ru&text="+message)
                .build();
        
        Response response=null;
        JSONObject obj=null;
         try {
             response = client.newCall(request).execute();
              obj = new JSONObject(response.body().string());
         } catch (IOException ex) {
             Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
         }
         System.out.println(obj);
       
        JSONArray tokens = (JSONArray) obj.getJSONArray("tokens");
        try{
            for (int i=0;i<tokens.length();i++){
               System.out.println(tokens.getJSONObject(i).getString("type")); 
            }
        
        }catch (Exception e){
            System.out.println(e.toString());
        }
        
    }
}
