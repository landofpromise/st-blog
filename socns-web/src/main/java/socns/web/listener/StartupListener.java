/**
 * 
 */
package socns.web.listener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import socns.data.Config;
import socns.lang.Consts;
import socns.persist.service.ConfigService;

/**
 * @author langhsu
 *
 */
public class StartupListener implements InitializingBean, ServletContextAware {
	@Autowired
	private ConfigService configService;
	
	private ServletContext servletContext;
	
	/**
	 * 加载配置信息到系统
	 * 
	 */
	private void loadConfig() {
        Timer timer = new Timer("loadConfig", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	System.out.println(" --- 站点信息初始化... --- ");
            	
            	List<Config> configs = configService.findAll();
            	for (Config conf : configs) {
            		servletContext.setAttribute(conf.getKey(), conf.getValue());
            	}
            	
            	System.out.println(" --- 站点信息初始化结束 --- ");
            }
        }, 2 * Consts.TIME_MIN);
    }

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadConfig();
	}

}
