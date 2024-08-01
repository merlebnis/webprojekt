<template>
    <tr>
        <td>{{ zeile.abfahrDateTime }}</td>
        <td>{{ zeile.startOrtName }}</td>
        <td>{{ zeile.zielOrtName }}</td>
        <td>{{ entfernung }}</td>
        <td>{{ zeile.plaetze }}</td>
        <td>{{ frei }}</td>
        <td>{{ zeile.preis }}</td>
        <td class="invisible"> <button @click="routing()">Details</button> </td>
    </tr>
</template>

<script setup lang="ts">

import { ref ,computed } from 'vue'
import type { ITourDTD } from '@/stores/ITourDTD'
import { useRouter } from 'vue-router'

const router = useRouter();
const props = defineProps<{zeile: ITourDTD}>();
const zeile = props.zeile;
const frei = computed(() => zeile.plaetze - zeile.buchungen)
const entfernung = computed(() => Math.round(zeile.distanz));

function routing(){
    router.push("/tour/"+ zeile.id)
}

</script>

<style scoped>
    button{
        color: white;
        background-color: rgb(79, 87, 161);
        border-radius: 1em;
        border-color: rgb(79, 87, 161);
        padding-left: 1em;
        padding-right: 1em;
        padding-top: 0.25em;
        padding-bottom: 0.25em;
    }
</style>