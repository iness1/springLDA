package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import comparator.MapComparator;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;
import entities.TweetEntity;

/**
 * 
 * @author iness
 *
 */
public class ParsingService {
	private static ParsingService singleton;
	
	/**
	 * private constructor
	 */
	private ParsingService(){
		
	}
	
	public static ParsingService getSingleton(){
		if(null == singleton){
			singleton = new ParsingService();
		}
		return singleton;
	}
	
	/**
	 * Constructors
	 */
	public TweetEntity buildTweet(String line){
		TweetEntity tweet = new TweetEntity();
		Integer currentPosition = 0;	
		String usingPart;
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss", Locale.ENGLISH);
		while(currentPosition <= 16 && line.length() > 0){
			switch(currentPosition){
			
				case 0:
					usingPart = line.substring(0, line.indexOf(",")).replace("\"", "");
					tweet.setLigneNumber(Integer.parseInt(usingPart));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 1:					
					Integer textEnd = getTextEnd(line);
					tweet.setText(line.substring(0, textEnd).replace("\"", ""));
					line = line.substring(textEnd+2, line.length());
					break;
					
				case 2:
					usingPart = line.substring(0, line.indexOf(",")).replace("\"", "").toLowerCase();
					tweet.setFavorited(Boolean.parseBoolean(usingPart));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 3:
					usingPart = line.substring(0, line.indexOf(",")).replace("\"", "");
					tweet.setFavoritedCount(Integer.parseInt(usingPart));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 4:
					tweet.setReplyToSN(line.substring(0, line.indexOf(",")).replace("\"", ""));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 5:
					usingPart = line.substring(0, line.indexOf(",")).replace("\"", "");				
					tweet.setCreate(Calendar.getInstance());				
					try {
						tweet.getCreated().setTime(sdf.parse(usingPart));
					} catch (ParseException e) {
						
					}
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 6:
					usingPart = line.substring(0, line.indexOf(",")).replace("\"", "").toLowerCase();
					tweet.setTruncated(Boolean.parseBoolean(usingPart));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 7:
					tweet.setReplyToSID(line.substring(0, line.indexOf(",")).replace("\"", ""));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 8:
					usingPart = line.substring(0, line.indexOf(",")).replace("\"", "");
					tweet.setId(Long.parseLong(usingPart));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 9:
					tweet.setReplyToUID(line.substring(0, line.indexOf(",")).replace("\"", ""));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 10:
					tweet.setStatusSource(line.substring(1, line.indexOf(",")-1));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 11:
					tweet.setScreenName(line.substring(0, line.indexOf(",")).replace("\"", ""));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 12:
					usingPart = line.substring(0, line.indexOf(",")).replace("\"", "");
					tweet.setRetweetCount(Integer.parseInt(usingPart));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 13:
					usingPart = line.substring(0, line.indexOf(",")).replace("\"", "").toLowerCase();
					tweet.setIsRetweet(Boolean.parseBoolean(usingPart));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 14:
					usingPart = line.substring(0, line.indexOf(",")).replace("\"", "").toLowerCase();
					tweet.setRetweeted(Boolean.parseBoolean(usingPart));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 15:
					tweet.setLongitude(line.substring(0, line.indexOf(",")).replace("\"", ""));
					line = line.substring(line.indexOf(",")+1, line.length());
					break;
					
				case 16:
					tweet.setLatitude(line.replace("\"", ""));
					break;
			}
			currentPosition ++;
		}
		return tweet;
	}
	
	/**
	 * Récuperation de toute la list
	 * @param textFile
	 * @return
	 */
	public List<TweetEntity> getAll(BufferedReader textFile) {
		List<TweetEntity> result = new ArrayList<TweetEntity>();
		String unvalidLine = "";
		Boolean haveUnvalidPart = false;
		String currentLine; 
		Boolean firstLine= true;
		try {
			while ((currentLine = textFile.readLine()) != null) {
				if(!firstLine) {
					if(!haveUnvalidPart && isValide(currentLine)) {
						result.add(buildTweet(currentLine));
					}else if(!haveUnvalidPart && !isValide(currentLine)){
						haveUnvalidPart = true;
						unvalidLine = currentLine;
					}else {
						currentLine =  unvalidLine+currentLine;
						if(isValide(currentLine)){
							haveUnvalidPart = false;
							result.add(buildTweet(currentLine));
						}else{
							unvalidLine = currentLine;
						}
					}
				} else {
					firstLine = false;
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Verification de la validité de la chaine
	 * @param currentLine
	 * @return
	 */
	public boolean isValide(String currentLine) {	
		int total = 0;
		for(int i=0;  i<currentLine.length(); i++) {
			if(currentLine.charAt(i) == ',') {
				total ++;
			}
		}
		if (total >= 16) {
			if(isNumber(currentLine.substring(0, currentLine.indexOf(",")).replace("\"", ""))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verification position fin de text
	 * @param currentLine
	 * @return
	 */
	public Integer getTextEnd(String currentLine) {
		Integer posFalse = currentLine.indexOf("\",FALSE");
		Integer posTrue = currentLine.indexOf("\",TRUE");
		if(posFalse == -1){						
			return posTrue;
		}else if(posTrue == -1){
			return posFalse;
		}else {
			if(posFalse < posTrue) {
				return posFalse;
			}else {
				return posTrue;
			}
		}
	}

	
	/**
	 * Nbr of each hashtag 
	 * @param tweets
	 * @return
	 */
	public Map<String, Integer> hashtagByValue(List<TweetEntity> tweets){
		Map<String, Integer> result = new HashMap<String, Integer>();
		if(null == tweets){
			return result;
		}
		for(TweetEntity tweet : tweets){	
			String text = tweet.getText().toLowerCase();
			while(text.length() > 0 ){
				Integer startHash = indexOf(text, '#');			
				if(startHash > -1){				
					String currentHash = text.substring(startHash);
					List<Integer> positions = new ArrayList<Integer>();
					positions.add(indexOf(currentHash, ' '));
					positions.add(indexOf(currentHash, '#'));
					positions.add(indexOf(currentHash, '.'));
					positions.add(indexOf(currentHash, ';'));
					positions.add(indexOf(currentHash, ':'));
					positions.add(indexOf(currentHash, '!'));
					positions.add(indexOf(currentHash, ','));
					positions.add(indexOf(currentHash, '?'));					
					int min = getMinPlace(positions, currentHash.length());
					currentHash = currentHash.substring(0, min);					
					text = text.substring(startHash+currentHash.length(), text.length());
					if(null != result.get(currentHash)) {
						result.put(currentHash, result.get(currentHash) + 1);
					}else {
						result.put(currentHash, 1);
					}
				}else{
					text = "";
				}
				
			}			
		}
		return result;
	}

	/**
	 * Index of
	 * @param text
	 * @param value
	 * @return
	 */
	private Integer indexOf(String text, char value){
		int result = -1;
		for(int i=0; i<text.length(); i++){
			if(text.charAt(i) == value){
				return i;
			}
		}
		return result;				
	}
	
	/**
	 * get the min position
	 * @param positions
	 * @param length
	 * @return
	 */
	public Integer getMinPlace(List<Integer> positions, Integer length){
		int result = length;
		for(Integer position : positions){
			if(result > position && position >0){
				result = position;
			}
		}
		return result;
	}
	
	/**
	 * Get the maxResult most Used hashTag
	 * @param allHash
	 * @param maxresult
	 * @return
	 */
	public Map<String, Integer>  getMostUsed(Map<String, Integer> allHash, Integer maxresult){
		Map<String,Integer> mapTriee = new TreeMap<String,Integer>(new MapComparator(allHash));
		Map<String,Integer> result = new HashMap<String,Integer>();
		mapTriee.putAll(allHash);
		int total = 0;
		for(String key : mapTriee.keySet()){
			result.put(key, allHash.get(key));
			total++;
			if(total>=maxresult){
				return result;
			}
			
		}
		return mapTriee;
	}
	
	/**
	 * Get the tweets who constains the most used hashTag
	 * @param allHash
	 * @param tweets
	 * @return
	 */
	public List<TweetEntity> getMostUsedTweets(String hash, List<TweetEntity> tweets){
		List<TweetEntity> result = new ArrayList<TweetEntity>();
		for(TweetEntity tweet : tweets){
			if(tweet.getText().contains(hash)){
				result.add(tweet);
			}	
		}
		return result;
	}
}
