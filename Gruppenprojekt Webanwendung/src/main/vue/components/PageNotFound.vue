<template>
    <div class="title">
    <q-toolbar-title>
        {{ $t("404.title") }}</q-toolbar-title>
    </div>
    <br>
    <h1> {{ $t("404.masage") }} </h1>
    <br>    <br>

    <q-btn @click="goToPrevious" color="primary">
        {{ $t("404.button") }}
    </q-btn>
</template>

<script>
export default {
    name: "PageNotfound",
    methods: {
        //path to the previous page
        goToPrevious() {
            this.$router.go(-2);
        }
    }

}
</script>

<style scoped>
.title{
    color: #b22222;
}
</style>
