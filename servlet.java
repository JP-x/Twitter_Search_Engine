/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author JP
 */
@WebServlet("/results")
public class servlet extends HttpServlet {

    public static final String INDEX_DIR = "C:\\Users\\JP\\Documents\\NetBeansProjects\\172SeachEnging\\testIndex\\";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String query = request.getParameter("queryString");

        TopDocs results = search(query, 10);
        int numDocs;
        ScoreDoc[] hits;
        if (results != null) {
            if (results.scoreDocs != null) {
                hits = results.scoreDocs;
                numDocs = results.totalHits;
            } else {
                numDocs = 0;
                hits = null;
            }
        } else {
            numDocs = 0;
            hits = null;
        }

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            /*    out.println("<!DOCTYPE html><html>");
             out.println("<html>");
             out.println("<head>");
             out.println("<title>Twitter34</title>");  
           
             out.println("</head>");
             out.println("<body>");
             out.println("<h1>Servlet myservlet has documents :  " + numDocs +  query + "</h1>");
             out.println("</body>");
             out.println("</html>");
             */
            /*out.println("<%@page contentType=\"text/html\" pageEncoding=\"UTF-8\"%>");
             out.println("<%@page import=\"java.io.*,java.util.*, java.util.regex.*, com.mycompany.*\" %>");
             out.println("<%@page session=\"true\"%>");
             */
            out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");

            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<title> Twitter34 </title>");
            out.println("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"${pageContext.request.contextPath}/favicon.ico\" />");
            out.println("<style type=\"text/css\">");
            out.println(".cursorHand{  cursor: pointer;   cursor: hand; }	");
            out.println("#container {    margin:0 auto;    width: 50%;	float: center;	text-align: center;	postion: relative;}");
            out.println("#contents {    width: 100%;	text-align: left;	float: center;}	");
            out.println("#masthead {width: 100%;height: 60px;text-align: center;background-color: #0099cc;float: center;margin:0 auto;}");
            out.println("#results_area {margin-top: 30px; font-family: \"Arial Black\", Gadget, sans-serif; }");
            out.println("#form {	float: center;margin-top: 30px;}");

            /* unvisited link */
            out.println("a:link {color: #55ACEE;}");

            /* visited link */
            out.println("a:visited {color: #00FF00;}");

            /* mouse over link */
            out.println("a:hover {color: #FF00FF;}");

            /* selected link */
            out.println("a:active {color: #0000FF;}");
          out.println(".tg  {border-collapse:collapse;border-spacing:0;}");
out.println(".tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}");
out.println(".tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}");
out.println(".tg .tg-7h0m{background-color:#9b9b9b}");
            out.println("</style>");

            out.println("<script type =\"text/javascript\">");
            out.println("function empty() {");
            out.println("  var x;");
            out.println("  x = document.getElementById(\"queryString\").value;");
            out.println("  if (x == \"\") {");
            out.println("      alert(\"Enter a non empty string.\");");
            out.println("      return false;");
            out.println("   };");
            out.println("}");
            out.println("</script>");

            out.println("</head>");
            out.println("<body topmargin=\"0\" leftmargin=\"10\" rightmargin=\"10\" bottommargin=\"0\" style=\"color:white\">");
            out.println("        <body background=\"twitterBG.png\">  ");
            /*out.println("<% ");
             out.println("String queryString = \"\";");
             out.println("Integer answerSize = 10;");
             out.println("%>");
             out.println("<%");
             out.println("	if (request.getParameter(\"search_submit\") != null) {");
             out.println("		queryString = request.getParameter(\"queryString\");	");		
             out.println("	}");
       
             out.println("%>");
             */
            out.println("<div id=\"page\">");

            out.println("	<text color =\"white\">");
            out.println("               <center><h1><a href=\"index.jsp\"><img src=\"twitter34lettering.png\" height=\"78\" width=\"400\"></a></h1></center>");

            out.println("<div id=\"container\">");
            out.println("	<div id=\"form\">");
            out.println("		<form action=\"results\" method=\"POST\">");
            out.println("			<br />");
            out.println("			<input type=\"text\" name=\"queryString\" value=\"\" size=\"40\" /> ");
            out.println("			<input type=\"submit\" value=\"Search\" onClick=\"return empty()\" />");
            out.println("		</form>");
            out.println("	</div>");
            out.println("	<div id=\"contents\">	");
            out.println("<h1><center>" + numDocs + " results found for: " + query + "</h1></center>");
            out.println("		<div id=\"results_area\">");
            out.println("<center>");
            IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(INDEX_DIR))));
//        TopDocs results = search("happy", 10);
//        ScoreDoc[] hits=results.scoreDocs;
//        int numDocs = results.totalHits;
//        System.out.println(numDocs + " total matching documents");
//        
//        for (int i=0; i < hits.length; i++) {
//            //System.out.println("doc=" + hits[i].doc + " score="+ hits[i].score);
//            Document doc= indexSearcher.doc(hits[i].doc);
//            String screenName =doc.get("screenName");
//            String text =doc.get("text");
//            if (screenName != null) {
//              System.out.println((i + 1) + ". " + screenName + " - " + text);
//            }
//            else {
//              System.out.println((i + 1) + ". " + "No tweet found.");
//            }
//        }
            if (numDocs != 0) {
                for (int i = 0; i < 10; i++) {
                    if (hits[i] != null) {
                        Document doc = indexSearcher.doc(hits[i].doc);
                        String text = doc.get("text");
                        String exturl = doc.get("exturl");
                        String tid = doc.get("tid");
                        String username = doc.get("username");
                        String screenname = doc.get("screenName");
                        String createdat = doc.get("createdat");
                        String favoritecount = doc.get("favoriteCount");
                        String retweetcount = doc.get("retweetCount");
                        String pagetitle = doc.get("pagetitle");
                        String latitude = doc.get("latitude");
                        String longitude = doc.get("longitude");
                        String profileimageurl = doc.get("profileImageUrl");
                        out.println("<table class=\"tg\"> <tr> <th class=\"tg-7h0m\">");
                        out.println("<img src=\"" + profileimageurl + "\">");
                        out.println("<p>Username: " + username + "</p>");
                        out.println("<p>Created At: " + createdat + "</p>");
                        out.println("<p>Retweet Count: " + retweetcount + "</p>");
                        out.println("<p>Favorite Count: " + favoritecount + "</p>");
                        out.println("<a href=\"http://twitter.com/statuses/" + tid + "\">Tweet: </a>");
                        out.println(text);
                        
                        out.println("<br>");
                        out.println("<iframe src=\"https://www.google.com/maps/embed/v1/place?key=AIzaSyCb7Q4ASa4r_Md9eSMYPeVmi1fSsaWBLNU&q=" + latitude + "," + longitude + "\"width=\"400\" height=\"200\"></iframe>");
                        out.println("</th>");
                        out.println("</tr>");
                        out.println("</table>");
                        out.println("<br><br>");
                    }

                }

            }
            out.print("</center>");
            out.println("");
            out.println("		</div>");
            out.println("	</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public static TopDocs search(String queryString, int topk) throws CorruptIndexException, IOException {

        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(INDEX_DIR))));
        QueryParser queryparser = new org.apache.lucene.queryparser.classic.QueryParser("text", new StandardAnalyzer());

        try {
            StringTokenizer strtok = new StringTokenizer(queryString, " ~`!$%^&*()_-+={[}]|:;'<>,./?\"\'\\/\n\t\b\f\r");//removed @#
            String querytoparse = "";
            while (strtok.hasMoreElements()) {
                String token = strtok.nextToken();
                //querytoparse += "text:" + token + "^1" + "title:" + token + "^1.5";
                querytoparse += "text:" + token + "^1";
            }
            Query query = queryparser.parse(querytoparse);
            //System.out.println(query.toString());
            TopDocs results = indexSearcher.search(query, topk);

            return results;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //indexSearcher.close();
        }
        return null;
    }

}
