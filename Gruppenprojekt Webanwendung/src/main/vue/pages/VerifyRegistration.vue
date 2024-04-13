<template>
    <h1 id="header"> Verifizierung wird durchgeführt...</h1><br>
    <div>
        <q-spinner-dots id="dots" color="black" size="4em"/>
    </div>
</template>

<script>
import {useUserStore} from "../stores/users";
import {useRoute, useRouter} from "vue-router";
import {useQuasar} from "quasar";
import {useI18n} from "vue-i18n";

export default {
    setup() {
        const $q = useQuasar()
        const userStore = useUserStore()
        const router = useRouter()
        const route = useRoute()
        const token = route.params.token
        validate()

        const {t} = useI18n({});

        function validate() {
            userStore.requestValidateUser(token).then(() => {
                $q.notify({
                    type: 'positive',
                    message: t('verification.success'),
                    caption: t('verification.caption')
                })
                router.push('/Login')
            }).catch(() => {
                $q.notify({
                    type: 'negative',
                    message: t('verification.failed'),
                    caption: t('verification.failedtext')
                })
                router.push('/verifyAccountFailed')
            })
        }
    }
}
</script>

<style scoped>

</style>
