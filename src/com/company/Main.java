package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class Main {

    public static void main(String[] args) {

        ReturnArticleName2();
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

            for (int i = 2; i <=10; i++) {

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



            Elements articles = doc.select("div [class=col-xs-12 col-md-8 col-lg-9]");
            //System.out.println(articles.text());

            Elements article;

            article = articles.select("div [class=c-news-card]");

            System.out.println(article.select("div [class=c-news-card__head]").text());


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;

    }

    static ArrayList<String> ReturnArticleUrl()
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

    public static void InputInDataBase()
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
                String query = "INSERT INTO article (name, url) VALUES ('" + ReturnArticleName1().get(i) + "','" + ReturnArticleUrl().get(i) + "')";
                System.out.println(query);
                preparedStatement = con.prepareStatement(query);
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


}
