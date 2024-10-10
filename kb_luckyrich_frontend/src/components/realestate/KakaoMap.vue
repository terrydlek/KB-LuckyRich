<template>
    <div class="map-container">
        <!-- 로딩 상태일 때 화면을 어둡게 하고 로딩 스피너와 문구 표시 -->
        <div v-if="isLoading" class="loading-overlay">
            <div class="loading-spinner">
                <div class="spinner-border text-primary" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
                <p>Loading...</p>
            </div>
        </div>

        <!-- 지역 선택 영역 -->
        <div class="region-panel">
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

        <!-- 지도 영역 -->
        <div id="map"></div>

        <!-- 모달 -->
        <div v-if="selectedPlace" class="modal">
            <div class="modal-content">
                <span class="close" @click="closeModal">&times;</span>
                <div class="modal-scrollable-content">
                    <p>단지명: {{ selectedPlace.complexName }}</p>
                    <p>주소: {{ selectedPlace.address }}</p>
                    <div v-for="(priceInfo, index) in selectedPlace.priceInfo" :key="index">
                        <p>{{ priceInfo.floor }}층: {{ priceInfo.price }}억</p>
                    </div>
                    <p>계약월: {{ selectedPlace.contractTime }}</p>
                    <div id="roadview" class="roadview"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'KakaoMap',
    data() {
        return {
            map: null,
            selectedPlace: null,
            selectedOverlay: null,
            selectedRegion: '서울특별시',
            markers: [],
            estate: [],
            isLoading: true,
            isLoadingOtherRegions: false,
            regionCoordinates: {
                "서울특별시": { lat: 37.5663, lng: 126.9779 },
                "경기도": { lat: 37.2752, lng: 127.0094 },
                "부산광역시": { lat: 35.1796, lng: 129.0756 },
                "대구광역시": { lat: 35.8714, lng: 128.6014 },
                "인천광역시": { lat: 37.4563, lng: 126.7052 },
                "광주광역시": { lat: 35.1595, lng: 126.8526 },
                "대전광역시": { lat: 36.3504, lng: 127.3845 },
                "울산광역시": { lat: 35.5396, lng: 129.3115 },
                "충청북도": { lat: 36.6358, lng: 127.4914 },
                "충청남도": { lat: 36.5184, lng: 126.8000 },
                "전북특별자치도": { lat: 35.8205, lng: 127.1082 },
                "전라남도": { lat: 34.8160, lng: 126.4629 },
                "경상북도": { lat: 36.4919, lng: 128.8889 },
                "경상남도": { lat: 35.2382, lng: 128.6928 },
                "강원특별자치도": { lat: 37.8853, lng: 127.7298 },
                "제주특별자치도": { lat: 33.4996, lng: 126.5312 }
            }
        };
    },
    mounted() {
        if (!localStorage.getItem('initialLoadDone')) {
            this.isLoading = true; // 처음 로딩 시에만 로딩창 표시
            localStorage.setItem('initialLoadDone', 'true');
        } else {
            this.isLoading = false;
        }
        this.loadKakaoMap();
        this.fetchRealEstateData('서울특별시');
    },
    methods: {
        loadKakaoMap() {
            if (window.kakao && window.kakao.maps) {
                this.initKakaoMap();
            } else {
                const script = document.createElement('script');
                script.onload = () => {
                    kakao.maps.load(() => {
                        this.initKakaoMap();
                        this.addGovernmentMarkers();
                    });
                };
                script.onerror = () => {
                    console.error("Failed to load Kakao Map script");
                };
                const kakaoApiKey = import.meta.env.VITE_KAKAO_MAP_APPKEY;
                script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${kakaoApiKey}&libraries=services`;
                document.head.appendChild(script);
            }
        },
        initKakaoMap() {
            const mapContainer = document.getElementById('map');
            const mapOptions = {
                center: new kakao.maps.LatLng(37.5663, 126.9779), // 서울 시청을 기본 위치로 설정
                level: 4,
            };
            this.map = new kakao.maps.Map(mapContainer, mapOptions);
            this.addGovernmentMarkers();
        },
        fetchRealEstateData(region) {
            // isLoading은 처음 로딩 때만 사용 (서울특별시)
            if (region === '서울특별시') {
                this.isLoading = true;
            }

            const token = localStorage.getItem('access_token');
             //axios.get(`http://localhost:8080/realestate/?region=${region}`, {
                 axios.get(`http://localhost:8080/realestate/?region=서울특별시`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            })
                .then((response) => {
                    this.estate = response.data.map(item => ({
                        complexName: item.estateName,
                        address: `${item.city} ${item.streetNumber}`,
                        price: item.transactionAmount || '가격 정보 없음',
                        floor: item.floor,
                        contractTime: item.contractTime
                    }));

                    this.createMarkers();
                })
                .catch((error) => {
                    console.error('There was an error fetching the estate data:', error);
                    this.isLoading = false;
                });
        },
        createMarkers() {
            let createdMarkers = 0;

            this.estate.forEach((place) => {
                this.getCoordinatesFromAddress(place.address, (coords) => {
                    if (coords) {
                        const markerPosition = new kakao.maps.LatLng(coords.lat, coords.lng);
                        const imageSrc = '/src/assets/images/EstateImg.png';
                        const imageSize = new kakao.maps.Size(50, 50);
                        const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

                        const marker = new kakao.maps.Marker({
                            position: markerPosition,
                            image: markerImage,
                            map: null,
                        });

                        kakao.maps.event.addListener(marker, 'click', () => {
                            this.openModal({ ...place, ...coords });
                        });

                        this.markers.push({ marker, place });
                    }

                    createdMarkers++;
                    if (createdMarkers === this.estate.length) {
                        this.markers.forEach(markerObj => {
                            markerObj.marker.setMap(this.map);
                        });

                        // 처음 로딩(서울특별시)만 로딩창을 없애고, 다른 지역은 로딩창을 사용하지 않음
                        if (this.selectedRegion === '서울특별시') {
                            this.isLoading = false;
                        }

                        if (!this.isLoadingOtherRegions) {
                            this.loadOtherRegions();
                        }
                    }
                });
            });
        },
        loadOtherRegions() {
            this.isLoadingOtherRegions = true;
            const otherRegions = Object.keys(this.regionCoordinates).filter(region => region !== '서울특별시');
            otherRegions.forEach((region, index) => {
                setTimeout(() => {
                    this.fetchRealEstateData(region);
                }, index * 2000); // 2초 간격으로 다른 지역 데이터 로딩
            });
        },
        getCoordinatesFromAddress(address, callback) {
            const geocoder = new kakao.maps.services.Geocoder();
            geocoder.addressSearch(address, function (result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    const coords = {
                        lat: result[0].y,
                        lng: result[0].x,
                    };
                    callback(coords);
                } else {
                    callback(null);
                }
            });
        },
        navigateToRegion() {
            const region = this.selectedRegion;
            const coords = this.regionCoordinates[region];
            const moveLatLon = new kakao.maps.LatLng(coords.lat, coords.lng);
            this.map.setCenter(moveLatLon); // 선택된 지역으로 지도 이동

            this.addCityHallMarker(coords, region);

        },
        addCityHallMarker(coords, region) {
            const imageSrc = 'http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png'; // 파란색 별 모양 마커 이미지
            const imageSize = new kakao.maps.Size(40, 35); 
            const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

            // 마커 생성
            const markerPosition = new kakao.maps.LatLng(coords.lat, coords.lng);
            const marker = new kakao.maps.Marker({
                position: markerPosition,
                image: markerImage,
                title: region + " 시청/도청", // 마커에 마우스 오버 시 지역명 표시
            });

            // 지도에 마커 표시
            marker.setMap(this.map);
        },
        openModal(place) {
            const relatedTransactions = this.estate.filter(item => item.address === place.address);

            const priceInfo = relatedTransactions.map(item => ({
                floor: item.floor,
                price: item.price,
            }));

            this.selectedPlace = {
                ...place,
                priceInfo,
            };

            this.$nextTick(() => {
                this.initRoadView(place);
            });
        },
        closeModal() {
            this.selectedPlace = null;
        },
        initRoadView(place) {
            const rvContainer = document.getElementById('roadview');
            if (!rvContainer) {
                console.error("로드뷰 div를 찾을 수 없습니다.");
                return;
            }

            const rv = new kakao.maps.Roadview(rvContainer);
            const rc = new kakao.maps.RoadviewClient();
            const rvPosition = new kakao.maps.LatLng(place.lat, place.lng);

            rc.getNearestPanoId(rvPosition, 250, (panoid) => {
                if (panoid) {
                    rv.setPanoId(panoid, rvPosition);
                } else {
                    console.error('로드뷰 파노라마 ID를 찾을 수 없습니다.');
                }
            });

            kakao.maps.event.addListener(rv, 'init', () => {
                const rMarker = new kakao.maps.Marker({
                    position: rvPosition,
                    map: rv
                });

                const rLabel = new kakao.maps.InfoWindow({
                    content: `<div style="padding:5px; width: 200px; word-break: break-all; border: 2px solid black; border-radius: 10px;">
                        ${place.complexName}
                    </div>`,
                });

                rLabel.open(rv, rMarker);

                kakao.maps.event.addListener(rMarker, 'click', () => {
                    rLabel.open(rv, rMarker);
                });
            });
        }
    },
};
</script>

<style scoped>
.map-container {
    display: flex;
    width: 100%;
    height: 100vh;
    position: relative;

}

.loading-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 2000;
    display: flex;
    justify-content: center;
    align-items: center;
}

.loading-spinner {
    text-align: center;
    color: white;
}

.region-panel {
    position: absolute;
    left: 20px; /* 지도 왼쪽에서 약간의 여백 */
    top: 20px;  /* 지도 상단에서 약간의 여백 */
    z-index: 1000;
    padding: 10px;
    background-color: rgba(255, 255, 255, 0.9); /* 살짝 투명한 흰색 배경 */
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.region-panel select {
    background-color: rgba(255, 255, 255, 0); /* 살짝 투명한 흰색 */
    border: none;
    padding: 10px;
    border-radius: 5px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    font-size: 16px;
    color: #333;
}

.region-panel select:focus {
    outline: none;
    box-shadow: 0 0 10px rgba(0, 123, 255, 0.5); /* 선택 시 강조 효과 */
}

#map {
    flex-grow: 1;
    height: 100vh;
}

.modal {
    display: block;
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    width: 800px;
    height: 600px;
    text-align: center;
    position: relative;
    display: flex;
    flex-direction: column;
}

.modal-scrollable-content {
    max-height: 550px;
    overflow-y: auto;
    margin-bottom: 20px;
}

#roadview {
    width: 100%;
    height: 500px;
    margin-top: 20px;
}

.close {
    position: absolute;
    top: 10px;
    right: 20px;
    font-size: 18px;
    cursor: pointer;
}
</style>