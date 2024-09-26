<template>
    <div>
        <h1>asdsd</h1>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            newsDetail : [],
        }
    },
    mounted() {
        this.code1 = this.$route.params.code1; // 첫 번째 코드 받아오기
        this.code2 = this.$route.params.code2; // 두 번째 코드 받아오기
        console.log("Code1:", this.code1);
        console.log("Code2:", this.code2);
    }
}
</script>

<style>

</style>