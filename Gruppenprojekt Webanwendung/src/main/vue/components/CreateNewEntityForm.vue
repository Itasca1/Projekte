<template>
    <div class="q-pa-md">
        <q-dialog v-model="visible">
            <q-card>
                <q-card-section>
                    <div class="text-h5 q-mb-md">Gruppe hinzufügen</div>
                    <q-separator inset/>
                    <div v-for="attr in attributes">
                        <div v-if="attr.options != null">

                        </div>
                        <q-input
                            filled
                            v-model="attr.new_value"
                            :label="attr.name"
                            :hint="attr.hint"
                            :lazy-rules="false"
                            :rules="[ val => val && val.length > 0 || 'enter something']"
                        />
                    </div>
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn flat label="Abbrechen" color="primary" v-close-popup @click="cancel_event_function"/>
                    <q-btn flat label="Speichern" color="primary" v-close-popup @click="confirm_event_function"/>
                </q-card-actions>
            </q-card>
        </q-dialog >
    </div>
</template>

<script>
export default {
    name: "CreateNewEntityForm",
    props: ['visible','attributes', 'confirm_event_function', 'cancel_event_function'],
    setup() {
    }
}
</script>

<style scoped>

</style>
