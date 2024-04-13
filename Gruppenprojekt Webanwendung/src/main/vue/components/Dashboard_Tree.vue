<template>
    <div>
        <div
            :style="{'margin-left': `${depth * 40}px`}"
            class="node"
            @mouseover="hover = true"
            @mouseleave="hover = false"
        >
            <!-- show different symbols depending on whether nodqe is expanded-->
            <span  @click="expanded= !expanded" v-if="hasChildren" class="type"> {{ expanded ? '&#9660;' : '&#9658;' }}</span>
            <span v-else> &#9670; </span>

            <!-- displays the name of the nodes -->
            <pre @click="goTofaculty(node.facultyId)" class=nodeStyle v-if="depth===0">
                {{ node.label }}
            </pre>
            <pre @click="goToGroup(node.groupId)" class=nodeStyle v-if="depth===1">
                {{ node.label }}
            </pre>
            <pre @click="goToCourse(node.courseId)" class=nodeStyle v-if="depth===2">
                {{ node.label }}</pre>
            <!-- displays the score of the node -->
            <q-btn  v-if="node.score <50 && node.score>10 " class="ScoreButton"  rounded color="negative" size="m"> {{ node.score }} %</q-btn>
            <q-btn  v-if="node.score <10 " class="ScoreButton"  rounded color="negative" size="m">	&nbsp; {{ node.score }} %</q-btn>
            <q-btn v-if="node.score >= 50 " class="ScoreButton"   rounded color="positive" size="m"> {{ node.score }} %</q-btn>

            <br> <br>
            <hr>
        </div>
        <Tree
            v-if="expanded"
            v-for="child in node.children"
            :key="child.name"
            :node="child"
            :depth="depth + 1"
        />
    </div>

</template>

<script>
export default {
    name: "Tree",

    props: {
        node: Object
        ,
        depth: {
            type: Number,
            default: 0,
        }
    },

    data() {
        return {
            expanded: false, hover: false,
        }
    },
    computed: {
        // returns boolean for whether node in tree has children
        hasChildren() {
            return this.node.children;
        }
    }
    , methods: {
        goToCourse(courseId) {
            if (courseId != null) {
                const id = courseId.toString()
                const path = "/Course/" + id
                this.$router.push(path);
            }
        },
        goToGroup(groupId) {
            if (groupId != null) {
                const id = groupId.toString()
                const path = "/Group/" + id
                this.$router.push(path);
            }
        },
        goTofaculty(facultyId) {
            if (facultyId != null) {
                const id = facultyId.toString()
                const path = "/Faculty/" + id
                this.$router.push(path);
            }
        }
    }
}
</script>
<style scoped>
.node {
    text-align: left;
    font-size: 14px;
    cursor: pointer;
}

.type {
    margin-right: 10px;
    font-size: 23px;
}

.ScoreButton {
    color: white;
    font-weight: bold;
    background-color: darkmagenta;
    float: right;

}
.nodeStyle:hover {
    text-shadow: 0.03125em 0.03125em #c5bebe;
    color:#3F50B5 ;
    text-decoration: underline;

}
</style>
