<template>
    <div class="headnav" style="text-align: left">
        <router-link :to="'/Dashboard'" class="navelem">{{$t('menu.dashboard')}}</router-link> <q-icon name="chevron_right" size="16px"/>
        <p class="navelem navactive">{{$t('profile.profile2') + email}}</p>
    </div>
    <h1>{{$t('profile.profile2') + email}}</h1>

    <div class="q-pa-md">
        <div class="q-gutter-y-md">
            <q-card>
                <q-tabs
                    v-model="tab"
                    dense
                    class="text-grey"
                    active-color="primary"
                    indicator-color="primary"
                    align="justify"
                    narrow-indicator
                >
                    <q-tab name="data" :label="$t('profile.data2')"/>
                </q-tabs>

                <q-separator/>

                <q-tab-panels v-model="tab" animated>
                    <q-tab-panel name="data">

                        <q-icon name="person" size="128px" color="primary"/>
                        <q-input v-model="email" :label="$t('profile.email')" :readonly="true"/>
                        <q-input v-model="firstname" :label="$t('profile.vorname')" :readonly="true"/>
                        <q-input v-model="lastname" :label="$t('profile.nachname')" :readonly="true"/>
                        <q-input v-model="score" label="Score" :readonly="true"/>

                    </q-tab-panel>
                </q-tab-panels>
            </q-card>
        </div>
    </div>
</template>

<script>
import {useQuasar} from "quasar";
import {useUserStore} from "../stores/users";
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import {useI18n} from "vue-i18n";
import axios from "axios";

export default {
    setup() {
        const $q = useQuasar()
        const {t} = useI18n({})
        sessionStorage.getItem('token')
        const userStore = useUserStore()
        const router = useRouter()
        const route = useRoute()
        const id = route.params.id
        const currentId = sessionStorage.getItem('id')

        let email = ref('')
        let firstname = ref()
        let lastname = ref()
        let score = ref()
        checkUser()
        loadUser()

        function checkUser() {
            if (id === currentId) {
                router.push('/Profile')
            }
        }

        async function loadUser() {

            await userStore.requestUser(id).then(() => {
                email.value = userStore.getUserWithId().email
                firstname.value = userStore.getUserWithId().firstname
                lastname.value = userStore.getUserWithId().lastname
                if (userStore.getUserWithId().userScoreDisabled === false) {
                    score.value = "N/A%"
                } else {
                    axios.get("/api/ranking/users/" + id).then(r => {
                        score.value = r.data[0].points
                    })
                }
            }).catch(() => {
                $q.notify({
                    type: 'negative',
                    message: 'Profil nicht gefunden!',
                    caption: 'Das gesuchte Profil existiert nicht!'
                })
                router.push('/Profile')
            })

        }

        return {
            email,
            firstname,
            lastname,
            score,
            tab: ref('data'),
        }
    }
}
</script>

<style scoped>

</style>
