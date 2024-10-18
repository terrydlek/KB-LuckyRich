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
                    <h5>단지명 정보</h5>
                    <table class="modal-table">
                        <thead>
                            <tr>
                                <th>항목</th>
                                <th>내용</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>단지명</td>
                                <td>{{ selectedPlace.complexName }}</td>
                            </tr>
                            <tr>
                                <td>주소</td>
                                <td>{{ selectedPlace.address }}</td>
                            </tr>
                            <tr>
                                <td>전용면적</td>
                                <td>{{ selectedPlace.exclusiveArea }}m²</td>
                            </tr>
                            <tr>
                                <td>건축연도</td>
                                <td>{{ selectedPlace.constructionTime }}년</td>
                            </tr>
                            <tr>
                                <td>주택 유형</td>
                                <td>{{ selectedPlace.houseType }}</td>
                            </tr>
                            <tr>
                                <td>거래 유형</td>
                                <td>{{ selectedPlace.transactionType }}</td>
                            </tr>
                            <tr v-for="(priceInfo, index) in selectedPlace.priceInfo" :key="index">
                                <td>{{ priceInfo.floor }}층</td>
                                <td>{{ priceInfo.price }}억</td>
                            </tr>
                            <tr>
                                <td>계약월</td>
                                <td>{{ formatContractTime(selectedPlace.contractTime) }}</td>
                            </tr>
                        </tbody>
                    </table>

                    <h5>로드뷰</h5>
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
            estateData: [],
            isLoading: true,
            isLoadingOtherRegions: false,
            regionCoordinates: {
                "서울특별시": { lat: 37.5663, lng: 126.9779 },
                "경기도": { lat: 37.288, lng: 127.054 },
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
            this.isLoading = true;
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
                center: new kakao.maps.LatLng(37.5663, 126.9779),
                level: 4,
            };
            this.map = new kakao.maps.Map(mapContainer, mapOptions);
            this.addAllCityHallMarkers();

            kakao.maps.event.addListener(this.map, 'zoom_changed', () => {
                const level = this.map.getLevel();
                this.toggleMarkers(level);
            });
        },
        fetchRealEstateData(region) {
            if (region === '서울특별시') {
                this.isLoading = true;
            }

            const token = localStorage.getItem('access_token');
            axios.get(`http://localhost:8080/realestate/?region=${region}`, {
                //axios.get(`http://localhost:8080/realestate/?region=서울특별시`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            })
                .then((response) => {
                    const regionEstateData = response.data.map(item => ({
                        complexName: item.estateName,
                        address: `${item.city} ${item.streetNumber}`,
                        price: item.transactionAmount || '가격 정보 없음',
                        floor: item.floor,
                        contractTime: item.contractTime,
                        exclusiveArea: item.exclusiveArea || '정보 없음',
                        constructionTime: item.constructionTime || '정보 없음',
                        houseType: item.houseType || '정보 없음',
                        transactionType: item.transactionType || '정보 없음',
                        region
                    }));
                    this.estate = regionEstateData;
                    this.estateData = [...this.estateData, ...regionEstateData];
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
                        const imageSize = new kakao.maps.Size(60, 50);
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
                }, index * 1000);
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
            this.map.setCenter(moveLatLon);
        },
        addAllCityHallMarkers() {
            Object.keys(this.regionCoordinates).forEach(region => {
                const coords = this.regionCoordinates[region];
                this.addCityHallMarker(coords, region);
            });
        },
        addCityHallMarker(coords, region) {
            const imageSrc = '/src/assets/images/cityMarker.png'
            const imageSize = new kakao.maps.Size(40, 50);
            const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

            const markerPosition = new kakao.maps.LatLng(coords.lat, coords.lng);
            const marker = new kakao.maps.Marker({
                position: markerPosition,
                image: markerImage,
                title: region + " 시청/도청",
            });
            marker.setMap(this.map);
        },
        toggleMarkers(level) {
            const shouldShowMarkers = level < 9; 

            this.markers.forEach(markerObj => {
                if (shouldShowMarkers) {
                    markerObj.marker.setMap(this.map); 
                } else {
                    markerObj.marker.setMap(null); 
                }
            });
        },
        formatContractTime(contractTime) {
            if (contractTime && contractTime.length === 6) {
                const year = contractTime.substring(0, 4);
                const month = contractTime.substring(4, 6);
                return `${year}년 ${month}월`;
            }
            return contractTime; 
        },
        openModal(place) {
            const relatedTransactions = this.estateData.filter(item => item.address === place.address);
            console.log("Related Transactions", relatedTransactions);

            const priceInfo = relatedTransactions.map(item => ({
                floor: item.floor,
                price: item.price,
            }));

            this.selectedPlace = {
                ...place,
                priceInfo,
                exclusiveArea: place.exclusiveArea || '정보 없음',  
                constructionTime: place.constructionTime || '정보 없음', 
                houseType: place.houseType || '정보 없음',  
                transactionType: place.transactionType || '정보 없음' 
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
    left: 20px;
    top: 20px;
    z-index: 1000;
    padding: 10px;
    background-color: rgba(255, 255, 255, 0.9);
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.region-panel select {
    background-color: rgba(255, 255, 255, 0);
    border: none;
    padding: 10px;
    border-radius: 5px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    font-size: 16px;
    color: #333;
}

.region-panel select:focus {
    outline: none;
    box-shadow: 0 0 10px rgba(0, 123, 255, 0.5);
    /* 선택 시 강조 효과 */
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
    margin-top: 20px
}

.modal-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}

.modal-table th,
.modal-table td {
    border: 1px solid #ddd;
    padding: 8px;
}

.modal-table th {
    background-color: #f4f4f4;
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
    font-size: 30px;
    cursor: pointer;
}
</style>