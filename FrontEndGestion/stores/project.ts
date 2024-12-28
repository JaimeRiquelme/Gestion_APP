import { defineStore } from 'pinia';

export const useProjectStore = defineStore('project', {
 state: () => ({
   projectId: null as string | null,
   projectName: null as string | null,
 }),
 actions: {
   setProjectData(data: { projectId: string; projectName: string; organization: string }) {
     this.projectId = data.projectId;
     this.projectName = data.projectName; 
   },
   clearProjectData() {
     this.projectId = null;
     this.projectName = null;
   },
 },
 persist: [
   {
     pick: ['projectId', 'projectName', 'organization'],
     storage: localStorage,
   },
 ],
});