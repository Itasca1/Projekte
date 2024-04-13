<template>
    <div>
        <h4> Email versenden</h4><br>

        <label>To :</label><br>

        <input required v-model="email.sentTo" placeholder="Empfänger eingeben"><br>

        <label>Subject :</label><br>

        <input required v-model="email.subject" placeholder="Betreff eingeben"><br>

        <label>Message :</label><br>
        <input required v-model="email.message" placeholder="Nachricht eingeben"><br>

        <button type="submit" class="button" v-on:click="send">Senden</button>
    </div>
</template>


<script>
import {ref} from "vue";
import axios from "axios";
import {useQuasar} from "quasar";

export default {
    setup() {
        const email = ref({
            subject: "",
            message: "",
            sentTo: "",
        })
        const $q = useQuasar()

        function send() {
                let emailCmd = {
                    subject: this.email.subject,
                    message: this.email.message,
                    sentTo: this.email.sentTo
                }
                if (emailCmd.sentTo === "") { return null }
                else {
                    try {
                        return axios.post('/api/sendEmail', emailCmd)
                    } catch (e) {
                        $q.notify({
                            type: 'negative',
                            message: 'Email Versenden fehlgeschlagen',
                            caption: 'Bitte füllen Sie alle Felder!'
                        })
                    }
                }
            }

        return {
            email,
            send
        }
    }
}
</script>

<style scoped>

</style>
