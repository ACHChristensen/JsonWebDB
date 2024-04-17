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

package jsondb.objects;

import jsondb.Config;
import jsondb.Response;
import org.json.JSONObject;
import jsondb.messages.Messages;
import database.definitions.JdbcInterface;


public class Session implements DatabaseRequest
{
   private final JSONObject definition;

   private static final String SESSION = "session";
   private static final String USERNAME = "username";
   private static final String PASSWORD = "password";
   private static final String PASSTOKEN = "password-token";
   private static final String DATASECTION = "connection-data";


   public Session(JSONObject definition) throws Exception
   {
      this.definition = definition;
   }


   public Response connect() throws Exception
   {
      String token = null;
      String password = null;
      JSONObject response = new JSONObject();
      String username = Config.pool().defaultuser();

      JSONObject data = definition.getJSONObject(DATASECTION);

      if (data.has(USERNAME))
         username = data.getString(USERNAME);

      if (data.has(PASSWORD))
         password = data.getString(PASSWORD);

      if (data.has(PASSTOKEN))
         token = data.getString(PASSTOKEN);

      data.remove(PASSWORD);
      data.remove(PASSTOKEN);

      try
      {
         boolean authenticated = false;
         JdbcInterface db = JdbcInterface.getInstance(false);

         if (token != null && token.equals(db.passtoken())) authenticated = true;
         else if (password != null && db.authenticate(username,password)) authenticated = true;

         if (authenticated)
         {
            String guid = jsondb.Session.create(username).getGuid();

            response.put("success",true);
            response.put("guid",guid);

            return(new Response(response));
         }
         else
         {
            response.put("success",false);
            response.put("message",Messages.get("AUTHENTICATION_FAILED"));
            return(new Response(response));

         }
      }
      catch (Exception e)
      {
         return(new Response().exception(e));
      }
   }


   public Response disconnect() throws Exception
   {
      JSONObject response = new JSONObject();
      String session = definition.optString(SESSION);
      response.put("success",true);
      response.put("session",session);
      return(new Response(response));
   }
}
