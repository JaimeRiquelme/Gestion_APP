import { defineStore } from 'pinia'


export const useProcessStore = defineStore('process', {
    state: () => ({
        processId: null as string | null,
        processName: null as string | null,
    }),
    actions: {
        setProcessData(data: { processId: string; processName: string }) {
            this.processId = data.processId;
            this.processName = data.processName;
        },
        clearProcessData() {
            this.processId = null;
            this.processName = null;
        },
    },
    persist: [
        {
            pick: ['processId', 'processName'],
            storage: localStorage,
        },
    ],
});