<template>
    <div>
        <q-card class="Card">
            <q-card-section>
                <div class="Title">
                    {{title}}
                </div>
                <div class="Subtitle">
                    {{subtitle}}
                </div>
            </q-card-section>

            <q-separator></q-separator>

            <q-card-section
                class="bg-secondary"
            >
                <div>
                    {{$t('database.course_name')}} :
                    <b>{{name}}</b>
                </div>
                <div>
                    {{$t('database.group_of_course')}} :
                    <b>{{group.name}}</b>
                </div>
            </q-card-section>

            <q-card-actions>
                <q-btn flat @click="edit=true">{{$t("manage.edit")}}</q-btn>
                <q-btn flat @click="delete_method" icon="delete" color="red">{{$t("manage.delete")}}</q-btn>
            </q-card-actions>
        </q-card>
    </div>

    <div>
        <q-dialog v-model="edit" persistent>
            <q-card>
                <q-card-section>
                    <div class="text-h5 q-mb-md">{{ $t('manage.update_course') }}</div>
                    <q-separator inset/>
                    <div>
                        {{$t('database.course_name')}} :
                        <q-input
                            outlined
                            v-model="new_name"
                        />
                    </div>
                    <q-separator spaced/>
                    <div >
                        {{$t('database.group_of_course')}} :

                        <q-select
                            outlined
                            v-model="new_group"
                            :options="grouplist"
                            option-label="name"
                            :label="$t('database.group')"
                        />
                    </div>
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn
                        flat
                        :label="$t('manage.cancel')"
                        color="primary"
                        v-close-popup
                    />
                    <q-btn
                        flat
                        :label="$t('manage.save')"
                        color="primary"
                        v-close-popup
                        @click="save_method(new_name, new_group)"
                    />
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>

</template>



<script>
import {ref} from "vue";

export default {
    name: "CourseOverviewCard",
    props: ['title', 'subtitle', 'name', 'group', 'grouplist',
        'save_method', 'delete_method'],
    setup(props) {
        const edit = ref(false)
        const new_name = ref("")
        const new_group = ref("")

        new_name.value = props.name
        new_group.value = props.group
        return {
            edit,
            new_name,
            new_group
        }
    }
}
</script>

<style scoped>

.Card {
    width: 80%;
}

.Title {
    font-size: large;
}

.Subtitle {
    font-size: medium;
}

</style>
