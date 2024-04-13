<template>
    <img src="../assets/logo.png" alt="Access Logo" style="width: 200px;">
    <div class="content">
        <h1> Register</h1>

        <div class="user_input">
            <q-input outlined v-model="user.firstname" type="text" label="First Name" />
            <q-input outlined v-model="user.lastname" type="text" label="Last Name" />
            <q-input outlined v-model="user.email" type="email" label="E-Mail" />
            <q-input outlined v-model="user.password" :type="ispwd ? 'password' : 'text'" label="Password">
                <template v-slot:append>
                    <q-icon
                        :name="ispwd ? 'visibility_off' : 'visibility'"
                        class="cursor-pointer"
                        @click="ispwd = !ispwd"
                    />
                </template>
            </q-input>
            <q-input outlined v-model="check_password" :type="ispwd ? 'password' : 'text'" label="Repeat Password">
                <template v-slot:append>
                    <q-icon
                        :name="ispwd ? 'visibility_off' : 'visibility'"
                        class="cursor-pointer"
                        @click="ispwd = !ispwd"
                    />
                </template>
            </q-input>

        </div>

        <div class="q-pa-md">
            <div class="q-gutter-sm">
                <div>
                    <q-checkbox v-model="right" label="Datenschutz zustimmen" />
                </div>
            </div>
        </div>

        <div class="buttonclass">
            <q-btn class="buttonitem" outline rounded color="black" @click="login">Login</q-btn>
            <q-btn class="buttonitem" unelevated rounded color="primary" @click="save">Register</q-btn>
        </div>
    </div>
</template>

<script>
import {ref} from "vue";
import {useUserStore} from "../stores/users";
import {useRouter} from 'vue-router'
import {useQuasar} from "quasar";

export default {
    setup() {
        const right = ref(false)
        const check_password = ref('')
        const ispwd = ref(true);
        const user = ref({
            email: "",
            firstname: "",
            lastname: "",
            password: ""
        })

        const $q = useQuasar()
        const userStore = useUserStore()
        const router = useRouter()

        function login() {
            router.push('/Login')
        }

        function save() {
            if (user.value.email === "" || user.value.firstname === "" || user.value.lastname === ""
                || user.value.password === "" || check_password.value === "" || right.value === false) {
                $q.notify({
                    type: 'negative',
                    message: 'Registrierung fehlgeschlagen!',
                    caption: 'Bitte füllen Sie alle Felder aus!'
                })
                // if repeated password is not same, an Error appears
            } else if (user.value.password !== check_password.value) {
                $q.notify({
                    type: 'negative',
                    message: 'Registrierung fehlgeschlagen!',
                    caption: 'Die angegebenen Passwörter stimmen nicht überein!'
                })
            } else {
                userStore.requestSaveUser(user.value)
                $q.notify({
                    type: 'positive',
                    message: 'Verifizierung nötig!',
                    caption: 'Eine Email wird zur Verifizierung gesendet.'
                })
                router.push('/Login')
            }
        }

        return {
            user,
            check_password,
            save,
            ispwd,
            login,
            right
        }
    }
}
</script>

<style scoped>

.content{
    margin: 40px;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
}

input {
    width: 30%;
    padding-bottom: 10px;
    padding-top: 10px;
    margin-bottom: 10px;
    border-radius: 10px;
    font-size: 100%;
}

input[type=checkbox] {
    width: auto;
}

</style>
