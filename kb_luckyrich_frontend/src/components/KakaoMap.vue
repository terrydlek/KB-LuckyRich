<template>
    <div class="map-container">
        <!-- 검색 영역 -->
        <div class="search-panel">
            <input v-model="searchQuery" placeholder="단지명을 입력하세요" />
            <button @click="searchPlaces">검색</button>

            <!-- 검색 결과 표시 -->
            <div v-if="searchResults.length > 0" class="result-list">
                <div v-for="(place, index) in searchResults" :key="index" class="result-item">
                    <p>단지명: {{ place.complexName }}</p>
                    <p>주소: {{ place.address }}</p>
                    <p>거래가: {{ place.price }}억</p>
                    <button @click="highlightMarker(place)">지도에서 보기</button>
                </div>
            </div>
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

                    <!-- 층별 거래 정보 표시 -->
                    <div v-for="(priceInfo, index) in selectedPlace.priceInfo" :key="index">
                        <p>{{ priceInfo.floor }}층: {{ priceInfo.price }}억</p>
                    </div>

                    <p>계약월: {{ selectedPlace.contractTime }}</p>

                    <!-- 로드뷰 영역 -->
                    <div id="roadview" class="roadview"></div> <!-- 로드뷰를 표시할 div -->
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
            searchQuery: '',
            searchResults: [],
            markers: [],
            estate: []
        };
    },
    mounted() {
        this.loadKakaoMap();
        axios.get('http://localhost:8080/realestate/getEstate')
            .then((response) => {
                // estate 데이터를 변환해서 places로 사용할 수 있도록 함
                this.estate = response.data.map(item => ({
                    complexName: item.estateName,
                    address: `${item.city} ${item.streetNumber}`,
                    price: item.transactionAmount || '가격 정보 없음',
                    floor: item.floor,
                    contractTime: item.contractTime
                }));
                this.initKakaoMap();
            })
            .catch((error) => {
                console.error('There was an error fetching the estate data:', error);
            });
    },
    methods: {
        loadKakaoMap() {
            if (window.kakao && window.kakao.maps) {
                this.initKakaoMap();
            } else {
                const script = document.createElement('script');
                script.onload = () => {
                    kakao.maps.load(this.initKakaoMap);
                };
                script.onerror = () => {
                    console.error("Failed to load Kakao Map script");
                };
                const kakaoApiKey = import.meta.env.VITE_KAKAO_MAP_APPKEY;
                script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${kakaoApiKey}&libraries=services`;                document.head.appendChild(script);
            }
        },
        initKakaoMap() {
            const mapContainer = document.getElementById('map');
            const mapOptions = {
                center: new kakao.maps.LatLng(37.5, 126.9),
                level: 7,
            };
            this.map = new kakao.maps.Map(mapContainer, mapOptions);

            const markerInfo = {};  // 주소를 키로 해서 해당 주소에 대한 거래 정보를 저장

            // estate 데이터를 사용하여 지도에 마커 추가
            this.estate.forEach((place) => {
                this.getCoordinatesFromAddress(place.address, (coords) => {
                    if (coords) {
                        const addressKey = `${place.address}`;

                        // 이미 해당 주소에 대한 마커가 존재하는 경우
                        if (markerInfo[addressKey]) {
                            // 기존 오버레이에 층수와 가격 추가
                            const existingOverlayContent = markerInfo[addressKey].overlayContent;
                            existingOverlayContent.innerHTML += `<div>${place.floor}층: ${place.price}억</div>`;
                        } else {
                            // 새로운 마커와 오버레이 생성
                            const markerPosition = new kakao.maps.LatLng(coords.lat, coords.lng);
                            const marker = new kakao.maps.Marker({
                                position: markerPosition,
                                map: this.map,
                            });

                            const overlayContent = document.createElement('div');
                            overlayContent.style.padding = '10px';
                            overlayContent.style.backgroundColor = 'white';
                            overlayContent.style.border = '2px solid black';
                            overlayContent.style.borderRadius = '10px';
                            overlayContent.style.fontSize = '14px';
                            overlayContent.style.fontWeight = 'bold';
                            overlayContent.style.boxShadow = '2px 2px 5px rgba(0,0,0,0.3)';
                            overlayContent.style.whiteSpace = 'nowrap';
                            overlayContent.innerHTML = `
                        <div>단지명: ${place.complexName}</div>
                        <div>${place.floor}층: ${place.price}억</div>
                    `;

                            overlayContent.addEventListener('click', () => {
                                this.openModal({ ...place, ...coords });
                            });

                            const customOverlay = new kakao.maps.CustomOverlay({
                                content: overlayContent,
                                position: markerPosition,
                                yAnchor: 1,
                                zIndex: 100,
                            });
                            customOverlay.setMap(this.map);

                            kakao.maps.event.addListener(marker, 'click', () => {
                                this.openModal({ ...place, ...coords });
                            });

                            // 마커 정보 저장
                            markerInfo[addressKey] = {
                                marker,
                                overlayContent,
                                customOverlay
                            };
                        }

                        this.markers.push({ marker: markerInfo[addressKey].marker, place, customOverlay: markerInfo[addressKey].customOverlay });
                    }
                });
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
                    //console.error("주소 변환 실패", status);
                    callback(null);
                }
            });
        },
        openModal(place) {
            // 동일한 주소의 모든 거래 정보를 찾음
            const relatedTransactions = this.estate.filter(item => item.address === place.address);

            // priceInfo 배열에 해당 주소의 모든 층과 가격 정보를 저장
            const priceInfo = relatedTransactions.map(item => ({
                floor: item.floor,
                price: item.price,
            }));

            // 선택된 장소에 모든 거래 정보를 포함
            this.selectedPlace = {
                ...place,
                priceInfo, // 층별 가격 정보 배열 추가
            };

            this.$nextTick(() => {
                this.initRoadView(place);
            });
        }
        ,
        closeModal() {
            this.selectedPlace = null;
        },
        initRoadView(place) {
            const rvContainer = document.getElementById('roadview'); // 로드뷰를 표시할 div
            if (!rvContainer) {
                console.error("로드뷰 div를 찾을 수 없습니다.");
                return;
            }

            const rv = new kakao.maps.Roadview(rvContainer); // 로드뷰 객체 생성
            const rc = new kakao.maps.RoadviewClient(); // 로드뷰 헬퍼 객체 생성
            const rvPosition = new kakao.maps.LatLng(place.lat, place.lng); // 선택한 장소의 좌표

            rc.getNearestPanoId(rvPosition, 200, (panoid) => {
                if (panoid) {
                    rv.setPanoId(panoid, rvPosition); // 로드뷰에 파노라마 ID 설정
                } else {
                    console.error('로드뷰 파노라마 ID를 찾을 수 없습니다.');
                }
            });
            // 로드뷰 초기화 시 이벤트 설정
            kakao.maps.event.addListener(rv, 'init', () => {
                // 로드뷰에 마커 설정
                const rMarker = new kakao.maps.Marker({
                    position: rvPosition, // 로드뷰의 중심 좌표에 마커를 설정
                    map: rv // 마커를 로드뷰에 추가
                });

                // 로드뷰에 인포윈도우 추가
                const rLabel = new kakao.maps.InfoWindow({
                    content: `<div style="padding:5px; width: 200px; word-break: break-all; border: 2px solid black; border-radius: 10px;">
                  ${place.complexName}
                </div>`,
                });

                // 마커 위에 인포윈도우 표시
                rLabel.open(rv, rMarker);

                // 마커를 클릭했을 때 인포윈도우를 다시 열기
                kakao.maps.event.addListener(rMarker, 'click', () => {
                    rLabel.open(rv, rMarker);
                });
            });
        },

        searchPlaces() {
            if (this.searchQuery) {
                this.searchResults = this.estate.filter((place) =>
                    place.complexName.includes(this.searchQuery)
                );
                this.highlightSearchedMarkers();
            } else {
                this.clearHighlight();  // 검색어가 없으면 하이라이트를 초기화
            }
        },
        highlightSearchedMarkers() {
            this.markers.forEach((markerObj) => {
                const isSearchedPlace = this.searchResults.some((place) => place.address === markerObj.place.address);

                const markerImage = new kakao.maps.MarkerImage(
                    isSearchedPlace ? 'http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png' : null,
                    new kakao.maps.Size(24, 35)
                );
                markerObj.marker.setImage(markerImage);
            });
        },
        highlightMarker(place) {
            // 기존에 선택된 오버레이가 있으면 원래 흰색으로 복원
            if (this.selectedOverlay) {
                this.selectedOverlay.style.backgroundColor = 'white';
            }

            // 선택된 장소의 오버레이를 찾음
            const markerObj = this.markers.find((markerObj) => markerObj.place.address === place.address);
            if (markerObj) {
                console.log("asdasd", markerObj)
                this.map.setCenter(markerObj.marker.getPosition());

                // 오버레이의 배경을 노란색으로 변경
                const overlayContent = markerObj.customOverlay.getContent();
                if (overlayContent instanceof HTMLElement) {
                    overlayContent.style.backgroundColor = 'yellow';

                    // 선택된 오버레이로 저장
                    this.selectedOverlay = overlayContent;
                }
            }
        },
        clearHighlight() {
            if (this.selectedOverlay) {
                this.selectedOverlay.style.backgroundColor = 'white';
                this.selectedOverlay = null;
            }
        },
    },
};
</script>

<style scoped>
.map-container {
    display: flex;
    width: 100%;
    height: 100vh;
}

.search-panel {
    width: 300px;
    padding: 20px;
    background-color: white;
    overflow-y: auto;
    z-index: 1000;
}

.search-panel input {
    width: calc(100% - 20px);
    padding: 10px;
    margin-bottom: 10px;
}

.search-panel button {
    padding: 10px;
    width: 100%;
}

.result-list {
    margin-top: 20px;
}

.result-item {
    background-color: transparent;
    padding: 10px;
    margin: 10px 0;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
