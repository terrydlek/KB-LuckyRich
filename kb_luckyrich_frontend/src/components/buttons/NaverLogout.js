
export default {
    methods: {
        NaverLogout() {
            localStorage.removeItem('access_token'); // 로컬 스토리지에서 토큰 삭제
            window.location.href = '/'; // 홈 페이지로 리다이렉트
        } 
    }
}

