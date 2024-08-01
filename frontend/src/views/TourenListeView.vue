<template>
    <h1 class="title">Das aktuelle MItfahrangebot</h1>
  
    <input type="text" v-model="suchbegriff" placeholder="Suche..."></input>
    <button @click="resetSuche()">Reset</button>
    <TourenListe :touren="filterListe"></TourenListe>
  </template>
  
  <script setup lang="ts">
    import { ref, computed, onMounted } from 'vue';
    import TourenListe from '@/components/tour/TourenListe.vue'
    import { useTourenStore } from '@/stores/tourenstore';
    const { updateTourListe, tourdata} = useTourenStore()
      
    onMounted( async () => {
      updateTourListe();
    })

    const suchbegriff = ref("");

    const filterListe = computed(() => {
      return tourdata.lst.filter(e => e.zielOrtName.toLowerCase().includes(suchbegriff.value.toLowerCase()) || e.startOrtName.toLowerCase().includes(suchbegriff.value.toLowerCase()));
    });

    function resetSuche(){
      suchbegriff.value = "";
    }
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
</style>