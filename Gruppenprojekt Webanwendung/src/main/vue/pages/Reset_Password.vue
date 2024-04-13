<template>
    <img src="../assets/logo.png" alt="Access Logo">
    <div class="content">

        <h1> Passwort zurücksetzen </h1>

        <div class="user_input">
            <input type="email" id="mail" name="mail" placeholder="E-Mail Adresse"><br>
            <input type="password" id="password" name="password" placeholder="neues Passwort"><br>
            <input type="password" id="password_replica" name="password_replica" placeholder="Passwort bestätigen"><br>
            <input v-on:change="showPassword" type="checkbox" id="show_password" name="show_password">
            <label for="show_password"> Show Password</label>
        </div>
        <div class="links">
            <Button>Bestätigen</Button>
        </div>
    </div>
</template>

<script>
export default {
    name: "Login",
    setup() {
        function showPassword() {
            const passwordField = document.getElementById("password");
            passwordField.type === "password" ? passwordField.type = "text" : passwordField.type = "password";
            const passwordReplicaField = document.getElementById("password_replica");
            passwordReplicaField.type === "password" ? passwordReplicaField.type = "text" : passwordReplicaField.type = "password";
        }

        return {showPassword}
    }
}
</script>

<style scoped>

.content {
    padding-left: 40%;
    padding-right: 40%;
    alignment: left;
}


input {
    width: 100%;
    padding-bottom: 10px;
    padding-top: 10px;
    margin-bottom: 10px;
    border-radius: 10px;
    font-size: 100%;
}


button:hover {
    background-color: #9e9e9e; /* Green */
    color: white;
}

button:active {
    background-color: #6f6f6f; /* Green */
    color: white;
}


</style>
