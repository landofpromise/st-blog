package socns.web.utils;

import java.util.HashMap;
import java.util.Map;

public class UserInfoCache {
	private static Map<String, String> userMap = new HashMap();

	public static synchronized void put(String username, String password) {
		userMap.put(username, password);
	}

	public static synchronized String get(String username) {
		return (String) userMap.get(username);
	}
}
