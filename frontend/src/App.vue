<script setup lang="ts">
    import { useInfo } from './composables/useInfo';
    import { RouterView } from 'vue-router';

    const info = useInfo().info;
    const loescheInfo = useInfo().loescheInfo;

    function closeInfo() : void {
        loescheInfo();
    }
</script>

<template>
  <head>
        <link rel="stylesheet" th:href="@{/css/style.css}" />
    </head>
    <body>
        <block>
            <div class="header">
                <span class="links">
                    <i class="fa fa car"></i>
                    <RouterLink to="/touren">Touren</RouterLink>
                </span>
                <span class="languages">
                    <a href="/">DE</a>
                    <a href="/">EN</a>
                    <a href="/">NL</a>
                </span>
            </div>
        </block>
         
        <div class="info" v-if="info !== ''">
            <button class="close" v-on:click="closeInfo()">x</button>
            <p class="errortext" >{{ info }}</p>
        </div>
        
        <RouterView/>

        <footer class="footer">
            <a href="#">Impressum</a>
            <a href="#">Kontakt</a>
        </footer>
    </body>
</template>

<style >

</style>
