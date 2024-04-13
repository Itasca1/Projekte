import axios from 'axios';

export default {
    send(email) {
        let emailCmd = {subject: email.subject, message: email.message, sentTo: email.sentTo};
        return axios.post('/api/sendEmail', emailCmd);
    }
}
