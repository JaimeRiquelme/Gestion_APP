<template>
  <div class="container">
    <header class="header">
      <div class="logo-container">
        <div class="logo">USACH</div>
        <h1 class="title">USACH</h1>
      </div>
    </header>

    <main class="main-content">
      <div class="project-info">
        <!-- Mostrar el texto con el nombre del proyecto -->
        <p>Áreas de gestión del proyecto: <strong>{{ projectName }}</strong></p>
      </div>

      <div class="button-container">
        <button @click="goToPage('/project-management')" class="button-square">
          <span class="button-number">1.</span> Gestión de la Integracion
        </button>

        <button @click="goToPage('/another-page')" class="button-square">
          <span class="button-number">2.</span> Gestión del Alcance
        </button>

        <button @click="handleBackendRequest(3)" class="button-square">
          <span class="button-number">3.</span> Gestión del Cronograma
        </button>
        <button @click="handleBackendRequest(4)" class="button-square">
          <span class="button-number">4.</span> Gestión de los Costos
        </button>
        <button @click="handleBackendRequest(5)" class="button-square">
          <span class="button-number">5.</span> Gestión de la Calidad
        </button>
        <button @click="handleBackendRequest(6)" class="button-square">
          <span class="button-number">6.</span> Gestión de las Comunicaciones
        </button>
        <button @click="handleBackendRequest(7)" class="button-square">
          <span class="button-number">7.</span> Gestión de los Riesgos
        </button>
        <button @click="handleBackendRequest(8)" class="button-square">
          <span class="button-number">8.</span> Gestión de las Adquisiciones
        </button>
        <button @click="handleBackendRequest(9)" class="button-square">
          <span class="button-number">9.</span> Gestión de
        </button>
        <button @click="handleBackendRequest(10)" class="button-square">
          <span class="button-number">10.</span> Gestión de
        </button>
        <button @click="handleBackendRequest(11)" class="button-square">
          <span class="button-number">11.</span> Gestión de
        </button>
      </div>
    </main>

    <footer class="footer">
      <p>&copy; 2024 USACH. Todos los derechos reservados.</p>
    </footer>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import axios from 'axios'; // Se importa Axios para la comunicación con el backend

const router = useRouter();

// Variable reactiva para almacenar el nombre del proyecto
let projectName = 'Cargando...'; // Valor inicial mientras se obtiene desde el backend

// Esta variable simula el valor recibido desde otra vista (puede pasarse como parámetro en `router.push`).
let receivedValue = 1; // Cambiar esto con el valor real recibido desde otra vista

function goToPage(path) {
  router.push(path);
}

// Nueva función para manejar la solicitud GET al backend
async function handleBackendRequest(sectionId) {
  try {
    // Realiza una solicitud GET al backend, pasando el ID de la sección en la URL
    const response = await axios.get(`http://localhost:8090/api/v1/proyect/getById/${sectionId}`);

    if (response.data) {
      // Accede al nombre del proyecto (nameProyect) desde la respuesta
      projectName = response.data.nameProyect; // Asigna el nombre del proyecto a la propiedad reactiva
      console.log('Nombre del proyecto:', projectName);
      alert(`Proyecto: ${projectName} (Sección ${sectionId})`);
    } else {
      console.log('No se encontró el proyecto');
    }
  } catch (error) {
    console.error('Error al comunicarse con el backend:', error);
    alert('Hubo un error al obtener la información del proyecto');
  }
}
</script>

<style scoped>
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
}

.header {
  background-color: white;
  padding: 20px;
  border-bottom: 1px solid #ddd;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo {
  width: 40px;
  height: 40px;
  background-color: #00b8b0;
  color: white;
  font-weight: bold;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
}

.title {
  color: #333;
  font-size: 30px;
  font-weight: bold;
}

.main-content {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 40px;
  background-color: white;
}

.project-info {
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: bold;
  color: #000; /* Asegurando que el texto sea negro */
}

.button-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  width: 70%;
}

.button-square {
  background-color: #d3d3d3;
  color: #333;
  border: none;
  padding: 40px;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
  border-radius: 8px;
  text-align: center;
  width: 100%;
  position: relative;
  min-width: 200px;
}

.button-square:hover {
  background-color: #a9a9a9;
}

.button-number {
  position: absolute;
  top: 10px;
  left: 10px;
  font-size: 22px;
  font-weight: bold;
  color: #333;
}

.footer {
  background-color: #00b8b0;
  color: white;
  text-align: center;
  padding: 20px;
  font-size: 18px;
}
</style>
