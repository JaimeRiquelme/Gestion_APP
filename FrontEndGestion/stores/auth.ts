import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null as string | null,
    userId: null as string | null,
    names: null as string | null,
  }),
  actions: {
    setAuthData(data: { token: string; userId: string; names: string }) {
      this.token = data.token;
      this.userId = data.userId;
      this.names = data.names;
    },
    clearAuthData() {
      this.token = null;
      this.userId = null;
      this.names = null;
    },
  },
  persist: [
    {
      pick: ['token', 'userId', 'names'], // Propiedades que quieres guardar
      storage: localStorage, // O usa sessionStorage según lo que necesites
    },
  ],
});
