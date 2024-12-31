import { defineStore } from 'pinia';

export const useManagementsStore = defineStore('managements', {
    state: () => ({
        managementId: null as string | null,
        managementName: null as string | null,
    }),
    actions: {
        setManagementData(data: { managementId: string; managementName: string }) {
            this.managementId = data.managementId;
            this.managementName = data.managementName;
        },
        clearManagementData() {
            this.managementId = null;
            this.managementName = null;
        },
    },
    persist: [
        {
            pick: ['managementId', 'managementName'],
            storage: localStorage,
        },
    ],
});