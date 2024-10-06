import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
    state() {
        return {
            assetData: {},      // 자산 증감추이
            accountData: [],    // accountBookChart
            totalAssets: {},     // totalChart
            theirAssets: {},    // 자산 비교 차트

        };
    },
    mutations: {
        setAssetData(state, payload) {
            state.assetData = payload;
        },
        setAccountData(state, payload) {
            state.accountData = payload;
        },
        setTotalAssets(state, payload) {
            state.totalAssets = payload;
        },
        setTheirAssets(state, payload) {
            state.theirAssets = payload;
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
        },
        async fetchAccountData({ commit }) {
            const token = localStorage.getItem('access_token');
            try {
                const response = await axios.get('http://localhost:8080/myasset/accounts', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                commit('setAccountData', response.data);  // Commit account data
            } catch (error) {
                console.error('Error fetching account data:', error);
            }
        },
        async fetchTotalAssets({ commit }) {
            const token = localStorage.getItem('access_token');
            try {
                const response = await axios.get('http://localhost:8080/myasset/total', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                commit('setTotalAssets', response.data);
            } catch (error) {
                console.error('Error fetching total assets data:', error);
            }
        },
        async fetchTheirAssets({ commit }) {
            const token = localStorage.getItem('access_token');
            try {
                const response = await axios.get('http://localhost:8080/myasset/their-assets', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                commit('setTheirAssets', response.data);
            } catch (error) {
                console.error('Error fetching their assets data:', error);
            }
        },
    },
    getters: {
        getAssetData(state) {
            return state.assetData;
        },
        getAccountData(state) {
            return state.accountData;
        },
        getTotalAssets(state) {
            return state.totalAssets;
        },
        getTheirAssets(state) {
            return state.theirAssets;
        }
    }
});
