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

package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpHandler;

import jsondb.JsonDB;

import com.sun.net.httpserver.HttpExchange;


public class Handler implements HttpHandler
{
   @Override
   public void handle(HttpExchange exchange) throws IOException
   {
      String meth = exchange.getRequestMethod();

      if (meth.equals("GET"))
      {
         doGet(exchange);
         return;
      }

      /*
      InputStream in = exchange.getRequestBody();
      OutputStream out = exchange.getResponseBody();

      exchange.sendResponseHeaders(200,5);
      out.write("Hello".getBytes());
      out.flush();
      out.close();
      */
   }

   public void doGet(HttpExchange exchange) throws IOException
   {
      JsonDB jsondb = new JsonDB();
      OutputStream out = exchange.getResponseBody();
   }
}
