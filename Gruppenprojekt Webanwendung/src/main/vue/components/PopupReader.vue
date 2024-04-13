<template>
    <div>
        <div style="padding: 15px 0 0 3px !important;" class="q-pa-md q-gutter-sm">
            <div class="buttonclass">
                <q-btn class="buttonitem" style="margin: 0 !important;" :label="label" @click="dialog=true" outline rounded color="black"/>
            </div>
            <q-dialog
                v-model="dialog"
                persistent
                :maximized="maximizedToggle"
                transition-show="slide-up"
                transition-hide="slide-down"
            >
                <q-card class="bg-primary text-white">
                    <q-bar>
                        {{ htmlTitle }}
                        <q-space />
                        <q-btn dense flat icon="close" v-close-popup>
                            <q-tooltip class="bg-white text-primary">Close</q-tooltip>
                        </q-btn>
                    </q-bar>
                    <iframe v-if="htmlObj" id="contextframe" @load="iframeclick" :srcdoc="htmlObj"></iframe>
                </q-card>
            </q-dialog>
        </div>
    </div>
</template>
<script>
import {useHTMLStore} from "../stores/html";
import {ref} from "vue";
export default {
    name: "PopupReader",
    props: {
        label: String,
        htmlID: Number
    },
    data() {
        const iframeclick = function() {
            document.getElementById("contextframe").contentWindow.document.body.onclick = function(event) {
                event.preventDefault()
            }
        }
        return {
            htmlObj : ref(null),
            htmlTitle : ref(null),
            iframeclick
        }
    },
    mounted() {
        const htmlStore = useHTMLStore();
        // noinspection JSUnresolvedFunction
        htmlStore.getHTMLpage(this.htmlID).then(html => { this.htmlObj = html.data.htmlString; this.htmlTitle = html.data.name});
    }
}
</script>
<script setup>
const dialog = ref(false);
const maximizedToggle = ref(true);
</script>
<style scoped>
iframe {
    width: 100%;
    height: calc(100% - 40px);
    border: none;
}
</style>
