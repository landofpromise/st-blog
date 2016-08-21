/**
 * 
 */
package socns.lang;


/**
 * @author langhsu
 *
 */
public interface Consts {
	/**
	 * 默认头像
	 */
	String AVATAR = "/dist/images/ava/default.png";
	
	/**
	 * 文件目录步进
	 */
	int FILE_PATH_SEED = 997;
	
	String TYPE_TEXT = "text";
	String TYPE_IMAGE = "image";
	
	String SEPARATOR = ",";
	
	String UPLOAD_ROOT = "/store";
	
	String ERROR_PAGE_404 = "/error/404.html";
	
	int IDENTITY_STEP = 1;
	
	int TIME_MIN = 1000;
	
	/* 状态 */
	int status_normal = 0;
	
	int status_featured = 1;
	
	int status_locked = 1;
	
	String order_featured = "featured";
	
	String order_newest = "newest";
	
	String order_hottest = "hottest";
}
