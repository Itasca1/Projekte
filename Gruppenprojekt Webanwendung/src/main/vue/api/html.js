import axios from 'axios';

export default {

    get(id) {
        return axios.get('/api/html/' + id);
    },
    getLinksFromHTML(id) {
        return axios.get('/api/htmlcrawler/getlinks/' + id)
    },
    getImagesFromHTML(id) {
        return axios.get('/api/htmlcrawler/getimagesrcandalt/' + id)
    },
    getVideosFromHTML(id) {
        return axios.get('/api/htmlcrawler/getvideos/' + id)
    },
    getWithMarkedLists(id) {
        return axios.get('/api/htmlcrawler/marklists/' + id)
    },
    getWithMarkedFormatting(id) {
        return axios.get('/api/htmlcrawler/markformatting/' + id)
    },
    getWithMarkedLinks(id) {
        return axios.get('/api/htmlcrawler/marklinks/' + id)
    },
    update(html) {
        let htmlCmd = {htmlString: html.htmlString, review: html.review, name: html.name};
        return axios.put('/api/html/' + html.id, htmlCmd);
    },
    save(html) {
        let htmlCmd = {htmlString: html.htmlString, review: html.review, name: html.name};
        return axios.post('/api/html', htmlCmd);
    }
}
