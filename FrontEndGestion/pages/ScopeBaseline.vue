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
const url_exit = "http://localhost:8080/api/v1/exit"

// Lista de documentos con sus respectivos valores de nameExit y state
const documents = ref([
  { label: 'Última EDT aprobada', nameExit: 'prueba1', state: '1' },
  { label: 'EDT actual', nameExit: 'prueba1', state: '0' },
  { label: 'Último enunciado del alcance del proyecto', nameExit: 'Línea Base del Alcance', state: '0' },
  { label: 'Enunciado del alcance actual', nameExit: 'Línea Base del Alcance', state: '1' },
]);

// Lista para manejar los documentos que no se encontraron
const missingDocuments = ref([]);
const pdfBlobs = ref([]); // Asegúrate de que esta lista exista para almacenar las URLs de los PDFs

async function fetchPdfs() {
  try {
    const token = AuthStore.token;

    // Verificar si el usuario tiene sesión iniciada
    if (!token) {
      alert('ALERTA: ¡Sesión no iniciada! Redirigiendo a login...');
      await router.push('/login');
      return;
    }

    // Crear solicitudes de PDFs de manera asíncrona para cada documento
    const pdfPromises = documents.value.map(async (doc) => {
      try {
        const response = await axios.get(`http://localhost:8080/api/v1/exit/ByNameAndState`, {
          params: {
            nameExit: doc.nameExit,
            state: doc.state,
          },
          headers: { Authorization: `Bearer ${token}` },
          responseType: 'blob', // Importante para manejar archivos binarios
        });

        // Verificar que la respuesta sea un PDF
        if (response.data.type !== 'application/pdf') {
          throw new Error('El archivo recibido no es un PDF válido');
        }

        // Crear URL para el Blob
        const blob = new Blob([response.data], { type: 'application/pdf' });
        const pdfUrl = URL.createObjectURL(blob);

        return {
          label: doc.label,
          pdfUrl: pdfUrl,
          found: true,
        };
      } catch (error) {
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

    // Filtrar y asignar los resultados
    pdfBlobs.value = results
      .filter(result => result.found) // Solo los encontrados
      .map(result => result.pdfUrl);

    missingDocuments.value = results
      .filter(result => !result.found) // Solo los no encontrados
      .map(result => ({
        label: result.label,
      }));

    // Mensaje opcional si hay documentos faltantes
    if (missingDocuments.value.length > 0) {
      console.warn('Documentos faltantes:', missingDocuments.value);
    }

  } catch (error) {
    console.error('Error al cargar los documentos:', error);
    alert('Hubo un problema al cargar los documentos. Intenta nuevamente.');
  }
}



// Llamar a la función cuando el componente se monte
onMounted(fetchPdfs);

// Función para autorizar el documento
async function createExit() {
  try {
    if (!pdfBlobs.value[0]) {
      alert('El PDF no está cargado.');
      return;
    }

    const reader = new FileReader();

    // Leer el archivo como ArrayBuffer
    reader.readAsArrayBuffer(pdfBlobs.value[0]);

    reader.onload = async () => {
      const arrayBuffer = reader.result;

      // Convertir el ArrayBuffer a un array de bytes
      const documentBytes = Array.from(new Uint8Array(arrayBuffer));

      // Realizar la solicitud al backend
      const createResponse = await fetch(url_exit + '/create', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${AuthStore.token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          idProcess: 1, // ProcessStore.processId
          nameExit: 'EDT',
          state: '1',
          dateCreation: new Date().toISOString().split('T')[0],
          dateValidation: new Date().toISOString().split('T')[0],
          priority: 'Media',
          documents: documentBytes, // Enviar como array de bytes
          responsible: 'place holder',
          description:
            'La EDT (Estructura de Desglose del Trabajo) es una herramienta comúnmente ocupada en la Gestión del Alcance',
        }),
      });

      const responseText = await createResponse.text();
      if (!responseText.trim()) {
        throw new Error('No se recibió ningún contenido JSON');
      }

      if (!createResponse.ok) {
        throw new Error(`Error al crear la salida: ${responseText}`);
      }

      alert('Exit creado exitosamente');
    };

    reader.onerror = () => {
      console.error('Error al leer el archivo:', reader.error);
      alert('Hubo un problema al procesar el archivo.');
    };
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
