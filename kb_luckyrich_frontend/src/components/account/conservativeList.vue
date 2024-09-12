<template>

</template>



<script>
import axios from 'axios';

export default {
    name: 'ConservativeList',
    data() {
        return {
            list: []
        }
    },
    mounted() {
        this.getList();
    },
    methods: {
        getList() {
            axios.get("http://localhost:8080/asset/conservativeList")
                .then(res => {
                    alert(res.data);
                    this.list = res.data;
                })
                .catch(err => {
                    alert(err);
                })
        }
    }
}

</script>
