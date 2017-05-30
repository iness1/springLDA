package comparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

/**
 * Comparator tweet by calendar
 * @author iness
 *
 */
public class TweetByDateComparator implements Comparator<String> {
	Map<String, Integer> base;
	SimpleDateFormat sdf;
	
	public TweetByDateComparator(Map<String, Integer> base) {
		this.base = base;
		this.sdf = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * Comparaison de 2 tweets par date
	 */
	public int compare(String a, String b) {
		Date date1;
		Date date2;
		try {
			date1 = sdf.parse(a);
		   date2 = sdf.parse(b);
		   return date1.compareTo(date2);
		} catch (ParseException e) {
			return 0;
		}
	}

}
