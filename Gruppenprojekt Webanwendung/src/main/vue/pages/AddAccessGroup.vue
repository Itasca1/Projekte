<template>
  <h1>{{ $t('add_group_course.add_group') }}</h1>
  <div class="q-pa-md" ref="faculty_ref">
    <q-table
        :columns="faculty_columns"
        :rows="faculty_row"
        row-key="id"
        selection="single"
        v-model:selected="selected_faculty"
        :filter="facultyFilter"
        :filter-method="filterMethodFaculty"

        @selection="facultySelection"
    >
      <template
          v-slot:top
      >
        <div
            class="text-bold"
            style="font-size: 1.5em"
        >
          {{ $t('database.faculty') }}
        </div>
        <q-space></q-space>
        <div>
          <q-input
              v-model="facultyFilter"
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
  <q-input rounded v-model="group.name" placeholder="Access Gruppennamen eingeben"></q-input>
  <br>

    <div class="buttonclass">
        <q-btn class="buttonitem" :label="$t('manage.confirm')" @click="accept()" unelevated rounded color="primary"/>
    </div>
</template>
<script>

import {useQuasar} from "quasar";
import {ref} from "vue";
import {useFacultyStore} from "../stores/faculties";
import {useGroupStore} from "../stores/groups";
import {useI18n} from "vue-i18n";

const faculty_columns = [
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
let faculty_input_list = [
  {
    label: "Fakultäts-ID",
    new_value: ref(""),
    readonly: true
  },
  {
    label: "Fackultätsname",
    new_value: ref(""),
    readonly: false
  }
]
export default {
  name: "AddAccessGroup",
  components: {},
  methods: {
    filterMethodFaculty(rows, terms) {
      let lowerCaseTerm = terms.toLowerCase()
        return rows.filter(row => row.name.toLowerCase().startsWith(lowerCaseTerm))
    }
  },
  setup() {
    const $q = useQuasar()
    const {t} = useI18n({})

    const group = ref({
      facultyID: "",
      name: "",
    })

    const facultyStore = useFacultyStore()
    const groupStore = useGroupStore()
    let facultyFilter = ref('')
    const selected_faculty = ref([])
    const faculty_row = ref([])

    const faculties = ref([])

    loadFaculties()

    function loadFaculties() {
      facultyStore.requestFaculties().then(() => {
        faculties.value = facultyStore.getFaculties()
        faculty_row.value = facultyStore.getFaculties()
      })
    }

    // gets called when user selects faculty in table
    function facultySelection(details) {
      if (details.added) {
        faculty_input_list[0].new_value.value = details.rows[0].id
        faculty_input_list[1].new_value.value = details.rows[0].name
      } else {
        faculty_input_list[0].new_value.value = ""
        faculty_input_list[1].new_value.value = ""
      }
    }

    function accept() {
      group.value.facultyID = faculty_input_list[0].new_value.value
      if (group.value.name === "" || group.value.facultyID === "") {
        $q.notify({
          type: 'negative',
          message: t("add_group_course.request_failed"),
          caption: t("add_group_course.request_not_valid_group")
        })
        return null;
      } else {
        groupStore.requestSaveGroup(group)
            .then(() => {
              $q.notify({
                type: 'positive',
                message: t("add_group_course.request_successful_group"),
              })
            })
            .catch(() => {
              $q.notify({
                type: 'negative',
                message: t("add_group_course.request_failed"),
                caption: t("add_group_course.request_denied")
              })
            })
      }
    }

    return {
      semester: ref(null),
      group,
      faculties,
      faculty_columns,
      faculty_row,
      facultyFilter,
      selected_faculty,
      facultySelection,
      accept
    }
  }
}

</script>
<style scoped>
</style>
