import { defineStore } from 'pinia';

export const useConstitutionFormStore = defineStore('ConstitutionForm', {
    state: () => ({
        ContitutionCreation: false as boolean | null,
    }),
    actions: {
        setConstitutionFormData(data: { ContitutionCreation: boolean }) {
            this.ContitutionCreation = data.ContitutionCreation;
        },
        clearConstitutionData() {
            this.ContitutionCreation = false;
            this.$reset();
        },
    },
    persist: [
        {
            pick: ['ContitutionCreation'],
            storage: localStorage,
        },
    ],
});

