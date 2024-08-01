<template>
    <h1 class="title">Tour {{ tour.id }} von {{ tour.startOrtName }} nach {{ tour.zielOrtName }}</h1>
    <br/>
    <p>{{ tour.info }}</p>
    <br/>
    <p>Abfahrt {{ tour.abfahrDateTime }}</p>
    <p>Preis {{ tour.preis }} EURO für {{ entfernung }} km.</p>
    <p>Anbieter ist {{ tour.anbieterName }}</p>
    <p>Schon {{ tour.buchungen }} von {{ tour.plaetze }} gebucht, also noch {{ frei }} Plätze frei.</p>
  </template>
  
  <script setup lang="ts">
    import { computed, onMounted, watchEffect, ref } from 'vue';
    import TourenListe from '@/components/tour/TourenListe.vue'
    import { useTourenStore } from '@/stores/tourenstore';
    import type { ITourDTD } from '@/stores/ITourDTD';
    import { useInfo } from '@/composables/useInfo';
    const { updateTourListe, tourdata, findTourByID} = useTourenStore()

    const info = useInfo().info;
    const infoSetzen = useInfo().setzeInfo;
      
    const props = defineProps<{tourid: string}>();

    const tourid = parseInt(props.tourid);
    const tour = ref(findTourByID(tourid));

    watchEffect(async () => {
      await updateTourListe();
      if(entfernung.value > 300){
        infoSetzen("Was für eine lange Fahrt von "+ tour.value.startOrtName + " nach " + tour.value.zielOrtName + "!");
      }
    })

    const frei = computed(() => tour.value.plaetze - tour.value.buchungen)
    const entfernung = computed(() => Math.round(tour.value.distanz));

    

    onMounted(async () => {
        if(!tourdata.ok){
          await updateTourListe();
          tour.value = findTourByID(tourid);
        }
      });
  </script>
  
  <style scoped>
    button{
      width: 6em;
      float: right;
    }

    input{
      width: 70%;
      display: inline;
      margin-bottom: 0.25em;
      padding: 0.75em;
      border-radius: 0;
    }


    p{
      margin:0;
    }
</style>