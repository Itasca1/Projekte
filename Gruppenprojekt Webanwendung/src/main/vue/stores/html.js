import {defineStore} from 'pinia'
import {ref} from 'vue'
import api from "../api";

export const useHTMLStore = defineStore('htmlObjects', () => {
    const htmlObjects = ref([])

    function updateHTML(id, newHTML) {
        const html = getHTML(id)
        if (html) {
            html.htmlString = newHTML.htmlString;
            html.review = newHTML.review;
            html.name = newHTML.name;
        } else {
            htmlObjects.value.push(newHTML)
        }
    }

    function requestSaveHTML(html) {
        return new Promise((resolve, reject) => {
            api.html.save(html).then(res => {
                html = res.data
                updateHTML(html.id, html)
                resolve(html)
            }).catch(() => {
                reject()
            })
        })
    }

    function requestUpdateHTML(html) {
        return new Promise((resolve, reject) => {
            api.html.update(html).then(res => {
                html = res.data
                updateHTML(html.id, html)
                resolve(html)
            }).catch(() => {
                reject()
            })
        })
    }

    function getHTMLpage(id) {
        return api.html.get(id);
    }

    function getHTML(id) {
        // noinspection EqualityComparisonWithCoercionJS
        return htmlObjects.value.find(html => html.id == id)
    }

    function getImagesFromHTML(id) {
        return api.html.getImagesFromHTML(id)
    }

    function getVideosFromHTML(id) {
        return api.html.getVideosFromHTML(id)
    }

    function getLinksFromHTML(id) {
        return api.html.getLinksFromHTML(id)
    }

    function getHTMLWithMarkedLists(id) {
        return api.html.getWithMarkedLists(id)
    }

    function getHTMLWithMarkedFormatting(id) {
        return api.html.getWithMarkedFormatting(id)
    }

    function getHTMLWithMarkedLinks(id) {
        return api.html.getWithMarkedLinks(id)
    }

    function requestHTML(id) {
        return new Promise((resolve, reject) => {
            api.html.get(id).then(res => {
                updateHTML(id, res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function deleteHTML(id) {
        return api.html.delete(id)
    }

    return {
        htmlObjects,
        getHTMLpage,
        requestSaveHTML,
        getHTML,
        getImagesFromHTML,
        getVideosFromHTML,
        getLinksFromHTML,
        getHTMLWithMarkedLists,
        getHTMLWithMarkedFormatting,
        getHTMLWithMarkedLinks,
        requestUpdateHTML,
        requestHTML,
        deleteHTML
    }
})
