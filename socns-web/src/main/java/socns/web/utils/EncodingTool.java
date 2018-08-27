package socns.web.utils;

public class EncodingTool
{
  public static String encodeStr(String str)
  {
    try
    {
      return new String(str.getBytes("ISO-8859-1"), "UTF-8");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
}