<template>
    <img src="../assets/logo.png" alt="Access Logo" style="width: 200px;">
    <div class="content">

        <h1> Login </h1>

        <div class="user_input">
            <q-input outlined v-model="username" type="email" id="mail" name="mail" label="E-Mail" />

            <q-input outlined v-model="password" :type="isPwd ? 'password' : 'text'" id="show_password" name="show_password" label="Password">
                <template v-slot:append>
                    <q-icon
                        :name="isPwd ? 'visibility_off' : 'visibility'"
                        class="cursor-pointer"
                        @click="isPwd = !isPwd"
                    />
                </template>
            </q-input>

        </div>

        <div class="buttonclass">
            <q-btn class="buttonitem" unelevated rounded color="primary" @click="login">Login</q-btn>
            <q-btn class="buttonitem" outline rounded color="black" @click="register">Register</q-btn>
        </div>
    </div>
</template>

<script>
import {ref} from 'vue'
import {useUserStore} from "../stores/users"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'

export default {
    name: "Login",
    setup() {
        const $q = useQuasar()
        const router = useRouter()
        const username = ref('')
        const password = ref('')
        const isPwd = ref(true)

        const userStore = useUserStore()

        function forgetpwd() {
            router.push('/Reset_Password')
        }

        function register() {
            router.push('/Registration')
        }

        function login() {
            sessionStorage.removeItem('token')
          sessionStorage.removeItem('id')
            userStore.authenticate(null)
            userStore.requestToken({
                username: username.value,
                password: password.value
            }).then(() => {
                userStore.myId()
                router.push('/Dashboard')
            }).catch(() => {
                $q.notify({
                    type: 'negative',
                    message: 'Login Fehlgeschlagen',
                    caption: 'Falsches Passwort/Benutzername oder Account ist nicht verifiziert'
                })
            })
        }

        function showPassword() {
            let x = document.getElementById("password");
            x.type === "password" ? x.type = "text" : x.type = "password";
        }

        return {showPassword, username, password, login, isPwd, register, forgetpwd}
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

q-input {
    margin: 5px !important;
}
</style>
