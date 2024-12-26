<template>
    <v-header class="appheader">
        <div class="logotype">
            <img src="assets/usach-logo.png" alt="USACH Logo" class="logo" />
        </div>
    </v-header>
</template>

<script>
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

.logotype {
    display: flex;
}

.logo {
    height: 100px;
    width: auto;
}

</style>