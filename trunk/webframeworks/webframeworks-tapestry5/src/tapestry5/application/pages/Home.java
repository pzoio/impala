package tapestry5.application.pages;

import java.util.Date;

import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;

import tapestry5.application.MessageHolder;

/**
 * Start page of application.
 */
public class Home {
	
	/*
	This part of the example does not work because of an issue supporting factory beans. See:
	https://issues.apache.org/jira/browse/TAPESTRY-2706
	@Inject
	@Service("messageService")
	private MessageService messageService;	
	*/
	
	@Inject
	@Service("messageHolder")
	private MessageHolder messageHolder;
	
	public Date getCurrentTime() {
		return new Date();
	}
	
	public String getMessage(){
		return messageHolder.getMessage();
		//return messageService.getMessage();
	}
	
}