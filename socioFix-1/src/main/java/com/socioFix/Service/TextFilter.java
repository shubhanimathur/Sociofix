package com.socioFix.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFilter {
  
	 private static final String[] ignoreWords = {"sexual","sexuall","rape","raped","molester"};
	 private ArrayList<String> replacedKey= new ArrayList<String>();
	    HashMap<String, String> positionToBadwordMap = new HashMap<String, String>();
	    ArrayList<Integer> toExcludeStartPositions = new ArrayList<Integer>();
	    ArrayList<Integer> toExcludeEndPositions = new ArrayList<Integer>();

	    public  String filter(String text) {
	        Pattern pattern = Pattern.compile("\\b(" + String.join("|", ignoreWords) + ")\\b", Pattern.CASE_INSENSITIVE);
	        System.out.println(pattern.pattern());	  
	        Matcher matcher = pattern.matcher(text);
//	       / System.out.println(matcher.group());	
	        StringBuilder filteredText = new StringBuilder();
	        int lastEnd = 0;
	        System.out.println("in");
	        int pos=0;
	        while (matcher.find()) {
	        	System.out.println(matcher.start()+","+lastEnd);
	        	//System.out.println(matcher.find());
	            filteredText.append(text, lastEnd, matcher.start());
	            String replaced="";
	            
	            
	            for ( int i = 0; i < matcher.group().length(); i++) {
	            	
	            	if(i==0) {
	            		toExcludeStartPositions.add(filteredText.length()-1+1);
	            	}
	            	
	            	if(i<=matcher.group().length()-2) {
	                filteredText.append("#");
	                replaced= replaced+"#";
	            	}else {
	            		 filteredText.append(""+pos);
	 	                replaced= replaced+""+pos;
	 	               toExcludeEndPositions.add(filteredText.length()-1);

	            	}
	              
	            }
	           
	            positionToBadwordMap.put(replaced,matcher.group());
	            replacedKey.add(replaced);
	            pos++;
	            lastEnd = matcher.end();
	        }
	        filteredText.append(text.substring(lastEnd));
//	        System.out.println("28"+filteredText.toString().charAt(28));
//	        System.out.println("29"+filteredText.toString().charAt(29));
//	        System.out.println("36"+filteredText.toString().charAt(36));
//	        System.out.println("37"+filteredText.toString().charAt(37));
//	        System.out.println(toExcludeStartPositions.toString());
//	        System.out.println(toExcludeEndPositions.toString());
	     
	        return filteredText.toString();
	    }
	    
	    
//	    public  String filter(String text) {
//	        Pattern pattern = Pattern.compile("\\b(" + String.join("|", ignoreWords) + ")\\b", Pattern.CASE_INSENSITIVE);
//	        System.out.println(pattern.pattern());	  
//	        Matcher matcher = pattern.matcher(text);
////	       / System.out.println(matcher.group());	
//	        StringBuilder filteredText = new StringBuilder();
//	        int lastEnd = 0;
//	        System.out.println("in");
//	        int pos=0;
//	        while (matcher.find()) {
//	        	System.out.println(matcher.start()+","+lastEnd);
//	        	//System.out.println(matcher.find());
//	            filteredText.append(text, lastEnd, matcher.start());
//	            String replaced="";
//	            
//	            
//	            for (int i = 0; i < matcher.group().length(); i++) {
//	                filteredText.append(pos+"#");
//	                replaced= replaced+ pos+"#";
//	              
//	            }
//	            positionToBadwordMap.put(replaced,matcher.group());
//	            replacedKey.add(replaced);
//	            pos++;
//	            lastEnd = matcher.end();
//	        }
//	        filteredText.append(text.substring(lastEnd));
//	        
//	     
//	        return filteredText.toString();
//	    }
	    
	    public  String backConvert(String text,String extraFiltereedText) {

	    	 StringBuilder extraFiltereedTextBuilder = new StringBuilder(extraFiltereedText);
	    	for(int i=0;i<toExcludeStartPositions.size();i++) {
	    		
	    		int start = toExcludeStartPositions.get(i);
	    		int end =toExcludeEndPositions.get(i) ;		
	    		extraFiltereedTextBuilder.replace(start, end+1 , text.substring(start, end+1));
	    		
	    	}
	    	
	    	text = extraFiltereedTextBuilder.toString();
	    	System.out.println("extraFiltereedTextBuilder"+extraFiltereedTextBuilder);
	    	// Pattern pattern = Pattern.compile("\\b(" + String.join("|", replacedKey) + ")\\b", Pattern.CASE_INSENSITIVE);
	    	Pattern pattern = Pattern.compile("(" + String.join("|", replacedKey) + ")", Pattern.CASE_INSENSITIVE);
	    	Matcher matcher = pattern.matcher(text);
	         StringBuilder filteredText = new StringBuilder();
	         System.out.println("out while");
	         int lastEnd = 0;
	         while (matcher.find()) {
	        	  System.out.println("in while");
	             filteredText.append(text, lastEnd, matcher.start());
	             String replaced = matcher.group();
	             String value = positionToBadwordMap.get(replaced);
	             if(value!=null)
	            	 filteredText.append(value);
	             lastEnd = matcher.end();
	         }
	         filteredText.append(text.substring(lastEnd));
	         
	         
	         
	         
	         
	         
	         return filteredText.toString();
	         
//		        
	    	
	    	
	    }
	    
	    
	    public String adequateCensor(String text, String extraFiltereedText) {
	    	
	    	
	    	   TextFilter tf = new TextFilter();
		     
		        String exculteInFilter = tf.filter(text);    
		        System.out.println(exculteInFilter);
		        String correctCensored = tf.backConvert(exculteInFilter,extraFiltereedText);
		        System.out.println("correctCensored  ****** "+correctCensored);
	    	
		        return correctCensored;
	    	
	    }
	    
	    

}

// Iterator hmIterator = positionToBadwordMap.entrySet().iterator();

// Display message only
//  System.out.println(
///    "HashMap after adding bonus marks:");

// Iterating through Hashmap and
// adding some bonus marks for every student
//for (Map.Entry<String,String> mapElement : positionToBadwordMap.entrySet()) {
//    String key = mapElement.getKey();
//
//    // Adding some bonus marks to all the students
//    //int value = (mapElement.getValue() + 10);
//
//    // Printing above marks corresponding to
//    // students names
//    System.out.println(key + " : " + mapElement.getValue());
//}