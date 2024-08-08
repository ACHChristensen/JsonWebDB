/*
MIT License

Copyright (c) 2024 Alex Høffner

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package application;

import jsondb.Config;
import jsondb.JsonDB;
import org.json.JSONObject;
import java.util.logging.Logger;
import com.sun.net.httpserver.HttpExchange;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Application
{
   JsonDB jsondb = new JsonDB();
   Logger logger = Config.logger();


   public void init()
   {
   }


   public boolean intercept(HttpExchange exchange) throws Exception
   {
      return(false);
   }


   public boolean intercept(HttpServletRequest request, HttpServletResponse response) throws Exception
   {
      return(false);
   }


   public boolean accept(JSONObject request) throws Exception
   {
      return(true);
   }


   public JSONObject rewrite(JSONObject request) throws Exception
   {
      return(request);
   }
}
