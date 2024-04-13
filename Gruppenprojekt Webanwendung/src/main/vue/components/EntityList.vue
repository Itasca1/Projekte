<template>


    <q-table
        :columns="columns"
        :rows="rows"
        :row-key="row_key"
        :selection="selection"
        :filter="filter"
        :filter-method="filterMethod"
        v-model:selected="selected_v_model"


        @selection="selection_method"
    >
        <template
            v-slot:top
        >
            <div
                class="Title"
            >
                {{title}}
            </div>
            <q-space/>
            <div>
                <q-input

                    v-model="filter"
                    :placeholder="placeholder"
                    debounce="300"
                    dense
                    outlined
                >
                    <template v-slot:prepend>
                        <q-icon name="search"/>
                    </template>
                </q-input>
            </div>
        </template>

        <template #no-data>
            <q-icon name="warning" size="2em"/>
            {{no_data_message}}
        </template>
    </q-table>
</template>
<script>
import {ref} from "vue";
import {useI18n} from "vue-i18n";

export default {
    name: "EntityList",
    props: ['title', 'columns', 'rows', 'row_key', 'selection',
        'selected_v_model', 'filter_method', 'selection_method',
        'filter_placeholder', 'no_data_message'],
    methods: {
        filterMethod(rows, terms) {
            let lowerCaseTerm = terms.toLowerCase()
            return rows.filter(row => row.name.toLowerCase().startsWith(lowerCaseTerm))
        },
    },

    setup(props) {
        let filter = ref('')
        const {t} = useI18n()
        const placeholder = t('manage.search')

        function doIt(selected) {
            props.selected_v_model = selected
        }
        return {
            placeholder,
            filter,
            props,
            doIt
        }
    }
}
</script>

<style scoped>
.Title {
    font-weight: bold;
    font-size: medium;
}
</style>
