import { defineStore } from 'pinia'

export const useExitStore = defineStore('Exit', {
    state: () => ({
        exitId: null as string | null,
        exitName: null as string | null,
    }),
    actions: {
        setExitData(data: { exitId: string; exitName: string }) {
            this.exitId = data.exitId;
            this.exitName = data.exitName;
        },
        clearExitData() {
            this.exitId = null;
            this.exitName = null;
        },
    },
    persist: [
        {
            pick: ['exitId', 'exitName'],
            storage: localStorage,
        },
    ],
});