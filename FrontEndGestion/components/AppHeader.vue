<template>
    <v-header v-if="!isLandingPage && !isLoginPage && !isRegisterPage" class="appheader">
        <div class="header-content">
            <div class="logotype">
                <img src="assets/usach-logo.png" alt="USACH Logo" class="logo" />
            </div>
 
            <div class="nav-controls">
                <v-btn
                    variant="text"
                    color="white"
                    @click="goBack"
                    class="nav-btn"
                >
                    <v-icon start>mdi-arrow-left</v-icon>
                    Atrás
                </v-btn>
 
                <v-btn
                    variant="text"
                    color="white"
                    @click="goToMenu"
                    class="nav-btn"
                >
                    <v-icon start>mdi-menu</v-icon>
                    Menú Principal
                </v-btn>
 
                <v-menu
                    v-model="showUserMenu"
                    :close-on-content-click="false"
                    location="bottom end"
                    offset="10"
                >
                    <template v-slot:activator="{ props }">
                        <v-btn
                            color="white"
                            variant="text"
                            v-bind="props"
                            class="user-btn"
                        >
                            <v-avatar
                                color="white"
                                size="32"
                                class="mr-2"
                            >
                                <v-icon color="#00B8B0">mdi-account</v-icon>
                            </v-avatar>
                            <span class="username">{{ userName }}</span>
                            <v-icon>mdi-chevron-down</v-icon>
                        </v-btn>
                    </template>
 
                    <v-card min-width="200">
                        <v-list>
                            <v-list-item>
                                <v-list-item-title class="text-subtitle-2 font-weight-bold">
                                    {{ userName }}
                                </v-list-item-title>
                            </v-list-item>
                            <v-divider></v-divider>
                            <v-list-item @click="logout" class="logout-item">
                                <template v-slot:prepend>
                                    <v-icon color="error">mdi-logout</v-icon>
                                </template>
                                <v-list-item-title class="text-error">
                                    Cerrar Sesión
                                </v-list-item-title>
                            </v-list-item>
                        </v-list>
                    </v-card>
                </v-menu>
            </div>
        </div>
    </v-header>
 </template>
 
 <script setup>
 import { ref, computed } from 'vue';
 import { useRouter, useRoute } from 'vue-router';
 import { useAuthStore } from '../stores/auth';
 
 const router = useRouter();
 const route = useRoute();
 const authStore = useAuthStore();
 const showUserMenu = ref(false);
 const userName = computed(() => authStore.names);
 
 const isLandingPage = computed(() => route.path === '/');
 const isLoginPage = computed(() => route.path === '/login');
 const isRegisterPage = computed(() => route.path === '/register');
 
 const goBack = () => {
   if (route.path === '/wbs') {
      router.push('/principalView');
   } else {
      router.back();
   }
};
 
 const goToMenu = () => {
    router.push('/principalView');
 };
 
 const logout = () => {
    showUserMenu.value = false;
    authStore.clearAuthData();
    router.push('/login');
 };
 </script>
 
 <style scoped>
 .appheader {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;
    background-color: #00B8B0;
 }
 
 .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 2rem;
    height: 100px;
 }
 
 .logotype {
    display: flex;
    align-items: center;
 }
 
 .logo {
    height: 100px;
    width: auto;
 }
 
 .nav-controls {
    display: flex;
    align-items: center;
    gap: 8px;
 }
 
 .nav-btn {
    margin: 0 4px;
    text-transform: none !important;
    letter-spacing: normal;
    height: 36px;
 }
 
 .user-btn {
    margin-left: 4px;
    text-transform: none !important;
    letter-spacing: normal;
    height: 36px;
 }
 
 .username {
    margin: 0 4px;
    color: white;
 }
 
 .logout-item {
    cursor: pointer;
 }
 
 .logout-item:hover {
    background-color: rgb(var(--v-theme-error), 0.1);
 }
 
 @media (max-width: 768px) {
    .header-content {
        padding: 0 1rem;
        height: 80px;
    }
 
    .logo {
        height: 80px;
    }
 
    .nav-btn :deep(.v-btn__content) span {
        display: none;
    }
 
    .username {
        display: none;
    }
 
    .nav-controls {
        gap: 4px;
    }
 }
 </style>