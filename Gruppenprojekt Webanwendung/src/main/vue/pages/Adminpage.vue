<template>
    <div class="headnav" style="text-align: left">
        <router-link :to="'/Dashboard'" class="navelem">{{$t('menu.dashboard')}}</router-link> <q-icon name="chevron_right" size="16px"/>
        <p class="navelem navactive">{{$t('admin.admin_settings')}}</p>
    </div>
    <h1>{{$t('admin.admin_settings')}}</h1>

  <div class="q-pa-md" ref="user_ref">
    <q-table
        :columns="user_columns"
        :rows="user_row"
        row-key="id"
        selection="single"
        v-model:selected="selected_user"
        :filter="userFilter"
        :filter-method="filterMethodUser"

        @selection="userSelection"
    >
      <template
          v-slot:top
      >
        <div
            class="text-bold"
            style="font-size: 1.5em"
        >
          {{ $t('User') }}
        </div>
        <q-space></q-space>
        <div>
          <q-input
              v-model="userFilter"
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

    <h2> Fakultät verwalten </h2>

    <div class="buttonclass">
        <q-btn class="buttonitem" :label="$t('admin.assign_facultyManager')" @click="addFacultyManager()"  unelevated rounded color="primary"/>
        <q-btn class="buttonitem" :label="$t('admin.delete_facultyManager')" @click="removeFacultyManager()" outline rounded color="black"/>
    </div>

    <h2> Nutzer verwalten </h2>

    <div class="buttonclass">
        <q-btn class="buttonitem" :label="$t('admin.assign_admin')" @click="addAdmin()"  unelevated rounded color="primary"/>
        <div class="buttonitem">
            <q-btn flat round color="black" icon="more_vert" />
            <q-menu touch-position>
                <q-list style="min-width: 100px">
                    <q-item clickable v-close-popup>
                        <q-item-section @click="deleteUser()">{{ $t('admin.delete_user') }}</q-item-section>
                    </q-item>
                </q-list>
            </q-menu>
        </div>
    </div>

  <h2> Fakultät erstellen </h2>
  <div class="Input_FacultyCreation">
    <q-input rounded v-model="faculty.name" placeholder="Fakultätsnamen eingeben"></q-input>
    <br>
  </div>

    <div class="buttonclass">
        <q-btn class="buttonitem" :label="$t('admin.add_faculty')" @click="addFaculty()"  unelevated rounded color="primary"/>
        <div class="buttonitem">
            <q-btn flat round color="black" icon="more_vert" />
            <q-menu touch-position>
                <q-list style="min-width: 100px">
                    <q-item clickable v-close-popup>
                        <q-item-section @click="deleteFaculty()">{{ $t('admin.delete_faculty') }}</q-item-section>
                    </q-item>
                </q-list>
            </q-menu>
        </div>
    </div>
</template>

<script>
import {ref} from "vue";
import {useUserStore} from "../stores/users";
import {useFacultyStore} from "../stores/faculties";
import {useQuasar} from "quasar";
import CreateNewEntityForm from "../components/CreateNewEntityForm.vue";
import {useI18n} from "vue-i18n";

const user_table_title = "User"
const user_columns = [
  {
    name: 'id',
    align: 'center',
    label: 'ID',
    field: row => row.id,
    sortable: true,
    sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
  },
  {
    name: 'email',
    align: 'left',
    label: 'Username',
      format: val => `${val}`,
    field: row => row.email,
    sortable: true
  }
]
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
    format: val => `${val}`,
    sortable: true
  }
]
let user_input_list = [
  {
    label: "User-ID",
    new_value: ref(""),
    readonly: true
  },
  {
    label: "Username",
    new_value: ref(""),
    readonly: false
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

  name: "Adminpage",
  components: {
    CreateNewEntityForm
  },
  methods: {
    filterMethodUser(rows, terms) {
      let lowerCaseTerm = terms.toLowerCase()
        return rows.filter(row => row.email.toLowerCase().startsWith(lowerCaseTerm))
    },
    filterMethodFaculty(rows, terms) {
      let lowerCaseTerm = terms.toLowerCase()
        return rows.filter(row => row.name.toLowerCase().startsWith(lowerCaseTerm))
    },
  },

  setup() {
    const $q = useQuasar()
    const {t} = useI18n({})

    const faculty = ref({
      name: "",
      score: ""
    })
    const facultyManager = ref({
          userID: "",
          facultyID: ""
        }
    )
    const user = ref({
      id: ""
    })
    const title_admin_settings = t("admin.admin_settings")

    //Import Users & Faculties
    const facultyStore = useFacultyStore()
    const userStore = useUserStore()
    let facultyFilter = ref('')
    let userFilter = ref('')
    const selected_faculty = ref([])
    const selected_user = ref([])

    const faculty_row = ref([])
    const user_row = ref([])
    const users = ref([])
    const faculties = ref([])

    loadUsers()
    loadFaculties()

    function loadUsers() {
      userStore.requestUsers().then(() => {
        users.value = userStore.getUsers()
        user_row.value = userStore.getUsers()
      })
    }

    function loadFaculties() {
      facultyStore.requestAllFaculties().then(() => {
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

    // gets called when user selects faculty in table
    function userSelection(details) {
      if (details.added) {
        user_input_list[0].new_value.value = details.rows[0].id
        user_input_list[1].new_value.value = details.rows[0].email
      } else {
        user_input_list[0].new_value.value = ""
        user_input_list[1].new_value.value = ""
      }
    }


    function addFacultyManager() {
      facultyManager.value.facultyID = faculty_input_list[0].new_value.value
      facultyManager.value.userID = user_input_list[0].new_value.value
      if (faculty_input_list[0].new_value.value === "" || user_input_list[0].new_value.value === "") {
        $q.notify({
          type: 'negative',
          message: t("admin.request_failed"),
          caption: t("admin.request_not_valid_user_faculty"),
        })
        return null;
      } else {
        userStore.requestAddFacultyManager(facultyManager).then(() => {
          $q.notify({
            type: 'positive',
            message: t("admin.request_successful"),
          })
        }).catch(() => {
          $q.notify({
            type: 'negative',
            message: t("admin.request_failed"),
            caption: t("admin.request_denied")
          })
        })
      }
    }

    function removeFacultyManager() {
      facultyManager.value.facultyID = faculty_input_list[0].new_value.value
      facultyManager.value.userID = user_input_list[0].new_value.value
      if (faculty_input_list[0].new_value.value === "" || user_input_list[0].new_value.value === "") {
        $q.notify({
          type: 'negative',
          message: t("admin.request_failed"),
          caption: t("admin.request_not_valid_user_faculty"),
        })
        return null;
      } else {
        userStore.requestDeleteFacultyManager(facultyManager).then(() => {
          $q.notify({
            type: 'positive',
            message: t("admin.request_successful"),
          })
        }).catch(() => {
          $q.notify({
            type: 'negative',
            message: t("admin.request_failed"),
            caption: t("admin.request_denied")
          })
        })
      }
    }


    function addFaculty() {
      if (faculty.value.name === "") {
        $q.notify({
          type: 'negative',
          message: t("admin.request_failed"),
          caption: t("admin.request_not_valid_faculty"),
        })
        return null;
      } else {
        facultyStore.requestSaveFaculty(faculty.value)
            .then(() => {
              $q.notify({
                type: 'positive',
                message: t("admin.request_successful"),
                caption: 'Erstellung der Fakultät erfolgreich.'
              })
            })
            .catch(() => {
              $q.notify({
                type: 'negative',
                message: t("admin.request_failed"),
                caption: t("admin.request_denied")
              })
            })
      }
    }

    function deleteFaculty() {
      if (faculty.value.name === "") {
        $q.notify({
          type: 'negative',
          message: t("admin.request_failed"),
          caption: t("admin.request_not_valid_faculty"),
        })
        return null;
      } else {
        facultyStore.requestDeleteFaculty(faculty.value)
            .then(() => {
              $q.notify({
                type: 'positive',
                message: t("admin.request_successful"),
              })
            })
            .catch(() => {
              $q.notify({
                type: 'negative',
                message: t("admin.request_failed"),
                caption: t("admin.request_denied")
              })
            })
      }
    }

    function deleteUser() {
      if (user_input_list[0].new_value.value === "") {
        $q.notify({
          type: 'negative',
          message: t("admin.request_failed"),
          caption: t("admin.request_not_valid_user"),
        })
        return null;
      } else {
        userStore.requestDeleteUser(user_input_list[0].new_value.value)
            .then(() => {
              $q.notify({
                type: 'positive',
                message: t("admin.request_successful"),
              })
            })
            .catch(() => {
              $q.notify({
                type: 'negative',
                message: t("admin.request_failed"),
                caption: t("admin.request_denied")
              })
            })
      }
    }

    function addAdmin() {
      if (user_input_list[0].new_value.value === "") {
        $q.notify({
          type: 'negative',
          message: t("admin.request_failed"),
          caption: t("admin.request_not_valid_user"),
        })
        return null;
      } else {
        userStore.requestAddAdminRole(user_input_list[0].new_value.value)
            .then(() => {
              $q.notify({
                type: 'positive',
                message: t("admin.request_successful"),
              })
            })
            .catch(() => {
              $q.notify({
                type: 'negative',
                message: t("admin.request_failed"),
                caption: t("admin.request_denied")
              })
            })
      }
    }

    return {
      title_admin_settings,
      user_table_title,
      faculties,
      users,
      user_row,
      user_columns,
      faculty_columns,
      userFilter,
      selected_user,
      facultyManager,
      faculty,
      faculty_row,
      facultyFilter,
      selected_faculty,
      user,
      userSelection,
      facultySelection,
      addFacultyManager,
      removeFacultyManager,
      addFaculty,
      deleteFaculty,
      deleteUser,
      addAdmin

    }

  }

}

</script>

<style scoped>

</style>
