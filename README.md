# 🌟 Lucky Rich

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

**MZ 세대를 위한 자산 관리 서비스**, Lucky Rich는 자산 관리, 투자 분석 및 맞춤형 금융 상품 추천 기능을 제공합니다.

<br>
<p>
   <img src="./title_image.png" alt="대표 이미지" height="600px" width="820px">
</p>

---

## 📋 목차
- [주요 기능](#-주요-기능)
- [설치 방법](#-설치-방법)
- [사용 방법](#-사용-방법)
- [시스템 아키텍처](#-시스템-아키텍처)
- [구조도](#-구조도)
- [API 문서](#-api-문서)
- [ERD](#-erd)
- [기술 스택](#-기술-스택)
- [주요 포인트](#주요-포인트)
- [팀원](#-팀원)
- [서비스 시연](#-서비스-시연)

---

## ✨ 주요 기능

- 📊 **포트폴리오 관리**: 사용자 맞춤형 투자 포트폴리오 자동 생성
- 💰 **자산 분석**: 시간에 따른 자산 시각화 및 분석
- 🔍 **금융 상품 추천**: 사용자 프로필에 따른 맞춤형 금융 상품 추천
- 💹 **실시간 자산 비교**: 비슷한 연령대의 자산과 비교
- 📈 **트렌드 추적**: 자산 증가 추이 및 투자 동향 추적
 
 ### 1. 투자 성향 분석을 통한 개인맞춤형 추천
 
 ### 🧪 투자 성향 테스트를 통한 상품 추천
 - 총 테스트 점수를 바탕으로 사용자의 성향을 분류하고, 해당 성향에 맞는 투자 상품을 추천합니다.
 - 성향은 총 5가지로 나뉘며, 안정형/안정추구형/위험중립형/적극투자형/공격투자형이 있습니다.
 - 안정형은 예적금 상품, 안정추구형은 채권형 펀드 상품, 위험중립형은 재간접 펀드 상품, 적극투자형은 주식 상품, 공격투자형은 암호화폐를 추천합니다.
 
 ### 👍 추천 상품 상세 페이지
 - 테스트를 완료 사용자에게 성향에 대한 설명과 해당 상품을 추천하는 이유를 제공합니다.
 - 추천 상품 TOP 100 리스트를 표 형식으로 볼 수 있으며, 주식명을 클릭하면 해당 주식의 상세 정보를 확인하여 상품의 정보를 쉽게 찾을 수 있습니다.
 - 페이지의 로딩 시간을 줄이기 위해 Redis를 도입하여 사용자 경험을 개선했습니다.
 
 ### 2. 💰 한눈에 내 자산 확인하기
 - 모든 자산을 직관적인 대시보드로 나타내 한눈에 자신의 자산 상태를 파악할 수 있어 자산 관리가 더 쉬워집니다.
 - 사용자의 자본에 맞는 투자 상품을 추천합니다.
 - 자산 분포, 목표 자산 달성도, 자본 증감 추이, 계좌 보유 잔액, 카테고리별 소비 분석, 내 또래의 자산 분포를 시각화된 차트로 볼 수 있습니다.
 
 ### 3. 🎨 AI 기반의 스마트 자산 관리 조언
 - 자산 포트폴리오를 다운 받으면 사용자의 모든 자산정보와 금융 트렌드, 사용자 데이터에 기반한 맞춤형 조언을 받아 더 나은 투자 결정을 내릴 수 있습니다.
 - 포트폴리오 다운이 완료되면 실시간 알림을 받을 수 있어 대기 시간을 효율적으로 사용할 수 있습니다.
 - 포트폴리오 다운로드 시간이 길어짐에 따라, Rebbit MQ와 WebSocket을 사용해 실시간 알림을 전송하도록 구현했습니다.
 
 ### 4. 💳 거래 내역 조회
 - 사용자가 가진 계좌별로 거래 내역을 조회할 수 있습니다.
 - 날짜, 입금/출금, 카테고리 필터링을 통해 원하는 내역을 쉽고 빠르게 찾을 수 있습니다.
 - 거래 내역을 csv 파일로 다운 받아 개인적으로 백업 및 보관이 가능합니다.
 
### 5. ❓ Q&A
- 자주 묻는 질문 리스트를 통해 빠르게 궁금증을 해결할 수 있습니다.
- 게시판에 직접 글을 작성하여 문의할 수 있고, 관리자만 답변이 가능합니다. 

---

## 🚀 데모

🔗 [Live Demo](http://your-demo-url.com)

![Lucky Rich Demo](https://your-demo-image-url.com/demo.gif)

---

## 🔧 설치 방법

아래 단계를 따라 로컬 환경에 프로젝트를 설치하세요.

1. 저장소 클론:
   ```bash
   git clone https://github.com/terrydlek/KB-LuckyRich.git

2. 프로젝트 디렉토리로 이동:
   ```bash
   cd LuckyRich

3. 의존성 설치:
   ```bash
   npm install

4. 개발 서버 실행:
   ```bash
   npm run dev

---

## 📖 사용 방법

회원가입: 회원 가입 후 자산 관리 시작.

계좌 연결: 은행 및 투자 계좌를 연동하여 자산 데이터 조회.

포트폴리오 생성: '포트폴리오 생성' 버튼을 통해 맞춤형 투자 전략 생성.

분석 확인: 재무 상태에 대한 상세 분석 보고서 제공.

---

## 🏗️ 시스템 아키텍처

<div align="center">
  <img src="./architecture.png" alt="시스템 아키텍처 이미지" style="border: 1px solid #ddd; border-radius: 10px;" height="400px" width="700px">
  <p><b>시스템 아키텍처</b></p>
</div>

---

## 📋 구조도

<div align="center">
  <img src="./structure.png" alt="구조도 이미지" style="border: 1px solid #ddd; border-radius: 10px;" height="490px" width="800px">
  <p><b>시스템 구조도</b></p>
</div>

---

## 📚 ERD (Entity Relationship Diagram)

<div align="center">
  <img src="./ERD.png" alt="ERD 이미지" style="border: 1px solid #ddd; border-radius: 10px;" height="500px" width="700px">
  <p><b>ERD (Entity Relationship Diagram)</b></p>
</div>


---

## 📺 기술 스택

### ⚙️ Backend
- <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=strapi&logoColor=white">
- <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white">

### 💾 Database
- <img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white">
- <img src="https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white">

### ☁️ Infrastructure
- <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white">
- <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
- <img src="https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white">

### 🔐 Authentication & Security
- <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens">

### 🛠 Development Tools
- <img src="https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white">
- <img src="https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
- <img src="https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white">

### 🧑‍💻 Collaboration & Design
- <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
- <img src="https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white">
- <img src="https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white">

### 📦 API & Testing
- <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
- <img src="https://img.shields.io/badge/chatGPT-74aa9c?style=for-the-badge&logo=openai&logoColor=white">

---

### 주요 포인트:
1. **시각적 요소**: 프로젝트 뱃지와 아이콘을 사용해 가독성 높임.
2. **데모 및 이미지**: 실시간 데모 링크와 프로젝트 스크린샷, GIF 등 추가.
3. **깔끔한 목차**: 사용자들이 문서를 쉽게 탐색할 수 있도록 목차 구성.
4. **설치 및 사용법**: 명확한 설치 방법과 사용 예시 제공.
5. **API 문서**: 주요 엔드포인트를 간단히 소개하고 전체 문서로 링크 연결.

---

### 🪄 서비스 시연

| **로그인** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/로그인.gif"> |
<br/>

| **최초 로그인 시 정보 이용 동의** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/계좌연동.gif"> |
<br/>

| **정보 이용 동의 후 자산 관리 서비스를 받을 계좌 연동** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/계좌선택.gif"> |
<br/>

| **로그인 후 메인화면** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/로그인후_메인.gif"> |
<br/>

| **마이페이지** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/마이페이지.gif"> |
<br/>

| **Lucky Rich 서비스 소개** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/서비스소개.gif"> |
<br/>

| **투자 성향 테스트에 기반한 상품 추천** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/테스트.gif"> |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/추천상품_상세페이지.gif"> |
<br/>

| **한눈에 내 자산 확인하기** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/자산대시보드.gif"> |
<br/>

| **자산 포트폴리오** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/자산대시보드.gif"> |
<br/>

| **거래 내역 조회** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/거래내역조회_필터링.gif"> |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/거래내역조회_csv.gif"> |
<br/>

| **자주 묻는 질문** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/자주 묻는 질문.gif"> |
<br/>

| **Q&A 게시판** |
| :---: |
| <img height="420px" width="720px" alt="프로필" src="./README-GIF/게시판.gif"> |
<br/>

| **관리자 페이지** |
| :---: |
| <img height="420px" width="720px" alt="프로필" src="./README-GIF/관리자.gif"> |

| **금융 뉴스** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/거래내역조회_필터링.gif"> |
<br/>

| **부동산** |
| :---: |
| <img height="420px" width="720px" alt="로그인" src="./README-GIF/거래내역조회_필터링.gif"> |
<br/>

---

### 팀원
| [황세호](https://github.com/ssoo1234) | [박제욱](https://github.com/terrydlek) |[김재이](https://github.com/Kim-Jaei) | [이시윤](https://github.com/siyun2)
| :---: | :---: | :---: | :---: |
| <img src="https://avatars.githubusercontent.com/u/97676838?v=4" width="150"/> | <img src="https://avatars.githubusercontent.com/u/103116277?v=4" width="150"/> | <img src="https://avatars.githubusercontent.com/u/170384603?v=4" width="150"/> | <img src="https://avatars.githubusercontent.com/u/138185165?v=4" width="150"/> | 
| Full-Stack | Full-Stack | Full-Stack | Full-Stack |
