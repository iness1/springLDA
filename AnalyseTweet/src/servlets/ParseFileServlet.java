package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.TweetDAO;
import entities.TweetEntity;
import responses.MostTweetedHash;
import responses.TweetByHash;
import services.ParsingService;

/**
 * Servlet implementation class ParseFileServlet
 */
@WebServlet("/parse")
@MultipartConfig
public class ParseFileServlet extends GenericServlet {
	private static final long serialVersionUID = 1L;
    
	private final String MOST_TWEETED_HASH = "1";
	private final String MAP_BY_HASH = "2";
	private final String UNIQUE_TWEET = "3";
	private final String TWEET_UNIQUE_BY_SOURCES = "4";
	private final String DELETE_BASE = "5";
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		TweetDAO tweetDAO = TweetDAO.getSingleton();
		ParsingService parsingService = ParsingService.getSingleton();
		List<TweetEntity> tweets = tweetDAO.getAll();
		switch(action){
			case MOST_TWEETED_HASH :
				MostTweetedHash mostTweetedHash = new MostTweetedHash();
				mostTweetedHash.setData(parsingService.getMostUsed(parsingService.hashtagByValue(tweets), 20));
				super.buildAndSend(response, mostTweetedHash, MostTweetedHash.class);
				break;
			case MAP_BY_HASH :
				TweetByHash tweeByHash = new TweetByHash();
				String hash = request.getParameter("hash");
				tweeByHash.setData(parsingService.getMostUsedTweets(hash,tweets));
				super.buildAndSend(response, tweeByHash, TweetByHash.class);
				break;
			case UNIQUE_TWEET :
				break;
			case TWEET_UNIQUE_BY_SOURCES :
				break;
			case DELETE_BASE : 
				tweetDAO.deleteAll();
				super.buildAndSend(response, "success", String.class);
				break;
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TweetDAO tweetDAO = TweetDAO.getSingleton();
		ParsingService parsingService = ParsingService.getSingleton();
		try {
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			for (FileItem item : items) {	        	
	        	if (!item.isFormField()) {
	        		if(("file").equals(item.getFieldName())){
	        			InputStreamReader stream = new InputStreamReader(item.getInputStream());
	        			BufferedReader reader = new BufferedReader(stream);		
	        			List<TweetEntity> tweets = parsingService.getAll(reader);
	        			tweetDAO.createFromFile(tweets);
	        		}
	            }
	        }
		} catch (FileUploadException | IOException e1) {
			System.out.println(e1.getMessage());
		}
	}
}