# 베이스 이미지 선택
FROM node:20

# 작업 디렉토리 설정
WORKDIR /app

# 의존성 설치
COPY package*.json ./
RUN npm install

# 모든 파일 복사
COPY . .

# 빌드
RUN npm run build

# 빌드된 파일을 서빙
RUN npm install -g serve
CMD ["serve", "-s", "dist"]

# 포트 노출
EXPOSE 3000