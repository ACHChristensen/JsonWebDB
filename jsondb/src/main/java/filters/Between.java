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

package filters;

import sources.Source;
import database.DataType;
import database.BindValue;
import java.util.ArrayList;
import org.json.JSONObject;
import sources.TableSource;
import filters.definitions.Filter;


public class Between extends Filter
{
   public Between(Source source, JSONObject definition)
   {
      super(source,definition);
   }

   @Override
   public String sql()
   {
      return(column+" between ? and ?");
   }

   @Override
   public ArrayList<BindValue> bindvalues()
   {
      if (bindvalues.size() == 0)
      {
         BindValue bv1 = new BindValue(column);
         BindValue bv2 = new BindValue(column);

         bindvalues.add(bv1.value(values[0]));
         bindvalues.add(bv2.value(values[1]));

         if (source instanceof TableSource)
         {
            TableSource ts = (TableSource) source;
            DataType coldef = ts.getColumn(column);

            if (coldef != null)
            {
               bv1.type(coldef.sqlid);
               bv2.type(coldef.sqlid);
            }
         }
      }

      return(bindvalues);
   }
}