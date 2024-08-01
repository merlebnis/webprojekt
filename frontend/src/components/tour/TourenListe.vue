<template>
    <table>
      <thead>
        <th>Abfahrt</th>
        <th>von</th>
        <th>nach</th>
        <th class="entfernung">Entfernung</th>
        <th class="plaetze">Plätze</th>
        <th class="smol">frei</th>
        <th class="smol">Preis</th>
        <th class="invisible"></th>
      </thead>
      <tbody>
        <TourenListeZeile :item="ele" v-for="ele in props.touren" :key="ele.id" :zeile="ele"/>
      </tbody>
    </table>
</template>
  
  <script setup lang="ts">

  import { ref , computed } from 'vue'
  import type { ITourDTD } from '@/stores/ITourDTD'
  import TourenListeZeile from '@/components/tour/TourenListeZeile.vue'
  
  const props = defineProps<{touren: Array<ITourDTD>}>();
  //const items = ref<Array<ITourDTD>>(props.touren);

  </script>
  
  <style scoped>
    .entfernung{
      width: 5em;
    }

    .plaetze{
      width: 3em;
    }

    .smol{
      width: 4%;
    }
  </style>