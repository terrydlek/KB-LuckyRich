<!-- <template>
    <div>
        <h2>게시글 수정</h2>

        <div v-if="loading">로딩 중...</div>
        <div v-else-if="error">{{ error }}</div>

        <form v-else @submit.prevent="updatePost">
            <div>
                <label for="title">제목:</label>
                <input type="text" v-model="post.board.title" id="title" readonly />
            </div>
            <div>
                <label for="content">내용:</label>
                <textarea v-model="post.board.content" id="content" required></textarea> -->
<!-- SummernoteEditor 컴포넌트로 내용 입력 -->
<!-- <SummernoteEditor v-model="post.board.content" @update:modelValue="summernoteChange($event)"
                    @summernoteImageLinkInsert="summernoteImageLinkInsert" /> -->
<!-- </div>
            <button type="submit">수정 완료</button>
        </form>
    </div>
</template> -->

<!-- <script>
import axios from 'axios';
import SummernoteEditor from 'vue3-summernote-editor';

export default {
    components: { SummernoteEditor },
    data() {
        return {
            post: {
                board: {
                    title: '',
                    content: '',
                },
            },
            loading: true, // 데이터 로딩 상태
            error: null, // 에러 메시지
        };
    },
    mounted() {
        this.fetchPostDetails(); // 컴포넌트가 마운트될 때 게시글 데이터 불러오기
    },
    methods: {
        // 게시글 상세 정보 불러오기
        async fetchPostDetails() {
            const postId = this.$route.params.boardNum;
            const token = localStorage.getItem('access_token');
            try {
                console.log(postId);
                const response = await axios.get(`http://localhost:8080/board/${postId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                this.post = response.data;
                console.log(this.post);
                this.loading = false;
            } catch (error) {
                this.error = '게시글을 불러오는 중 오류가 발생했습니다.';
                console.error('Error fetching post details:', error);
                this.loading = false;
            }
        },
        // 게시글 수정 요청
        async updatePost() {
            const postId = this.$route.params.boardNum; // URL에서 게시글 ID 추출
            const token = localStorage.getItem('access_token'); // 토큰 불러오기
            console.log(this.post.board.title);
            console.log(this.post.board.content);
            try {
                const response = await axios.post(
                    'http://localhost:8080/board/updateBoard',
                    {
                        boardNum: postId,
                        title: this.post.board.title,
                        content: this.post.board.content,
                    },
                    {
                        headers: {
                            Authorization: `Bearer ${token}`, // 토큰 헤더에 포함
                        },
                    }
                );
                alert('게시글이 성공적으로 수정되었습니다.', response.data);
                this.$router.push(`/luckyrich/qa/ask/${postId}`); // 게시글 목록 페이지로 리다이렉트
            } catch (error) {
                console.error('게시글 수정 중 오류가 발생했습니다.', error);
            }
        },
        // Summernote 내용 변경 시 호출
        summernoteChange(newValue) {
            this.post.board.content = newValue; // 에디터 내용 업데이트
        },
        // 이미지 삽입 처리 (필요 시)
        summernoteImageLinkInsert(...args) {
            console.log('Image Inserted:', args);
        },

    },
};
</script> -->


<template>
    <div id="summernote"></div>
</template>

<script setup>
import { onMounted } from 'vue';


onMounted(() => {
    $('#summernote').summernote({
        placeholder: 'Hello stand alone ui',
        tabsize: 2,
        height: 400,
        width: 1500,
        toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'underline', 'clear']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['table', ['table']],
            ['insert', ['link', 'picture', 'video']],
            ['view', ['fullscreen', 'codeview', 'help']]
        ]
    });
})

</script>