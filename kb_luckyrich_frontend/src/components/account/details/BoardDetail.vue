<template>
    <div v-if="post" class="container mt-5">
        <div class="card">
            <div class="card-header">
                <h2>{{ post.board.title }}</h2>
            </div>
            <div class="card-body">
                <h5 class="card-title">작성자: {{ post.board.nickName }}</h5>
                <p class="card-text">{{ post.board.content }}</p>
            </div>
            <div class="card-footer text-muted">
                작성일: {{ new Date(post.board.createdAt).toLocaleString() }}
            </div>
        </div>
    </div>
    <p v-else>게시글을 불러오는 중입니다...</p>
    <center>
        <div class="mb-3">
            <button id="bt2" v-if="post && post.userName === post.board.userName" class="btn btn-primary"
                @click="edit">수정</button>
            <button id="bt3" v-if="post && post.userName === post.board.userName" class="btn btn-primary"
                @click="deletePost">삭제</button>
            <button id="bt1" class="btn btn-primary" @click="goBack">목록으로</button>
        </div>
    </center>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            post: null,
        };
    },
    mounted() {
        const boardNum = this.$route.params.boardNum;
        const token = localStorage.getItem('access_token');
        axios.get(`http://localhost:8080/board/${boardNum}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        })
            .then((resp) => {
                this.post = resp.data;
                console.log(this.post)
            })
            .catch((err) => {
                console.error('There was an error fetching the deposit details:', err);
            })
    },
    methods: {
        goBack() {
            this.$router.push('/luckyrich/qa/ask');
        },
        deletePost() {
            const token = localStorage.getItem('access_token');
            const boardNum = this.$route.params.boardNum;
            axios.delete(`http://localhost:8080/board/deleteBoard?boardNum=${boardNum}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    }
                })
                .then((resp) => {
                    if (resp.data === 'ok') {
                        alert('게시글이 성공적으로 삭제되었습니다.');
                        this.$router.push('/luckyrich/qa/ask');
                    } else {
                        alert('게시글 삭제에 실패했습니다.');
                    }
                })
                .catch((err) => {
                    console.error('게시글 삭제 중 오류가 발생했습니다:', err);
                });
        },
        editPost() {
            const newTitle = prompt('새 제목을 입력하세요', this.post.board.title);
            const newContent = prompt('새 내용을 입력하세요', this.post.board.content);
            if (newTitle && newContent) {
                const token = localStorage.getItem('access_token');
                axios.post('http://localhost:8080/board/updateBoard',
                    {
                        boardNum: this.$route.params.boardNum,
                        title: newTitle,
                        content: newContent,
                    },
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    })
                    .then((resp) => {
                        if (resp.data === 'success') {
                            alert('게시글이 성공적으로 수정되었습니다.');
                            this.post.board.title = newTitle;
                            this.post.board.content = newContent;
                        } else {
                            alert('게시글 수정에 실패했습니다.');
                        }
                    })
                    .catch((err) => {
                        console.error('게시글 수정 중 오류가 발생했습니다:', err);
                    });
            }
        },
        edit() {
            const postId = this.post.board.boardNum;
            this.$router.push(`/luckyrich/qa/ask/edit/${postId}`);
        }
    },
};

</script>

<style>
#bt1,
#bt2,
#bt3 {
    margin-top: 20px;
}

#bt2,
#bt3 {
    margin-right: 20px;
}
</style>