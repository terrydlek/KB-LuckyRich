<template>
    <div class="map-container">
        <div v-if="isLoading" class="loading-overlay">
            <div class="loading-spinner">
                <div class="spinner-border text-primary" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
                <p>Loading...</p>
            </div>
        </div>

        <div class="search-panel">
            <input v-model="searchQuery" placeholder="단지명을 입력하세요" />
            <button @click="searchPlaces">검색</button>

            <div v-if="searchResults.length > 0" class="result-list">
                <div v-for="(place, index) in searchResults" :key="index" class="result-item">
                    <p>단지명: {{ place.complexName }}</p>
                    <p>주소: {{ place.address }}</p>
                    <p>거래가: {{ place.price }}억</p>
                    <button @click="highlightMarker(place)">지도에서 보기</button>
                </div>
            </div>
        </div>

        <div id="map"></div>

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
            selectedPlace: null,
            selectedOverlay: null,
            searchQuery: '',
            searchResults: [],
            markers: [],
            estate: [],
            clusterer: null, 
            isLoading: true,
        };
    },
    mounted() {
        this.loadKakaoMap();
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
            script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${kakaoApiKey}&libraries=services,clusterer`; 
            document.head.appendChild(script);
        }
    },
        fetchRealEstateData() {
            const token = localStorage.getItem('access_token');
            axios.get('http://localhost:8080/realestate/getEstate', {
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
        initKakaoMap() {
        const mapContainer = document.getElementById('map');
        const mapOptions = {
            center: new kakao.maps.LatLng(37.5, 126.9),
            level: 7,
        };
        const map = new kakao.maps.Map(mapContainer, mapOptions);  // 지역변수 map 사용

        this.clusterer = new kakao.maps.MarkerClusterer({
            map: map,
            averageCenter: true,
            minLevel: 4,
        });

        // clusterer가 초기화된 후 데이터 로드
        this.fetchRealEstateData();  // initKakaoMap 후에 호출

        kakao.maps.event.addListener(map, 'zoom_changed', this.handleZoomChange);
    },

        handleZoomChange() {
            if (!this.map) return;

            const level = this.map.getLevel();
            if (level <= 3) {
                this.showMarkers();
            } else {
                this.hideMarkers();
            }

            // Ensure the map layout is recalculated on zoom change
            this.map.relayout();
        },

        showMarkers() {
            this.markers.forEach(markerObj => {
                if (markerObj && markerObj.customOverlay) {
                    markerObj.customOverlay.setMap(this.map);
                    markerObj.isVisible = true;
                }
            });
        },

        hideMarkers() {
            this.markers.forEach(markerObj => {
                if (markerObj && markerObj.customOverlay) {
                    markerObj.customOverlay.setMap(null);
                    markerObj.isVisible = false;
                }
            });
        },
        
        createMarkers() {
    const markerInfo = {};  
    let createdMarkers = 0;  
    const markers = [];

    this.estate.forEach((place) => {
        this.getCoordinatesFromAddress(place.address, (coords) => {
            if (coords) {
                const addressKey = `${place.address}`;

                if (markerInfo[addressKey]) {
                    const existingOverlayContent = markerInfo[addressKey].overlayContent;
                    existingOverlayContent.innerHTML += `<div>${place.floor}층: ${place.price}억</div>`;
                } else {
                    const markerPosition = new kakao.maps.LatLng(coords.lat, coords.lng);
                    const marker = new kakao.maps.Marker({
                        position: markerPosition,
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

                    kakao.maps.event.addListener(marker, 'click', () => {
                        this.openModal({ ...place, ...coords });
                    });

                    markers.push(marker);

                    markerInfo[addressKey] = {
                        marker,
                        overlayContent,
                        customOverlay
                    };

                    this.markers.push({
                        marker: markerInfo[addressKey].marker,
                        place,
                        customOverlay: markerInfo[addressKey].customOverlay,
                        isVisible: false
                    });
                }
            }

            createdMarkers++;
            if (createdMarkers === this.estate.length) {
                this.isLoading = false;
                if (this.clusterer) {  // clusterer가 null이 아닌지 확인
                    this.clusterer.addMarkers(markers);
                } else {
                    console.error('clusterer is not initialized.');
                }
            }
        });
    });
},

        getCoordinatesFromAddress(address, callback) {
            const geocoder = new kakao.maps.services.Geocoder();
            geocoder.addressSearch(address, (result, status) => {
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
        },

        searchPlaces() {
            if (this.searchQuery) {
                this.searchResults = this.estate.filter((place) =>
                    place.complexName.includes(this.searchQuery)
                );
                this.highlightSearchedMarkers();
            } else {
                this.clearHighlight();
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
            if (this.selectedOverlay) {
                this.selectedOverlay.style.backgroundColor = 'white';
            }

            const markerObj = this.markers.find((markerObj) => markerObj.place.address === place.address);
            if (markerObj) {
                this.map.setCenter(markerObj.marker.getPosition());

                const overlayContent = markerObj.customOverlay.getContent();
                if (overlayContent instanceof HTMLElement) {
                    overlayContent.style.backgroundColor = 'yellow';

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
