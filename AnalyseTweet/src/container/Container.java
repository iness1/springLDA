package container;

import java.util.ArrayList;
import java.util.List;

import entities.TweetEntity;
import services.FichierLDA;
import services.LanguageDetection;
import services.MostUsedHashService;
import services.NombreTweetPerRetweetService;
import services.ParsingService;
import services.StopWordService;
import services.TweetByHashService;
import services.TweetByUserService;
import services.TweetPerObjectifService;
import services.UniqueTweetService;

/**
 * Données laisée en RAM pour éviter les aller-retour en base de données et l'instanciation
 * @author iness
 */
public class Container {
	
	/**
	 * Les données en RAM
	 */
	public static List<TweetEntity> TWEETS = new ArrayList<TweetEntity>();
	
	/**
	 * Services
	 */
	public static MostUsedHashService mostUsedHashService = MostUsedHashService.getSingleton();
	public static ParsingService parsingService = ParsingService.getSingleton();
	public static TweetByHashService tweetByHashService = TweetByHashService.getSingleton();
	public static UniqueTweetService uniqueTweetService = UniqueTweetService.getSingleton();
	public static TweetByUserService tweetByUserService = TweetByUserService.getSingleton();
	public static NombreTweetPerRetweetService nombreTweetPerRetweetService = NombreTweetPerRetweetService.getSingleton();
	public static TweetPerObjectifService tweetPerObjectifService = TweetPerObjectifService.getSingleton();
	public static StopWordService stopWordService = StopWordService.getSingleton();
	public static FichierLDA fichierLDA = FichierLDA.getSingleton();
	//public static LanguageDetection languageDetection = LanguageDetection.getSingleton();
}
