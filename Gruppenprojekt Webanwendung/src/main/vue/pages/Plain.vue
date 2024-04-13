<template>

    <!--
        This is just a example/template page for styling. should not be accessible via routing/urls
    -->

    <div class="header">
        <div class="headerLeft">
            <div class="headnav" style="text-align: left">
                <router-link :to="'/Dashboard'" class="navelem">Dashboard</router-link> <q-icon name="chevron_right" size="16px"/>
                <router-link :to="'/Dashboard'" class="navelem">Dashboard</router-link> <q-icon name="chevron_right" size="16px"/>
                <p class="navelem navactive">Active</p>
            </div>
            <h1>Seitenüberschrift Template</h1>
        </div>
        <div style="display: flex; flex-direction: row; align-items: center; justify-content: center">
            <h4>Score</h4> <p class="scoreRect">50%</p>
        </div>
    </div>
    <h2>Section-Überschrift</h2>
    <h3>Untersektion</h3>
    <h4>h4</h4>
    <h5>h5</h5>
    <h6>h6</h6>
    <p>Dies ist ein Paragraph. Die Seite besitzt nur das Margin der #contentcard</p>

    <div class="buttonclass">
        <q-btn class="buttonitem" unelevated rounded color="primary">Primary</q-btn>
    </div>

    <div class="buttonclass"><q-btn class="buttonitem" outline rounded color="black">Secondary</q-btn></div>

    <div class="buttonclass">
        <q-btn class="buttonitem" unelevated rounded color="primary">Primary</q-btn>
        <q-btn class="buttonitem" outline rounded color="black">Secondary</q-btn>
        <div class="buttonitem">
            <q-btn flat round color="black" icon="more_vert" />
            <q-menu touch-position>
                <q-list style="min-width: 100px">
                    <q-item clickable v-close-popup>
                        <q-item-section>Branches</q-item-section>
                    </q-item>
                    <q-item clickable v-close-popup>
                        <q-item-section>Leaves</q-item-section>
                    </q-item>
                    <q-item clickable v-close-popup>
                        <q-item-section>Roots</q-item-section>
                    </q-item>
                </q-list>
            </q-menu>
        </div>
    </div>

</template>

<script>
export default {
    name: "Plain"
}
</script>

<style scoped>

</style>
