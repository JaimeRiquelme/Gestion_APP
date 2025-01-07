<template>
    <div class="container">
      <!-- Barra Superior -->
      <header class="header">
        <div class="logo-container">
          <div class="logo">LOGO</div>
          <h1 class="title">Gestión de Documentos</h1>
        </div>
      </header>
  
      <!-- Contenido Principal -->
      <main class="main-content">
        <!-- Contenedor de PDFs -->
        <div class="pdf-container">
          <div class="pdf-wrapper">
            <span class="pdf-label top-left">Última EDT aprobada</span>
            <iframe
              v-if="pdfBlobs[0]"
              :src="pdfBlobs[0]"
              class="pdf-viewer"
              frameborder="0"
            ></iframe>
          </div>
  
          <div class="pdf-wrapper">
            <span class="pdf-label top-right">EDT actual</span>
            <iframe
              v-if="pdfBlobs[1]"
              :src="pdfBlobs[1]"
              class="pdf-viewer"
              frameborder="0"
            ></iframe>
          </div>
  
          <div class="pdf-wrapper">
            <span class="pdf-label bottom-left">Último enunciado del alcance del proyecto</span>
            <iframe
              v-if="pdfBlobs[2]"
              :src="pdfBlobs[2]"
              class="pdf-viewer"
              frameborder="0"
            ></iframe>
          </div>
  
          <div class="pdf-wrapper">
            <span class="pdf-label bottom-right">Enunciado del alcance actual</span>
            <iframe
              v-if="pdfBlobs[3]"
              :src="pdfBlobs[3]"
              class="pdf-viewer"
              frameborder="0"
            ></iframe>
          </div>
        </div>
  
        <!-- Botón para recargar documentos -->
        <button class="action-button" @click="createExit">
          Autorizar Documento
        </button>
      </main>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useAuthStore } from '../stores/auth';
  import axios from 'axios';
  
  const AuthStore = useAuthStore();
  const pdfBlobs = ref([]);
  const pdfIds = ref([1, 2, 3, 5]);
  
  async function fetchPdfs() {
    try {
      const token = AuthStore.token;
      if (!token) {
        alert('ALERTA: ¡Sesión no iniciada! Redirigiendo a login...');
        await router.push('/login');
        return;
      }
  
      const pdfPromises = pdfIds.value.map((id) =>
        axios.get(`http://localhost:8080/api/v1/exit/${id}/document`, {
          headers: { Authorization: `Bearer ${token}` },
          responseType: 'blob',
        })
      );
  
      const responses = await Promise.all(pdfPromises);
  
      pdfBlobs.value = responses.map((response) =>
        URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }))
      );
    } catch (error) {
      console.error('Error al cargar los documentos:', error);
      alert('Hubo un problema al cargar los documentos.');
    }
  }
  
  async function createExit() {
    try {
      const token = AuthStore.token;
  
      if (!pdfBlobs.value[1]) {
        alert('El PDF no está cargado.');
        return;
      }
  
      const response = await axios.post(
        'http://localhost:8080/api/v1/exit/create',
        {
          document: pdfBlobs.value[1], // Ajusta según el formato esperado
          state: '1',
          nameExit: 'Nombre del Exit',
          description: 'Descripción de prueba',
          priority: 'Alta',
          responsible: 'Usuario Responsable',
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
  
      alert('Exit creado exitosamente');
    } catch (error) {
      console.error('Error al crear el Exit:', error);
      alert('Hubo un problema al crear el Exit.');
    }
  }
  
  onMounted(() => {
    fetchPdfs();
  });
  </script>
  
  <style scoped>
  /* Estilo General */
  html, body {
    height: 100%;
    margin: 0;
    font-family: Arial, sans-serif;
  }
  
  .container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    background-color: white;
    color: black;
  }
  
  /* Barra Superior */
  .header {
    background-color: #00B8B0;
    padding: 20px;
  }
  
  .logo-container {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .logo {
    width: 40px;
    height: 40px;
    background-color: white;
    color: #00B8B0;
    font-weight: bold;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 22px;
  }
  
  .title {
    color: white;
    font-size: 30px;
    font-weight: bold;
  }
  
  /* Contenido Principal */
  .main-content {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 20px;
    text-align: center;
  }
  
  /* Contenedor de PDFs */
  .pdf-container {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    max-width: 1200px;
    width: 100%;
    margin-bottom: 20px;
  }
  
  /* Estilo del contenedor y los PDF */
  .pdf-wrapper {
    position: relative;
    height: 300px;
    overflow: hidden;
    padding-top: 40px; /* Espacio para las etiquetas de texto */
  }
  
  .pdf-viewer {
    width: 100%;
    height: 100%;
    border: 1px solid #ddd;
    border-radius: 8px;
    object-fit: contain;
    z-index: 0; /* Aseguramos que el iframe quede en el fondo */
  }
  
  /* Texto sobre los PDFs (fuera de los iframes) */
  .pdf-label {
    position: absolute;
    font-size: 18px;
    font-weight: bold;
    color: black;
    background-color: rgba(255, 255, 255, 0.8); /* Fondo semi-transparente */
    padding: 5px;
    border-radius: 4px;
    z-index: 1; /* Aseguramos que el texto esté por encima del iframe */
  }
  
  .top-left {
    top: 0; /* Ubicación del texto arriba a la izquierda */
    left: 10px;
  }
  
  .top-right {
    top: 0;
    right: 10px;
  }
  
  .bottom-left {
    bottom: 0; /* Ubicación del texto abajo a la izquierda */
    left: 10px;
  }
  
  .bottom-right {
    bottom: 0;
    right: 10px;
  }
  
  /* Botón debajo */
  .action-button {
    background-color: #00B8B0;
    color: white;
    padding: 15px 30px;
    border-radius: 30px;
    font-size: 18px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    border: none;
  }
  
  .action-button:hover {
    background-color: #009990;
  }
  </style>
  