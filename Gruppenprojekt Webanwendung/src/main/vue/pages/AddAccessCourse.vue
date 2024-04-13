<template>
  <h1>{{ $t('add_group_course.add_course') }}</h1>
  <div class="q-pa-md" ref="faculty_ref">
    <q-table
        :columns="group_columns"
        :rows="group_row"
        row-key="id"
        selection="single"
        v-model:selected="selected_Group"
        :filter="groupFilter"
        :filter-method="filterMethodGroups"

        @selection="groupSelection"
    >
      <template
          v-slot:top
      >
        <div
            class="text-bold"
            style="font-size: 1.5em"
        >
          {{ $t('database.group') }}
        </div>
        <q-space></q-space>
        <div>
          <q-input
              v-model="groupFilter"
              placeholder="Search"
              debounce="300"
              dense
              outlined
          >
            <template v-slot:append>
              <q-icon name="search"></q-icon>
            </template>
          </q-input>
        </div>
      </template>
    </q-table>
  </div>

    <div>
        <q-input rounded v-model="course.name" placeholder="Access Kursnname"></q-input>
        <q-select filled v-model="course.tempWeight" :options="options" label="Gewichtung"/>
    </div>

    <br>

    <div class="buttonclass">
        <q-btn class="buttonitem" :label="$t('manage.confirm')" @click="accept()" unelevated rounded color="primary"/>
    </div>

</template>
<script>

import {useQuasar} from "quasar";
import {ref} from "vue";
import {useGroupStore} from "../stores/groups";
import {useCourseStore} from "../stores/courses";
import {useRouter} from "vue-router"
import {useI18n} from "vue-i18n";

const group_columns = [
    {
        name: 'id',
        align: 'center',
        label: 'ID',
        field: row => row.id,
        sortable: true,
        sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
    },
    {
        name: 'Name',
        align: 'left',
        label: 'Name',
        field: row => row.name,
        sortable: true,
        sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
    }
]
let group_input_list = [
    {
        new_value: ref(""),
        readonly: true
    },
    {
        new_value: ref(""),
        readonly: false
    }
]
export default {
  name: "AddAccessCourse",
  components: {},
  methods: {
    filterMethodGroups(rows, terms) {
      let lowerCaseTerm = terms.toLowerCase()
        return rows.filter(row => row.name.toLowerCase().startsWith(lowerCaseTerm))
    }
  },
  setup() {
    const $q = useQuasar()
    const {t} = useI18n({})

        const course = ref({
            groupID: "",
            name: "",
            weighted: false,
            tempWeight:"",
        })

        const groupStore = useGroupStore()
        const courseStore = useCourseStore()
        const router = useRouter()
        let groupFilter = ref('')
        const selected_Group = ref([])
        const group_row = ref([])

        const groupies = ref([])

        loadGroups()

    function loadGroups() {
      groupStore.requestGroups().then(() => {
        groupies.value = groupStore.getGroups()
        group_row.value = groupStore.getGroups()
      })
    }

    // gets called when user selects faculty in table
    function groupSelection(details) {
      if (details.added) {
        group_input_list[0].new_value.value = details.rows[0].id
        group_input_list[1].new_value.value = details.rows[0].name
      } else {
        group_input_list[0].new_value.value = ""
        group_input_list[1].new_value.value = ""
      }
    }

        function accept() {
            course.value.groupID = group_input_list[0].new_value.value
            course.value.visible =false
            if (course.value.tempWeight.toString() === t("course_page.weight_weak")) {
                course.value.weighted = false
            }
            if (course.value.tempWeight.toString() === t("course_page.weight_strong")) {
                course.value.weighted = true
            }
            if (course.value.name === "" || course.value.groupID === "") {
                $q.notify({
                    type: 'negative',
                    message: t("add_group_course.request_failed_course"),
                    caption: t("add_group_course.request_not_valid_course")
                })
                return null;
            } else {
                courseStore.requestSaveCourse(course)
                    .then(res => {
                        $q.notify({
                            type: 'positive',
                            message: t("add_group_course.request_successful_course"),
                        })
                        router.push("/course/" + res.id)
                    })
                    .catch(() => {
                        $q.notify({
                            type: 'negative',
                            message: t("add_group_course.request_failed_course"),
                            caption: t("add_group_course.request_denied")
                        })
                    })
            }
        }

        return {
            model: ref(null),
            weigth: ref(null),
            options:[t("course_page.weight_weak"), t("course_page.weight_strong")]
            ,
            course,
            groupies,
            group_columns,
            group_row,
            groupFilter,
            selected_Group,
            groupSelection,
            accept
        }
    }
}

</script>
<style scoped>
</style>
