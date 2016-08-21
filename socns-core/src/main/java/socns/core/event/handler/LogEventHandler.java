/**
 * 
 */
package socns.core.event.handler;

import java.util.Date;

import mtons.modules.exception.MtonsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import socns.core.event.LogEvent;
import socns.lang.EnumLog;
import socns.persist.service.LogService;
import socns.persist.service.PostService;

/**
 * @author langhsu
 *
 */
public class LogEventHandler implements ApplicationListener<LogEvent> {
	@Autowired
	private LogService logService;
	@Autowired
	private PostService postService;
	
	@Override
	public void onApplicationEvent(LogEvent event) {
		EnumLog type = event.getType();
		Date now = new Date();
		
		switch (type) {
			case LIKED:
				int logs = logService.statsByDay(type.getIndex(), event.getUserId(), event.getTargetId(), event.getIp(), now);
				if (logs > 0) {
					throw new MtonsException("您今天已经支持过该文章了");
				}
				postService.identityHearts(event.getTargetId());
				logService.add(type.getIndex(), event.getUserId(), event.getTargetId(), event.getIp());
				break;
			case BROWSE:
				break;
			default:
				break;
		}
	}


}
