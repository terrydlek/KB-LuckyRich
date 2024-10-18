<template>
  <div class="right-sidebar">
    <center>
      <div class="clock">
        <div id="hr" class="hand"></div>
        <div id="mn" class="hand"></div>
        <!-- <div id="sc" class="hand"></div> -->
        <div class="clock-center"></div>
      </div>
    </center>

    <div id="timeEvent">{{ messageText }}</div>
    <img id="lolcatImage" :src="image" alt="Status Image" />

    <label for="wakeUpTimeSelector">Wake Up Time:</label>
    <select id="wakeUpTimeSelector" v-model="wakeuptime">
      <option v-for="hour in hours" :value="hour" :key="hour">{{ hour }}:00</option>
    </select>

    <label for="lunchTimeSelector">Lunch Time:</label>
    <select id="lunchTimeSelector" v-model="lunchtime">
      <option v-for="hour in hours" :value="hour" :key="hour">{{ hour }}:00</option>
    </select>

    <label for="napTimeSelector">Nap Time:</label>
    <select id="napTimeSelector" v-model="naptime">
      <option v-for="hour in hours" :value="hour" :key="hour">{{ hour }}:00</option>
    </select>

    <button id="partyTimeButton" @click="partyEvent">{{ partyButtonText }}</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const hours = Array.from({ length: 24 }, (_, i) => i); // 0 to 23
const wakeuptime = ref(7);
const lunchtime = ref(12);
const naptime = ref(14);
let partytime = ref(-1);
const currentTime = ref('');
const messageText = ref('');
const image = ref('https://s3.amazonaws.com/media.skillcrush.com/skillcrush/wp-content/uploads/2016/08/normalTime.jpg');
const partyButtonText = ref('Party Time!');

const showCurrentTime = () => {
  const now = new Date();
  const hours = now.getHours();
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  const meridian = hours >= 12 ? 'PM' : 'AM';
  const displayHours = hours % 12 || 12;

  currentTime.value = `${displayHours}:${minutes}:${seconds} ${meridian}!`;
};

const updateClock = () => {
  const now = new Date();
  const time = now.getHours();

  // Update message and image based on current time
  if (time == partytime.value) {
    image.value = "https://s3.amazonaws.com/media.skillcrush.com/skillcrush/wp-content/uploads/2016/08/partyTime.jpg";
    messageText.value = "Let's party!";
  } else if (time == wakeuptime.value) {
    image.value = "https://s3.amazonaws.com/media.skillcrush.com/skillcrush/wp-content/uploads/2016/09/cat1.jpg";
    messageText.value = "Wake up!";
  } else if (time == lunchtime.value) {
    image.value = "https://s3.amazonaws.com/media.skillcrush.com/skillcrush/wp-content/uploads/2016/09/cat2.jpg";
    messageText.value = "Let's have some lunch!";
  } else if (time == naptime.value) {
    image.value = "https://s3.amazonaws.com/media.skillcrush.com/skillcrush/wp-content/uploads/2016/09/cat3.jpg";
    messageText.value = "Sleep tight!";
  } else if (time < 12) {
    image.value = "http://image.dongascience.com/Photo/2020/10/8a5748b94df480da7df06adcdaa417c9.jpg";
    messageText.value = "Good morning!";
  } else if (time >= 18) {
    image.value = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Cat_sleep.jpg";
    messageText.value = "Good evening!";
  } else {
    image.value = "https://s3.amazonaws.com/media.skillcrush.com/skillcrush/wp-content/uploads/2016/08/normalTime.jpg";
    messageText.value = "Good afternoon!";
  }

  showCurrentTime();
};

const partyEvent = () => {
  if (partytime.value < 0) {
    partytime.value = new Date().getHours();
    partyButtonText.value = "Party Over!";
  } else {
    partytime.value = -1;
    partyButtonText.value = "Party Time!";
  }
};

// Clock hands rotation logic
const updateClockHands = () => {
  const deg = 6;
  const day = new Date();
  const hh = day.getHours() * 30;
  const mm = day.getMinutes() * deg;
  const ss = day.getSeconds() * deg;

  const hr = document.querySelector('#hr');
  const mn = document.querySelector('#mn');
  const sc = document.querySelector('#sc');

  hr.style.transform = `rotateZ(${(hh) + (mm / 12)}deg)`;
  mn.style.transform = `rotateZ(${mm}deg)`;
};

onMounted(() => {
  setInterval(() => {
    updateClock();
    updateClockHands();
  }, 1000);
  updateClock(); // Initial call
  updateClockHands(); // Initial call
});
</script>

<style scoped>
.right-sidebar {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.clock {
  position: relative;
  width: 100px;
  height: 100px;
  border: 3px solid #000000;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.hand {
  position: absolute;
  background: #333;
  border-radius: 10px;
  transform-origin: bottom center;
  /* Change this line */
  bottom: 50%;
  /* Ensure hands are centered vertically */
}

#hr {
  height: 35%;
  width: 6px;
  background: rgb(0, 0, 0);
}

#mn {
  height: 45%;
  width: 4px;
  background: rgb(7, 7, 7);
}

#sc {
  height: 50%;
  width: 2px;
  background: #00f;
}

.clock-center {
  position: absolute;
  width: 10px;
  height: 10px;
  background: #333;
  border-radius: 50%;
  z-index: 1;
  /* Ensure the center is above the hands */
}

#lolcatImage {
  border-radius: 30px;
}
</style>
