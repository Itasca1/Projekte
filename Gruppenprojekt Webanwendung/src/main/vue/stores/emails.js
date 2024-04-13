import {defineStore} from 'pinia'
import {ref} from 'vue'
import axios from "axios";
import api from "../api";

export const useEmailStore = defineStore('emails', () => {
    const emails = ref([])

    function sendEmail(email) {
        /*return new Promise((resolve, reject) => {
            api.email.send(email).then(res => {*/
    }
})
