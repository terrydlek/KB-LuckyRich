<template>
    <div class="terms-of-service-page">
        <!-- Hero Section for Terms of Service -->
        <section class="terms-hero-section">
            <h1>이용 약관</h1>
            <p>Lucky Rich의 서비스에 대한 이용 약관을 확인하세요.</p>
        </section>

        <!-- Introduction -->
        <section class="terms-introduction">
            <h2>소개</h2>
            <p>
                Lucky Rich에 오신 것을 환영합니다! 본 이용 약관은 귀하가 저희 서비스를 이용할 때의 권리와 의무를 명확히 합니다.
                서비스를 이용하기 전에 본 약관을 주의 깊게 읽어주시기 바랍니다.
            </p>
        </section>

        <!-- Acceptance of Terms -->
        <section class="terms-section">
            <h2>약관의 동의</h2>
            <p>
                본 약관은 귀하와 Lucky Rich 간의 법적 계약을 형성합니다.
                서비스를 이용함으로써 귀하는 본 약관에 동의하는 것으로 간주됩니다.
            </p>
        </section>

        <!-- Changes to Terms -->
        <section class="terms-section">
            <h2>약관 변경</h2>
            <p>
                저희는 필요에 따라 본 이용 약관을 수정할 수 있으며, 변경 사항은 이 페이지에 게시됩니다.
                변경 사항은 게시 즉시 효력을 가지며, 약관을 정기적으로 확인하시기 바랍니다.
            </p>
        </section>

        <!-- Eligibility -->
        <section class="terms-section">
            <h2>이용 자격</h2>
            <p>
                본 서비스를 이용하기 위해서는 18세 이상의 성인이어야 하며,
                귀하는 본 약관을 준수할 수 있는 법적 능력을 갖추어야 합니다.
            </p>
        </section>

        <!-- User Responsibilities -->
        <section class="terms-section">
            <h2>사용자의 의무</h2>
            <p>
                사용자는 다음과 같은 의무를 가집니다:
            </p>
            <ul>
                <li>정확하고 완전한 정보를 제공할 것.</li>
                <li>서비스 이용 중 발생하는 모든 활동에 대해 책임을 질 것.</li>
                <li>타인의 권리를 침해하지 않도록 주의할 것.</li>
                <li>서비스를 불법적으로 사용하지 않을 것.</li>
            </ul>
        </section>

        <!-- User Accounts -->
        <section class="terms-section">
            <h2>사용자 계정</h2>
            <p>
                사용자는 자신의 계정을 안전하게 유지할 책임이 있으며, 계정의 모든 활동에 대해 책임을 집니다.
                계정 정보의 무단 사용이 의심될 경우 즉시 저희에게 연락해 주세요.
            </p>
        </section>

        <!-- Termination of Service -->
        <section class="terms-section">
            <h2>서비스 종료</h2>
            <p>
                저희는 귀하가 본 약관을 위반할 경우, 서비스 제공을 종료할 수 있는 권리를 가집니다.
                이러한 경우, 귀하는 즉시 서비스 이용을 중단해야 합니다.
            </p>
        </section>

        <!-- Limitation of Liability -->
        <section class="terms-section">
            <h2>책임 제한</h2>
            <p>
                Lucky Rich는 서비스 이용으로 인해 발생하는 직간접적인 손해에 대해 책임을 지지 않습니다.
                귀하는 서비스 이용의 결과에 대해 전적으로 책임을 집니다.
            </p>
        </section>

        <!-- Governing Law -->
        <section class="terms-section">
            <h2>준거법</h2>
            <p>
                본 약관은 대한민국의 법률에 따라 해석되며, 해당 법률에 따라 분쟁이 발생할 경우
                관할 법원은 [관할 법원]으로 정합니다.
            </p>
        </section>

        <!-- Contact Us -->
        <section class="terms-section contact-section">
            <h2>문의하기</h2>
            <p>
                본 이용 약관에 대한 질문이나 문의가 있으시면, 아래의 연락처로 문의해 주세요:
            </p>
            <address>
                Lucky Rich, Inc.<br />
                부자 스트리트 123, 금융시티, 재정주 45678<br />
                이메일: <a
                    href="https://github.com/terrydlek/KB-LuckyRich">https://github.com/terrydlek/KB-LuckyRich</a><br />
            </address>
        </section>
    </div>
</template>

<style scoped>
.terms-of-service-page {
    font-family: 'Noto Sans KR', sans-serif;
    line-height: 1.6;
    max-width: 100%;
    margin: 0 auto;
    padding: 20px;
    background-color: #fafafa;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.terms-hero-section {
    background-color: #6c63ff;
    color: white;
    text-align: center;
    padding: 40px 20px;
    border-radius: 8px;
    margin-bottom: 30px;
}

.terms-hero-section h1 {
    font-size: 2.5rem;
    margin-bottom: 10px;
}

.terms-hero-section p {
    font-size: 1.2rem;
}

.terms-introduction,
.terms-section {
    background-color: white;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.terms-section h2 {
    font-size: 1.8rem;
    margin-bottom: 10px;
    color: #333;
}

.terms-section p,
.terms-section ul {
    font-size: 1rem;
    color: #555;
}

.terms-section ul {
    padding-left: 20px;
}

.terms-section ul li {
    margin-bottom: 8px;
}

.contact-section address {
    font-style: normal;
    line-height: 1.6;
}

.contact-section a {
    color: #6c63ff;
    text-decoration: none;
}

.contact-section a:hover {
    text-decoration: underline;
}
</style>