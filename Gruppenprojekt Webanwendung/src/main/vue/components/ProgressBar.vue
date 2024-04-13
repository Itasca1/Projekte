<template>
    <div class="q-pa-md">
        <h2 style="text-align:center;"><u>{{$t("progressBar.Your_personal_progress")}}</u></h2>
        <div class="q-gutter-ymd column">
            <q-toolbar class="bg-white">
                <!-- progress bar -->
                <q-linear-progress size="20px" :value="ownAvgScore/100" rounded color="positive" track-color="negative" class="q-mt-sm" />
            </q-toolbar>

            <q-toolbar class="bg-white">
                <!-- explanation for progress bar color 1 -->
                <q-badge rounded color="positive" />
                &nbsp {{$t("progressBar.barrier_free")}}
                <q-space />
                <!-- display of average over all the user's groups -->
                <strong> {{$t("progressBar.own_avg")}} &nbsp </strong>
                <p style="width: 120px; font-size: 16px !important;" v-if="ownAvgScore === null" class="scoreRect scoreNegative">{{ $t("progressBar.NA") }}</p>
                <p style="width: 80px" v-else-if="ownAvgScore >= 50" class="scoreRect">{{ ownAvgScore }}%</p>
                <p style="width: 80px" v-else class="scoreRect scoreNegative">{{ ownAvgScore }}%</p>
                <q-icon name="info" style="color: #3F50B5; padding-top: 4vh;" size="sm">
                    <q-tooltip>
                        {{ $t("progressBar.infoOwn") }}
                    </q-tooltip>
                </q-icon>
            </q-toolbar>

            <q-toolbar class="bg-white">
                <!-- explanation for progress bar color 2 -->
                <q-badge rounded color="negative" />
                &nbsp {{$t("progressBar.not_yet_barrier_free")}}
                <q-space />
                <!-- display of average over all groups in db -->
                {{$t("progressBar.total_avg")}} &nbsp
                <p style="width: 120px; font-size: 16px !important;" v-if="totalAvgScore === null" class="scoreRect scoreNegative">{{ $t("progressBar.NA") }}</p>
                <p style="width: 80px" v-if="totalAvgScore >= 50" class="scoreRect">{{ totalAvgScore }}%</p>
                <p style="width: 80px" v-else class="scoreRect scoreNegative">{{ totalAvgScore }}%</p>
                <q-icon name="info" style="color: #3F50B5; padding-top: 4vh;" size="sm">
                    <q-tooltip>
                        {{ $t("progressBar.infoAvg") }}
                    </q-tooltip>
                </q-icon>
            </q-toolbar>

        </div>

    </div>
</template>

<script>
import { ref } from 'vue'
import {useGroupStore} from "../stores/groups";
import {useCourseStore} from "../stores/courses";

export default {
    setup () {
        let ownAvgScore = ref(null);
        let totalAvgScore = ref(null);
        const courseStore = useCourseStore();
        const allGroupStore = useGroupStore();      //  Another groupstore for requests

        function loadData() {

            // request average score over all the user's course stores
            courseStore.requestAvgMyCourses().then(() => {
                ownAvgScore.value = courseStore.getAvgMyCoursesInStore();
            })
            // request average score over all groups in data base
            allGroupStore.requestAvgAllGroups().then(() => {
                totalAvgScore.value = allGroupStore.getAvgAllGroupsInStore();
            })
        }

        loadData()

        return{ownAvgScore, totalAvgScore}
    }
}
</script>

<style scoped>


</style>
