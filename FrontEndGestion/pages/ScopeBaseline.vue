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
        <div
          v-for="(document, index) in documents"
          :key="index"
          class="pdf-wrapper"
        >
          <span class="pdf-label">{{ document.label }}</span>
          <div v-if="pdfBlobs[index]">
            <iframe
              :src="pdfBlobs[index]"
              class="pdf-viewer"
              frameborder="0"
            ></iframe>
          </div>
          <div v-else>
            <p>Documento no disponible</p>
          </div>
        </div>
      </div>

      <!-- Mostrar documentos faltantes -->
      <div v-if="missingDocuments.length > 0" class="missing-documents">
        <h3>Documentos Faltantes:</h3>
        <ul>
          <li v-for="doc in missingDocuments" :key="doc.label">{{ doc.label }} no se encontró.</li>
        </ul>
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
import { useProcessStore } from '../stores/Process';

const ProcessStore = useProcessStore();
const AuthStore = useAuthStore();
const pdfBlobs = ref([]);

// Lista de documentos con sus respectivos valores de nameExit y state
const documents = ref([
  { label: 'Última EDT aprobada', nameExit: 'Línea Base del Alcance', state: '1' },
  { label: 'EDT actual', nameExit: 'Línea Base del Alcance', state: '0' },
  { label: 'Último enunciado del alcance del proyecto', nameExit: 'Enunciado del alcance', state: '1' },
  { label: 'Enunciado del alcance actual', nameExit: 'Enunciado del alcance', state: '0' },
]);

// Lista para manejar los documentos que no se encontraron
const missingDocuments = ref([]);

async function fetchPdfs() {
  try {
    const token = AuthStore.token;
    if (!token) {
      alert('ALERTA: ¡Sesión no iniciada! Redirigiendo a login...');
      await router.push('/login');
      return;
    }

    // Hacer la solicitud de PDFs de manera asíncrona para cada documento
    const pdfPromises = documents.value.map(async (doc) => {
      try {
        const response = await axios.get('http://localhost:8080/api/v1/exit/ByNameAndState', {
          params: {
            nameExit: doc.nameExit,
            state: doc.state,
          },
          headers: { Authorization: `Bearer ${token}` },
          responseType: 'blob',
        });

        // Si el PDF se encuentra, se agrega a pdfBlobs
        return {
          label: doc.label,
          pdfUrl: URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' })),
          found: true,
        };
      } catch (error) {
        // Si ocurre un error (por ejemplo, el documento no se encuentra), agregar a missingDocuments
        console.error(`Error al cargar el PDF de ${doc.label}:`, error);
        return {
          label: doc.label,
          pdfUrl: null,
          found: false,
        };
      }
    });

    // Esperar a que todas las promesas se resuelvan
    const results = await Promise.all(pdfPromises);

    // Filtrar los documentos encontrados y los que faltan
    pdfBlobs.value = results.filter(result => result.found).map(result => result.pdfUrl);
    missingDocuments.value = results.filter(result => !result.found);

  } catch (error) {
    console.error('Error al cargar los documentos:', error);
    alert('Hubo un problema al cargar los documentos.');
  }
}

// Llamar a la función cuando el componente se monte
onMounted(fetchPdfs);

// Función para autorizar el documento
async function createExit() {
  try {
    const token = AuthStore.token;

    if (!pdfBlobs.value[1]) {
      alert('El PDF no está cargado.');
      return;
    }

    // Asegúrate de enviar el cuerpo correcto con todos los parámetros
    const response = await axios.post(
      'http://localhost:8080/api/v1/exit/create',
      {
        document: pdfBlobs.value[1], // Ajusta según el formato esperado
        state: '1', // Estado 1
        nameExit: 'Línea Base del Alcance', // Puedes ajustar esto dinámicamente
        description: 'Descripción de prueba', // Descripción proporcionada
        priority: 'Alta', // Prioridad
        responsible: 'Usuario Responsable', // Responsable
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (response.status === 201) {
      alert('Exit creado exitosamente');
    } else {
      alert('Hubo un problema al crear el Exit.');
    }
  } catch (error) {
    console.error('Error al crear el Exit:', error);
    alert('Hubo un problema al crear el Exit.');
  }
}
</script>

<style scoped>
/* Estilos generales */
html,
body {
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
  background-color: #00b8b0;
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
  color: #00b8b0;
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

.pdf-wrapper {
  position: relative;
  height: 300px;
  overflow: hidden;
  padding-top: 40px;
}

.pdf-viewer {
  width: 100%;
  height: 100%;
  border: 1px solid #ddd;
  border-radius: 8px;
  object-fit: contain;
  z-index: 0;
}

.pdf-label {
  position: absolute;
  font-size: 18px;
  font-weight: bold;
  color: black;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 5px;
  border-radius: 4px;
  z-index: 1;
}

.action-button {
  background-color: #00b8b0;
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

/* Estilo para los documentos faltantes */
.missing-documents {
  margin-top: 20px;
  text-align: left;
  font-size: 16px;
  color: red;
}
</style>
