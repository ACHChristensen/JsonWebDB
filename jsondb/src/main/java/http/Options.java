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

import jsondb.Config;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


public class Options
{
   private static final String PATH = "path";
   private static final String PAGE = "page";
   private static final String ADMN = "admin";
   private static final String INDX = "index";
   private static final String VIRT = "virtual";
   private static final String USER = "username";
   private static final String PASW = "password";
   private static final String AUTH = "protected";
   private static final String APPL = "application";

   private static String usr = null;
   private static String pwd = null;
   private static String admin = null;
   private static String index = null;

   private static ArrayList<VirtualPath> virtual =
      new ArrayList<VirtualPath>();


   public static void initialize()
   {
      JSONObject http = Config.get(APPL);
      JSONObject auth = Config.get(http,AUTH);

      Options.usr = Config.get(auth,USER);
      Options.pwd = Config.get(auth,PASW);

      Options.admin = Config.get(auth,ADMN);
      Options.index = Config.get(http,INDX);

      if (!Options.index.startsWith("/"))
         Options.index = "/" + Options.index;

      if (!Options.admin.startsWith("/"))
         Options.admin = "/" + Options.admin;

      JSONArray arr = Config.get(http,VIRT);

      for (int i = 0; i < arr.length(); i++)
      {
         JSONObject def = arr.getJSONObject(i);
         virtual.add(new VirtualPath(def.getString(PATH),def.getString(PAGE)));
      }
   }


   public static String admin()
   {
      return(admin);
   }


   public static String index()
   {
      return(index);
   }


   public static String getVirtual(String path)
   {
      if (!path.startsWith("/"))
         path = "/"+path;

      for (int i = 0; i < virtual.size(); i++)
      {
         VirtualPath vp = virtual.get(i);
         if (path.startsWith(vp.path)) return(vp.page);
      }

      return(null);
   }


   public static boolean authenticated(String header)
   {
      if (header == null) return(false);

      if (header.toLowerCase().startsWith("basic "))
         header = header.substring(6);

      String auth = new String(Base64.getDecoder().decode(header));
      int pos = auth.indexOf(':');

      String usr = auth.substring(0,pos);
      String pwd = auth.substring(pos+1);

      if (usr.equals(Options.usr) && pwd.equals(Options.pwd))
         return(true);

      return(false);
   }


   private static class VirtualPath
   {
      public final String path;
      public final String page;

      private VirtualPath(String path, String page)
      {
         if (!page.startsWith("/")) page = "/" + page;

         path = path.toLowerCase();
         if (!path.endsWith("/")) path += "/";
         if (!path.startsWith("/")) path = "/" + path;

         this.path = path;
         this.page = page;
      }

      public String toString()
      {
         return(path+" -> "+page);
      }
   }
}