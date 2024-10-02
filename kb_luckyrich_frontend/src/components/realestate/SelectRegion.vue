<template>
    <div>
      <h1>원하는 지역 부동산 거래가 보기</h1>
      <select v-model="selectedRegion" @change="navigateToRegion">
        <option value="서울특별시">서울특별시</option>
        <option value="경기도">경기도</option>
        <option value="부산광역시">부산광역시</option>
        <option value="대구광역시">대구광역시</option>
        <option value="인천광역시">인천광역시</option>
        <option value="광주광역시">광주광역시</option>
        <option value="대전광역시">대전광역시</option>
        <option value="울산광역시">울산광역시</option>
        <option value="충청북도">충청북도</option>
        <option value="충청남도">충청남도</option>
        <option value="전북특별자치도">전북특별자치도</option>
        <option value="전라남도">전라남도</option>
        <option value="경상북도">경상북도</option>
        <option value="경상남도">경상남도</option>
        <option value="강원특별자치도">강원특별자치도</option>
        <option value="제주특별자치도">제주특별자치도</option>
      </select>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        selectedRegion: '서울특별시', 
      };
    },
    methods: {
      navigateToRegion() {
        const selectedRegion = this.selectedRegion;
        // 선택된 지역에 따라 페이지 이동
        this.$router.push(`/luckyrich/realestate/${selectedRegion}`);
      },
    },
  };
  </script>
  
  <style>
  /* 필요에 따라 스타일 추가 */
  </style>
  