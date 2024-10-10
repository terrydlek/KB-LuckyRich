<template>
  <highcharts :options="chartOptions" />
  <div class="asset-info">
    <p>
      현재 자산: {{ currentAsset.toLocaleString() }} 원 / 목표 자산:
      {{ userGoal.toLocaleString() }} 원
    </p>
    <input
      v-model.number="userGoal"
      type="number"
      id="userGoal"
      :placeholder="placeholderText"
    />
    <button type="button" @click="updateGoal">수정</button>
  </div>
</template>

<script>
import axios from 'axios';
import Highcharts from 'highcharts';

export default {
  name: 'GoalChart',
  mounted() {
    this.fetchUserGoal();
    this.fetchTotalAssetData();
  },
  data() {
    return {
      userGoal: 100000000,
      placeholderText: '목표 금액 입력',
      currentAsset: 0,
      chartOptions: this.generateChartOptions(100000000, 0),
    };
  },
  methods: {
    fetchUserGoal() {
      const storedGoal = localStorage.getItem('user_goal');
      if (storedGoal) {
        this.userGoal = Number(storedGoal);
      }
    },
    fetchTotalAssetData() {
      const token = localStorage.getItem('access_token');
      axios
        .get('http://localhost:8080/myasset/total', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((res) => {
          const totalAsset =
            res.data.Car +
            res.data['real estate'] +
            res.data['Bank Balance'] +
            res.data['Stock Total'];

          // 총 자산을 currentAsset에 저장
          this.currentAsset = totalAsset;
          console.log('총 자산:', totalAsset);

          // 차트 옵션 업데이트
          this.chartOptions = this.generateChartOptions(
            this.userGoal,
            totalAsset
          );
        })
        .catch((err) => {
          console.log(err);
        });
    },
    generateChartOptions(goalAsset, currentAsset) {
      const tickInterval = Math.floor(goalAsset / 8); // 8부분으로 나눠서 표시
      const cappedAsset = Math.min(currentAsset, goalAsset);

      return {
        chart: {
          type: 'gauge',
          plotBackgroundColor: null,
          plotBackgroundImage: null,
          plotBorderWidth: 0,
          plotShadow: false,
          height: '280px',
        },
        title: {
          text: '목표 자산까지!!!',
          style: {
            color: '#333333',
            fontSize: '18px', // 제목 폰트 크기
            fontFamily: 'Noto Sans',
          },
        },
        pane: {
          startAngle: -90,
          endAngle: 90,
          background: null,
          center: ['50%', '75%'],
          size: '110%',
        },
        yAxis: {
          min: 0,
          max: goalAsset, // 목표 자산이 최대값
          tickPixelInterval: 72,
          tickPosition: 'inside',
          tickLength: 20,
          tickWidth: 1,
          minorTickInterval: null,
          tickInterval: tickInterval, // 8단계로 나눠서 표현
          labels: {
            distance: 20,
            style: {
              fontSize: '14px',
            },
          },
          lineWidth: 0,
          plotBands: [
            {
              from: 0,
              to: goalAsset * 0.6,
              color: '#55BF3B', // Green for lower part
              thickness: 20,
            },
            {
              from: goalAsset * 0.6,
              to: goalAsset * 0.8,
              color: '#DDDF0D', // Yellow for medium
              thickness: 20,
            },
            {
              from: goalAsset * 0.8,
              to: goalAsset,
              color: '#DF5353', // Red for upper part
              thickness: 20,
            },
          ],
        },
        series: [
          {
            name: 'Total Asset',
            data: [cappedAsset],
            tooltip: {
              valueSuffix: ' ₩',
            },
            dataLabels: {
              enabled: false,
            },
            dial: {
              radius: '100%',
              backgroundColor: 'gray',
              baseWidth: 12,
              baseLength: '0%',
              rearLength: '0%',
            },
            pivot: {
              backgroundColor: 'orange',
              radius: 6,
            },
          },
        ],
      };
    },
    updateGoal() {
      const newGoal = this.userGoal || 100000000;
      localStorage.setItem('user_goal', newGoal); // 목표 자산을 로컬 스토리지에 저장
      this.chartOptions = this.generateChartOptions(newGoal, this.currentAsset);
    },
  },
};
</script>

<style>
.asset-info {
  text-align: center;
  margin-top: 10px;
  font-size: 18px;
}
</style>
