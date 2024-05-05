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

package utils;

import java.nio.ByteBuffer;


public class Bytes
{
   public static byte[] getBytes(int x)
   {
      ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
      buffer.putInt(x);
      return(buffer.array());
   }

   public static byte[] getBytes(long x)
   {
      ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
      buffer.putLong(x);
      return(buffer.array());
   }

   public static int getInt(byte[] bytes, int pos)
   {
      ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
      buffer.put(bytes,pos,4);
      buffer.flip();
      return(buffer.getInt());
   }

   public static long getLong(byte[] bytes, int pos)
   {
      ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
      buffer.put(bytes,pos,8);
      buffer.flip();
      return(buffer.getLong());
   }
}