import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class compiler extends HttpServlet 
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
        {
            String clientId = "86e3b4da0ce141c7c043d1dbb1e3b059";
            String clientSecret = "74a51f4f738cd2d737d5d992b8d29233677617a6b732e10d4f8d781fbd72893";
            String script = request.getParameter("data");
            String language = request.getParameter("lang");
            String versionIndex = request.getParameter("v");
            
            try {
                URL url = new URL("https://api.jdoodle.com/v1/execute");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                
                String input = "{\"clientId\": \"" + clientId + "\",\"clientSecret\":\"" + clientSecret + "\",\"script\":\"" + script +
                "\",\"language\":\"" + language + "\",\"versionIndex\":\"" + versionIndex + "\"} ";
                
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(input.getBytes());
                outputStream.flush();
                
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("Please check your inputs : HTTP error code : "+ connection.getResponseCode());
                }
                
                BufferedReader bufferedReader;
                bufferedReader = new BufferedReader(new InputStreamReader(
                (connection.getInputStream())));
                
                String output;
                while ((output = bufferedReader.readLine()) != null) {
                    out.println(output);
                }
                connection.disconnect();                
            } 
            catch (IOException | RuntimeException e) 
            {
                out.println(e.getMessage());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
