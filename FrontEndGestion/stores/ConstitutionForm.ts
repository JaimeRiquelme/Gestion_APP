import { defineStore } from 'pinia';

export const useConstitutionFormStore = defineStore('ConstitutionForm', {
    state: () => ({
        idManagement: null as string | null,
        idProcess: null as string | null,
        idExit: null as string | null,
    }),
    actions: {
        setConstitutionFormData(data: { idManagement: string; idProcess: string; idExit: string }) {
            this.idManagement = data.idManagement;
            this.idProcess = data.idProcess;
            this.idExit = data.idExit;
        },
        clearConstitutionFormData() {
            this.idManagement = null;
            this.idProcess = null;
            this.idExit = null;
        },
    },
    persist: [
        {
            pick: ['idManagement', 'idProcess', 'idExit'],
            storage: localStorage,
        },
    ],
});

