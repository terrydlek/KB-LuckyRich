<!-- 포트폴리오 PDF 내용 -->
<template>
  <div>
    <div id="element-to-print" style="padding: 20px; border: 1px solid #ccc">
      <h3>자산 포트폴리오</h3>
      <p>asdsa</p>
      <p>dwqoijcqwdiojcwdq</p>
    </div>
    <button @click="generatePDF">PDF 다운로드</button>
  </div>
  <button @click="generatePDF">PDF 다운로드</button>
</template>

<script>
import html2pdf from 'html2pdf.js';
import totalChart from '@/components/account/chart/totalChart.vue';
import goalChart from '@/components/account/chart/goalChart.vue';
import assetGraph from '@/components/account/chart/assetGraph.vue';
import accountBookChart from '@/components/account/chart/accountBookChart.vue';
import assetcomparison from '@/components/account/chart/assetComparison.vue';
import consumptionstatus from '@/components/account/chart/consumptionstatus.vue';

export default {
  components: {
    totalChart,
    goalChart,
    assetGraph,
    accountBookChart,
    consumptionstatus,
    assetcomparison,
  },
  methods: {
    generatePDF() {
      const element = document.getElementById('element-to-print');
      const options = {
        filename: 'asset_portfolio.pdf', // 파일명 설정
        image: { type: 'jpeg', quality: 0.98 }, // 이미지 품질 설정
        html2canvas: { scale: 2 }, // 캔버스 스케일 설정 (더 높은 해상도)
        jsPDF: { unit: 'in', format: 'a3', orientation: 'landscape' }, // PDF를 가로 방향으로 설정
      };
      html2pdf()
        .from(element)
        .set(options)
        .save()
        .then(() => {
          console.log('PDF가 가로로 다운로드되었습니다.');
        })
        .catch((error) => {
          console.error('PDF 다운로드 중 오류 발생:', error);
        });
    },
  },
};
</script>
