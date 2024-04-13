<template>
    <div style="height: 100vh;background-image: url('src/main/vue/assets/background.png');background-size: 100% 100%;">
        <div style="backdrop-filter:blur(10px) brightness(70%) grayscale(100%); width: 100vw; height: 50vh;position: absolute;margin-top: 25vh;">
            <div class="row justify-around">
                <div class="col-6" style="padding-top: 1.5%">
                    <div class="q-pa-md">
                        <q-carousel
                            v-model="slide"
                            transition-prev="slide-left"
                            transition-next="slide-right"
                            swipeable
                            animated
                            control-color="white"
                            navigation
                            padding
                            arrows
                            height="40vh"
                            class="bg-primary text-white shadow-1 rounded-borders"
                        >
                            <q-carousel-slide name="style" class="column no-wrap flex-center" style="background-color: #3F50B5">
                                <div class="text-h6">
                                    {{$t("landingPage.whyDigital")}}
                                </div>
                                <div class="q-mt-md text-center">
                                    {{$t("landingPage.barriere")}}
                                </div>
                            </q-carousel-slide>
                            <q-carousel-slide name="tv" class="column no-wrap flex-center" style="background-color: #3F50B5">
                                <div class="text-h6">
                                    {{$t("landingPage.whyAccess")}}
                                </div>
                                <div class="q-mt-md text-center">
                                    {{ $t("landingPage.access") }}
                                </div>
                            </q-carousel-slide>
                        </q-carousel>
                    </div>
                </div>
                <div class="col-4" style="padding-top: 2.5%">
                    <div>
                        <img alt  src="../assets/logo.png">
                    </div>
                    <div style="padding-top: 1.5vh">
                        <q-btn style=" background-color: #3F50B5; color: #ffffff;" @click="goToLogin">
                            <b>{{ $t("landingPage.start") }}</b>
                        </q-btn>
                    </div>


                </div>
            </div>

        </div>
    </div>
</template>

<script>

</script>

<script setup>
import {useRouter} from "vue-router";
import {ref} from "vue";

const router = useRouter()
const access = ref(false);
const slide = ref("style")

function goToLogin() {
    router.push("/Login")
}

</script>

<style scoped>
</style>
