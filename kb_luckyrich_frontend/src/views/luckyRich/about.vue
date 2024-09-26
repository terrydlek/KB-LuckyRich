<template>
    <div class="lucky-rich-page">
        <section class="hero-section">
            <div class="hero-overlay">
                <h1 class="hero-title">Lucky Rich에 오신 것을 환영합니다.</h1>
                <p class="hero-subtitle">자산 관리의 자유를 위한 모든 것</p>
            </div>
        </section>

        <section class="about-section">
            <div class="about-content">
                <h2>Lucky Rich란?</h2>
                <p>
                    Lucky Rich는 재정적 자유를 추구하는 모든 사람들을 위한 맞춤형 자산 관리 플랫폼입니다.
                    우리는 최신 투자 트렌드와 개별 맞춤형 전략을 통해 여러분의 자산을 안전하게 보호하고 성장시킵니다.
                </p>
            </div>
            <div class="about-stats">
                <div class="stat">
                    <h3>5M+</h3>
                    <p>Active Users</p>
                </div>
                <div class="stat">
                    <h3>$10B+</h3>
                    <p>Assets Managed</p>
                </div>
                <div class="stat">
                    <h3>150+</h3>
                    <p>Investment Options</p>
                </div>
            </div>
        </section>

        <section class="benefits-section">
            <h2>왜 Lucky Rich인가?</h2>
            <div class="benefits-grid">
                <div class="benefit">
                    <img src="@/assets/images/security.png" alt="Security" />
                    <h3>Unmatched Security</h3>
                    <p>
                        최고 수준의 보안 시스템을 통해 자산을 안전하게 관리하며, 해킹 및 위험 요소로부터 안전하게 보호합니다.
                    </p>
                </div>
                <div class="benefit">
                    <img src="@/assets/images/customization.png" alt="Customization" />
                    <h3>Personalized Strategy</h3>
                    <p>
                        각 사용자별로 맞춤형 재정 전략을 제시하여 보다 효율적인 자산 증대를 돕습니다.
                    </p>
                </div>
                <div class="benefit">
                    <img src="@/assets/images/growth.png" alt="Growth" />
                    <h3>Proven Growth</h3>
                    <p>
                        Lucky Rich는 수익성 높은 투자 포트폴리오를 제공하며, 장기적으로 자산을 안정적으로 성장시킵니다.
                    </p>
                </div>
            </div>
        </section>

        <section class="testimonials-section">
            <h2>고객 사용후기</h2>
            <div class="testimonials-grid">
                <div class="testimonial">
                    <p>
                        "Lucky Rich 덕분에 제 자산이 안정적으로 성장하고 있습니다. 맞춤형 서비스 덕분에 복잡한 재정 관리도 이제 쉽고 간단해졌어요!"
                    </p>
                    <h4>- 김민수, 34세</h4>
                </div>
                <div class="testimonial">
                    <p>
                        "고객 지원도 훌륭하고, 투자에 대한 정보도 매우 정확해요. 이제는 재정 걱정 없이 여유롭게 살고 있습니다."
                    </p>
                    <h4>- 박지영, 29세</h4>
                </div>
                <div class="testimonial">
                    <p>
                        "처음엔 의심스러웠지만, Lucky Rich의 서비스와 플랫폼은 기대 이상이었습니다. 추천합니다!"
                    </p>
                    <h4>- 이정훈, 41세</h4>
                </div>
            </div>
        </section>
    </div>
</template>

<script setup>

</script>

<style scoped>
.lucky-rich-page {
    font-family: 'Arial', sans-serif;
}

.hero-section {
    background-image: url('@/assets/images/represent.jpg');
    background-size: cover;
    background-position: center;
    height: 70vh;
    position: relative;
}

.hero-overlay {
    background-color: rgba(0, 0, 0, 0.5);
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #fff;
    text-align: center;
}

.hero-title {
    font-size: 3rem;
    margin-bottom: 10px;
}

.hero-subtitle {
    font-size: 1.5rem;
    margin-bottom: 20px;
}

.cta-button {
    background-color: #f8b400;
    color: #fff;
    padding: 10px 20px;
    font-size: 1rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

.cta-button:hover {
    background-color: #e69c00;
}

.about-section {
    padding: 50px 20px;
    background-color: #f7f7f7;
    text-align: center;
}

.about-content h2 {
    font-size: 2rem;
    margin-bottom: 20px;
}

.about-content p {
    font-size: 1.1rem;
    margin-bottom: 30px;
}

.about-stats {
    display: flex;
    justify-content: space-around;
    margin-top: 20px;
}

.stat {
    text-align: center;
}

.stat h3 {
    font-size: 2rem;
    color: #f8b400;
}

.stat p {
    font-size: 1rem;
    margin-top: 10px;
}

.benefits-section {
    padding: 50px 20px;
    text-align: center;
}

.benefits-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
}

.benefit img {
    width: 60px;
    height: 60px;
    margin-bottom: 20px;
}

.benefit h3 {
    font-size: 1.5rem;
    margin-bottom: 10px;
}

.benefit p {
    font-size: 1rem;
}

.testimonials-section {
    padding: 50px 20px;
    background-color: #f7f7f7;
    text-align: center;
}

.testimonials-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
}

.testimonial p {
    font-size: 1rem;
    font-style: italic;
    margin-bottom: 10px;
}

.testimonial h4 {
    font-size: 1rem;
    font-weight: bold;
}

.cta-section {
    padding: 50px 20px;
    background-color: #333;
    color: #fff;
    text-align: center;
}

.cta-button-large {
    background-color: #f8b400;
    color: #fff;
    padding: 15px 30px;
    font-size: 1.2rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

.cta-button-large:hover {
    background-color: #e69c00;
}
</style>