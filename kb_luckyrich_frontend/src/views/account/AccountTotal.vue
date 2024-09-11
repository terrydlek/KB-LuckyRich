<template>
    <div class="account-total">
        <!-- 
        <button @click="modalOpen">
            자산 추가
        </button>
        <div class="modal-wrap" v-show="modalCheck">
            <div class="modal-container">
                <AccountUpdate @close="modalOpen" />
            </div>
        </div> 
        -->
        <button @click="navigate">자산 등록</button>
    </div>
</template>

<script setup>
import AccountUpdate from './AccountUpdate.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const navigate = () => {
    router.push({ name: 'accountUpdate' });
};

// const modalCheck = ref(false);
// function modalOpen() {
//     modalCheck.value = !modalCheck.value
// }

</script>

<style scoped>
.modal-wrap {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    /* display: flex; */
    justify-content: center;
    align-items: center;
}

.modal-container {
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 550px;
    background: #fff;
    border-radius: 10px;
    padding: 20px;
    box-sizing: border-box;
}
</style>