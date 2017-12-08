package main.model;

public class Helper {

	 static public String getUrl(String referer){
		 String url;
		 int index = referer.indexOf("?");
		 if (index > 0) {
			 url = referer.substring(0, index);
		 } else {
			 url = referer;
		 }
		 return url;
	 }

}
