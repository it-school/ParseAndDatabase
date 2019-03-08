package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import static java.sql.DriverManager.getConnection;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i<3; i++)
        {
            list.add(in.nextLine());
        }
        CheckAlternative(list).toString();
        //InputInDataBase();
    }

    static ArrayList<String> ReturnArticleName1()
    {
        ArrayList<String> list = new ArrayList<>();
        String url = "https://dumskaya.net/";
        try
        {
            Document doc = Jsoup.connect(url).get();



            Elements articles = doc.select("tbody");

            Elements article;

            for (int i = 2; i <=7; i++) {

                article = articles.select("tr [id=newstr" + i + "]");

                String articleName = article.select("td").text();

                list.add(articleName);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;

    }

    static ArrayList<String> ReturnArticleName2()
    {
        ArrayList<String> list = new ArrayList<>();
        String url = "https://www.048.ua/news";
        try
        {
            Document doc = Jsoup.connect(url).get();



            Elements articles = doc.select("div [data-key=1]");
            //System.out.println(articles.text());

            Elements article;

            article = articles.select("div [class=c-news-card__head]");

            //System.out.println(article.select("div [class=c-news-card__head]").text());
                for (Element news : article)
                {
                    //System.out.println((numberElement+1) + "\n"+news.text());
                    list.add(news.text());
                }



        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;

    }

    static ArrayList<String> ReturnArticleUrl1()
    {
        ArrayList<String> list = new ArrayList<>();
        String url = "https://dumskaya.net/";
        try
        {
            Document doc = Jsoup.connect(url).get();



            Elements articles = doc.select("tbody");

            Elements article;

            for (int i = 2; i <=10; i++) {

                article = articles.select("tr [id=newstr" + i + "]");

                list.add("https://dumskaya.net" + article.select("a").attr("href"));

            }



        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;

    }

    static ArrayList<String> ReturnArticleUrl2()
    {
        ArrayList<String> list = new ArrayList<>();
        String url = "https://www.048.ua/news";
        try
        {
            Document doc = Jsoup.connect(url).get();



            Elements articles = doc.select("div [data-key=0]");

            Elements article;

            article = articles.select("div [class=c-news-card__head]");

            int numberElement = 0;
            for (Element news : article)
            {
                list.add("https://www.048.ua" + article.select("a").attr("href"));
                numberElement++;
            }



        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    static ArrayList<String> ReturnArticleName3()
    {
        ArrayList<String> list = new ArrayList<>();
        String url = "https://od.vgorode.ua/news/";
        try
        {
            Document doc = Jsoup.connect(url).get();



            Elements articles = doc.select("div [class=row]");
            //System.out.println(articles.text());

            Elements article = articles.select("div [class=col-xs-12 col-sm-6 col-lg-4]").select("div [class=title]");
            //System.out.println(article.select("a").text());

            for(Element news : article)
            {
                //System.out.println(news.text());
                list.add(news.text());
            }



        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;

    }

    static ArrayList<String> ReturnArticleUrl3()
    {
        ArrayList<String> list = new ArrayList<>();
        String url = "https://od.vgorode.ua/news/";
        try
        {
            Document doc = Jsoup.connect(url).get();



            Elements articles = doc.select("div [class=row]");
            //System.out.println(articles.text());

            Elements article = articles.select("div [class=col-xs-12 col-sm-6 col-lg-4]").select("div [class=title]");
            //System.out.println(article.select("a").text());

            for(Element news : article)
            {
                list.add(news.select("a").attr("href"));
            }



        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    static void InputInDataBase()
    {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try
        {
            // JDBC URL для базы данных на Localhost
            String url = "jdbc:mysql://localhost/articles?serverTimezone=UTC&useSSL=false;";

            // здесь осуществляется соединение c login и password
            Properties properties = new Properties();
            properties.setProperty("user", "root");
            properties.setProperty("password", "");
            properties.setProperty("useSSL", "false");
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("characterEncoding","utf8");

            con = getConnection(url, properties);
//            System.out.println("Connection ID" + con.toString());


            //  Формирование запросов к БД
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("use articles;");

            PreparedStatement preparedStatement = null;



            for (int i = 0; i < ReturnArticleName1().size(); i++) {
                String query1 = "INSERT INTO article (name, url) VALUES ('" + ReturnArticleName1().get(i) + "','" + ReturnArticleUrl1().get(i) + "')";
                String query2 = "INSERT INTO article (name, url) VALUES ('" + ReturnArticleName2().get(i) + "','" + ReturnArticleUrl2().get(i) + "')";
                String query3 = "INSERT INTO article (name, url) VALUES ('" + ReturnArticleName3().get(i) + "','" + ReturnArticleUrl3().get(i) + "')";
                preparedStatement = con.prepareStatement(query1);
                preparedStatement.executeUpdate();
                preparedStatement = con.prepareStatement(query2);
                preparedStatement.executeUpdate();
                preparedStatement = con.prepareStatement(query3);
                preparedStatement.executeUpdate();

            }



            rs.close();
            preparedStatement.close();
//  Закончили запрос
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
        finally
        {
            if ( con != null )
            {
                try
                {
                    con.close();
                }
                catch( Exception e ) { }
            }
        }
    }

    static ArrayList<String> CheckAlternative(ArrayList<String> articleNamesList)
    {
        

        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i < articleNamesList.size() - 1; i++)
        {
            String word1[];
            String word2[];
            word1 = articleNamesList.get(i).split(" ");
            word2 = articleNamesList.get(i+1).split(" ");
            if(word1.length<word2.length)
            {
                for(int j = 0; j<word1.length; j++)
                {
                    for(int k = 0; k<word1.length; k++)
                    {
                        if(word1[j] == word2[k])
                        {
                            list.add(articleNamesList.get(i));
                        }
                    }
                }
            }
        }

        return list;

    }

}
