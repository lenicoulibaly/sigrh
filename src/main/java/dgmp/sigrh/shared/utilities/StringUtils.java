package dgmp.sigrh.shared.utilities;

import java.text.Normalizer;

public class StringUtils
{
	public static String stripAccents(String string)
	{
		if(string==null) return null;
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		string = string.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return string;
	}
	
	public static String stripAccentsToUpperCase(String string)
	{
		if(string==null) return null;
		return stripAccents(string).toUpperCase();
	}
	
	public static String stripAccentsToLowerCase(String string)
	{
		if(string==null) return null;
		return stripAccents(string).toLowerCase();
	}

	public static String blankToNull(String str)
	{
		return str == null ? null : str.trim().equals("") ? null : str;
	}
}