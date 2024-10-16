# 🌟 Lucky Rich

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

**MZ 세대를 위한 자산 관리 서비스**, Lucky Rich는 자산 관리, 투자 분석 및 맞춤형 금융 상품 추천 기능을 제공합니다.

<p>
   <img src="./README-GIF/title_image.png" alt="대표 이미지" height="630px" width="920px">
</p>

---

## 📋 목차
- [주요 기능](#-주요-기능)
- [설치 방법](#-설치-방법)
- [사용 방법](#-사용-방법)
- [시스템 아키텍처](#%EF%B8%8F-시스템-아키텍처)
- [구조도](#-구조도)
- [ERD](#-erd-entity-relationship-diagram)
- [기술 스택](#-기술-스택)
- [서비스 시연](#-서비스-시연)
- [팀원](#%EF%B8%8F-팀원)

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

- 회원가입: 회원 가입 후 자산 관리 시작.

- 계좌 연결: 은행 및 투자 계좌를 연동하여 자산 데이터 조회.

- 포트폴리오 생성: '포트폴리오 생성' 버튼을 통해 맞춤형 투자 전략 생성.

- 분석 확인: 재무 상태에 대한 상세 분석 보고서 제공.

---

## 🏗️ 시스템 아키텍처

<div>
  <img src="./README-GIF/architecture.png" alt="시스템 아키텍처 이미지" style="border: 1px solid #ddd; border-radius: 10px;" height="400px" width="680px">
  <p><b>시스템 아키텍처</b></p>
</div>

---

## 📋 구조도

<div>
  <img src="./README-GIF/structure.png" alt="구조도 이미지" style="border: 1px solid #ddd; border-radius: 10px;" height="400px" width="750px">
  <p><b>시스템 구조도</b></p>
</div>

---

## 📚 ERD (Entity Relationship Diagram)

<div>
  <img src="./README-GIF/ERD.png" alt="ERD 이미지" style="border: 1px solid #ddd; border-radius: 10px;" height="530px" width="700px">
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

## 🪄 서비스 시연

### 로그인 및 회원가입
| **로그인** |
| :---: |
| <img height="400px" width="720px" alt="로그인" src="./README-GIF/로그인.gif"> |
- 구글, 카카오, 네이버 계정을 통해 간편하게 소셜 로그인 및 회원가입이 가능합니다.
<br/>

| **최초 로그인 시** |
| :---: |
| <img height="400px" width="720px" alt="로그인" src="./README-GIF/계좌연동.gif"> |
| <img height="400px" width="720px" alt="로그인" src="./README-GIF/계좌선택.gif"> |
- 마이데이터 제공 동의를 선택하면 유저의 계좌를 연동할 수 있습니다.
<br/>

| **로그인 후 메인화면** |
| :---: |
| <img height="400px" width="720px" alt="로그인 후 메인" src="./README-GIF/로그인후_메인.gif"> |
- 사용자가 보유한 자산의 총액, 자산 분포, 투자 성과 등을 한눈에 확인할 수 있는 시각적 대시보드를 제공합니다. 그래프와 차트를 통해 자산의 변동 추세를 쉽게 이해할 수 있습니다.

<br/>

| **마이페이지** |
| :---: |
| <img height="400px" width="720px" alt="마이페이지" src="./README-GIF/마이페이지.gif"> |
- 사용자가 자신의 계정 정보를 확인하고 수정할 수 있는 기능을 제공합니다. 여기에는 성별, 나이 등의 개인 정보 업데이트가 포함됩니다.
- 회원 탈퇴 기능을 제공합니다.
<br/>

| **Lucky Rich 서비스 소개** |
| :---: |
| <img height="400px" width="720px" alt="서비스 소개" src="./README-GIF/서비스소개.gif"> |
- Lucky Rich의 주요 서비스 및 기능을 간략하게 소개하여, 사용자가 플랫폼이 제공하는 다양한 금융 관리 도구와 혜택을 이해할 수 있도록 합니다.
- 사용자가 Lucky Rich를 어떻게 활용할 수 있는지에 대한 단계별 안내를 제공하여, 가입, 자산 관리, 투자 상품 추천 등의 과정을 쉽게 이해하고 이용할 수 있도록 돕습니다.
<br/>

| **투자 성향 테스트에 기반한 상품 추천** |
| :---: |
| <img height="400px" width="720px" alt="테스트" src="./README-GIF/테스트.gif"> |
| <img height="400px" width="720px" alt="상세페이지" src="./README-GIF/추천상품_상세페이지.gif"> |
- 사용자의 투자 성향 테스트 결과에 따라 적합한 금융 상품(예: 예적금, 펀드, 주식 등)을 추천하여, 개인의 위험 선호도와 목표에 맞는 투자 옵션을 제공합니다.
- 추천된 각 상품에 대한 상세 정보를 제공하여, 수익률, 리스크, 투자 기간 등의 데이터를 한눈에 확인할 수 있도록 하여, 사용자가 정보 기반의 결정을 할 수 있도록 지원합니다.
<br/>

| **한눈에 내 자산 확인하기** |
| :---: |
| <img height="400px" width="720px" alt="자산대시보드" src="./README-GIF/자산대시보드.gif"> |
- 사용자가 보유한 자산의 총액과 자산 분포를 한눈에 확인할 수 있는 기능으로, 자산의 변화 추이를 시각적으로 보여줍니다.
- 사용자의 자산을 카테고리별로 구분하여, 예적금, 주식, 펀드 등 각 자산 유형별 상세 정보를 제공합니다.
<br/>

| **자산 포트폴리오** |
| :---: |
| <img height="400px" width="720px" alt="포트폴리오" src="./README-GIF/포트폴리오.gif"> |
- 사용자가 보유한 자산을 그래프나 차트 형식으로 시각화하여 자산의 구성 비율을 한눈에 파악할 수 있는 기능을 제공합니다.
- 사용자의 자산 구성과 투자 성향을 바탕으로 AI가 맞춤형 분석을 제공하여, 현재 자산 상태를 효율적으로 파악할 수 있는 기능을 제공합니다.
<br/>

| **거래 내역 조회** |
| :---: |
| <img height="400px" width="720px" alt="거래내역조회1" src="./README-GIF/거래내역조회_필터링.gif"> |
| <img height="400px" width="720px" alt="거래내역조회2" src="./README-GIF/거래내역조회_csv.gif"> |
- 사용자가 원하는 기간을 설정하여 해당 기간 동안의 모든 거래 내역을 확인할 수 있는 기능을 제공합니다.
- 거래 내역을 csv 형식의 파일로 다운로드 할 수 있는 기능을 제공합니다.

<br/>

| **자주 묻는 질문** |
| :---: |
| <img height="400px" width="720px" alt="자주묻는질문" src="./README-GIF/자주 묻는 질문.gif"> |
- 자주 묻는 질문들을 카테고리별로 분류하여 사용자가 관련 질문과 답변을 쉽게 찾을 수 있는 기능을 제공합니다.
<br/>

| **Q&A 게시판** |
| :---: |
| <img height="400px" width="720px" alt="게시판" src="./README-GIF/board.gif"> |
- 사용자가 새로운 질문을 작성하거나 기존 질문을 수정할 수 있는 기능을 제공. 각 질문에 제목과 내용을 입력할 수 있으며, 수정된 질문은 즉시 업데이트됩니다.
- 작성된 질문들의 목록을 확인할 수 있으며, 각 질문을 클릭하면 해당 질문의 상세 내용을 조회할 수 있는 기능을 제공합니다.

<br/>

| **관리자 페이지** |
| :---: |
| <img height="400px" width="720px" alt="관리자" src="./README-GIF/관리자.gif"> |
- 사용자가 작성한 Q&A 질문 게시글을 검토, 수정, 삭제할 수 있으며, 다수의 게시글을 선택하여 일괄 삭제하는 기능을 제공합니다.
<br/>

| **금융 뉴스** |
| :---: |
| <img height="400px" width="720px" alt="뉴스" src="./README-GIF/뉴스.gif"> |
- 최신 금융 뉴스를 실시간으로 업데이트하고 사용자에게 제공합니다.
- 각 뉴스의 제목을 클릭하면 해당 뉴스의 상세 페이지로 이동하여 더 많은 정보를 확인할 수 있습니다.
<br/>

| **부동산** |
| :---: |
| <img height="400px" width="720px" alt="부동산" src="./README-GIF/부동산.gif"> |
- 다양한 매물의 위치, 가격, 크기, 거래 조건 등을 한눈에 볼 수 있습니다.
- 필터링 기능을 통해 원하는 조건에 맞는 부동산 매물을 검색할 수 있습니다.
<br/>

---

## 🏄‍♀️ 팀원
| [황세호](https://github.com/ssoo1234) | [박제욱](https://github.com/terrydlek) |[김재이](https://github.com/Kim-Jaei) | [이시윤](https://github.com/siyun2)
| :---: | :---: | :---: | :---: |
| <img src="https://avatars.githubusercontent.com/u/97676838?v=4" width="150"/> | <img src="https://avatars.githubusercontent.com/u/103116277?v=4" width="150"/> | <img src="https://avatars.githubusercontent.com/u/170384603?v=4" width="150"/> | <img src="https://avatars.githubusercontent.com/u/138185165?v=4" width="150"/> | 
| Full-Stack | Full-Stack | Full-Stack | Full-Stack |
