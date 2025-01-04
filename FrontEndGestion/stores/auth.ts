import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null as string | null,
    userId: null as string | null,
    names: null as string | null,
    refreshToken: null as string | null,
  }),
  actions: {
    setAuthData(data: { token: string; userId: string; names: string , refreshToken: string }) {
      this.token = data.token;
      this.userId = data.userId;
      this.names = data.names;
      this.refreshToken = data.refreshToken;
    },
    clearAuthData() {
      this.token = null;
      this.userId = null;
      this.names = null;
      this.refreshToken = null;
    },
  },
  persist: [
    {
      pick: ['token', 'userId', 'names','refreshToken'], 
      storage: localStorage, 
    },
  ],
});
