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

package jsondb;

import state.State;
import http.Cluster;
import java.io.File;
import http.HTTPConfig;
import sources.Sources;
import files.FileConfig;
import logger.Applogger;
import database.SQLTypes;
import messages.Messages;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.net.InetAddress;
import state.StatePersistency;
import java.io.FileInputStream;
import java.util.logging.Logger;
import database.definitions.AdvancedPool;


public class Config
{
   private static final String CONF = "config";
   private static final String POOL = "enabled";
   private static final String SESS = "session";
   private static final String PATH = "location";
   private static final String DBSC = "database";
   private static final String FILE = "config.json";
   private static final String APPL = "application";

   private static final String CLUSTER = "cluster";
   private static final String INSTTYPES = "types";
   private static final String ENDPOINT = "endpoint";
   private static final String INSTANCES = "instances";

   private static final String SESTMOUT = "session-timeout";
   private static final String CONTMOUT = "connection-timeout";
   private static final String TRXTMOUT = "transaction-timeout";

   private static int sestmout = 0;
   private static int trxtmout = 0;
   private static int contmout = 0;

   private static String inst = null;
   private static String appl = null;
   private static String endp = null;

   private static Logger logger = null;
   private static JSONObject config = null;
   private static JSONObject instance = null;

   private static String root = null;
   private static AdvancedPool pool = null;


   /**
    *
    * @param root the root directory of JsonWebDB
    * @param inst the instance name.
    * @return The parsed config with some helper methods to navigate the config.
    * @throws Exception
    */
    protected static synchronized void load(String root, String inst) throws Exception
    {
      load(root,inst,FILE,true);
    }


   /**
    *
    * @param root the root directory of JsonWebDB
    * @param inst the instance name
    * @param file the configuration file name
    * @param logall Also handle messages written to the root logger
    * @return The parsed config with some helper methods to navigate the config
    * @throws Exception
    */
   protected static synchronized void load(String root, String inst, String file, boolean logall) throws Exception
   {
      if (file == null)
         file = FILE;

      if (Config.root != null)
         return;

      if (file.indexOf('.') < 0)
         file += ".json";

      Config.root = root;
      inst = inst.toLowerCase();
      String path = path(CONF,file);

      FileInputStream in = new FileInputStream(path);

      JSONTokener tokener = new JSONTokener(in);
      JSONObject config = new JSONObject(tokener);

      in.close();

      Config.inst = inst;
      Config.config = config;
      Config.appl = get(get(APPL),PATH);
      Config.instance = getInstanceConfig();

      Config.sestmout = get(get(SESS),SESTMOUT);
      Config.trxtmout = get(get(SESS),TRXTMOUT);
      Config.contmout = get(get(SESS),CONTMOUT);

      Config.logger = Applogger.setup(logall);

      JSONObject dbsc = config.getJSONObject(DBSC);

      if (dbsc.getBoolean(POOL))
         Config.pool = new database.AdvancedPool(dbsc);
   }

   public static void initialize() throws Exception
   {
      SQLTypes.initialize();

      State.initialize();
      Admins.initialize();
      Cluster.initialize();
      Sources.initialize();
      FileConfig.initialize();
      HTTPConfig.initialize();
      StatePersistency.initialize();

      if (Cluster.getServer(inst) == null)
         throw new Exception(Messages.get("INSTANCE_NOT_REGISTERED",inst));

      Monitor.monitor();
   }

   /** Installation root */
   public static String root()
   {
      return(root);
   }

   /** Installation root */
   public static void root(String root)
   {
      Config.root = root;
   }

   /** The instance name */
   public static String inst()
   {
      return(inst);
   }

   /** Path to application */
   public static String appl()
   {
      return(appl);
   }

   /** Connection idle timeout */
   public static int conTimeout()
   {
      return(contmout);
   }

   /** Session timeout */
   public static int sesTimeout()
   {
      return(sestmout);
   }

   /** Transaction timeout */
   public static int trxTimeout()
   {
      return(trxtmout);
   }

   /** The logger */
   public static Logger logger()
   {
      return(logger);
   }

   /** The config json */
   public static JSONObject get()
   {
      return(config);
   }

   /** Get the pool */
   public static AdvancedPool pool()
   {
      return(pool);
   }

   /** Inject the pool */
   protected static void pool(AdvancedPool pool)
   {
      Config.pool = pool;
   }

   /** Get the mimetype from file(name) */
   public static String getMimeType(String file)
   {
      return(FileConfig.getMimeType(file));
   }

   /** Get an object from the config json */
   @SuppressWarnings({ "unchecked" })
   public static <T> T get(String section)
   {
      return((T) config.get(section));
   }

   /** Get an object from a json */
   @SuppressWarnings({ "unchecked" })
   public static <T> T get(JSONObject section, String attr)
   {
      return((T) section.get(attr));
   }

   /** Get path to a given file or folder */
   public static String path(String... parts)
   {
      String path = root;

      if (parts != null)
      {
         for (int i = 0; i < parts.length; i++)
            path += File.separator + parts[i];
      }

      return(path);
   }


   private static JSONObject getInstanceConfig() throws Exception
   {
      String type = null;

      JSONObject instance = null;
      JSONObject insttype = null;
      JSONObject cluster = get(CLUSTER);

      JSONArray types = get(cluster,INSTTYPES);
      JSONArray instances = get(cluster,INSTANCES);

      for (int i = 0; i < instances.length(); i++)
      {
         JSONObject entry = instances.getJSONObject(i);

         if (entry.getString("name").equals(inst))
         {
            instance = entry;
            type = instance.getString("type");
            break;
         }
      }

      if (instance == null)
         throw new Exception("No configuration found for instance "+inst);

      for (int i = 0; i < types.length(); i++)
      {
         JSONObject entry = types.getJSONObject(i);

         if (entry.getString("type").equals(type))
         {
            insttype = entry;
            break;
         }
      }

      if (insttype == null)
         throw new Exception("No configuration found for instance type "+type);

      for(String name : JSONObject.getNames(insttype))
      {
         if (!instance.has(name))
            instance.put(name,insttype.get(name));
      }

      if (instance.has(PATH))
         Config.appl = instance.getString(PATH);

      String endpoint = instance.getString(ENDPOINT);

      if (endpoint.indexOf("{ssl}") >= 0)
      {
         int ssl = instance.getInt("ssl");
         endpoint = endpoint.replace("{ssl}",ssl+"");
      }

      if (endpoint.indexOf("{port}") >= 0)
      {
         int port = instance.getInt("port");
         endpoint = endpoint.replace("{port}",port+"");
      }

      if (endpoint.indexOf("{host}") >= 0)
      {
         try
         {
            InetAddress id = InetAddress.getLocalHost();
            endpoint = endpoint.replace("{host}",id.getHostName());
         }
         catch (Exception e) {}
      }

      Config.endp = endpoint;
      return(instance);
   }
}