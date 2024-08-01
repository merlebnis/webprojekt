package de.hsrm.mi.web.projekt.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FrontendNachrichtService {
    private Logger logger = LoggerFactory.getLogger(FrontendNachrichtService.class);
    @Autowired
    private SimpMessagingTemplate messaging;
    private final String DEST = "/topic/tour";

    public void sendEvent(FrontendNachrichtEvent ev){
        logger.info("neues Event :" + ev );
        messaging.convertAndSend(DEST, ev);
    }
}
