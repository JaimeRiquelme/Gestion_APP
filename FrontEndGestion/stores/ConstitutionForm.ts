import { defineStore } from 'pinia';

export const useConstitutionFormStore = defineStore('ConstitutionForm', {
    state: () => ({
        ContitutionCreation: false as boolean | null,
    }),
    actions: {
        setConstitutionFormData(data: { ContitutionCreation: boolean }) {
            this.ContitutionCreation = data.ContitutionCreation;
        },
        clearConstitutionFormData() {
            this.ContitutionCreation = null;
        },
    },
    persist: [
        {
            pick: ['ContitutionCreation'],
            storage: localStorage,
        },
    ],
});

