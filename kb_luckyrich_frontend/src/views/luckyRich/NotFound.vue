<template>
    <center>
        <section class="page_404">
            <div class="container">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="col-sm-10 col-sm-offset-1 text-center">
                            <div class="four_zero_four_bg">
                                <h1 class="text-center">404</h1>
                            </div>
                            <div>
                                <h3 class="h2">길을 잃었군요</h3>
                                <p>찾으시는 페이지가 존재하지 않습니다.</p>
                                <a href="/luckyrich" class="link_404">홈으로...</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </center>
</template>

<style scoped>
/* Google Fonts 추가 */
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans:wght@400;700&display=swap');

/*======================
    404 page
=======================*/
.page_404 {
    padding: 40px 0;
    background: #fff;
    font-family: 'Noto Sans', sans-serif;
    /* 폰트 적용 */
}

.page_404 img {
    width: 100%;
}

.four_zero_four_bg {
    background-image: url(https://cdn.dribbble.com/users/285475/screenshots/2083086/dribbble_1.gif);
    height: 400px;
    background-position: center;
}

.four_zero_four_bg h1 {
    font-size: 80px;
}

.four_zero_four_bg h3 {
    font-size: 80px;
}

.link_404 {
    color: #fff !important;
    padding: 10px 20px;
    background: #3498db;
    margin: 20px 0;
    display: inline-block;
    border-radius: 30px;
}

.contant_box_404 {
    margin-top: -50px;
}

a {
    color: #6c63ff;
    text-decoration: none;
}

a:hover {
    text-decoration: underline;
}
</style>
