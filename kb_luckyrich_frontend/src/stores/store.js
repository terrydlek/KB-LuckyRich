import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    name: '',
    email: ''
  }),
  actions: {
    setUser(user) {
      this.name = user.name;
      this.email = user.email;
    }
  }
});
