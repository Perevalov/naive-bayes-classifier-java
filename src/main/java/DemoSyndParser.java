/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.IOException;

import java.net.MalformedURLException;

import java.net.URL;



import com.sun.syndication.feed.synd.SyndContent;

import com.sun.syndication.feed.synd.SyndEntry;

import com.sun.syndication.feed.synd.SyndFeed;

import com.sun.syndication.feed.synd.SyndPerson;

import com.sun.syndication.io.FeedException;

import com.sun.syndication.io.SyndFeedInput;

import com.sun.syndication.io.XmlReader;



public class DemoSyndParser

{

    public SyndFeed parseFeed(String url)

            throws IllegalArgumentException, MalformedURLException, FeedException, IOException

    {

        return new SyndFeedInput().build(new XmlReader(new URL(url)));

    }



    public static String getNews(SyndFeed feed)
    {
        String cont = "";
        for (Object object : feed.getEntries())
        {
            SyndEntry entry = (SyndEntry) object;

            SyndContent content = entry.getDescription();

            if (content != null)

        cont += content.getValue() + "Следующая новость";
        }
return cont;

    }



    public static void main(String[] args) throws Exception

    {

        DemoSyndParser parser = new DemoSyndParser();

        parser.getNews(parser.parseFeed("https://news.yandex.ru/index.rss"));

    }

}