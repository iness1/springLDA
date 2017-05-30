package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import comparator.MapComparator;
import entities.TweetEntity;

/**
 * @author iness
 */
public class MostUsedHashService extends GenericService {
	private static MostUsedHashService singleton;
	
	/**
	 * private constructor
	 */
	private MostUsedHashService(){
		
	}
	
	/**
	 * Recuperation du singleton
	 * @return
	 */
	public static MostUsedHashService getSingleton(){
		if(null == singleton){
			singleton = new MostUsedHashService();
		}
		return singleton;
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
}
