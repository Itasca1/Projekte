<template>
    <h1>{{$t("verification.failed")}}</h1><br>
    <div>
        <q-icon name="warning" color="negative" size="4rem"/>
    </div>
    <p>{{$t("verification.failedtext")}}</p>

    <div class="buttonclass">
        <q-btn class="buttonitem" unelevated rounded color="primary" @click="login">{{$t("verification.back")}}</q-btn>
    </div>
</template>

<script>
import router from "../router";

export default {
    setup() {
        function login() {
            router.push('/Login')
        }

        return {
            login
        }
    }
}
</script>

<style scoped>

</style>
