import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
    state() {
        return {
            assetData: {}
        };
    },
    mutations: {
        setAssetData(state, payload) {
            state.assetData = payload;
        }
    },
    actions: {
        async fetchAssetData({ commit }) {
            const token = localStorage.getItem('access_token');
            try {
                const response = await axios.get('http://localhost:8080/myasset/idTrend', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                commit('setAssetData', response.data);
            } catch (error) {
                console.error('Error fetching asset data:', error);
            }
        }
    },
    getters: {
        getAssetData(state) {
            return state.assetData;
        }
    }
});
