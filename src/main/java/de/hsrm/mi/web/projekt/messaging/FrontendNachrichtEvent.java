package de.hsrm.mi.web.projekt.messaging;

public class FrontendNachrichtEvent {
    private EventTyp event;
    private long id;
    private AenderungsTyp aenderung;


    public FrontendNachrichtEvent(EventTyp event, long id, AenderungsTyp aenderung){
        this.event = event;
        this.id = id;
        this.aenderung = aenderung;
    }

    public EventTyp getEvent() {
        return event;
    }

    public long getId(){
        return id;
    }

    public AenderungsTyp getAenderung() {
        return aenderung;
    }

    @Override
    public String toString(){
        return "Event: " + event.toString() + " " + aenderung.toString();
    }
}
