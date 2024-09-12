<template>
    <div class="service-component">
        <div class="header">
            <span>{{ serviceName }}</span>
            <button @click="navigate">→</button>
        </div>
        <div class="content">
            <slot></slot>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const props = defineProps({
    serviceName: {
        type: String,
        required: true
    },
    route: {
        type: String,
        required: true
    }
});

const router = useRouter();

const navigate = () => {
    router.push({ name: props.route });
};

</script>

<style scoped>
.service-component {
    border: 1px solid #ccc;
    padding: 16px;
    border-radius: 8px;
    margin: 10px;
    width: 300px;
    /* min-width: 200px; */
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
}

.header span {
    font-size: 18px;
    font-weight: bold;
}

.header button {
    background: none;
    border: none;
    font-size: 18px;
    cursor: pointer;
}

.content {
    margin: auto;
    padding: 10px;
}
</style>