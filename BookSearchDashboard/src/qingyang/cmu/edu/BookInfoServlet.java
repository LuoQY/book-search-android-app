package qingyang.cmu.edu;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;


public class BookInfoServlet extends javax.servlet.http.HttpServlet {

    private BookInfoModel model = new BookInfoModel();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("Console: Android visited");
        // get time stamp
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        // get device information
        String userAgent = request.getHeader("User-Agent");
        String deviceInfo = userAgent.substring(userAgent.indexOf("(")+1,userAgent.indexOf(")"));

        // get search term from request and check if it is legal
        String searchTerm = request.getParameter("search");
        if(searchTerm.equals("")) {
            response.setStatus(400);
            return;
        }

        BookBean bookInfo;
        long startTime = System.currentTimeMillis();
        bookInfo = model.searchBook(searchTerm);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        LogBean log = new LogBean(deviceInfo, searchTerm, currentTime, bookInfo, responseTime);
        model.pushToMongo(log);
        if(bookInfo ==  null) {
            response.setStatus(400);
            return;
        }
        response.setStatus(200);
        JSONObject result = new JSONObject();
        result.put("title", bookInfo.getTitle());
        result.put("authors", bookInfo.getAuthors());
        result.put("publisher", bookInfo.getPublisher());
        result.put("imageLink", bookInfo.getImageLink());
        PrintWriter out = response.getWriter();
        System.out.println("response: "+result.toString());
        out.println(result.toString());
        out.close();
    }
}
