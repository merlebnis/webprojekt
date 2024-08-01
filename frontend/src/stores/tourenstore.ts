import { reactive, readonly, ref, toRaw } from "vue";
import { defineStore } from "pinia";
import type { ITourDTD } from "./ITourDTD";
import { useInfo } from "@/composables/useInfo";
import { Client } from "@stomp/stompjs";
import type { IFrontendNachrichtEvent } from "@/services/IFrontendNachrichtEvent";

export const useTourenStore = defineStore("listestore", () => {

    const infoSetzen = useInfo().setzeInfo;
    interface ITourData {
        ok: boolean,
        lst: Array<ITourDTD>
    }
    const tourdata = reactive<ITourData>({ok:false,  lst:[]});

    const DEST = "/topic/tour";
    const stompclient = new Client({brokerURL:`ws://${window.location.host}/stompbroker`})
    stompclient.onWebSocketError = (event) => {infoSetzen(event)};
    stompclient.onStompError = (frame) => {infoSetzen(frame.body)}

    stompclient.onConnect = (frame) => {
        console.log("Connected");
        stompclient.subscribe(DEST, (message) => {
            console.log("subscribed");
            const nachricht : IFrontendNachrichtEvent = JSON.parse(message.body);
            if(nachricht.event = "TOUR"){
                updateTourListe();
                console.log(JSON.stringify(nachricht));
            }
        })
    }

    async function updateTourListe(): Promise<void> {
        try{
            const url = '/api/tour';

            const response = await fetch(url);
            if(!response.ok){
                throw new Error(response.statusText);
            }


            const jsondata : ITourDTD[] = await response.json();
            tourdata.lst = jsondata
            tourdata.ok = true;
     
            startTourLiveUpdate();
        } catch (reason){ 
            infoSetzen("Fehler beim Touren abrufen");
            tourdata.ok = false;
        }
    }
    

    function findTourByID(id : number): ITourDTD {
        for(let value of tourdata.lst) {
            if(value.id === id){
                return  value;
            }
        };
        return {id : 0, abfahrDateTime : "0", preis : 0, plaetze : 0, buchungen :0, startOrtId: 0, zielOrtId : 0, startOrtName :"", zielOrtName : "", anbieterId : 0, anbieterName : "", distanz :0, info :""};
    }

    function startTourLiveUpdate(){
        if(!stompclient.active){
            stompclient.activate();
            console.log("Stomp aktiviert");
        }

        console.log("Stomp ist bereits aktiv");
    }

    return {
        tourdata: tourdata,
        updateTourListe,
        findTourByID,
        startTourLiveUpdate
    };
});