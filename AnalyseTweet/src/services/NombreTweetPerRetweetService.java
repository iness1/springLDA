package services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.TweetEntity;

/**
 * 
 * @author iness
 *
 */
public class NombreTweetPerRetweetService extends GenericService {
	private static NombreTweetPerRetweetService singleton;

	/**
	 * private constructor
	 */
	private NombreTweetPerRetweetService() {

	}

	/**
	 * Recuperation du singleton
	 * 
	 * @return
	 */
	public static NombreTweetPerRetweetService getSingleton() {
		if (null == singleton) {
			singleton = new NombreTweetPerRetweetService();
		}
		return singleton;
	}

	public Map<String, Integer> getNbRetweetPerTweet(List<TweetEntity> tweets) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (TweetEntity tweet : tweets) {
			if (!tweet.getIsRetweet()) {
				result.put("" + tweet.getLigneNumber(), tweet.getRetweetCount());

			}
		}
		return result;
	}

}
