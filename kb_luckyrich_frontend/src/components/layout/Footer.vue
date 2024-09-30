<template>
    <footer class="footer">
        <div class="footer-content">
            <!-- <div class="footer-section about">
                <h2>Lucky Rich</h2>
                <p>
                    MZ 세대를 위한 스마트 자산 관리 플랫폼. 맞춤형 투자 상품 추천, 실시간
                    자산 정보 및 더 나은 재정 계획을 위한 도구들을 제공합니다.
                </p>
            </div> -->
            <div class="footer-section links">
                <ul>
                    &copy; 2024 Lucky Rich MZ 자산관리. All rights reserved.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="/">About Lucky Rich</a> &nbsp;&nbsp;&nbsp;  <a href="/luckyrich/privacy">Privacy Policy</a> &nbsp;&nbsp;&nbsp; <a href="/luckyrich/terms">Terms of Service</a>&nbsp;&nbsp;&nbsp; <a href="https://github.com/terrydlek/KB-LuckyRich">Lucky Rich's Git Url</a>
                </ul>
            </div>
        </div>
    </footer>
</template>

<style scoped>
.footer {
    background-color: #333;
    color: #fff;
    padding: 20px;
    text-align: center;
    position: fixed;
    bottom: 0;
    width: 100%;
    height: 50px;
    z-index: 1000;
}

.footer-content {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}

.footer-section {
    flex: 1;
    min-width: 200px;
}

.footer-section h2 {
    color: #f8b400;
    margin-bottom: 10px;
}

.footer-section p,
.footer-section ul {
    font-size: 14px;
}

.footer-section ul {
    list-style-type: none;
    padding: 0;
}

.footer-section ul li {
    margin: 5px 0;
}

.footer-section ul li a {
    color: #fff;
    text-decoration: none;
}

.footer-section ul li a:hover {
    text-decoration: underline;
}

.social-icons {
    display: flex;
    gap: 15px;
    justify-content: center;
}

.social-icons a img {
    width: 24px;
    height: 24px;
}

.footer-bottom {
    margin-top: 10px;
    border-top: 1px solid #444;
    padding-top: 10px;
}

.footer-bottom p {
    font-size: 12px;
    color: #aaa;
}
</style>