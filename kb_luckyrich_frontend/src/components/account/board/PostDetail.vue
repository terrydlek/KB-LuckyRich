<template>
  <div class="post-detail">
    <h2>{{ post.title }}</h2>
    <p>작성자: {{ post.author }} | 작성일: {{ formatDate(post.created_at) }}</p>
    <div class="post-content">
      {{ post.content }}
    </div>

    <h3>답변</h3>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const post = ref({});
const answer = ref('');
const isAdmin = ref(false);

const formatDate = (dateString) => {
  const options = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  };
  return new Date(dateString).toLocaleDateString(undefined, options);
};

/*
const submitAnswer = async() => {
    if (isAdmin.value && answer.value.trim()) {
        try {
            const response = await axios.post(`http://localhost:8080/board/${post.value.id}/answer`,)
        }
    }
*/

const fetchPostAndAnswer = async (id) => {
  try {
    // 게시글 정보 가져오기
    const postResponse = await axios.get(`http://localhost:8080/board/${id}`);
    post.value = postResponse.data;
  } catch (error) {
    console.log('질문글과 답변을 불러오는 중 에러 발생', error);
  }
};

/*
const checkAdminStatus = async () => {
    try {
        // UserDto 통해 관리자 여부 확인
        const response = await axios.get('http')
    }
}
*/

onMounted(() => {
  const postId = route.params.id;
  fetchPostAndAnswer(postId);
  // checkAdminStatus();
});
</script>
<style lang=""></style>
